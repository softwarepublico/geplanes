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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_painelindicadorfiltro", sequenceName = "sq_painelindicadorfiltro")
public class PainelIndicadorFiltro {
	
	private Integer id;
	private UnidadeGerencial unidadeGerencial;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	
	//=========================Construtor================================//
	
	public PainelIndicadorFiltro(){
	}
	
	public PainelIndicadorFiltro(Integer id){
		this.id = id;
	}
	
	public PainelIndicadorFiltro(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico){
		this.unidadeGerencial = unidadeGerencial;
		this.objetivoMapaEstrategico = objetivoMapaEstrategico;
	}
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_painelindicadorfiltro")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Objetivo Estratégico")
	public ObjetivoMapaEstrategico getObjetivoMapaEstrategico() {
		return objetivoMapaEstrategico;
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
	
	
	// Transientes
	private boolean excluir = true;
	
	@Transient
	public boolean isExcluir() {
		return excluir;
	}
	
	public void setExcluir(boolean excluir) {
		this.excluir = excluir;
	}
}
