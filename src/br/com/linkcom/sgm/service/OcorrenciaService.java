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

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Ocorrencia;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.dao.OcorrenciaDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.OcorrenciaFiltro;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class OcorrenciaService extends GenericService<Ocorrencia> {
		
	private OcorrenciaDAO ocorrenciaDAO;
	private UnidadeGerencialService unidadeGerencialService;
	private UsuarioService usuarioService;
	private AnomaliaService anomaliaService;
	
	public void setAnomaliaService(AnomaliaService anomaliaService) {this.anomaliaService = anomaliaService;}
	public void setOcorrenciaDAO(OcorrenciaDAO ocorrenciaDAO) {this.ocorrenciaDAO = ocorrenciaDAO;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	
	@Override
	public ListagemResult<Ocorrencia> findForListagem(FiltroListagem filtro) {
		OcorrenciaFiltro ocorrenciaFiltro = (OcorrenciaFiltro) filtro;
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(ocorrenciaFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (!usuarioLogadoIsAdmin) {
			if (listaUGUsuario != null) {
				listaUGDisponivel.addAll(listaUGUsuario);
			}
		}
		
		ocorrenciaFiltro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		return super.findForListagem(ocorrenciaFiltro);
	}	
	
	
	public boolean isOcorrenciaReincidente(Ocorrencia ocorrencia) {
		return ((OcorrenciaDAO) getGenericDAO()).isOcorrenciaReincidente(ocorrencia);
	}

	/**
	 * Verifica se o usuario logado pode alterar a ocorrência
	 * @author Rodrigo Duarte
	 * @param ocorrencia
	 * @return
	 */
	public Boolean podeAlterar(Ocorrencia ocorrencia) {		
		if  (usuarioService.isUsuarioLogadoAdmin() || usuarioService.isUsuarioLogadoResponsavelUG(ocorrencia.getUnidadeGerencial())) {
			return true;
		}		
		return false;
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param filtro
	 * @return Report com dados do diário de bordo
	 */
	public IReport createReportDiarioBordo(OcorrenciaFiltro filtro) {
		
		//Objetos
		Report report = new Report("/diarioBordo");
		UnidadeGerencial unidadeGerencial = unidadeGerencialService.load(filtro.getUnidadeGerencial());
		PlanoGestao planoGestao = unidadeGerencialService.obtemPlanoGestao(unidadeGerencial).getPlanoGestao();
		//Listas
		List<Ocorrencia> listaOcorrencia = this.findByUnidadeGerencial(unidadeGerencial);
		//Setando as anomalias em cada ocorrência.
		for (Ocorrencia ocorrencia : listaOcorrencia) {
			ocorrencia.setAnomalia(anomaliaService.findByOcorrencia(ocorrencia));
		}
		//Setando os paramentros no report.
		report.setDataSource(listaOcorrencia);
		report.addParameter("planoGestao", planoGestao.getAnoExercicio().toString());
		report.addParameter("unidadeGerencial", unidadeGerencial.getNome());
		
		return report;
	}
	
	public List<Ocorrencia> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return ocorrenciaDAO.findByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Remove todas as ocorrências de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deletebyUnidadeGerencial(UnidadeGerencial bean) {
		ocorrenciaDAO.deleteByUnidadeGerencial(bean); 
	}
	
	/**
	 * Seta para null as anomalias da ocorrencia
	 * @author Rodrigo Duarte
	 * @param bean
	 */
	public void setNullAnomalia(Anomalia bean) {
		ocorrenciaDAO.setNullAnomalia(bean);		
	}
	
	@Override
	public void delete(Ocorrencia bean) {
		//Verificando se a ocorrência faz parte de alguma nomalia antes de excluí-la.
		if(!anomaliaService.fazParteAnomalia(bean)){
			super.delete(bean);
		}
		else{
			throw new GeplanesException("Esta ocorrência faz parte de uma anomalia e não pode ser excluida.");
		}
	}
	
	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial) {
		return ocorrenciaDAO.getProximoSequencial(unidadeGerencial);
	}
	
}
