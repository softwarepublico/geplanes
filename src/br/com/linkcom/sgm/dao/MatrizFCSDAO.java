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
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;

public class MatrizFCSDAO extends GenericDAO<MatrizFCS> {
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaMatrizFcsFator");
	}
	
	public MatrizFCS loadInfoMatriz(MatrizFCS matrizFCS){
		if (matrizFCS == null || matrizFCS.getObjetivoMapaEstrategico() == null || matrizFCS.getUnidadeGerencial() == null) {
			throw new RuntimeException("Todos os campos são obrigatórios");
		}
		return
			query()
				.leftOuterJoinFetch("matrizFCS.fatorAvaliacao fatorAvaliacao")
				.where("matrizFCS.unidadeGerencial=?",matrizFCS.getUnidadeGerencial())
				.where("matrizFCS.objetivoMapaEstrategico = ?", matrizFCS.getObjetivoMapaEstrategico())
				.unique();
	}
	
	public MatrizFCS carregarMatriz(MatrizFCS matrizFCS){
		return query()
				.joinFetch("matrizFCS.objetivoMapaEstrategico objetivoMapaEstrategico")
				.joinFetch("matrizFCS.listaMatrizFcsIniciativa listaMatrizFcsIniciativa")
				.joinFetch("listaMatrizFcsIniciativa.listaMatrizFcsIniciativaFator listaMatrizFcsIniciativaFator")
				.joinFetch("listaMatrizFcsIniciativaFator.itemFatorAvaliacao itemFatorAvaliacao")
				.entity(matrizFCS)
				.unique();
	}
	
	/**
	 * Retorna as matrizes para uma unidade gerencial e um objetivo estratégico.
	 * Se o objetivo estratégico for nulo retorna as matrizes para todos os objetivos da unidade gerencial.
	 * 
	 * @param unidadeGerencial
	 * @param objetivoMapaEstrategico
	 * @return
	 */
	public List<MatrizFCS> findByUnidadeGerencialObjetivoEstrategico(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		return 
			query()
				.joinFetch("matrizFCS.objetivoMapaEstrategico objetivoMapaEstrategico")
				.where("matrizFCS.unidadeGerencial = ?", unidadeGerencial)
				.where("objetivoMapaEstrategico = ?", objetivoMapaEstrategico)
				.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
				.list();
	}
	
	/***
	 * Busca os objetivos estratégicos que estão associados a uma determinada unidade gerencial
	 * através da matriz de iniciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */	
	public List<MatrizFCS> findWithEstrategiasByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		if (unidadeGerencial == null || unidadeGerencial.getId() == null) {
			return new ArrayList<MatrizFCS>();
		}
		return 
			query()
				.select("matrizFCS.id, " +
						"objetivoEstrategico.id, objetivoEstrategico.descricao")
				.join("matrizFCS.objetivoMapaEstrategico objetivoMapaEstrategico")
				.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
				.where("matrizFCS.unidadeGerencial = ?", unidadeGerencial)
				.orderBy("objetivoEstrategico.descricao")
				.list();
	}
	
	/**
	 * Remove todas as matrizes de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		getJdbcTemplate().update("DELETE FROM MATRIZFCS WHERE MATRIZFCS.UNIDADEGERENCIAL_ID = ?", new Object[]{unidadeGerencial.getId()});		
	}	

	/**
	 * Carrega a matriz de iniciativas x fcs de uma determinada unidade gerencial.
	 *
	 * @param unidadeGerencial
	 * @return
	 */
	public MatrizFCS loadByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return 
			query()
				.join("matrizFCS.unidadeGerencial unidadeGerencial")
				.where("unidadeGerencial = ?", unidadeGerencial)
				.unique();
	}	
}
