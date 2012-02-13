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

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_iniciativa", sequenceName = "sq_iniciativa")
public class Iniciativa {
	
	private Integer id;
	private String descricao;
	
	private UnidadeGerencial unidadeGerencial;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	private List<PlanoAcao> listaPlanoAcao = new ListSet<PlanoAcao>(PlanoAcao.class);
	
	//=========================Construtor================================//
	
	public Iniciativa(){
	}
	
	public Iniciativa(Integer id){
		this.id = id;
	}
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_iniciativa")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@DisplayName("Descrição")
	@MaxLength(100)
	@Required
	@DescriptionProperty
	public String getDescricao() {
		return descricao;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Objetivo Estratégico")
	public ObjetivoMapaEstrategico getObjetivoMapaEstrategico() {
		return objetivoMapaEstrategico;
	}
	
	@OneToMany(mappedBy="iniciativa")
	public List<PlanoAcao> getListaPlanoAcao() {
		return listaPlanoAcao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}

	public void setObjetivoMapaEstrategico(ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		this.objetivoMapaEstrategico = objetivoMapaEstrategico;
	}

	public void setListaPlanoAcao(List<PlanoAcao> listaPlanoAcao) {
		this.listaPlanoAcao = listaPlanoAcao;
	}
	
	//=============================Transiente============================//
	private String idxPerspectivaMapaEstrategico;
	private String idxObjetivoMapaEstrategico;
	private String idxIniciativa;

	@Transient
	public String getIdxPerspectivaMapaEstrategico() {
		return idxPerspectivaMapaEstrategico;
	}

	@Transient
	public String getIdxObjetivoMapaEstrategico() {
		return idxObjetivoMapaEstrategico;
	}

	@Transient
	public String getIdxIniciativa() {
		return idxIniciativa;
	}

	public void setIdxPerspectivaMapaEstrategico(String idxPerspectivaMapaEstrategico) {
		this.idxPerspectivaMapaEstrategico = idxPerspectivaMapaEstrategico;
	}

	public void setIdxObjetivoMapaEstrategico(String idxObjetivoMapaEstrategico) {
		this.idxObjetivoMapaEstrategico = idxObjetivoMapaEstrategico;
	}

	public void setIdxIniciativa(String idxIniciativa) {
		this.idxIniciativa = idxIniciativa;
	}
}
