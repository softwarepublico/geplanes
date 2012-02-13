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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_matrizfcsiniciativafator", sequenceName = "sq_matrizfcsiniciativafator")
public class MatrizFCSIniciativaFator {
	
	private Integer id;
	private MatrizFCSIniciativa matrizFCSIniciativa;
	private MatrizFCSFator matrizFCSFator;
	private ItemFatorAvaliacao itemFatorAvaliacao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_matrizfcsiniciativafator")
	public Integer getId() {
		return id;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public MatrizFCSIniciativa getMatrizFCSIniciativa() {
		return matrizFCSIniciativa;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemFatorAvaliacao_id")
	@Required
	public ItemFatorAvaliacao getItemFatorAvaliacao() {
		return itemFatorAvaliacao;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public MatrizFCSFator getMatrizFCSFator() {
		return matrizFCSFator;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMatrizFCSIniciativa(MatrizFCSIniciativa matrizFCSIniciativa) {
		this.matrizFCSIniciativa = matrizFCSIniciativa;
	}

	public void setItemFatorAvaliacao(ItemFatorAvaliacao itemFatorAvaliacao) {
		this.itemFatorAvaliacao = itemFatorAvaliacao;
	}
	
	public void setMatrizFCSFator(MatrizFCSFator matrizFCSFator) {
		this.matrizFCSFator = matrizFCSFator;
	}
}
