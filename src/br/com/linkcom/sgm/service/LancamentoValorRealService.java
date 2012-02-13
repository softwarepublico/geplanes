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
package br.com.linkcom.sgm.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.controller.filtro.LancamentoValorRealFiltro;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;


@Bean
public class LancamentoValorRealService{

	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private UsuarioService usuarioService;
	private PlanoGestaoService planoGestaoService;
	private UnidadeGerencialService unidadeGerencialService;
	private SolicitacaoRepactuacaoIndicadorService solicitacaoRepactuacaoIndicadorService;

	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;}
	public void setSolicitacaoRepactuacaoIndicadorService(SolicitacaoRepactuacaoIndicadorService solicitacaoRepactuacaoIndicadorService) {this.solicitacaoRepactuacaoIndicadorService = solicitacaoRepactuacaoIndicadorService;}
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	
	
	public LancamentoValorRealFiltro pesquisar(LancamentoValorRealFiltro filtro){
				
		/*** retorna os objetivos estratégicos, os indicadores e seus acompanhamentos ***/
		filtro.setListaPerspectivaMapaEstrategico(perspectivaMapaEstrategicoService.montaArvorePlanejamentoEstrategico(filtro.getUnidadeGerencial(), true, false, false));
		
		/*** organiza os acompanhamentos ***/
		organizarAcompanhamentos(filtro.getListaPerspectivaMapaEstrategico(),filtro.getPlanoGestao());
						
		return filtro;
	}
	
	private void organizarAcompanhamentos(List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico, PlanoGestao planoGestao) {
		if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {					
								List<AcompanhamentoIndicador> l2 = acompanhamentoIndicadorService.obtemAcompanhamentos(indicador);
								LinkedHashSet<AcompanhamentoIndicador> l = (new LinkedHashSet<AcompanhamentoIndicador>(l2));
								indicador.setAcompanhamentosIndicador(l);
							}
						}						
					}
				}
			}
		}
	}
	
	/**
	 * @author Rodrigo Duarte 
	 * @param filtro
	 */
	public void configuraExibicaoAcompanhamentos(LancamentoValorRealFiltro filtro) {
		if (filtro.getListaPerspectivaMapaEstrategico() != null && !filtro.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : filtro.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
								if (indicador.getAcompanhamentosIndicador().size() > 0) {
									for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
										acompanhamentoIndicador.setPodeMostrar(acompanhamentoIndicadorService.podeMostrar(acompanhamentoIndicador));
									}
								}
							}
						}
					}
				}
			}	
		}
	}
	
		
	/**
	 * Percorre os objetivos estratégicos e seus indicadores e verifica, para cada acompanhamento
	 * se o mesmo pode ser alterado.
	 *  
	 * @param filtro
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @param isUsuarioApoioUG
	 */
	public void configuraEdicaoAcompanhamentos(LancamentoValorRealFiltro filtro, Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG) {
		
		Integer trimestreAcompanhamento;
		Boolean isDtTravLancResExp;
		
		if (filtro.getListaPerspectivaMapaEstrategico() != null && !filtro.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : filtro.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
								if(indicador.getAcompanhamentosIndicador().size() > 0){
									for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
										trimestreAcompanhamento = CalculosAuxiliares.getAcompanhamentoTrimestre(indicador.getFrequencia(), acompanhamentoIndicador.getIndice());
										isDtTravLancResExp = planoGestaoService.isDtTravLancResultadosExpirada(filtro.getPlanoGestao(), trimestreAcompanhamento);
										acompanhamentoIndicador.setPodeAlterar(acompanhamentoIndicadorService.podeAlterar(acompanhamentoIndicador, isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG, isDtTravLancResExp));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param filtro
	 */
	public void salvar(LancamentoValorRealFiltro filtro){
		if (filtro.getListaPerspectivaMapaEstrategico() != null && !filtro.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : filtro.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
								if (indicador.getAcompanhamentosIndicador().size() > 0) {
									for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
										acompanhamentoIndicadorService.updateValorReal(acompanhamentoIndicador, acompanhamentoIndicador.getValorReal());
									}
								}
							}
						}
					}
				}
			}
		}
	}
		

	/**
	 * @author Rodrigo Duarte
	 * @param filtro
	 */
	public void somarValoresAcumulados(LancamentoValorRealFiltro filtro) {
		if (filtro.getListaPerspectivaMapaEstrategico() != null && !filtro.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : filtro.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
								CalculosAuxiliares.calculaAcumulados( new ArrayList<AcompanhamentoIndicador>( indicador.getAcompanhamentosIndicador()));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Habilita ou não a exibição do botão de cancelamento para cada um dos indicadores do filtro.
	 * O botão só será exibido se o usuário for administrador ou participante da unidade gerencial 
	 * e também o indicador esteja com o status diferente de EM_CANCELAMENTO ou CANCELADO.
	 *  
	 * @author Rodrigo Alvarenga 
	 * @param filtro
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @param isUsuarioApoioUG
	 */
	public void configuraBotaoSolicitacaoCancelamentoIndicador(LancamentoValorRealFiltro filtro, Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG) {	
		if (filtro.getListaPerspectivaMapaEstrategico() != null && !filtro.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : filtro.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
								if (!isUsuarioAdmin && !isUsuarioResponsavelUG) {
									indicador.setPodeCancelar(false);
								}
								else {
									indicador.setPodeCancelar(true);
								}						
							}
						}						
					}
				}
			}
		}
	}	
	
	public void configuraParametrosTela(WebRequestContext request, LancamentoValorRealFiltro filtro) {
		Boolean isUsuarioAdmin         = usuarioService.isUsuarioLogadoAdmin();
		Boolean isUsuarioResponsavelUG = usuarioService.isUsuarioLogadoResponsavelUG(filtro.getUnidadeGerencial());
		Boolean isUsuarioApoioUG       = usuarioService.isUsuarioLogadoApoioUG(filtro.getUnidadeGerencial());
		Boolean isUsuarioParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(filtro.getUnidadeGerencial());		
		
		Boolean usuarioTemPermissaoLeitura  = usuarioTemPermissaoLeitura(isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG, isUsuarioParticipanteUGAncestral);
		
		if (!usuarioTemPermissaoLeitura) {
			request.addMessage("Você não tem permissão para acessar os dados dessa Unidade Gerencial.", MessageType.WARN);
		}
		else {
			
			/***  Carrega os dados do plano de gestão ***/
			filtro.setPlanoGestao(planoGestaoService.load(filtro.getPlanoGestao()));			
			
			/*** Define se o botão para cancelar o indicador será mostrado ou não. ***/
			configuraBotaoSolicitacaoCancelamentoIndicador(filtro, isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG);
			
			/*** Define se o botão para repactuar o indicador será mostrado ou não. ***/
			configuraBotaoSolicitacaoRepactuacaoIndicador(filtro, isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG);
			
			/*** Define se o acompanhamento vai ser mostrado. (se foi aprovado) ***/
			configuraExibicaoAcompanhamentos(filtro);
			
			/*** Define se o acompanhamento pode ser alterado ***/
			configuraEdicaoAcompanhamentos(filtro,isUsuarioAdmin,isUsuarioResponsavelUG,isUsuarioApoioUG);			
			
			/*** Altera os nomes das épocas de acordo com a frequência dos indicadores ***/				
			CalculosAuxiliares.nomeiaEpocasPorObjetivoEstrategico(filtro.getListaPerspectivaMapaEstrategico());
			
			request.setAttribute("showBotaoSalvar", showBotaoSalvar(filtro));
			request.setAttribute("usuarioAdministrador", isUsuarioAdmin);		
			request.setAttribute("usuarioResponsavel", isUsuarioResponsavelUG);
			request.setAttribute("usuarioPodeCriarAnomalia", unidadeGerencialService.usuarioPodeCriarAnomalia(isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG));
			request.setAttribute("usuarioPodeCriarAcaoPreventiva", unidadeGerencialService.usuarioPodeCriarAcaoPreventiva(isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG));
		}
		
		request.setAttribute("showTela", usuarioTemPermissaoLeitura);		
	}
	
	private void configuraBotaoSolicitacaoRepactuacaoIndicador(LancamentoValorRealFiltro filtro, Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG) {
		if (filtro.getListaPerspectivaMapaEstrategico() != null && !filtro.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : filtro.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {		
								if (!isUsuarioAdmin && !isUsuarioResponsavelUG) {
									indicador.setPodeRepactuar(false);
								}
								else {
									indicador.setPodeRepactuar(true);
								}			
								indicador.setRepactuacaoEmAprovacao(solicitacaoRepactuacaoIndicadorService.existeSolicitacaoRepactuacaoAberta(indicador));
							}
						}						
					}
				}
			}
		}
	}
	
	/**
	 * Verifica se o usuário tem permissão de leitura aos registros de lançamento de valores base. Somente terão essa permissão
	 * os administradores e os usuários vinculados à UG da meta, sejam eles responsáveis ou de apoio. Além disso, os usuários participantes
	 * das UGs ancestrais da UG da meta também terão acesso.
	 * 
	 * @author Rodrigo Alvarenga 
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @param isUsuarioApoioUG
	 * @param isUsuarioParticipanteUGAncestral
	 * @return verdadeiro ou falso
	 */	
	private Boolean usuarioTemPermissaoLeitura(Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG, Boolean isUsuarioParticipanteUGAncestral) {
		if (isUsuarioAdmin) {
			return true;
		}		
		if (isUsuarioResponsavelUG) {
			return true;
		}		
		if (isUsuarioApoioUG) {
			return true;
		}		
		if (isUsuarioParticipanteUGAncestral) {
			return true;
		}		
		return false;
	}
	
	/**
	 * Retorna se o botão salvar pode ser exibido. Para isso, verifica:
	 *  - se a tela está no modo valores apurados ou acumulados
	 *  - se existem indicadores
	 *  - se existem acompanhamentos de indicadores que possam ser exibidos e alterados
	 * 
	 * @author Rodrigo Alvarenga
	 * @param filtro
	 * @return
	 */
	public Boolean showBotaoSalvar(LancamentoValorRealFiltro filtro) {
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = filtro.getListaPerspectivaMapaEstrategico();
		
		if (filtro.getAlternar() != null && filtro.getAlternar()) {
			return false;
		}
		
		if (listaPerspectivaMapaEstrategico.isEmpty()) {
			return false;
		}
		
		Boolean existeAcompanhamentoIndicador = false;
		Boolean existeAcompanhamentoIndicadorVisivel = false;
		Boolean existeAcompanhamentoIndicadorEditavel = false;

		if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
								if (!indicador.getAcompanhamentosIndicador().isEmpty()) {
									existeAcompanhamentoIndicador = true;
									for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
										if (acompanhamentoIndicador.getPodeMostrar() != null && acompanhamentoIndicador.getPodeMostrar()) {
											existeAcompanhamentoIndicadorVisivel = true;
										}
										if (acompanhamentoIndicador.getPodeAlterar() != null && acompanhamentoIndicador.getPodeAlterar()) {
											existeAcompanhamentoIndicadorEditavel = true;
										}									
									}							
								}
							}
						}
					}
				}
			}
		}
		
		return existeAcompanhamentoIndicador && existeAcompanhamentoIndicadorVisivel && existeAcompanhamentoIndicadorEditavel;
	}	
}
