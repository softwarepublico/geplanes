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
import br.com.linkcom.sgm.beans.MapaCompetencia;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.PlanoGestaoUnidadeGerencialFiltro;
import br.com.linkcom.sgm.service.AtividadeService;
import br.com.linkcom.sgm.service.CompetenciaService;
import br.com.linkcom.sgm.service.MapaCompetenciaService;
import br.com.linkcom.sgm.service.MapaNegocioService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Bean
@Controller(path="/sgm/process/DefinicaoCompetencia", authorizationModule=ProcessAuthorizationModule.class)
public class DefinicaoCompetenciaProcess extends MultiActionController {
	
	private UnidadeGerencialService unidadeGerencialService;
	private UsuarioService usuarioService;
	private MapaCompetenciaService mapaCompetenciaService;
	private AtividadeService atividadeService;
	private CompetenciaService competenciaService;
	private PlanoGestaoService planoGestaoService;
	private MapaNegocioService mapaNegocioService;
	
	public void setCompetenciaService(CompetenciaService competenciaService) {this.competenciaService = competenciaService;}
	public void setAtividadeService(AtividadeService atividadeService) {this.atividadeService = atividadeService;}
	public void setMapaCompetenciaService(MapaCompetenciaService mapaCompetenciaService) {this.mapaCompetenciaService = mapaCompetenciaService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setMapaNegocioService(MapaNegocioService mapaNegocioService) {this.mapaNegocioService = mapaNegocioService;}
	
	@DefaultAction	
    public ModelAndView entrada(WebRequestContext request, PlanoGestaoUnidadeGerencialFiltro filtro) {
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}	
		
		if (filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
			
			UnidadeGerencial unidadeGerencial = unidadeGerencialService.loadWithMapaCompetencia(filtro.getUnidadeGerencial());
			
			MapaCompetencia mapaCompetencia;
			if (unidadeGerencial.getMapaCompetencia() != null && unidadeGerencial.getMapaCompetencia().getId() != null) {
				mapaCompetencia = unidadeGerencial.getMapaCompetencia();
				mapaCompetencia.setAtividades(atividadeService.findByMapaCompetencia(mapaCompetencia));
				mapaCompetencia.setCompetencias(competenciaService.findByMapaCompetencia(mapaCompetencia));
			} 
			else {
				mapaCompetencia = new MapaCompetencia();
			}
			
			// Busca o campo missão vindo do cadastro do mapa do negócio.
			mapaCompetencia.setMissao(mapaNegocioService.loadMissaoByUnidadeGerencial(unidadeGerencial));			
			
			unidadeGerencial.setMapaCompetencia(mapaCompetencia);
			request.setAttribute("unidadeGerencial", unidadeGerencial);
			
			boolean podeCadastrarMapa = unidadeGerencial.getPermitirMapaCompetencia() != null ? unidadeGerencial.getPermitirMapaCompetencia() : false;
			boolean dtLimCrMapNegExpirada = !(planoGestaoService.dataLimiteCriacaoMapaCompetenciaNaoExpirada(unidadeGerencial.getPlanoGestao()));			
			boolean usuarioLogadoParticipanteUG = usuarioService.isUsuarioLogadoParticipanteUG(unidadeGerencial);
			boolean usuarioLogadoParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(unidadeGerencial);
			boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
			
			if (podeCadastrarMapa) {
				if (usuarioLogadoParticipanteUG || usuarioLogadoParticipanteUGAncestral || usuarioLogadoIsAdmin) {
					if (dtLimCrMapNegExpirada) {
						if (usuarioLogadoIsAdmin) {
							request.addMessage("A data limite para criação do mapa de competências está ultrapassada.", MessageType.WARN);
						}
						else {
							request.addMessage("A data limite para criação do mapa de competências está ultrapassada.", MessageType.ERROR);
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
				request.addMessage("Não é permitido o cadastro do mapa de competências para essa unidade gerencial.", MessageType.ERROR);
			}
		}
				
		return new ModelAndView("process/definicaoCompetencia", "filtro", filtro);
    }
	
	public ModelAndView salvar(WebRequestContext request, UnidadeGerencial unidadeGerencial){
		if (unidadeGerencial != null) {			
			mapaCompetenciaService.salvarDefinicaoCompetencia(unidadeGerencial);
			request.addMessage("Registro salvo com sucesso.");
		}
		return continueOnAction("entrada", new PlanoGestaoUnidadeGerencialFiltro());
	}		
	
}