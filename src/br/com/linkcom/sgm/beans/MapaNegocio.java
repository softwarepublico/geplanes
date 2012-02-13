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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;

@Entity
@Bean
@SequenceGenerator(name = "sq_mapanegocio", sequenceName = "sq_mapanegocio")
public class MapaNegocio {
	
	private Integer id;
	private UnidadeGerencial unidadeGerencial;
	
	private String fornecedores;
	private String insumos;
	private String negocio;
	private String pessoal;
	private String equipamentos;
	private String produtos;
	private String clientes;
	private String missao;
	
	private String produto;
	private String cliente;
	private String descQualidade;
	private String indQualidade;
	private String exprQualidade;
	private String freqQualidade;
	private String metaQualidade;
	private String descCusto;
	private String indCusto;
	private String exprCusto;
	private String freqCusto;
	private String metaCusto;
	private String descEntrega;
	private String indEntrega;
	private String exprEntrega;
	private String freqEntrega;
	private String metaEntrega;
	private String descSeguranca;
	private String indSeguranca;
	private String exprSeguranca;
	private String freqSeguranca;
	private String metaSeguranca;	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_mapanegocio")
	public Integer getId() {
		return id;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}

	@MaxLength(1500)
	public String getFornecedores() {
		return fornecedores;
	}
	
	@MaxLength(1500)
	public String getInsumos() {
		return insumos;
	}
	
	@MaxLength(1500)
	@DisplayName("Negócio")
	public String getNegocio() {
		return negocio;
	}
	
	@MaxLength(1500)
	public String getPessoal() {
		return pessoal;
	}
	
	@MaxLength(1500)
	public String getEquipamentos() {
		return equipamentos;
	}
	
	@MaxLength(1500)
	public String getProdutos() {
		return produtos;
	}
	
	@MaxLength(1500)
	public String getClientes() {
		return clientes;
	}
	
	@MaxLength(1500)
	@DisplayName("Missão")
	public String getMissao() {
		return missao;
	}
	
	@MaxLength(200)	
	@DisplayName("Produto")
	public String getProduto() {
		return produto;
	}
	
	@DisplayName("Cliente")
	@MaxLength(200)
	public String getCliente() {
		return cliente;
	}
	
	@DisplayName("Descrição")
	@MaxLength(200)
	public String getDescQualidade() {
		return descQualidade;
	}
	
	@DisplayName("Indicador de gestão")	
	@MaxLength(200)
	public String getIndQualidade() {
		return indQualidade;
	}
	
	@DisplayName("Como expressar o Ind.Gestão")	
	@MaxLength(200)
	public String getExprQualidade() {
		return exprQualidade;
	}
	
	@DisplayName("Frequência de medição do IG")	
	@MaxLength(200)
	public String getFreqQualidade() {
		return freqQualidade;
	}
	
	@DisplayName("Meta")	
	@MaxLength(200)
	public String getMetaQualidade() {
		return metaQualidade;
	}
	
	@DisplayName("Descrição")	
	@MaxLength(200)
	public String getDescCusto() {
		return descCusto;
	}
	
	@MaxLength(200)
	@DisplayName("Indicador de gestão")
	public String getIndCusto() {
		return indCusto;
	}
	
	@DisplayName("Como expressar o Ind.Gestão")
	@MaxLength(200)
	public String getExprCusto() {
		return exprCusto;
	}
	
	@DisplayName("Frequência de medição do IG")	
	@MaxLength(200)
	public String getFreqCusto() {
		return freqCusto;
	}
	
	@DisplayName("Meta")	
	@MaxLength(200)
	public String getMetaCusto() {
		return metaCusto;
	}
	
	@DisplayName("Descrição")	
	@MaxLength(200)
	public String getDescEntrega() {
		return descEntrega;
	}
	
	@DisplayName("Indicador de gestão")	
	@MaxLength(200)
	public String getIndEntrega() {
		return indEntrega;
	}
	
	@MaxLength(200)
	@DisplayName("Como expressar o Ind.Gestão")		
	public String getExprEntrega() {
		return exprEntrega;
	}
	
	@DisplayName("Frequência de medição do IG")		
	@MaxLength(200)
	public String getFreqEntrega() {
		return freqEntrega;
	}
	
	@DisplayName("Meta")		
	@MaxLength(200)
	public String getMetaEntrega() {
		return metaEntrega;
	}
	
	@DisplayName("Descrição")		
	@MaxLength(200)
	public String getDescSeguranca() {
		return descSeguranca;
	}
	
	@DisplayName("Indicador de gestão")	
	@MaxLength(200)
	public String getIndSeguranca() {
		return indSeguranca;
	}
	
	@DisplayName("Como expressar o Ind.Gestão")	
	@MaxLength(200)
	public String getExprSeguranca() {
		return exprSeguranca;
	}
	
	@DisplayName("Frequência de medição do IG")	
	@MaxLength(200)
	public String getFreqSeguranca() {
		return freqSeguranca;
	}
	
	@DisplayName("Meta")	
	@MaxLength(200)
	public String getMetaSeguranca() {
		return metaSeguranca;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}

	public void setFornecedores(String fornecedores) {
		this.fornecedores = fornecedores;
	}

	public void setInsumos(String insumos) {
		this.insumos = insumos;
	}

	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}

	public void setPessoal(String pessoal) {
		this.pessoal = pessoal;
	}

	public void setEquipamentos(String equipamentos) {
		this.equipamentos = equipamentos;
	}

	public void setProdutos(String produtos) {
		this.produtos = produtos;
	}

	public void setClientes(String clientes) {
		this.clientes = clientes;
	}

	public void setMissao(String missao) {
		this.missao = missao;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public void setDescQualidade(String descQualidade) {
		this.descQualidade = descQualidade;
	}

	public void setIndQualidade(String indQualidade) {
		this.indQualidade = indQualidade;
	}

	public void setExprQualidade(String exprQualidade) {
		this.exprQualidade = exprQualidade;
	}

	public void setFreqQualidade(String freqQualidade) {
		this.freqQualidade = freqQualidade;
	}

	public void setMetaQualidade(String metaQualidade) {
		this.metaQualidade = metaQualidade;
	}

	public void setDescCusto(String descCusto) {
		this.descCusto = descCusto;
	}

	public void setIndCusto(String indCusto) {
		this.indCusto = indCusto;
	}

	public void setExprCusto(String exprCusto) {
		this.exprCusto = exprCusto;
	}

	public void setFreqCusto(String freqCusto) {
		this.freqCusto = freqCusto;
	}

	public void setMetaCusto(String metaCusto) {
		this.metaCusto = metaCusto;
	}

	public void setDescEntrega(String descEntrega) {
		this.descEntrega = descEntrega;
	}

	public void setIndEntrega(String indEntrega) {
		this.indEntrega = indEntrega;
	}

	public void setExprEntrega(String exprEntrega) {
		this.exprEntrega = exprEntrega;
	}

	public void setFreqEntrega(String freqEntrega) {
		this.freqEntrega = freqEntrega;
	}

	public void setMetaEntrega(String metaEntrega) {
		this.metaEntrega = metaEntrega;
	}

	public void setDescSeguranca(String descSeguranca) {
		this.descSeguranca = descSeguranca;
	}

	public void setIndSeguranca(String indSeguranca) {
		this.indSeguranca = indSeguranca;
	}

	public void setExprSeguranca(String exprSeguranca) {
		this.exprSeguranca = exprSeguranca;
	}

	public void setFreqSeguranca(String freqSeguranca) {
		this.freqSeguranca = freqSeguranca;
	}

	public void setMetaSeguranca(String metaSeguranca) {
		this.metaSeguranca = metaSeguranca;
	}	

}
