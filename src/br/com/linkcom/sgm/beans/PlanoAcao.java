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
import java.text.SimpleDateFormat;

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
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;

@Entity
@SequenceGenerator(name = "sq_planoacao", sequenceName = "sq_planoacao")
public class PlanoAcao {
	
	private Integer id;	
	private String texto;
	private String textoComo;
	private String textoPorque;
	private String textoQuem;
	private Date dtPlano;
	private StatusPlanoAcaoEnum status = StatusPlanoAcaoEnum.PLANEJADO;
	private Date dtAtualizacaoStatus;
	
	private UnidadeGerencial unidadeGerencial;
	private Iniciativa iniciativa;
	
	private Anomalia anomalia;
	private AcaoPreventiva acaoPreventiva;	
	
	//========================Get e Set==================================//
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_planoacao")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@DisplayName("Iniciativa")
	@ManyToOne(fetch=FetchType.LAZY)
	public Iniciativa getIniciativa() {
		return iniciativa;
	}
	
	@DescriptionProperty
	@MaxLength(5000)
	@DisplayName("O que")
	@Required
	public String getTexto() {
		return texto;
	}
	
	@DisplayName("Como")
	@Required
	public String getTextoComo() {
		return textoComo;
	}
	
	@DisplayName("Por que")
	@Required
	public String getTextoPorque() {
		return textoPorque;
	}
	
	@DisplayName("Quem")
	@Required
	public String getTextoQuem() {
		return textoQuem;
	}
	
	@DisplayName("Anomalia")
	@ManyToOne(fetch=FetchType.LAZY)
	public Anomalia getAnomalia() {
		return anomalia;
	}
	
	@DisplayName("Ação preventiva")
	@ManyToOne(fetch=FetchType.LAZY)
	public AcaoPreventiva getAcaoPreventiva() {
		return acaoPreventiva;
	}
	
	@DisplayName("Quando")
	@Required
	public Date getDtPlano() {
		return dtPlano;
	}
	
	@DisplayName("Status")
	@Required
	public StatusPlanoAcaoEnum getStatus() {
		return status;
	}	

	public void setTextoComo(String textoComo) {
		this.textoComo = textoComo;
	}
	
	public void setTextoPorque(String textoPorque) {
		this.textoPorque = textoPorque;
	}
	
	public void setTextoQuem(String textoQuem) {
		this.textoQuem = textoQuem;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setIniciativa(Iniciativa iniciativa) {
		this.iniciativa = iniciativa;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setAnomalia(Anomalia anomalia) {
		this.anomalia = anomalia;
	}
	
	public void setAcaoPreventiva(AcaoPreventiva acaoPreventiva) {
		this.acaoPreventiva = acaoPreventiva;
	}
	
	public void setDtPlano(Date dtPlano) {
		this.dtPlano = dtPlano;
	}
	
	public void setStatus(StatusPlanoAcaoEnum status) {
		this.status = status;
	}
	
	public Date getDtAtualizacaoStatus() {
		if (dtAtualizacaoStatus == null) {
			dtAtualizacaoStatus = new Date(System.currentTimeMillis());
		}
		return dtAtualizacaoStatus;
	}
	
	public void setDtAtualizacaoStatus(Date dtAtualizacaoStatus) {
		this.dtAtualizacaoStatus = dtAtualizacaoStatus;
	}
	
	//========================Transientes==================================//
	private boolean podeEditarTexto;
	private boolean podeEditarStatus;

	@Transient
	public boolean isPodeEditarTexto() {
		return podeEditarTexto;
	}
	
	@Transient
	public boolean isPodeEditarStatus() {
		return podeEditarStatus;
	}
	
	public void setPodeEditarTexto(boolean podeEditarTexto) {
		this.podeEditarTexto = podeEditarTexto;
	}
	
	public void setPodeEditarStatus(boolean podeEditarStatus) {
		this.podeEditarStatus = podeEditarStatus;
	}
	
	@Transient
	@DisplayName("Status")
	public String getDescricaoStatus() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (status != null) {
			return "<b>" + status.toString() + "</b><br>(Atualizado em: " + (dtAtualizacaoStatus != null ? simpleDateFormat.format(dtAtualizacaoStatus) : "-") + ")";
		}
		return "-";
	}
	
	@Transient
	public String getDescricaoDtAtualizacaoStatus() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return "(Atualizado em: " + (dtAtualizacaoStatus != null ? simpleDateFormat.format(dtAtualizacaoStatus) : "-") + ")";
	} 
}
