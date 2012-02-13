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
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.MapaNegocio;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.PlanoGestaoUnidadeGerencialFiltro;
import br.com.linkcom.sgm.service.MapaNegocioService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Bean
@Controller(path="/sgm/process/DefinicaoNegocio", authorizationModule=ProcessAuthorizationModule.class)
public class DefinicaoNegocioProcess extends MultiActionController {
	private UsuarioService usuarioService;
	private UnidadeGerencialService unidadeGerencialService;
	private PlanoGestaoService planoGestaoService;
	private MapaNegocioService mapaNegocioService;
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	public void setMapaNegocioService(MapaNegocioService mapaNegocioService) {
		this.mapaNegocioService = mapaNegocioService;
	}
	
	@DefaultAction	
    public ModelAndView entrada(WebRequestContext request, PlanoGestaoUnidadeGerencialFiltro filtro) {
		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);		
		}
		
		if (filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
			UnidadeGerencial unidadeGerencial = unidadeGerencialService.loadWithPlanoGestao(filtro.getUnidadeGerencial());
			
			MapaNegocio mapaNegocio = mapaNegocioService.loadByUnidadeGerencial(unidadeGerencial);
			if (mapaNegocio == null) {
				mapaNegocio = new MapaNegocio();
			}
			mapaNegocio.setUnidadeGerencial(unidadeGerencial);
			unidadeGerencial.setMapaNegocio(mapaNegocio);			
			request.setAttribute("unidadeGerencial", unidadeGerencial);
			
			boolean podeCadastrarMapa = unidadeGerencial.getPermitirMapaNegocio() != null ? unidadeGerencial.getPermitirMapaNegocio() : false;
			boolean dtLimCrMapCompExpirada = !(planoGestaoService.dataLimiteCriacaoMapaNegocioNaoExpirada(unidadeGerencial.getPlanoGestao()));
			boolean usuarioLogadoParticipanteUG = usuarioService.isUsuarioLogadoParticipanteUG(unidadeGerencial);
			boolean usuarioLogadoParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(unidadeGerencial);
			boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
			
			if (podeCadastrarMapa) {
				if (usuarioLogadoParticipanteUG || usuarioLogadoParticipanteUGAncestral || usuarioLogadoIsAdmin) {
					if (dtLimCrMapCompExpirada) {
						if (usuarioLogadoIsAdmin) {
							request.addMessage("A data limite para criação do mapa do negócio está ultrapassada.", MessageType.WARN);
						}
						else {
							request.addMessage("A data limite para criação do mapa do negócio está ultrapassada.", MessageType.ERROR);
							request.setAttribute("HIDEBOTAOSALVAR", Boolean.TRUE);							
						}
					}
					else {
						request.setAttribute("SEMPERMISSAO", false);
					}
				}
				else {
					request.setAttribute("SEMPERMISSAO", true);
					request.addMessage("Você não tem permissão para acessar os dados dessa unidade gerencial.", MessageType.ERROR);
				}
			}
			else {
				request.setAttribute("SEMPERMISSAO", true);
				request.addMessage("Não é permitido o cadastro do mapa do negócio para essa unidade gerencial.", MessageType.ERROR);
			}
		}
				
		return new ModelAndView("process/definicaoNegocio", "filtro", filtro);
    }
	
	public ModelAndView salvar(WebRequestContext request, UnidadeGerencial unidadeGerencial) {
		if (unidadeGerencial != null && unidadeGerencial.getMapaNegocio() != null) {
			mapaNegocioService.saveOrUpdate(unidadeGerencial.getMapaNegocio());
			request.addMessage("Registro salvo com sucesso.");
		}
		return continueOnAction("entrada", new PlanoGestaoUnidadeGerencialFiltro());
	}	
}