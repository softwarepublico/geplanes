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

import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_auditoriagestaoindicadoritem", sequenceName = "sq_auditoriagestaoindicadoritem")
public class AuditoriaGestaoIndicadorItem {
	
	private Integer id;
	private AuditoriaGestaoIndicador auditoriaGestaoIndicador;
	private ItemModeloAuditoriaGestao itemModeloAuditoriaGestao;
	private ItemFatorAvaliacao itemFatorAvaliacao;
	private String texto;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_auditoriagestaoindicadoritem")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="auditoriaGestaoIndicador_id")
	public AuditoriaGestaoIndicador getAuditoriaGestaoIndicador() {
		return auditoriaGestaoIndicador;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemModeloAuditoriaGestao_id")
	public ItemModeloAuditoriaGestao getItemModeloAuditoriaGestao() {
		return itemModeloAuditoriaGestao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemFatorAvaliacao_id")
	public ItemFatorAvaliacao getItemFatorAvaliacao() {
		return itemFatorAvaliacao;
	}
	
	@MaxLength(500)
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setItemFatorAvaliacao(ItemFatorAvaliacao itemFatorAvaliacao) {
		this.itemFatorAvaliacao = itemFatorAvaliacao;
	}

	public void setItemModeloAuditoriaGestao(
			ItemModeloAuditoriaGestao itemModeloAuditoriaGestao) {
		this.itemModeloAuditoriaGestao = itemModeloAuditoriaGestao;
	}

	public void setAuditoriaGestaoIndicador(
			AuditoriaGestaoIndicador auditoriaGestaoIndicador) {
		this.auditoriaGestaoIndicador = auditoriaGestaoIndicador;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
