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

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.filtro.UnidadeGerencialFiltro;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.GeplanesUtils;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

@DefaultOrderBy("unidadeGerencial.sigla")
public class UnidadeGerencialDAO extends GenericDAO<UnidadeGerencial> {
	
	private UsuarioService usuarioService;

	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	
	@Override
	public void updateListagemQuery(QueryBuilder<UnidadeGerencial> query, FiltroListagem _filtro) {
		UnidadeGerencialFiltro filtro = (UnidadeGerencialFiltro) _filtro;
		List<UnidadeGerencial> ugUsuarioFiltro = null;
		
		if (!usuarioService.isUsuarioLogadoAdmin()) {
			Usuario usuario = (Usuario) Neo.getRequestContext().getUser();			
			ugUsuarioFiltro = loadByUsuarioPlanoGestao(usuario, filtro.getPlanoGestao());
			query.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(ugUsuarioFiltro,"id",","));
		}
		
		query
			.select("unidadeGerencial.id,unidadeGerencial.nome,unidadeGerencial.sigla," +
					"planoGestao.id,planoGestao.anoExercicio," +
					"subordinacao.id,subordinacao.sigla")
			.leftOuterJoin("unidadeGerencial.planoGestao planoGestao")					
			.leftOuterJoin("unidadeGerencial.subordinacao subordinacao")
			.whereLikeIgnoreAll("unidadeGerencial.nome",filtro.getNome())
			.whereLikeIgnoreAll("unidadeGerencial.sigla",filtro.getSigla())
			.where("unidadeGerencial.nivelHierarquico = ?",filtro.getNivelHierarquico())
			.where("planoGestao = ?",filtro.getPlanoGestao())
			.where("subordinacao = ?", filtro.getSubordinacao())
			.orderBy("unidadeGerencial.nome");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("usuariosUnidadeGerencial");
	}
	
	public List<UnidadeGerencial> find(UnidadeGerencial pai, int niveis, PlanoGestao planoGestao){
		QueryBuilder<UnidadeGerencial> query = query();
		query.select("unidadeGerencial.id,unidadeGerencial.nome,unidadeGerencial.sigla, " +
				"subordinacao.id, subordinacao.nome, subordinacao.sigla");
		query.leftOuterJoin("unidadeGerencial.subordinacao subordinacao");
		query.leftOuterJoin("unidadeGerencial.planoGestao planoGestao");		
		query.where("planoGestao = ?", planoGestao);
		if(pai == null){
			query.where("subordinacao.id is null");
		} else {
			query.where("subordinacao = ? ", pai);
		}
		query.orderBy("unidadeGerencial.sigla");
		List<UnidadeGerencial> list = query.list();
		
		if(niveis > 1 || niveis < 0){
			for (UnidadeGerencial unidade : list) {
				unidade.setFilhos(find(unidade, niveis - 1, planoGestao));
				unidade.setNumeroFilhos(unidade.getFilhos().size());
			}
		} else if(niveis == 1){
			//contar os filhos
			for (UnidadeGerencial unidade : list) {
				int numeroFilhos = getJdbcTemplate().queryForInt("select count(*) from unidadegerencial where subordinacao_id = ?", new Object[]{unidade.getId()});
				unidade.setNumeroFilhos(numeroFilhos);
			}
		}
		return list;
	}
	
	public UnidadeGerencial obtemSigla(UnidadeGerencial unidadeGerencial) {
		return query()
			.select("unidadeGerencial.id,unidadeGerencial.sigla")
			.from(UnidadeGerencial.class)
			.where("unidadeGerencial = ?", unidadeGerencial)
			.unique();
	}

	public void loadSiglaNome(UnidadeGerencial unidade) {
		UnidadeGerencial unidadeGerencial2 = query().select("unidadeGerencial.nome, unidadeGerencial.sigla").entity(unidade).unique();
		if (unidadeGerencial2 != null) {
			unidade.setSigla(unidadeGerencial2.getSigla());
			unidade.setNome(unidadeGerencial2.getNome());
		}
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<UnidadeGerencial> query) {
		query
			.select("unidadeGerencial.id,unidadeGerencial.nome,unidadeGerencial.sigla,unidadeGerencial.areaQualidade,unidadeGerencial.areaAuditoriaInterna,unidadeGerencial.seqAnomalia, unidadeGerencial.seqAcaoPreventiva, unidadeGerencial.seqOcorrencia, " +
					"unidadeGerencial.permitirMapaNegocio, unidadeGerencial.permitirMapaEstrategico, unidadeGerencial.permitirMapaCompetencia, unidadeGerencial.permitirMatrizFcs, " +
					"nivelHierarquico.id, nivelHierarquico.descricao, " +
					"planoGestao.id,planoGestao.anoExercicio," +
					"subordinacao.id,subordinacao.sigla," +
					"usuariosUnidadeGerencial.id, usuariosUnidadeGerencial.funcao, " +
					"usuario.id, usuario.nome")
			.leftOuterJoin("unidadeGerencial.nivelHierarquico nivelHierarquico")
			.leftOuterJoin("unidadeGerencial.usuariosUnidadeGerencial usuariosUnidadeGerencial")
			.leftOuterJoin("usuariosUnidadeGerencial.usuario usuario")
			.join("unidadeGerencial.planoGestao planoGestao")
			.leftOuterJoin("unidadeGerencial.subordinacao subordinacao");
	}

	public List<UnidadeGerencial> findFilhas(UnidadeGerencial bean) {
		return query()
			.select("unidadeGerencial.id, unidadeGerencial.nome, unidadeGerencial.sigla, unidadeGerencial.areaQualidade, unidadeGerencial.areaAuditoriaInterna, unidadeGerencial.nivelNum, " +
					"unidadeGerencial.permitirMapaNegocio, unidadeGerencial.permitirMapaEstrategico, unidadeGerencial.permitirMapaCompetencia, unidadeGerencial.permitirMatrizFcs, " +
					"subordinacao.id, subordinacao.nome, subordinacao.sigla, subordinacao.areaQualidade, subordinacao.areaAuditoriaInterna, subordinacao.nivelNum, " +
					"nivelHierarquico.id, nivelHierarquico.descricao")
			.join("unidadeGerencial.subordinacao subordinacao")
			.leftOuterJoin("unidadeGerencial.nivelHierarquico nivelHierarquico")
			.where("subordinacao = ?", bean)
			.list();	
	}
	
	public List<UnidadeGerencial> loadUGWithUsuarios(PlanoGestao planoGestao, List<Integer> listaIdUG) {
		
		// Lista de unidades gerenciais e seus usuários vinculados		
		return query()
			.select("unidadeGerencial.id,unidadeGerencial.sigla,unidadeGerencial.nome,unidadeGerencial.nivelNum," +
					"subordinacao.id,subordinacao.sigla,subordinacao.nome," +
					"usuariosUnidadeGerencial.id,usuariosUnidadeGerencial.funcao," +
					"usuario.id,usuario.nome")
			.leftOuterJoin("unidadeGerencial.subordinacao subordinacao")
			.leftOuterJoin("unidadeGerencial.usuariosUnidadeGerencial usuariosUnidadeGerencial")
			.leftOuterJoin("usuariosUnidadeGerencial.usuario usuario")
			.leftOuterJoin("unidadeGerencial.planoGestao planoGestao")
			.where("planoGestao = ?", planoGestao)
			.whereIn("unidadeGerencial.id", GeplanesUtils.concatenate(listaIdUG,","))
			.list();		
	}
	
	public int contaUG(PlanoGestao planoGestao){
		QueryBuilder<Long> q = new QueryBuilder<Long>(getHibernateTemplate())
			.select("count(*)")
			.from(UnidadeGerencial.class)
			.join("unidadeGerencial.planoGestao planoGestao")
			.where("planoGestao = ?", planoGestao);			
		return q.unique().intValue();
	}
	
	public List<UnidadeGerencial> findByPlanoGestao(PlanoGestao bean, String orderBy) {
		return 
			query()
				.where("unidadeGerencial.planoGestao=?", bean)
				.orderBy(orderBy)
				.list();
	}
	
	public List<UnidadeGerencial> findWithSiglaNomeByPlanoGestao(PlanoGestao bean) {
		return query()
			.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, unidadeGerencial.areaQualidade, unidadeGerencial.areaAuditoriaInterna")
			.where("unidadeGerencial.planoGestao=?", bean)
			.orderBy("unidadeGerencial.sigla, unidadeGerencial.nome")
			.list();
	}
	
	public List<UnidadeGerencial> findWithSiglaNomeSubordinacaoByPlanoGestao(PlanoGestao bean) {
		return query()
		.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, unidadeGerencial.areaQualidade, unidadeGerencial.areaAuditoriaInterna, " +
				"unidadeGerencialPai.id, unidadeGerencialPai.nome, unidadeGerencialPai.sigla")
		.leftOuterJoin("unidadeGerencial.subordinacao unidadeGerencialPai")		
		.where("unidadeGerencial.planoGestao=?", bean)
		.orderBy("unidadeGerencial.sigla, unidadeGerencial.nome")
		.list();
	}	
	
	public List<UnidadeGerencial> loadByUsuarioPlanoGestao(Usuario usuario, PlanoGestao planoGestao) {
		return query()
					.select("unidadeGerencial.id,unidadeGerencial.sigla,unidadeGerencial.nome,unidadeGerencial.areaQualidade, unidadeGerencial.areaAuditoriaInterna")
					.leftOuterJoin("unidadeGerencial.usuariosUnidadeGerencial usuariosUnidadeGerencial")
					.leftOuterJoin("usuariosUnidadeGerencial.usuario usuario")
					.join("unidadeGerencial.planoGestao planoGestao")
					.where("usuario = ?", usuario)
					.where("planoGestao = ?", planoGestao)
					.list();
	}
	
	public UnidadeGerencial loadByNomePlanoGestao(String nome, PlanoGestao bean) {
		return query()
			.where("unidadeGerencial.nome = ?", nome)
			.where("unidadeGerencial.planoGestao=?", bean)
			.unique();
	}
	
	/**
	 * Retorna uma lista contendo somente os nodos raiz de um determinado plano de gestão
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @return lista de unidades gerenciais raiz
	 */	
	public List<UnidadeGerencial> findNodosRaiz(PlanoGestao planoGestao) {
		if (planoGestao == null || planoGestao.getId() == null) {
			return new ArrayList<UnidadeGerencial>();
		}
		return 
			query()
				.leftOuterJoin("unidadeGerencial.subordinacao subordinacao")
				.where("unidadeGerencial.planoGestao = ?", planoGestao)
				.where("subordinacao.id is null")
				.orderBy("unidadeGerencial.sigla, unidadeGerencial.nome")
				.list();
	}
	/**
	 * Retorna o pai de uma determinada Unidade Gerencial.
	 * @author Matheus Melo Gonçalves
	 * @param unidadeGerencial filha.
	 * @return unidadeGerencial Pai
	 */	
	public UnidadeGerencial getPai(UnidadeGerencial unidadeGerencial) {
		if (unidadeGerencial.getSubordinacao() != null) {
			return query()
				.select("unidadeGerencial.id,unidadeGerencial.nome, unidadeGerencial.sigla, unidadeGerencial.nivelNum, " +
						"unidadeGerencialPai.id, unidadeGerencialPai.nome, unidadeGerencialPai.sigla")
				.leftOuterJoin("unidadeGerencial.subordinacao unidadeGerencialPai")			
				.where("unidadeGerencial = ?", unidadeGerencial.getSubordinacao())
				.unique();
		}
		return null;
	}

	/**
	 * Retorna o Plano de Gestao de uma determinada UG.
	 * @author Matheus Melo Gonçalves
	 * @param unidadeGerencial.
	 * @return Plano de Gestao.
	 */	
	public UnidadeGerencial obtemPlanoGestao(UnidadeGerencial unidadeGerencial) {
		return
			query()
			.select("unidadeGerencial.id,unidadeGerencial.planoGestao")
			.join("unidadeGerencial.planoGestao planoGestao")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.unique();
	}
	
	/**
	 * Carrega o nome, a descrição e o ano de gestão de uma UG
	 * @param unidadeGerencial
	 * @return
	 */	
	public UnidadeGerencial loadWithPlanoGestao(UnidadeGerencial unidadeGerencial) {
		return query()
			.select("unidadeGerencial.id,unidadeGerencial.sigla,unidadeGerencial.nome," +
					"unidadeGerencial.permitirMapaNegocio, unidadeGerencial.permitirMapaEstrategico, unidadeGerencial.permitirMapaCompetencia, unidadeGerencial.permitirMatrizFcs, " +
					"planoGestao.id, planoGestao.anoExercicio")
			.from(UnidadeGerencial.class)
			.join("unidadeGerencial.planoGestao planoGestao")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.unique();
	}
	
	/**
	 * Carrega o nome, a descrição e o ano de gestão de uma UG
	 * @param indicador
	 * @return
	 */	
	public UnidadeGerencial loadWithPlanoGestaoByIndicador(Indicador indicador) {
		return query()
			.select("unidadeGerencial.id,unidadeGerencial.sigla,unidadeGerencial.nome," +
					"planoGestao.id, planoGestao.anoExercicio")
			.from(UnidadeGerencial.class)
			.join("unidadeGerencial.planoGestao planoGestao")
			.join("unidadeGerencial.listaIndicador indicador")
			.where("indicador = ?", indicador)
			.unique();
	}	
	
	/**
	 * Carrega a UG com o mapa de competências preenchido.
	 *
	 * @param unidadeGerencial
	 * @return
	 * @author Rodrigo Freitas
	 */
	public UnidadeGerencial loadWithMapaCompetencia(UnidadeGerencial unidadeGerencial) {
		return query()
					.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, unidadeGerencial.permitirMapaCompetencia, " +
							"mapaCompetencia.id, " +
							"planoGestao.id, planoGestao.anoExercicio")
					.leftOuterJoin("unidadeGerencial.mapaCompetencia mapaCompetencia")	
					.join("unidadeGerencial.planoGestao planoGestao")
					.where("unidadeGerencial = ?", unidadeGerencial)
					.unique();
	}
	
	public List<UnidadeGerencial> findAutocomplete(String q, PlanoGestao planoGestao) {
		return query()
					.select("unidadeGerencial.id, unidadeGerencial.sigla")
					.join("unidadeGerencial.planoGestao planoGestao")
					.where("planoGestao = ?",planoGestao)
					.whereLikeIgnoreAll("unidadeGerencial.sigla", q)
					.list();
	}

	public UnidadeGerencial loadWithSiglaNomeAno(UnidadeGerencial unidadeGerencial) {
		return query()
		.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, planoGestao.id, planoGestao.anoExercicio")
		.join("unidadeGerencial.planoGestao planoGestao")
		.where("unidadeGerencial = ?", unidadeGerencial)
		.unique();
	}
	
	public UnidadeGerencial loadWithSiglaNomeAnoSubordinacao(UnidadeGerencial unidadeGerencial) {
		return query()
					.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome, " +
							"subordinacao.id, subordinacao.sigla, subordinacao.nome, " +
							"planoGestao.id, planoGestao.anoExercicio")
					.leftOuterJoin("unidadeGerencial.subordinacao subordinacao")
					.join("unidadeGerencial.planoGestao planoGestao")
					.where("unidadeGerencial = ?", unidadeGerencial)
					.unique();
	}
	
	public int getProximoSequencialAnomalia(UnidadeGerencial unidadeGerencial) {
		return getProximoSequencialAnomalia(unidadeGerencial, 1);
	}

	public int getProximoSequencialAnomalia(UnidadeGerencial unidadeGerencial, int offset) {
		
		// Busca o próximo sequencial.
		QueryBuilder<Integer> q = new QueryBuilder<Integer>(getHibernateTemplate())
		.select("seqAnomalia")
		.from(UnidadeGerencial.class)
		.entity(unidadeGerencial);
		
		Integer seq = q.unique();
		if (seq == null) {
			seq = 1;
		}
		
		// Atualiza o valor no banco de dados.
		getJdbcTemplate().update("update unidadeGerencial set seqAnomalia = ? where id = ?", new Object[]{seq + offset, unidadeGerencial.getId()});
		
		return seq;
	}
	
	public int getProximoSequencialAcaoPreventiva(UnidadeGerencial unidadeGerencial) {
		// Busca o próximo sequencial.
		QueryBuilder<Integer> q = new QueryBuilder<Integer>(getHibernateTemplate())
		.select("seqAcaoPreventiva")
		.from(UnidadeGerencial.class)
		.entity(unidadeGerencial);
		
		Integer seq = q.unique();
		if (seq == null) {
			seq = 1;
		}
		
		// Atualiza o valor no banco de dados.
		getJdbcTemplate().update("update unidadeGerencial set seqAcaoPreventiva = " + (seq + 1) + " where id = " + unidadeGerencial.getId());
		
		
		return seq;
	}
	
	public int getProximoSequencialOcorrencia(UnidadeGerencial unidadeGerencial) {
		// Busca o próximo sequencial.
		QueryBuilder<Integer> q = new QueryBuilder<Integer>(getHibernateTemplate())
			.select("seqOcorrencia")
			.from(UnidadeGerencial.class)
			.entity(unidadeGerencial);
		
		Integer seq = q.unique();
		if (seq == null) {
			seq = 1;
		}
		
		// Atualiza o valor no banco de dados.
		getJdbcTemplate().update("update unidadeGerencial set seqOcorrencia = " + (seq + 1) + " where id = " + unidadeGerencial.getId());
		
		
		return seq;
	}	
	
	/**
	 * Retorna as UGs da Área de Qualidade.
	 * @param planoGestao
	 * @return
	 */
	public List<UnidadeGerencial> findUGQualidade(PlanoGestao planoGestao) {
		return query()
				.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome")
				.join("unidadeGerencial.planoGestao planoGestao")
				.where("planoGestao = ?",planoGestao)
				.where("unidadeGerencial.areaQualidade = ?", Boolean.TRUE)
				.list();
	}
	
	public UnidadeGerencial loadWithSiglaNomeSubordinacao(UnidadeGerencial unidadeGerencial) {
		return query()
			.select("unidadeGerencial.id,unidadeGerencial.nome, unidadeGerencial.sigla, " +
					"unidadeGerencialPai.id, unidadeGerencialPai.nome, unidadeGerencialPai.sigla")
			.leftOuterJoin("unidadeGerencial.subordinacao unidadeGerencialPai")
			.entity(unidadeGerencial)
			.unique();
	}	
	
	/**
	 * Carrega a unidadeGerencial com o id do mapaEstrategico preenchido também.
	 *
	 * @param unidadeGerencial
	 * @return
	 * @author Rodrigo Freitas
	 */
	public UnidadeGerencial loadWithMapaEstrategico(UnidadeGerencial unidadeGerencial) {
		return query()
			.select("unidadeGerencial.id, unidadeGerencial.permitirMapaEstrategico, " +
					"mapaEstrategico.id, mapaEstrategico.visao, " +
					"planoGestao.id")
			.from(UnidadeGerencial.class)
			.leftOuterJoin("unidadeGerencial.mapaEstrategico mapaEstrategico")
			.leftOuterJoin("unidadeGerencial.planoGestao planoGestao")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.unique();
	}
	
	/**
	 * Atualiza o valor do campo <code>nivelnum</code> de cada unidade gerencial
	 * presente na lista passada como parâmetro.
	 * 
	 * @param listaUnidadeGerencial
	 * @return
	 */
	public void atualizaUnidadeGerencialNivelNum(List<UnidadeGerencial> listaUnidadeGerencial) {
		if (listaUnidadeGerencial != null && !listaUnidadeGerencial.isEmpty()) {
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencial) {
				// Atualiza o valor no banco de dados.
				getJdbcTemplate().update("update unidadeGerencial set nivelnum = " + unidadeGerencial.getNivelNum() + " where id = " + unidadeGerencial.getId());
			}
		}
	}
	
	/**
	 * Retorna as UGs da Área de Auditoria Interna de um determinado Ano da Gestão.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<UnidadeGerencial> findUGAuditoriaInterna(PlanoGestao planoGestao) {
		return 
			query()
				.select("unidadeGerencial.id, unidadeGerencial.sigla, unidadeGerencial.nome")
				.join("unidadeGerencial.planoGestao planoGestao")
				.where("planoGestao = ?",planoGestao)
				.where("unidadeGerencial.areaAuditoriaInterna = ?", Boolean.TRUE)
				.list();
	}	
}