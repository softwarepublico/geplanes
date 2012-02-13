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

import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.SolicitacaoRepactuacaoIndicador;
import br.com.linkcom.sgm.beans.enumeration.AprovacaoEnum;
import br.com.linkcom.sgm.controller.filtro.SolicitacaoRepactuacaoIndicadorFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

public class SolicitacaoRepactuacaoIndicadorDAO extends GenericDAO<SolicitacaoRepactuacaoIndicador> {

	/**
	 * Veifica se tem alguma solicitação de repactuação do indicador aberta.
	 *
	 * @param indicador
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Boolean existeSolicitacaoRepactuacaoAberta(Indicador indicador) {
		if(indicador == null || indicador.getId() == null){
			throw new GeplanesException("O id do indicador não pode ser nulo.");
		}
		return new QueryBuilder<Long>(getHibernateTemplate())
				.select("count(*)")
				.from(SolicitacaoRepactuacaoIndicador.class)
				.where("solicitacaoRepactuacaoIndicador.status = ?", AprovacaoEnum.AG_APROVANDO)
				.where("solicitacaoRepactuacaoIndicador.indicador = ?", indicador)
				.unique() > 0;
	}
	
	public void saveOrUpdateStatus(SolicitacaoRepactuacaoIndicador repactuacaoIndicador) {
		getJdbcTemplate().update("update solicitacaoRepactuacaoIndicador set status="+repactuacaoIndicador.getStatus().ordinal()+", justificativaRes = '" + repactuacaoIndicador.getJustificativaRes() + "' where id=?", new Object[]{repactuacaoIndicador.getId()});		
	}
	
	public List<SolicitacaoRepactuacaoIndicador> findSolicitacoes(SolicitacaoRepactuacaoIndicadorFiltro filtro) {
		if (filtro == null || filtro.getPlanoGestao() == null) {
			throw new GeplanesException("Parâmetros inválidos na chamada do método SolicitacaoRepactuacaoIndicadorDAO.findSolicitacoes");
		}
		return query()
			.select("solicitacaoRepactuacaoIndicador.id, solicitacaoRepactuacaoIndicador.justificativaSol, solicitacaoRepactuacaoIndicador.justificativaRes, solicitacaoRepactuacaoIndicador.dtSolicitacao, solicitacaoRepactuacaoIndicador.status, " +
					"unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, " +
					"objetivoMapaEstrategico.id, " +
					"objetivoEstrategico.id, objetivoEstrategico.descricao, " +
					"indicador.id, indicador.nome, " +
					"usuario.id, usuario.nome")
			.join("solicitacaoRepactuacaoIndicador.indicador indicador")
			.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
			.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
			.join("indicador.unidadeGerencial unidadeGerencial")
			.join("unidadeGerencial.planoGestao planoGestao")
			.join("solicitacaoRepactuacaoIndicador.solicitante usuario")
			.where("planoGestao = ?", filtro.getPlanoGestao())
			.where("unidadeGerencial = ?", filtro.getUnidadeGerencial())
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
			.where("perspectivaMapaEstrategico = ?", filtro.getPerspectivaMapaEstrategico())
			.where("objetivoMapaEstrategico = ?", filtro.getObjetivoMapaEstrategico())
			.where("solicitacaoRepactuacaoIndicador.status = ?", filtro.getStatus())
			.orderBy("unidadeGerencial.sigla,objetivoEstrategico.descricao,indicador.nome")
			.list();
	}
	
	/**
	 * Retorna a solicitação de repactuação com os comentários associados
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoRepactuacaoIndicador
	 * @return solicitacaoRepactuacaoIndicador
	 */
	public SolicitacaoRepactuacaoIndicador loadWithComentarios(SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador) {
		return query()
			.leftOuterJoinFetch("solicitacaoRepactuacaoIndicador.comentario comentario")
			.leftOuterJoinFetch("comentario.listaComentarioItem comentarioitem")
			.leftOuterJoinFetch("comentarioitem.usuario usuario")
			.where("solicitacaoRepactuacaoIndicador = ?", solicitacaoRepactuacaoIndicador)
			.unique(); 
	}
	
	/**
	 * Retorna a solicitação de repactuação com o indicador associado
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoRepactuacaoIndicador
	 * @return solicitacaoRepactuacao
	 */
	public SolicitacaoRepactuacaoIndicador loadWithIndicador(SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador) {
		return query()
			.select("solicitacaoRepactuacaoIndicador.id, " +
					"indicador.id")
			.join("solicitacaoRepactuacaoIndicador.indicador indicador")
			.where("solicitacaoRepactuacaoIndicador = ?", solicitacaoRepactuacaoIndicador)
			.unique(); 
	}	
}
