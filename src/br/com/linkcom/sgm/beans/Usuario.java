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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.authorization.User;
import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.MinLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_usuario", sequenceName = "sq_usuario")
@DisplayName("Usuário")
public class Usuario implements User{
	
	private Integer id;
	
	private String nome;
	private String cargo;
	private String infComplementar;
	private String login;
	private String senha;
	private String email;	
	private Arquivo foto;
	private String ramal;
	protected Boolean bloqueado;
	
	private Set<UsuarioPapel> usuariosPapel = new ListSet<UsuarioPapel>(UsuarioPapel.class);
	private List<UsuarioUnidadeGerencial> usuariosUnidadeGerencial = new ArrayList<UsuarioUnidadeGerencial>();	
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_usuario")
	public Integer getId() {
		return id;
	}
	
	@Required
	@MaxLength(50)
	public String getLogin() {
		return login;
	}
	
	@DescriptionProperty
	@Required
	@MaxLength(60)
	public String getNome() {
		return nome;
	}
	
	@MinLength(5)
	@MaxLength(50)
	public String getSenha() {
		return senha;
	}

	@MaxLength(60)
	public String getCargo() {
		return cargo;
	}
	
	@DisplayName("E-mail")
	@Required
	@MaxLength(50)
	public String getEmail() {
		return email;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public Arquivo getFoto() {
		return foto;
	}
	
	@DisplayName("Ramal")
	@MaxLength(20)	
	public String getRamal() {
		return ramal;
	}
	
	@Required
	public Boolean getBloqueado() {
		return bloqueado;
	}
	
	@OneToMany(mappedBy="usuario")
	public Set<UsuarioPapel> getUsuariosPapel() {
		return usuariosPapel;
	}
	
	@MaxLength(500)
	@DisplayName("Informação Complementar")
	public String getInfComplementar() {
		return infComplementar;
	}
	
	@OneToMany(mappedBy="usuario")
	@DisplayName("Unidades gerenciais vinculadas")
	public List<UsuarioUnidadeGerencial> getUsuariosUnidadeGerencial() {
		return usuariosUnidadeGerencial;
	}	
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setUsuariosPapel(Set<UsuarioPapel> usuariosPapel) {
		this.usuariosPapel = usuariosPapel;
	}
	public void setFoto(Arquivo foto) {
		this.foto = foto;
	}
	public void setRamal(String ramal) {
		this.ramal = ramal;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public void setInfComplementar(String infComplementar) {
		this.infComplementar = infComplementar;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setUsuariosUnidadeGerencial(List<UsuarioUnidadeGerencial> usuariosUnidadeGerencial) {
		this.usuariosUnidadeGerencial = usuariosUnidadeGerencial;
	}	
	
	//==========================Transiente=======================//
	private String ugsAtuais;
	private String verificaSenha;
	private Set<Papel> papeis = new ListSet<Papel>(Papel.class);
	
	@Transient
	@DisplayName("UG(s)")
	public String getUgsAtuais() {
		return ugsAtuais;
	}
	
	@Transient
	@DisplayName("Nível de acesso")
	public Set<Papel> getPapeis() {
		return papeis;
	}
	
	@Transient
	public String getListaPapel() {
		String lista = "";
		for (Papel papel : getPapeis()) {
			lista += papel.getNome() + " ";
		}
		return lista;
	}
	
	@Transient
	@DisplayName("Confirmação da senha")
	@MaxLength(50)
	public String getVerificaSenha() {
		return verificaSenha;
	}
	
	@Transient
	public String getPassword() {
		return senha;
	}
	
	public void setUgsAtuais(String ugsAtuais) {
		this.ugsAtuais = ugsAtuais;
	}
	
	public void setVerificaSenha(String verificaSenha) {
		this.verificaSenha = verificaSenha;
	}
	
	public void setPapeis(Set<Papel> papeis) {
		this.papeis = papeis;
	}
	
	
	@Override
	public String toString() {
		return this.nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof Usuario) ) return false;
		Usuario that = (Usuario) obj;
		if (this.id == null || that.getId() == null ) return false; 
		return this.id.equals(that.getId());
	}
	
	@Override
	public int hashCode() {
		if (this.getId() != null) {
			return this.getId().hashCode();
		}
		return super.hashCode();
	}
}
