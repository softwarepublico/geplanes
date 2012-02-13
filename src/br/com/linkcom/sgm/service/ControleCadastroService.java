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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.ControleCadastro;
import br.com.linkcom.sgm.beans.ControleCadastroItem;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.MapaCompetencia;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.MapaNegocio;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.ItemControleCadastroEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;
import br.com.linkcom.sgm.controller.filtro.IniciativaPlanoAcaoFiltro;
import br.com.linkcom.sgm.util.GeplanesUtils;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;

@Bean
public class ControleCadastroService {
	private PlanoGestaoService planoGestaoService;
	private UnidadeGerencialService unidadeGerencialService;
	private MapaNegocioService mapaNegocioService;
	private MapaEstrategicoService mapaEstrategicoService;
	private MapaCompetenciaService mapaCompetenciaService;
	private MatrizFCSService matrizFCSService;
	private IndicadorService indicadorService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private AnomaliaService anomaliaService;
	private PlanoAcaoService planoAcaoService;
	private IniciativaService iniciativaService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setMapaNegocioService(MapaNegocioService mapaNegocioService) {
		this.mapaNegocioService = mapaNegocioService;
	}
	
	public void setMapaEstrategicoService(MapaEstrategicoService mapaEstrategicoService) {
		this.mapaEstrategicoService = mapaEstrategicoService;
	}
	
	public void setMapaCompetenciaService(MapaCompetenciaService mapaCompetenciaService) {
		this.mapaCompetenciaService = mapaCompetenciaService;
	}
	
	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {
		this.matrizFCSService = matrizFCSService;
	}
	
	public void setIndicadorService(IndicadorService indicadorService) {
		this.indicadorService = indicadorService;
	}
	
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {
		this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;
	}
	
	public void setAnomaliaService(AnomaliaService anomaliaService) {
		this.anomaliaService = anomaliaService;
	}
	
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {
		this.planoAcaoService = planoAcaoService;
	}
	
	public void setIniciativaService(IniciativaService iniciativaService) {
		this.iniciativaService = iniciativaService;
	}
	
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {
		this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;
	}

	/**
	 * Percorre as unidades gerenciais selecionadas e verifica se existe
	 * pendência em cada item do controle de cadastro.
	 * 
	 * @param planoGestao
	 * @param listaUnidadeGerencial
	 * @return
	 */
	public List<ControleCadastro> geraListaControleCadastro(PlanoGestao planoGestao, List<UnidadeGerencial> listaUnidadeGerencial) {
		
		//Listas
		List<ControleCadastro> listaControleCadastro = new ArrayList<ControleCadastro>();
		List<ControleCadastroItem> listaControleCadastroItem;
		List<Indicador> listaIndicador;
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
		
		//Beans
		ControleCadastro controleCadastro;
		ControleCadastroItem controleCadastroItemIndicador;
		ControleCadastroItem controleCadastroItemValorBase;
		ControleCadastroItem controleCadastroItemValorReal;
		ControleCadastroItem controleCadastroItemIniciativa;
		IniciativaPlanoAcaoFiltro iniciativaPlanoAcaoFiltro;
		
		//Geral
		Boolean cadastroValorBaseNaoOK;
		Boolean cadastroValorRealNaoOK;
		Integer trimestreAcompanhamento;
		Boolean isDtLimLancResExp;		
		
		if (listaUnidadeGerencial != null) {
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencial) {
				unidadeGerencial = unidadeGerencialService.load(unidadeGerencial);

				controleCadastro = new ControleCadastro();
				controleCadastro.setPlanoGestao(planoGestao);
				controleCadastro.setUnidadeGerencial(unidadeGerencial);
				
				listaControleCadastroItem = new ArrayList<ControleCadastroItem>();
				
				try {
					HttpServletRequest servletRequest = NeoWeb.getRequestContext().getServletRequest();
					
					// Mapa do Negócio					
					listaControleCadastroItem.add(montaItemControleCadastroMapaNegocio(unidadeGerencial, servletRequest));
					
					// Mapa Estratégico
					listaControleCadastroItem.add(montaItemControleCadastroMapaEstrategico(unidadeGerencial, servletRequest));
					
					// Mapa de Competências
					listaControleCadastroItem.add(montaItemControleCadastroMapaCompetencia(unidadeGerencial, servletRequest));
					
					// Matriz de Iniciativas x Fatores Críticos de Sucesso
					listaControleCadastroItem.add(montaItemControleCadastroMatrizFCS(unidadeGerencial, servletRequest));
					
					//Verifica se existe pelo menos um indicador cadastrado e ativo para a ug
					listaIndicador = indicadorService.findAtivosByUnidadeGerencial(unidadeGerencial);
					
					controleCadastroItemIndicador = new ControleCadastroItem();
					controleCadastroItemIndicador.setItemControleCadastroEnum(ItemControleCadastroEnum.INDICADORES);
					controleCadastroItemIndicador.setDescricao(ItemControleCadastroEnum.INDICADORES.getDescricao());
					controleCadastroItemIndicador.setUrl(ItemControleCadastroEnum.INDICADORES.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
					controleCadastroItemIndicador.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.INDICADORES.getPath(), "execute", servletRequest));					
					
					if (listaIndicador == null || listaIndicador.isEmpty()) {
						controleCadastroItemIndicador.setPendente(true);
						listaControleCadastroItem.add(controleCadastroItemIndicador);
						
						controleCadastroItemValorBase = new ControleCadastroItem();
						controleCadastroItemValorBase.setItemControleCadastroEnum(ItemControleCadastroEnum.VALORES_BASE);
						controleCadastroItemValorBase.setPendente(null);
						listaControleCadastroItem.add(controleCadastroItemValorBase);
						
						controleCadastroItemValorReal = new ControleCadastroItem();
						controleCadastroItemValorReal.setItemControleCadastroEnum(ItemControleCadastroEnum.VALORES_REAIS);
						controleCadastroItemValorReal.setPendente(null);
						listaControleCadastroItem.add(controleCadastroItemValorReal);
					}
					else {
						controleCadastroItemIndicador.setPendente(false);
						listaControleCadastroItem.add(controleCadastroItemIndicador);
						
						//Para cada indicador, verifica se existem todos os lançamentos de valores a serem alcançados.
						for (Indicador indicador : listaIndicador) {
							
							cadastroValorBaseNaoOK = null;
							cadastroValorRealNaoOK = null;								
							
							listaAcompanhamentoIndicador = acompanhamentoIndicadorService.obtemAcompanhamentos(indicador);
							
							controleCadastroItemValorBase = new ControleCadastroItem();
							controleCadastroItemValorBase.setItemControleCadastroEnum(ItemControleCadastroEnum.VALORES_BASE);
							controleCadastroItemValorBase.setDescricao(ItemControleCadastroEnum.VALORES_BASE.getDescricao() + " para o indicador '" + indicador.getNome() + "'");
							controleCadastroItemValorBase.setUrl(ItemControleCadastroEnum.VALORES_BASE.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
							controleCadastroItemValorBase.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.VALORES_BASE.getPath(), "execute", servletRequest));							
							
							controleCadastroItemValorReal = new ControleCadastroItem();
							controleCadastroItemValorReal.setItemControleCadastroEnum(ItemControleCadastroEnum.VALORES_REAIS);
							controleCadastroItemValorReal.setDescricao(ItemControleCadastroEnum.VALORES_REAIS.getDescricao() + " para o indicador '" + indicador.getNome() + "'");
							controleCadastroItemValorReal.setUrl(ItemControleCadastroEnum.VALORES_REAIS.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
							controleCadastroItemValorReal.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.VALORES_REAIS.getPath(), "execute", servletRequest));							
							
							if (listaAcompanhamentoIndicador != null && !listaAcompanhamentoIndicador.isEmpty()) {
								for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoIndicador) {
									
									// Se o acompanhamento em questão não for aplicável, 
									// desconsidera os valores base e real passa para o próximo acompanhamento
									if (Boolean.TRUE.equals(acompanhamentoIndicador.getNaoaplicavel())) {
										continue;
									}
									else {
										cadastroValorBaseNaoOK = false;
										cadastroValorRealNaoOK = false;										
									}
									
									switch (indicador.getMelhor()) {
										case MELHOR_CIMA:
											if (acompanhamentoIndicador.getValorLimiteSuperior() == null) {
												cadastroValorBaseNaoOK = true;
												cadastroValorRealNaoOK = null;
											}											
											break;
											
										case MELHOR_ENTRE_FAIXAS:
											if (acompanhamentoIndicador.getValorLimiteInferior() == null || acompanhamentoIndicador.getValorLimiteSuperior() == null) {
												cadastroValorBaseNaoOK = true;
												cadastroValorRealNaoOK = null;
											}											
											break;
											
										case MELHOR_BAIXO:
											if (acompanhamentoIndicador.getValorLimiteInferior() == null) {
												cadastroValorBaseNaoOK = true;
												cadastroValorRealNaoOK = null;
											}											
											break;
	
										default:
											break;
									}
									
									if (Boolean.TRUE.equals(cadastroValorBaseNaoOK)) {
										break;
									}
									
									trimestreAcompanhamento = CalculosAuxiliares.getAcompanhamentoTrimestre(indicador.getFrequencia(), acompanhamentoIndicador.getIndice());
									isDtLimLancResExp = planoGestaoService.isDtLimLancResultadosExpirada(planoGestao, trimestreAcompanhamento);
									
									//Se tiver pelo menos um resultado que não tenha sido lançado e a data limite para lançamento esteja expirada, o cadastro não está ok
									if (acompanhamentoIndicador.getValorReal() == null && isDtLimLancResExp) {
										cadastroValorRealNaoOK = true;
										break;
									}
								}
							}
							else {
								cadastroValorBaseNaoOK = true;
								cadastroValorRealNaoOK = null;
							}
							
							controleCadastroItemValorBase.setPendente(cadastroValorBaseNaoOK);
							controleCadastroItemValorReal.setPendente(cadastroValorRealNaoOK);
							
							listaControleCadastroItem.add(controleCadastroItemValorBase);
							listaControleCadastroItem.add(controleCadastroItemValorReal);
						}
					}
					
					// Tratamento de Anomalias
					List<Anomalia> listaAnomalia = anomaliaService.findByUGResponsavelForControlePendencia(unidadeGerencial);
					if (listaAnomalia != null && !listaAnomalia.isEmpty()) {
						for (Anomalia anomalia : listaAnomalia) {
							listaControleCadastroItem.add(montaItemControleTratamentoAnomlia(anomalia, servletRequest));
						}
					}
					else {
						ControleCadastroItem controleCadastroItemTratamentoAnomalia = new ControleCadastroItem();
						controleCadastroItemTratamentoAnomalia.setItemControleCadastroEnum(ItemControleCadastroEnum.TRATAMENTO_ANOMALIA);
						controleCadastroItemTratamentoAnomalia.setPendente(null);
						listaControleCadastroItem.add(controleCadastroItemTratamentoAnomalia);
					}
					
					// Plano de Ação das Iniciativas - Busca as ações expiradas e não concluídas.
					List<StatusPlanoAcaoEnum> listaStatus = new ArrayList<StatusPlanoAcaoEnum>();
					listaStatus.add(StatusPlanoAcaoEnum.PLANEJADO);
					listaStatus.add(StatusPlanoAcaoEnum.EM_ANDAMENTO);
					List<PlanoAcao> listaPlanoAcao;
					List<Iniciativa> listaIniciativa = iniciativaService.findByUnidadeGerencial(unidadeGerencial);
					if (listaIniciativa != null && !listaIniciativa.isEmpty()) {
						for (Iniciativa iniciativa : listaIniciativa) {
							
							controleCadastroItemIniciativa = new ControleCadastroItem();
							controleCadastroItemIniciativa.setItemControleCadastroEnum(ItemControleCadastroEnum.PLANO_ACAO_INICIATIVA);
							controleCadastroItemIniciativa.setDescricao(ItemControleCadastroEnum.PLANO_ACAO_INICIATIVA.getDescricao() + "' " + iniciativa.getDescricao() + "'");
							controleCadastroItemIniciativa.setUrl(ItemControleCadastroEnum.PLANO_ACAO_INICIATIVA.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId()+"&objetivoMapaEstrategico.id=" + iniciativa.getObjetivoMapaEstrategico().getId() + "&matrizFCSIniciativa.id=" + iniciativa.getId());
							controleCadastroItemIniciativa.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.PLANO_ACAO_INICIATIVA.getPath(), "execute", servletRequest));							
							controleCadastroItemIniciativa.setPendente(false);

							iniciativaPlanoAcaoFiltro = new IniciativaPlanoAcaoFiltro();
							iniciativaPlanoAcaoFiltro.setPlanoGestao(planoGestao);
							iniciativaPlanoAcaoFiltro.setUnidadeGerencial(unidadeGerencial);
							iniciativaPlanoAcaoFiltro.setIniciativa(iniciativa);
							iniciativaPlanoAcaoFiltro.setExpirado(true);
							iniciativaPlanoAcaoFiltro.setListaStatusPlanoAcaoEnum(listaStatus);
							
							listaPlanoAcao = planoAcaoService.findByIniciativas(iniciativaPlanoAcaoFiltro);
							if (listaPlanoAcao != null && !listaPlanoAcao.isEmpty()) {
								controleCadastroItemIniciativa.setPendente(true);
							}
							else {
								controleCadastroItemIniciativa.setPendente(false);
							}
							listaControleCadastroItem.add(controleCadastroItemIniciativa);
						}
					}
					else {
						controleCadastroItemIniciativa = new ControleCadastroItem();
						controleCadastroItemIniciativa.setItemControleCadastroEnum(ItemControleCadastroEnum.PLANO_ACAO_INICIATIVA);
						controleCadastroItemIniciativa.setPendente(null);
						listaControleCadastroItem.add(controleCadastroItemIniciativa);
					}					
					controleCadastro.setListaControleCadastroItem(listaControleCadastroItem);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				listaControleCadastro.add(controleCadastro);
			}
		}		
		return listaControleCadastro;
	}
	
	private ControleCadastroItem montaItemControleCadastroMapaNegocio(UnidadeGerencial unidadeGerencial, HttpServletRequest servletRequest) {
		ControleCadastroItem controleCadastroItemMapaNegocio = new ControleCadastroItem();
		controleCadastroItemMapaNegocio.setItemControleCadastroEnum(ItemControleCadastroEnum.MAPA_NEGOCIO);
		controleCadastroItemMapaNegocio.setDescricao(ItemControleCadastroEnum.MAPA_NEGOCIO.getDescricao());
		controleCadastroItemMapaNegocio.setUrl(ItemControleCadastroEnum.MAPA_NEGOCIO.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
		controleCadastroItemMapaNegocio.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.MAPA_NEGOCIO.getPath(), "execute", servletRequest));		

		if (Boolean.TRUE.equals(unidadeGerencial.getPermitirMapaNegocio())) {
			MapaNegocio mapaNegocio = mapaNegocioService.loadByUnidadeGerencial(unidadeGerencial);
			
			// Para que o cadastro do Mapa do Negócio não esteja pendente, todos os campos devem estar preenchidos.					
			if (mapaNegocio == null || mapaNegocio.getMissao() == null || mapaNegocio.getMissao().trim().equals("") ||
					mapaNegocio.getInsumos() == null || mapaNegocio.getInsumos().trim().equals("") || 
					mapaNegocio.getPessoal() == null || mapaNegocio.getPessoal().trim().equals("") || 
					mapaNegocio.getProdutos() == null || mapaNegocio.getProdutos().trim().equals("") || 
					mapaNegocio.getFornecedores() == null || mapaNegocio.getFornecedores().trim().equals("") || 
					mapaNegocio.getNegocio() == null || mapaNegocio.getNegocio().trim().equals("") || 
					mapaNegocio.getEquipamentos() == null || mapaNegocio.getEquipamentos().trim().equals("") || 
					mapaNegocio.getClientes() == null || mapaNegocio.getClientes().trim().equals("")){
				
				controleCadastroItemMapaNegocio.setPendente(true);		
			} 
			else {
				controleCadastroItemMapaNegocio.setPendente(false);
			}
		}
		else {
			controleCadastroItemMapaNegocio.setPendente(null);
		}
		return controleCadastroItemMapaNegocio;
	}
	
	private ControleCadastroItem montaItemControleCadastroMapaEstrategico(UnidadeGerencial unidadeGerencial, HttpServletRequest servletRequest) {
		ControleCadastroItem controleCadastroItemMapaEstrategico = new ControleCadastroItem();
		controleCadastroItemMapaEstrategico.setItemControleCadastroEnum(ItemControleCadastroEnum.MAPA_ESTRATEGICO);
		controleCadastroItemMapaEstrategico.setDescricao(ItemControleCadastroEnum.MAPA_ESTRATEGICO.getDescricao());
		controleCadastroItemMapaEstrategico.setUrl(ItemControleCadastroEnum.MAPA_ESTRATEGICO.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
		controleCadastroItemMapaEstrategico.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.MAPA_ESTRATEGICO.getPath(), "execute", servletRequest));		
		
		if (Boolean.TRUE.equals(unidadeGerencial.getPermitirMapaEstrategico())) {
			MapaEstrategico mapaEstrategico = mapaEstrategicoService.loadByUnidadeGerencial(unidadeGerencial);
			
			// Para que o cadastro do Mapa Estratégico não esteja pendente, o campo visão deve estar preenchido
			// e também deve existir pelo menos um objetivo estratégico cadastrado.			
			if (mapaEstrategico == null || mapaEstrategico.getVisao() == null || mapaEstrategico.getVisao().trim().equals("") ||
				mapaEstrategico.getListaPerspectivaMapaEstrategico() == null || mapaEstrategico.getListaPerspectivaMapaEstrategico().isEmpty()){
				controleCadastroItemMapaEstrategico.setPendente(true);
			} 
			else {
				controleCadastroItemMapaEstrategico.setPendente(true);
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : mapaEstrategico.getListaPerspectivaMapaEstrategico()) {
					if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
						controleCadastroItemMapaEstrategico.setPendente(false);
						break;
					}
				}
			}
		}
		else {
			controleCadastroItemMapaEstrategico.setPendente(null);
		}
		return controleCadastroItemMapaEstrategico;
	}
	
	private ControleCadastroItem montaItemControleCadastroMapaCompetencia(UnidadeGerencial unidadeGerencial, HttpServletRequest servletRequest) {
		ControleCadastroItem controleCadastroItemMapaCompetencia = new ControleCadastroItem();
		controleCadastroItemMapaCompetencia.setItemControleCadastroEnum(ItemControleCadastroEnum.MAPA_COMPETENCIA);
		controleCadastroItemMapaCompetencia.setDescricao(ItemControleCadastroEnum.MAPA_COMPETENCIA.getDescricao());
		controleCadastroItemMapaCompetencia.setUrl(ItemControleCadastroEnum.MAPA_COMPETENCIA.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
		controleCadastroItemMapaCompetencia.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.MAPA_COMPETENCIA.getPath(), "execute", servletRequest));		
		
		if (Boolean.TRUE.equals(unidadeGerencial.getPermitirMapaCompetencia())) {
			MapaCompetencia mapaCompetencia = mapaCompetenciaService.loadByUnidadeGerencial(unidadeGerencial);
			
			// Para que o cadastro do Mapa de Competências não esteja pendente, deve haver pelo menos uma atividade
			// e uma competência preenchida.
			if (mapaCompetencia == null || mapaCompetencia.getAtividades() == null || mapaCompetencia.getAtividades().isEmpty() ||
					mapaCompetencia.getCompetencias() == null || mapaCompetencia.getCompetencias().isEmpty()){
				
				controleCadastroItemMapaCompetencia.setPendente(true);		
			} 
			else {
				controleCadastroItemMapaCompetencia.setPendente(false);
			}
		}
		else {
			controleCadastroItemMapaCompetencia.setPendente(null);
		}
		return controleCadastroItemMapaCompetencia;
	}
	
	private ControleCadastroItem montaItemControleCadastroMatrizFCS(UnidadeGerencial unidadeGerencial, HttpServletRequest servletRequest) {
		ControleCadastroItem controleCadastroItemMatrizFCS = new ControleCadastroItem();
		controleCadastroItemMatrizFCS.setItemControleCadastroEnum(ItemControleCadastroEnum.MATRIZ_FCS);
		controleCadastroItemMatrizFCS.setDescricao(ItemControleCadastroEnum.MATRIZ_FCS.getDescricao());
		controleCadastroItemMatrizFCS.setUrl(ItemControleCadastroEnum.MATRIZ_FCS.getPath() + "?unidadeGerencial.id=" + unidadeGerencial.getId());
		controleCadastroItemMatrizFCS.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.MATRIZ_FCS.getPath(), "execute", servletRequest));		
		
		if (Boolean.TRUE.equals(unidadeGerencial.getPermitirMatrizFcs())) {
			controleCadastroItemMatrizFCS.setPendente(true);
			List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico = objetivoMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial, true);
			if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
				for (ObjetivoMapaEstrategico objetivoMapaEstrategico : listaObjetivoMapaEstrategico) {
					List<MatrizFCS> listaMatrizFCS = matrizFCSService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
					
					// Para que o cadastro da Matriz de Iniciativas x FCS não esteja pendente, deve haver uma matriz cadastrada para cada objetivo estratégico.
					if (listaMatrizFCS == null || listaMatrizFCS.isEmpty()) {
						controleCadastroItemMatrizFCS.setPendente(true);
						break;
					} 
					else {
						controleCadastroItemMatrizFCS.setPendente(false);
					}
				}
			}
		}
		else {
			controleCadastroItemMatrizFCS.setPendente(null);
		}
		return controleCadastroItemMatrizFCS;
	}
	
	private ControleCadastroItem montaItemControleTratamentoAnomlia(Anomalia anomalia, HttpServletRequest servletRequest) {
		String descricaoReduzida = anomalia.getDescricao();
		if (descricaoReduzida != null && descricaoReduzida.length() > 100) {
			descricaoReduzida = descricaoReduzida.substring(0,100) + "...";
		}
		
		ControleCadastroItem controleCadastroItemTratamentoAnomalia = new ControleCadastroItem();
		controleCadastroItemTratamentoAnomalia.setItemControleCadastroEnum(ItemControleCadastroEnum.TRATAMENTO_ANOMALIA);
		controleCadastroItemTratamentoAnomalia.setDescricao(ItemControleCadastroEnum.TRATAMENTO_ANOMALIA.getDescricao() + " '" + descricaoReduzida + "'");
		controleCadastroItemTratamentoAnomalia.setUrl(ItemControleCadastroEnum.TRATAMENTO_ANOMALIA.getPath() + "?ACAO=editar&id=" + anomalia.getId());
		controleCadastroItemTratamentoAnomalia.setExibirLink(GeplanesUtils.hasAuthorization(servletRequest.getContextPath() + ItemControleCadastroEnum.TRATAMENTO_ANOMALIA.getPath(), CrudController.EDITAR, servletRequest));		

		// Verifica se a anomalia foi tratada
		if (anomaliaService.isAnomaliaTratada(anomalia)) {
			controleCadastroItemTratamentoAnomalia.setPendente(false);	
		}
		else {
			controleCadastroItemTratamentoAnomalia.setPendente(true);
		}
		
		return controleCadastroItemTratamentoAnomalia;
	}
}
