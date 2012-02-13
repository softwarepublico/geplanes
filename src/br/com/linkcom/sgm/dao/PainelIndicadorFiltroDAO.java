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

import br.com.linkcom.sgm.beans.PainelIndicadorFiltro;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

public class PainelIndicadorFiltroDAO extends GenericDAO<PainelIndicadorFiltro> {
	
	/**
	 * Retorna uma lista com os objetivos estratégicos que serão disponibilizados
	 * na tela de Painel de Indicadores.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */	
	public List<PainelIndicadorFiltro> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		if (unidadeGerencial == null || unidadeGerencial.getId() == null) {
			return new ArrayList<PainelIndicadorFiltro>();
		}
		return 
			query()
				.select("painelIndicadorFiltro.id, " +
						"objetivoMapaEstrategico.id")
				.join("painelIndicadorFiltro.objetivoMapaEstrategico objetivoMapaEstrategico")
				.where("painelIndicadorFiltro.unidadeGerencial = ?", unidadeGerencial)
				.list();
	}
	
	/**
	 * Remove todos os filtros do painel de indicadores de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		getJdbcTemplate().update("DELETE FROM PAINELINDICADORFILTRO WHERE PAINELINDICADORFILTRO.UNIDADEGERENCIAL_ID = ?", new Object[]{unidadeGerencial.getId()});		
	}	
	
}
