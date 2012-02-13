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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.controller.report.filtro.DesempenhoReportFiltro;
import br.com.linkcom.sgm.dao.ObjetivoMapaEstrategicoDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.report.bean.DesempenhoReportBean;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class ObjetivoMapaEstrategicoService extends GenericService<ObjetivoMapaEstrategico> {

	private static ObjetivoMapaEstrategicoService instance;
	private ObjetivoMapaEstrategicoDAO objetivoMapaEstrategicoDAO;
	private UnidadeGerencialService unidadeGerencialService;
	private IniciativaService iniciativaService;
	private MatrizFCSService matrizFCSService;
	private MatrizFCSIniciativaService matrizFCSIniciativaService;
	private IndicadorService indicadorService;
	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;

	public void setObjetivoMapaEstrategicoDAO(ObjetivoMapaEstrategicoDAO objetivoMapaEstrategicoDAO) {
		this.objetivoMapaEstrategicoDAO = objetivoMapaEstrategicoDAO;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setIniciativaService(IniciativaService iniciativaService) {
		this.iniciativaService = iniciativaService;
	}

	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {
		this.matrizFCSService = matrizFCSService;
	}	
	
	public void setMatrizFCSIniciativaService(MatrizFCSIniciativaService matrizFCSIniciativaService) {
		this.matrizFCSIniciativaService = matrizFCSIniciativaService;
	}
	
	public void setIndicadorService(IndicadorService indicadorService) {
		this.indicadorService = indicadorService;
	}
	
	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {
		this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;
	}

	/**
	 * Carrega os ids do objetivo estratégico, da unidade gerencial e do ano da gestão.
	 * 
	 * @param indicador
	 * @return
	 */	
	public ObjetivoMapaEstrategico loadWithIDsUnidadePlanoGestao(ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		return objetivoMapaEstrategicoDAO.loadWithIDsUnidadePlanoGestao(objetivoMapaEstrategico);
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada UG, 
	 * baseado na Matriz de Iniciativas x FCS.
	 * Se o parâmetro <code>pesquisarUGSuperior<code> for verdadeiro, 
	 * caso uma UG não tenha a Matriz FCS cadastrada, procura nas UGs superiores, recursivamente.
	 * 
	 * @param unidadeGerencial
	 * @param pesquisarUGSuperior
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialThroughMatrizFCS(UnidadeGerencial unidadeGerencial, boolean pesquisarUGSuperior) {
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico = objetivoMapaEstrategicoDAO.findByUnidadeGerencialThroughMatrizFCS(unidadeGerencial);
		if ((listaObjetivoMapaEstrategico == null || listaObjetivoMapaEstrategico.isEmpty()) && pesquisarUGSuperior) {
			// Percorre as UGs superiores até encontrar alguma que possua MatrizFCS cadastrada.
			List<UnidadeGerencial> ugPais = unidadeGerencialService.findAllParents(unidadeGerencial);
			if (ugPais != null && !ugPais.isEmpty()) {
				for (UnidadeGerencial ugPai : ugPais) {
					listaObjetivoMapaEstrategico = objetivoMapaEstrategicoDAO.findByUnidadeGerencialThroughMatrizFCS(ugPai);
					if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
						break;
					}
				}
			}
		}
		return listaObjetivoMapaEstrategico;
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada UG, 
	 * baseado no Mapa Estratégico.
	 * Se o parâmetro <code>pesquisarUGSuperior</code> for verdadeiro, 
	 * caso uma UG não tenha o mapa estratégico cadastrado, procura nas UGs superiores, recursivamente.
	 * 
	 * @param unidadeGerencial
	 * @param pesquisarUGSuperior
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, boolean pesquisarUGSuperior) {
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico = new ArrayList<ObjetivoMapaEstrategico>();
		if (unidadeGerencial != null) {
			listaObjetivoMapaEstrategico = objetivoMapaEstrategicoDAO.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial);
			if ((listaObjetivoMapaEstrategico == null || listaObjetivoMapaEstrategico.isEmpty()) && pesquisarUGSuperior) {
				// Percorre as UGs superiores até encontrar alguma que possua o Mapa Estratégico cadastrado.
				List<UnidadeGerencial> ugPais = unidadeGerencialService.findAllParents(unidadeGerencial);
				if (ugPais != null && !ugPais.isEmpty()) {
					for (UnidadeGerencial ugPai : ugPais) {
						listaObjetivoMapaEstrategico = objetivoMapaEstrategicoDAO.findByUnidadeGerencialThroughMapaEstrategico(ugPai);
						if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
							break;
						}
					}
				}
			}
		}
		return listaObjetivoMapaEstrategico;
	}	
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva do Mapa Estratégico.
	 * 
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByPerspectivaMapaEstrategico(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return objetivoMapaEstrategicoDAO.findByPerspectivaMapaEstrategico(perspectivaMapaEstrategico);
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva do Mapa Estratégico de uma unidade gerencial.
	 *  
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial, perspectivaMapaEstrategico, false, false, false, false, false);
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva do Mapa Estratégico de uma unidade gerencial. 
	 *  
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @param incluirIniciativas
	 * @param incluirIndicadores
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico, boolean incluirIniciativas, boolean incluirIndicadores, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia) {
		return findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial, perspectivaMapaEstrategico, incluirIniciativas, incluirIndicadores, listarAcompanhamento, listarAnexo, listarAnomalia, null);
	}	
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva do Mapa Estratégico de uma unidade gerencial. 
	 *  
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @param incluirIniciativas
	 * @param incluirIndicadores
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @param orderBy
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico, boolean incluirIniciativas, boolean incluirIndicadores, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia, String orderBy) {
		
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico = new ArrayList<ObjetivoMapaEstrategico>();
		List<Iniciativa> listaIniciativa;
		Iniciativa iniciativa;
		List<MatrizFCSIniciativa> listaMatrizFCSIniciativa;
		
		if (unidadeGerencial != null && perspectivaMapaEstrategico != null) {
			
			if (perspectivaMapaEstrategico.getMapaEstrategico() == null || perspectivaMapaEstrategico.getMapaEstrategico().getUnidadeGerencial() == null) {
				perspectivaMapaEstrategico = perspectivaMapaEstrategicoService.loadWithUnidadeGerencial(perspectivaMapaEstrategico);
			}
			
			listaObjetivoMapaEstrategico = objetivoMapaEstrategicoDAO.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(perspectivaMapaEstrategico.getMapaEstrategico().getUnidadeGerencial(), perspectivaMapaEstrategico, orderBy);
			
			if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
				for (ObjetivoMapaEstrategico objetivoMapaEstrategico : listaObjetivoMapaEstrategico) {
					
					if (incluirIndicadores) {
						objetivoMapaEstrategico.setListaIndicador(indicadorService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico, true, listarAcompanhamento, listarAnexo, listarAnomalia));
					}
					
					if (incluirIniciativas) {
						listaIniciativa = iniciativaService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
						
						// Caso não exista iniciativa e indicador cadastrados, busca as iniciativas prioritárias cadastradas na matriz de iniciativas x fcs.
						if ((listaIniciativa == null || listaIniciativa.isEmpty()) && (objetivoMapaEstrategico.getListaIndicador() == null || objetivoMapaEstrategico.getListaIndicador().isEmpty())) {
							listaMatrizFCSIniciativa = matrizFCSIniciativaService.findPrioritariasByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
							
							if (listaMatrizFCSIniciativa != null && !listaMatrizFCSIniciativa.isEmpty()) {
								listaIniciativa = new ArrayList<Iniciativa>();
								for (MatrizFCSIniciativa matrizFCSIniciativa : listaMatrizFCSIniciativa) {
									iniciativa = new Iniciativa();
									iniciativa.setDescricao(matrizFCSIniciativa.getDescIniciativa());
									iniciativa.setUnidadeGerencial(unidadeGerencial);
									iniciativa.setObjetivoMapaEstrategico(objetivoMapaEstrategico);
									listaIniciativa.add(iniciativa);
								}
							}
						}
						objetivoMapaEstrategico.setListaIniciativa(listaIniciativa);
					}
				}
			}
		}
		return listaObjetivoMapaEstrategico;		
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva, baseado na Matriz de Inciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMatrizFCS(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return findByUnidadeGerencialPerspectivaThroughMatrizFCS(unidadeGerencial, perspectivaMapaEstrategico, false, false, false, false, false);
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva, baseado na Matriz de Inciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @param incluirIniciativas
	 * @param incluirIndicadores
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMatrizFCS(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico, boolean incluirIniciativas, boolean incluirIndicadores, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia) {
		
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico = new ArrayList<ObjetivoMapaEstrategico>();
		List<Iniciativa> listaIniciativa;
		Iniciativa iniciativa;
		List<MatrizFCSIniciativa> listaMatrizFCSIniciativa;
		
		if (unidadeGerencial != null && perspectivaMapaEstrategico != null) {
			listaObjetivoMapaEstrategico = objetivoMapaEstrategicoDAO.findByUnidadeGerencialPerspectivaThroughMatrizFCS(unidadeGerencial, perspectivaMapaEstrategico);
			
			if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
				for (ObjetivoMapaEstrategico objetivoMapaEstrategico : listaObjetivoMapaEstrategico) {
					
					if (incluirIndicadores) {
						objetivoMapaEstrategico.setListaIndicador(indicadorService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico, true, listarAcompanhamento, listarAnexo, listarAnomalia));
					}
					
					if (incluirIniciativas) {
						listaIniciativa = iniciativaService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
						
						// Caso não exista iniciativa e indicador cadastrados, busca as iniciativas prioritárias cadastradas na matriz de iniciativas x fcs.
						if ((listaIniciativa == null || listaIniciativa.isEmpty()) && (objetivoMapaEstrategico.getListaIndicador() == null || objetivoMapaEstrategico.getListaIndicador().isEmpty())) {
							listaMatrizFCSIniciativa = matrizFCSIniciativaService.findPrioritariasByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
							
							if (listaMatrizFCSIniciativa != null && !listaMatrizFCSIniciativa.isEmpty()) {
								listaIniciativa = new ArrayList<Iniciativa>();
								for (MatrizFCSIniciativa matrizFCSIniciativa : listaMatrizFCSIniciativa) {
									iniciativa = new Iniciativa();
									iniciativa.setDescricao(matrizFCSIniciativa.getDescIniciativa());
									iniciativa.setUnidadeGerencial(unidadeGerencial);
									iniciativa.setObjetivoMapaEstrategico(objetivoMapaEstrategico);
									listaIniciativa.add(iniciativa);
								}
							}
						}
						objetivoMapaEstrategico.setListaIniciativa(listaIniciativa);
					}
				}
			}
		}
		return listaObjetivoMapaEstrategico;		
	}
	
	/**
	 * Exclui uma lista de objetivos estratégicos
	 * @param listaObjetivoMapaEstrategico
	 * 
	 */
	public void exclui (List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico) {
		for (ObjetivoMapaEstrategico objetivoMapaEstrategico : listaObjetivoMapaEstrategico) {
			this.delete(objetivoMapaEstrategico);
		}
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para um determinado ano de gestão.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByPlanoGestaoThroughMapaEstrategico(PlanoGestao planoGestao) {
		return objetivoMapaEstrategicoDAO.findByPlanoGestaoThroughMapaEstrategico(planoGestao);
	}
	
	/**
	 * Cria o relatório de Desempenho (por Objetivo Estratégico ou Unidade Gerencial).
	 * 
	 * @author Rodrigo Alvarenga 
	 * @param filtro
	 * @return
	 */
	public IReport createDesempenhoReport(DesempenhoReportFiltro filtro) {
		
		//Services
		IndicadorService indicadorService = IndicadorService.getInstance();
		PlanoGestaoService planoGestaoService = PlanoGestaoService.getInstance();
		
		//Listas
		List<DesempenhoReportBean> listaDesempenhoBean = new ArrayList<DesempenhoReportBean>();
		List<Indicador> listaIndicador;	
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
		
		//Beans
		PlanoGestao planoGestao = planoGestaoService.load(filtro.getPlanoGestao());
		DesempenhoReportBean desempenhoBean;
		
		//Geral
		Map<Integer, Double> mapaPercentualEpoca;		
		
		// Instancia o Report
		Report report;
		String orderBy;
		if (filtro.getTipoRelatorio() == 1) { // Desempenho por Unidade Gerencial
			report = new Report("../relatorio/desempenhoUnidadeGerencial");
			orderBy = "unidadeGerencial.sigla, objetivoEstrategico.descricao, indicador.nome, acompanhamentoIndicador.dataInicial";			
		}
		else { // Desempenho por Objetivo Estratégico
			report = new Report("../relatorio/desempenhoObjetivoEstrategico");
			orderBy = "objetivoEstrategico.descricao, unidadeGerencial.sigla, indicador.nome, acompanhamentoIndicador.dataInicial";
		}
		
		filtro.setPlanoGestao(planoGestaoService.load(filtro.getPlanoGestao()));
		
		// Busca as informações dos indicadores de acordo com o filtro.		
		listaIndicador = indicadorService.findBy(filtro.getPlanoGestao(), filtro.getUnidadeGerencial(), filtro.getObjetivoEstrategico(), orderBy);
		
		if (listaIndicador != null) {
			for (Indicador indicador : listaIndicador) {
				desempenhoBean = new DesempenhoReportBean();
				desempenhoBean.setDescObjetivoEstrategico(indicador.getObjetivoMapaEstrategico().getDescricao());
				desempenhoBean.setIdUnidadeGerencial(indicador.getUnidadeGerencial().getId());
				
				if (filtro.getTipoRelatorio() == 1) { // Desempenho por Unidade Gerencial
					desempenhoBean.setDescUnidadeGerencial(unidadeGerencialService.getDescricaoUnidadeGerencialComDescendencia(indicador.getUnidadeGerencial()));
				}
				else { // Desempenho por Objetivo Estratégico
					desempenhoBean.setDescUnidadeGerencial(indicador.getUnidadeGerencial().getSigla());
				}
				
				desempenhoBean.setDescMeta(null);
				desempenhoBean.setDescIndicador(indicador.getNome());
				
				mapaPercentualEpoca = new HashMap<Integer, Double>();
				
				listaAcompanhamentoIndicador = new ArrayList<AcompanhamentoIndicador>(indicador.getAcompanhamentosIndicador());
				for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoIndicador) {
					acompanhamentoIndicador.setIndicador(indicador);
				}
				
				// Agrupa os indicadores por trimestre
				listaAcompanhamentoIndicador = CalculosAuxiliares.agruparPorFator(new ArrayList<AcompanhamentoIndicador>(indicador.getAcompanhamentosIndicador()), FrequenciaIndicadorEnum.TRIMESTRAL.getFatorDivisao(), indicador.getPercentualTolerancia());
				
				// Busca o acompanhamento do indicador para cada trimestre.
				for (int i = 0; i < listaAcompanhamentoIndicador.size(); i++) {
					mapaPercentualEpoca.put(i+1,listaAcompanhamentoIndicador.get(i).getPercentualReal());
				}
				desempenhoBean.setMapaPercentualEpoca(mapaPercentualEpoca);
				
				listaDesempenhoBean.add(desempenhoBean);
			}
		}	
		report.setDataSource(listaDesempenhoBean);
		
		report.addParameter("ANOGESTAO", planoGestao.getAnoExercicio().toString());
		report.addParameter("FORMATADOR", new DecimalFormat("##0.00"));
		return report;
	}
	
	@Override
	public void delete(ObjetivoMapaEstrategico bean) {
		List<MatrizFCS> listaMatrizFCS = matrizFCSService.findByUnidadeGerencialObjetivoEstrategico(null, bean);
		if (listaMatrizFCS != null && !listaMatrizFCS.isEmpty()) {
			if (bean.getDescricao() == null) {
				bean = load(bean);
			}
			throw new GeplanesException("Não foi possível excluir o objetivo estratégico '" + bean.getDescricao() + "', pois existe(m) registro(s) de Matriz de Iniciativas x Fatores Críticos de Sucesso vinculados.");
		}
		super.delete(bean);
	}
	
	public static ObjetivoMapaEstrategicoService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(ObjetivoMapaEstrategicoService.class);
		}
		return instance;
	}	
}
