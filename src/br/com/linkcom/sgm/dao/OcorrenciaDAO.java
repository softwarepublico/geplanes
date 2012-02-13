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
package br.com.linkcom.sgm.dao;

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Ocorrencia;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.filtro.OcorrenciaFiltro;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

public class OcorrenciaDAO extends GenericDAO<Ocorrencia> {

	private UnidadeGerencialDAO unidadeGerencialDAO;
	
	public void setUnidadeGerencialDAO(UnidadeGerencialDAO unidadeGerencialDAO) {
		this.unidadeGerencialDAO = unidadeGerencialDAO;
	}

	@Override
	public void updateListagemQuery(QueryBuilder<Ocorrencia> query, FiltroListagem filtro) {
		OcorrenciaFiltro ocorrenciaFiltro = (OcorrenciaFiltro) filtro;
		query
			.select("ocorrencia.id,ocorrencia.numero,ocorrencia.descricao, ocorrencia.relator, ocorrencia.dataLancamento,ocorrencia.reincidente," +
					"unidadeGerencial.id,unidadeGerencial.sigla,unidadeGerencial.nome")
			.join("ocorrencia.unidadeGerencial unidadeGerencial")
			.join("unidadeGerencial.planoGestao planoGestao")
			.where("unidadeGerencial = ?",ocorrenciaFiltro.getUnidadeGerencial())
			.where("planoGestao = ?", ocorrenciaFiltro.getPlanoGestao())
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(ocorrenciaFiltro.getListaUnidadeGerencialDisponivel(),"id",","));
	}
	
	public boolean isOcorrenciaReincidente(Ocorrencia ocorrencia) {
		List<Ocorrencia> listaOcorrencias = 
			query()
				.leftOuterJoin("ocorrencia.anomalia anomalia")
				.where("ocorrencia <> ?", ocorrencia)
				.where("anomalia = ?", ocorrencia.getAnomalia())
				.list();
	
		return !listaOcorrencias.isEmpty();
	}	
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Ocorrencia> query) {
		query
		.select("ocorrencia.id,ocorrencia.numero,ocorrencia.descricao,ocorrencia.dataLancamento," +
				"ocorrencia.situacao,ocorrencia.unidadeGerencial,ocorrencia.relator,ocorrencia.reincidente," +
				"ocorrencia.contraMedidasImediatas")
		.leftOuterJoin("ocorrencia.unidadeGerencial unidadeGerencial")
		.leftOuterJoin("ocorrencia.relator usuario");
	}
	
	public List<Ocorrencia> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return query()
		.leftOuterJoinFetch("ocorrencia.relator usuario")
		.where("ocorrencia.unidadeGerencial=?",unidadeGerencial)
		.list();
	}

	/**
	 * Remove todas as ocorrências de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial bean) {
		getJdbcTemplate().update("DELETE FROM OCORRENCIA WHERE OCORRENCIA.UNIDADEGERENCIAL_ID = ?", new Object[]{bean.getId()});
	}

	/**
	 * @author Rodrigo Duarte
	 * @param bean
	 */
	public void setNullAnomalia(Anomalia bean) {
		getJdbcTemplate().update("update ocorrencia set anomalia_id=null where anomalia_id=?", new Object[]{bean.getId()});
	}

	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.getProximoSequencialOcorrencia(unidadeGerencial);
	}

}
