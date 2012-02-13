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

import java.sql.Date;
import java.util.List;

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;
import br.com.linkcom.sgm.controller.filtro.IniciativaPlanoAcaoFiltro;


public class PlanoAcaoDAO extends GenericDAO<PlanoAcao> {
	
	/**
	 * Busca todos planos de ação vinculados a uma determinada anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return List<PlanoAcao>
	 */
	public List<PlanoAcao> findByAnomalia(Anomalia anomalia){
		return 
			query()
			.where("planoAcao.anomalia = ?", anomalia)
			.list();
	}

	/**
	 * Busca todos planos de ação vinculados a uma determinada ação preventiva.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return List<PlanoAcao>
	 */	
	public List<PlanoAcao> findByAcaoPreventiva(AcaoPreventiva acaoPreventiva) {
		return 
		query()
		.where("planoAcao.acaoPreventiva = ?", acaoPreventiva)
		.list();
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
		return 
			query()
				.where("planoAcao.unidadeGerencial = ?", unidadeGerencial)
				.where("planoAcao.iniciativa = ?", iniciativa)
				.list();
	}
	
	/**
	 * Remove todos os planos de ação (vinculados a iniciativas) de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		getJdbcTemplate().update("DELETE FROM PLANOACAO WHERE PLANOACAO.UNIDADEGERENCIAL_ID = ?", new Object[]{unidadeGerencial.getId()});		
	}
	
	/**
	 * Lista os planos de ação das iniciativas de acordo com o filtro selecionado.
	 * 
	 * @param filtro
	 */	
	public List<PlanoAcao> findByIniciativas(IniciativaPlanoAcaoFiltro filtro) {
		QueryBuilder<PlanoAcao> queryBuilder = 
			query()
				.select("planoAcao.id, planoAcao.texto, planoAcao.textoComo, planoAcao.textoPorque, planoAcao.textoQuem, planoAcao.dtPlano, planoAcao.status, planoAcao.dtAtualizacaoStatus, " +
						"iniciativa.id, iniciativa.descricao, " +
						"objetivoEstrategico.id, objetivoEstrategico.descricao, " +
						"unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, " +
						"planoGestao.id, planoGestao.anoExercicio")
				.join("planoAcao.unidadeGerencial unidadeGerencial")
				.join("unidadeGerencial.planoGestao planoGestao")
				.join("planoAcao.iniciativa iniciativa")
				.join("iniciativa.objetivoMapaEstrategico objetivoMapaEstrategico")
				.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
				.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
				.where("planoGestao = ?", filtro.getPlanoGestao())
				.where("perspectivaMapaEstrategico = ?", filtro.getPerspectivaMapaEstrategico())
				.where("objetivoMapaEstrategico = ?", filtro.getObjetivoMapaEstrategico())
				.where("iniciativa = ?", filtro.getIniciativa())
				.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencial(),"id",","))
				.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
				.orderBy("planoGestao.anoExercicio, unidadeGerencial.sigla, unidadeGerencial.nome, objetivoEstrategico.descricao, iniciativa.descricao, planoAcao.id");
		
		if (filtro.getExpirado() != null) {
			if (filtro.getExpirado()) {
				queryBuilder.where("planoAcao.dtPlano < ?", new Date(System.currentTimeMillis()));
			}
			else {
				queryBuilder.where("planoAcao.dtPlano >= ?", new Date(System.currentTimeMillis()));
			}
		}
		
		if (filtro.getListaStatusPlanoAcaoEnum() != null && !filtro.getListaStatusPlanoAcaoEnum().isEmpty()) {
			queryBuilder.openParentheses();
			int i = 0;
			for (StatusPlanoAcaoEnum statusPlanoAcaoEnum : filtro.getListaStatusPlanoAcaoEnum()) {
				queryBuilder.where("planoAcao.status = ?", statusPlanoAcaoEnum);
				if (i < filtro.getListaStatusPlanoAcaoEnum().size() - 1) {
					queryBuilder.or();
				}
				i++;
			}
			queryBuilder.closeParentheses();
		}		
		
		return queryBuilder.list();		
	}
}
