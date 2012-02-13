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
package br.com.linkcom.sgm.beans;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.authorization.impl.AbstractPermission;

@Entity
@SequenceGenerator(name = "sq_permissao", sequenceName = "sq_permissao")
public class Permissao extends AbstractPermission {
	
	private Integer id;
	
	private Papel papel;
	private Tela tela;
	
	private String permissionString;
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_permissao")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public Papel getPapel() {
		return papel;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public Tela getTela() {
		return tela;
	}
	
	public void setTela(Tela tela) {
		this.tela = tela;
	}
	
	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public void setPermissionString(String string) {
		this.permissionString = string;
	}

	@Override
	public String getPermissionString() {
		return permissionString;
	}
	
	@Transient
	public Role getRole() {
		return papel;
	}

}
