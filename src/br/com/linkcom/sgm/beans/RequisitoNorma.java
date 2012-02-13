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

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_requisitonorma", sequenceName = "sq_requisitonorma")
public class RequisitoNorma implements Comparable<RequisitoNorma> {
	
	private Integer id;
	private Norma norma;
	private String descricao;
	private String indice;
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_requisitonorma")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public Norma getNorma() {
		return norma;
	}
	
	@MaxLength(500)
	@DisplayName("Descrição")
	@Required
	public String getDescricao() {
		return descricao;
	}
	
	@MaxLength(20)
	@DisplayName("Índice")
	@Required
	public String getIndice() {
		return indice;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNorma(Norma norma) {
		this.norma = norma;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setIndice(String indice) {
		this.indice = indice;
	}
	
	@Transient
	@DescriptionProperty
	public String getDescricaoCompleta() {
		return indice + " " + (descricao != null && descricao.length() > 80 ? descricao.substring(0, 80) + "..." : descricao);
	}

	public int compareTo(RequisitoNorma that) {
		if (this.getIndice() != null && that.getIndice() != null) {
			String[] thisFields = this.getIndice().split("\\.");
			String[] thatFields = that.getIndice().split("\\.");
			Integer thisField;
			Integer thatField;
			
			int count = thisFields.length < thatFields.length ? thisFields.length : thatFields.length;
			
			for (int i = 0; i < count; i++) {
				try {
					thisField = Integer.parseInt(thisFields[i]);
				}
				catch (NumberFormatException e) {
					thisField = (int) thisFields[i].charAt(0);
				}
				
				try {
					thatField = Integer.parseInt(thatFields[i]);
				}
				catch (NumberFormatException e) {
					thatField = (int) thatFields[i].charAt(0);
				}
				
				if (thisField.compareTo(thatField) != 0) {
					return thisField.compareTo(thatField);
				}
			}
			
			// Caso os pedaços comparados sejam iguais, o menor número é o que tiver menor quantidade de pedaços.
			// Ex: 1 < 1.1
			return thisFields.length < thatFields.length ? -1 : 1;
		}
		return 0;
	}
}
