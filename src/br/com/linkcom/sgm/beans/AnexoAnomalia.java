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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_anexoanomalia", sequenceName = "sq_anexoanomalia")
public class AnexoAnomalia {
	
	private Integer id;
	
	private Arquivo arquivo;
	private Anomalia anomalia;
	private String nome;
	private String descricao;
	
	//=========================Get e Set==================================//
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_anexoanomalia")
	public Integer getId() {
		return id;
	}
	
	@MaxLength(30)
	@Required
	public String getNome() {
		return nome;
	}	
	
	@DisplayName("Descrição")
	@MaxLength(500)
	public String getDescricao() {
		return descricao;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public Arquivo getArquivo() {
		return arquivo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public Anomalia getAnomalia() {
		return anomalia;
	}
	
	public void setAnomalia(Anomalia anomalia) {
		this.anomalia = anomalia;
	}
	
	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AnexoAnomalia) ) return false;
		AnexoAnomalia anexoAnomalia = (AnexoAnomalia) obj;
		if (this.id == null || anexoAnomalia.getId() == null ) return false; 
		return this.id.equals(anexoAnomalia.getId());
	}
	
	
}
