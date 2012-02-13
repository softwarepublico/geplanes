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
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.enumeration.StatusAcaoPreventivaEnum;
import br.com.linkcom.sgm.dao.AcaoPreventivaDAO;
import br.com.linkcom.sgm.filtro.AcaoPreventivaFiltro;
import br.com.linkcom.sgm.report.MergeReport;

public class AcaoPreventivaService extends GenericService<AcaoPreventiva> {
	
	private UnidadeGerencialService unidadeGerencialService;
	private AcaoPreventivaDAO acaoPreventivaDAO;
	private UsuarioService usuarioService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private NeoImageResolver neoImageResolver;
	private PlanoAcaoService planoAcaoService;
	
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService){this.planoAcaoService = planoAcaoService;}
	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {this.neoImageResolver = neoImageResolver;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setAcaoPreventivaDAO(AcaoPreventivaDAO acaoPreventivaDAO) {this.acaoPreventivaDAO = acaoPreventivaDAO;}	
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}	
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	
	@Override
	public ListagemResult<AcaoPreventiva> findForListagem(FiltroListagem filtro) {
		AcaoPreventivaFiltro acaoPreventivaFiltro = (AcaoPreventivaFiltro) filtro;
		
		List<UnidadeGerencial> listaUGRegistro = new ArrayList<UnidadeGerencial>();		
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(acaoPreventivaFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsRepQualidade = usuarioService.isUsuarioLogadoVinculadoAreaQualidade(acaoPreventivaFiltro.getPlanoGestao());
		
		// UG de Registro
		if (acaoPreventivaFiltro.getUgRegistro() != null) {
			listaUGRegistro.add(acaoPreventivaFiltro.getUgRegistro());
			
			if (acaoPreventivaFiltro.isIncluirSubordinadasReg()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGRegistro = unidadeGerencialService.getListaDescendencia(acaoPreventivaFiltro.getUgRegistro(), listaUGRegistro);
			}
		}
		acaoPreventivaFiltro.setListaUnidadeGerencialReg(listaUGRegistro);
		
		// UGs disponíveis para a listagem
		if (!usuarioLogadoIsAdmin && !usuarioLogadoIsRepQualidade) {
			for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
				listaUGDisponivel.add(unidadeGerencial);
				listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
			}			
		}
		acaoPreventivaFiltro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		
		return super.findForListagem(acaoPreventivaFiltro);
	}
	
	@Override
	public void saveOrUpdate(AcaoPreventiva bean) {
		if (bean.getPlanosAcao() != null && !bean.getPlanosAcao().isEmpty()) {
			PlanoAcao planoAcaoDB;
			Date dataAtual = new Date(System.currentTimeMillis());
			for (PlanoAcao planoAcao : bean.getPlanosAcao()) {		
				// Verifica se houve mudança no status.
				// Caso afirmativo, grava a data atual.				
				if (planoAcao.getId() != null) {
					planoAcaoDB = planoAcaoService.load(planoAcao);
					if (!planoAcaoDB.getStatus().equals(planoAcao.getStatus())) {
						planoAcao.setDtAtualizacaoStatus(dataAtual);
					}
				}
			}
		}
		super.saveOrUpdate(bean);
	}	
	
	public UnidadeGerencial verificaUgOrigem(String idStringUgOrigem, AcaoPreventiva acaoPreventiva){
		UnidadeGerencial ugOrigem = new UnidadeGerencial();
		try {
			Integer idUgOrigem = Integer.parseInt(idStringUgOrigem);
			ugOrigem.setId(idUgOrigem);
			ugOrigem = unidadeGerencialService.loadWithPlanoGestao(ugOrigem);
		} catch (Exception e) {}
		return ugOrigem;
	}

	/**
	 * Método responsável por criar o relatório de ações preventivas.
	 * @author Rodrigo Alvarenga
	 * @param AcaoPreventiva
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public Resource gerarRelatorioAcaoPreventiva(AcaoPreventiva bean){
		bean = this.loadForEntrada(bean);
		bean.setPlanosAcao(new ListSet<PlanoAcao>(PlanoAcao.class,planoAcaoService.findByAcaoPreventiva(bean)));
		
		//Listas
		List<AcaoPreventiva> listaAcaoPreventiva = new ArrayList<AcaoPreventiva>();
		List<Report> listaReport = new ArrayList<Report>();
		listaAcaoPreventiva.add(bean);
		
		//Objetos
		Report report = new Report("../relatorio/acaoPreventivaMae");
		MergeReport mergeReport = new MergeReport("Ação Preventiva");
		Image image1 = null;
		Image image2 = null;
		Image image4 = null;
		
		//Recuperando as imagens do relatório.
		try {
			image1 = neoImageResolver.getImage("/images/img_sgm_relatorio.png");
			image2 = neoImageResolver.getImage("/images/fd_rodape.gif");
			image4 = neoImageResolver.getImage("/images/img_empresa_relatorio.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Primeiro Sub-relatório
		report.addSubReport("primeiroSub", new Report("../relatorio/acaoPreventiva"));
		report.addParameter("numero", bean.getSequencial());
		if (bean.getOrigem() != null){
			report.addParameter("origem", bean.getOrigem().toString());
		}		
		report.addParameter("dataAcaoPreventiva", bean.getDataAbertura());
		report.addParameter("origemAcaoPreventiva", bean.getUgRegistro().getDescricao());
		report.addParameter("descAcaoPreventiva", bean.getDescricao() == null ? "" : bean.getDescricao());
		report.addParameter("obsComplementares", bean.getObservacoes() == null ? "" : bean.getObservacoes());
		if(bean.getTipo() != null){
			report.addParameter("tipo", bean.getTipo().toString());
		}	
		report.addParameter("LOGO", image1);
		report.addParameter("IMG_RODAPE", image2);
		report.addParameter("LOGO_EMPRESA", image4);
		report.addParameter("NEOFORMATER", NeoFormater.getInstance());
		report.addParameter("TITULO", "RELATÓRIO DE AÇÃO PREVENTIVA");
		report.addParameter("DATA",new Date(System.currentTimeMillis()));
		report.addParameter("HORA", System.currentTimeMillis());
		report.addParameter("USUARIOLOGADO", ((Usuario)Neo.getUser()).getNome());
		report.addParameter("HEADER", "SISTEMA DE GESTÃO INTEGRADA");
		report.setDataSource(listaAcaoPreventiva);
		
		//Segundo sub.
		report.addSubReport("segundoSub", new Report("../relatorio/acaoPreventivaSub"));
		report.addParameter("avalEficaciaAcao", bean.getAvalEficaciaAcao() == null ? "" : bean.getAvalEficaciaAcao().toString());
		report.addParameter("evidenciaEficaciaAcao", bean.getEvidenciaEficaciaAcao() == null ? "" : bean.getEvidenciaEficaciaAcao());
		report.addParameter("conclusao", bean.getConclusao() == null ? "" : bean.getConclusao());
		
		//Adicionando os dos dois relatórios a uma lista que será passada a mergeReport.
		listaReport.add(report);
		mergeReport.setReportlist(listaReport);
		
		try {
			return mergeReport.generateResource();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public void delete(AcaoPreventiva bean) {
		acompanhamentoIndicadorService.setNullAcaoPreventiva(bean);
		super.delete(bean);
	}
	
	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial) {
		return acaoPreventivaDAO.getProximoSequencial(unidadeGerencial);
	}
	
	public StatusAcaoPreventivaEnum getStatusAcaoPreventiva(AcaoPreventiva acaoPreventiva) {
		if (acaoPreventiva != null) {
			
			if (acaoPreventiva.getStatus() != null) {
				
				if (acaoPreventiva.getStatus().equals(StatusAcaoPreventivaEnum.ABERTA)) {
					if (acaoPreventiva.getDataEncerramento() != null) {
						return StatusAcaoPreventivaEnum.ENCERRADA;
					}
					return StatusAcaoPreventivaEnum.ABERTA;
				}
				
				if (acaoPreventiva.getStatus().equals(StatusAcaoPreventivaEnum.ENCERRADA)) {
					if (acaoPreventiva.getDataEncerramento() != null) {
						return StatusAcaoPreventivaEnum.ENCERRADA;
					}
					return StatusAcaoPreventivaEnum.ABERTA;
				}

			}
			return StatusAcaoPreventivaEnum.ABERTA;
		}
		return null;
	}
	
	public Boolean usuarioPodeEncerrarAcaoPreventiva(PlanoGestao planoGestao) {
		return usuarioService.isUsuarioLogadoVinculadoAreaQualidade(planoGestao);		
	}
	
	/**
	 * Percorre a hierarquia de UGs e retorna a unidade gerencial que possui 
	 * o mesmo id da ug passada como parâmetro.
	 *  
	 * @param ugRaiz
	 * @param ug
	 * @return
	 */
	public UnidadeGerencial obtemUGHierarquia(UnidadeGerencial ugRaiz, UnidadeGerencial ug) {
		if (ugRaiz != null && ug != null) {
			if (ugRaiz.getId().equals(ug.getId())) {
				return ugRaiz;
			}
			if (ugRaiz.getFilhos() != null) {
				for (UnidadeGerencial ugFilha : ugRaiz.getFilhos()) {
					UnidadeGerencial ugNeta = obtemUGHierarquia(ugFilha, ug);
					if (ugNeta != null) {
						return ugNeta;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Remove todas as ações preventivas de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deletebyUnidadeGerencial(UnidadeGerencial bean) {
		acaoPreventivaDAO.deleteByUnidadeGerencial(bean); 
	}	
}
