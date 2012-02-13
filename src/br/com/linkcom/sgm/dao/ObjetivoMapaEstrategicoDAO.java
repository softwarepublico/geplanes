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
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;

public class ObjetivoMapaEstrategicoDAO extends GenericDAO<ObjetivoMapaEstrategico> {

	/**
	 * Carrega os ids do objetivo estratégico, da unidade gerencial e do ano da gestão.
	 * 
	 * @param indicador
	 * @return
	 */
	public ObjetivoMapaEstrategico loadWithIDsUnidadePlanoGestao(ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		return query()
					.select(
							"objetivoMapaEstrategico.id, " +
							"objetivoEstrategico.id, objetivoEstrategico.descricao, " +
							"unidadeGerencial.id, " +
							"planoGestao.id")
					.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
					.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
					.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
					.join("mapaEstrategico.unidadeGerencial unidadeGerencial")
					.join("unidadeGerencial.planoGestao planoGestao")
					.where("objetivoMapaEstrategico = ?", objetivoMapaEstrategico)
					.unique();
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada UG, 
	 * baseado na Matriz de Iniciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialThroughMatrizFCS(UnidadeGerencial unidadeGerencial) {
		return query()
			.join("objetivoMapaEstrategico.listaMatrizFCS matrizFCS")
			.join("matrizFCS.unidadeGerencial unidadeGerencial")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
			.list();
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada UG, 
	 * baseado no Mapa Estratégico.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialThroughMapaEstrategico(UnidadeGerencial unidadeGerencial) {
		return query()
		.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
		.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
		.join("mapaEstrategico.unidadeGerencial unidadeGerencial")
		.where("unidadeGerencial = ?", unidadeGerencial)
		.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
		.list();
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva do Mapa Estratégico.
	 * 
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByPerspectivaMapaEstrategico(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return query()
			.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
			.where("perspectivaMapaEstrategico = ?", perspectivaMapaEstrategico)
			.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
			.list();
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva
	 * e uma determinada unidade gerencial, baseado no Mapa Estratégico.
	 * 
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @param orderBy
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico, String orderBy) {
		return query()
			.joinFetch("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")			
			.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")			
			.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
			.join("mapaEstrategico.unidadeGerencial unidadeGerencial")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.where("perspectivaMapaEstrategico = ?", perspectivaMapaEstrategico)
			.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
			.orderBy(orderBy != null ? orderBy : "objetivoEstrategico.descricao")
			.list();
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para uma determinada perspectiva
	 * e uma determinada unidade gerencial, baseado na Matriz de Inciativas x FCS.
	 * 
	 * @param unidadeGerencial
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByUnidadeGerencialPerspectivaThroughMatrizFCS(UnidadeGerencial unidadeGerencial, PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return query()
			.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")			
			.join("objetivoMapaEstrategico.listaMatrizFCS matrizFCS")
			.join("matrizFCS.unidadeGerencial unidadeGerencial")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.where("perspectivaMapaEstrategico = ?", perspectivaMapaEstrategico)
			.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
			.list();
	}
	
	/**
	 * Retorna os objetivos estratégicos cadastrados para um determinado ano de gestão.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<ObjetivoMapaEstrategico> findByPlanoGestaoThroughMapaEstrategico(PlanoGestao planoGestao) {
		return query()
		.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
		.join("perspectivaMapaEstrategico.mapaEstrategico mapaEstrategico")
		.join("mapaEstrategico.unidadeGerencial unidadeGerencial")
		.join("unidadeGerencial.planoGestao planoGestao")
		.where("planoGestao = ?", planoGestao)
		.orderBy("objetivoMapaEstrategico.objetivoEstrategico.descricao")
		.list();
	}
}
