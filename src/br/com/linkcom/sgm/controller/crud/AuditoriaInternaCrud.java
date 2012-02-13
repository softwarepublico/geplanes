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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.view.ajax.View;
import br.com.linkcom.sgm.beans.AuditoriaInterna;
import br.com.linkcom.sgm.beans.ItemAuditoriaInterna;
import br.com.linkcom.sgm.beans.Norma;
import br.com.linkcom.sgm.beans.RequisitoNorma;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.UsuarioAuditoriaInterna;
import br.com.linkcom.sgm.beans.enumeration.StatusAuditoriaInternaEnum;
import br.com.linkcom.sgm.beans.enumeration.TipoUsuarioAuditoriaInternaEnum;
import br.com.linkcom.sgm.filtro.AuditoriaInternaFiltro;
import br.com.linkcom.sgm.service.AuditoriaInternaService;
import br.com.linkcom.sgm.service.NormaService;
import br.com.linkcom.sgm.service.RequisitoNormaService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.GeplanesUtils;


@Controller(path="/sgm/crud/AuditoriaInterna", authorizationModule=CrudAuthorizationModule.class)
public class AuditoriaInternaCrud extends CrudController<AuditoriaInternaFiltro, AuditoriaInterna, AuditoriaInterna> {
	
	private UsuarioService usuarioService;
	private UnidadeGerencialService unidadeGerencialService;
	private RequisitoNormaService requisitoNormaService;
	private AuditoriaInternaService auditoriaInternaService;
	private NormaService normaService;
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setRequisitoNormaService(RequisitoNormaService requisitoNormaService) {
		this.requisitoNormaService = requisitoNormaService;
	}
	
	public void setAuditoriaInternaService(AuditoriaInternaService auditoriaInternaService) {
		this.auditoriaInternaService = auditoriaInternaService;
	}
	
	public void setNormaService(NormaService normaService) {
		this.normaService = normaService;
	}
	
	@Override
	public ModelAndView doListagem(WebRequestContext request, AuditoriaInternaFiltro filtro) throws CrudException {
		
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		
		return super.doListagem(request, filtro);
	}
	
	@Override
	protected ListagemResult<AuditoriaInterna> getLista(WebRequestContext request, AuditoriaInternaFiltro filtro) {
		ListagemResult<AuditoriaInterna> result = super.getLista(request, filtro);
		List<AuditoriaInterna> listaAuditoriaInterna = result.list();
		
		// Permissões de Tela
		Boolean temPermissaoEdicao    = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/crud/AuditoriaInterna", CrudController.EDITAR, request.getServletRequest());
		Boolean temPermissaoExclusao  = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/crud/AuditoriaInterna", CrudController.EXCLUIR, request.getServletRequest());
		Boolean temPermissaoRelatorio = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/report/AuditoriaInterna", "generate", request.getServletRequest());		
		
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsAuditorInterno = usuarioService.isUsuarioLogadoVinculadoAreaAuditoriaInterna(filtro.getPlanoGestao());
		Boolean usuarioLogadoIsRepQualidade = usuarioLogadoIsAdmin || auditoriaInternaService.usuarioPodeEncerrarAuditoriaInterna(filtro.getPlanoGestao());;
		
		for (AuditoriaInterna auditoriaInterna : listaAuditoriaInterna) {
			if (usuarioLogadoIsAdmin || usuarioLogadoIsAuditorInterno || usuarioLogadoIsRepQualidade) {
				auditoriaInterna.setPodeEditar(temPermissaoEdicao);

				// Se estiver com o status ABERTA, somente o administrador e o auditor interno poderão excluí-la
				if (auditoriaInterna.getStatus().equals(StatusAuditoriaInternaEnum.ABERTA)) {
					auditoriaInterna.setPodeExcluir(temPermissaoExclusao && (usuarioLogadoIsAdmin || usuarioLogadoIsAuditorInterno));
				}
				else {
					// Se não estiver com o status ABERTA, somente o administrador poderá excluí-la
					auditoriaInterna.setPodeExcluir(temPermissaoExclusao && usuarioLogadoIsAdmin);
				}				
				
				auditoriaInterna.setPodeImprimir(temPermissaoRelatorio);
			}
			else { 
				auditoriaInterna.setPodeEditar(false);
				auditoriaInterna.setPodeExcluir(false);
				auditoriaInterna.setPodeImprimir(temPermissaoRelatorio);
			}
		}
		
		request.setAttribute("podeCriarRegistroAuditoriaInterna", usuarioLogadoIsAdmin || usuarioLogadoIsAuditorInterno);
		
		return result;
	}
	
	@Override
	public ModelAndView doEntrada(WebRequestContext request, AuditoriaInterna auditoriaInterna) throws CrudException {
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(auditoriaInterna);
		}
		
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsAuditorInterno = usuarioService.isUsuarioLogadoVinculadoAreaAuditoriaInterna(auditoriaInterna.getPlanoGestao());
		Boolean usuarioLogadoIsRepQualidade = usuarioLogadoIsAdmin || auditoriaInternaService.usuarioPodeEncerrarAuditoriaInterna(auditoriaInterna.getPlanoGestao());;
		Boolean usuarioPodeConsultarAuditoriaInterna = usuarioLogadoIsAdmin || usuarioLogadoIsAuditorInterno || usuarioLogadoIsRepQualidade;
		Boolean usuarioPodeEditarAuditoriaInterna = false;
		
		if (usuarioPodeConsultarAuditoriaInterna) {
			// Criação de Auditoria Interna
			if (auditoriaInterna.getId() == null) {
				auditoriaInterna.setDataAuditoria(new Date(System.currentTimeMillis()));
				auditoriaInterna.setStatus(StatusAuditoriaInternaEnum.ABERTA);
			}
			
			if (auditoriaInterna.getPlanoGestao() != null) {
				request.setAttribute("listaUGAuditoriaInterna", unidadeGerencialService.findUGAuditoriaInterna(auditoriaInterna.getPlanoGestao()));
			}
			
			if (auditoriaInterna.getNorma() != null) {
				request.setAttribute("listaRequisitoNormaNaoConformidades", requisitoNormaService.findByNorma(auditoriaInterna.getNorma()));
			}
			else {
				request.setAttribute("listaRequisitoNormaNaoConformidades", new ArrayList<RequisitoNorma>());
			}
			usuarioPodeEditarAuditoriaInterna = usuarioPodeEditarAuditoriaInterna(auditoriaInterna, usuarioLogadoIsAdmin, usuarioLogadoIsAuditorInterno, usuarioLogadoIsRepQualidade);
			
			if (auditoriaInterna.getPlanoGestao() != null) {
				request.setAttribute("listaUGExterna", unidadeGerencialService.findWithSiglaNomeByPlanoGestao(auditoriaInterna.getPlanoGestao()));
			}
			else {
				request.setAttribute("listaUGExterna", new ArrayList<UnidadeGerencial>());
			}
			
			// Lista as outras normas disponíveis com exceção da norma selecionada para a auditoria
			List<Norma> listaNormas = normaService.findAll();
			List<Norma> listaOutrasNormas = new ArrayList<Norma>();
			if (listaNormas != null && !listaNormas.isEmpty()) {
				if (auditoriaInterna.getNorma() != null) {
					for (Norma norma : listaNormas) {
						if (!norma.equals(auditoriaInterna.getNorma())) {
							listaOutrasNormas.add(norma);
						}
					}
				}
				else {
					listaOutrasNormas = listaNormas;
				}
			}
			request.setAttribute("listaOutrasNormas", listaOutrasNormas);
			
			// Preenche as listas referentes aos requisitos de cada norma escolhida na aba "Outras não conformidades"
			if (auditoriaInterna.getListaOutrasNaoConformidades() != null && !auditoriaInterna.getListaOutrasNaoConformidades().isEmpty()) {
				Map<Integer, List<RequisitoNorma>> mapaRequisitoNormaOutrasNaoConformidades = new HashMap<Integer, List<RequisitoNorma>>();
				int i = 0;
				for (ItemAuditoriaInterna outraNaoConformidade : auditoriaInterna.getListaOutrasNaoConformidades()) {
					
					if (outraNaoConformidade.getNorma() != null && !outraNaoConformidade.getNorma().equals(auditoriaInterna.getNorma())) {
						mapaRequisitoNormaOutrasNaoConformidades.put(i, requisitoNormaService.findByNorma(outraNaoConformidade.getNorma()));
					}
					else {
						mapaRequisitoNormaOutrasNaoConformidades.put(i, new ArrayList<RequisitoNorma>());
					}
					
					i++;
				}
				request.setAttribute("listaRequisitoNormaOutrasNaoConformidades", mapaRequisitoNormaOutrasNaoConformidades);
			}
			
		}
		else {
			request.addMessage("Você não tem permissão para acessar os dados dessa auditoria interna.", MessageType.WARN);
		}
		
		request.setAttribute("podeConsultarAuditoriaInterna", usuarioPodeConsultarAuditoriaInterna);
		request.setAttribute("podeEditarAuditoriaInterna", usuarioPodeEditarAuditoriaInterna);
		return super.doEntrada(request, auditoriaInterna);
	}
	
	@Override
	protected void salvar(WebRequestContext request, AuditoriaInterna bean) throws Exception {
		prepareListsToSave(bean);
		
		// Salva a auditoria interna
		auditoriaInternaService.salvaAuditoriaInterna(bean,false,false);
		
		request.addMessage("Auditoria interna salva com sucesso", MessageType.INFO);
	}
	
	@Input(ENTRADA)
	@Action("salvarESolicitarEncerramento")
	public ModelAndView doSalvarESolicitarEncerramento(WebRequestContext request, AuditoriaInterna bean) throws Exception {
		prepareListsToSave(bean);
		bean.setStatus(StatusAuditoriaInternaEnum.ENCERRAMENTO_SOLICITADO);
		
		// Salva e envia email para os responsáveis da área de qualidade envolvida
		String errorMessage = auditoriaInternaService.salvaAuditoriaInterna(bean,true,false);
		
		if (errorMessage == null || errorMessage.equals("")) {
			request.addMessage("Solicitação de encerramento da auditoria interna efetuada com sucesso", MessageType.INFO);
		}
		else {
			request.addMessage("Solicitação de encerramento da auditoria interna efetuada com pendência(s): " + errorMessage, MessageType.WARN);
		}
		
		return getSalvarModelAndView(request, bean);
	}
	
	@Input(ENTRADA)
	@Action("salvarEEncerrar")	
	public ModelAndView doSalvarEEncerrar(WebRequestContext request, AuditoriaInterna bean) throws Exception {
		prepareListsToSave(bean);
		bean.setStatus(StatusAuditoriaInternaEnum.ENCERRADA);
		bean.setDataEncerramento(new Date(System.currentTimeMillis()));

		// Salva a auditoria interna e cria os registros de anomalia
		String errorMessage = auditoriaInternaService.salvaAuditoriaInterna(bean,false,true);
		
		if (errorMessage == null || errorMessage.equals("")) {
			request.addMessage("Encerramento da auditoria interna efetuado com sucesso", MessageType.INFO);
		}
		else {
			request.addMessage("Encerramento da auditoria interna efetuado com pendência(s): " + errorMessage, MessageType.WARN);
		}		
		
		return getSalvarModelAndView(request, bean);
	}
	
	
	private void prepareListsToSave(AuditoriaInterna bean) {
		List<ItemAuditoriaInterna> listaItemAuditoriaInterna = new ArrayList<ItemAuditoriaInterna>();
		listaItemAuditoriaInterna.addAll(bean.getListaNaoConformidades());
		listaItemAuditoriaInterna.addAll(bean.getListaOutrasNaoConformidades());
		bean.setListaItemAuditoriaInterna(listaItemAuditoriaInterna);
		
		List<UsuarioAuditoriaInterna> listaUsuarioAuditoriaInterna = new ArrayList<UsuarioAuditoriaInterna>();
		if (bean.getListaEquipeAuditora() != null) {
			for (UsuarioAuditoriaInterna usuarioAuditoriaInterna : bean.getListaEquipeAuditora()) {
				usuarioAuditoriaInterna.setTipo(TipoUsuarioAuditoriaInternaEnum.AUDITOR);
				listaUsuarioAuditoriaInterna.add(usuarioAuditoriaInterna);
			}
		}
		if (bean.getListaEquipeAuditada() != null) {
			for (UsuarioAuditoriaInterna usuarioAuditoriaInterna : bean.getListaEquipeAuditada()) {
				usuarioAuditoriaInterna.setTipo(TipoUsuarioAuditoriaInternaEnum.AUDITADO);
				listaUsuarioAuditoriaInterna.add(usuarioAuditoriaInterna);
			}
		}
		bean.setListaUsuarioAuditoriaInterna(listaUsuarioAuditoriaInterna);
	}
	
	private Boolean usuarioPodeEditarAuditoriaInterna(AuditoriaInterna auditoriaInterna, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsAuditorInterno, Boolean usuarioLogadoIsRepQualidade) {
		Boolean podeEditarAuditoriaInterna = false;
		
		if (!auditoriaInterna.getStatus().equals(StatusAuditoriaInternaEnum.ENCERRADA)) {
			// Usuário admin
			if (usuarioLogadoIsAdmin) {
				podeEditarAuditoriaInterna = true;
			}
			else {
				// Se a anomalia estiver com o status ABERTA, somente o usuario auditor poderá editá-la.
				if (auditoriaInterna.getStatus().equals(StatusAuditoriaInternaEnum.ABERTA)) {
					if (usuarioLogadoIsAuditorInterno) {
						podeEditarAuditoriaInterna = true;
					}
				}
				// Se a anomalia estiver com o status ENCERRAMENTO SOLICITADO, somente o usuario auditor poderá editá-la.
				else if (auditoriaInterna.getStatus().equals(StatusAuditoriaInternaEnum.ENCERRAMENTO_SOLICITADO)) {
					if (usuarioLogadoIsRepQualidade) {
						podeEditarAuditoriaInterna = true;
					}				
				}
			}
		}
		
		return podeEditarAuditoriaInterna;
	}
	
	public void ajaxComboRequisito(WebRequestContext request) {
		List<RequisitoNorma> listaRequisitoNorma = new ArrayList<RequisitoNorma>();
		
		String idNorma = request.getParameter("idNorma");
		Norma norma = null;
		
		if (idNorma != null && !idNorma.equals("")) {
			norma = new Norma(Integer.parseInt(idNorma));
			listaRequisitoNorma = requisitoNormaService.findByNorma(norma);
		}
		
		View.getCurrent().println(GeplanesUtils.convertToJavaScript(listaRequisitoNorma, "listaRequisitoNorma", ""));
	}	
	
}