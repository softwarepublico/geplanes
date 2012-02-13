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
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.StatusAuditoriaInternaEnum;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@DisplayName("Auditoria interna")
@SequenceGenerator(name = "sq_auditoriainterna", sequenceName = "sq_auditoriainterna")
public class AuditoriaInterna {
	
	private Integer id;
	private UnidadeGerencial ugRegistro;
	private UnidadeGerencial ugResponsavel;
	private Norma norma;
	private Date dataAuditoria;
	private Date dataEncerramento;
	private String observacoes;
	private StatusAuditoriaInternaEnum status;
	
	private List<ItemAuditoriaInterna> listaItemAuditoriaInterna = new ListSet<ItemAuditoriaInterna>(ItemAuditoriaInterna.class);
	private List<UsuarioAuditoriaInterna> listaUsuarioAuditoriaInterna = new ListSet<UsuarioAuditoriaInterna>(UsuarioAuditoriaInterna.class);
	
	//TRANSIENTES
	private PlanoGestao planoGestao;
	private Boolean podeEditar;
	private Boolean podeExcluir;
	private Boolean podeImprimir;	
	private List<ItemAuditoriaInterna> listaNaoConformidades = new ListSet<ItemAuditoriaInterna>(ItemAuditoriaInterna.class);
	private List<ItemAuditoriaInterna> listaOutrasNaoConformidades = new ListSet<ItemAuditoriaInterna>(ItemAuditoriaInterna.class);
	private List<UsuarioAuditoriaInterna> listaEquipeAuditora = new ListSet<UsuarioAuditoriaInterna>(UsuarioAuditoriaInterna.class);
	private List<UsuarioAuditoriaInterna> listaEquipeAuditada = new ListSet<UsuarioAuditoriaInterna>(UsuarioAuditoriaInterna.class);
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_auditoriainterna")
	public Integer getId() {
		return id;
	}
	
	@Required
	@DisplayName("Setor de registro")
	@ManyToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUgRegistro() {
		return ugRegistro;
	}

	@Required
	@DisplayName("Área auditada")	
	@ManyToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUgResponsavel() {
		return ugResponsavel;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public Norma getNorma() {
		return norma;
	}

	@Required
	@DisplayName("Data da auditoria")
	public Date getDataAuditoria() {
		return dataAuditoria;
	}
	
	@DisplayName("Data de encerramento")
	public Date getDataEncerramento() {
		return dataEncerramento;
	}
	
	@MaxLength(3000)
	@DisplayName("Observações")
	public String getObservacoes() {
		return observacoes;
	}
	
	@Required
	public StatusAuditoriaInternaEnum getStatus() {
		return status;
	}
	
	@OneToMany(mappedBy="auditoriaInterna")
	public List<ItemAuditoriaInterna> getListaItemAuditoriaInterna() {
		return listaItemAuditoriaInterna;
	}
	
	@OneToMany(mappedBy="auditoriaInterna")
	public List<UsuarioAuditoriaInterna> getListaUsuarioAuditoriaInterna() {
		return listaUsuarioAuditoriaInterna;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUgRegistro(UnidadeGerencial ugRegistro) {
		this.ugRegistro = ugRegistro;
	}

	public void setUgResponsavel(UnidadeGerencial ugResponsavel) {
		this.ugResponsavel = ugResponsavel;
	}

	public void setNorma(Norma norma) {
		this.norma = norma;
	}

	public void setDataAuditoria(Date dataAuditoria) {
		this.dataAuditoria = dataAuditoria;
	}
	
	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	public void setStatus(StatusAuditoriaInternaEnum status) {
		this.status = status;
	}
	
	public void setListaItemAuditoriaInterna(List<ItemAuditoriaInterna> listaItemAuditoriaInterna) {
		this.listaItemAuditoriaInterna = listaItemAuditoriaInterna;
	}
	
	public void setListaUsuarioAuditoriaInterna(List<UsuarioAuditoriaInterna> listaUsuarioAuditoriaInterna) {
		this.listaUsuarioAuditoriaInterna = listaUsuarioAuditoriaInterna;
	}
	
	//TRANSIENTES	

	@DisplayName(Nomes.Plano_de_gestao)
	@Transient
	@Required
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	
	@Transient
	@DisplayName("Não conformidades")
	public List<ItemAuditoriaInterna> getListaNaoConformidades() {
		return listaNaoConformidades;
	}

	@Transient
	@DisplayName("Outras não conformidades")	
	public List<ItemAuditoriaInterna> getListaOutrasNaoConformidades() {
		return listaOutrasNaoConformidades;
	}

	@Transient
	@DisplayName("Equipe auditora")		
	public List<UsuarioAuditoriaInterna> getListaEquipeAuditora() {
		return listaEquipeAuditora;
	}
	
	@Transient
	@DisplayName("Auditados")		
	public List<UsuarioAuditoriaInterna> getListaEquipeAuditada() {
		return listaEquipeAuditada;
	}
	
	@Transient
	public Boolean getPodeEditar() {
		return podeEditar;
	}
	
	@Transient
	public Boolean getPodeExcluir() {
		return podeExcluir;
	}
	
	@Transient
	public Boolean getPodeImprimir() {
		return podeImprimir;
	}
	
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}

	public void setListaNaoConformidades(List<ItemAuditoriaInterna> listaNaoConformidades) {
		this.listaNaoConformidades = listaNaoConformidades;
	}

	public void setListaOutrasNaoConformidades(List<ItemAuditoriaInterna> listaOutrasNaoConformidades) {
		this.listaOutrasNaoConformidades = listaOutrasNaoConformidades;
	}
	
	public void setListaEquipeAuditora(List<UsuarioAuditoriaInterna> listaEquipeAuditora) {
		this.listaEquipeAuditora = listaEquipeAuditora;
	}
	
	public void setListaEquipeAuditada(List<UsuarioAuditoriaInterna> listaEquipeAuditada) {
		this.listaEquipeAuditada = listaEquipeAuditada;
	}

	public void setPodeEditar(Boolean podeEditar) {
		this.podeEditar = podeEditar;
	}

	public void setPodeExcluir(Boolean podeExcluir) {
		this.podeExcluir = podeExcluir;
	}

	public void setPodeImprimir(Boolean podeImprimir) {
		this.podeImprimir = podeImprimir;
	}
	
}
