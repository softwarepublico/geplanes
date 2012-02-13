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
import br.com.linkcom.neo.types.ListSet;

@Entity
@SequenceGenerator(name = "sq_mapacompetencia", sequenceName = "sq_mapacompetencia")
public class MapaCompetencia {
	
	private Integer id;
	private UnidadeGerencial unidadeGerencial;
	
	/* Campo TRANSIENTE. Seu cadastro é feito no Mapa do Negócio. */
	private String missao;
	
	private List<Competencia> competencias = new ListSet<Competencia>(Competencia.class);
	private List<Atividade> atividades = new ListSet<Atividade>(Atividade.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_mapacompetencia")
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
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setMissao(String missao) {
		this.missao = missao;
	}
	
	// LISTAS
	
	@OneToMany(mappedBy = "mapaCompetencia")
	public List<Atividade> getAtividades() {
		return atividades;
	}
	
	@OneToMany(mappedBy = "mapaCompetencia")
	public List<Competencia> getCompetencias() {
		return competencias;
	}
	
	public void setAtividades(List<Atividade> atividades) {
		this.atividades = atividades;
	}
	
	public void setCompetencias(List<Competencia> competencias) {
		this.competencias = competencias;
	}
	
}
