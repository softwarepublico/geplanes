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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.MatrizFCSFator;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativa;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativaFator;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.MatrizFCSFiltro;
import br.com.linkcom.sgm.controller.report.filtro.MatrizFCSReportFiltro;
import br.com.linkcom.sgm.dao.MatrizFCSDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.report.bean.MatrizFCSItemReportBean;
import br.com.linkcom.sgm.report.bean.MatrizFCSReportBean;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class MatrizFCSService extends GenericService<MatrizFCS>{

	private MatrizFCSDAO matrizFCSDAO;
	private MatrizFCSIniciativaService matrizFCSIniciativaService;
	private MatrizFCSIniciativaFatorService matrizFCSIniciativaFatorService;
	private MatrizFCSFatorService matrizFCSFatorService;
	private UsuarioService usuarioService;
	private UnidadeGerencialService unidadeGerencialService;
	
	public void setMatrizFCSDAO(MatrizFCSDAO matrizFCSDAO) {
		this.matrizFCSDAO = matrizFCSDAO;
	}
	
	public void setMatrizFCSIniciativaService(MatrizFCSIniciativaService matrizFCSIniciativaService) {
		this.matrizFCSIniciativaService = matrizFCSIniciativaService;
	}

	public void setMatrizFCSIniciativaFatorService(MatrizFCSIniciativaFatorService matrizFCSIniciativaFatorService) {
		this.matrizFCSIniciativaFatorService = matrizFCSIniciativaFatorService;
	}

	public void setMatrizFCSFatorService(MatrizFCSFatorService matrizFCSFatorService) {
		this.matrizFCSFatorService = matrizFCSFatorService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public MatrizFCS loadInfoMatriz(MatrizFCS matrizFCS){
		return matrizFCSDAO.loadInfoMatriz(matrizFCS);
	}
	
	public MatrizFCS carregarMatriz(MatrizFCS matrizFCS){
		return matrizFCSDAO.carregarMatriz(matrizFCS);
	}
	
	public void salvaMatriz(MatrizFCSFiltro matrizFCSFiltro, boolean useTransaction) {
		List<MatrizFCSFator> listaMatrizFcsFatorForDelete = matrizFCSFiltro.getListaMatrizFCSFatorForDelete();
		List<MatrizFCSIniciativa> listaMatrizFcsIniciativaForDelete = matrizFCSFiltro.getListaMatrizFCSIniciativaForDelete();
		
		MatrizFCS matrizFCS = matrizFCSFiltro.getMatrizFCS();
		List<MatrizFCSIniciativa> listaMatrizFcsIniciativa = matrizFCS.getListaMatrizFcsIniciativa();
		List<MatrizFCSFator> listaMatrizFcsFator = matrizFCS.getListaMatrizFcsFator();
		
		// Remove do banco de dados as relações iniciativa x fcs excluídas pelo usuário.		
		if (listaMatrizFcsFatorForDelete != null && !listaMatrizFcsFatorForDelete.isEmpty()) {
			for (MatrizFCSFator matrizFCSFatorForDelete : listaMatrizFcsFatorForDelete) {
				matrizFCSIniciativaFatorService.deleteFromMatrizFator(matrizFCSFatorForDelete);				
			}
		}
		
		// Salva a matriz FCS
		// Remove automaticamente os fcs que foram excluídos pelo usuário.
		if (useTransaction) {
			saveOrUpdate(matrizFCS);
		}
		else {
			saveOrUpdateWithoutTransaction(matrizFCS);
		}

		// Remove do banco de dados as iniciativas excluídas pelo usuário.
		if (listaMatrizFcsIniciativaForDelete != null && !listaMatrizFcsIniciativaForDelete.isEmpty()) {
			for (MatrizFCSIniciativa matrizFCSIniciativaForDelete : listaMatrizFcsIniciativaForDelete) {
				matrizFCSIniciativaFatorService.deleteFromMatrizIniciativa(matrizFCSIniciativaForDelete);
				matrizFCSIniciativaService.delete(matrizFCSIniciativaForDelete);
			}
		}

		// Inclui/atualiza as iniciativas adicionadas/alteradas pelo usuário.
		int i = 0;
		for (MatrizFCSIniciativa matrizFCSIniciativa : listaMatrizFcsIniciativa) {
			matrizFCSIniciativa.setPrioritaria(matrizFCSIniciativa.getPrioritaria() == null ? false : matrizFCSIniciativa.getPrioritaria());
			matrizFCSIniciativa.setMatrizFCS(matrizFCS);
			List<MatrizFCSIniciativaFator> listaMatrizFcsIniciativaFator = matrizFCSIniciativa.getListaMatrizFcsIniciativaFator();
			i=0;
			for (MatrizFCSIniciativaFator matrizFCSIniciativaFator : listaMatrizFcsIniciativaFator) {
				matrizFCSIniciativaFator.setMatrizFCSFator(listaMatrizFcsFator.get(i));
				i++;
			}
			if (useTransaction) {
				matrizFCSIniciativaService.saveOrUpdate(matrizFCSIniciativa);
			}
			else {
				matrizFCSIniciativaService.saveOrUpdateWithoutTransaction(matrizFCSIniciativa);
			}
		}
	}
	
	/***
	 * Busca os objetivos estratégicos que estão associados a uma determinada unidade gerencial
	 * através da matriz de iniciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<MatrizFCS> findWithEstrategiasByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return matrizFCSDAO.findWithEstrategiasByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Retorna as matrizes para uma unidade gerencial e um objetivo estratégico.
	 * Se o objetivo estratégico for nulo, retorna as matrizes para todos os objetivos da unidade gerencial.
	 * 
	 * @param unidadeGerencial
	 * @param objetivoMapaEstrategico
	 * @return
	 */
	public List<MatrizFCS> findByUnidadeGerencialObjetivoEstrategico(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		return matrizFCSDAO.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
	}	
	
	/**
	 * Exibe o relatório de Matriz de Iniciativas x FCS
	 * de acordo com o filtro solicitado.
	 *  
	 * @param filtro
	 * @return
	 */
	public IReport createMatrizFCSReport(MatrizFCSReportFiltro filtro) {
		
		boolean usuarioLogadoParticipanteUG = usuarioService.isUsuarioLogadoParticipanteUG(filtro.getUnidadeGerencial());
		boolean usuarioLogadoParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(filtro.getUnidadeGerencial());
		boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (!usuarioLogadoParticipanteUG && !usuarioLogadoParticipanteUGAncestral &&!usuarioLogadoIsAdmin) {
			throw new GeplanesException("Você não tem permissão para acessar os dados dessa Unidade Gerencial.");
		}
		
		// Carrega a matriz de acordo com o filtro solicitado.
		List<MatrizFCS> listaMatrizFCS = findByUnidadeGerencialObjetivoEstrategico(filtro.getUnidadeGerencial(), filtro.getObjetivoMapaEstrategico());
		
		// Verifica se existe alguma matriz cadastrada
		if (listaMatrizFCS == null || listaMatrizFCS.isEmpty()) {
			throw new GeplanesException("Não existe nenhuma Matriz de Iniciativas x FCS cadastrada para essa Unidade Gerencial.");
		}
		
		Report report = new Report("../relatorio/matrizFCS");
		
		// Carrega a descrição da UG e do Ano da Gestão
		filtro.setUnidadeGerencial(unidadeGerencialService.loadWithPlanoGestao(filtro.getUnidadeGerencial()));
		
		// Classifica e ordena a matriz de acordo com os impactos das iniciativas nos FCS
		classificaEOrdenaMatrizPorImpacto(listaMatrizFCS);
		
		List<MatrizFCSReportBean> listaMatrizFCSReportBean = new ArrayList<MatrizFCSReportBean>();
		
		MatrizFCSReportBean matrizFCSReportBean;
		List<MatrizFCSItemReportBean> listaMatrizFCSItemReportBean;
		MatrizFCSItemReportBean matrizFCSItemReportBean;
		
		for (MatrizFCS matrizFCS : listaMatrizFCS) {
			
			matrizFCSReportBean = new MatrizFCSReportBean();
			matrizFCSReportBean.setDescPlanoGestao(filtro.getUnidadeGerencial().getPlanoGestao().getAnoExercicio().toString());
			matrizFCSReportBean.setDescUnidadeGerencial(unidadeGerencialService.getDescricaoUnidadeGerencialComDescendencia(filtro.getUnidadeGerencial()));
			matrizFCSReportBean.setDescObjetivoEstrategico(matrizFCS.getObjetivoMapaEstrategico().getDescricao());
			
			listaMatrizFCSItemReportBean = new ArrayList<MatrizFCSItemReportBean>();
			
			if (matrizFCS.getListaMatrizFcsIniciativa() != null && !matrizFCS.getListaMatrizFcsIniciativa().isEmpty()) {
				for (MatrizFCSIniciativa matrizFCSIniciativa : matrizFCS.getListaMatrizFcsIniciativa()) {
					if (matrizFCSIniciativa.getListaMatrizFcsIniciativaFator() != null && !matrizFCSIniciativa.getListaMatrizFcsIniciativaFator().isEmpty()) {
						for (MatrizFCSIniciativaFator matrizFCSIniciativaFator : matrizFCSIniciativa.getListaMatrizFcsIniciativaFator()) {
							
							matrizFCSItemReportBean = new MatrizFCSItemReportBean();
							matrizFCSItemReportBean.setNomeIniciativa(matrizFCSIniciativa.getDescIniciativa() + (matrizFCSIniciativa.getPrioritaria() ? " (Prioritária)" : ""));
							matrizFCSItemReportBean.setNomeFatorCriticoSucesso(matrizFCSIniciativaFator.getMatrizFCSFator().getDescFator());
							matrizFCSItemReportBean.setDescAvaliacao(matrizFCSIniciativaFator.getItemFatorAvaliacao().getDescricao());		
							listaMatrizFCSItemReportBean.add(matrizFCSItemReportBean);
							
						}
					}
				}
			}
			matrizFCSReportBean.setListaMatrizFCSItemReportBean(listaMatrizFCSItemReportBean);
			listaMatrizFCSReportBean.add(matrizFCSReportBean);						
		}
		
		report.setDataSource(listaMatrizFCSReportBean);
		
		return report;
	}
	
	/**
	 * Soma os valores dos impactos de cada iniciativa da matriz nos diferentes fatores críticos de sucesso
	 * e depois ordena-os de forma decrescente.
	 * 
	 * @param listaMatrizFCS
	 */
	public void classificaEOrdenaMatrizPorImpacto(List<MatrizFCS> listaMatrizFCS) {
		if (listaMatrizFCS != null && !listaMatrizFCS.isEmpty()) {
			
			double soma;
			List<MatrizFCSFator> listaMatrizFCSFator;
			int i;
			
			for (MatrizFCS matrizFCS : listaMatrizFCS) {
				
				matrizFCS.setListaMatrizFcsIniciativa(matrizFCSIniciativaService.findByMatrizFCS(matrizFCS));
				listaMatrizFCSFator = matrizFCSFatorService.findByMatrizFCS(matrizFCS);
				
				if (matrizFCS.getListaMatrizFcsIniciativa() != null && !matrizFCS.getListaMatrizFcsIniciativa().isEmpty()) {
					for (MatrizFCSIniciativa matrizFCSIniciativa : matrizFCS.getListaMatrizFcsIniciativa()) {
						if (matrizFCSIniciativa.getListaMatrizFcsIniciativaFator() != null && !matrizFCSIniciativa.getListaMatrizFcsIniciativaFator().isEmpty()) {
							soma = 0;
							i = 0;
							for (MatrizFCSIniciativaFator matrizFCSIniciativaFator : matrizFCSIniciativa.getListaMatrizFcsIniciativaFator()) {
								matrizFCSIniciativaFator.setMatrizFCSFator(listaMatrizFCSFator.get(i));
								soma += matrizFCSIniciativaFator.getItemFatorAvaliacao().getValor();
								i++;
							}
							matrizFCSIniciativa.setTotalImpacto(soma);
						}
					}
				}
				
				// Ordena as iniciativas da matriz de acordo com o impacto
				Collections.sort(matrizFCS.getListaMatrizFcsIniciativa(), new Comparator<MatrizFCSIniciativa>() {

					public int compare(MatrizFCSIniciativa m1, MatrizFCSIniciativa m2) {
						return m2.getTotalImpacto().compareTo(m1.getTotalImpacto());
					}
					
				});
			}
		}
	}
	
	/**
	 * Remove todas as matrizes de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		matrizFCSDAO.deleteByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Carrega a matriz de iniciativas x fcs de uma determinada unidade gerencial.
	 *
	 * @param unidadeGerencial
	 * @return
	 */
	public MatrizFCS loadByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return matrizFCSDAO.loadByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Copia os dados da Matriz FCS de uma unidade gerencial antiga para uma unidade gerencial nova.
	 * 
	 * @param uGAntiga
	 * @param uGNova
	 * @param mapaObjetivoMapaEstrategico
	 * @param useTransaction
	 */
	public void copiaMatriz(UnidadeGerencial uGAntiga, UnidadeGerencial uGNova, Map<Integer, Integer> mapaObjetivoMapaEstrategico, boolean useTransaction) {
		
		//Objetos
		MatrizFCSFiltro matrizFCSFiltro;
		MatrizFCS matrizFCSNova;
		MatrizFCSFator matrizFCSFatorNova;
		MatrizFCSIniciativa matrizFCSIniciativaNova;
		MatrizFCSIniciativaFator matrizFCSIniciativaFatorNova;
		
		//Listas
		List<MatrizFCS> listaMatrizFCS;
		List<MatrizFCSFator> listaMatrizFCSFatorNova;
		List<MatrizFCSIniciativa> listaMatrizFCSIniciativaNova;
		List<MatrizFCSIniciativaFator> listaMatrizFCSIniciativaFatorNova;
		
		listaMatrizFCS = this.findByUnidadeGerencialObjetivoEstrategico(uGAntiga, null);
		if (listaMatrizFCS != null && !listaMatrizFCS.isEmpty()) {
			for (MatrizFCS matrizFCSAntiga : listaMatrizFCS) {
				
				matrizFCSNova = new MatrizFCS();
				matrizFCSNova.setFatorAvaliacao(matrizFCSAntiga.getFatorAvaliacao());
				matrizFCSNova.setObjetivoMapaEstrategico(new ObjetivoMapaEstrategico(mapaObjetivoMapaEstrategico.get(matrizFCSAntiga.getObjetivoMapaEstrategico().getId())));
				matrizFCSNova.setUnidadeGerencial(uGNova);				
				
				matrizFCSAntiga.setListaMatrizFcsIniciativa(matrizFCSIniciativaService.findByMatrizFCS(matrizFCSAntiga));
				matrizFCSAntiga.setListaMatrizFcsFator(matrizFCSFatorService.findByMatrizFCS(matrizFCSAntiga));
				
				if (matrizFCSAntiga.getListaMatrizFcsIniciativa() != null && !matrizFCSAntiga.getListaMatrizFcsIniciativa().isEmpty()) {
					listaMatrizFCSIniciativaNova = new ArrayList<MatrizFCSIniciativa>();
					for (MatrizFCSIniciativa matrizFCSIniciativaAntiga : matrizFCSAntiga.getListaMatrizFcsIniciativa()) {
						
						matrizFCSIniciativaNova = new MatrizFCSIniciativa();
						matrizFCSIniciativaNova.setDescIniciativa(matrizFCSIniciativaAntiga.getDescIniciativa());
						matrizFCSIniciativaNova.setPrioritaria(matrizFCSIniciativaAntiga.getPrioritaria());
						
						if (matrizFCSIniciativaAntiga.getListaMatrizFcsIniciativaFator() != null && !matrizFCSIniciativaAntiga.getListaMatrizFcsIniciativaFator().isEmpty()) {
							listaMatrizFCSIniciativaFatorNova = new ArrayList<MatrizFCSIniciativaFator>();
							for (MatrizFCSIniciativaFator matrizFCSIniciativaFatorAntiga : matrizFCSIniciativaAntiga.getListaMatrizFcsIniciativaFator()) {
								matrizFCSIniciativaFatorNova = new MatrizFCSIniciativaFator();
								matrizFCSIniciativaFatorNova.setItemFatorAvaliacao(matrizFCSIniciativaFatorAntiga.getItemFatorAvaliacao());
								
								listaMatrizFCSIniciativaFatorNova.add(matrizFCSIniciativaFatorNova);
							}
							matrizFCSIniciativaNova.setListaMatrizFcsIniciativaFator(listaMatrizFCSIniciativaFatorNova);
						}
						listaMatrizFCSIniciativaNova.add(matrizFCSIniciativaNova);
					}
					matrizFCSNova.setListaMatrizFcsIniciativa(listaMatrizFCSIniciativaNova);
				}
				
				if (matrizFCSAntiga.getListaMatrizFcsFator() != null && !matrizFCSAntiga.getListaMatrizFcsFator().isEmpty()) {
					listaMatrizFCSFatorNova = new ArrayList<MatrizFCSFator>();
					for (MatrizFCSFator matrizFCSFatorAntiga : matrizFCSAntiga.getListaMatrizFcsFator()) {
						matrizFCSFatorNova = new MatrizFCSFator();
						matrizFCSFatorNova.setDescFator(matrizFCSFatorAntiga.getDescFator());
						
						listaMatrizFCSFatorNova.add(matrizFCSFatorNova);
					}
					matrizFCSNova.setListaMatrizFcsFator(listaMatrizFCSFatorNova);
				}
				
				matrizFCSFiltro = new MatrizFCSFiltro();
				matrizFCSFiltro.setMatrizFCS(matrizFCSNova);
				this.salvaMatriz(matrizFCSFiltro, useTransaction);
			}
		}
	}
}
