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
package br.com.linkcom.sgm.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.Usuario;


public class AlterarSenhaFiltro {
	String senha,novaSenha,repetirNovaSenha;
	Usuario listaUsuarios;
	
	@Required
	@MaxLength(20)
	@DisplayName("Sua senha")
	public String getSenha() {
		return senha;
	}
	@Required
	@MaxLength(20)
	@DisplayName("Nova senha")
	public String getNovaSenha() {
		return novaSenha;
	}
	@Required
	@MaxLength(20)
	@DisplayName("Repetir nova senha")
	public String getRepetirNovaSenha() {
		return repetirNovaSenha;
	}
	@Required
	@DisplayName("Usuário")
	public Usuario getListaUsuarios() {
		return listaUsuarios;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	public void setRepetirNovaSenha(String repetirNovaSenha) {
		this.repetirNovaSenha = repetirNovaSenha;
	}
	public void setListaUsuarios(Usuario listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

}
