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

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Command;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.DefinicaoEstrategiaFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.IniciativaService;
import br.com.linkcom.sgm.service.MapaEstrategicoService;
import br.com.linkcom.sgm.service.MapaNegocioService;
import br.com.linkcom.sgm.service.MatrizFCSService;
import br.com.linkcom.sgm.service.PerspectivaMapaEstrategicoService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Bean
@Controller(path="/sgm/process/DefinicaoEstrategia", authorizationModule=ProcessAuthorizationModule.class)
public class DefinicaoEstrategiaProcess extends MultiActionController {
	
	private UnidadeGerencialService unidadeGerencialService;
	private PlanoGestaoService planoGestaoService;
	private UsuarioService usuarioService;
	private MapaEstrategicoService mapaEstrategicoService;
	private MapaNegocioService mapaNegocioService;
	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;
	private MatrizFCSService matrizFCSService;
	private IndicadorService indicadorService;
	private IniciativaService iniciativaService;	
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setMapaEstrategicoService(MapaEstrategicoService mapaEstrategicoService) {
		this.mapaEstrategicoService = mapaEstrategicoService;
	}
	
	public void setMapaNegocioService(MapaNegocioService mapaNegocioService) {
		this.mapaNegocioService = mapaNegocioService;
	}
	
	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {
		this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;
	}
	
	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {
		this.matrizFCSService = matrizFCSService;
	}
	
	public void setIndicadorService(IndicadorService indicadorService) {
		this.indicadorService = indicadorService;
	}
	
	public void setIniciativaService(IniciativaService iniciativaService) {
		this.iniciativaService = iniciativaService;
	}	
	
	@DefaultAction	
    public ModelAndView entrada(WebRequestContext request, DefinicaoEstrategiaFiltro filtro) {
		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);		
		}
		
		if (filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
			
			UnidadeGerencial unidadeGerencial = unidadeGerencialService.loadWithMapaEstrategico(filtro.getUnidadeGerencial());
			
			MapaEstrategico mapaEstrategico;
			if (unidadeGerencial.getMapaEstrategico() != null && unidadeGerencial.getMapaEstrategico().getId() != null) {
				mapaEstrategico = unidadeGerencial.getMapaEstrategico();
				
				//Lista de todas as perspectivas de um determinado Mapa Estratégico.
				List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoService.findByMapaEstrategico(unidadeGerencial.getMapaEstrategico());
				mapaEstrategico.setListaPerspectivaMapaEstrategico(listaPerspectivaMapaEstrategico);
			}
			else {
				// Caso ainda não exista o mapa estratégico cadastrado para a UG,
				// verifica se já foi cadastrada a matriz de iniciativas x fcs para a UG.
				// Caso afirmativo, o cadastro foi feito baseado no mapa estratégico de uma UG superior.
				// Nesse caso, deve-se impedir o cadastro do mapa estratégico para a UG em questão.
				List<MatrizFCS> listaMatrizFCS = matrizFCSService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, null);
				if (listaMatrizFCS != null && !listaMatrizFCS.isEmpty()) {
					request.setAttribute("SEMPERMISSAO", true);
					request.addMessage("Não é permitido o cadastro do mapa estratégico para essa unidade gerencial, pois a matriz de iniciativas x fcs já foi criada para os objetivos definidos no mapa estratégico de uma unidade gerencial superior.", MessageType.ERROR);
					return new ModelAndView("process/definicaoEstrategia", "filtro", filtro);
				}
				
				// Caso ainda não exista o mapa estratégico cadastrado para a UG,
				// verifica se já foi cadastrado algum indicador para a UG.
				// Caso afirmativo, o cadastro foi feito baseado no mapa estratégico de uma UG superior.
				// Nesse caso, deve-se impedir o cadastro do mapa estratégico para a UG em questão.
				List<Indicador> listaIndicador = indicadorService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, null, true, false, false, false);
				if (listaIndicador != null && !listaIndicador.isEmpty()) {
					request.setAttribute("SEMPERMISSAO", true);
					request.addMessage("Não é permitido o cadastro do mapa estratégico para essa unidade gerencial, pois já existe(m) indicador(es) cadastrado(s) para os objetivos definidos no mapa estratégico de uma unidade gerencial superior.", MessageType.ERROR);
					return new ModelAndView("process/definicaoEstrategia", "filtro", filtro);
				}
				
				// Caso ainda não exista o mapa estratégico cadastrado para a UG,
				// verifica se já foi cadastrado alguma iniciativa para a UG.
				// Caso afirmativo, o cadastro foi feito baseado no mapa estratégico de uma UG superior.
				// Nesse caso, deve-se impedir o cadastro do mapa estratégico para a UG em questão.
				List<Iniciativa> listaIniciativa = iniciativaService.findByUnidadeGerencial(unidadeGerencial);
				if (listaIniciativa != null && !listaIniciativa.isEmpty()) {
					request.setAttribute("SEMPERMISSAO", true);
					request.addMessage("Não é permitido o cadastro do mapa estratégico para essa unidade gerencial, pois já existe(m) iniciativa(s) cadastrada(s) para os objetivos definidos no mapa estratégico de uma unidade gerencial superior.", MessageType.ERROR);
					return new ModelAndView("process/definicaoEstrategia", "filtro", filtro);
				}
				
				mapaEstrategico = new MapaEstrategico();
			}
			
			// Busca o campo missão vindo do cadastro do mapa do negócio.
			mapaEstrategico.setMissao(mapaNegocioService.loadMissaoByUnidadeGerencial(unidadeGerencial));
			
			unidadeGerencial.setMapaEstrategico(mapaEstrategico);
			filtro.setUnidadeGerencial(unidadeGerencial);
			
			boolean podeCadastrarMapa = unidadeGerencial.getPermitirMapaEstrategico() != null ? unidadeGerencial.getPermitirMapaEstrategico() : false;
			boolean dtLimCrMapEstExpirada = !(planoGestaoService.dataLimiteCriacaoMapaEstrategicoNaoExpirada(unidadeGerencial.getPlanoGestao()));
			boolean usuarioLogadoParticipanteUG = usuarioService.isUsuarioLogadoParticipanteUG(unidadeGerencial);
			boolean usuarioLogadoParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(unidadeGerencial);
			boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
			
			if (podeCadastrarMapa) {
				if (usuarioLogadoParticipanteUG || usuarioLogadoParticipanteUGAncestral || usuarioLogadoIsAdmin) {
					if (dtLimCrMapEstExpirada) {
						if (usuarioLogadoIsAdmin) {
							request.addMessage("A data limite para criação do mapa estratégico está ultrapassada.", MessageType.WARN);
						}
						else {
							request.addMessage("A data limite para criação do mapa estratégico está ultrapassada.", MessageType.ERROR);
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
				request.addMessage("Não é permitido o cadastro do mapa estratégico para essa unidade gerencial.", MessageType.ERROR);
			}
		}
				
		return new ModelAndView("process/definicaoEstrategia", "filtro", filtro);
    }
	
	@Command(validate=true)
	@Input("error")
	public ModelAndView salvar(WebRequestContext request, DefinicaoEstrategiaFiltro filtro) {
		if (filtro.getUnidadeGerencial() != null) {
			try {
				UnidadeGerencial unidadeGerencial = filtro.getUnidadeGerencial();
				mapaEstrategicoService.salvaDefinicaoObjetivoEstrategico(unidadeGerencial.getMapaEstrategico(), unidadeGerencial, true);
				request.addMessage("Registro salvo com sucesso.");
			}
			catch (GeplanesException e) {
				request.addError(e.getMessage());
			}
		}
		return continueOnAction("entrada", new DefinicaoEstrategiaFiltro());
	}
	
	@Command(validate=true)
	@Input("error")
	public ModelAndView excluir(WebRequestContext request, DefinicaoEstrategiaFiltro filtro) throws Exception {
		MapaEstrategico mapaEstrategico = filtro.getUnidadeGerencial().getMapaEstrategico();
		
		if (mapaEstrategico != null) {
			try {
				mapaEstrategicoService.excluiMapaEstrategico(mapaEstrategico);
				request.addMessage("Mapa estratégico excluído com sucesso!");
			}
			catch (GeplanesException e) {
				request.addError(e.getMessage());
			}
		}

		return continueOnAction("entrada", new DefinicaoEstrategiaFiltro());
	}	
	
	@Override
	protected void validate(Object obj, BindException errors, String acao) {
		DefinicaoEstrategiaFiltro filtro = (DefinicaoEstrategiaFiltro) obj;
		UnidadeGerencial unidadeGerencial = filtro.getUnidadeGerencial();
		
		boolean cadastroCompleto = false;
		// Verifica se foi inserido pelo menos um objetivo estratégico.
		if (unidadeGerencial.getMapaEstrategico() != null && unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico() != null && !unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					cadastroCompleto = true;
				}
			}
		}
		
		if (!cadastroCompleto) {
			errors.reject("","É necessário cadastrar pelo menos um objetivo estratégico no mapa.");
		}
		super.validate(obj, errors, acao);
	}	
	
	public ModelAndView error(WebRequestContext request, DefinicaoEstrategiaFiltro filtro) {
    	return new ModelAndView("process/definicaoEstrategia", "filtro", filtro);
    }	
}