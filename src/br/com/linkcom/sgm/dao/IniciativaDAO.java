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

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;

public class IniciativaDAO extends GenericDAO<Iniciativa> {
	
	/**
	 * Retorna uma lista com as iniciativas de uma determinada unidade gerencial.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */	
	public List<Iniciativa> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		if (unidadeGerencial == null || unidadeGerencial.getId() == null) {
			return new ArrayList<Iniciativa>();
		}
		return 
			query()
				.select("iniciativa.id, iniciativa.descricao, " +
						"objetivoMapaEstrategico.id")
				.join("iniciativa.objetivoMapaEstrategico objetivoMapaEstrategico")
				.where("iniciativa.unidadeGerencial = ?", unidadeGerencial)
				.orderBy("iniciativa.descricao")
				.list();
	}
	
	/**
	 * Retorna uma lista com as iniciativas de uma determinada
	 * unidade gerencial e um determinado objetivo estratégico.
	 * 
	 * @param unidadeGerencial
	 * @param objetivoMapaEstrategico
	 * @return
	 */	
	public List<Iniciativa> findByUnidadeGerencialObjetivoEstrategico(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		if (unidadeGerencial == null || objetivoMapaEstrategico == null) {
			return new ArrayList<Iniciativa>();
		}
		return 
			query()
				.select("iniciativa.id, iniciativa.descricao")
				.join("iniciativa.unidadeGerencial unidadeGerencial")
				.join("iniciativa.objetivoMapaEstrategico objetivoMapaEstrategico")
				.where("unidadeGerencial = ?", unidadeGerencial)
				.where("objetivoMapaEstrategico = ?", objetivoMapaEstrategico)
				.orderBy("iniciativa.descricao")
				.list();
	}
	
	/**
	 * Retorna a iniciativa, bem como o plano de ação associado a ela.
	 * 
	 * @param iniciativa
	 * @return
	 */
	public Iniciativa loadWithPlanoAcao(Iniciativa iniciativa) {
		return query()
			.from(Iniciativa.class)
			.leftOuterJoinFetch("iniciativa.listaPlanoAcao planoAcao")
			.entity(iniciativa)
			.unique();
	}
	
	public void atualizaIniciativa(Iniciativa iniciativa) {
		getHibernateTemplate().bulkUpdate("update Iniciativa i set i.descricao = ? where i.id = ?", new Object[]{iniciativa.getDescricao(), iniciativa.getId()});				
	}
	
	public void deleteWhereNotIn(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico, String listAndConcatenate) {
		getJdbcTemplate().execute("DELETE FROM INICIATIVA WHERE INICIATIVA.UNIDADEGERENCIAL_ID = " + unidadeGerencial.getId() + " AND INICIATIVA.OBJETIVOMAPAESTRATEGICO_ID = " + objetivoMapaEstrategico.getId() + (!listAndConcatenate.equals("") ? " AND INICIATIVA.ID NOT IN ("+listAndConcatenate+")" : ""));
	}
	
	/**
	 * Remove todas as iniciativas de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		getJdbcTemplate().update("DELETE FROM INICIATIVA WHERE INICIATIVA.UNIDADEGERENCIAL_ID = ?", new Object[]{unidadeGerencial.getId()});		
	}	
}
