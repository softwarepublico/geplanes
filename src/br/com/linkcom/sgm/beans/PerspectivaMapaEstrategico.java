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
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;

@Entity
@SequenceGenerator(name = "sq_perspectivamapaestrategico", sequenceName = "sq_perspectivamapaestrategico")
@DisplayName("Perspectiva do mapa estratégico")
public class PerspectivaMapaEstrategico implements Hierarquizavel<UnidadeGerencial, ObjetivoMapaEstrategico> {

	private Integer id;
	private MapaEstrategico mapaEstrategico;
	private Perspectiva perspectiva;
	private Integer ordem;
	private List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_perspectivamapaestrategico")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public MapaEstrategico getMapaEstrategico() {
		return mapaEstrategico;
	}

	@Required
	@ManyToOne(fetch = FetchType.EAGER)
	public Perspectiva getPerspectiva() {
		return perspectiva;
	}
	
	@Required
	@MaxLength(2)	
	public Integer getOrdem() {
		return ordem;
	}
	
	@OneToMany(mappedBy="perspectivaMapaEstrategico")
	public List<ObjetivoMapaEstrategico> getListaObjetivoMapaEstrategico() {
		return listaObjetivoMapaEstrategico;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMapaEstrategico(MapaEstrategico mapaEstrategico) {
		this.mapaEstrategico = mapaEstrategico;
	}

	public void setPerspectiva(Perspectiva perspectiva) {
		this.perspectiva = perspectiva;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public void setListaObjetivoMapaEstrategico(List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico) {
		this.listaObjetivoMapaEstrategico = listaObjetivoMapaEstrategico;
	}
	
	/* TRANSIENTE */
	private String descricao;
	private UnidadeGerencial unidadeGerencial;
	private ItemPainelControleDTO itemPainelControleDTO;
	
	@Transient
	@DescriptionProperty
	public String getDescricao() {
		if (perspectiva != null) {
			return perspectiva.getDescricao();
		}
		return descricao;
	}
	
	@Transient
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@Transient
	public ItemPainelControleDTO getItemPainelControleDTO() {
		return itemPainelControleDTO;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setItemPainelControleDTO(ItemPainelControleDTO itemPainelControleDTO) {
		this.itemPainelControleDTO = itemPainelControleDTO;
	}
	
	//========================= INTERFACE ==================================//
	
	@Transient
	public List<ObjetivoMapaEstrategico> getChildren() {
		List<ObjetivoMapaEstrategico> retorno = new ListSet<ObjetivoMapaEstrategico>(ObjetivoMapaEstrategico.class);
		retorno.addAll( getListaObjetivoMapaEstrategico() );
		return retorno;
	}

	@Transient
	public String getDescricaoCompleta() {
		return getDescricao() != null ? getDescricao() : "<SEM NOME>";
	}

	@Transient
	public UnidadeGerencial getParent() {
		return getUnidadeGerencial();
	}

	@Transient
	public Double getPercentualTolerancia() {
		return null;
	}

	@Transient
	public Double getPeso() {
		return null;
	}

	@Transient
	public String getTipo() {
		return "perspectiva";
	}

	@Transient
	public void setParent(UnidadeGerencial parent) {
		setUnidadeGerencial(parent);
	}
	
}
