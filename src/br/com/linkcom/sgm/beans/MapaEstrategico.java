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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_mapaestrategico", sequenceName = "sq_mapaestrategico")
public class MapaEstrategico {
	
	private Integer id;
	private UnidadeGerencial unidadeGerencial;
	private String visao;
	private List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico;

	/* Campo TRANSIENTE. Seu cadastro é feito no Mapa do Negócio. */
	private String missao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_mapaestrategico")
	public Integer getId() {
		return id;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@Transient
	@DisplayName("Missão")
	public String getMissao() {
		return missao;
	}
	
	@Required
	public String getVisao() {
		return visao;
	}
	
	@OneToMany(mappedBy = "mapaEstrategico")
	public List<PerspectivaMapaEstrategico> getListaPerspectivaMapaEstrategico() {
		return listaPerspectivaMapaEstrategico;
	}
	
	public void setListaPerspectivaMapaEstrategico(List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico) {
		this.listaPerspectivaMapaEstrategico = listaPerspectivaMapaEstrategico;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setMissao(String missao) {
		this.missao = missao;
	}
	
	public void setVisao(String visao) {
		this.visao = visao;
	}
	
}

