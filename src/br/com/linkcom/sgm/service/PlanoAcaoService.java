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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;
import br.com.linkcom.sgm.controller.filtro.IniciativaPlanoAcaoFiltro;
import br.com.linkcom.sgm.dao.PlanoAcaoDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.report.bean.IniciativaPlanoAcaoReportBean;


public class PlanoAcaoService extends GenericService<PlanoAcao> {

	private PlanoAcaoDAO planoAcaoDAO;
	private UsuarioService usuarioService;
	private UnidadeGerencialService unidadeGerencialService;
	
	public void setPlanoAcaoDAO(PlanoAcaoDAO planoAcaoDAO) {this.planoAcaoDAO = planoAcaoDAO;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	
	/**
	 * Busca todos planos de ação vinculados a uma determinada anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return List<PlanoAcao>
	 */
	public List<PlanoAcao> findByAnomalia(Anomalia anomalia){
		if(anomalia == null){
			throw new GeplanesException("A anomalia não pode ser nula na pesquisa por Planos de Ação.");
		}
		return planoAcaoDAO.findByAnomalia(anomalia);
	}
	
	/**
	 * Busca todos planos de ação vinculados a uma determinada ação preventiva.
	 * @author Matheus Melo Gonçalves
	 * @param acaoPreventiva
	 * @return List<PlanoAcao>
	 */
	public List<PlanoAcao> findByAcaoPreventiva(AcaoPreventiva acaoPreventiva){
		if (acaoPreventiva == null) {
			throw new GeplanesException("A ação preventiva não pode ser nula na pesquisa por Planos de Ação.");
		}
		return planoAcaoDAO.findByAcaoPreventiva(acaoPreventiva);
	}
	
	/**
	 * Busca todos planos de ação vinculados a uma determinada unidade gerencial
	 * e a uma determinada iniciativa.
	 * 
	 * @param unidadeGerencial
	 * @param iniciativa
	 * @return List<PlanoAcao>
	 */
	public List<PlanoAcao> findByUGIniciativa(UnidadeGerencial unidadeGerencial, Iniciativa iniciativa) {
		if (unidadeGerencial == null || iniciativa == null) {
			throw new GeplanesException("A unidade gerencial e a iniciativa não podem ser nulas na pesquisa por Planos de Ação.");
		}
		return planoAcaoDAO.findByUGIniciativa(unidadeGerencial, iniciativa);
	}
	
	@Override
	public void saveOrUpdate(PlanoAcao planoAcao) {
		PlanoAcao planoAcaoDB;
		Date dataAtual = new Date(System.currentTimeMillis());
		
		// Verifica se houve mudança no status.
		// Caso afirmativo, grava a data atual.
		if (planoAcao.getId() != null) {
			planoAcaoDB = this.load(planoAcao);
			if (!planoAcaoDB.getStatus().equals(planoAcao.getStatus())) {
				planoAcao.setDtAtualizacaoStatus(dataAtual);
			}
		}
		
		super.saveOrUpdate(planoAcao);
	}
	
	/**
	 * Remove todos os planos de ação (vinculados a iniciativas) de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		planoAcaoDAO.deleteByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Exibe o relatório de Plano de Ação das Iniciativas
	 * de acordo com o filtro solicitado.
	 *  
	 * @param filtro
	 * @return
	 */
	public IReport createIniciativaPlanoAcaoReport(IniciativaPlanoAcaoFiltro filtro) {
		
		Report report = new Report("../relatorio/iniciativaPlanoAcao");
		
		if (filtro.getExpirado() != null) {
			report.addParameter("EXPIRADO", filtro.getExpirado() ? "Sim" : "Não");
		}
		
		List<StatusPlanoAcaoEnum> listaStatusPlanoAcaoEnum = null;
		if (filtro.getListaStatus() != null && !filtro.getListaStatus().isEmpty()) {
			String textoStatus = "";
			listaStatusPlanoAcaoEnum = new ArrayList<StatusPlanoAcaoEnum>();
			String[] arrayStatusFiltro = filtro.getListaStatus().split(",");
			for (String statusFiltro : arrayStatusFiltro) {
				for (StatusPlanoAcaoEnum statusEnum : StatusPlanoAcaoEnum.values()) {
					if (statusEnum.getName().equals(statusFiltro)) {
						textoStatus += statusEnum.toString() + ", ";
						listaStatusPlanoAcaoEnum.add(statusEnum);
						break;
					}
				}				
			}
			
			// Retira a vírgula e o espaço do final do texto.
			if (!textoStatus.equals("")) {
				textoStatus = textoStatus.substring(0, textoStatus.length() - 2);
				report.addParameter("STATUS", textoStatus);
			}
			
		}
		filtro.setListaStatusPlanoAcaoEnum(listaStatusPlanoAcaoEnum);
		
		List<PlanoAcao> listaPlanoAcao = this.findByIniciativas(filtro);
		List<IniciativaPlanoAcaoReportBean> listaReportBean = new ArrayList<IniciativaPlanoAcaoReportBean>();
		IniciativaPlanoAcaoReportBean reportBean;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		if (listaPlanoAcao != null && !listaPlanoAcao.isEmpty()) {
			for (PlanoAcao planoAcao : listaPlanoAcao) {
				reportBean = new IniciativaPlanoAcaoReportBean();
				reportBean.setAnoGestao(planoAcao.getUnidadeGerencial().getPlanoGestao().getAnoExercicio().toString());
				reportBean.setUnidadeGerencial(planoAcao.getUnidadeGerencial().getDescricao());
				reportBean.setObjetivoEstrategico(planoAcao.getIniciativa().getObjetivoMapaEstrategico().getDescricao());
				reportBean.setIniciativa(planoAcao.getIniciativa().getDescricao());
				reportBean.setTextoOque(planoAcao.getTexto());
				reportBean.setTextoComo(planoAcao.getTextoComo());
				reportBean.setTextoPorque(planoAcao.getTextoPorque());
				reportBean.setTextoQuem(planoAcao.getTextoQuem());
				reportBean.setData(simpleDateFormat.format(planoAcao.getDtPlano()));
				reportBean.setStatus(planoAcao.getStatus().toString());
				listaReportBean.add(reportBean);				
			}
		}
		
		if (listaReportBean.isEmpty()) {
			throw new GeplanesException("Nenhuma informação a ser exibida de acordo com o filtro selecionado.");
		}

		report.setDataSource(listaReportBean);
		return report;
	}
	
	/**
	 * Lista os planos de ação das iniciativas de acordo com o filtro selecionado.
	 * 
	 * @param filtro
	 */	
	public List<PlanoAcao> findByIniciativas(IniciativaPlanoAcaoFiltro filtro) {

		// Preenche as unidades gerenciais de acordo com a seleção feita pelo usuário
		List<UnidadeGerencial> listaUnidadeGerencial = new ArrayList<UnidadeGerencial>();		
		if (filtro.getUnidadeGerencial() != null) {
			listaUnidadeGerencial.add(filtro.getUnidadeGerencial());
			
			if (filtro.isIncluirSubordinadas()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUnidadeGerencial = unidadeGerencialService.getListaDescendencia(filtro.getUnidadeGerencial(), listaUnidadeGerencial);
			}
		}	
		filtro.setListaUnidadeGerencial(listaUnidadeGerencial);		
		
		// Preenche as unidades gerenciais de acordo com o perfil de acesso do usuário
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(filtro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (!usuarioLogadoIsAdmin) {
			if (listaUGUsuario != null) {
				for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
					listaUGDisponivel.add(unidadeGerencial);
					listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
				}				
			}
		}		
		filtro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		
		return planoAcaoDAO.findByIniciativas(filtro);
	}
}
