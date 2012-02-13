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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.core.web.WebApplicationContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.Atividade;
import br.com.linkcom.sgm.beans.Competencia;
import br.com.linkcom.sgm.beans.ControleCadastro;
import br.com.linkcom.sgm.beans.ControleCadastroItem;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.MapaCompetencia;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.MapaNegocio;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.ItemControleCadastroEnum;
import br.com.linkcom.sgm.dao.PlanoGestaoDAO;
import br.com.linkcom.sgm.report.bean.PendenciaCadastroReportBean;
import br.com.linkcom.sgm.util.CalendarComparator;
import br.com.linkcom.sgm.util.neo.service.GenericService;


public class PlanoGestaoService extends GenericService<PlanoGestao> {
	
	private static PlanoGestaoService instance;	
	
	private PlanoGestaoDAO planoGestaoDAO;
	private UnidadeGerencialService unidadeGerencialService;
	private ControleCadastroService controleCadastroService;
	private UsuarioUnidadeGerencialService usuarioUnidadeGerencialService;
	private MapaNegocioService mapaNegocioService;
	private MapaCompetenciaService mapaCompetenciaService;
	private MapaEstrategicoService mapaEstrategicoService;
	private MatrizFCSService matrizFCSService;
	private IndicadorService indicadorService;
	
	public void setPlanoGestaoDAO(PlanoGestaoDAO planoGestaoDAO) {
		this.planoGestaoDAO = planoGestaoDAO;
	}

	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setControleCadastroService(ControleCadastroService controleCadastroService) {this.controleCadastroService = controleCadastroService;}
	public void setUsuarioUnidadeGerencialService(UsuarioUnidadeGerencialService usuarioUnidadeGerencialService) {this.usuarioUnidadeGerencialService = usuarioUnidadeGerencialService;}
	public void setMapaNegocioService(MapaNegocioService mapaNegocioService) {this.mapaNegocioService = mapaNegocioService;}
	public void setMapaCompetenciaService(MapaCompetenciaService mapaCompetenciaService) {this.mapaCompetenciaService = mapaCompetenciaService;}
	public void setMapaEstrategicoService(MapaEstrategicoService mapaEstrategicoService) {this.mapaEstrategicoService = mapaEstrategicoService;}
	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {this.matrizFCSService = matrizFCSService;}
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}

	public PlanoGestao obtemAnoExercicio(PlanoGestao planoGestao) {
		return planoGestaoDAO.obtemAnoExercicio(planoGestao); 
	}
	
	public List<PlanoGestao> findLembreteCriacaoMetasIndicadoresNaoEnviado() {
		return planoGestaoDAO.findLembreteCriacaoMetasIndicadoresNaoEnviado();
	}
	
	public PlanoGestao findByIndicador(Indicador indicador) {
		return planoGestaoDAO.findByIndicador(indicador);
	}	
	
	/**
	 * Verifica se a data limite para criação de metas e indicadores já foi expirada
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return verdadeiro ou falso
	 */	
	public Boolean dataLimiteCriacaoMetasIndicadoresNaoExpirada(PlanoGestao planoGestao) {
		
		if(planoGestao == null || planoGestao.getId() == null)			
			return null;
		
		if(planoGestao.getLimiteCriacaoMetasIndicadores() == null)
			planoGestao = this.load(planoGestao);
		
		Calendar limiteCriacaoMetasIndicadoresCalendar = new GregorianCalendar();
		limiteCriacaoMetasIndicadoresCalendar.setTime(planoGestao.getLimiteCriacaoMetasIndicadores());
		
		if  (CalendarComparator.getDataAtualSemHora().compareTo(limiteCriacaoMetasIndicadoresCalendar) <= 0)		
			return true;
		else
			return false;
	}
	
	/**
	 * Verifica se a data limite para lançamento de resultados está expirada.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return verdadeiro ou falso
	 */
	public Boolean isDtLimLancResultadosExpirada(PlanoGestao planoGestao, Integer trimestre) {
		
		if (planoGestao == null || planoGestao.getId() == null) {			
			return null;
		}
		
		if (trimestre == null || trimestre.intValue() < 0 || trimestre.intValue() > 3) {
			return null;
		}
		
		Calendar calLimite = new GregorianCalendar();
		// 1º Trimestre
		if (trimestre.intValue() == 0) {
			calLimite.setTime(planoGestao.getDtLimLancRes1T());
		}
		// 2º Trimestre
		else if (trimestre.intValue() == 1) {
			calLimite.setTime(planoGestao.getDtLimLancRes2T());
		}
		// 3º Trimestre
		else if (trimestre.intValue() == 2) {
			calLimite.setTime(planoGestao.getDtLimLancRes3T());
		}
		// 4º Trimestre
		else {
			calLimite.setTime(planoGestao.getDtLimLancRes4T());
		}
		
		if (CalendarComparator.getDataAtualSemHora().compareTo(calLimite) > 0) {		
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Verifica se a data para travamento do lançamento de resultados está expirada.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return verdadeiro ou falso
	 */
	public Boolean isDtTravLancResultadosExpirada(PlanoGestao planoGestao, Integer trimestre) {
		
		if (planoGestao == null || planoGestao.getId() == null) {			
			return null;
		}
		
		if (trimestre == null || trimestre.intValue() < 0 || trimestre.intValue() > 3) {
			return null;
		}
		
		Calendar calTravamento = new GregorianCalendar();
		// 1º Trimestre
		if (trimestre.intValue() == 0) {
			calTravamento.setTime(planoGestao.getDtTravLancRes1T());
		}
		// 2º Trimestre
		else if (trimestre.intValue() == 1) {
			calTravamento.setTime(planoGestao.getDtTravLancRes2T());
		}
		// 3º Trimestre
		else if (trimestre.intValue() == 2) {
			calTravamento.setTime(planoGestao.getDtTravLancRes3T());
		}
		// 4º Trimestre
		else {
			calTravamento.setTime(planoGestao.getDtTravLancRes4T());
		}
		
		if (CalendarComparator.getDataAtualSemHora().compareTo(calTravamento) > 0) {		
			return true;
		}
		else {
			return false;
		}
	}	
	
	/**
	 * Verifica se a data limite para criação do Mapa do Negócio já foi ultrapassada.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return true, caso a data limite não esteja expirada e false, caso contrário.
	 */
	public Boolean dataLimiteCriacaoMapaNegocioNaoExpirada(PlanoGestao planoGestao) {
		
		if (planoGestao == null)			
			return null;
		
		if (planoGestao.getId() != null) {
			planoGestao = this.load(planoGestao);
		}
		
		Calendar limiteCriacaoMapaNegocioCalendar = new GregorianCalendar();
		limiteCriacaoMapaNegocioCalendar.setTime(planoGestao.getLimiteCriacaoMapaNegocio());
		
		if (CalendarComparator.getDataAtualSemHora().compareTo(limiteCriacaoMapaNegocioCalendar) <= 0) {		
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Verifica se a data limite para criação do Mapa de Competências já foi ultrapassada.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return true, caso a data limite não esteja expirada e false, caso contrário.
	 */
	public Boolean dataLimiteCriacaoMapaCompetenciaNaoExpirada(PlanoGestao planoGestao) {
		
		if (planoGestao == null)			
			return null;
		
		if (planoGestao.getId() != null) {
			planoGestao = this.load(planoGestao);
		}
		
		Calendar limiteCriacaoMapaCompetenciaCalendar = new GregorianCalendar();
		limiteCriacaoMapaCompetenciaCalendar.setTime(planoGestao.getLimiteCriacaoMapaCompetencia());
		
		if (CalendarComparator.getDataAtualSemHora().compareTo(limiteCriacaoMapaCompetenciaCalendar) <= 0) {		
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Verifica se a data limite para criação do Mapa do Estratégico já foi ultrapassada.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return true, caso a data limite não esteja expirada e false, caso contrário.
	 */
	public Boolean dataLimiteCriacaoMapaEstrategicoNaoExpirada(PlanoGestao planoGestao) {
		
		if (planoGestao == null)			
			return null;
		
		if (planoGestao.getId() != null) {
			planoGestao = this.load(planoGestao);
		}
		
		Calendar limiteCriacaoMapaEstrategicoCalendar = new GregorianCalendar();
		limiteCriacaoMapaEstrategicoCalendar.setTime(planoGestao.getLimiteCriacaoMapaEstrategico());
		
		if (CalendarComparator.getDataAtualSemHora().compareTo(limiteCriacaoMapaEstrategicoCalendar) <= 0) {		
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Verifica se a data limite para criação da Matriz FCS já foi ultrapassada.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return true, caso a data limite não esteja expirada e false, caso contrário.
	 */
	public Boolean dataLimiteCriacaoMatrizFcsNaoExpirada(PlanoGestao planoGestao) {
		
		if (planoGestao == null)			
			return null;
		
		if (planoGestao.getId() != null) {
			planoGestao = this.load(planoGestao);
		}
		
		Calendar limiteCriacaoMatrizFcsCalendar = new GregorianCalendar();
		limiteCriacaoMatrizFcsCalendar.setTime(planoGestao.getLimiteCriacaoMatrizFcs());
		
		if (CalendarComparator.getDataAtualSemHora().compareTo(limiteCriacaoMatrizFcsCalendar) <= 0) {		
			return true;
		}
		else {
			return false;
		}
	}	
	
	@Override
	public void delete(PlanoGestao bean) {
		unidadeGerencialService.excluiEmCascata(unidadeGerencialService.findNodosRaiz(bean));
		super.delete(bean);
	}
	
	public PlanoGestao obtemPlanoGestaoAtual(){
		PlanoGestaoDAO dao = (PlanoGestaoDAO) getGenericDAO();
		PlanoGestao planoGestao = dao.obtemPlanoGestaoAnoAtual();
		if (planoGestao == null){
			planoGestao = dao.obtemPlanoGestaoComMaiorAnoExercicio();
		}
		return planoGestao;
	}
	
	/**
	 * Gera um relatório informando quais as unidades gerenciais possuem pendência no cadastro de
	 * indicadores, valores a serem alcançados e resultados.
	 * 
	 * @author Rodrigo Alvarenga
	 * 
	 * @param planoGestao
	 * @param listaUnidadeGerencial
	 * @return relatório
	 */
	public IReport createReportPendenciaCadastro(PlanoGestao planoGestao, List<UnidadeGerencial> listaUnidadeGerencial) {
		Report report = new Report("../relatorio/pendenciaCadastro");
		
		//Listas
		List<PendenciaCadastroReportBean> listaReportBean = new ArrayList<PendenciaCadastroReportBean>();
		List<ControleCadastro> listaControleCadastro;
		
		//Beans
		ServletContext context = ((WebApplicationContext) NeoWeb.getApplicationContext()).getServletContext();
		PendenciaCadastroReportBean reportBean;
		
		//Geral
		String imagemCadastroOk      = "images/ico_cadastro_ok.png";
		String imagemCadastroNaoOk   = "images/ico_cadastro_nao_ok.png";
		String imagemCadastroNaoDisp = "images/ico_cadastro_nao_disponivel.png";
		boolean encontrouValorBasePendente;
		boolean encontrouValorRealPendente;
		boolean encontrouTratamentoAnomaliaPendente;
		boolean encontrouPlanoAcaoIniciativaPendente;
		
		planoGestao = this.load(planoGestao);
		
		listaControleCadastro = controleCadastroService.geraListaControleCadastro(planoGestao, listaUnidadeGerencial);
		
		if (listaControleCadastro != null && !listaControleCadastro.isEmpty()) {
			for (ControleCadastro controleCadastro : listaControleCadastro) {
				reportBean = new PendenciaCadastroReportBean();
				reportBean.setUnidadeGerencial(controleCadastro.getUnidadeGerencial().getSigla());
				
				encontrouValorBasePendente = false;
				encontrouValorRealPendente = false;
				encontrouTratamentoAnomaliaPendente = false;
				encontrouPlanoAcaoIniciativaPendente = false;
				
				if (controleCadastro.getListaControleCadastroItem() != null && !controleCadastro.getListaControleCadastroItem().isEmpty()) {
					for (ControleCadastroItem controleCadastroItem : controleCadastro.getListaControleCadastroItem()) {
						// Verificação do Mapa do Negócio
						if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.MAPA_NEGOCIO)) {
							reportBean.setImgMapaNegocio(controleCadastroItem.getPendente() == null ? context.getResourceAsStream(imagemCadastroNaoDisp) : controleCadastroItem.getPendente() ? context.getResourceAsStream(imagemCadastroNaoOk) : context.getResourceAsStream(imagemCadastroOk));		
						}
						
						// Verificação do Mapa Estratégico
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.MAPA_ESTRATEGICO)) {
							reportBean.setImgMapaEstrategico(controleCadastroItem.getPendente() == null ? context.getResourceAsStream(imagemCadastroNaoDisp) : controleCadastroItem.getPendente() ? context.getResourceAsStream(imagemCadastroNaoOk) : context.getResourceAsStream(imagemCadastroOk));		
						}
						
						// Verificação do Mapa de Competências
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.MAPA_COMPETENCIA)) {
							reportBean.setImgMapaCompetencia(controleCadastroItem.getPendente() == null ? context.getResourceAsStream(imagemCadastroNaoDisp) : controleCadastroItem.getPendente() ? context.getResourceAsStream(imagemCadastroNaoOk) : context.getResourceAsStream(imagemCadastroOk));		
						}
						
						// Verificação da Matriz de Iniciativas x FCS
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.MATRIZ_FCS)) {
							reportBean.setImgMatrizFCS(controleCadastroItem.getPendente() == null ? context.getResourceAsStream(imagemCadastroNaoDisp) : controleCadastroItem.getPendente() ? context.getResourceAsStream(imagemCadastroNaoOk) : context.getResourceAsStream(imagemCadastroOk));		
						}
						
						// Verificação do Cadastro de Indicadores
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.INDICADORES)) {
							reportBean.setImgIndicador(controleCadastroItem.getPendente() == null ? context.getResourceAsStream(imagemCadastroNaoDisp) : controleCadastroItem.getPendente() ? context.getResourceAsStream(imagemCadastroNaoOk) : context.getResourceAsStream(imagemCadastroOk));
						}
						
						// Verificação do Cadastro de Valores a Serem Alcançados
						// Se já encontrou um registro de valor base pendente, não precisa verificar os demais (de valor base).
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.VALORES_BASE) && !encontrouValorBasePendente) {
							if (controleCadastroItem.getPendente() == null) {
								reportBean.setImgValorBase(context.getResourceAsStream(imagemCadastroNaoDisp));
							}
							else {
								if (controleCadastroItem.getPendente()) {
									reportBean.setImgValorBase(context.getResourceAsStream(imagemCadastroNaoOk));
									encontrouValorBasePendente = true;
								}
								else {
									reportBean.setImgValorBase(context.getResourceAsStream(imagemCadastroOk));
								}
							}
						}
						
						// Verificação do Cadastro de Valores Obtidos
						// Se já encontrou um registro de valor real pendente, não precisa verificar os demais (de valor real).
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.VALORES_REAIS) && !encontrouValorRealPendente) {
							if (controleCadastroItem.getPendente() == null) {
								reportBean.setImgValorReal(context.getResourceAsStream(imagemCadastroNaoDisp));
							}
							else {
								if (controleCadastroItem.getPendente()) {
									reportBean.setImgValorReal(context.getResourceAsStream(imagemCadastroNaoOk));
									encontrouValorRealPendente = true;
								}
								else {
									reportBean.setImgValorReal(context.getResourceAsStream(imagemCadastroOk));
								}
							}
						}
						
						// Verificação do Tratamento de Anomalias
						// Se já encontrou um registro de anomalia pendente, não precisa verificar os demais (de anomalia).
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.TRATAMENTO_ANOMALIA) && !encontrouTratamentoAnomaliaPendente) {
							if (controleCadastroItem.getPendente() == null) {
								reportBean.setImgTratamentoAnomalia(context.getResourceAsStream(imagemCadastroNaoDisp));
							}
							else {
								if (controleCadastroItem.getPendente()) {
									reportBean.setImgTratamentoAnomalia(context.getResourceAsStream(imagemCadastroNaoOk));
									encontrouTratamentoAnomaliaPendente = true;
								}
								else {
									reportBean.setImgTratamentoAnomalia(context.getResourceAsStream(imagemCadastroOk));
								}
							}				
						}						
						
						// Verificação dos Planos de Ação das Iniciativas
						// Se já encontrou um registro de plano de ação de iniciativa pendente, não precisa verificar os demais (de plano de ação de iniciativa).						
						else if (controleCadastroItem.getItemControleCadastroEnum().equals(ItemControleCadastroEnum.PLANO_ACAO_INICIATIVA) && !encontrouPlanoAcaoIniciativaPendente) {
							if (controleCadastroItem.getPendente() == null) {
								reportBean.setImgPlanoAcaoIniciativa(context.getResourceAsStream(imagemCadastroNaoDisp));
							}
							else {
								if (controleCadastroItem.getPendente()) {
									reportBean.setImgPlanoAcaoIniciativa(context.getResourceAsStream(imagemCadastroNaoOk));
									encontrouPlanoAcaoIniciativaPendente = true;
								}
								else {
									reportBean.setImgPlanoAcaoIniciativa(context.getResourceAsStream(imagemCadastroOk));
								}
							}
						}						
					}
				}
				listaReportBean.add(reportBean);
			}
		}
		
		report.addParameter("PLANOGESTAO", planoGestao.getAnoExercicio().toString());
		report.addParameter("IMGLEGENDA1", context.getResourceAsStream(imagemCadastroOk));
		report.addParameter("IMGLEGENDA2", context.getResourceAsStream(imagemCadastroNaoOk));
		report.addParameter("IMGLEGENDA3", context.getResourceAsStream(imagemCadastroNaoDisp));
		
		report.setDataSource(listaReportBean);
		return report;
	}
	
	/**
	 * Copia o ano da gestão, suas unidades gerenciais, seus mapas, matriz e indicadores.
	 * 
	 * @param planoGestaoAntigo
	 * @param planoGestaoNovo
	 */
	public void copiaPlanoGestao(final PlanoGestao planoGestaoAntigo, final PlanoGestao planoGestaoNovo) {
		getGenericDAO().getTransactionTemplate().execute(new TransactionCallback(){

			public Object doInTransaction(TransactionStatus arg0) {
				
				//Mapas
				Map<UnidadeGerencial, UnidadeGerencial> mapaUG = new HashMap<UnidadeGerencial, UnidadeGerencial>();
				Map<Integer, Integer> mapaObjetivoMapaEstrategicoGlobal = new LinkedHashMap<Integer, Integer>();
				Map<Integer, Integer> mapaObjetivoMapaEstrategicoUG;
				
				//Objetos
				UnidadeGerencial uGNova;
				UnidadeGerencial subUGNova;
				MapaCompetencia mapaCompetencia;
				MapaEstrategico mapaEstrategico;
				MapaNegocio mapaNegocio;
				Integer idObjetivoMapaEstrategicoAntigo;
				Integer idObjetivoMapaEstrategicoNovo;
				ObjetivoMapaEstrategico objetivoMapaEstrategicoNovo;
				
				//Listas
				List<Indicador> listaIndicadorAntigo;
				
				// Salva o novo Plano de Gestão
				saveOrUpdateWithoutTransaction(planoGestaoNovo);				
				
				// Busca as UGs vinculadas ao Plano de gestão antigo
				// ATENÇÃO: Para que a cópia funcione corretamente, é imprescindível que as UGs superiores venham antes das subordinadas na lista.
				List<UnidadeGerencial> listaUnidadesGerenciaisAntigas = unidadeGerencialService.findByPlanoGestaoOrdenadasPorHierarquia(planoGestaoAntigo);
				if (listaUnidadesGerenciaisAntigas != null) {
					for (UnidadeGerencial uGAntiga : listaUnidadesGerenciaisAntigas) {
						
						//Grava as novas UGs, mas sem as subordinações
						uGNova = new UnidadeGerencial();
						uGNova.setNome(uGAntiga.getNome());
						uGNova.setSigla(uGAntiga.getSigla());
						uGNova.setNivelHierarquico(uGAntiga.getNivelHierarquico());
						uGNova.setNivelNum(uGAntiga.getNivelNum());
						uGNova.setAreaQualidade(uGAntiga.getAreaQualidade());
						uGNova.setAreaAuditoriaInterna(uGAntiga.getAreaAuditoriaInterna());
						uGNova.setPermitirMapaCompetencia(uGAntiga.getPermitirMapaCompetencia());
						uGNova.setPermitirMapaEstrategico(uGAntiga.getPermitirMapaEstrategico());
						uGNova.setPermitirMapaNegocio(uGAntiga.getPermitirMapaNegocio());
						uGNova.setPermitirMatrizFcs(uGAntiga.getPermitirMatrizFcs());
						uGNova.setPlanoGestao(planoGestaoNovo);
						uGNova.setSubordinacao(uGAntiga.getSubordinacao());
						
						List<UsuarioUnidadeGerencial> listaUsuariosUnidadeGerencialAntiga = usuarioUnidadeGerencialService.findByUnidadeGerencial(uGAntiga);
						if (listaUsuariosUnidadeGerencialAntiga != null && !listaUsuariosUnidadeGerencialAntiga.isEmpty()) {
							Set<UsuarioUnidadeGerencial> usuariosUnidadeGerencialNova = new HashSet<UsuarioUnidadeGerencial>();
							for (UsuarioUnidadeGerencial usuarioUnidadeGerencialAntiga : listaUsuariosUnidadeGerencialAntiga) {
								usuarioUnidadeGerencialAntiga.setId(null);
								usuariosUnidadeGerencialNova.add(usuarioUnidadeGerencialAntiga);
							}
							uGNova.setUsuariosUnidadeGerencial(usuariosUnidadeGerencialNova);
						}
						
						unidadeGerencialService.saveOrUpdateWithoutTransaction(uGNova);
						
						// Mapa de Competências
						mapaCompetencia = mapaCompetenciaService.loadByUnidadeGerencial(uGAntiga);
						if (mapaCompetencia != null) {
							
							mapaCompetencia.setId(null);
							
							if (mapaCompetencia.getAtividades() != null) {
								for (Atividade atividade : mapaCompetencia.getAtividades()) {
									atividade.setId(null);
								}
							}
							
							if (mapaCompetencia.getCompetencias() != null) {
								for (Competencia competencia : mapaCompetencia.getCompetencias()) {
									competencia.setId(null);
								}
							}
							
							mapaCompetencia.setUnidadeGerencial(uGNova);
							
							mapaCompetenciaService.saveOrUpdateWithoutTransaction(mapaCompetencia);
						}
						
						// Mapa do Negócio
						mapaNegocio = mapaNegocioService.loadByUnidadeGerencial(uGAntiga);
						if (mapaNegocio != null) {
							mapaNegocio.setId(null);
							mapaNegocio.setUnidadeGerencial(uGNova);
							
							mapaNegocioService.saveOrUpdateWithoutTransaction(mapaNegocio);
						}				
						
						// Mapa Estratégico
						mapaObjetivoMapaEstrategicoUG = new LinkedHashMap<Integer, Integer>();
						mapaEstrategico = mapaEstrategicoService.loadByUnidadeGerencial(uGAntiga);
						if (mapaEstrategico != null) {
							
							mapaEstrategico.setId(null);
							
							if (mapaEstrategico.getListaPerspectivaMapaEstrategico() != null) {
								for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : mapaEstrategico.getListaPerspectivaMapaEstrategico()) {
									perspectivaMapaEstrategico.setId(null);
									
									if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null) {
										for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
											mapaObjetivoMapaEstrategicoUG.put(objetivoMapaEstrategico.getId(), null);
											objetivoMapaEstrategico.setId(null);
										}
									}
								}
							}
							
							mapaEstrategicoService.salvaDefinicaoObjetivoEstrategico(mapaEstrategico, uGNova, false);
						}
						
						// Preenchimento do mapa de vinculação entre IDs novos e antigos dos objetivos estratégicos.
						if (mapaEstrategico != null) {
							Iterator<Integer> itObjetivoMapaEstrategico = mapaObjetivoMapaEstrategicoUG.keySet().iterator();
							if (mapaEstrategico.getListaPerspectivaMapaEstrategico() != null) {
								for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : mapaEstrategico.getListaPerspectivaMapaEstrategico()) {
									if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null) {
										for (ObjetivoMapaEstrategico objMapaEstratNovo : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {				
											if (itObjetivoMapaEstrategico.hasNext()) {
												idObjetivoMapaEstrategicoAntigo = itObjetivoMapaEstrategico.next(); 
												mapaObjetivoMapaEstrategicoUG.put(idObjetivoMapaEstrategicoAntigo, objMapaEstratNovo.getId());
											}
										}
									}
								}
							}
						}
						
						// Adicionando os objetivos estratégicos da unidade gerencial no mapa de objetivos estratégicos global
						if (mapaObjetivoMapaEstrategicoUG != null && !mapaObjetivoMapaEstrategicoUG.isEmpty()) {
							Iterator<Integer> itObjetivoMapaEstrategicoUG = mapaObjetivoMapaEstrategicoUG.keySet().iterator();
							Integer idAntigo;
							Integer idNovo;
							while (itObjetivoMapaEstrategicoUG.hasNext()) {
								idAntigo = itObjetivoMapaEstrategicoUG.next();
								idNovo   = mapaObjetivoMapaEstrategicoUG.get(idAntigo);
								mapaObjetivoMapaEstrategicoGlobal.put(idAntigo, idNovo);
							}
						}
						
						// Matriz FCS
						matrizFCSService.copiaMatriz(uGAntiga, uGNova, mapaObjetivoMapaEstrategicoGlobal, false);
						
						// Indicadores
						listaIndicadorAntigo = indicadorService.findByUnidadeGerencialObjetivoEstrategico(uGAntiga, null, true, true, false, false);
						if (listaIndicadorAntigo != null) {
							for (Indicador indicadorAntigo : listaIndicadorAntigo) {
								
								idObjetivoMapaEstrategicoAntigo = indicadorAntigo.getObjetivoMapaEstrategico().getId();
								
								idObjetivoMapaEstrategicoNovo = mapaObjetivoMapaEstrategicoGlobal.get(idObjetivoMapaEstrategicoAntigo);
								objetivoMapaEstrategicoNovo = new ObjetivoMapaEstrategico(idObjetivoMapaEstrategicoNovo);								
								
								indicadorService.copiaIndicador(uGNova, objetivoMapaEstrategicoNovo, indicadorAntigo, false);
							}
						}						
						
						mapaUG.put(uGAntiga, uGNova);
					}
					
					//Busca a descrição das subordinações antigas, recupera o id das novas com a descrição obtida e depois atualiza as subordinações
					for (UnidadeGerencial UGAntiga : listaUnidadesGerenciaisAntigas) {
						uGNova = mapaUG.get(UGAntiga);
						
						if (UGAntiga.getSubordinacao() != null) {
							subUGNova = unidadeGerencialService.loadByNomePlanoGestao(UGAntiga.getSubordinacao().getNome(), planoGestaoNovo);
							uGNova.setSubordinacao(subUGNova);
							unidadeGerencialService.saveOrUpdateWithoutTransaction(uGNova);
						}
					}
				}
				
				return null;
			}
		});
	}
	
	public static PlanoGestaoService getInstance(){
		if(instance == null){
			instance = Neo.getObject(PlanoGestaoService.class);
		}
		return instance;
	}	
}
