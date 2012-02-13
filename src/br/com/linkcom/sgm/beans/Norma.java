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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_norma", sequenceName = "sq_norma")
public class Norma {
	
	private Integer id;
	private String nome;
	private String descricao;
	
	private List<RequisitoNorma> listaRequisitoNorma = new ListSet<RequisitoNorma>(RequisitoNorma.class);
	
	public Norma(Integer id) {
		this.id = id;
	}
	
	public Norma() {
	}
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_norma")
	public Integer getId() {
		return id;
	}
	
	@Required
	@DisplayName("Nome")
	@MaxLength(100)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@MaxLength(500)
	@DisplayName("Descrição")
	public String getDescricao() {
		return descricao;
	}

	@DisplayName("Requisitos")
	@OneToMany(mappedBy="norma")
	public List<RequisitoNorma> getListaRequisitoNorma() {
		return listaRequisitoNorma;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setListaRequisitoNorma(List<RequisitoNorma> listaRequisitoNorma) {
		this.listaRequisitoNorma = listaRequisitoNorma;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof Norma) ) return false;
		Norma that = (Norma) obj;
		if (this.id == null || that.getId() == null ) return false; 
		return this.id.equals(that.getId());
	}	
}
