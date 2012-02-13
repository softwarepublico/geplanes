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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_itemmodeloauditoriagestao", sequenceName = "sq_itemmodeloauditoriagestao")
public class ItemModeloAuditoriaGestao {
	
	private Integer id;
	private ModeloAuditoriaGestao modeloAuditoriaGestao;
	private String nome;
	private String descricao;
	private FatorAvaliacao fatorAvaliacao;
	private Integer ordem;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_itemmodeloauditoriagestao")
	public Integer getId() {
		return id;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="modeloAuditoriaGestao_id")
	public ModeloAuditoriaGestao getModeloAuditoriaGestao() {
		return modeloAuditoriaGestao;
	}

	@Required
	@MaxLength(100)
	@DescriptionProperty
	public String getNome() {
		return nome;
	}

	@DisplayName("Sistema de pontuação")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fatorAvaliacao_id")
	public FatorAvaliacao getFatorAvaliacao() {
		return fatorAvaliacao;
	}

	@Required
	public Integer getOrdem() {
		return ordem;
	}
	
	@DisplayName("Descrição")
	@MaxLength(500)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setModeloAuditoriaGestao(ModeloAuditoriaGestao modeloAuditoriaGestao) {
		this.modeloAuditoriaGestao = modeloAuditoriaGestao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setFatorAvaliacao(FatorAvaliacao fatorAvaliacao) {
		this.fatorAvaliacao = fatorAvaliacao;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
