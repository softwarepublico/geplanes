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
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.AnexoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoEstrategico;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ApresentacaoResultadosDTO;
import br.com.linkcom.sgm.beans.DTO.ApresentacaoResultadosReportDTO;
import br.com.linkcom.sgm.beans.DTO.PendenciaAnomaliaReportDTO;
import br.com.linkcom.sgm.beans.enumeration.GraficoTipoEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.controller.report.filtro.IndicadoresEstrategicoReportFiltro;
import br.com.linkcom.sgm.controller.report.filtro.PendenciaAnomaliaReportFiltro;
import br.com.linkcom.sgm.dao.IndicadorDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.IndicadorFiltro;
import br.com.linkcom.sgm.report.bean.ApresentacaoResultadosReportBean;
import br.com.linkcom.sgm.report.bean.IndicadoresEstrategicoReportBean;
import br.com.linkcom.sgm.util.CalendarComparator;
import br.com.linkcom.sgm.util.GeplanesUtils;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class IndicadorService extends GenericService<Indicador> {

	private static IndicadorService instance;
	
	private IndicadorDAO indicadorDAO;
	private UsuarioService usuarioService;
	private PlanoGestaoService planoGestaoService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private ObjetivoEstrategicoService objetivoEstrategicoService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	private AnexoIndicadorService anexoIndicadorService;
	private UnidadeGerencialService unidadeGerencialService;
	
	public void setObjetivoEstrategicoService(ObjetivoEstrategicoService objetivoEstrategicoService) {this.objetivoEstrategicoService = objetivoEstrategicoService;}
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;}
	public void setIndicadorDAO(IndicadorDAO indicadorDAO) {this.indicadorDAO = indicadorDAO;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) { this.acompanhamentoIndicadorService = acompanhamentoIndicadorService; }
	public void setAnexoIndicadorService(AnexoIndicadorService anexoIndicadorService) {this.anexoIndicadorService = anexoIndicadorService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	
	@Override
	public ListagemResult<Indicador> findForListagem(FiltroListagem filtro) {
		IndicadorFiltro indicadorFiltro = (IndicadorFiltro) filtro;
		
		List<UnidadeGerencial> listaUnidadeGerencial = new ArrayList<UnidadeGerencial>();		
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(indicadorFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		
		// Unidade Gerencial selecionada no filtro
		if (indicadorFiltro.getUnidadeGerencial() != null) {
			listaUnidadeGerencial.add(indicadorFiltro.getUnidadeGerencial());
		}
		indicadorFiltro.setListaUnidadeGerencial(listaUnidadeGerencial);
		
		// Unidades gerenciais disponíveis para a listagem
		if (!usuarioLogadoIsAdmin) {
			for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
				listaUGDisponivel.add(unidadeGerencial);
				listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
			}			
		}
		indicadorFiltro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		
		return super.findForListagem(indicadorFiltro);
	}	
	
	/**
	 * Retorna uma lista de indicadores cadastrados para uma determinada unidade gerencial
	 * e vinculados a um determinado objetivo estratégico.
	 * 
	 * @param unidadeGerencial
	 * @param objetivoMapaEstrategico
	 * @param incluirIndicadoresCancelados
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @return  
	 */
	public List<Indicador> findByUnidadeGerencialObjetivoEstrategico(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico, boolean incluirIndicadoresCancelados, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia) {
		return indicadorDAO.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico, incluirIndicadoresCancelados, listarAcompanhamento, listarAnexo, listarAnomalia);
	}
	
	public Indicador obtemFrequencia(Indicador indicador) {
		Indicador itemp = indicadorDAO.obtemFrequencia(indicador);
		indicador.setFrequencia(itemp.getFrequencia());
		return indicador;
	}
	
	public void defineDatas(ApresentacaoResultadosDTO bean) {
		Calendar d1 = Calendar.getInstance();
		d1.set(Calendar.DAY_OF_YEAR, 1);
		d1.set(Calendar.YEAR,bean.getPlanoGestao().getAnoExercicio());
		bean.setDataInicial(d1);
		Calendar d2 = Calendar.getInstance();
		d2.set(Calendar.MONTH, 11);
		d2.set(Calendar.DAY_OF_MONTH, 31);
		d2.set(Calendar.YEAR,bean.getPlanoGestao().getAnoExercicio());		
		bean.setDataFinal(d2);
	}
	
	public List<Indicador> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return indicadorDAO.findByUnidadeGerencial(unidadeGerencial,  new StatusIndicadorEnum[]{});
	}
	
	public List<Indicador> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial, StatusIndicadorEnum statusIndicador) {
		return indicadorDAO.findByUnidadeGerencial(unidadeGerencial,  statusIndicador);
	}
	
	/**
	 * Retorna os indicadores com status 'Aprovado' ou 'Em cancelamento'
	 * de uma determinada unidade gerencial.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<Indicador> findAtivosByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return indicadorDAO.findByUnidadeGerencial(unidadeGerencial, new StatusIndicadorEnum[]{StatusIndicadorEnum.APROVADO, StatusIndicadorEnum.EM_CANCELAMENTO});
	}	
	
	public boolean usuarioPodeAlterar(Indicador indicador) {		
		if  (usuarioService.isUsuarioLogadoAdmin()) {
			return true;
		}
		else {
			if (usuarioService.isUsuarioLogadoResponsavelUG(indicador.getUnidadeGerencial())) {
				return true;
			}
			else {
				return false;
			}
		}
			
	}
	
	/**
	 * Exclui uma lista de indicadores.
	 * @author Rodrigo Duarte
	 * @param request
	 * @param bean
	 * @throws Exception
	 */
	public void exclui( List<Indicador> listaIndicador){
		for (Indicador indicador : listaIndicador) {
			this.delete(indicador);
		}
	}
	
	public Indicador obtemColecaoAnexoIndicador(Indicador indicador){
		return ((IndicadorDAO)getGenericDAO()).obtemColecaoAnexoIndicador(indicador); 
	}

	
	public Indicador obtemColecaoIndicador(String idStringIndicador) {
		Indicador indicador = new Indicador();
		try {
			Integer idIndicador = Integer.parseInt(idStringIndicador);
			indicador.setId(idIndicador);
			indicador = obtemColecaoAnexoIndicador(indicador);
		} catch (Exception e) {}
		return indicador;
		
	}

	
	public Indicador obtemSomatorioColecaoAcompanhamento(Indicador indicador, Calendar dtInicial, Calendar dtFinal){
		indicador = indicadorDAO.obtemColecaoAcompanhamento(indicador, dtInicial, dtFinal);		
		if (indicador != null) {			
			List<Indicador> listaIndicador = new ArrayList<Indicador>();
			listaIndicador.add(indicador);
			List<Calendar> listaData = acompanhamentoIndicadorService.getListaDataAcompanhamentoIndicador(listaIndicador,dtInicial,dtFinal);
			indicador = calculaAcompanhamentosIndicador(indicador, listaData);
		}
		return indicador;
	}
	
	public Indicador calculaAcompanhamentosIndicador(Indicador indicador, List<Calendar> listaData) {
		List<AcompanhamentoIndicador> lista = new ArrayList<AcompanhamentoIndicador>();
		AcompanhamentoIndicador acompanhamentoIndicadorTotal;
		Double valorLimiteSuperior;
		Double valorReal;
		Double valorLimiteInferior;
		boolean naoAplicavel;
	
		if (indicador.getAcompanhamentosIndicador() != null) {
			for (Calendar data : listaData) {
				acompanhamentoIndicadorTotal = new AcompanhamentoIndicador();
				acompanhamentoIndicadorTotal.setPercentualTolerancia(indicador.getTolerancia());
				acompanhamentoIndicadorTotal.setDataInicial(data);
				
				valorLimiteSuperior = null;
				valorReal = null;
				valorLimiteInferior = null;
				naoAplicavel = true;
								
				for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
					if (acompanhamentoIndicador.getDataInicial().equals(data)) {
						
						if (acompanhamentoIndicador.getValorLimiteSuperior() != null) {
							if (valorLimiteSuperior == null) {
								valorLimiteSuperior = 0d;
							}
							valorLimiteSuperior += acompanhamentoIndicador.getValorLimiteSuperior();
						}
						
						if (acompanhamentoIndicador.getValorReal() != null) {
							if (valorReal == null) {
								valorReal = 0d;
							}
							valorReal += acompanhamentoIndicador.getValorReal();
						} 
						
						if (acompanhamentoIndicador.getValorLimiteInferior() != null) {
							if (valorLimiteInferior == null) {
								valorLimiteInferior = 0d;
							}
							valorLimiteInferior += acompanhamentoIndicador.getValorLimiteInferior();
						}
						
						if (acompanhamentoIndicador.getNaoaplicavel() != null) {
							naoAplicavel = naoAplicavel && acompanhamentoIndicador.getNaoaplicavel();
						}						
					}					
				}
				acompanhamentoIndicadorTotal.setValorLimiteSuperior(valorLimiteSuperior);
				acompanhamentoIndicadorTotal.setValorReal(valorReal);
				acompanhamentoIndicadorTotal.setValorLimiteInferior(valorLimiteInferior);
				acompanhamentoIndicadorTotal.setIndicador(indicador);
				acompanhamentoIndicadorTotal.setNaoaplicavel(naoAplicavel);
				acompanhamentoIndicadorTotal.setDataFinal(CalculosAuxiliares.calculaDataFinalAcompanhamento(indicador.getFrequencia(), acompanhamentoIndicadorTotal.getDataInicial()));
				lista.add(acompanhamentoIndicadorTotal);
			}
		}
		indicador.setAcompanhamentosIndicador(new LinkedHashSet<AcompanhamentoIndicador>(lista));	
		return indicador;
	}
	
	@SuppressWarnings("unchecked")
	public List<AcompanhamentoIndicador> agruparPorFator(Indicador indicador, int fatorDivisao,List<Calendar> listaDataInicial) {
		List<AcompanhamentoIndicador> acompanhamentos = new ArrayList<AcompanhamentoIndicador>(indicador.getAcompanhamentosIndicador());
		Collections.sort(acompanhamentos, new BeanComparator("dataInicial"));
		List<AcompanhamentoIndicador> calculados = new ArrayList<AcompanhamentoIndicador>();
		
		if(acompanhamentos.size() < fatorDivisao){
			throw new GeplanesException("Não foi possível agrupar os acompanhamentos do indicador. O número de acompanhamentos ("+acompanhamentos.size()+") é menor que o fator de divisão ("+fatorDivisao+")");
		}
		if(acompanhamentos.size() % fatorDivisao != 0){
			throw new GeplanesException("Não foi possível agrupar os acompanhamentos do indicador. O número de acompanhamentos ("+acompanhamentos.size()+") não é múltiplo do fator de divisão ("+fatorDivisao+")");
		}
		
		int grupo = acompanhamentos.size() / fatorDivisao;
		int indice = 0;
		for (int i = 0; i < acompanhamentos.size(); i+= grupo) {
			double valorLimiteSuperior = 0;
			double valorReal = 0;
			double valorLimiteInferior = 0;	
			
			Double percentualTolerancia = indicador.getPercentualTolerancia();//a tolerancia não precisa somar porque vai ser igual em todos os acompanhamentos
			for(int j = 0; j < grupo; j++) {
				//valorLimiteSuperior   += acompanhamentos.get(i + j).getValorLimiteSuperior() != null && acompanhamentos.get(i + j).getValorBaseOK() != null && acompanhamentos.get(i + j).getValorBaseOK() ? acompanhamentos.get(i + j).getValorLimiteSuperior() : 0; 
				valorLimiteSuperior   += acompanhamentos.get(i + j).getValorLimiteSuperior() != null ? acompanhamentos.get(i + j).getValorLimiteSuperior() : 0;
				//valorReal      += acompanhamentos.get(i + j).getValorReal() != null && acompanhamentos.get(i + j).getValorRealOK() != null && acompanhamentos.get(i + j).getValorRealOK() ? acompanhamentos.get(i + j).getValorReal() : 0;						
				valorReal      += acompanhamentos.get(i + j).getValorReal() != null ? acompanhamentos.get(i + j).getValorReal() : 0;
				valorLimiteInferior += acompanhamentos.get(i + j).getValorLimiteInferior() != null ? acompanhamentos.get(i + j).getValorLimiteInferior() : 0;				
			}

			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			acompanhamentoIndicador.setPercentualTolerancia(percentualTolerancia);
			acompanhamentoIndicador.setValorLimiteSuperior(valorLimiteSuperior);
			acompanhamentoIndicador.setValorReal(valorReal);
			acompanhamentoIndicador.setValorLimiteInferior(valorLimiteInferior);
			acompanhamentoIndicador.setDataInicial(listaDataInicial.get(indice));
			acompanhamentoIndicador.setValorBaseOK(true);
			acompanhamentoIndicador.setValorRealOK(true);
			acompanhamentoIndicador.setIndicador(indicador);
			
			calculados.add(acompanhamentoIndicador);
			indice++;
		}
		
		return calculados;
	}
	
    @SuppressWarnings("unchecked")
	public boolean isListaContemElemento(List lista, Calendar calendar) {
    	boolean contem = false;
    	for (int i = 0; i < lista.size(); i++) {
    		if (lista.get(i) instanceof AcompanhamentoIndicador) {
    			if (((AcompanhamentoIndicador) lista.get(i)).getDataInicial().equals(calendar)) {
    				contem = true;
    				break;
    			}	
    		}
    		else if (lista.get(i) instanceof Calendar) {
    			if (((Calendar) lista.get(i)).equals(calendar)) {
    				contem = true;
    				break;
    			}    			
    		}
    		else {
    			throw new GeplanesException("Tipo de Argumento inválido para o método!");
    		}
    	}
    	return contem;
    }	
	
	public IReport createReportApresentacaoResultados(ApresentacaoResultadosDTO filtro) {
	
		Report report = new Report("");
		
		if (filtro.getTipoGrafico().equals(GraficoTipoEnum.ACOMPANHAMENTO)) {
			if (filtro.getIndicadorSelecionado() != null) {
				switch (filtro.getIndicadorSelecionado().getMelhor()) {
				
					case MELHOR_CIMA:
						report = new Report("/apresentacaoResultadosAcompanhamentoC");
						break;
						
					case MELHOR_ENTRE_FAIXAS:
						report = new Report("/apresentacaoResultadosAcompanhamentoEF");
						break;
						
					case MELHOR_BAIXO:
						report = new Report("/apresentacaoResultadosAcompanhamentoB");
						break;

					default:
						report = new Report("");
						break;
				}
			}
		}
		else if (filtro.getTipoGrafico().equals(GraficoTipoEnum.PERCENTUAL)) {
			report = new Report("/apresentacaoResultadosPercentual");
		}
		else if (filtro.getTipoGrafico().equals(GraficoTipoEnum.FAROL)) {
			report = new Report("/apresentacaoResultadosFarol");
		}
		
		ApresentacaoResultadosReportBean reportBean = new ApresentacaoResultadosReportBean(filtro);
		
		report.addParameter("DATAINICIAL", reportBean.getDataInicial());
		report.addParameter("DATAFINAL",reportBean.getDataFinal());
		report.addParameter("GRAFICOTIPO",reportBean.getGraficoTipo());
		report.addParameter("GRAFICOAPRESENTACAO",reportBean.getGraficoApresentacao());
		report.addParameter("GRAFICOTIPORESULTADO",reportBean.getGraficoTipoResultado());
		report.addParameter("PLANOGESTAO",reportBean.getPlanoGestao());
		report.addParameter("UNIDADEGERENCIAL",reportBean.getUnidadeGerencial());
		report.addParameter("GRAFICO", reportBean.getGrafico());
		List<ApresentacaoResultadosReportDTO> dataSource = reportBean.getDataSource();
		report.setDataSource(dataSource);
		
		return report;
	}
	
	public int contaIndicadores(PlanoGestao pg){
		return indicadorDAO.contaIndicadores(pg);
	}
	
	/**
	 * Verifica se cada indicador da lista passada como parâmetro está com os dados de detalhamento devidamente cadastrados.
	 * @author Rodrigo Alvarenga
	 * @param  listaIndicador
	 * @return verdadeiro ou falso
	 * @throws GeplanesException - caso a lista de indicadores seja nula
	 */	
	public boolean isDetalhamentoIndicadorOK(List<Indicador> listaIndicador) {
		if (listaIndicador == null) {
			throw new GeplanesException("A lista de indicadores não pode ser nula.");
		}
		
		for (Indicador indicador : listaIndicador) {
			if (!indicador.isDetalhamentoOK()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Atualiza o status do indicador
	 * 
	 * @author Rodrigo Alvarenga
	 * @param indicador
	 * @param status
	 */	
	public void atualizaStatusIndicador(Indicador indicador, StatusIndicadorEnum status) {
		indicadorDAO.atualizaStatusIndicador(indicador, status);
	}
	
	/**
	 * Cria o relatório de pendência de anomalia.
	 *
	 * @see br.com.linkcom.sgm.service.IndicadorService#findForPendenciaAnomalia
	 * 
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	public IReport createReportPendenciaAnomalia(PendenciaAnomaliaReportFiltro filtro) {
		
		//Instanciando o Report
		Report report = new Report("../relatorio/pendenciaAnomalia");
		
		List<PendenciaAnomaliaReportDTO> listaReport = new ArrayList<PendenciaAnomaliaReportDTO>();
		PendenciaAnomaliaReportDTO bean;
		
		List<Indicador> listaIndicador = this.findForPendenciaAnomalia(filtro);
		List<AcompanhamentoIndicador> listaAcomp;
		
		for (Indicador ind : listaIndicador) {
			listaAcomp = new ArrayList<AcompanhamentoIndicador>(ind.getAcompanhamentosIndicador());
			listaAcomp = CalculosAuxiliares.agruparPorFator(listaAcomp, ind.getFrequencia().getFatorDivisao(), ind.getPercentualTolerancia());
			CalculosAuxiliares.nomeiaEpocas(listaAcomp);
			
			for (AcompanhamentoIndicador acomp : listaAcomp) {
				if (acomp.getPercentualReal() != null && acomp.getPercentualReal() < 100 && (acomp.getAnomalia() == null || acomp.getAnomalia().getId() == null)) {
					bean = new PendenciaAnomaliaReportDTO();
					
					bean.setDescricaoObjetivoEstrategico(ind.getObjetivoMapaEstrategico().getObjetivoEstrategico().getDescricao());
					bean.setDescricaoIndicador(ind.getNome());
					bean.setDescricaoEpoca(acomp.getEpoca());
					bean.setPercentual(acomp.getPercentualReal());
					
					listaReport.add(bean);
				}
			}
			
		}
		
		
		report.setDataSource(listaReport);
		
		report.addParameter("UNIDADEGERENCIAL", UnidadeGerencialService.getInstance().load(filtro.getUnidadeGerencial()).getDescricao());
		report.addParameter("PLANOGESTAO", PlanoGestaoService.getInstance().load(filtro.getPlanoGestao()).getAnoExercicio().toString());
		
		if (filtro.getObjetivoMapaEstrategico() != null && filtro.getObjetivoMapaEstrategico().getId() != null) {
			report.addParameter("ESTRATEGIA", objetivoMapaEstrategicoService.load(filtro.getObjetivoMapaEstrategico()).getDescricao());
		}
		
		return report;
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.sgm.dao.IndicadorDAO#findForPendenciaAnomalia
	 *
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	private List<Indicador> findForPendenciaAnomalia(PendenciaAnomaliaReportFiltro filtro) {
		return indicadorDAO.findForPendenciaAnomalia(filtro);
	}
	
	public void atualizaIndicadores(List<Indicador> listaIndicadores, UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		for (Indicador indicador : listaIndicadores) {
			if (indicador != null && indicador.getId() != null) {
				this.atualizaIndicador(indicador);
			} 
			else {
				indicador.setUnidadeGerencial(unidadeGerencial);
				indicador.setStatus(StatusIndicadorEnum.APROVADO);
				indicador.setObjetivoMapaEstrategico(objetivoMapaEstrategico);
				this.saveOrUpdate(indicador);
			}
		}
	}
	
	private void atualizaIndicador(Indicador indicador) {
		indicadorDAO.atualizaIndicador(indicador);
	}
	
	public void deleteWhereNotIn(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico, String listAndConcatenate) {
		indicadorDAO.deleteWhereNotIn(unidadeGerencial, objetivoMapaEstrategico, listAndConcatenate);
	}

	/**
	 * Remove todos os indicadores de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		indicadorDAO.deleteByUnidadeGerencial(unidadeGerencial);
	}
	
	public IReport criarRelatorioIndicadoresEstrategico(IndicadoresEstrategicoReportFiltro filtro) {
		Report report = new Report("../relatorio/indicadoresEstrategico");
		
		List<Indicador> listaIndicador = this.findBy(filtro.getPlanoGestao(), null, filtro.getObjetivoEstrategico(), "objetivoEstrategico.descricao, unidadeGerencial.sigla, indicador.nome");
		
		List<IndicadoresEstrategicoReportBean> listaBean = new ArrayList<IndicadoresEstrategicoReportBean>();
		IndicadoresEstrategicoReportBean bean;
		
		filtro.setPlanoGestao(planoGestaoService.load(filtro.getPlanoGestao()));
		
		if (filtro.getObjetivoEstrategico() != null) {
			filtro.setObjetivoEstrategico(objetivoEstrategicoService.load(filtro.getObjetivoEstrategico()));
		}
		
		for (Indicador indicador : listaIndicador) {
			bean = new IndicadoresEstrategicoReportBean();
			bean.setAnoGestao(""+filtro.getPlanoGestao().getAnoExercicio());
			bean.setObjetivoEstrategico(indicador.getObjetivoMapaEstrategico().getDescricao());
			bean.setUnidadeGerencial(indicador.getUnidadeGerencial().getSigla());
			bean.setNome(indicador.getNome());
			bean.setMelhor(""+indicador.getMelhor());
			bean.setUnidadeMedida(""+indicador.getUnidadeMedida().getSigla());
			bean.setPrecisao(""+indicador.getPrecisao());
			bean.setTolerancia(""+indicador.getTolerancia());
			bean.setFrequencia(""+indicador.getFrequencia());
			
			if (indicador.getStatus().equals(StatusIndicadorEnum.CANCELADO)) {
				bean.setCancelado(true);
			} 
			else {
				bean.setCancelado(false);
			}
			
			listaBean.add(bean);
		}
		
		if(listaBean.size() > 0){
			report.setDataSource(listaBean);
		} else {
			throw new GeplanesException("Nenhum registro a ser mostrado.");
		}
		
		return report;
	}
	
	public List<Indicador> findBy(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencial, ObjetivoEstrategico objetivoEstrategico, String orderBy) {
		return indicadorDAO.findBy(planoGestao, unidadeGerencial, objetivoEstrategico, orderBy);
	}
	
	public static IndicadorService getInstance(){
		if(instance == null){
			instance = Neo.getObject(IndicadorService.class);
		}
		return instance;
	}
	
	public Indicador loadForEmail(Indicador indicador) {
		return indicadorDAO.loadForEmail(indicador);
	}
	
	/**
	 * Carrega os ids do indicador, da unidade gerencial e do ano da gestão.
	 * 
	 * @param indicador
	 * @return
	 */	
	public Indicador loadWithUnidadePlanoGestao(Indicador indicador) {
		return indicadorDAO.loadWithUnidadePlanoGestao(indicador);
	}
	
	@SuppressWarnings("unchecked")
	public void copiaIndicador(UnidadeGerencial ugNova, ObjetivoMapaEstrategico objetivoMapaEstrategicoNovo, Indicador indicadorAntigo, boolean useTransaction) {
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicadorAntigo;
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicadorNovo;
		AcompanhamentoIndicador acompanhamentoIndicadorNovo;
		List<AnexoIndicador> listaAnexoAntiga;
		
		Indicador indicadorNovo = new Indicador();
		indicadorNovo.setUnidadeGerencial(ugNova);
		indicadorNovo.setObjetivoMapaEstrategico(objetivoMapaEstrategicoNovo);
		indicadorNovo.setNome(indicadorAntigo.getNome());
		indicadorNovo.setDescricao(indicadorAntigo.getDescricao());
		indicadorNovo.setPeso(indicadorAntigo.getPeso());
		indicadorNovo.setPrecisao(indicadorAntigo.getPrecisao());
		indicadorNovo.setTolerancia(indicadorAntigo.getTolerancia());
		indicadorNovo.setMelhor(indicadorAntigo.getMelhor());
		indicadorNovo.setFrequencia(indicadorAntigo.getFrequencia());
		indicadorNovo.setUnidadeMedida(indicadorAntigo.getUnidadeMedida());
		indicadorNovo.setStatus(indicadorAntigo.getStatus());
		indicadorNovo.setResponsavel(indicadorAntigo.getResponsavel());
		indicadorNovo.setRelevancia(indicadorAntigo.getRelevancia());
		indicadorNovo.setFrequenciaAcompanhamento(indicadorAntigo.getFrequenciaAcompanhamento());
		indicadorNovo.setMecanismoControle(indicadorAntigo.getMecanismoControle());
		indicadorNovo.setFonteDados(indicadorAntigo.getFonteDados());
		indicadorNovo.setFormulaCalculo(indicadorAntigo.getFormulaCalculo());
		
		// VALORES A SEREM ALCANÇADOS
		listaAcompanhamentoIndicadorAntigo = acompanhamentoIndicadorService.obtemAcompanhamentos(indicadorAntigo);
		listaAcompanhamentoIndicadorNovo = new ArrayList<AcompanhamentoIndicador>();
		for (AcompanhamentoIndicador acompanhamentoIndicadorAntigo : listaAcompanhamentoIndicadorAntigo) {
			acompanhamentoIndicadorNovo = new AcompanhamentoIndicador();
			
			Calendar calDataInicial = CalendarComparator.getDataAtualSemHora();
			calDataInicial.set(Calendar.DAY_OF_MONTH, acompanhamentoIndicadorAntigo.getDataInicial().get(Calendar.DAY_OF_MONTH));
			calDataInicial.set(Calendar.MONTH, acompanhamentoIndicadorAntigo.getDataInicial().get(Calendar.MONTH));
			calDataInicial.set(Calendar.YEAR, ugNova.getPlanoGestao().getAnoExercicio());
			acompanhamentoIndicadorNovo.setDataInicial(calDataInicial);
			
			Calendar calDataFinal = CalendarComparator.getDataAtualSemHora();
			calDataFinal.set(Calendar.DAY_OF_MONTH, acompanhamentoIndicadorAntigo.getDataFinal().get(Calendar.DAY_OF_MONTH));
			calDataFinal.set(Calendar.MONTH, acompanhamentoIndicadorAntigo.getDataFinal().get(Calendar.MONTH));
			calDataFinal.set(Calendar.YEAR, ugNova.getPlanoGestao().getAnoExercicio());
			acompanhamentoIndicadorNovo.setDataFinal(calDataFinal);
			
			acompanhamentoIndicadorNovo.setIndice(acompanhamentoIndicadorAntigo.getIndice());
			acompanhamentoIndicadorNovo.setValorBaseOK(acompanhamentoIndicadorAntigo.getValorBaseOK());
			acompanhamentoIndicadorNovo.setValorLimiteSuperior(acompanhamentoIndicadorAntigo.getValorLimiteSuperior());
			acompanhamentoIndicadorNovo.setValorLimiteInferior(acompanhamentoIndicadorAntigo.getValorLimiteInferior());
			acompanhamentoIndicadorNovo.setPercentualTolerancia(acompanhamentoIndicadorAntigo.getPercentualTolerancia());
			acompanhamentoIndicadorNovo.setNaoaplicavel(acompanhamentoIndicadorAntigo.getNaoaplicavel());
			
			listaAcompanhamentoIndicadorNovo.add(acompanhamentoIndicadorNovo);
		}
		indicadorNovo.setAcompanhamentosIndicador(GeplanesUtils.listToSet(listaAcompanhamentoIndicadorNovo, AcompanhamentoIndicador.class));
		
		// Salva o novo indicador
		if (useTransaction) {
			this.saveOrUpdate(indicadorNovo);
		}
		else {
			this.saveOrUpdateWithoutTransaction(indicadorNovo);
		}
		
		// ANEXOS
		listaAnexoAntiga = anexoIndicadorService.findByIndicador(indicadorAntigo);
		for (AnexoIndicador anexo : listaAnexoAntiga) {
			anexo.setId(null);
			anexo.setIndicador(indicadorNovo);
			if(anexo.getArquivo() != null){
				anexo.getArquivo().setId(null);
			}
			anexoIndicadorService.salvaAnexoIndicador(anexo, useTransaction);
		}
	}
	
	/**
	 * Verifica se a data limite para a criação de um determinado indicador está expirada.
	 * 
	 * @param indicador
	 * @return
	 */
	public boolean dataLimiteCriacaoNaoExpirada(Indicador indicador) {		
				
		PlanoGestao planoGestao;
		
		if (indicador.getObjetivoMapaEstrategico() == null) {
			indicador = this.loadWithUnidadePlanoGestao(indicador);
			planoGestao = indicador.getUnidadeGerencial().getPlanoGestao();
			planoGestao = planoGestaoService.load(planoGestao);
		}
		else {
			planoGestao = indicador.getUnidadeGerencial().getPlanoGestao();
			if (planoGestao.getLimiteCriacaoMetasIndicadores() == null) {
				planoGestao = planoGestaoService.load(planoGestao);
			}
		}
		
		if (planoGestaoService.dataLimiteCriacaoMetasIndicadoresNaoExpirada(planoGestao)) {				
			return true;
		}
		
		return false;
	}	
}
