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

import java.sql.Date;

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
@SequenceGenerator(name = "sq_comentarioitem", sequenceName = "sq_comentarioitem")
public class ComentarioItem {
	
	private Integer id;
	
	private Comentario comentario;	
	private Usuario usuario;
	private String texto;
	private Date data;	
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_comentarioitem")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)	
	public Comentario getComentario() {
		return comentario;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)	
	public Usuario getUsuario() {
		return usuario;
	}
	
	@Required
	@DisplayName("Comentário")
	@MaxLength(3000)		
	public String getTexto() {
		return texto;
	}
	
	@DisplayName("Data")
	@Required	
	public Date getData() {
		return data;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setData(Date data) {
		this.data = data;
	}

	
	// Transientes
	private SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador;
	private SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador;

	@Transient
	public SolicitacaoCancelamentoIndicador getSolicitacaoCancelamentoIndicador() {
		return solicitacaoCancelamentoIndicador;
	}

	@Transient
	public SolicitacaoRepactuacaoIndicador getSolicitacaoRepactuacaoIndicador() {
		return solicitacaoRepactuacaoIndicador;
	}

	public void setSolicitacaoCancelamentoIndicador(
			SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador) {
		this.solicitacaoCancelamentoIndicador = solicitacaoCancelamentoIndicador;
	}

	public void setSolicitacaoRepactuacaoIndicador(
			SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador) {
		this.solicitacaoRepactuacaoIndicador = solicitacaoRepactuacaoIndicador;
	}
}
