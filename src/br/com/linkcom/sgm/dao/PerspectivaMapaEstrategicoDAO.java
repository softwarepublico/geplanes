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

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;

public class PerspectivaMapaEstrategicoDAO extends GenericDAO<PerspectivaMapaEstrategico> {
	
	/**
	 * Método retorna a lista de todas as perspectivas (com seus objetivos estratégicos) relacionadas a um determinado MapaEstratégico.
	 * @author Matheus Melo Gonçalves
	 * @param mapaEstrategico
	 * @return List<Perspectiva>
	 */
	public List<PerspectivaMapaEstrategico> findByMapaEstrategico(MapaEstrategico mapaEstrategico){
		return 
			query()
				.select("perspectivaMapaEstrategico.id, perspectivaMapaEstrategico.ordem, " +
						"perspectiva.id, perspectiva.descricao," +
						"objetivoMapaEstrategico.id, " +
						"objetivoEstrategico.id, objetivoEstrategico.descricao")
				.join("perspectivaMapaEstrategico.perspectiva perspectiva")
				.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
				.leftOuterJoin("perspectivaMapaEstrategico.listaObjetivoMapaEstrategico objetivoMapaEstrategico")
				.leftOuterJoin("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
				.where("mapaEstrategico = ?", mapaEstrategico)
				.orderBy("perspectivaMapaEstrategico.ordem, objetivoMapaEstrategico.id")
				.list();
	}
	
	/**
	 * Retorna as perspectivas cadastradas para uma determinada UG, 
	 * baseado no Mapa Estrategico.
	 * 
	 * @param unidadeGerencial
	 * @param orderBy
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> findByUnidadeGerencialThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, String orderBy) {
		return query()
			.select("perspectivaMapaEstrategico.id, " +
					"perspectiva.id, perspectiva.descricao, " +
					"mapaEstrategico.id, " +
					"unidadeGerencial.id")
			.join("perspectivaMapaEstrategico.perspectiva perspectiva")
			.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
			.join("mapaEstrategico.unidadeGerencial unidadeGerencial")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.orderBy(orderBy != null ? orderBy : "perspectivaMapaEstrategico.perspectiva.descricao")
			.list();
	}
	
	/**
	 * Retorna as perspectivas cadastradas para uma determinada UG, 
	 * baseado na Matriz de Inciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> findByUnidadeGerencialThroughMatrizFCS(UnidadeGerencial unidadeGerencial) {
		return query()
			.join("perspectivaMapaEstrategico.listaObjetivoMapaEstrategico objetivoMapaEstrategico")
			.join("objetivoMapaEstrategico.listaMatrizFCS matrizFCS")
			.join("matrizFCS.unidadeGerencial unidadeGerencial")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.orderBy("perspectivaMapaEstrategico.perspectiva.descricao")
			.list();
	}
	
	/**
	 * Carrega a perspectiva do mapa estratégico com a unidade gerencial à qual está vinculada.
	 * 
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public PerspectivaMapaEstrategico loadWithUnidadeGerencial(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return query()
			.select("perspectivaMapaEstrategico.id, " +
					"perspectiva.id, perspectiva.descricao, " +
					"mapaEstrategico.id, " +
					"unidadeGerencial.id")
			.from(PerspectivaMapaEstrategico.class)
			.join("perspectivaMapaEstrategico.perspectiva perspectiva")
			.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
			.join("mapaEstrategico.unidadeGerencial unidadeGerencial")
			.entity(perspectivaMapaEstrategico)
			.unique();
	}
}
