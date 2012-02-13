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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.validator.Max;
import org.hibernate.validator.Min;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaAcompanhamentoIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.MelhorDoIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.RelevanciaIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;

@SuppressWarnings("unchecked")
@Entity
@SequenceGenerator(name = "sq_indicador", sequenceName = "sq_indicador")
public class Indicador implements Hierarquizavel<Hierarquizavel, Indicador>{
	
	private Integer id;
	
	private String nome;
	private String descricao;
	private Double peso;
	private Integer precisao;
	private Double tolerancia;
	
	private UnidadeGerencial unidadeGerencial;
	private MelhorDoIndicadorEnum melhor;
	private FrequenciaIndicadorEnum frequencia;
	private UnidadeMedida unidadeMedida;
	private StatusIndicadorEnum status;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	
	private String responsavel;
	private RelevanciaIndicadorEnum relevancia;
	private FrequenciaAcompanhamentoIndicadorEnum frequenciaAcompanhamento;
	private String mecanismoControle;
	private String fonteDados;
	private String formulaCalculo;
	
	private Set<AcompanhamentoIndicador> acompanhamentosIndicador = new ListSet<AcompanhamentoIndicador>(AcompanhamentoIndicador.class);
	private Set<AnexoIndicador> anexosIndicador = new ListSet<AnexoIndicador>(AnexoIndicador.class);
	
	//=========================Construtor================================//
	
	public Indicador(){
	}
	
	public Indicador(Integer id){
		this.id = id;
	}
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_indicador")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@DisplayName("Descrição")
	@MaxLength(1500)
	@Required
	public String getDescricao() {
		return descricao;
	}
	
	@Required
	@DisplayName("Frequência de lançamento")
	public FrequenciaIndicadorEnum getFrequencia() {
		return frequencia;
	}
	
	@Required
	@DisplayName("Polaridade")
	public MelhorDoIndicadorEnum getMelhor() {
		return melhor;
	}
	
	@Required
	@DescriptionProperty
	@MaxLength(80)
	public String getNome() {
		return nome;
	}
	
	@Required
	public Double getPeso() {
		return peso;
	}
	
	@Required
	@DisplayName("Casas decimais")
	public Integer getPrecisao() {
		return precisao;
	}
	
	@Required
	@DisplayName("Tolerância(%)")
	@Max(100)
	@Min(0)
	public Double getTolerancia() {
		return tolerancia;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Objetivo Estratégico")
	public ObjetivoMapaEstrategico getObjetivoMapaEstrategico() {
		return objetivoMapaEstrategico;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName("Unidade de medida")
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}
	
	@OneToMany(mappedBy="indicador")
	public Set<AcompanhamentoIndicador> getAcompanhamentosIndicador() {
		return acompanhamentosIndicador;
	}

	@OneToMany(mappedBy="indicador")
	@DisplayName("Anexos")
	public Set<AnexoIndicador> getAnexosIndicador() {
		return anexosIndicador;
	}
	
	public StatusIndicadorEnum getStatus() {
		return status;
	}
	
	@Required
	@MaxLength(100)
	@DisplayName("Responsável")
	public String getResponsavel() {
		return responsavel;
	}

	@Required
	@DisplayName("Relevância")	
	public RelevanciaIndicadorEnum getRelevancia() {
		return relevancia;
	}

	@Required
	@DisplayName("Frequência de acompanhamento")	
	public FrequenciaAcompanhamentoIndicadorEnum getFrequenciaAcompanhamento() {
		return frequenciaAcompanhamento;
	}

	@Required
	@MaxLength(500)
	@DisplayName("Mecanismo de controle")	
	public String getMecanismoControle() {
		return mecanismoControle;
	}

	@Required
	@MaxLength(500)
	@DisplayName("Fonte dos dados")	
	public String getFonteDados() {
		return fonteDados;
	}

	@Required
	@MaxLength(1500)
	@DisplayName("Fórmula do cálculo")	
	public String getFormulaCalculo() {
		return formulaCalculo;
	}
	
	public void setObjetivoMapaEstrategico(ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		this.objetivoMapaEstrategico = objetivoMapaEstrategico;
	}
	
	public void setAcompanhamentosIndicador(
			Set<AcompanhamentoIndicador> acompanhamentosIndicador) {
		this.acompanhamentosIndicador = acompanhamentosIndicador;
	}
	
	public void setAnexosIndicador(Set<AnexoIndicador> anexosIndicador) {
		this.anexosIndicador = anexosIndicador;
	}
	
	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setFrequencia(FrequenciaIndicadorEnum frequencia) {
		this.frequencia = frequencia;
	}
	
	public void setMelhor(MelhorDoIndicadorEnum melhor) {
		this.melhor = melhor;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	public void setPrecisao(Integer precisao) {
		this.precisao = precisao;
	}
	
	public void setTolerancia(Double tolerancia) {
		this.tolerancia = tolerancia;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setStatus(StatusIndicadorEnum status) {
		this.status = status;
	}
	
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public void setRelevancia(RelevanciaIndicadorEnum relevancia) {
		this.relevancia = relevancia;
	}

	public void setFrequenciaAcompanhamento(
			FrequenciaAcompanhamentoIndicadorEnum frequenciaAcompanhamento) {
		this.frequenciaAcompanhamento = frequenciaAcompanhamento;
	}

	public void setMecanismoControle(String mecanismoControle) {
		this.mecanismoControle = mecanismoControle;
	}

	public void setFonteDados(String fonteDados) {
		this.fonteDados = fonteDados;
	}

	public void setFormulaCalculo(String formulaCalculo) {
		this.formulaCalculo = formulaCalculo;
	}	
	
	//=============================Transiente============================//
	private boolean podeEditar;
	private int[] numFarois;
	private String idxPerspectivaMapaEstrategico;
	private String idxObjetivoMapaEstrategico;
	private String idxIndicador;
	private Boolean podeCancelar;
	@SuppressWarnings("unused")
	private Boolean detalhamentoOK;
	private Boolean reloadTela;
	private Boolean podeRepactuar;
	private Boolean repactuacaoEmAprovacao;
	
	@Transient
	public Boolean getPodeRepactuar() {
		return podeRepactuar;
	}
	
	@Transient
	public Boolean getRepactuacaoEmAprovacao() {
		return repactuacaoEmAprovacao;
	}
	
	public void setRepactuacaoEmAprovacao(Boolean repactuacaoEmAprovacao) {
		this.repactuacaoEmAprovacao = repactuacaoEmAprovacao;
	}
	
	public void setPodeRepactuar(Boolean podeRepactuar) {
		this.podeRepactuar = podeRepactuar;
	}
	
	@Transient
	public String getIdxIndicador() {
		return idxIndicador;
	}
	
	@Transient
	public String getIdxObjetivoMapaEstrategico() {
		return idxObjetivoMapaEstrategico;
	}
	
	@Transient
	public String getIdxPerspectivaMapaEstrategico() {
		return idxPerspectivaMapaEstrategico;
	}
	
	public void setIdxIndicador(String idxIndicador) {
		this.idxIndicador = idxIndicador;
	}
	
	public void setIdxObjetivoMapaEstrategico(String idxObjetivoMapaEstrategico) {
		this.idxObjetivoMapaEstrategico = idxObjetivoMapaEstrategico;
	}
	
	public void setIdxPerspectivaMapaEstrategico(String idxPerspectivaMapaEstrategico) {
		this.idxPerspectivaMapaEstrategico = idxPerspectivaMapaEstrategico;
	}
	
	@Transient
	public Boolean getReloadTela() {
		return reloadTela;
	}
	
	public void setReloadTela(Boolean reloadTela) {
		this.reloadTela = reloadTela;
	}
	
	@Transient
	public Boolean getPodeCancelar() {
		return podeCancelar;
	}	
	
	@Transient
	public Boolean getEmCancelamento() {
		return this.getStatus() != null && this.getStatus() == StatusIndicadorEnum.EM_CANCELAMENTO;
	}
	
	@Transient
	public Boolean getCancelado() {
		return this.getStatus() != null && this.getStatus() == StatusIndicadorEnum.CANCELADO;
	}	

	@Transient
	public boolean isPodeEditar() {
		return podeEditar;
	}
	
	@Transient
	/**
	 * Retorna a quantidade de faróis para cada indicador
	 * [0] - Branco
	 * [1] - Vermelho
	 * [2] - Amarelo
	 * [3] - Verde
	 * [4] - Cinza
	 */	
	public int[] getNumFarois() {
		int qtdeBranco   = 0;
		int qtdeVermelho = 0;
		int qtdeAmarelo  = 0;
		int qtdeVerde    = 0;
		int qtdeCinza    = 0;
		
		for (AcompanhamentoIndicador acompanhamentoIndicador : getAcompanhamentosIndicador()) {
			switch(acompanhamentoIndicador.getFarol()){
				case 0: 
					qtdeBranco++;
					break;
				case 1: 
					qtdeVermelho++;
					break;
				case 2: 
					qtdeAmarelo++;
					break;
				case 3: 
					qtdeVerde++;
					break;
				case 4: 
					qtdeCinza++;
					break;					
				default: 
					break;
			}
		}
		int[] resultado = {qtdeBranco,qtdeVermelho,qtdeAmarelo,qtdeVerde,qtdeCinza};
		setNumFarois(resultado);
		return numFarois;
	}
	
	@Transient
	public boolean isDetalhamentoOK() {
		return 
			descricao != null && !descricao.equals("") && !descricao.equals("--- Não cadastrado ---") &&
			responsavel != null && !responsavel.equals("") && !responsavel.equals("--- Não cadastrado ---") &&
			relevancia != null &&
			frequenciaAcompanhamento != null &&
			mecanismoControle != null && !mecanismoControle.equals("--- Não cadastrado ---") && !mecanismoControle.equals("") &&
			fonteDados != null && !fonteDados.equals("--- Não cadastrado ---") && !fonteDados.equals("") &&
			formulaCalculo != null && !formulaCalculo.equals("--- Não cadastrado ---") && !formulaCalculo.equals("");
	}
	
	public void setDetalhamentoOK(Boolean detalhamentoOK) {
		this.detalhamentoOK = detalhamentoOK;
	}

	public void setPodeCancelar(Boolean podeCancelar) {
		this.podeCancelar = podeCancelar;
	}
	
	public void setPodeEditar(boolean podeEditar) {
		this.podeEditar = podeEditar;
	}
	
	public void setNumFarois(int[] numFarois) {
		this.numFarois = numFarois;
	}
	
	//=============================Implementacoes============================//
	
	@Override
	public boolean equals(Object obj) {
		return this.getId().intValue() == ((Indicador) obj).getId().intValue();
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	
    //========================= INTERFACE ==================================//
	
	@Transient
	public List<Indicador> getChildren() {
		return null;
	}
	
	@Transient
	public String getTipo() {
		return "ind";
	}
	
	@Transient
	public String getDescricaoCompleta() {
		return 	( getNome() != null ? getNome() : "<SEM NOME>" ) + " (" + 
				( getUnidadeMedida() != null && getUnidadeMedida().getSigla() != null  ? getUnidadeMedida().getSigla() + " - " : "" ) + 
				( getPrecisao() != null  ? getPrecisao() : "" ) + ")";	
	}

	@Transient
	public Hierarquizavel getParent() {
		return getObjetivoMapaEstrategico();
	}

	public void setParent(Hierarquizavel parent) {
		setObjetivoMapaEstrategico((ObjetivoMapaEstrategico) parent);
	}
	
	@Transient
	public Double getPercentualTolerancia() {
		return tolerancia;
	}

}
