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

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ItemAuditoriaInterna;
import br.com.linkcom.sgm.beans.Ocorrencia;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.controller.report.filtro.AnomaliaListagemReportFiltro;
import br.com.linkcom.sgm.controller.report.filtro.AnomaliaSinteticoPorStatusReportFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AnomaliaFiltro;
import br.com.linkcom.sgm.service.AnexoAnomaliaService;
import br.com.linkcom.sgm.service.CausaEfeitoService;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

public class AnomaliaDAO extends GenericDAO<Anomalia> {
	
	private CausaEfeitoService causaEfeitoService;
	private UnidadeGerencialDAO unidadeGerencialDAO;
	private AnexoAnomaliaService anexoAnomaliaService;
	
	public void setCausaEfeitoService(CausaEfeitoService causaEfeitoService) {this.causaEfeitoService = causaEfeitoService;}
	public void setUnidadeGerencialDAO(UnidadeGerencialDAO unidadeGerencialDAO) {this.unidadeGerencialDAO = unidadeGerencialDAO;}
	public void setAnexoAnomaliaService(AnexoAnomaliaService anexoAnomaliaService) {this.anexoAnomaliaService = anexoAnomaliaService;}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Anomalia> query, FiltroListagem filtro) {
		AnomaliaFiltro anomaliaFiltro = (AnomaliaFiltro) filtro;
		query
			.select("anomalia.id,anomalia.descricao, anomalia.dataAbertura, anomalia.sequencial, " +
					"anomalia.dataEncerramento, anomalia.dataDestravamento, anomalia.lembreteEnviado, anomalia.status, anomalia.statusTratamento, " +
					"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, " +
					"nivelHierarquicoRegistro.id, nivelHierarquicoRegistro.descricao, " +
					"ugRegistroPai.id, " +
					"ugResponsavel.id, ugResponsavel.nome, ugResponsavel.sigla, " +
					"nivelHierarquicoResponsavel.id, nivelHierarquicoResponsavel.descricao, " +
					"ugResponsavelPai.id, " +
					"planoGestaoOrigem.id, planoGestaoOrigem.anoExercicio")
			.leftOuterJoin("anomalia.ugRegistro ugRegistro")
			.leftOuterJoin("ugRegistro.nivelHierarquico nivelHierarquicoRegistro")
			.leftOuterJoin("anomalia.ugResponsavel ugResponsavel")
			.leftOuterJoin("ugResponsavel.nivelHierarquico nivelHierarquicoResponsavel")
			.leftOuterJoin("ugRegistro.planoGestao planoGestaoOrigem")
			.leftOuterJoin("ugResponsavel.planoGestao planoGestaoResponsavel")
			.leftOuterJoin("ugRegistro.subordinacao ugRegistroPai")
			.leftOuterJoin("ugResponsavel.subordinacao ugResponsavelPai")
			.whereLikeIgnoreAll("anomalia.descricao", anomaliaFiltro.getDescricao())
			.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(anomaliaFiltro.getListaUnidadeGerencialReg(),"id",","))
			.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(anomaliaFiltro.getListaUnidadeGerencialResp(),"id",","))
			.openParentheses()
				.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(anomaliaFiltro.getListaUnidadeGerencialDisponivel(),"id",","))
				.or()
				.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(anomaliaFiltro.getListaUnidadeGerencialDisponivel(),"id",","))
			.closeParentheses()
			.where("planoGestaoOrigem = ?" , anomaliaFiltro.getPlanoGestao())
			.where("planoGestaoResponsavel = ?" , anomaliaFiltro.getPlanoGestao())
			.where("anomalia.sequencial = ?" , anomaliaFiltro.getSequencial())
			.where("anomalia.status = ?", anomaliaFiltro.getStatus())
			.where("anomalia.origem = ?", anomaliaFiltro.getOrigem())
			.whereLikeIgnoreAll("anomalia.tipoAuditoria", anomaliaFiltro.getTipoAuditoria())
			.orderBy("anomalia.sequencial,ugRegistro.sigla,ugResponsavel.sigla");
	}
	@Override
	public Anomalia loadForEntrada(Anomalia bean) {
		return query()
			.select("anomalia.id,anomalia.observacoes,anomalia.verificacao,anomalia.ocorrencia,anomalia.padronizacao,anomalia.responsavel,anomalia.dataDestravamento,anomalia.dataSolicitacaoEncerramento,anomalia.lembreteEnviado,"+
					"anomalia.descricao, anomalia.contraMedidasImediatas, anomalia.conclusao,anomalia.classificacao,anomalia.status,anomalia.statusTratamento,"+
					"anomalia.dataAbertura, anomalia.dataEncerramento,anomalia.sequencial,anomalia.analiseCausas,anomalia.tipo,anomalia.local,anomalia.origem,"+
					"anomaliaPai.id, anomaliaPai.sequencial, anomalia.tipoAuditoria, " +					
					"ugResponsavel.id, ugResponsavel.nome, ugResponsavel.sigla, " +
					"nivelHierarquicoResponsavel.id, nivelHierarquicoResponsavel.descricao, " +
					"ugResponsavelPai.id, " +					
					"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, " +
					"nivelHierarquicoRegistro.id, nivelHierarquicoRegistro.descricao, " +
					"ugRegistroPai.id, " +
					"planoGestaoResponsavel.id, planoGestaoResponsavel.anoExercicio," +
					"planoGestaoOrigem.id, planoGestaoOrigem.anoExercicio, " +
					"ocorrencia.descricao,ocorrencia.id, " +
					"itemAuditoriaInterna.id")
			.from(Anomalia.class)
			.entity(bean)
			.leftOuterJoin("anomalia.ugResponsavel ugResponsavel")
			.leftOuterJoin("ugResponsavel.nivelHierarquico nivelHierarquicoResponsavel")
			.leftOuterJoin("anomalia.ugRegistro ugRegistro")
			.leftOuterJoin("ugRegistro.nivelHierarquico nivelHierarquicoRegistro")
			.leftOuterJoin("ugResponsavel.planoGestao planoGestaoResponsavel")
			.leftOuterJoin("ugRegistro.planoGestao planoGestaoOrigem")
			.leftOuterJoin("anomalia.ocorrencia ocorrencia")
			.leftOuterJoin("anomalia.itemAuditoriaInterna itemAuditoriaInterna")
			.leftOuterJoin("ugRegistro.subordinacao ugRegistroPai")
			.leftOuterJoin("ugResponsavel.subordinacao ugResponsavelPai")
			.leftOuterJoin("anomalia.subordinacao anomaliaPai")			
			.unique();
	}

	public List<Anomalia> findForReportAnomalia(UnidadeGerencial ugRegistro, UnidadeGerencial ugResponsavel, String descricao) {
		return query()
		.leftOuterJoinFetch("anomalia.ugRegistro ugRegistro")
		.leftOuterJoinFetch("anomalia.ugResponsavel ugResponsavel")
		.where("ugRegistro=?", ugRegistro)
		.where("ugResponsavel=?", ugResponsavel)
		.whereLikeIgnoreAll("descricao", descricao)
		.list();
	}
	
	/**
	 * Remove todas as anomalias cuja Unidade Gerencial seja responsável pelo registro ou tratamento.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial bean) {
		getJdbcTemplate().update("DELETE FROM ANOMALIA WHERE ANOMALIA.UGREGISTRO_ID = ?", new Object[]{bean.getId()});
		getJdbcTemplate().update("DELETE FROM ANOMALIA WHERE ANOMALIA.UGRESPONSAVEL_ID = ?", new Object[]{bean.getId()});
	}

	public List<Anomalia> findByIndicador(Indicador indicador) {
		return query()
		.select("acompanhamentoIndicador.anomalia")
		.from(AcompanhamentoIndicador.class, "acompanhamentoIndicador")
		.leftOuterJoin("acompanhamentoIndicador.indicador indicador")
		.leftOuterJoin("acompanhamentoIndicador.anomalia anomalia")
		.where("indicador=?", indicador)
		.list();
	}
	
	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial) {
		return unidadeGerencialDAO.getProximoSequencialAnomalia(unidadeGerencial);
	}
	
	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial, int offset) {
		return unidadeGerencialDAO.getProximoSequencialAnomalia(unidadeGerencial, offset);
	}
	
	/**
	 * Verifica se a ocorrência faz parte de alguma anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param ocorrencia
	 * @return boolean 
	 */
	public Boolean fazParteAnomalia(Ocorrencia ocorrencia){
		if(ocorrencia == null){
			throw new GeplanesException("A ocorrência não pode ser nula ao verificar se ela faz parte de uma anomalia.");
		}
		List<Anomalia> listaAnomalia =
			query()
			.where("anomalia.ocorrencia = ?", ocorrencia)
			.list();
		return listaAnomalia.size() > 0;
	}
	/**
	 * Obtem a ocorrencia de uma determinada anomalia.
	 * @author Matheus Melo 
	 * @param anomalia
	 * @return anomalia
	 */
	public Anomalia obtemOcorrencia(Anomalia anomalia) {
		return 
			query()
			.select("anomalia.ocorrencia,anomalia.id,ocorrencia.descricao")
			.leftOuterJoin("anomalia.ocorrencia ocorrencia")
			.where("anomalia = ?", anomalia)
			.unique();
	}
	/**
	 * Procurar anomalia pela ocorrencia
	 * @author Matheus Melo Gonçalves
	 * @param ocorrencia
	 * @return Anomalia 
	 */
	public Anomalia findByOcorrencia(Ocorrencia ocorrencia){
		return 
			query()
			.where("anomalia.ocorrencia = ?",ocorrencia)
			.unique();
	}
	
	/**
	 * Verifica se existe alguma anomalia vinculada ao registro de auditoria interna passado como parâmetro.
	 * 
	 * @param itemAuditoriaInterna
	 * @return boolean 
	 */
	public Boolean existeAnomaliaVinculada(ItemAuditoriaInterna itemAuditoriaInterna) {
		if (itemAuditoriaInterna == null) {
			throw new GeplanesException("O registro não pode ser nulo em existeAnomaliaVinculada.");
		}
		List<Anomalia> listaAnomalia =
			query()
				.where("anomalia.itemAuditoriaInterna = ?", itemAuditoriaInterna)
				.list();
		return listaAnomalia.size() > 0;
	}
	
	/**
	 * Carrega o registro de anomalia com a informação da auditoria interna à
	 * qual está vinculada, caso exista.
	 * 
	 * @param anomalia
	 * @return anomalia
	 */
	public Anomalia loadWithItemAuditoriaInterna(Anomalia anomalia) {
		return 
			query()
			.select("anomalia.id, " +
					"itemAuditoriaInterna.id, " +
					"ugExterna.id")
			.leftOuterJoin("anomalia.itemAuditoriaInterna itemAuditoriaInterna")
			.leftOuterJoin("itemAuditoriaInterna.ugExterna ugExterna")
			.where("anomalia = ?", anomalia)
			.unique();
	}
	
	/**
	 * Retorna a anomalia vinculada à auditoria interna passada como parâmetro.
	 * 
	 * @param itemAuditoriaInterna
	 * @return Anomalia 
	 */
	public Anomalia loadByItemAuditoriaInterna(ItemAuditoriaInterna itemAuditoriaInterna) {
		return 
			query()
			.where("anomalia.itemAuditoriaInterna = ?", itemAuditoriaInterna)
			.unique();
	}	
	
	@Override
	public void saveOrUpdate(final Anomalia bean){
		getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus arg0){
				AnomaliaDAO.super.saveOrUpdate(bean);
				if(bean.getEfeito() != null){
					causaEfeitoService.salvarCausaEfeito(bean);
				}
				
				// Exclui os anexos da anomalia
				anexoAnomaliaService.excluiAnexoAnomaliaDetalhe(bean);
				
				// Salva os anexos da anomalia
				anexoAnomaliaService.salvaAnexoAnomaliaDetalhe(bean);
				
				return null;
			}});
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("planosAcao");
	}
	
	/**
	 * Carrega as informações para o relatório de listagem de anomalias.
	 * 
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	public List<Anomalia> findForReportListagemAnomalia(AnomaliaListagemReportFiltro filtro) {
		QueryBuilder<Anomalia> query = query()
				.select("anomalia.id,anomalia.descricao, anomalia.dataAbertura, anomalia.sequencial, anomalia.status, anomalia.statusTratamento, " +
						"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, anomalia.dataEncerramento, " +
						"ugResponsavel.id, ugResponsavel.nome, ugResponsavel.sigla")
				.leftOuterJoin("anomalia.ugRegistro ugRegistro")
				.leftOuterJoin("anomalia.ugResponsavel ugResponsavel")
				.leftOuterJoin("ugRegistro.planoGestao planoGestaoOrigem")
				.leftOuterJoin("ugResponsavel.planoGestao planoGestaoResponsavel")
				.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialReg(),"id",","))
				.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialResp(),"id",","))
				.openParentheses()
					.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
					.or()
					.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialDisponivel(),"id",","))
				.closeParentheses()				
				.where("planoGestaoOrigem = ?" , filtro.getPlanoGestao())
				.where("planoGestaoResponsavel = ?" , filtro.getPlanoGestao())
				.where("anomalia.status = ?", filtro.getStatus())
				.where("anomalia.statusTratamento = ?", filtro.getStatusTratamento())
				.orderBy("anomalia.sequencial,ugRegistro.sigla,ugResponsavel.sigla");
		
		return query.list();
	}
	
	/**
	 * Carrega as informações para o relatório de anomalias sintético por status.
	 * 
	 * @param filtro
	 * @return
	 */
	public Integer findForAnomaliaSinteticoPorStatusReport(AnomaliaSinteticoPorStatusReportFiltro filtro, StatusAnomaliaEnum status) {
		
		QueryBuilder<Long> q = new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(Anomalia.class)
			.leftOuterJoin("anomalia.ugRegistro ugRegistro")
			.leftOuterJoin("anomalia.ugResponsavel ugResponsavel")
			.leftOuterJoin("ugRegistro.planoGestao planoGestaoOrigem")
			.leftOuterJoin("ugResponsavel.planoGestao planoGestaoResponsavel")
			.whereIn("ugRegistro.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialReg(),"id",","))
			.whereIn("ugResponsavel.id", CollectionsUtil.listAndConcatenate(filtro.getListaUnidadeGerencialResp(),"id",","))
			.where("planoGestaoOrigem = ?" , filtro.getPlanoGestao())
			.where("planoGestaoResponsavel = ?" , filtro.getPlanoGestao())
			.where("anomalia.status = ?", status);
		
		return q.unique().intValue();
	}
	
	/**
	 * Retorna as anomalias não encerradas de um determinado ano de gestão.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<Anomalia> findAnomaliasNaoEncerradas(PlanoGestao planoGestao) {
		return query()
			.select("anomalia.id, anomalia.descricao, anomalia.dataAbertura, anomalia.lembreteEnviado, anomalia.analiseCausas, "+
					"ugResponsavel.id, ugResponsavel.nome, ugResponsavel.sigla, " +
					"nivelHierarquicoResponsavel.id, nivelHierarquicoResponsavel.descricao, " +
					"ugRegistro.id, ugRegistro.nome, ugRegistro.sigla, " +
					"nivelHierarquicoRegistro.id, nivelHierarquicoRegistro.descricao, " +					
					"planoAcao.id, planoAcao.texto, planoAcao.textoComo, planoAcao.textoPorque, planoAcao.textoQuem, planoAcao.dtPlano, planoAcao.status")
			.from(Anomalia.class)
			.leftOuterJoin("anomalia.ugResponsavel ugResponsavel")
			.leftOuterJoin("ugResponsavel.nivelHierarquico nivelHierarquicoResponsavel")
			.leftOuterJoin("anomalia.ugRegistro ugRegistro")
			.leftOuterJoin("ugRegistro.nivelHierarquico nivelHierarquicoRegistro")			
			.leftOuterJoin("ugRegistro.planoGestao planoGestaoRegistro")
			.leftOuterJoin("anomalia.planosAcao planoAcao")
			.where("planoGestaoRegistro = ?", planoGestao)
			.where("anomalia.dataEncerramento is NULL")
			.list();		
	}
	
	public void atualizaEnvioLembreteTratamentoAnomalia(Anomalia anomalia) {
		getHibernateTemplate().bulkUpdate("update Anomalia a set a.lembreteEnviado = ? where a.id = ?", new Object[]{Boolean.TRUE, anomalia.getId()});		
	}
	
	public void atualizaStatusAnomalia(Anomalia anomalia, StatusAnomaliaEnum status) {
		getHibernateTemplate().bulkUpdate("update Anomalia a set a.status = ? where a.id = ?", new Object[]{status, anomalia.getId()});		
	}
	
	public List<Anomalia> findForAtualizacaoStatus() {
		return query()
			.select("anomalia.id, anomalia.dataDestravamento, anomalia.status, anomalia.dataAbertura, anomalia.dataSolicitacaoEncerramento, anomalia.dataEncerramento, anomalia.analiseCausas, " +
					"planoAcao.id, planoAcao.texto, planoAcao.textoComo, planoAcao.textoPorque, planoAcao.textoQuem, planoAcao.dtPlano, planoAcao.status")
			.leftOuterJoin("anomalia.planosAcao planoAcao")					
			.list();		
	}
	
	public Anomalia loadWithParent(Anomalia anomalia) {
		return query()
			.select("anomalia.id, anomaliaPai.id")
			.join("anomalia.subordinacao anomaliaPai")
			.where("anomalia = ?", anomalia)
			.unique();		
	}
	
	public List<Anomalia> findIdByUGRegistro(UnidadeGerencial ugRegistro) {
		return 
			query()
				.select("anomalia.id")
				.join("anomalia.ugRegistro ugRegistro")
				.where("ugRegistro = ?", ugRegistro)
				.list();
	}	
	
	public List<Anomalia> findIdByUGResponsavel(UnidadeGerencial ugResponsavel) {
		return 
			query()
				.select("anomalia.id")
				.join("anomalia.ugResponsavel ugResponsavel")
				.where("ugResponsavel = ?", ugResponsavel)
				.list();
	}
	
	/**
	 * Retorna as anomalias NÃO ENCERRADAS cuja unidade gerencial responsável pelo tratamento
	 * seja a passada como parâmetro. 
	 * Serão preenchidos somente os campos necessários para a verificação do tratamento da anomalia.
	 * 
	 * @param ugResponsavel
	 * @return
	 */
	public List<Anomalia> findByUGResponsavelForControlePendencia(UnidadeGerencial ugResponsavel) {
		return query()
			.select("anomalia.id, anomalia.descricao, anomalia.analiseCausas, "+
					"planoAcao.id, planoAcao.texto, planoAcao.textoComo, planoAcao.textoPorque, planoAcao.textoQuem, planoAcao.dtPlano, planoAcao.status")
			.join("anomalia.ugResponsavel ugResponsavel")
			.leftOuterJoin("anomalia.planosAcao planoAcao")
			.where("ugResponsavel = ?", ugResponsavel)
			.where("anomalia.dataEncerramento is NULL")
			.list();		
	}
	
	public List<Anomalia> findSubordinadas(Anomalia anomaliaPai) {
		return 
			query()
				.select("anomalia.id")
				.join("anomalia.subordinacao anomaliaPai")
				.where("anomaliaPai = ?", anomaliaPai)
				.list();		
	}	
}
