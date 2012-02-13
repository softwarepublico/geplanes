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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@SequenceGenerator(name = "sq_matrizfcs", sequenceName = "sq_matrizfcs")
@DisplayName("Matriz de iniciativas x fatores críticos de sucesso")
public class MatrizFCS {
	
	private Integer id;
	private UnidadeGerencial unidadeGerencial;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	private FatorAvaliacao fatorAvaliacao;
	private List<MatrizFCSIniciativa> listaMatrizFcsIniciativa = new ListSet<MatrizFCSIniciativa>(MatrizFCSIniciativa.class);
	private List<MatrizFCSFator> listaMatrizFcsFator = new ListSet<MatrizFCSFator>(MatrizFCSFator.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_matrizfcs")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName(Nomes.Estrategia)
	public ObjetivoMapaEstrategico getObjetivoMapaEstrategico() {
		return objetivoMapaEstrategico;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	public FatorAvaliacao getFatorAvaliacao() {
		return fatorAvaliacao;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setObjetivoMapaEstrategico(ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		this.objetivoMapaEstrategico = objetivoMapaEstrategico;
	}
	public void setFatorAvaliacao(FatorAvaliacao fatorAvaliacao) {
		this.fatorAvaliacao = fatorAvaliacao;
	}

	@OneToMany(mappedBy="matrizFCS")
	public List<MatrizFCSIniciativa> getListaMatrizFcsIniciativa() {
		return listaMatrizFcsIniciativa;
	}
	
	@OneToMany(mappedBy="matrizFCS")
	public List<MatrizFCSFator> getListaMatrizFcsFator() {
		return listaMatrizFcsFator;
	}

	public void setListaMatrizFcsIniciativa(
			List<MatrizFCSIniciativa> listaMatrizFcsIniciativa) {
		this.listaMatrizFcsIniciativa = listaMatrizFcsIniciativa;
	}
	
	public void setListaMatrizFcsFator(List<MatrizFCSFator> listaMatrizFcsFator) {
		this.listaMatrizFcsFator = listaMatrizFcsFator;
	}
	
	/**
	 * Transientes
	 **/
	private PerspectivaMapaEstrategico perspectivaMapaEstrategico;
	
	@Transient
	public PerspectivaMapaEstrategico getPerspectivaMapaEstrategico() {
		return perspectivaMapaEstrategico;
	}
	
	public void setPerspectivaMapaEstrategico(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		this.perspectivaMapaEstrategico = perspectivaMapaEstrategico;
	}
}
