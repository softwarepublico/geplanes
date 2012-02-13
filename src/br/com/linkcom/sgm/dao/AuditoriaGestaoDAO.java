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

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.AuditoriaGestao;
import br.com.linkcom.sgm.beans.ModeloAuditoriaGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.filtro.AuditoriaGestaoFiltro;


public class AuditoriaGestaoDAO extends GenericDAO<AuditoriaGestao> {

	@Override
	public void updateListagemQuery(QueryBuilder<AuditoriaGestao> query, FiltroListagem _filtro) {
		AuditoriaGestaoFiltro filtro = (AuditoriaGestaoFiltro) _filtro;
		
		query
			.select("auditoriaGestao.id, auditoriaGestao.descricao, auditoriaGestao.responsavel, auditoriaGestao.dataAuditoria, " +
					"unidadeGerencial.sigla, planoGestao.anoExercicio")
			.join("auditoriaGestao.unidadeGerencial unidadeGerencial")
			.join("auditoriaGestao.modeloAuditoriaGestao modeloAuditoriaGestao")
			.join("unidadeGerencial.planoGestao planoGestao")
			.whereLikeIgnoreAll("auditoriaGestao.descricao", filtro.getDescricao())
			.where("planoGestao = ?", filtro.getPlanoGestao())
			.where("unidadeGerencial = ?", filtro.getUnidadeGerencial())
			.where("modeloAuditoriaGestao = ?", filtro.getModeloAuditoriaGestao())
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
			.where("auditoriaGestao.dataAuditoria >= ?", filtro.getDataAuditoria1())
			.where("auditoriaGestao.dataAuditoria <= ?", filtro.getDataAuditoria2());
		
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<AuditoriaGestao> query) {
		query
			.leftOuterJoinFetch("auditoriaGestao.unidadeGerencial unidadeGerencial")
			.leftOuterJoinFetch("unidadeGerencial.planoGestao planoGestao")
			.leftOuterJoinFetch("auditoriaGestao.modeloAuditoriaGestao modeloAuditoriaGestao")
			.leftOuterJoinFetch("auditoriaGestao.listaAuditoriaGestaoIndicador listaAuditoriaGestaoIndicador")
			.leftOuterJoinFetch("listaAuditoriaGestaoIndicador.indicador indicador")
			.openParentheses()
				.where("indicador.status = ?", StatusIndicadorEnum.APROVADO)			
				.or()
				.where("indicador.status = ?", StatusIndicadorEnum.EM_CANCELAMENTO)			
			.closeParentheses()
			.orderBy("indicador.nome");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}

	public boolean modeloUsado(ModeloAuditoriaGestao form) {
		return new QueryBuilder<Long>(getHibernateTemplate())
					.from(AuditoriaGestao.class)
					.select("count(*)")
					.setUseTranslator(false)
					.where("auditoriaGestao.modeloAuditoriaGestao = ?", form)
					.unique() > 0;
	}
	
	/**
	 * Remove todas as auditorias de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		getJdbcTemplate().update("DELETE FROM AUDITORIAGESTAO WHERE AUDITORIAGESTAO.UNIDADEGERENCIAL_ID = ?", new Object[]{unidadeGerencial.getId()});		
	}	
}
