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

import java.util.Calendar;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoEstrategico;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.controller.report.filtro.PendenciaAnomaliaReportFiltro;
import br.com.linkcom.sgm.filtro.IndicadorFiltro;
import br.com.linkcom.sgm.service.AnexoIndicadorService;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;


@DefaultOrderBy("nome")
public class IndicadorDAO extends GenericDAO<Indicador> {
	
	private AnexoIndicadorService anexoIndicadorService;
	
	public void setAnexoIndicadorService(
			AnexoIndicadorService anexoIndicadorService) {
		this.anexoIndicadorService = anexoIndicadorService;
	}	

	/**
	 * Retorna uma lista de indicadores cadastrados para uma determinada unidade gerencial
	 * e vinculados a um determinado objetivo estratégico.
	 * 
	 * @param unidadeGerencial
	 * @param objetivoMapaEstrategico
	 * @param incluirIndicadoresCancelados
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @return  
	 */
	public List<Indicador> findByUnidadeGerencialObjetivoEstrategico(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico, boolean incluirIndicadoresCancelados, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia) {
		QueryBuilder<Indicador> queryBuilder = query();
		queryBuilder
			.joinFetch("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.joinFetch("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
			.join("indicador.unidadeGerencial unidadeGerencial")
			.leftOuterJoinFetch("indicador.unidadeMedida um")
			.where("objetivoMapaEstrategico = ?", objetivoMapaEstrategico)
			.where("unidadeGerencial = ?", unidadeGerencial)
			.orderBy("indicador.nome");
		
		if (!incluirIndicadoresCancelados) {
			queryBuilder
				.where("indicador.status <> ?", StatusIndicadorEnum.CANCELADO);
		}
		
		if (listarAnexo) {
			queryBuilder
				.leftOuterJoinFetch("indicador.anexosIndicador anexoIndicador1")
				.leftOuterJoinFetch("anexoIndicador1.arquivo arquivo1");
		}
		
		if (listarAcompanhamento) {
			queryBuilder.leftOuterJoinFetch("indicador.acompanhamentosIndicador acompanhamentoIndicador1");
			
			if (listarAnomalia) {
				queryBuilder.leftOuterJoinFetch("acompanhamentoIndicador1.anomalia anomalia1");
			}
		}
				
		return queryBuilder.list();
	}
	
	/**
	 * Lista os indicadores de uma determinada unidade gerencial.
	 * Pode ser filtrado por um ou vários status.
	 * 
	 * @param unidadeGerencial
	 * @param listaStatusIndicador
	 * @return 
	 */
	public List<Indicador> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial, StatusIndicadorEnum... listaStatusIndicador) { 
		QueryBuilder<Indicador> queryBuilder = 
			query()
				.select("indicador.id,indicador.nome,indicador.frequencia,indicador.melhor," +
						"objetivoEstrategico.id, objetivoEstrategico.descricao")
				.leftOuterJoin("indicador.unidadeMedida unidadeMedida")
				.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
				.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
				.join("indicador.unidadeGerencial unidadeGerencial")
				.join("unidadeGerencial.planoGestao planoGestao")
				.where("unidadeGerencial = ?", unidadeGerencial)
				.orderBy("indicador.nome");				
		
		if (listaStatusIndicador != null && listaStatusIndicador.length > 0) {
			queryBuilder.openParentheses();
			int i = 0;
			for (StatusIndicadorEnum statusIndicadorEnum : listaStatusIndicador) {
				queryBuilder.where("indicador.status = ?", statusIndicadorEnum);
				if (i < listaStatusIndicador.length - 1) {
					queryBuilder.or();
				}
				i++;
			}
			queryBuilder.closeParentheses();
		}

		return queryBuilder.list();
	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Indicador> query, FiltroListagem filtro) {
		IndicadorFiltro indicadorFiltro = (IndicadorFiltro) filtro;
		
		query
			.select("indicador.id,indicador.nome,indicador.melhor,indicador.precisao,indicador.tolerancia," +
					"indicador.frequencia,indicador.responsavel,indicador.relevancia,indicador.frequenciaAcompanhamento," +
					"indicador.mecanismoControle,indicador.fonteDados,indicador.formulaCalculo, " +
					"objetivoEstrategico.id, objetivoEstrategico.descricao, " +
					"unidadeMedida.sigla," +
					"unidadeGerencial.id, " +
					"planoGestao.id, planoGestao.limiteCriacaoMetasIndicadores")
			.leftOuterJoin("indicador.unidadeMedida unidadeMedida")			
			.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.join("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
			.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
			.leftOuterJoin("indicador.unidadeGerencial unidadeGerencial")			
			.leftOuterJoin("unidadeGerencial.planoGestao planoGestao")			
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(indicadorFiltro.getListaUnidadeGerencial(),"id",","))
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(indicadorFiltro.getListaUnidadeGerencialDisponivel(),"id",","))
			.where("planoGestao = ?", indicadorFiltro.getPlanoGestao())
			.where("perspectivaMapaEstrategico = ?", indicadorFiltro.getPerspectivaMapaEstrategico())
			.where("objetivoMapaEstrategico = ?", indicadorFiltro.getObjetivoMapaEstrategico());
		
		super.updateListagemQuery(query, filtro);
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Indicador> query) {
		query
			.joinFetch("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.joinFetch("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
			.leftOuterJoinFetch("indicador.anexosIndicador anexoIndicador")
			.leftOuterJoinFetch("anexoIndicador.arquivo arquivo");
		super.updateEntradaQuery(query);
	}	

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		Indicador indicador = (Indicador) save.getEntity();
		if(indicador.getId() != null){
			anexoIndicadorService.excluiAnexoIndicadorDetalhe(indicador);
		}
		
		save.saveOrUpdateManaged("acompanhamentosIndicador");
	}


	@Override
	public void saveOrUpdate(final Indicador arg0) {
		getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus arg1) {
				IndicadorDAO.super.saveOrUpdate(arg0);
				
				anexoIndicadorService.salvaAnexoIndicadorDetalhe(arg0);
				
				return null;
			}});
	}

	public Indicador obtemFrequencia(Indicador indicador) {
		return query()
			.select("indicador.id, indicador.frequencia")
			.from(Indicador.class)
			.where("indicador = ?", indicador)
			.unique();
	}
	
	public Indicador obtemColecaoAnexoIndicador(Indicador indicador) {
		return query()
		.from(Indicador.class)
		.leftOuterJoinFetch("indicador.anexosIndicador anexosIndicador")
		.leftOuterJoinFetch("anexosIndicador.arquivo")
		.entity(indicador)
		.unique();
	}

	public Indicador obtemColecaoAcompanhamento(Indicador indicador, Calendar dtLimiteInicial, Calendar dtLimiteFinal) {
		return query()
			.leftOuterJoinFetch("indicador.acompanhamentosIndicador acompanhamentoIndicador")
			.entity(indicador)
			.where("acompanhamentoIndicador.dataInicial >= ?", dtLimiteInicial)
			.where("acompanhamentoIndicador.dataInicial <= ?", dtLimiteFinal)
			.orderBy("acompanhamentoIndicador.dataInicial")
			.unique();
	}
	
	public Indicador obtemColecaoAcompanhamento(Indicador indicador) {
		return query()
			.leftOuterJoinFetch("indicador.acompanhamentosIndicador acompanhamentoIndicador")
			.entity(indicador)
			.orderBy("acompanhamentoIndicador.dataInicial")
			.unique();
	}
	
	public int contaIndicadores(PlanoGestao pg){
		QueryBuilder<Long> q = new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(Indicador.class)
			.join("indicador.unidadeGerencial unidadeGerencial")
			.join("unidadeGerencial.planoGestao planoGestao")
			.where("planoGestao = ?", pg);
		return q.unique().intValue();
	}
	
	/**
	 * Atualiza o status do indicador
	 * 
	 * @author Rodrigo Alvarenga
	 * @param indicador
	 * @param status
	 */
	public void atualizaStatusIndicador(Indicador indicador, StatusIndicadorEnum status) {
		getJdbcTemplate().update("update indicador set status = ? where id = ?", new Object[]{status.ordinal(), indicador.getId()});		
	}

	/**
	 * Carrega informações para que seja feito o relatório de pendência de anomalia.
	 *
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Indicador> findForPendenciaAnomalia(PendenciaAnomaliaReportFiltro filtro) {
		return query()
				.select("indicador.nome, indicador.descricao, indicadorAcomp.precisao, indicadorAcomp.melhor, indicador.frequencia, indicador.tolerancia, " +
						"objetivoEstrategico.descricao, " +
						"unidadeGerencial.nome, unidadeGerencial.sigla, " +
						"acompanhamentosIndicador.valorReal, acompanhamentosIndicador.valorLimiteSuperior, acompanhamentosIndicador.valorLimiteInferior, acompanhamentosIndicador.naoaplicavel, " +
						"anomalia.id")
				.from(Indicador.class)
				.leftOuterJoin("indicador.acompanhamentosIndicador acompanhamentosIndicador")
				.leftOuterJoin("acompanhamentosIndicador.anomalia anomalia")
				.leftOuterJoin("acompanhamentosIndicador.indicador indicadorAcomp")
				.leftOuterJoin("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
				.leftOuterJoin("objetivoMapaEstrategico.perspectivaMapaEstrategico perspectivaMapaEstrategico")
				.leftOuterJoin("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
				.leftOuterJoin("indicador.unidadeGerencial unidadeGerencial")			
				.leftOuterJoin("unidadeGerencial.planoGestao planoGestao")
				.where("unidadeGerencial = ?", filtro.getUnidadeGerencial())
				.where("perspectivaMapaEstrategico = ?", filtro.getPerspectivaMapaEstrategico())
				.where("objetivoMapaEstrategico = ?", filtro.getObjetivoMapaEstrategico())
				.where("indicador.status <> ?", StatusIndicadorEnum.CANCELADO)
				.where("indicador.status <> ?", StatusIndicadorEnum.EM_CANCELAMENTO)
				.orderBy("objetivoEstrategico.descricao, indicador.descricao, acompanhamentosIndicador.dataInicial")
				.list();
	}

	public void atualizaIndicador(Indicador indicador) {
		getHibernateTemplate().bulkUpdate("update Indicador i set i.peso = ?, i.nome = ?, i.melhor = ? where i.id = ?", new Object[]{indicador.getPeso(), indicador.getNome(), indicador.getMelhor(), indicador.getId()});				
	}

	public void deleteWhereNotIn(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico, String listAndConcatenate) {
		getJdbcTemplate().execute("DELETE FROM INDICADOR WHERE INDICADOR.UNIDADEGERENCIAL_ID = " + unidadeGerencial.getId() + " AND INDICADOR.OBJETIVOMAPAESTRATEGICO_ID = " + objetivoMapaEstrategico.getId() + (!listAndConcatenate.equals("") ? " AND INDICADOR.ID NOT IN ("+listAndConcatenate+")" : ""));
	}
	
	/**
	 * Remove todos os indicadores de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		getJdbcTemplate().update("DELETE FROM INDICADOR WHERE INDICADOR.UNIDADEGERENCIAL_ID = ?", new Object[]{unidadeGerencial.getId()});		
	}	

	/**
	 * Carrega as informações de indicadores para o relatório.
	 * 
	 * @param planoGestao
	 * @param unidadeGerencial
	 * @param objetivoEstrategico
	 * @param orderBy
	 * @return
	 */
	public List<Indicador> findBy(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencial, ObjetivoEstrategico objetivoEstrategico, String orderBy) {
		return query()
			.select(
					"unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, " +
					"objetivoMapaEstrategico.id, objetivoEstrategico.descricao, " +
					"indicador.id, indicador.nome, indicador.melhor, indicador.precisao, indicador.tolerancia, indicador.frequencia, indicador.status, " +
					"unidadeMedida.sigla, " +
					"acompanhamentoIndicador.id, acompanhamentoIndicador.indice, acompanhamentoIndicador.dataInicial, acompanhamentoIndicador.valorReal, acompanhamentoIndicador.valorLimiteSuperior, acompanhamentoIndicador.valorLimiteInferior, acompanhamentoIndicador.naoaplicavel")
			.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
			.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
			.join("indicador.unidadeGerencial unidadeGerencial")
			.join("unidadeGerencial.planoGestao planoGestao")
			.leftOuterJoin("indicador.unidadeMedida unidadeMedida")
			.join("indicador.acompanhamentosIndicador acompanhamentoIndicador")
			.where("planoGestao = ?", planoGestao)
			.where("objetivoEstrategico = ?", objetivoEstrategico)
			.where("unidadeGerencial = ?", unidadeGerencial)
			.openParentheses()
				.where("indicador.status = ?", StatusIndicadorEnum.APROVADO)
				.or()
				.where("indicador.status = ?", StatusIndicadorEnum.EM_CANCELAMENTO)			
			.closeParentheses()
			.orderBy(orderBy)
			.list();
	}

	public Indicador loadForEmail(Indicador indicador) {
		return query()
					.select("indicador.nome, indicador.precisao, " +
							"unidadeMedida.sigla, " +
							"objetivoEstrategico.descricao, " +
							"ug.sigla, ug.nome, pg.anoExercicio")
					.leftOuterJoin("indicador.unidadeMedida unidadeMedida")
					.join("indicador.objetivoMapaEstrategico objetivoMapaEstrategico")
					.join("objetivoMapaEstrategico.objetivoEstrategico objetivoEstrategico")
					.join("indicador.unidadeGerencial ug")
					.join("ug.planoGestao pg")
					.where("indicador = ?", indicador)
					.unique();
	}

	/**
	 * Carrega os ids do indicador, da unidade gerencial e do ano da gestão.
	 * 
	 * @param indicador
	 * @return
	 */
	public Indicador loadWithUnidadePlanoGestao(Indicador indicador) {
		return query()
					.select("indicador.id, ug.id, pg.id")
					.join("indicador.unidadeGerencial ug")
					.join("ug.planoGestao pg")
					.where("indicador = ?", indicador)
					.unique();
	}	
}
