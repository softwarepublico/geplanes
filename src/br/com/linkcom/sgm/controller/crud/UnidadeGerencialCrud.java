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

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.filtro.UnidadeGerencialFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.Nomes;


@Controller(path="/sgm/crud/UnidadeGerencial", authorizationModule=CrudAuthorizationModule.class)
public class UnidadeGerencialCrud extends CrudController<UnidadeGerencialFiltro, UnidadeGerencial, UnidadeGerencial> {

	private UnidadeGerencialService unidadeGerencialService;
	private PlanoGestaoService planoGestaoService;
	private UsuarioService usuarioService;
		
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) { this.planoGestaoService = planoGestaoService;}
	public void setUsuarioService(UsuarioService usuarioService) { this.usuarioService = usuarioService;}
	
	@Override
	protected void validateBean(UnidadeGerencial bean, BindException errors) {
		
		String beanNome = (Util.strings.tiraAcento(bean.getNome())).trim();
		//String beanSigla = (Util.strings.tiraAcento(bean.getSigla())).trim();
		
		List<UnidadeGerencial> listaUnidadeGerencial = unidadeGerencialService.findWithSiglaNomeByPlanoGestao(bean.getPlanoGestao());
		
		/*** verifica se a unidade gerencial já está cadastrada no banco para o ano de gestão especificado ***/
		if(listaUnidadeGerencial != null) {
			
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencial) {
				
				String unidadeGerencialNome  = (Util.strings.tiraAcento(unidadeGerencial.getNome())).trim();
				//String unidadeGerencialSigla = (Util.strings.tiraAcento(unidadeGerencial.getSigla())).trim();
			
				if(!unidadeGerencial.getId().equals(bean.getId()) &&  beanNome.compareToIgnoreCase(unidadeGerencialNome)==0){
					errors.reject("","Já existe uma unidade gerencial com este nome para o " + Nomes.plano_de_gestao + " escolhido.");
				}
			}
		}
		
		if (bean.getId() != null) {
			UnidadeGerencial subordinacao = bean.getSubordinacao();
			if (subordinacao != null && bean.getId().equals(subordinacao.getId())) {
				errors.reject("","Não é possível cadastrar uma unidade gerencial subordinada a ela mesma");
			}
			if (subordinacao != null) {
				if (unidadeGerencialService.isSubordinado(bean, subordinacao, bean.getPlanoGestao())) {
					errors.reject("","A subordinação escolhida já é subordinada a essa unidade gerencial.");
				}
			}
		}
		
		super.validateBean(bean, errors);
	}

	@Override
	protected void salvar(WebRequestContext request, UnidadeGerencial bean) throws Exception {
		
		try {
			UnidadeGerencial unidadeGerencialPai = unidadeGerencialService.getPai(bean);
			if (unidadeGerencialPai != null) {
				bean.setNivelNum(unidadeGerencialPai.getNivelNum() + 1);
			}
			
			unidadeGerencialService.saveOrUpdate(bean);
			
			// Atualiza os níveis das unidades gerenciais envolvidas.
			List<UnidadeGerencial> listaUnidadeGerencial = new ArrayList<UnidadeGerencial>();
			listaUnidadeGerencial = unidadeGerencialService.getListaDescendencia(bean, listaUnidadeGerencial);
			unidadeGerencialService.atualizaUnidadeGerencialNivelNum(listaUnidadeGerencial);
			
			request.addMessage("Unidade gerencial salva com sucesso.", MessageType.INFO);
		} 
		catch (Exception e) {
			request.addError(e.getMessage());
		}
	}
	
	@Override
	protected ListagemResult<UnidadeGerencial> getLista(WebRequestContext request, UnidadeGerencialFiltro filtro) {
		ListagemResult<UnidadeGerencial> result = super.getLista(request, filtro);
		List<UnidadeGerencial> listaUnidadeGerencial = result.list();
		
		Boolean hasAuthMapaNegocioReport = Neo.getApplicationContext().getAuthorizationManager().isAuthorized("/sgm/report/MapaNegocio", "generate", Neo.getUser());
		
		Boolean isUsuarioAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (listaUnidadeGerencial != null) {
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencial) {
				//Verifica se a ug pode ser editada
				if (isUsuarioAdmin) {
					unidadeGerencial.setPodeEditar(true);
				}
				else {
					unidadeGerencial.setPodeEditar(usuarioService.isUsuarioLogadoParticipanteUG(unidadeGerencial));
				}
				
				//Define se o botão de impressão do mapa do negócio será exibido
				unidadeGerencial.setPodeImprimirMapaNegocio(hasAuthMapaNegocioReport);
			}
		}		
		return result;
	}
	
	@Override
	public ModelAndView doListagem(WebRequestContext request, UnidadeGerencialFiltro filtro) throws CrudException {
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);		

		return super.doListagem(request, filtro);
	}
	
	@Override
	protected void entrada(WebRequestContext request, UnidadeGerencial form) throws Exception {
		
		/*** Caso o plano de gestão esteja nulo, seta o atual ***/
		if (form.getPlanoGestao() == null) {
			form.setPlanoGestao(planoGestaoService.obtemPlanoGestaoAtual());
		}
		
		request.setAttribute("listaNaoAdministradores", usuarioService.findNaoAdministradores());

		super.entrada(request, form);
	}
}
