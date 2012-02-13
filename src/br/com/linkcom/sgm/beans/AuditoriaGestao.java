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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@DisplayName("Auditoria")
@SequenceGenerator(name = "sq_auditoriagestao", sequenceName = "sq_auditoriagestao")
public class AuditoriaGestao {
	
	private Integer id;
	private String descricao;
	private UnidadeGerencial unidadeGerencial;
	private ModeloAuditoriaGestao modeloAuditoriaGestao;
	private String responsavel;
	private Date dataAuditoria;
	
	private List<AuditoriaGestaoIndicador> listaAuditoriaGestaoIndicador = new ListSet<AuditoriaGestaoIndicador>(AuditoriaGestaoIndicador.class);
	
	//TRANSIENTES
	private PlanoGestao planoGestao;
	private Indicador indicador;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_auditoriagestao")
	public Integer getId() {
		return id;
	}
	
	@DisplayName("Período avaliado")
	@MaxLength(100)
	@Required
	@DescriptionProperty
	public String getDescricao() {
		return descricao;
	}

	@Required
	@DisplayName("Unidade gerencial")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="unidadeGerencial_id")
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}

	@Required
	@DisplayName("Modelo de auditoria")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="modeloAuditoriaGestao_id")
	public ModeloAuditoriaGestao getModeloAuditoriaGestao() {
		return modeloAuditoriaGestao;
	}

	@Required
	@MaxLength(100)
	@DisplayName("Analista")
	public String getResponsavel() {
		return responsavel;
	}

	@Required
	@DisplayName("Data da auditoria")
	public Date getDataAuditoria() {
		return dataAuditoria;
	}
	
	@OneToMany(mappedBy="auditoriaGestao")
	public List<AuditoriaGestaoIndicador> getListaAuditoriaGestaoIndicador() {
		return listaAuditoriaGestaoIndicador;
	}
	
	public void setListaAuditoriaGestaoIndicador(
			List<AuditoriaGestaoIndicador> listaAuditoriaGestaoIndicador) {
		this.listaAuditoriaGestaoIndicador = listaAuditoriaGestaoIndicador;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}

	public void setModeloAuditoriaGestao(ModeloAuditoriaGestao modeloAuditoriaGestao) {
		this.modeloAuditoriaGestao = modeloAuditoriaGestao;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public void setDataAuditoria(Date dataAuditoria) {
		this.dataAuditoria = dataAuditoria;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	//TRANSIENTES
	
	@Transient
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	
	@Transient
	public Indicador getIndicador() {
		return indicador;
	}
	
	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	
}
