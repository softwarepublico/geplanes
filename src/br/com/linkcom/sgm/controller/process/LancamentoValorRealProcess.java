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

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Command;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.view.DownloadFileServlet;
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.AnexoIndicador;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.SolicitacaoCancelamentoIndicador;
import br.com.linkcom.sgm.beans.SolicitacaoRepactuacaoIndicador;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.controller.filtro.LancamentoValorRealFiltro;
import br.com.linkcom.sgm.service.AcompanhamentoIndicadorService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.LancamentoValorRealService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.Nomes;

@Bean
@Controller(path="/sgm/process/LancamentoValorReal", authorizationModule=ProcessAuthorizationModule.class)
public class LancamentoValorRealProcess extends MultiActionController {
	
	private IndicadorService indicadorService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private LancamentoValorRealService lancamentoValorRealService;
	private UnidadeGerencialService unidadeGerencialService;
		
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	public void setLancamentoValorRealService(LancamentoValorRealService lancamentoValorRealService) {this.lancamentoValorRealService = lancamentoValorRealService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	
	@DefaultAction	
    public ModelAndView entrada(WebRequestContext request, LancamentoValorRealFiltro filtro) {
		
		request.setLastAction("entrada");
		
		/*** Seta valores default para o filtro ***/
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}	
				
				
		if (filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
				
			try {
				/*** retorna os objetivos estratégicos, os indicadores e seus acompanhamentos ***/
				filtro = lancamentoValorRealService.pesquisar(filtro);
				
				/*** insere informações no request ***/
				lancamentoValorRealService.configuraParametrosTela(request, filtro);				
				
				/*** Se necessário, calcula os valores acumulados***/
				if (filtro.getAlternar()) {
					lancamentoValorRealService.somarValoresAcumulados(filtro);
				}
			}
			catch (Exception e) {
				request.setAttribute("showTela", false);
				request.addMessage(e.getMessage(),MessageType.WARN);
			}
			
		}
		return new ModelAndView("process/lancamentoValorReal", "filtro", filtro);
    }
	
	
	@Command(validate=true)
	public ModelAndView salvar(WebRequestContext request, LancamentoValorRealFiltro filtro){
		lancamentoValorRealService.salvar(filtro);		
		request.addMessage("Lançamentos de "+ Nomes.valores_reais +" salvos com sucesso", MessageType.INFO);
		return entrada(request,filtro);
	}
		
	
	/**
	 * Coloca no escopo a ordem para alternar
	 * @author Rodrigo Duarte
	 * @param request
	 * @param filtro
	 * @return
	 */
	@Input("entrada")
	public ModelAndView alternarValores( WebRequestContext request, LancamentoValorRealFiltro filtro){

		/*** Alterna o valor de filtro.alternar ***/	
		filtro.setAlternar( !filtro.getAlternar() );		
		
		return entrada(request, filtro);

	} 
	
	//=================== Popups ==========================================================================
	
	@Input("entrada")
	public ModelAndView popUpIndicador(WebRequestContext request, LancamentoValorRealFiltro filtro) throws CrudException {
		String idStringIndicador= request.getParameter("indicadorID");		
		if (idStringIndicador != null && !idStringIndicador.equals("")) {			
			Indicador indicador = new Indicador(new Integer(idStringIndicador));
			indicador = indicadorService.loadForEntrada(indicador);
			indicador.setUnidadeGerencial(unidadeGerencialService.loadWithPlanoGestaoByIndicador(indicador));
			
			for (AnexoIndicador anexo : indicador.getAnexosIndicador()) {
				DownloadFileServlet.addCdfile(request.getSession(), anexo.getArquivo().getCdfile());
			}
			
			request.setAttribute("indicador", indicador);
		}
		
		return new ModelAndView("forward:/popup/popUpDetalhamentoIndicadorCompleta.jsp");
	}
	
	@Input("entrada")   
	public ModelAndView popUpSolicitacaoCancelamentoIndicador(WebRequestContext request, LancamentoValorRealFiltro filtro) throws CrudException {
		
		
		Indicador indicador = this.getIndicadorByRequest(request);
		Usuario usuario = (Usuario) Neo.getUser();
		
		SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador = new SolicitacaoCancelamentoIndicador();
		solicitacaoCancelamentoIndicador.setIndicador(indicador);
		solicitacaoCancelamentoIndicador.setSolicitante(usuario);
		
		request.setAttribute("solicitacaoCancelamentoIndicador", solicitacaoCancelamentoIndicador);
		
		return new ModelAndView("forward:/popup/popUpSolicitacaoCancelamentoIndicador.jsp");
	}		
	
	@Input("entrada")   
	public ModelAndView popUpSolicitacaoRepactuacaoIndicador(WebRequestContext request, LancamentoValorRealFiltro filtro) throws CrudException {
		
		
		Indicador indicador = this.getIndicadorByRequest(request);
		Usuario usuario = (Usuario) Neo.getUser();
		
		SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador = new SolicitacaoRepactuacaoIndicador();
		solicitacaoRepactuacaoIndicador.setIndicador(indicador);
		solicitacaoRepactuacaoIndicador.setSolicitante(usuario);
		
		request.setAttribute("solicitacaoRepactuacaoIndicador", solicitacaoRepactuacaoIndicador);
		
		return new ModelAndView("forward:/popup/popUpSolicitacaoRepactuacaoIndicador.jsp");
	}		
	
	private Indicador getIndicadorByRequest(WebRequestContext request) {
		
		Integer indicadorID = Integer.parseInt(request.getParameter("indicadorID"));
		
		Indicador indicador = new Indicador();
		indicador.setId(indicadorID);
		
		return indicador;
	}	
	
	/**
	 * @author Rodrigo Alvarenga
	 * @param request
	 * @param acompanhamentoIndicador
	 * @return
	 */
	public ModelAndView tratarAcaoPreventiva( WebRequestContext request, AcompanhamentoIndicador acompanhamentoIndicador){
		
		if (acompanhamentoIndicador.getId() != null) {		
			acompanhamentoIndicador = acompanhamentoIndicadorService.load(acompanhamentoIndicador);
		}
				
		AcaoPreventiva acaoPreventiva = acompanhamentoIndicador.getAcaoPreventiva();
		if (acaoPreventiva!= null && acaoPreventiva.getId()!=null) {
			return new ModelAndView("redirect:/sgm/crud/AcaoPreventiva?ACAO=editar&id="+acaoPreventiva.getId()+"&acompanhamentoIndicador.id="+acompanhamentoIndicador.getId());
		}
		else{
			String idStringUgOrigem = request.getParameter("idUgOrigem");
			if (idStringUgOrigem != null && !idStringUgOrigem.equals("")) {
				try {
					Integer idUgOrigem = Integer.parseInt(idStringUgOrigem);
					return new ModelAndView("redirect:/sgm/crud/AcaoPreventiva?ACAO=criar&acompanhamentoIndicador.id="+acompanhamentoIndicador.getId()+"&idUgOrigem="+idUgOrigem);
				} catch (Exception e) {}
			}
			return new ModelAndView("redirect:/sgm/crud/AcaoPreventiva?ACAO=criar&acompanhamentoIndicador.id="+acompanhamentoIndicador.getId());
		}
		
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param request
	 * @param acompanhamentoIndicador
	 * @return
	 */
	public ModelAndView tratarAnomalia( WebRequestContext request, AcompanhamentoIndicador acompanhamentoIndicador){
		
		if(acompanhamentoIndicador.getId()!=null)		
			acompanhamentoIndicador = acompanhamentoIndicadorService.load(acompanhamentoIndicador);
		
		Anomalia anomalia = acompanhamentoIndicador.getAnomalia();
		if(anomalia!= null && anomalia.getId()!=null){
			return new ModelAndView("redirect:/sgm/crud/Anomalia?ACAO=editar&id="+anomalia.getId()+"&acompanhamentoIndicador.id="+acompanhamentoIndicador.getId());
		}else{
			//relacionado ao caso de uso tratamento de anomalia onde é preciso fixar a ug de origem
			String idStringUgOrigem = request.getParameter("idUgOrigem");
			if(idStringUgOrigem != null && !idStringUgOrigem.equals("")){
				try {
					Integer idUgOrigem = Integer.parseInt(idStringUgOrigem);
					return new ModelAndView("redirect:/sgm/crud/Anomalia?ACAO=criar&acompanhamentoIndicador.id="+acompanhamentoIndicador.getId()+"&idUgOrigem="+idUgOrigem);
				} catch (Exception e) {}
			}
			return new ModelAndView("redirect:/sgm/crud/Anomalia?ACAO=criar&acompanhamentoIndicador.id="+acompanhamentoIndicador.getId());
		}
		
	}
	
}
