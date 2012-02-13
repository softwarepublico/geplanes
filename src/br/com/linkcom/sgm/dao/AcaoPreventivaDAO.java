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
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.filtro.AcaoPreventivaFiltro;

public class AcaoPreventivaDAO extends GenericDAO<AcaoPreventiva> {
	
	private UnidadeGerencialDAO unidadeGerencialDAO;
	
	public void setUnidadeGerencialDAO(UnidadeGerencialDAO unidadeGerencialDAO) {this.unidadeGerencialDAO = unidadeGerencialDAO;}
	
	@Override
	public void updateListagemQuery(QueryBuilder<AcaoPreventiva> query, FiltroListagem filtro) {
		AcaoPreventivaFiltro acaoPreventivaFiltro = (AcaoPreventivaFiltro) filtro;
		query
			.select("acaoPreventiva.id,acaoPreventiva.descricao, acaoPreventiva.dataAbertura, acaoPreventiva.sequencial, " +
					"acaoPreventiva.dataEncerramento, acaoPreventiva.status, " +
					"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, " +
					"ugRegistroPai.id, " +
					"nivelHierarquico.id, nivelHierarquico.descricao, " +					
					"planoGestaoRegistro.id, planoGestaoRegistro.anoExercicio")
			.join("acaoPreventiva.ugRegistro ugRegistro")
			.join("ugRegistro.planoGestao planoGestaoRegistro")
			.leftOuterJoin("ugRegistro.nivelHierarquico nivelHierarquico")			
			.leftOuterJoin("ugRegistro.subordinacao ugRegistroPai")
			.whereLikeIgnoreAll("acaoPreventiva.descricao", acaoPreventivaFiltro.getDescricao())
			.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(acaoPreventivaFiltro.getListaUnidadeGerencialReg(),"id",","))
			.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(acaoPreventivaFiltro.getListaUnidadeGerencialDisponivel(),"id",","))
			.where("planoGestaoRegistro = ?" , acaoPreventivaFiltro.getPlanoGestao())
			.where("acaoPreventiva.sequencial = ?" , acaoPreventivaFiltro.getSequencial())
			.where("acaoPreventiva.status = ?", acaoPreventivaFiltro.getStatus())
			.orderBy("acaoPreventiva.sequencial,ugRegistro.sigla");
	}
	
	@Override
	public AcaoPreventiva loadForEntrada(AcaoPreventiva bean) {
		return query()
			.select("acaoPreventiva.id, acaoPreventiva.observacoes, acaoPreventiva.descricao, acaoPreventiva.conclusao, acaoPreventiva.status, " +
					"acaoPreventiva.dataAbertura, acaoPreventiva.dataEncerramento,acaoPreventiva.sequencial, acaoPreventiva.tipo, acaoPreventiva.origem, " +
					"acaoPreventiva.avalEficaciaAcao, acaoPreventiva.evidenciaEficaciaAcao, " +
					"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, " +
					"ugRegistroPai.id, " +
					"nivelHierarquico.id, nivelHierarquico.descricao, " +					
					"planoGestaoRegistro.id, planoGestaoRegistro.anoExercicio")
			.from(AcaoPreventiva.class)
			.entity(bean)
			.join("acaoPreventiva.ugRegistro ugRegistro")
			.leftOuterJoin("ugRegistro.nivelHierarquico nivelHierarquico")
			.join("ugRegistro.planoGestao planoGestaoRegistro")
			.leftOuterJoin("ugRegistro.subordinacao ugRegistroPai")
			.unique();
	}

	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.getProximoSequencialAcaoPreventiva(unidadeGerencial);
	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("planosAcao");
	}

	/**
	 * Remove todas as ações preventivas de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial bean) {
		getJdbcTemplate().update("DELETE FROM ACAOPREVENTIVA WHERE ACAOPREVENTIVA.UGREGISTRO_ID = ?", new Object[]{bean.getId()});
	}	
}
