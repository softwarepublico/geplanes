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
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.controller.filtro.IniciativaPlanoAcaoFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.PlanoAcaoService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;

@Bean
@Controller(path="/sgm/process/IniciativaPlanoAcao", authorizationModule=ProcessAuthorizationModule.class)
public class IniciativaPlanoAcaoProcess extends MultiActionController {

	private PlanoAcaoService planoAcaoService;
	private UsuarioService usuarioService;
	
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {this.planoAcaoService = planoAcaoService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	
	@DefaultAction	
    public ModelAndView executar(WebRequestContext request, IniciativaPlanoAcaoFiltro filtro) {
		
		boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		boolean usuarioLogadoIsParticipanteUG;
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}		
		
		if (filtro.getPlanoGestao() != null) {
			filtro.setListaPlanoAcao(planoAcaoService.findByIniciativas(filtro));
			
			if (filtro.getListaPlanoAcao() != null) {
				for (PlanoAcao planoAcao : filtro.getListaPlanoAcao()) {
					usuarioLogadoIsParticipanteUG = usuarioService.isUsuarioLogadoParticipanteUG(planoAcao.getUnidadeGerencial());
					
					if (usuarioLogadoIsAdmin || usuarioLogadoIsParticipanteUG) {
						planoAcao.setPodeEditarStatus(true);
					}
					else {
						planoAcao.setPodeEditarStatus(false);
					}
				}
			}
		}
		request.setAttribute("USUARIOADMIN", usuarioLogadoIsAdmin);
		return new ModelAndView("process/iniciativaPlanoAcao", "filtro", filtro);
	}
	
	public ModelAndView salvar(WebRequestContext request, IniciativaPlanoAcaoFiltro filtro) {
		if (filtro != null && filtro.getListaPlanoAcao() != null && !filtro.getListaPlanoAcao().isEmpty()) {
			try {
				for (PlanoAcao planoAcao : filtro.getListaPlanoAcao()) {
					planoAcaoService.saveOrUpdate(planoAcao);
				}
				request.addMessage("Registro(s) salvo(s) com sucesso.");
			}
			catch (GeplanesException e) {
				request.addError(e.getMessage());
			}
		}
		return executar(request, filtro);
	}
}
