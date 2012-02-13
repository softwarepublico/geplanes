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

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.FuncaoUGEnum;


public class UsuarioUnidadeGerencialDAO extends GenericDAO<UsuarioUnidadeGerencial> {
	
	/**
	 * Retorna uma lista com todos os usuários vinculados a uma unidade gerencial
	 * @author Rodrigo Alvarenga
	 * @param unidadeGerencial
	 * @param funcao
	 * @return lista de usuários vinculados à unidade gerencial
	 */
	public List<UsuarioUnidadeGerencial> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial, FuncaoUGEnum funcao) {
		if (unidadeGerencial == null) {
			return new ArrayList<UsuarioUnidadeGerencial>();
		}
		return query()
			.select("usuarioUnidadeGerencial.id, usuarioUnidadeGerencial.funcao, " +
					"usuario.id, usuario.nome, usuario.email, " +
					"unidadeGerencial.id, unidadeGerencial.nome, unidadeGerencial.sigla, unidadeGerencial.areaQualidade")
			.from(UsuarioUnidadeGerencial.class)
			.join("usuarioUnidadeGerencial.usuario usuario")
			.join("usuarioUnidadeGerencial.unidadeGerencial unidadeGerencial")
			.where("unidadeGerencial = ?", unidadeGerencial)
			.where("usuarioUnidadeGerencial.funcao = ?", funcao)			
			.orderBy("usuario.nome")
			.list();
	}
}
