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

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.AuditoriaGestao;
import br.com.linkcom.sgm.beans.AuditoriaGestaoIndicador;
import br.com.linkcom.sgm.beans.AuditoriaGestaoIndicadorItem;
import br.com.linkcom.sgm.beans.FatorAvaliacao;
import br.com.linkcom.sgm.beans.ItemFatorAvaliacao;
import br.com.linkcom.sgm.beans.ModeloAuditoriaGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.dao.AuditoriaGestaoDAO;
import br.com.linkcom.sgm.filtro.AuditoriaGestaoFiltro;
import br.com.linkcom.sgm.report.bean.AuditoriaGestaoGraficoBean;
import br.com.linkcom.sgm.report.bean.AuditoriaGestaoItemReportBean;
import br.com.linkcom.sgm.report.bean.AuditoriaGestaoReportBean;
import br.com.linkcom.sgm.util.grafico.GraficoServlet;

public class AuditoriaGestaoService extends GenericService<AuditoriaGestao> {
	
	private AuditoriaGestaoIndicadorService auditoriaGestaoIndicadorService;
	private AuditoriaGestaoIndicadorItemService auditoriaGestaoIndicadorItemService;
	private ItemFatorAvaliacaoService itemFatorAvaliacaoService;
	private UsuarioService usuarioService;
	private UnidadeGerencialService unidadeGerencialService;
	private AuditoriaGestaoDAO auditoriaGestaoDAO;
	
	
	public void setAuditoriaGestaoDAO(AuditoriaGestaoDAO auditoriaGestaoDAO) {
		this.auditoriaGestaoDAO = auditoriaGestaoDAO;
	}
	public void setAuditoriaGestaoIndicadorItemService(AuditoriaGestaoIndicadorItemService auditoriaGestaoIndicadorItemService) {
		this.auditoriaGestaoIndicadorItemService = auditoriaGestaoIndicadorItemService;
	}
	public void setAuditoriaGestaoIndicadorService(
			AuditoriaGestaoIndicadorService auditoriaGestaoIndicadorService) {
		this.auditoriaGestaoIndicadorService = auditoriaGestaoIndicadorService;
	}
	public void setItemFatorAvaliacaoService(ItemFatorAvaliacaoService itemFatorAvaliacaoService) {
		this.itemFatorAvaliacaoService = itemFatorAvaliacaoService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	@Override
	public void saveOrUpdate(AuditoriaGestao bean) {
		List<AuditoriaGestaoIndicador> listaAuditoriaGestaoIndicador = bean.getListaAuditoriaGestaoIndicador();
		bean.setListaAuditoriaGestaoIndicador(null);
		
		auditoriaGestaoDAO.saveOrUpdate(bean);
		
		for (AuditoriaGestaoIndicador auditoriaGestaoIndicador : listaAuditoriaGestaoIndicador) {
			auditoriaGestaoIndicador.setAuditoriaGestao(bean);
			auditoriaGestaoIndicadorService.saveOrUpdate(auditoriaGestaoIndicador);
		}
	}
	
	public boolean modeloUsado(ModeloAuditoriaGestao form) {
		return auditoriaGestaoDAO.modeloUsado(form);
	}
	
	/**
	 * Cria o relatório de Auditoria de Gestão.
	 * 
	 * @author Rodrigo Alvarenga 
	 * @param auditoriaGestao
	 * @return
	 */
	public IReport createAuditoriaGestaoReport(AuditoriaGestao auditoriaGestao) {
		
		List<AuditoriaGestaoReportBean> listaReportBean = new ArrayList<AuditoriaGestaoReportBean>();
		List<AuditoriaGestaoItemReportBean> listaItemReportBean = new ArrayList<AuditoriaGestaoItemReportBean>();
		AuditoriaGestaoReportBean auditoriaGestaoReportBean;
		AuditoriaGestaoItemReportBean auditoriaGestaoItemReportBean;
		Map<AuditoriaGestaoGraficoBean, Integer> mapaAvaliacao = new LinkedHashMap<AuditoriaGestaoGraficoBean, Integer>();
		FatorAvaliacao fatorDefault = null;
		
		// Carrega o bean de Auditoria de Gestão selecionado.
		auditoriaGestao = loadForEntrada(auditoriaGestao);
		auditoriaGestaoReportBean = new AuditoriaGestaoReportBean();
		auditoriaGestaoReportBean.setDescPlanoGestao(auditoriaGestao.getUnidadeGerencial().getPlanoGestao().getAnoExercicio().toString());
		auditoriaGestaoReportBean.setDescUnidadeGerencial(auditoriaGestao.getUnidadeGerencial().getDescricao());
		auditoriaGestaoReportBean.setDataAuditoria(new SimpleDateFormat("dd/MM/yyyy").format(auditoriaGestao.getDataAuditoria()));
		auditoriaGestaoReportBean.setResponsavel(auditoriaGestao.getResponsavel());
		auditoriaGestaoReportBean.setPeriodoAvaliado(auditoriaGestao.getDescricao());
		
		int i = 0;
		if (auditoriaGestao.getListaAuditoriaGestaoIndicador() != null) {
			for (AuditoriaGestaoIndicador auditoriaGestaoIndicador : auditoriaGestao.getListaAuditoriaGestaoIndicador()) {
				
				auditoriaGestaoIndicador.setListaAuditoriaGestaoIndicadorItem(auditoriaGestaoIndicadorItemService.findByAuditoriaIndicador(auditoriaGestaoIndicador));
				
				if (auditoriaGestaoIndicador.getListaAuditoriaGestaoIndicadorItem() != null) {
					for (AuditoriaGestaoIndicadorItem auditoriaGestaoIndicadorItem : auditoriaGestaoIndicador.getListaAuditoriaGestaoIndicadorItem()) {
						
						auditoriaGestaoItemReportBean = new AuditoriaGestaoItemReportBean();
						auditoriaGestaoItemReportBean.setIdIndicador(auditoriaGestaoIndicador.getIndicador().getId());
						auditoriaGestaoItemReportBean.setNomeIndicador(auditoriaGestaoIndicador.getIndicador().getNome());
						auditoriaGestaoItemReportBean.setNomeItemAuditoria(auditoriaGestaoIndicadorItem.getItemModeloAuditoriaGestao().getNome());
						auditoriaGestaoItemReportBean.setDescItemAuditoria(auditoriaGestaoIndicadorItem.getItemModeloAuditoriaGestao().getDescricao());
						auditoriaGestaoItemReportBean.setValorItemAvaliacao(auditoriaGestaoIndicadorItem.getItemFatorAvaliacao() != null ? auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getValor().intValue() : null);
						auditoriaGestaoItemReportBean.setDescItemAvaliacao(auditoriaGestaoIndicadorItem.getItemFatorAvaliacao() != null ? auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getDescricao() : null);
						auditoriaGestaoItemReportBean.setDescAvaliacao(
								auditoriaGestaoIndicadorItem.getItemFatorAvaliacao() != null ?
										auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getDescricao() + " (" + auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getValor().toString() + ")" 
										:
										auditoriaGestaoIndicadorItem.getTexto());
						listaItemReportBean.add(auditoriaGestaoItemReportBean);
						
						if (fatorDefault == null && auditoriaGestaoIndicadorItem.getItemFatorAvaliacao() != null) {
							fatorDefault = auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getFatorAvaliacao();
						}
						if (auditoriaGestaoIndicadorItem.getItemFatorAvaliacao() != null && auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getFatorAvaliacao().equals(fatorDefault)) {
							if (!auditoriaGestaoIndicadorItem.getItemModeloAuditoriaGestao().getNome().equals("Riscos")) {
								mapaAvaliacao.put(new AuditoriaGestaoGraficoBean(auditoriaGestaoIndicador.getIndicador().getNome(),auditoriaGestaoIndicadorItem.getItemModeloAuditoriaGestao().getNome(),auditoriaGestaoIndicadorItem.getItemModeloAuditoriaGestao().getDescricao()), auditoriaGestaoIndicadorItem.getItemFatorAvaliacao().getValor().intValue());
							}
						}
					}
				}
				i++;
			}
		}	
		auditoriaGestaoReportBean.setListaAuditoriaGestaoItemReportBean(listaItemReportBean);
		
		double valorMaximo = 0d;
		StringBuilder sbLegenda = new StringBuilder("LEGENDA: ");
		if (fatorDefault != null) {
			fatorDefault.setListaItemFatorAvaliacao(itemFatorAvaliacaoService.findByFatorAvaliacao(fatorDefault));
			if (fatorDefault.getListaItemFatorAvaliacao() != null) {
				for (ItemFatorAvaliacao itemFatorAvaliacao : fatorDefault.getListaItemFatorAvaliacao()) {
					sbLegenda.append(itemFatorAvaliacao.getDescription());
					
					if (itemFatorAvaliacao.getValor().doubleValue() > valorMaximo) {
						valorMaximo = itemFatorAvaliacao.getValor().doubleValue();
					}
				}
			}
		}		
		
		auditoriaGestaoReportBean.setGrafico(new ByteArrayInputStream(new GraficoServlet().geraGraficoAuditoriaGestao(mapaAvaliacao, valorMaximo)));
		listaReportBean.add(auditoriaGestaoReportBean);
		
		// Instancia o Report
		Report report = new Report("../relatorio/auditoriaGestao");
		report.addSubReport("SUBGRAFICO", new Report("../relatorio/auditoriaGestaoGrafico"));
		report.addParameter("LEGENDA", sbLegenda.toString());
		
		// Data Source
		report.setDataSource(listaReportBean);
		
		return report;
	}
	
	@Override
	public ListagemResult<AuditoriaGestao> findForListagem(FiltroListagem filtro) {
		AuditoriaGestaoFiltro auditoriaGestaoFiltro = (AuditoriaGestaoFiltro) filtro;
		
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(auditoriaGestaoFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		
		// UGs disponíveis para a listagem
		if (!usuarioLogadoIsAdmin) {
			for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
				listaUGDisponivel.add(unidadeGerencial);
				listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
			}			
		}
		auditoriaGestaoFiltro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		
		
		return super.findForListagem(filtro);
	}
	
	/**
	 * Remove todas as auditorias de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		auditoriaGestaoDAO.deleteByUnidadeGerencial(unidadeGerencial);
	}	
}
