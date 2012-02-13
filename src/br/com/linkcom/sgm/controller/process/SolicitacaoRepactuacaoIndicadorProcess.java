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
import br.com.linkcom.sgm.beans.SolicitacaoRepactuacaoIndicador;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.enumeration.AprovacaoEnum;
import br.com.linkcom.sgm.controller.filtro.SolicitacaoRepactuacaoIndicadorFiltro;
import br.com.linkcom.sgm.service.ComentarioItemService;
import br.com.linkcom.sgm.service.ComentarioService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.SolicitacaoRepactuacaoIndicadorService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Bean
@Controller(path="/sgm/process/SolicitacaoRepactuacaoIndicador", authorizationModule=ProcessAuthorizationModule.class)
public class SolicitacaoRepactuacaoIndicadorProcess extends MultiActionController {

	IndicadorService indicadorService;
	SolicitacaoRepactuacaoIndicadorService solicitacaoRepactuacaoIndicadorService;
	UsuarioService usuarioService;
	private ComentarioService comentarioService;
	private ComentarioItemService comentarioItemService;	
	
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	public void setSolicitacaoRepactuacaoIndicadorService(SolicitacaoRepactuacaoIndicadorService solicitacaoRepactuacaoIndicadorService) {this.solicitacaoRepactuacaoIndicadorService = solicitacaoRepactuacaoIndicadorService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setComentarioService(ComentarioService comentarioService) {this.comentarioService = comentarioService;}
	public void setComentarioItemService(ComentarioItemService comentarioItemService) {this.comentarioItemService = comentarioItemService;}	
	
	@DefaultAction
	@Action("executar")
    public ModelAndView executar(WebRequestContext request, SolicitacaoRepactuacaoIndicadorFiltro filtro) {

		request.setLastAction("executar");
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}		
		
		if (filtro.getPlanoGestao() != null) {
			
			/*** insere informações no request ***/
			request.setAttribute("isAdmin", usuarioService.isUsuarioLogadoAdmin());
					
			filtro.setListaSolicitacaoRepactuacaoIndicador(solicitacaoRepactuacaoIndicadorService.findSolicitacoes(filtro));
		}
			
		return new ModelAndView("process/solicitacaoRepactuacaoIndicador", "filtro", filtro);
	}
	
	@Command(validate=true)
	public ModelAndView salvar(WebRequestContext request, SolicitacaoRepactuacaoIndicadorFiltro bean) {
		List<SolicitacaoRepactuacaoIndicador> listaSolicitacoesRepactuacaoIndicador = bean.getListaSolicitacaoRepactuacaoIndicador();
		Integer id_indicador = null;
		
		for (SolicitacaoRepactuacaoIndicador solicitacaoRepactuacao : listaSolicitacoesRepactuacaoIndicador) {
			if (!solicitacaoRepactuacao.getId().equals(bean.getId())) {
				continue;
			}
			
			if (bean.getAprovar()) {
				solicitacaoRepactuacao.setStatus(AprovacaoEnum.APROVADO);
				id_indicador = solicitacaoRepactuacao.getIndicador().getId();
			} 
			else {
				solicitacaoRepactuacao.setStatus(AprovacaoEnum.REPROVADO);
			}
			
			solicitacaoRepactuacaoIndicadorService.saveOrUpdateStatus(solicitacaoRepactuacao);
			
			try {
				// Envia um email para o solicitante informando sobre a aprovação ou reprovação da solicitação.
				usuarioService.enviaEmailRespostaSolicitacaoRepactuacaoIndicador(solicitacaoRepactuacao.getIndicador(), solicitacaoRepactuacao.getSolicitante(), solicitacaoRepactuacao.getJustificativaRes(), bean.getAprovar() ? "APROVADA" : "REPROVADA");			
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}
		}		
		
		bean.setId(null);
		bean.setAprovar(null);
		
		if(id_indicador != null){
			request.addMessage("Solicitação de repactuação de indicador aprovada com sucesso.");			
			return new ModelAndView("redirect:/sgm/process/DistribuicaoPesosIndicadores?id_indicador=" + id_indicador);
		} else {
			request.addMessage("Solicitação de repactuação de indicador reprovada com sucesso.");			
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
	public ModelAndView excluir(WebRequestContext request, SolicitacaoRepactuacaoIndicadorFiltro bean) {
		
		SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador = new SolicitacaoRepactuacaoIndicador();
		solicitacaoRepactuacaoIndicador.setId(bean.getId());
			
		solicitacaoRepactuacaoIndicador = solicitacaoRepactuacaoIndicadorService.loadWithIndicador(solicitacaoRepactuacaoIndicador);
		solicitacaoRepactuacaoIndicadorService.delete(solicitacaoRepactuacaoIndicador);
		
		request.addMessage("Solicitação de repactuação de indicador excluída com sucesso");
		
		bean.setId(null);
		
		return new ModelAndView("redirect:/sgm/process/SolicitacaoRepactuacaoIndicador");
	}	
	
	/**
	 * Metodo chamado pelo popUp Solicitacao de Repactuacao. Salva uma solicitacao no banco.
	 * @author Rodrigo Duarte
	 * @param request
	 * @param bean
	 * @throws IOException
	 */
	public void repactuacaoPorLancamento(WebRequestContext request, SolicitacaoRepactuacaoIndicador bean) throws IOException {
		
		// Verifica se já não existe uma solicitação pendente.
		Boolean existeSolicitacaoRepactuacaoAberta = solicitacaoRepactuacaoIndicadorService.existeSolicitacaoRepactuacaoAberta(bean.getIndicador());
		
		PrintWriter out = request.getServletResponse().getWriter();
		request.getServletResponse().setContentType("text/html");
		
		if (Boolean.TRUE.equals(existeSolicitacaoRepactuacaoAberta)) {
			out.println("<script language='javascript'>alert('Já existe uma solicitação de repactuação em andamento para este indicador.');window.close();window.opener.form['unidadeGerencial'].onchange();</script>");
			out.flush();
		}
		else {
			bean.setStatus(AprovacaoEnum.AG_APROVANDO);
			
			try {
				usuarioService.enviaEmailAdministradorSolicitacaoRepactuacaoIndicador(bean.getIndicador(), bean.getSolicitante(), bean.getJustificativaSol());
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}				
	
			bean.setDtSolicitacao(new Date(System.currentTimeMillis()));		
			solicitacaoRepactuacaoIndicadorService.saveOrUpdate(bean);
			
			out.println("<script language='javascript'>window.close();window.opener.form['unidadeGerencial'].onchange();</script>");
			out.flush();
		}
	}
	
	@Input("executar")   
	public ModelAndView popUpComentario(WebRequestContext request, SolicitacaoRepactuacaoIndicador bean) throws CrudException {
		SolicitacaoRepactuacaoIndicador solicitacao = this.getSolicitacaoByRequest(request);
		
		List<ComentarioItem> listaComentarioItem = null;
		if (solicitacao != null && solicitacao.getComentario() != null) {
			listaComentarioItem = solicitacao.getComentario().getListaComentarioItem();
		}
		request.setAttribute("listaComentarioItem", listaComentarioItem);
		
		ComentarioItem comentarioItem = new ComentarioItem();
		comentarioItem.setUsuario((Usuario) Neo.getRequestContext().getUser());
		comentarioItem.setData(new Date(System.currentTimeMillis()));
		comentarioItem.setComentario(solicitacao != null ? solicitacao.getComentario() : null);
		comentarioItem.setSolicitacaoRepactuacaoIndicador(solicitacao);
		
		request.setAttribute("comentarioitem", comentarioItem);
		request.setAttribute("podeIncluir", solicitacao.getStatus().equals(AprovacaoEnum.AG_APROVANDO));
		return new ModelAndView("forward:/popup/popUpComentarioSolicitacaoRepactuacaoIndicador.jsp");
	}
	
	private SolicitacaoRepactuacaoIndicador getSolicitacaoByRequest(WebRequestContext request) {
		
		Integer solicitacaoID = Integer.parseInt(request.getParameter("solicitacaoID"));
		
		SolicitacaoRepactuacaoIndicador solicitacao = new SolicitacaoRepactuacaoIndicador();
		solicitacao.setId(solicitacaoID);
		solicitacao = solicitacaoRepactuacaoIndicadorService.loadWithComentarios(solicitacao);
		
		return solicitacao;
	}
	
	public ModelAndView incluirComentario(WebRequestContext request, ComentarioItem comentarioItem) throws CrudException {
		
		SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador = solicitacaoRepactuacaoIndicadorService.load(comentarioItem.getSolicitacaoRepactuacaoIndicador());
		
		if (comentarioItem.getComentario() == null) {
			Comentario comentario = new Comentario();
			List<ComentarioItem> listaComentarioItem = new ArrayList<ComentarioItem>();
			listaComentarioItem.add(comentarioItem);
			comentario.setListaComentarioItem(listaComentarioItem);
			
			// Salva o comentário
			comentarioService.saveOrUpdate(comentario);
			
			// Vincula o comentário à solicitação
			solicitacaoRepactuacaoIndicador.setComentario(comentario);
			solicitacaoRepactuacaoIndicadorService.saveOrUpdate(solicitacaoRepactuacaoIndicador);
		}
		else {
			comentarioItemService.saveOrUpdate(comentarioItem);
		}
		
		// Envia o email informando a inclusão do comentário
		Usuario usuarioAtual = (Usuario) Neo.getRequestContext().getUser();
		Usuario usuarioSolicitante = solicitacaoRepactuacaoIndicador.getSolicitante(); 
		
		// Se o usuário atual for o solicitante do repactuacao, envia email para os administradores.
		if (usuarioAtual.equals(usuarioSolicitante)) {
			try {
				usuarioService.enviaEmailAdministradorComentarioSolicitacaoRepactuacaoIndicador(solicitacaoRepactuacaoIndicador.getIndicador(), usuarioSolicitante, comentarioItem.getTexto());
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}				
		}
		else { // Senão, envia email para o solicitante
			try {
				usuarioService.enviaEmailComentarioSolicitacaoRepactuacaoIndicador(solicitacaoRepactuacaoIndicador.getIndicador(), usuarioSolicitante, comentarioItem.getTexto());
			}
			catch (Exception e) {
				request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
			}				
		}
		
		return new ModelAndView("redirect:/sgm/process/SolicitacaoRepactuacaoIndicador?ACAO=popUpComentario&solicitacaoID=" + comentarioItem.getSolicitacaoRepactuacaoIndicador().getId());
	}	

}
