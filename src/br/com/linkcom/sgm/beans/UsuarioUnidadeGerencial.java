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
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.FuncaoUGEnum;

@Entity
@SequenceGenerator(name = "sq_usuariounidadegerencial", sequenceName = "sq_usuariounidadegerencial")
public class UsuarioUnidadeGerencial {
	
	private Integer id;
	
	private Usuario usuario;
	private UnidadeGerencial unidadeGerencial;
	private FuncaoUGEnum funcao;	
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_usuariounidadegerencial")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@ManyToOne
	@Required
	public Usuario getUsuario() {
		return usuario;
	}
	
	@DisplayName("Função")
	@Required
	public FuncaoUGEnum getFuncao() {
		return funcao;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setFuncao(FuncaoUGEnum funcao) {
		this.funcao = funcao;
	}	
}
