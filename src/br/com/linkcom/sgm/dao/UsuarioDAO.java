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

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.exception.NeoException;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.Arquivo;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.enumeration.FuncaoUGEnum;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.UsuarioFiltro;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

@DefaultOrderBy("usuario.nome")
public class UsuarioDAO extends GenericDAO<Usuario> {
	
	private ArquivoDAO arquivoDAO;
	
	public void setArquivoDAO(ArquivoDAO arquivoDAO) {
		this.arquivoDAO = arquivoDAO;
	}
	
	@Override
	public void updateListagemQuery(QueryBuilder<Usuario> query, FiltroListagem filtro) {
		UsuarioFiltro usuarioFiltro = (UsuarioFiltro)filtro;
		query
			.select("distinct usuario.id, usuario.nome, usuario.login, usuario.email, usuario.bloqueado")
			.leftOuterJoin("usuario.usuariosPapel usuariopapel")
			.leftOuterJoin("usuariopapel.papel papel")
			.leftOuterJoin("usuario.usuariosUnidadeGerencial usuarioUnidadeGerencial")
			.leftOuterJoin("usuarioUnidadeGerencial.unidadeGerencial unidadeGerencial")
			.whereLikeIgnoreAll("usuario.nome", usuarioFiltro.getNome())
			.whereLikeIgnoreAll("usuario.email", usuarioFiltro.getEmail())
			.whereLikeIgnoreAll("usuario.login", usuarioFiltro.getLogin())
			.where("papel = ?", usuarioFiltro.getPapel())
			.where("usuario.bloqueado = ?", usuarioFiltro.getBloqueado())
			.whereIn("unidadeGerencial.id", CollectionsUtil.listAndConcatenate(usuarioFiltro.getListaUnidadeGerencial(),"id",","))
			.orderBy("usuario.nome")
			.ignoreJoin("usuarioUnidadeGerencial", "unidadeGerencial", "usuariopapel", "papel");
	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Usuario> query) {
		query
			.leftOuterJoinFetch("usuario.foto foto")
			.leftOuterJoinFetch("usuario.usuariosUnidadeGerencial usuariosUnidadeGerencial")
			.leftOuterJoinFetch("usuariosUnidadeGerencial.unidadeGerencial unidadeGerencial")
			.leftOuterJoinFetch("unidadeGerencial.planoGestao planoGestao");
	}
	
	@Override
	public void updateSaveOrUpdate(final SaveOrUpdateStrategy save) {
		getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				
				save.saveOrUpdateManaged("usuariosPapel");
				
				arquivoDAO.saveFile(save.getEntity(), "foto", false);
				return null;
			}			
		});
	}	

	public Boolean verificaExisteLogin(String login) {
		Usuario usuario = obtemUsuarioLogin(login);
		return usuario == null? false : true;
	}
	
	private Usuario obtemUsuarioLogin(String login) {
		List<Usuario> usuarios = query()
			.select("usuario.id,usuario.login")
			.from(Usuario.class)
			.where("usuario.login = ?", login)
			.list();
	
			if (usuarios.size() > 1){
				throw new NeoException("Existe mais de um usuário para o " +
						"login informado!");
			}
		
		/*Se não existir retorna true*/
		return usuarios.size() == 0 ? null : usuarios.get(0);
	}
	
	@Override
	public void delete(final Usuario usuario) {
		getTransactionTemplate().execute(new TransactionCallback(){

		public Object doInTransaction(TransactionStatus arg0) {
				// Apaga a foto
				Usuario usuFoto = obtemFoto(usuario);
				Arquivo foto = usuFoto.getFoto();
				
				UsuarioDAO.super.delete(usuario);
				
				if(foto != null){
					getHibernateTemplate().delete(foto);
				}
				
				return null;
			}});
		
	}
	
	public Usuario obtemSenha(Usuario usuario) {
		return query()
			.select("usuario.id,usuario.senha")
			.from(Usuario.class)
			.where("usuario = ?", usuario)
			.unique();
	}
	
	public Usuario obtemLogin(Usuario usuario) {
		return query()
		.select("usuario.id,usuario.login")
		.from(Usuario.class)
		.where("usuario = ?", usuario)
		.unique();
	}
	
	public Usuario obtemFoto(Usuario usuario) {
		return query()
			.leftOuterJoinFetch("usuario.foto foto")
			.from(Usuario.class)
			.where("usuario = ?", usuario)
			.unique();
	}
	
	public Usuario obtemPapeis (Usuario usuario) {
		Usuario usuarioPapeis = query()
			.from(Usuario.class)
			.entity(usuario)
			.leftOuterJoinFetch("usuario.usuariosPapel as up")
			.leftOuterJoinFetch("up.papel as p")
			.unique();
		
		return usuarioPapeis;
	}
	
	/**
	 * Retorna uma lista com todos os usuários que possuem vínculo a alguma UG em um determinado plano de gestão.
	 * Se o parâmetro funcao for diferente de nulo, serão retornados somente os usuários que possuem a função especificada
	 * @author Rodrigo Alvarenga
	 * @param planoGestao
	 * @param funcao
	 */	
	public List<Usuario> findVinculadosByPlanoGestao(PlanoGestao planoGestao, FuncaoUGEnum funcao) {
		if (planoGestao == null)
			return new ArrayList<Usuario>();
		else {
			return
				query()
					.select("distinct usuario.id, usuario.nome, usuario.email")
					.join("usuario.usuariosUnidadeGerencial usuariosUnidadeGerencial")
					.join("usuariosUnidadeGerencial.unidadeGerencial unidadeGerencial")
					.join("unidadeGerencial.planoGestao planoGestao")
					.where("planoGestao = ?", planoGestao)
					.where("usuariosUnidadeGerencial.funcao = ?", funcao)
					.orderBy("usuario.nome")
					.list();			
		}
	}
	
	public List<Usuario> findAllNaoBloqueados() {
		return query()
			   .where("usuario.bloqueado = ?", Boolean.FALSE)
			   .orderBy("usuario.nome")
			   .list();
	}

	public void alterarSenha(Usuario usuario) {
		if (usuario ==  null) {
			throw new GeplanesException("Dados inválidos");
		}
		Usuario select = this.load(usuario);
			
		select.setSenha(usuario.getSenha());
		save(select).execute();
		getHibernateTemplate().flush();
	}

	public List<Usuario> findAdministradores(){
		return 
			query()
				.select("usuario.id, usuario.nome, usuario.email")
				.join("usuario.usuariosPapel usuariosPapel")
				.join("usuariosPapel.papel papel")
				.where("papel.administrador = ?", Boolean.TRUE)
				.list();
	}
	
	public List<Usuario> findNaoAdministradores(){
		return 
			query()
				.select("usuario.id, usuario.nome, usuario.email")
				.join("usuario.usuariosPapel usuariosPapel")
				.join("usuariosPapel.papel papel")
				.where("papel.administrador = ?", Boolean.FALSE)
				.orderBy("usuario.nome")
				.list();
	}

	public void updatePassword(Usuario usuario) {
		getJdbcTemplate().update("update usuario set senha = ? where id = ?", new Object[]{usuario.getSenha(), usuario.getId()});
	}	
}
 