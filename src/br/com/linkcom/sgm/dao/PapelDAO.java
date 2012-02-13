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
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.sgm.beans.Papel;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioPapel;

@DefaultOrderBy("papel.nome")
public class PapelDAO extends GenericDAO<Papel> {

	/**
	 * @author Rodrigo Duarte
	 * @param usuario
	 * @return Lista dos papéis do usuario
	 */
	public List<Papel> findByUsuario(Usuario usuario) {		
		return query()
			.select("papel")
			.from(UsuarioPapel.class, "usuariopapel")		
			.join("usuariopapel.usuario usuario")	
			.join("usuariopapel.papel papel")
			.where("usuario=?", usuario)
			.list();
	}

	@Override
	public void updateListagemQuery(QueryBuilder<Papel> query, FiltroListagem filtro) {

	}

	@Override
	public void updateEntradaQuery(QueryBuilder<Papel> query) {

	}

	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
	}
	
	/**
	 * Retorna uma lista com os níveis de acesso que não são administradores do sistema.
	 * @return
	 */
	public List<Papel> findNaoAdministradores() {
		return query()
			.openParentheses()
				.where("administrador IS NULL")
				.or()
				.where("administrador = ?", Boolean.FALSE)
			.closeParentheses()
			.list();		
	}
	

	/* singleton */
	private static PapelDAO instance;
	public static PapelDAO getInstance() {
		if(instance == null){
			instance = Neo.getObject(PapelDAO.class);
		}
		return instance;
	}

}
