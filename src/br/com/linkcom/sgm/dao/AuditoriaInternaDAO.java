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
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.AuditoriaInterna;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.filtro.AuditoriaInternaFiltro;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

@DefaultOrderBy("nome")
public class AuditoriaInternaDAO extends GenericDAO<AuditoriaInterna> {

	@Override
	public void updateListagemQuery(QueryBuilder<AuditoriaInterna> query, FiltroListagem _filtro) {
		AuditoriaInternaFiltro filtro = (AuditoriaInternaFiltro) _filtro;
		query
			.select("auditoriaInterna.id, auditoriaInterna.dataAuditoria, auditoriaInterna.status, auditoriaInterna.dataEncerramento, " +
					"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, " +
					"ugResponsavel.id, ugResponsavel.nome, ugResponsavel.sigla, " +
					"norma.id, norma.nome, " +
					"planoGestaoOrigem.id, planoGestaoOrigem.anoExercicio")
			.join("auditoriaInterna.ugRegistro ugRegistro")
			.join("auditoriaInterna.ugResponsavel ugResponsavel")
			.join("auditoriaInterna.norma norma")
			.join("ugRegistro.planoGestao planoGestaoOrigem")
			.join("ugResponsavel.planoGestao planoGestaoResponsavel")
			.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialResp(),"id",","))
			.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
			.where("planoGestaoOrigem = ?" , filtro.getPlanoGestao())
			.where("planoGestaoResponsavel = ?" , filtro.getPlanoGestao())
			.where("norma = ?", filtro.getNorma())
			.where("auditoriaInterna.status = ?", filtro.getStatus())
			.orderBy("ugRegistro.sigla,ugResponsavel.sigla");
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<AuditoriaInterna> query) {
		query
			.select("auditoriaInterna.id, auditoriaInterna.dataAuditoria, auditoriaInterna.observacoes, auditoriaInterna.status, auditoriaInterna.dataEncerramento, " +
					"normaAuditoria.id, normaAuditoria.nome, " +
					"ugRegistro.id, ugRegistro.sigla, ugRegistro.nome, " +
					"ugResponsavel.id, ugResponsavel.sigla, ugResponsavel.nome, " +
					"planoGestao.id, planoGestao.anoExercicio, " +
					"itemAuditoriaInterna.id, itemAuditoriaInterna.descricao, " +
					"ugExterna.id, ugExterna.sigla, ugExterna.nome, " + 
					"requisitoNorma.id, requisitoNorma.indice, requisitoNorma.descricao, " +
					"normaRequisito.id, normaRequisito.nome, " +
					"usuarioAuditoriaInterna.id, usuarioAuditoriaInterna.nome, usuarioAuditoriaInterna.funcao, usuarioAuditoriaInterna.tipo")
			.leftOuterJoin("auditoriaInterna.norma normaAuditoria")
			.leftOuterJoin("auditoriaInterna.ugRegistro ugRegistro")
			.leftOuterJoin("auditoriaInterna.ugResponsavel ugResponsavel")
			.leftOuterJoin("ugRegistro.planoGestao planoGestao")
			.leftOuterJoin("auditoriaInterna.listaItemAuditoriaInterna itemAuditoriaInterna")
			.leftOuterJoin("itemAuditoriaInterna.ugExterna ugExterna")
			.leftOuterJoin("auditoriaInterna.listaUsuarioAuditoriaInterna usuarioAuditoriaInterna")
			.leftOuterJoin("itemAuditoriaInterna.requisitoNorma requisitoNorma")
			.leftOuterJoin("requisitoNorma.norma normaRequisito")
			.orderBy("usuarioAuditoriaInterna.nome, requisitoNorma.id");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaItemAuditoriaInterna");
		save.saveOrUpdateManaged("listaUsuarioAuditoriaInterna");
	}
	
	public List<AuditoriaInterna> findIdByUGRegistro(UnidadeGerencial ugRegistro) {
		return 
			query()
				.select("auditoriaInterna.id")
				.join("auditoriaInterna.ugRegistro ugRegistro")
				.where("ugRegistro = ?", ugRegistro)
				.list();
	}	
	
	public List<AuditoriaInterna> findIdByUGResponsavel(UnidadeGerencial ugResponsavel) {
		return 
			query()
				.select("auditoriaInterna.id")
				.join("auditoriaInterna.ugResponsavel ugResponsavel")
				.where("ugResponsavel = ?", ugResponsavel)
				.list();
	}	
}
