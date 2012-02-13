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

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@SequenceGenerator(name = "sq_ocorrencia", sequenceName = "sq_ocorrencia")
@DisplayName("Diário de bordo")
public class Ocorrencia {
	
	private Integer id;
	private Integer numero;
	private String descricao;
	private Date dataLancamento;
	private String situacao;
	private UnidadeGerencial unidadeGerencial;
	private Usuario relator;
	private Boolean reincidente;
	private String contraMedidasImediatas;
	
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_ocorrencia")
	public Integer getId() {
		return id;
	}
	@DisplayName("Data de Lançamento")
	@Required
	public Date getDataLancamento() {
		return dataLancamento;
	}
	@Required
	@DisplayName("Descrição")
	@MaxLength(1800)
	public String getDescricao() {
		return descricao;
	}
	@DisplayName("Nº Diário de Bordo")
	@DescriptionProperty
	public Integer getNumero() {
		return numero;
	}
	@DisplayName("Situação")
	@MaxLength(100)
	public String getSituacao() {
		return situacao;
	}
	@DisplayName("Relator")
	@ManyToOne(fetch=FetchType.LAZY)
	public Usuario getRelator() {
		return relator;
	}
	@DisplayName("Unidade Gerencial")
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	@DisplayName("Reincidente")
	public Boolean getReincidente() {
		return reincidente;
	}
	@DisplayName("Correção")
	public String getContraMedidasImediatas() {
		return contraMedidasImediatas;
	}
	public void setReincidente(Boolean reincidente) {
		this.reincidente = reincidente;
	}
	public void setContraMedidasImediatas(String contraMedidasImediatas) {
		this.contraMedidasImediatas = contraMedidasImediatas;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setRelator(Usuario relator) {
		this.relator = relator;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	//=============================Transiente============================//
	private Boolean podeEditar;
	private Boolean podeExcluir;
	private PlanoGestao planoGestao;
	private Anomalia anomalia;
	
	@Transient
	public Boolean getPodeEditar() {
		return podeEditar;
	}
	@Transient
	public Boolean getPodeExcluir() {
		return podeExcluir;
	}	
	@Transient
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	@Transient
	public Anomalia getAnomalia() {
		return anomalia;
	}
	public void setAnomalia(Anomalia anomalia) {
		this.anomalia = anomalia;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setPodeEditar(Boolean podeEditar) {
		this.podeEditar = podeEditar;
	}
	public void setPodeExcluir(Boolean podeExcluir) {
		this.podeExcluir = podeExcluir;
	}
}
