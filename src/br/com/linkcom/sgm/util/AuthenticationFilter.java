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
package br.com.linkcom.sgm.util;

import br.com.linkcom.neo.authorization.AuthenticationControlFilter;
import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.service.UsuarioService;

public class AuthenticationFilter extends AuthenticationControlFilter {
	
	@Override
	public void updatePasswordOnDataBase(User user, String hashSenha) {
		UsuarioService usuarioService = UsuarioService.getInstance();
		Usuario usuario = (Usuario) user;
		usuario.setSenha(hashSenha);
		usuarioService.updatePassword(usuario, false);			
	}
	
	@Override
	public boolean doLogin() {
		return true;
	}
}
