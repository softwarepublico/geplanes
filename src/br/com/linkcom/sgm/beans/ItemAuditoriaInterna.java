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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_itemauditoriainterna", sequenceName = "sq_itemauditoriainterna")
public class ItemAuditoriaInterna {
	
	private Integer id;
	private AuditoriaInterna auditoriaInterna;
	private RequisitoNorma requisitoNorma;
	private String descricao;
	private UnidadeGerencial ugExterna;
	
	// Transientes
	private Norma norma;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_itemauditoriainterna")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public AuditoriaInterna getAuditoriaInterna() {
		return auditoriaInterna;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Requisito")
	public RequisitoNorma getRequisitoNorma() {
		return requisitoNorma;
	}
	
	@Required
	@DisplayName("Evidências")
	@MaxLength(3000)
	public String getDescricao() {
		return descricao;
	}
	
	@DisplayName("UG responsável pelo tratamento (Causa externa)")
	@ManyToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUgExterna() {
		return ugExterna;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAuditoriaInterna(AuditoriaInterna auditoriaInterna) {
		this.auditoriaInterna = auditoriaInterna;
	}

	public void setRequisitoNorma(RequisitoNorma requisitoNorma) {
		this.requisitoNorma = requisitoNorma;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setUgExterna(UnidadeGerencial ugExterna) {
		this.ugExterna = ugExterna;
	}
	
	@Transient
	public String getDescricaoCompleta() {
		return requisitoNorma != null ? requisitoNorma.getIndice() + " - " + descricao : descricao;
	}
	
	@DisplayName("Norma")
	@Transient
	public Norma getNorma() {
		if (norma != null) {
			return norma;
		}
		if (requisitoNorma != null && requisitoNorma.getNorma() != null) {
			return requisitoNorma.getNorma();
		}
		return null;
	}
	
	public void setNorma(Norma norma) {
		this.norma = norma;
	}
	
}
