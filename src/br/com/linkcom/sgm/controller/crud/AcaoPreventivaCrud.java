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
package br.com.linkcom.sgm.controller.crud;


import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusAcaoPreventivaEnum;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AcaoPreventivaFiltro;
import br.com.linkcom.sgm.service.AcaoPreventivaService;
import br.com.linkcom.sgm.service.AcompanhamentoIndicadorService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.PlanoAcaoService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.GeplanesUtils;

@Controller(path="/sgm/crud/AcaoPreventiva", authorizationModule=CrudAuthorizationModule.class)
public class AcaoPreventivaCrud extends CrudController<AcaoPreventivaFiltro, AcaoPreventiva, AcaoPreventiva> {
	
	private UsuarioService usuarioService;
	private AcaoPreventivaService acaoPreventivaService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private IndicadorService indicadorService;
	private PlanoAcaoService planoAcaoService;
	
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {this.planoAcaoService = planoAcaoService;}
	public void setAcaoPreventivaService(AcaoPreventivaService acaoPreventivaService) {this.acaoPreventivaService = acaoPreventivaService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
		
	@Override
	public ModelAndView doListagem(WebRequestContext request,AcaoPreventivaFiltro filtro) throws CrudException {
		
		/*** Seta valores default para o filtro ***/
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro,"ugRegistro");
		}
		else {
			FiltroUtils.preencheFiltroPlanoGestao(filtro);
		}
		
		return super.doListagem(request, filtro);
	}
	
	@Override
	protected ListagemResult<AcaoPreventiva> getLista(WebRequestContext request, AcaoPreventivaFiltro filtro) {
		ListagemResult<AcaoPreventiva> result = super.getLista(request, filtro);
		List<AcaoPreventiva> listaAcaoPreventiva = result.list();
		
		// Permissões de Tela
		Boolean temPermissaoEdicao    = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/crud/AcaoPreventiva", CrudController.EDITAR, request.getServletRequest());
		Boolean temPermissaoExclusao  = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/crud/AcaoPreventiva", CrudController.EXCLUIR, request.getServletRequest());
		Boolean temPermissaoRelatorio = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/report/AcaoPreventiva", "generate", request.getServletRequest());		
		
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsParticipanteUgRegistro;
		Boolean usuarioLogadoQualidadeDireto = usuarioLogadoIsAdmin || acaoPreventivaService.usuarioPodeEncerrarAcaoPreventiva(filtro.getPlanoGestao());
		
		for (AcaoPreventiva acaoPreventiva : listaAcaoPreventiva) {
			usuarioLogadoIsParticipanteUgRegistro = usuarioService.isUsuarioLogadoParticipanteUG(acaoPreventiva.getUgRegistro());
			
			if (acaoPreventiva.getDataEncerramento() != null) {
				acaoPreventiva.setEncerrado(true); 
			}
			else {
				acaoPreventiva.setEncerrado(false); 
			}
			if (usuarioLogadoIsAdmin || usuarioLogadoIsParticipanteUgRegistro) {
				acaoPreventiva.setPodeEditar(temPermissaoEdicao);
				
				if (usuarioLogadoIsAdmin || (usuarioLogadoIsParticipanteUgRegistro && acaoPreventiva.getStatus().equals(StatusAcaoPreventivaEnum.ABERTA))) {
					acaoPreventiva.setPodeExcluir(temPermissaoExclusao);
				}
				else {
					acaoPreventiva.setPodeExcluir(false);
				}
				
				acaoPreventiva.setPodeImprimir(temPermissaoRelatorio);
			}
			else if (usuarioLogadoQualidadeDireto) {
				acaoPreventiva.setPodeEditar(temPermissaoEdicao);
				acaoPreventiva.setPodeExcluir(false);
				acaoPreventiva.setPodeImprimir(temPermissaoRelatorio);
			}
			else {
				acaoPreventiva.setPodeEditar(false);
				acaoPreventiva.setPodeExcluir(false);
				acaoPreventiva.setPodeImprimir(true);
			}			
		}
		
		return result;
	}
	
	@Override
	protected void entrada(WebRequestContext request, AcaoPreventiva form) throws Exception {
		
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(form, "ugRegistro");		
		
		//Objetos
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsParticipanteUgRegistro = usuarioService.isUsuarioLogadoParticipanteUG(form.getUgRegistro());
		
		Boolean usuarioPodeEncerrarAcaoPreventiva = usuarioLogadoIsAdmin || acaoPreventivaService.usuarioPodeEncerrarAcaoPreventiva(form.getPlanoGestao());
		Boolean usuarioPodeConsultarAcaoPreventiva = usuarioPodeConsultarAcaoPreventiva(form, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgRegistro, usuarioPodeEncerrarAcaoPreventiva);
		Boolean usuarioPodeEditarAcaoPreventiva;
		
		// Criação de AcaoPreventiva
		if (form.getId() == null) {
			usuarioPodeEditarAcaoPreventiva = true;
			form.setStatus(StatusAcaoPreventivaEnum.ABERTA);
			form.setDataAbertura(new Date(System.currentTimeMillis()));
			
			//Verifica se a acaoPreventiva foi requisitada de algum lugar
			String idStringUgOrigem = request.getParameter("idUgOrigem");
			if(idStringUgOrigem != null && !idStringUgOrigem.equals("")){
				UnidadeGerencial ugRegistro = acaoPreventivaService.verificaUgOrigem(idStringUgOrigem,form);
				form.setUgRegistro(ugRegistro);
				form.setPlanoGestao(ugRegistro.getPlanoGestao());
			}
		}
		// Edição de AcaoPreventiva
		else {
			usuarioPodeEditarAcaoPreventiva = usuarioPodeEditarAcaoPreventiva(form, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgRegistro);
			form.setPlanosAcao(new ListSet<PlanoAcao>(PlanoAcao.class,planoAcaoService.findByAcaoPreventiva(form)));
		}
		
		if (usuarioPodeConsultarAcaoPreventiva) {
			/*** Coloca na tela o acompanhamento indicador ***/	
			AcompanhamentoIndicador acompanhamentoIndicador = this.getAcompanhamentoIndicador(request);
					
			if( acompanhamentoIndicador!=null ){
				form.setAcompanhamentoIndicador(acompanhamentoIndicador);
				Indicador indicador = acompanhamentoIndicador.getIndicador();
				indicador= indicadorService.load(indicador);
				request.addMessage("Ação preventiva para acompanhamento do indicador "+
						indicador.getNome()+" no período de "+ 
						this.getStringDate( acompanhamentoIndicador.getDataInicial())+" até "+ 
						this.getStringDate(acompanhamentoIndicador.getDataFinal())+"." );
			}
		}
		else {
			request.addMessage("Você não tem permissão para acessar os dados dessa ação preventiva.", MessageType.WARN);
		}
		
		request.setAttribute("podeConsultarAcaoPreventiva", usuarioPodeConsultarAcaoPreventiva);
		request.setAttribute("podeEditarAcaoPreventiva", usuarioPodeEditarAcaoPreventiva);
		request.setAttribute("podeEncerrarAcaoPreventiva", usuarioPodeEncerrarAcaoPreventiva);
		super.entrada(request, form);		
	}
	
	private String getStringDate(Calendar date) {
		
		String dataString="";
		
		if( date.get(Calendar.DAY_OF_MONTH)<0)
			dataString+="0";
		
		dataString+=date.get(Calendar.DAY_OF_MONTH)+"/";
		dataString+=(date.get(Calendar.MONTH)+1)+"/";
		dataString+=date.get(Calendar.YEAR);
		
		
		return dataString;
		
	}
	
	private AcompanhamentoIndicador getAcompanhamentoIndicador(WebRequestContext request) {
		
		String acompanhamentoIndicadorString = request.getParameter("acompanhamentoIndicador.id");	
		
		if(acompanhamentoIndicadorString!=null){			
			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			acompanhamentoIndicador.setId(Integer.parseInt(acompanhamentoIndicadorString));
			return acompanhamentoIndicadorService.load(acompanhamentoIndicador);			
		}
		
		return null;
	}
	
	@Input(ENTRADA)
	@Override
	protected void salvar(WebRequestContext request, AcaoPreventiva acaoPreventiva) throws Exception {
		
		//Busca o próximo sequencial
		if (acaoPreventiva.getSequencial() == null) {
			acaoPreventiva.setSequencial(acaoPreventivaService.getProximoSequencial(acaoPreventiva.getUgRegistro()));
		}
		
		acaoPreventiva.setStatus(acaoPreventivaService.getStatusAcaoPreventiva(acaoPreventiva));
		
		super.salvar(request, acaoPreventiva);
		
		AcompanhamentoIndicador acompanhamentoIndicador = acaoPreventiva.getAcompanhamentoIndicador();
		
		try{
			if (acompanhamentoIndicador!=null) {	
				acompanhamentoIndicador.setAcaoPreventiva(acaoPreventiva);
				acompanhamentoIndicadorService.updateAcaoPreventiva(acompanhamentoIndicador, acaoPreventiva);			
			}
		}
		catch (Exception e) {
			throw new GeplanesException(e.getMessage());
		}
		request.addMessage("Ação preventiva salva com sucesso", MessageType.INFO);
	}
	
	@Input(ENTRADA)
	@Override
	protected void validateBean(AcaoPreventiva bean, BindException errors) {
		if (bean.getDataAbertura() != null && bean.getDataEncerramento() != null) {
			if(bean.getDataEncerramento().before(bean.getDataAbertura())){
				errors.reject("A data de encerramento deve ser maior que a data de abertura.","A data de encerramento deve ser maior que a data de abertura.");
			}
		}
		super.validateBean(bean, errors);
	}
	
	private Boolean usuarioPodeConsultarAcaoPreventiva(AcaoPreventiva acaoPreventiva, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgRegistro, Boolean usuarioPodeEncerrarAcaoPreventiva) {
		Boolean podeConsultarAcaoPreventiva = false;
		
		if (acaoPreventiva.getId() == null) {
			podeConsultarAcaoPreventiva = true;
		}
		else {
			if (usuarioLogadoIsAdmin || usuarioLogadoIsParticipanteUgRegistro || usuarioPodeEncerrarAcaoPreventiva) {
				podeConsultarAcaoPreventiva = true;
			}
		}
		
		return podeConsultarAcaoPreventiva;
	}
	
	private Boolean usuarioPodeEditarAcaoPreventiva(AcaoPreventiva acaoPreventiva, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgRegistro) {
		Boolean podeEditarAcaoPreventiva = false;
		
		// Usuário admin
		if (usuarioLogadoIsAdmin) {
			podeEditarAcaoPreventiva = true;
		}
		else {
			// Usuário participante da UG de Registro, desde que a acaoPreventiva esteja com status ABERTA
			if (acaoPreventiva.getStatus().equals(StatusAcaoPreventivaEnum.ABERTA)) {
				if (usuarioLogadoIsParticipanteUgRegistro) {
					podeEditarAcaoPreventiva = true;
				}
			}
		}
		
		return podeEditarAcaoPreventiva;
	}
}