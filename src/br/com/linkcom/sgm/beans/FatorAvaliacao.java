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
@DisplayName("Sistema de pontuação")
@SequenceGenerator(name = "sq_fatoravaliacao", sequenceName = "sq_fatoravaliacao")
public class FatorAvaliacao {
	
	private Integer id;
	private String nome;
	private Boolean utilizarMatrizFCS = Boolean.FALSE;

	private List<ItemFatorAvaliacao> listaItemFatorAvaliacao = new ListSet<ItemFatorAvaliacao>(ItemFatorAvaliacao.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_fatoravaliacao")
	public Integer getId() {
		return id;
	}
	
	@Required
	@DisplayName("Nome")
	@MaxLength(50)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	
	@DisplayName("Fatores de avaliação")
	@OneToMany(mappedBy="fatorAvaliacao")
	public List<ItemFatorAvaliacao> getListaItemFatorAvaliacao() {
		return listaItemFatorAvaliacao;
	}
	
	@DisplayName("Utilizar na Matriz FCS")
	public Boolean getUtilizarMatrizFCS() {
		return utilizarMatrizFCS;
	}
	
	public void setUtilizarMatrizFCS(Boolean utilizarMatrizFCS) {
		this.utilizarMatrizFCS = utilizarMatrizFCS;
	}

	public void setListaItemFatorAvaliacao(
			List<ItemFatorAvaliacao> listaItemFatorAvaliacao) {
		this.listaItemFatorAvaliacao = listaItemFatorAvaliacao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FatorAvaliacao) {
			FatorAvaliacao that = (FatorAvaliacao) obj;
			return this.getId().equals(that.getId());
		}
		return false;
	}
}
