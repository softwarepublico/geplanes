/* 
		Copyright 2007,2008,2009,2010 da Linkcom Informática Ltda
		
		Este arquivo é parte do programa GEPLANES.
 	   
 	    O GEPLANES é software livre; você pode redistribuí-lo e/ou 
		modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 	    publicada pela Free Software Foundation; tanto a versão 2 da 
		Licença como (a seu critério) qualquer versão mais nova.
 	
 	    Este programa é distribuído na expectativa de ser útil, mas SEM 
		QUALQUER GARANTIA; sem mesmo a garantia implícita de 
		COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM PARTICULAR. 
		Consulte a Licença Pública Geral GNU para obter mais detalhes.
 	 
 	    Você deve ter recebido uma cópia da Licença Pública Geral GNU  	    
		junto com este programa; se não, escreva para a Free Software 
		Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
		02111-1307, USA.
		
*/
package br.com.linkcom.sgm.controller.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Command;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.Comentario;
import br.com.linkcom.sgm.beans.ComentarioItem;
import br.com.linkcom.sgm.beans.SolicitacaoCancelamentoIndicador;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.enumeration.AprovacaoEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.controller.filtro.SolicitacaoCancelamentoIndicadorFiltro;
import br.com.linkcom.sgm.service.ComentarioItemService;
import br.com.linkcom.sgm.service.ComentarioService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.SolicitacaoCancelamentoIndicadorService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;

@Bean
@Controller(path="/sgm/process/SolicitacaoCancelamentoIndicador", authorizationModule=ProcessAuthorizationModule.class)
public class SolicitacaoCancelamentoIndicadorProcess extends MultiActionController {

	private IndicadorService indicadorService;
	private SolicitacaoCancelamentoIndicadorService solicitacaoCancelamentoIndicadorService;
	private UsuarioService usuarioService;
	private ComentarioService comentarioService;
	private ComentarioItemService comentarioItemService;	
	
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	public void setSolicitacaoCancelamentoIndicadorService(SolicitacaoCancelamentoIndicadorService solicitacaoCancelamentoIndicadorService) {this.solicitacaoCancelamentoIndicadorService = solicitacaoCancelamentoIndicadorService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setComentarioService(ComentarioService comentarioService) {this.comentarioService = comentarioService;}
	public void setComentarioItemService(ComentarioItemService comentarioItemService) {this.comentarioItemService = comentarioItemService;}	
	
	@DefaultAction
	@Action("executar")
    public ModelAndView executar(WebRequestContext request, SolicitacaoCancelamentoIndicadorFiltro filtro) {
		
		request.setLastAction("executar");
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}		
		
		if (filtro.getPlanoGestao() != null) {
			
			/*** insere informações no request ***/
			request.setAttribute("isAdmin", usuarioService.isUsuarioLogadoAdmin());
					
			filtro.setListaSolicitacaoCancelamentoIndicador(solicitacaoCancelamentoIndicadorService.findSolicitacoes(filtro));
		}
		
		return new ModelAndView("process/solicitacaoCancelamentoIndicador", "filtro", filtro);
	}
	
	@Command(validate=true)
	public ModelAndView salvar(WebRequestContext request, SolicitacaoCancelamentoIndicadorFiltro bean) {
		StatusIndicadorEnum statusIndicador;
		Integer id_indicador = null;
		List<SolicitacaoCancelamentoIndicador> listaSolicitacoesCancelamentoIndicador = bean.getListaSolicitacaoCancelamentoIndicador();
		
		for (SolicitacaoCancelamentoIndicador solicitacaoCancelamento : listaSolicitacoesCancelamentoIndicador) {
			if (!solicitacaoCancelamento.getId().equals(bean.getId())) {
				continue;
			}
			
			if (bean.getAprovar()) {
				statusIndicador = StatusIndicadorEnum.CANCELADO;
				solicitacaoCancelamento.setStatus(AprovacaoEnum.APROVADO);
				id_indicador = solicitacaoCancelamento.getIndicador().getId();
			} 
			else {
				statusIndicador = StatusIndicadorEnum.APROVADO;
				solicitacaoCancelamento.setStatus(AprovacaoEnum.REPROVADO);
			}
			
			solicitacaoCancelamentoIndicadorService.saveOrUpdateStatus(solicitacaoCancelamento);
			indicadorService.atualizaStatusIndicador(solicitacaoCancelamento.getIndicador(), statusIndicador);
			
			try {
				// Envia um email para o solicitante informando sobre a aprovação ou reprovação da solicitação.
				usuarioService.enviaEmailRespostaSolicitacaoCancelamentoIndicador(solicitacaoCancelamento.getIndicador(), solicitacaoCancelamento.getSolicitante(), solicitacaoCancelamento.getJustificativaRes(), bean.getAprovar() ? "APROVADA" : "REPROVADA");			
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}
		}		
		
		bean.setId(null);
		bean.setAprovar(null);
		
		if(id_indicador != null){
			request.addMessage("Solicitação de cancelamento de indicador aprovada com sucesso.");
			request.addMessage("Favor conferir a soma dos pesos dos indicadores.");			
			return new ModelAndView("redirect:/sgm/process/DistribuicaoPesosIndicadores?id_indicador=" + id_indicador);
		} else {
			request.addMessage("Solicitação de cancelamento de indicador reprovada com sucesso.");			
			return executar(request, bean);
		}
		
	}
	
	@Override
	@Input("executar")
	protected void validate(Object obj, BindException errors, String acao) {
		if(!usuarioService.isUsuarioLogadoAdmin())
			errors.reject("", "Somente os administradores do sistema têm permissão para cancelar um indicador.");
	}
	
	@Command(validate=true)	
	public ModelAndView excluir(WebRequestContext request, SolicitacaoCancelamentoIndicadorFiltro bean) {
		
		SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador = new SolicitacaoCancelamentoIndicador();
		solicitacaoCancelamentoIndicador.setId(bean.getId());
			
		solicitacaoCancelamentoIndicador = solicitacaoCancelamentoIndicadorService.loadWithIndicador(solicitacaoCancelamentoIndicador);
		solicitacaoCancelamentoIndicadorService.delete(solicitacaoCancelamentoIndicador);
		
		indicadorService.atualizaStatusIndicador(solicitacaoCancelamentoIndicador.getIndicador(), StatusIndicadorEnum.APROVADO);
		
		request.addMessage("Solicitação de cancelamento de indicador excluída com sucesso");
		request.addMessage("Favor conferir a soma dos pesos dos indicadores no Painel de indicadores");			
		
		bean.setId(null);
		
		return new ModelAndView("redirect:/sgm/process/SolicitacaoCancelamentoIndicador");
	}
	
	
	/**
	 * Metodo chamado pelo popUp Solicitacao de cancelamento. Salva uma solicitacao no banco.
	 * @author Rodrigo Duarte
	 * @param request
	 * @param bean
	 * @throws IOException
	 */
	public void cancelamentoPorLancamento(WebRequestContext request, SolicitacaoCancelamentoIndicador bean) throws IOException {
		
		// Verifica se já não existe uma solicitação pendente.
		Boolean existeSolicitacaoCancelamentoAberta = solicitacaoCancelamentoIndicadorService.existeSolicitacaoCancelamentoAberta(bean.getIndicador());
		
		PrintWriter out = request.getServletResponse().getWriter();
		request.getServletResponse().setContentType("text/html");
		
		if (Boolean.TRUE.equals(existeSolicitacaoCancelamentoAberta)) {
			out.println("<script language='javascript'>alert('Já existe uma solicitação de cancelamento em andamento para este indicador.');window.close();window.opener.form['unidadeGerencial'].onchange();</script>");
			out.flush();
		}
		else {
			bean.setStatus(AprovacaoEnum.AG_APROVANDO);
			
			//Salva o status do indicador
			indicadorService.atualizaStatusIndicador(bean.getIndicador(), StatusIndicadorEnum.EM_CANCELAMENTO);
			
			try {
				usuarioService.enviaEmailAdministradorSolicitacaoCancelamentoIndicador(bean.getIndicador(), bean.getSolicitante(), bean.getJustificativaSol());
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}
	
			bean.setDtSolicitacao(new Date(System.currentTimeMillis()));		
			solicitacaoCancelamentoIndicadorService.saveOrUpdate(bean);
			
			out.println("<script language='javascript'>window.close();window.opener.form['unidadeGerencial'].onchange();</script>");
			out.flush();
		}
	}

	@Input("executar")   
	public ModelAndView popUpComentario(WebRequestContext request, SolicitacaoCancelamentoIndicador bean) throws CrudException {
		SolicitacaoCancelamentoIndicador solicitacao = this.getSolicitacaoByRequest(request);
		
		List<ComentarioItem> listaComentarioItem = null;
		if (solicitacao != null && solicitacao.getComentario() != null) {
			listaComentarioItem = solicitacao.getComentario().getListaComentarioItem();
		}
		request.setAttribute("listaComentarioItem", listaComentarioItem);
		
		ComentarioItem comentarioItem = new ComentarioItem();
		comentarioItem.setUsuario((Usuario) Neo.getRequestContext().getUser());
		comentarioItem.setData(new Date(System.currentTimeMillis()));
		comentarioItem.setComentario(solicitacao != null ? solicitacao.getComentario() : null);
		comentarioItem.setSolicitacaoCancelamentoIndicador(solicitacao);
		
		request.setAttribute("comentarioitem", comentarioItem);
		request.setAttribute("podeIncluir", solicitacao.getStatus().equals(AprovacaoEnum.AG_APROVANDO));
		return new ModelAndView("forward:/popup/popUpComentarioSolicitacaoCancelamentoIndicador.jsp");
	}
	
	private SolicitacaoCancelamentoIndicador getSolicitacaoByRequest(WebRequestContext request) {
		
		Integer solicitacaoID = Integer.parseInt(request.getParameter("solicitacaoID"));
		
		SolicitacaoCancelamentoIndicador solicitacao = new SolicitacaoCancelamentoIndicador();
		solicitacao.setId(solicitacaoID);
		solicitacao = solicitacaoCancelamentoIndicadorService.loadWithComentarios(solicitacao);
		
		return solicitacao;
	}
	
	public ModelAndView incluirComentario(WebRequestContext request, ComentarioItem comentarioItem) throws CrudException {
		
		SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador = solicitacaoCancelamentoIndicadorService.load(comentarioItem.getSolicitacaoCancelamentoIndicador());
		
		if (comentarioItem.getComentario() == null) {
			Comentario comentario = new Comentario();
			List<ComentarioItem> listaComentarioItem = new ArrayList<ComentarioItem>();
			listaComentarioItem.add(comentarioItem);
			comentario.setListaComentarioItem(listaComentarioItem);
			
			// Salva o comentário
			comentarioService.saveOrUpdate(comentario);
			
			// Vincula o comentário à solicitação
			solicitacaoCancelamentoIndicador.setComentario(comentario);
			solicitacaoCancelamentoIndicadorService.saveOrUpdate(solicitacaoCancelamentoIndicador);
		}
		else {
			comentarioItemService.saveOrUpdate(comentarioItem);
		}
		
		// Envia o email informando a inclusão do comentário
		Usuario usuarioAtual = (Usuario) Neo.getRequestContext().getUser();
		Usuario usuarioSolicitante = solicitacaoCancelamentoIndicador.getSolicitante(); 
		
		// Se o usuário atual for o solicitante do cancelamento, envia email para os administradores.
		if (usuarioAtual.equals(usuarioSolicitante)) {
			try {
				usuarioService.enviaEmailAdministradorComentarioSolicitacaoCancelamentoIndicador(solicitacaoCancelamentoIndicador.getIndicador(), usuarioSolicitante, comentarioItem.getTexto());
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}				
		}
		else { // Senão, envia email para o solicitante
			try {
				usuarioService.enviaEmailComentarioSolicitacaoCancelamentoIndicador(solicitacaoCancelamentoIndicador.getIndicador(), usuarioSolicitante, comentarioItem.getTexto());
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}			
		}
		
		return new ModelAndView("redirect:/sgm/process/SolicitacaoCancelamentoIndicador?ACAO=popUpComentario&solicitacaoID=" + comentarioItem.getSolicitacaoCancelamentoIndicador().getId());
	}
	
}
