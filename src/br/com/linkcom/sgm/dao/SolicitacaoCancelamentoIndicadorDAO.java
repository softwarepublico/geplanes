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
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.SolicitacaoCancelamentoIndicador;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.enumeration.AprovacaoEnum;
import br.com.linkcom.sgm.controller.filtro.SolicitacaoCancelamentoIndicadorFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;


public class SolicitacaoCancelamentoIndicadorDAO extends GenericDAO<SolicitacaoCancelamentoIndicador> {

	/**
	 * Retorna uma lista de solicitações de cancelamento de indicador de acordo com o filtro
	 * Somente o planoGestao do filtro é obrigatório.
	 *  
	 * @author Rodrigo Alvarenga
	 * @param filtro
	 * @return List<SolicitacaoCancelamentoIndicador>
	 */
	public List<SolicitacaoCancelamentoIndicador> findSolicitacoes(SolicitacaoCancelamentoIndicadorFiltro filtro) {
		if (filtro == null || filtro.getPlanoGestao() == null) {
			throw new GeplanesException("Parâmetros inválidos na chamada do método SolicitacaoCancelamentoIndicadorDAO.findSolicitacoes");
		}
		return query()
			.select("solicitacaoCancelamentoIndicador.id, solicitacaoCancelamentoIndicador.justificativaSol, solicitacaoCancelamentoIndicador.justificativaRes, solicitacaoCancelamentoIndicador.dtSolicitacao, solicitacaoCancelamentoIndicador.status, " +
					"unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, " +
					"objetivoMapaEstrategico.id, " +
					"objetivoEstrategico.id, objetivoEstrategico.descricao, " +
					"indicador.id, indicador.nome, " +
					"usuario.id, usuario.nome")
			.join("solicitacaoCancelamentoIndicador.indicador indicador")
			.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
			.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
			.join("indicador.unidadeGerencial unidadeGerencial")
			.join("unidadeGerencial.planoGestao planoGestao")
			.join("solicitacaoCancelamentoIndicador.solicitante usuario")
			.where("planoGestao = ?", filtro.getPlanoGestao())
			.where("unidadeGerencial = ?", filtro.getUnidadeGerencial())
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
			.where("perspectivaMapaEstrategico = ?", filtro.getPerspectivaMapaEstrategico())
			.where("objetivoMapaEstrategico = ?", filtro.getObjetivoMapaEstrategico())
			.where("solicitacaoCancelamentoIndicador.status = ?", filtro.getStatus())
			.orderBy("unidadeGerencial.sigla,objetivoEstrategico.descricao,indicador.nome")
			.list();
	}	

	/**
	 * @author Rodrigo Duarte
	 * @param indicador
	 * @return
	 */
	public List<SolicitacaoCancelamentoIndicador> findByIndicador(Indicador indicador) {
		return 
			query()
				.select("solicitacaoCancelamentoIndicador.id, " +
						"indicador.id")
				.leftOuterJoin("solicitacaoCancelamentoIndicador.indicador indicador")
				.where("indicador = ?", indicador)
				.list();
	}
	
	/**
	 * Update status
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoCancelamento
	 */
	public void saveOrUpdateStatus(SolicitacaoCancelamentoIndicador solicitacaoCancelamento) {
		getJdbcTemplate().update("update solicitacaoCancelamentoIndicador set status="+solicitacaoCancelamento.getStatus().ordinal()+", justificativaRes = '" + solicitacaoCancelamento.getJustificativaRes() + "' where id=?", new Object[]{solicitacaoCancelamento.getId()});		
	}

	/**
	 * @author Rodrigo Alvarenga
	 * @param usuario
	 * @return
	 */
	public List<SolicitacaoCancelamentoIndicador> findByUsuario(Usuario solicitante) {
		return query()
		.leftOuterJoin("solicitacaoCancelamentoIndicador.solicitante solicitante")
		.where("solicitante=?", solicitante)
		.list();

	}
	
	/**
	 * Retorna as solicitações de cancelamento de indicador (em aprovação, aprovadas ou reprovadas) 
	 * para um determinado objetivo estratégico.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param objetivoMapaEstrategico
	 * @return lista de solicitações de cancelamento de indicador
	 */
	public List<SolicitacaoCancelamentoIndicador> find(ObjetivoMapaEstrategico objetivoMapaEstrategico, AprovacaoEnum statusAprovacao) {
		return query()
			.join("solicitacaoCancelamentoIndicador.indicador indicador")
			.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.where("solicitacaoCancelamentoIndicador.status = ?", statusAprovacao)
			.where("objetivoMapaEstrategico = ?", objetivoMapaEstrategico)
			.list();

	}
	
	/**
	 * Retorna a solicitação de cancelamento com o indicador associado
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoCancelamentoIndicador
	 * @return solicitacaoCancelamento
	 */
	public SolicitacaoCancelamentoIndicador loadWithIndicador(SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador) {
		return query()
			.select("solicitacaoCancelamentoIndicador.id, " +
					"indicador.id")
			.join("solicitacaoCancelamentoIndicador.indicador indicador")
			.where("solicitacaoCancelamentoIndicador = ?", solicitacaoCancelamentoIndicador)
			.unique(); 
	}

	public boolean isAprovado(SolicitacaoCancelamentoIndicador solicitacaoCancelamento) {
		return new QueryBuilder<Long>(getHibernateTemplate())
					.select("count(*)")
					.from(SolicitacaoCancelamentoIndicador.class, "sol")
					.where("sol = ?", solicitacaoCancelamento)
					.where("sol.status = ?", AprovacaoEnum.APROVADO)
					.unique() > 0;
	}
	
	/**
	 * Retorna a solicitação de cancelamento com os comentários associados
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoCancelamentoIndicador
	 * @return solicitacaoCancelamentoIndicador
	 */
	public SolicitacaoCancelamentoIndicador loadWithComentarios(SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador) {
		return query()
			.leftOuterJoinFetch("solicitacaoCancelamentoIndicador.comentario comentario")
			.leftOuterJoinFetch("comentario.listaComentarioItem comentarioitem")
			.leftOuterJoinFetch("comentarioitem.usuario usuario")
			.where("solicitacaoCancelamentoIndicador = ?", solicitacaoCancelamentoIndicador)
			.unique(); 
	}
	
	/**
	 * Veifica se tem alguma solicitação de cancelamento do indicador aberta.
	 *
	 * @param indicador
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Boolean existeSolicitacaoCancelamentoAberta(Indicador indicador) {
		if(indicador == null || indicador.getId() == null){
			throw new GeplanesException("O id do indicador não pode ser nulo.");
		}
		return new QueryBuilder<Long>(getHibernateTemplate())
				.select("count(*)")
				.from(SolicitacaoCancelamentoIndicador.class)
				.where("solicitacaoCancelamentoIndicador.status = ?", AprovacaoEnum.AG_APROVANDO)
				.where("solicitacaoCancelamentoIndicador.indicador = ?", indicador)
				.unique() > 0;
	}	
}
