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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.authorization.AuthorizationProcessItemFilter;
import br.com.linkcom.neo.authorization.Role;
import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_papel", sequenceName = "sq_papel")
@DisplayName("Nível de acesso")
public class Papel implements Role{
	
	private Integer id;	
	private String nome;
	private Boolean administrador;
	
	protected Set<Permissao> listaPermissao = new ListSet<Permissao>(Permissao.class);
	protected Set<UsuarioPapel> listaPessoapapel = new ListSet<UsuarioPapel>(UsuarioPapel.class);
	
	// Transiente
	private Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap = new HashMap<String, List<AuthorizationProcessItemFilter>>();
	
	//=========================Get e Set==================================//
	@Id
	@DisplayName("Id")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_papel")
	public Integer getId() {
		return id;
	}
	
	@MaxLength(50)
	@Required
	@DisplayName("Nome")
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@Required
	@DisplayName("Administrador")
	public Boolean getAdministrador() {
		return administrador;
	}
	
	@Transient
	public Map<String, List<AuthorizationProcessItemFilter>> getGroupAuthorizationMap() {
		return groupAuthorizationMap;
	}
	
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@OneToMany(mappedBy = "papel")
	public Set<Permissao> getListaPermissao() {
		return listaPermissao;
	}
	
	@OneToMany(mappedBy = "papel")
	public Set<UsuarioPapel> getListaPessoapapel() {
		return listaPessoapapel;
	}
	
	public void setListaPermissao(Set<Permissao> listaPermissao) {
		this.listaPermissao = listaPermissao;
	}
	
	public void setListaPessoapapel(Set<UsuarioPapel> listaPessoapapel) {
		this.listaPessoapapel = listaPessoapapel;
	}
	
	public void setGroupAuthorizationMap(Map<String, List<AuthorizationProcessItemFilter>> groupAuthorizationMap) {
		this.groupAuthorizationMap = groupAuthorizationMap;
	}
	
	@Transient
	public String getName() {
		return nome;
	}
	
	@Transient
	public String getDescription() {
		return nome;
	}
	
	@Transient
	public Boolean isAdmin() {
		return getAdministrador();
	}
}
