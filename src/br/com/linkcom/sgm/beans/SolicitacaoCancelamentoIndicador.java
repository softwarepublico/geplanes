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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.AprovacaoEnum;

@Entity
@SequenceGenerator(name = "sq_solicitacaocancelamentoindicador", sequenceName = "sq_solicitacaocancelamentoindicador")
@DisplayName("Solicitação de cancelamento de indicador")
public class SolicitacaoCancelamentoIndicador {
	
	private Integer id;
	
	private AprovacaoEnum status;
	private Usuario solicitante;
	private Indicador indicador;	
	private String justificativaSol;
	private String justificativaRes;
	private Date dtSolicitacao;
	private Comentario comentario;
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_solicitacaocancelamentoindicador")
	public Integer getId() {
		return id;
	}
	
	@DisplayName("Status da solicitação")	
	public AprovacaoEnum getStatus() {
		return status;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public Usuario getSolicitante() {
		return solicitante;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public Indicador getIndicador() {
		return indicador;
	}
	
	@DisplayName("Justificativa da solicitação")
	@MaxLength(500)
	@Required
	public String getJustificativaSol() {
		return justificativaSol;
	}

	@DisplayName("Justificativa da Resposta")
	@MaxLength(500)	
	public String getJustificativaRes() {
		return justificativaRes;
	}
	
	@DisplayName("Data")
	@Required
	public Date getDtSolicitacao() {
		return dtSolicitacao;
	}
	
	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
	
	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}
	
	public void setStatus(AprovacaoEnum status) {
		this.status = status;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setJustificativaSol(String justificativaSol) {
		this.justificativaSol = justificativaSol;
	}
	
	public void setJustificativaRes(String justificativaRes) {
		this.justificativaRes = justificativaRes;
	}
	
	public void setDtSolicitacao(Date dtSolicitacao) {
		this.dtSolicitacao = dtSolicitacao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)	
	public Comentario getComentario() {
		return comentario;
	}
	
	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}	
	
}
