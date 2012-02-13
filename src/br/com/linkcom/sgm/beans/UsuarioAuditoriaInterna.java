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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.TipoUsuarioAuditoriaInternaEnum;

@Entity
@SequenceGenerator(name = "sq_usuarioauditoriainterna", sequenceName = "sq_usuarioauditoriainterna")
public class UsuarioAuditoriaInterna {
	
	private Integer id;
	private AuditoriaInterna auditoriaInterna;
	private String nome;
	private TipoUsuarioAuditoriaInternaEnum tipo;
	private String funcao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_usuarioauditoriainterna")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public AuditoriaInterna getAuditoriaInterna() {
		return auditoriaInterna;
	}

	@Required
	@MaxLength(60)
	public String getNome() {
		return nome;
	}

	@Required
	public TipoUsuarioAuditoriaInternaEnum getTipo() {
		return tipo;
	}
	
	@Required
	@MaxLength(50)
	@DisplayName("Função")
	public String getFuncao() {
		return funcao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAuditoriaInterna(AuditoriaInterna auditoriaInterna) {
		this.auditoriaInterna = auditoriaInterna;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTipo(TipoUsuarioAuditoriaInternaEnum tipo) {
		this.tipo = tipo;
	}
	
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
}
