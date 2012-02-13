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
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_objetivomapaestrategico", sequenceName = "sq_objetivomapaestrategico")
@DisplayName("Objetivo do mapa estratégico")
public class ObjetivoMapaEstrategico implements Hierarquizavel<PerspectivaMapaEstrategico, Indicador> {

	private Integer id;
	private PerspectivaMapaEstrategico perspectivaMapaEstrategico;
	private ObjetivoEstrategico objetivoEstrategico;

	private List<MatrizFCS> listaMatrizFCS = new ListSet<MatrizFCS>(MatrizFCS.class);
	private List<Indicador> listaIndicador = new ListSet<Indicador>(Indicador.class);
	private List<Iniciativa> listaIniciativa = new ListSet<Iniciativa>(Iniciativa.class);
	
	public ObjetivoMapaEstrategico() {
	}
	
	public ObjetivoMapaEstrategico(int id) {
		this.id = id;
	}	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_objetivomapaestrategico")
	public Integer getId() {
		return id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	public PerspectivaMapaEstrategico getPerspectivaMapaEstrategico() {
		return perspectivaMapaEstrategico;
	}

	@Required
	@ManyToOne(fetch=FetchType.EAGER)
	public ObjetivoEstrategico getObjetivoEstrategico() {
		return objetivoEstrategico;
	}
	
	@OneToMany(mappedBy="objetivoMapaEstrategico")
	public List<Indicador> getListaIndicador() {
		return listaIndicador;
	}
	
	@OneToMany(mappedBy="objetivoMapaEstrategico")
	public List<Iniciativa> getListaIniciativa() {
		return listaIniciativa;
	}
	
	@OneToMany(mappedBy="objetivoMapaEstrategico")
	public List<MatrizFCS> getListaMatrizFCS() {
		return listaMatrizFCS;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPerspectivaMapaEstrategico(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		this.perspectivaMapaEstrategico = perspectivaMapaEstrategico;
	}

	public void setObjetivoEstrategico(ObjetivoEstrategico objetivoEstrategico) {
		this.objetivoEstrategico = objetivoEstrategico;
	}
	
	public void setListaIndicador(List<Indicador> listaIndicador) {
		this.listaIndicador = listaIndicador;
	}
	
	public void setListaIniciativa(List<Iniciativa> listaIniciativa) {
		this.listaIniciativa = listaIniciativa;
	}
	
	public void setListaMatrizFCS(List<MatrizFCS> listaMatrizFCS) {
		this.listaMatrizFCS = listaMatrizFCS;
	}

	/* TRANSIENTE */
	private String descricao;
	private boolean somenteLeitura;
	
	@Transient
	@DescriptionProperty
	public String getDescricao() {
		if (objetivoEstrategico != null) {
			return objetivoEstrategico.getDescricao();
		}
		return descricao;
	}
	
	@Transient
	public boolean isSomenteLeitura() {
		return somenteLeitura;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setSomenteLeitura(boolean somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getId().intValue() == ((ObjetivoMapaEstrategico) obj).getId().intValue();
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}	
	
	//========================= INTERFACE ==================================//

	@Transient
	public List<Indicador> getChildren() {
		List<Indicador> retorno = new ListSet<Indicador>(Indicador.class);
		retorno.addAll( getListaIndicador() );
		return retorno;
	}

	@Transient
	public String getDescricaoCompleta() {
		return getDescricao() != null ? getDescricao() : "<SEM NOME>";
	}

	@Transient
	public PerspectivaMapaEstrategico getParent() {
		return getPerspectivaMapaEstrategico();
	}

	@Transient
	public Double getPercentualTolerancia() {
		return null;
	}

	@Transient
	public Double getPeso() {
		double peso = 0;
		for (Indicador indicador : getListaIndicador()) {
			peso += indicador.getPeso();
		}
		return peso;
	}

	@Transient
	public String getTipo() {
		return "objetivoMapaEstrategico";
	}

	@Transient
	public void setParent(PerspectivaMapaEstrategico parent) {
		setPerspectivaMapaEstrategico(parent);
	}	
}
