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

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.MapaNegocio;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.controller.report.filtro.AlcanceMetaInstitucionalReportFiltro;
import br.com.linkcom.sgm.controller.report.filtro.MapaCompetenciaReportFiltro;
import br.com.linkcom.sgm.controller.report.filtro.MapaEstrategicoReportFiltro;
import br.com.linkcom.sgm.controller.report.filtro.MapaNegocioReportFiltro;
import br.com.linkcom.sgm.dao.UnidadeGerencialDAO;
import br.com.linkcom.sgm.report.MergeReport;
import br.com.linkcom.sgm.report.bean.AlcanceMetaInstitucionalReportBean;
import br.com.linkcom.sgm.report.bean.MapaDeNegocioReportBean;
import br.com.linkcom.sgm.util.calculos.CalculosPainelControle;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class UnidadeGerencialService extends GenericService<UnidadeGerencial> {
	
	private static UnidadeGerencialService instance;
	
	private UnidadeGerencialDAO unidadeGerencialDAO;
	private UsuarioUnidadeGerencialService usuarioUnidadeGerencialService;
	private UsuarioService usuarioService;
	private AtividadeService atividadeService;
	private CompetenciaService competenciaService;
	private PlanoGestaoService planoGestaoService;
	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;
	private MapaNegocioService mapaNegocioService;
	private PlanoAcaoService planoAcaoService;
	private AuditoriaGestaoService auditoriaGestaoService;
	private MatrizFCSService matrizFCSService;
	private IniciativaService iniciativaService;
	private IndicadorService indicadorService;
	private AnomaliaService anomaliaService;
	private OcorrenciaService ocorrenciaService;
	private AcaoPreventivaService acaoPreventivaService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	private AuditoriaInternaService auditoriaInternaService;
	private PainelIndicadorFiltroService painelIndicadorFiltroService;
	private NeoImageResolver neoImageResolver;
	
	public void setCompetenciaService(CompetenciaService competenciaService) {this.competenciaService = competenciaService;}
	public void setAtividadeService(AtividadeService atividadeService) {this.atividadeService = atividadeService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setUnidadeGerencialDAO(UnidadeGerencialDAO unidadeGerencialDAO) {this.unidadeGerencialDAO = unidadeGerencialDAO;}
	public void setUsuarioUnidadeGerencialService(UsuarioUnidadeGerencialService usuarioUnidadeGerencialService) { this.usuarioUnidadeGerencialService = usuarioUnidadeGerencialService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;}
	public void setMapaNegocioService(MapaNegocioService mapaNegocioService) {this.mapaNegocioService = mapaNegocioService;}
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {this.planoAcaoService = planoAcaoService;}
	public void setAuditoriaGestaoService(AuditoriaGestaoService auditoriaGestaoService) {this.auditoriaGestaoService = auditoriaGestaoService;}
	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {this.matrizFCSService = matrizFCSService;}
	public void setIniciativaService(IniciativaService iniciativaService) {this.iniciativaService = iniciativaService;}
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	public void setAnomaliaService(AnomaliaService anomaliaService) {this.anomaliaService = anomaliaService;}
	public void setOcorrenciaService(OcorrenciaService ocorrenciaService) {this.ocorrenciaService = ocorrenciaService;}
	public void setAcaoPreventivaService(AcaoPreventivaService acaoPreventivaService) {this.acaoPreventivaService = acaoPreventivaService;}
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;}
	public void setAuditoriaInternaService(AuditoriaInternaService auditoriaInternaService) {this.auditoriaInternaService = auditoriaInternaService;}
	public void setPainelIndicadorFiltroService(PainelIndicadorFiltroService painelIndicadorFiltroService) {this.painelIndicadorFiltroService = painelIndicadorFiltroService;}
	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {this.neoImageResolver = neoImageResolver;}
	
	public List<UnidadeGerencial> find(UnidadeGerencial pai, int niveis, PlanoGestao planoGestao) {
		return unidadeGerencialDAO.find(pai, niveis, planoGestao);
	}

	public UnidadeGerencial obtemSigla(UnidadeGerencial unidadeGerencial) {
		return ((UnidadeGerencialDAO)getGenericDAO()).obtemSigla(unidadeGerencial); 
	}
	/**
	 * Retorna o Plano de Gestao de uma determinada UG.
	 * @author Matheus Melo Gonçalves
	 * @param unidadeGerencial.
	 * @return Plano de Gestao.
	 */	
	public UnidadeGerencial obtemPlanoGestao(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.obtemPlanoGestao(unidadeGerencial);
	}	
	
	/**
	 * Lista todas as unidades gerenciais ancestrais de uma UG
	 * @author Rodrigo Alvarenga
	 * @param unidadeGerencial
	 * @return lista de UGs
	 */	
	public List<UnidadeGerencial> findAllParents(UnidadeGerencial unidadeGerencial) {
		List<UnidadeGerencial> list = new ArrayList<UnidadeGerencial>();
		if (unidadeGerencial == null) {
			return list;
		}
		unidadeGerencial = unidadeGerencialDAO.loadWithSiglaNomeSubordinacao(unidadeGerencial);
		
		while (unidadeGerencial.getSubordinacao() != null) {
			unidadeGerencial = unidadeGerencial.getSubordinacao();
			unidadeGerencial = unidadeGerencialDAO.loadWithSiglaNomeSubordinacao(unidadeGerencial);
			list.add(unidadeGerencial);			
		}
		return list;		
	}
	
	/**
	 * Retorna uma lista contendo as filhas de uma unidadeGerencial
	 * @author Rodrigo Duarte
	 * @param unidadeGerencial
	 * @return
	 */	
	public List<UnidadeGerencial> findFilhas(UnidadeGerencial bean) {		
		return unidadeGerencialDAO.findFilhas(bean) ;
	}
	
	@Override
	public void delete(UnidadeGerencial bean) {
		List<UnidadeGerencial> listaUnidadeGerencialFilha = findFilhas(bean);
		
		if (listaUnidadeGerencialFilha !=null && !listaUnidadeGerencialFilha.isEmpty()) {
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencialFilha) {
				delete(unidadeGerencial);
			}
		}
		
		painelIndicadorFiltroService.deleteByUnidadeGerencial(bean);
		planoAcaoService.deleteByUnidadeGerencial(bean);
		auditoriaGestaoService.deleteByUnidadeGerencial(bean);
		matrizFCSService.deleteByUnidadeGerencial(bean);
		iniciativaService.deleteByUnidadeGerencial(bean);
		indicadorService.deleteByUnidadeGerencial(bean);
		anomaliaService.deleteByUnidadeGerencial(bean);
		ocorrenciaService.deletebyUnidadeGerencial(bean);
		auditoriaInternaService.deleteByUnidadeGerencial(bean);
		acaoPreventivaService.deletebyUnidadeGerencial(bean);
		
		objetivoMapaEstrategicoService.exclui(objetivoMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(bean, false));
		
		super.delete(bean);
	}
	
	public List<UnidadeGerencial> loadUGObjetivoEstrategicoIndicadorAcompanhamento(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencialRaiz, boolean carregarSomenteUGRaiz) {
		
		List<UnidadeGerencial> listaUG = new ArrayList<UnidadeGerencial>();
		listaUG.add(unidadeGerencialRaiz);
		
		if (!carregarSomenteUGRaiz) {
			listaUG = getListaDescendencia(unidadeGerencialRaiz, listaUG);
		}
		
		List<Integer> listaIdUG = new ArrayList<Integer>();
		if (listaUG != null) {
			for (UnidadeGerencial uG : listaUG) {
				listaIdUG.add(uG.getId());
			}
		}
		
		planoGestao = planoGestaoService.load(planoGestao);
		listaUG = unidadeGerencialDAO.loadUGWithUsuarios(planoGestao, listaIdUG);
		
		// Carrega as perspectivas, os objetivos estratégicos, os indicadores e os acompanhamentos de cada UG.
		if (listaUG != null && !listaUG.isEmpty()) {
			for (UnidadeGerencial ug : listaUG) {
				ug.setListaPerspectivaMapaEstrategico(perspectivaMapaEstrategicoService.montaArvorePlanejamentoEstrategico(ug, true, true, true));
			}
		}
		
		return listaUG;
	}
	
	/**
	 * Recebe uma lista de unidades gerenciais e uma unidade gerencial raiz e monta uma hierarquia
	 * a partir desta.
	 * 
	 * @param ugsEmLinha
	 * @param unidadeGerencialRaiz
	 */
	public void montaHierarquia(List<UnidadeGerencial> ugsEmLinha, UnidadeGerencial unidadeGerencialRaiz) {
		
		//Joga as UGs filhas
		for (int i = ugsEmLinha.size() - 1 ; i >= 0 ; i--) {
			UnidadeGerencial ugFilha = ugsEmLinha.get(i);
			//Se filha tem algum pai...
			if (ugFilha.getSubordinacao() != null) {
				//Procura quem é o pai
				for (UnidadeGerencial ugPai : ugsEmLinha) {
					if (ugPai.equals(ugFilha.getSubordinacao())) {
						ugPai.getFilhos().add(ugFilha);
						ugFilha.setSubordinacao(ugPai);
						break;
					}
				}
			}
			if (ugFilha.equals(unidadeGerencialRaiz)) {
				ugFilha.setSubordinacao(null);
			}
		}
		
		//Elimina UGs deixando apenas as UGs raiz
		for (int i = ugsEmLinha.size() - 1 ; i >= 0 ; i--) {
			UnidadeGerencial ug = ugsEmLinha.get(i);
			if (ug.getSubordinacao() != null) {
				ugsEmLinha.remove(i);
			}
		}
	}
	
	public int contaUG(PlanoGestao planoGestao){
		return unidadeGerencialDAO.contaUG(planoGestao);
	}
	
	
	public List<UnidadeGerencial> findByPlanoGestao(PlanoGestao bean) {
		return unidadeGerencialDAO.findByPlanoGestao(bean, "unidadeGerencial.sigla, unidadeGerencial.nome");
	}
	
	public List<UnidadeGerencial> findByPlanoGestao(PlanoGestao bean, String orderBy) {
		return unidadeGerencialDAO.findByPlanoGestao(bean, orderBy);
	}
	
	public List<UnidadeGerencial> findWithSiglaNomeByPlanoGestao(PlanoGestao bean) {
		return unidadeGerencialDAO.findWithSiglaNomeByPlanoGestao(bean);
	}
	
	public List<UnidadeGerencial> findWithSiglaNomeSubordinacaoByPlanoGestao(PlanoGestao bean) {
		return unidadeGerencialDAO.findWithSiglaNomeSubordinacaoByPlanoGestao(bean);
	}
	
	public List<UnidadeGerencial> loadByUsuarioPlanoGestao(Usuario usuario, PlanoGestao planoGestao) {
		return unidadeGerencialDAO.loadByUsuarioPlanoGestao(usuario, planoGestao);
	}
	
	/***
	 * Remove as unidades gerenciais da lista, bem como toda a descendência
	 * de cada uma delas.
	 * 
	 * @param listaUnidadeGerencial
	 */
	public void excluiEmCascata(List<UnidadeGerencial> listaUnidadeGerencial) {
		if (listaUnidadeGerencial != null && !listaUnidadeGerencial.isEmpty()) {
			for (UnidadeGerencial unidadeGerencialRaiz : listaUnidadeGerencial) {
				this.delete(unidadeGerencialRaiz);
			}
		}
	}
	
	public UnidadeGerencial loadByNomePlanoGestao(String nome, PlanoGestao planoGestao) {
		return unidadeGerencialDAO.loadByNomePlanoGestao(nome, planoGestao);
	}
	
	/**
	 * Método recursivo que retorna todas as unidades gerenciais descendentes 
	 * de uma determinada unidade gerencial.
	 * 
	 * @param unidadeGerencialPai
	 * @param listaUnidadeGerencial
	 * @return
	 */
	public List<UnidadeGerencial> getListaDescendencia(UnidadeGerencial unidadeGerencialPai, List<UnidadeGerencial> listaUnidadeGerencial) {
		List<UnidadeGerencial> listaUnidadeGerencialFilha = findFilhas(unidadeGerencialPai);
		if (listaUnidadeGerencialFilha != null && !listaUnidadeGerencialFilha.isEmpty()) {
			for (UnidadeGerencial unidadeGerencialFilha : listaUnidadeGerencialFilha) {
				unidadeGerencialFilha.setNivelNum(unidadeGerencialPai.getNivelNum() + 1);
				getListaDescendencia(unidadeGerencialFilha, listaUnidadeGerencial);
			}
			
			for (UnidadeGerencial unidadeGerencialFilha : listaUnidadeGerencialFilha) {
				listaUnidadeGerencial.add(0,unidadeGerencialFilha);
			}
		}
		return listaUnidadeGerencial;
	}
	
	/**
	 * Gera o relatório de Alcance de Metas Institucionais.
	 * @param  AlcanceMetaInstitucionalReportFiltro
	 * @return IReport
	 */
	public IReport createReportAlcanceMetaInstitucional(WebRequestContext request, AlcanceMetaInstitucionalReportFiltro filtro) {
		
		List<ItemPainelControleDTO> listaDTOs;
		List<AlcanceMetaInstitucionalReportBean> listaAlcanceMetaInstitucionalReportBean = new ArrayList<AlcanceMetaInstitucionalReportBean>();
		
		// Instanciando o Report
		Report report = new Report("../relatorio/alcanceMetaInstitucional");
		
		filtro.setPlanoGestao(planoGestaoService.load(filtro.getPlanoGestao()));
		listaDTOs = new CalculosPainelControle().obtemHierarquiaCompleta(filtro.getPlanoGestao(), filtro.getUnidadeGerencial(), false);
		listaAlcanceMetaInstitucionalReportBean = montaListaAlcanceMetaInstitucionalReportBean(listaAlcanceMetaInstitucionalReportBean, listaDTOs, -1);
		
		report.setDataSource(listaAlcanceMetaInstitucionalReportBean);
		report.addParameter("ANO", filtro.getPlanoGestao().getAnoExercicio().toString());
		return report;
	}
	
	/**
	 * Retira da lista de itens do Painel de Controle somente os relacionados às unidades gerenciais.
	 * 
	 * @param listaAlcanceMetaInstitucionalReportBean
	 * @param listaItemPainelControleDTO
	 * @param recuo
	 * @return
	 */
	private List<AlcanceMetaInstitucionalReportBean> montaListaAlcanceMetaInstitucionalReportBean(List<AlcanceMetaInstitucionalReportBean> listaAlcanceMetaInstitucionalReportBean, List<ItemPainelControleDTO> listaItemPainelControleDTO, int recuo) {
		UnidadeGerencial unidadeGerencial;
		AlcanceMetaInstitucionalReportBean alcanceMetaInstitucionalReportBean;
		StringBuilder sbOffset = new StringBuilder();
		
		if (listaItemPainelControleDTO != null && !listaItemPainelControleDTO.isEmpty()) {
			for (ItemPainelControleDTO itemPainelControleDTO : listaItemPainelControleDTO) {
				if (itemPainelControleDTO.getHierarquizavel() instanceof UnidadeGerencial) {
					unidadeGerencial = (UnidadeGerencial) itemPainelControleDTO.getHierarquizavel();
					alcanceMetaInstitucionalReportBean = new AlcanceMetaInstitucionalReportBean();
					
					if (recuo == -1) {
						recuo = unidadeGerencial.getNivelNum();
					}
					
					sbOffset.delete(0, sbOffset.length());
					for (int i = 0; i < unidadeGerencial.getNivelNum() - recuo; i++) {
						sbOffset.append("\t\t\t\t\t\t\t\t\t\t");
					}
					
					alcanceMetaInstitucionalReportBean.setDescricao(sbOffset.toString() + unidadeGerencial.getSigla());
					alcanceMetaInstitucionalReportBean.setMapaPercentualEpoca(preencheMapaPercentualAMI(itemPainelControleDTO));
					listaAlcanceMetaInstitucionalReportBean.add(alcanceMetaInstitucionalReportBean);
				}
				
				// Passando os filhos encontrados no ItemPainelControleDTO.
				if (itemPainelControleDTO.getFilhos() != null) {
					listaAlcanceMetaInstitucionalReportBean = montaListaAlcanceMetaInstitucionalReportBean(listaAlcanceMetaInstitucionalReportBean, itemPainelControleDTO.getFilhos(), recuo);
				}				
			}
		}
		return listaAlcanceMetaInstitucionalReportBean;
	}
	
	/**
	 * Método responsável por preencher o mapa com os percentuais reais 
	 * de cada trimestre de uma determinada Unidade Gerencial.
	 * Todas as informações dos acompanhamentos devem estar presentes no <code>itemPainelControleDTO</code>.
	 * 
	 * @param itemPainelControleDTO
	 * @return
	 */
	private Map<Integer, String> preencheMapaPercentualAMI(ItemPainelControleDTO itemPainelControleDTO) {
		AcompanhamentoIndicador acompanhamentoIndicador;
		Map<Integer, String> mapaPercentualEpoca = null;
		DecimalFormat formatador = new DecimalFormat("##0.00");
		double somatorioPercentual = 0.0;
		int totalEpoca = 0;
		
		List<AcompanhamentoIndicador> listaAcompanhamentos = itemPainelControleDTO.getAcompanhamentos();
		//List<AcompanhamentoIndicador> listaAcompanhamentosAcumulados = itemPainelControleDTO.getAcompanhamentosAcumulados();
			
		// Verifica se existem acompanhamentos apurados para o indicador.							
		if(listaAcompanhamentos.size() == 4) {
			mapaPercentualEpoca = new HashMap<Integer, String>();							
			// Busca o acompanhamento do indicador para cada trimestre.
			for (int i = 0; i < 4; i++) {
				acompanhamentoIndicador = listaAcompanhamentos.get(i);
				if (acompanhamentoIndicador.getPercentualReal() != null) {
						mapaPercentualEpoca.put(i+1, formatador.format(acompanhamentoIndicador.getPercentualReal()) + " %");
						somatorioPercentual += acompanhamentoIndicador.getPercentualReal();
						totalEpoca++;
				}
				else {
					mapaPercentualEpoca.put(i+1, "-");	
				}
				
			}				
			mapaPercentualEpoca.put(0, totalEpoca > 0 ? formatador.format(somatorioPercentual / totalEpoca) + " %" : "-");		
		}
		return mapaPercentualEpoca;
	}
	
	/**
	 * Retorna uma lista contendo somente os nodos raiz de um determinado plano de gestão
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return lista de unidades gerenciais raiz
	 */	
	public List<UnidadeGerencial> findNodosRaiz(PlanoGestao planoGestao) {		
		return unidadeGerencialDAO.findNodosRaiz(planoGestao) ;
	}
	
	/**
	 * Retorna o pai de uma determinada Unidade Gerencial.
	 * @author Matheus Melo Gonçalves
	 * @param unidadeGerencial filha.
	 * @return unidadeGerencial Pai
	 */	
	public UnidadeGerencial getPai(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.getPai(unidadeGerencial);
	}
	
	/**
	 * Método responsável por gerar o relatório Mapa do Negócio.
	 * @author Matheus Melo	
	 * @param MapaNegocioReportFiltro
	 * @return report
	 */
	public IReport createMapaNegocioReport(MapaNegocioReportFiltro filtro){
	
		//Instanciando o Report
		Report report = new Report("../relatorio/definicaoDoNegocio");
		
		//Bean
		MapaDeNegocioReportBean mapaDeNegocioReportBean = new MapaDeNegocioReportBean();	
		
		filtro.setUnidadeGerencial(this.loadWithPlanoGestao(filtro.getUnidadeGerencial()));
		
		//Lista
		List<MapaDeNegocioReportBean> listaMapadeNegocioReportBean = new ArrayList<MapaDeNegocioReportBean>();
		List<MapaNegocio> listaMapaNegocio = new ArrayList<MapaNegocio>();

		listaMapaNegocio.add(mapaNegocioService.loadByUnidadeGerencial(filtro.getUnidadeGerencial()));
		
		//Setando a lista no bean Mapa de Negócio.
		mapaDeNegocioReportBean.setListaMapaNegocio(listaMapaNegocio);
		listaMapadeNegocioReportBean.add(mapaDeNegocioReportBean);
		report.setDataSource(listaMapaNegocio);
		
		
		report.addParameter("TITULORELATORIO", "MAPA DO NEGÓCIO");
		report.addParameter("DESCUNIDADEGERENCIAL", getDescricaoUnidadeGerencialComDescendencia(filtro.getUnidadeGerencial()));
		report.addParameter("RESPONSAVEISUNIDADEGERENCIAL", this.concatenaNomesResponsaveisUnidadeGerencial(filtro.getUnidadeGerencial()));		
		report.addParameter("DESCANOGESTAO", filtro.getUnidadeGerencial().getPlanoGestao().getAnoExercicio().toString());		

	 return report;	 
	}
	
	/**
	 * Método responsável por gerar o relatório Mapa Estratégico.
	 * @author Matheus Melo	
	 * @param MapaEstrategicoReportFiltro
	 * @return report
	 */
	public IReport criarRelatorioMapaEstrategico(MapaEstrategicoReportFiltro filtro){
		return null;
	}
	
	public Boolean usuarioPodeCriarAnomalia(Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG) {		
		if (isUsuarioAdmin) {
			return true;
		}		
		if (isUsuarioResponsavelUG) {
			return true;
		}
		if (isUsuarioApoioUG) {
			return true;
		}		
		return false;		
	}
	
	public Boolean usuarioPodeCriarAcaoPreventiva(Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG) {		
		if (isUsuarioAdmin) {
			return true;
		}		
		if (isUsuarioResponsavelUG) {
			return true;
		}
		if (isUsuarioApoioUG) {
			return true;
		}		
		return false;		
	}
	
	/**
	 * Confere se a soma dos pesos dos indicadores é igual a 100
	 * Indicadores com o Status diferente de APROVADO ou EM_CANCELAMENTO devem ter seus pesos desconsiderados
	 * 
	 * @param unidadeGerencial
	 * @param msg
	 */
	public String validaSomaPesoIndicadores(List<UnidadeGerencial> listaUnidadeGerencial, String msg) {
		Double somaPesos;
		
		if (listaUnidadeGerencial != null) {
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencial) {				
				if (unidadeGerencial.getListaPerspectivaMapaEstrategico() != null) {
					for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : unidadeGerencial.getListaPerspectivaMapaEstrategico()) {
						if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null) {
							for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
								somaPesos = 0d;
								
								/** soma pesos indicadores **/
								if (objetivoMapaEstrategico.getListaIndicador() != null) {
									for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {
										if (indicador.getStatus() == StatusIndicadorEnum.APROVADO || indicador.getStatus() == StatusIndicadorEnum.EM_CANCELAMENTO) {
											if (indicador.getPeso() != null) {
												somaPesos += indicador.getPeso();
											}
											else {
												msg = "O peso do indicador " + indicador.getNome() + " não pode estar em branco";
												return msg;
											}
										}								
									}
								}

								if(somaPesos != 100 && somaPesos != 0) {
									msg += "A soma dos pesos dos indicadores do objetivo estratégico '" + objetivoMapaEstrategico.getDescricao() + "' da unidade gerencial '" + unidadeGerencial.getSigla() + " - " + unidadeGerencial.getNome() + "' está com um valor diferente de 100.<br>";
								}								
							}
						}
					}
				}
				msg = validaSomaPesoIndicadores(unidadeGerencial.getFilhos(), msg);				
			}			
		}		
		return msg;
	}
	
	public static UnidadeGerencialService getInstance(){
		if(instance == null){
			instance = Neo.getObject(UnidadeGerencialService.class);
		}
		return instance;
	}
	
	/**
	 * Carrega o nome, a descrição e o ano de gestão de uma UG
	 * @param unidadeGerencial
	 * @return
	 */
	public UnidadeGerencial loadWithPlanoGestao(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.loadWithPlanoGestao(unidadeGerencial);
	}
	
	/**
	 * Carrega o nome, a descrição e o ano de gestão de uma UG
	 * @param indicador
	 * @return
	 */	
	public UnidadeGerencial loadWithPlanoGestaoByIndicador(Indicador indicador) {
		return unidadeGerencialDAO.loadWithPlanoGestaoByIndicador(indicador);
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.sgm.dao.UnidadeGerencialDAO#loadWithMapaCompetencia
	 *
	 * @param unidadeGerencial
	 * @return
	 * @author Rodrigo Freitas
	 */
	public UnidadeGerencial loadWithMapaCompetencia(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.loadWithMapaCompetencia(unidadeGerencial);
	}
	
	public Resource createMapaCompetenciaReport(MapaCompetenciaReportFiltro filtro) {
		
		try {
			MergeReport mergeReport = new MergeReport("Mapa de Competências");
			List<Report> listaReport = new ArrayList<Report>();		
			
			Report mainReport = new Report("../relatorio/mapaCompetencia");
			Report subAtividades = new Report("../relatorio/mapaCompetencia_subAtividades");
			Report subCompetencias = new Report("../relatorio/mapaCompetencia_subCompetencias");
			
			UnidadeGerencial unidadeGerencial = this.loadWithMapaCompetencia(filtro.getUnidadeGerencial());
			
			if (unidadeGerencial.getMapaCompetencia() == null){
				return null;
			}
			
			// Busca o campo missão vindo do cadastro do mapa do negócio.
			unidadeGerencial.getMapaCompetencia().setMissao(mapaNegocioService.loadMissaoByUnidadeGerencial(unidadeGerencial));			
			
			mainReport.addParameter("LOGO", neoImageResolver.getImage("/images/img_sgm_relatorio.png"));
			mainReport.addParameter("IMG_RODAPE", neoImageResolver.getImage("/images/fd_rodape.gif"));
			
			ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
			Image logoEmpresa;
			if (parametrosSistema.getImgEmpresaRelatorio() != null) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(parametrosSistema.getImgEmpresaRelatorio().getContent());
				logoEmpresa = ImageIO.read(byteArrayInputStream);
			}
			else {
				logoEmpresa = neoImageResolver.getImage("/images/img_empresa_relatorio.png");
			}			
			mainReport.addParameter("LOGO_EMPRESA", logoEmpresa);
			
			mainReport.addParameter("TITULO", "MAPA DE COMPETÊNCIAS");
			mainReport.addParameter("DESCUNIDADEGERENCIAL", getDescricaoUnidadeGerencialComDescendencia(filtro.getUnidadeGerencial()));
			mainReport.addParameter("RESPONSAVEISUNIDADEGERENCIAL", this.concatenaNomesResponsaveisUnidadeGerencial(unidadeGerencial));
			mainReport.addParameter("DESCANOGESTAO", unidadeGerencial.getPlanoGestao().getAnoExercicio().toString());
			mainReport.addParameter("MISSAO", unidadeGerencial.getMapaCompetencia().getMissao());
			mainReport.addParameter("ATIVIDADES", atividadeService.findByMapaCompetencia(unidadeGerencial.getMapaCompetencia()));
			mainReport.addParameter("COMPETENCIAS", competenciaService.findByMapaCompetencia(unidadeGerencial.getMapaCompetencia()));
			mainReport.addSubReport("SUBATIVIDADES", subAtividades);
			mainReport.addSubReport("SUBCOMPETENCIAS", subCompetencias);
			mainReport.addParameter("DATA",new Date(System.currentTimeMillis()));
			mainReport.addParameter("HORA", System.currentTimeMillis());
			mainReport.addParameter("USUARIOLOGADO", ((Usuario) Neo.getRequestContext().getUser()).getNome());			
			listaReport.add(mainReport);

			mergeReport.setReportlist(listaReport);
			
			return mergeReport.generateResource();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String concatenaNomesResponsaveisUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		List<UsuarioUnidadeGerencial> listaUsuario = usuarioUnidadeGerencialService.findResponsaveisByUnidadeGerencial(unidadeGerencial);
		StringBuilder string = new StringBuilder();
		
		UsuarioUnidadeGerencial uug;
		for (int i = 0; i < listaUsuario.size(); i++) {
			uug = listaUsuario.get(i);
			string.append(uug.getUsuario().getNome());
			if((i + 1) < listaUsuario.size()){
				string.append("\n");
			}
		}
		
		return string.toString();
	}
	
	public List<UnidadeGerencial> findAutocomplete(String q, PlanoGestao planoGestao) {
		return unidadeGerencialDAO.findAutocomplete(q, planoGestao);
	}
	
	public UnidadeGerencial loadWithSiglaNomeAno(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.loadWithSiglaNomeAno(unidadeGerencial);
	}
	
	public UnidadeGerencial loadWithSiglaNomeAnoSubordinacao(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.loadWithSiglaNomeAnoSubordinacao(unidadeGerencial);
	}
	
	/**
	 * Retorna as UGs da Área de Qualidade.
	 * @param planoGestao
	 * @return
	 */	
	public List<UnidadeGerencial> findUGQualidade(PlanoGestao planoGestao) {
		return unidadeGerencialDAO.findUGQualidade(planoGestao);
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.sgm.dao.UnidadeGerencialDAO#loadWithMapaEstrategico
	 *
	 * @param unidadeGerencial
	 * @return
	 * @author Rodrigo Freitas
	 */
	public UnidadeGerencial loadWithMapaEstrategico(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.loadWithMapaEstrategico(unidadeGerencial);
	}
	
	/**
	 * Método responsável por gerar o relatório Mapa Estratégico.
	 * @author Matheus Melo	
	 * @param MapaEstrategicoReportFiltro
	 * @return report
	 */
	public IReport createMapaEstrategicoReport(MapaEstrategicoReportFiltro filtro){
		//Objetos
		MapaEstrategico mapaEstrategico = new MapaEstrategico();
		Image imagemSeta = null;
		Report report = new Report("../relatorio/mapaEstrategico");
		Report subPerspectivas = new Report("../relatorio/mapaEstrategico_subPerspectivas");
		Report subPerspectivasSubObjetivoEstrategicos = new Report("../relatorio/mapaEstrategico_Perspectivas_subObjetivosEstrategicos");
		//Listas
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = new ArrayList<PerspectivaMapaEstrategico>();
		
		filtro.setUnidadeGerencial(this.loadWithMapaEstrategico(filtro.getUnidadeGerencial()));
		
		if (filtro.getUnidadeGerencial().getMapaEstrategico() != null) {
			// Lista de todas as perspectivas de um determinado Mapa Estratégico.
			listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoService.findByMapaEstrategico(filtro.getUnidadeGerencial().getMapaEstrategico());
			mapaEstrategico = filtro.getUnidadeGerencial().getMapaEstrategico();
			mapaEstrategico.setListaPerspectivaMapaEstrategico(listaPerspectivaMapaEstrategico);
			
			// Busca o campo missão vindo do cadastro do mapa do negócio.
			mapaEstrategico.setMissao(mapaNegocioService.loadMissaoByUnidadeGerencial(filtro.getUnidadeGerencial()));			
			
			filtro.getUnidadeGerencial().setMapaEstrategico(mapaEstrategico);
		}
		
		// Preenche a imagem da seta que será utilizada no relatório.	
		try {
			imagemSeta = neoImageResolver.getImage("/images/seta.jpg");
		}  
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//Enviando dados ao report.
		report.addParameter("DESCUNIDADEGERENCIAL", getDescricaoUnidadeGerencialComDescendencia(filtro.getUnidadeGerencial()));
		report.addParameter("RESPONSAVEISUNIDADEGERENCIAL", this.concatenaNomesResponsaveisUnidadeGerencial(filtro.getUnidadeGerencial()));
		report.addParameter("DESCANOGESTAO", planoGestaoService.load(filtro.getPlanoGestao()).getAnoExercicio().toString());
		report.addParameter("SETA", imagemSeta);
		report.addParameter("missao", filtro.getUnidadeGerencial().getMapaEstrategico() == null ? "" : filtro.getUnidadeGerencial().getMapaEstrategico().getMissao());
		report.addParameter("visao", filtro.getUnidadeGerencial().getMapaEstrategico() == null ? "" : filtro.getUnidadeGerencial().getMapaEstrategico().getVisao());
		report.addParameter("mapaEstrategico", filtro.getUnidadeGerencial().getMapaEstrategico());
		report.addSubReport("SUBPERSPECTIVAS", subPerspectivas );
		report.addSubReport("SUBESTRATEGIAS	", subPerspectivasSubObjetivoEstrategicos);
		
		return report;
	}
	
	public boolean isSubordinado(UnidadeGerencial pai, UnidadeGerencial filho, PlanoGestao planoGestao) {
		List<UnidadeGerencial> pais = find(pai, -1, planoGestao);
		return isSubordinado(pais, filho);
	}

	private boolean isSubordinado(List<UnidadeGerencial> pais, UnidadeGerencial filho) {
		if(pais != null){
			for (UnidadeGerencial pai : pais) {
				if(pai.getId().equals(filho.getId())){
					return true;
				}
				if(isSubordinado(pai.getFilhos(), filho)){
					return true;		
				}
			}
			return false;
		} else {
			return false;	
		}	
	}
	
	/**
	 * Monta uma lista de Unidades Gerenciais de acordo com a seleção feita pelo usuário, respeitando
	 * também o nível de acesso do mesmo.
	 * 
	 * @param planoGestaoFiltro
	 * @param unidadeGerencialFiltro
	 * @param incluirSubordinadas
	 * @return
	 */
	public List<UnidadeGerencial> montaListaUnidadeGerencial(PlanoGestao planoGestaoFiltro, UnidadeGerencial unidadeGerencialFiltro, boolean incluirSubordinadas) {
		
		if (planoGestaoFiltro == null) {
			return null;
		}
		
		// Preenche as unidades gerenciais de acordo com a seleção feita pelo usuário
		List<UnidadeGerencial> listaUGSelecionada = new ArrayList<UnidadeGerencial>();		
		
		if (unidadeGerencialFiltro != null) {
			listaUGSelecionada.add(unidadeGerencialFiltro);
			
			if (incluirSubordinadas) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGSelecionada = this.getListaDescendencia(unidadeGerencialFiltro, listaUGSelecionada);
			}
		}
		
		// Preenche as unidades gerenciais de acordo com o perfil de acesso do usuário
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(planoGestaoFiltro);
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (!usuarioLogadoIsAdmin) {
			if (listaUGUsuario != null) {
				if (incluirSubordinadas) {
					for (UnidadeGerencial uGUsuario : listaUGUsuario) {
						// Busca todas as UGs subordinadas da UG selecionada
						listaUGDisponivel = this.getListaDescendencia(uGUsuario, listaUGDisponivel);
					}
				}
				listaUGDisponivel.addAll(listaUGUsuario);
			}
		}
		else {
			listaUGDisponivel = this.findWithSiglaNomeByPlanoGestao(planoGestaoFiltro);
		}
		
		List<UnidadeGerencial> listaUnidadeGerencialFinal = new ArrayList<UnidadeGerencial>();
		
		// Caso o usuário não tenha selecionado nenhuma UG, coloca no filtro a lista de UGs a que ele tem acesso.
		if (listaUGSelecionada.isEmpty()) {
			listaUnidadeGerencialFinal.addAll(listaUGDisponivel);
		}
		// Senão, verifica se cada UG selecionada está também disponível. Se tiver, inclui no filtro.
		else {
			for (UnidadeGerencial uGSelecionada : listaUGSelecionada) {
				for (UnidadeGerencial uGDisponivel : listaUGDisponivel) {
					if (uGSelecionada.equals(uGDisponivel)) {
						listaUnidadeGerencialFinal.add(uGSelecionada);
						break;
					}
				}
			}
		}
		
		return listaUnidadeGerencialFinal;		
	}
	
	/**
	 * Atualiza o valor do campo <code>nivelnum</code> de cada unidade gerencial
	 * presente na lista passada como parâmetro.
	 * 
	 * @param listaUnidadeGerencial
	 * @return
	 */	
	public void atualizaUnidadeGerencialNivelNum(List<UnidadeGerencial> listaUnidadeGerencial) {
		unidadeGerencialDAO.atualizaUnidadeGerencialNivelNum(listaUnidadeGerencial);
	}
	
	/**
	 * Retorna uma string contendo a descrição da unidade gerencial passada como parâmetro
	 * com a rastreabilidade até a unidade gerencial raiz.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public String getDescricaoUnidadeGerencialComDescendencia(UnidadeGerencial unidadeGerencial) {
		List<String> listaDescricao = new ArrayList<String>();
		listaDescricao = montaListaDescricaoUnidadeGerencialComDescendencia(unidadeGerencial, listaDescricao);		
		
		if (listaDescricao != null && !listaDescricao.isEmpty()) {
			return CollectionsUtil.concatenate(listaDescricao, " -> ");
		}
		return "-";
	}
	
	/**
	 * Método recursivo que busca as unidades gerenciais ancestrais da unidade gerencial
	 * passada como parâmetro e vai armazenando as siglas até atingir a unidade gerencial raiz.
	 * 
	 * @param unidadeGerencial
	 * @param listaDescricao
	 * @return
	 */
	private List<String> montaListaDescricaoUnidadeGerencialComDescendencia(UnidadeGerencial unidadeGerencial, List<String> listaDescricao) {
		unidadeGerencial = loadWithSiglaNomeAnoSubordinacao(unidadeGerencial);
		listaDescricao.add(0, listaDescricao.isEmpty() ? unidadeGerencial.getDescricao() : unidadeGerencial.getSigla());

		if (unidadeGerencial.getSubordinacao() != null) {
			listaDescricao = montaListaDescricaoUnidadeGerencialComDescendencia(unidadeGerencial.getSubordinacao(), listaDescricao);
		}

		return listaDescricao;
	}
	
	/**
	 * Retorna uma lista de unidades gerenciais com a regra de que sempre as unidades gerenciais
	 * superiores vêm antes de suas subordinadas.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<UnidadeGerencial> findByPlanoGestaoOrdenadasPorHierarquia(PlanoGestao planoGestao) {
		List<UnidadeGerencial> listaUnidadeGerencialRaiz = findNodosRaiz(planoGestao);
		List<UnidadeGerencial> listaUnidadeGerencialDescendencia;
		List<UnidadeGerencial> listaUnidadeGerencial = new ArrayList<UnidadeGerencial>();
		if (listaUnidadeGerencialRaiz != null && !listaUnidadeGerencialRaiz.isEmpty()) {
			for (UnidadeGerencial ugRaiz : listaUnidadeGerencialRaiz) {
				listaUnidadeGerencialDescendencia = new ArrayList<UnidadeGerencial>();
				listaUnidadeGerencialDescendencia = getListaDescendencia(ugRaiz, listaUnidadeGerencialDescendencia);
				listaUnidadeGerencialDescendencia.add(0,ugRaiz);
				listaUnidadeGerencial.addAll(listaUnidadeGerencialDescendencia);
			}
		}
		return listaUnidadeGerencial;
	}
	
	/**
	 * Retorna as UGs da Área de Auditoria Interna de um determinado Ano da Gestão.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<UnidadeGerencial> findUGAuditoriaInterna(PlanoGestao planoGestao) {
		return unidadeGerencialDAO.findUGAuditoriaInterna(planoGestao);
	}	
}

