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

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.sgm.beans.enumeration.MelhorDoIndicadorEnum;
import br.com.linkcom.sgm.util.GeplanesUtils;

@Entity
@SequenceGenerator(name = "sq_acompanhamentoindicador", sequenceName = "sq_acompanhamentoindicador")
public class AcompanhamentoIndicador {
	
	private Integer id;
	
	private Integer indice;
	private Calendar dataInicial;
	private Calendar dataFinal;
	private Double valorLimiteSuperior;
	private Double valorReal;
	private Double valorLimiteInferior;
	private Date dtLembLancRes;
	
	private Boolean valorBaseOK;
	private Boolean valorRealOK;
	private Anomalia anomalia;
	private AcaoPreventiva acaoPreventiva;
	private Indicador indicador;
	
	// Acompanhamentos com esse campo setado serão desconsiderados
	private Boolean naoaplicavel;

	
	public AcompanhamentoIndicador(){
		
	}	

	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_acompanhamentoindicador")
	public Integer getId() {
		return id;
	}
	public Double getValorLimiteInferior() {
		return valorLimiteInferior;
	}
	public Double getValorLimiteSuperior() {
		return valorLimiteSuperior;
	}
	public Double getValorReal() {
		return valorReal;
	}
	public Calendar getDataFinal() {
		return dataFinal;
	}
	public Calendar getDataInicial() {
		return dataInicial;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public Anomalia getAnomalia() {
		return anomalia;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public AcaoPreventiva getAcaoPreventiva() {
		return acaoPreventiva;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public Indicador getIndicador() {
		return indicador;
	}
	public Date getDtLembLancRes() {
		return dtLembLancRes;
	}
	public Integer getIndice() {
		return indice;
	}
	@DisplayName("Base OK")
	public Boolean getValorBaseOK() {
		return valorBaseOK;
	}
	@DisplayName("Real OK")
	public Boolean getValorRealOK() {
		return valorRealOK;
	}
	@DisplayName("Não aplicável")
	public Boolean getNaoaplicavel() {
		return naoaplicavel;
	}

	public void setValorBaseOK(Boolean valorBaseOK) {
		this.valorBaseOK = valorBaseOK;
	}
	public void setValorRealOK(Boolean valorRealOK) {
		this.valorRealOK = valorRealOK;
	}
	public void setNaoaplicavel(Boolean naoaplicavel) {
		this.naoaplicavel = naoaplicavel;
	}
	public void setIndice(Integer indice) {
		this.indice = indice;
	}
	public void setDtLembLancRes(Date dtLembLancRes) {
		this.dtLembLancRes = dtLembLancRes;
	}
	public void setAnomalia(Anomalia anomalia) {
		this.anomalia = anomalia;
	}
	public void setAcaoPreventiva(AcaoPreventiva acaoPreventiva) {
		this.acaoPreventiva = acaoPreventiva;
	}
	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
	public void setDataFinal(Calendar dataFinal) {
		this.dataFinal = dataFinal;
	}
	public void setDataInicial(Calendar dataInicial) {
		this.dataInicial = dataInicial;
	}
	public void setValorLimiteInferior(Double valoLimiteInferior) {
		this.valorLimiteInferior = valoLimiteInferior;
	}
	public void setValorLimiteSuperior(Double valorLimiteSuperior) {
		this.valorLimiteSuperior = valorLimiteSuperior;
	}
	public void setValorReal(Double valorReal) {
		this.valorReal = valorReal;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	//////////////////////////////// TRANSIENTE ////////////////////////////////
	
	private Double percentualReal;
	private Double percentualTolerancia;
	private	Double valorRealAcumulado;
	private Double valorLimiteSuperiorAcumulado;
	private Double valorLimiteInferiorAcumulado;
	private Integer farol;
	private Set<Usuario> usuariosFaltantes = new HashSet<Usuario>();
	private List<Anomalia> anomaliasUsuarios = new ArrayList<Anomalia>();
	private Boolean podeMostrar;
	private Boolean podeAlterar;
	private String data;
	private String epoca;
	private boolean farolPequeno = false;
	private MelhorDoIndicadorEnum melhor;
	
	@Transient
	public boolean isFarolPequeno() {
		return farolPequeno;
	}

	public void setFarolPequeno(boolean farolPequeno) {
		this.farolPequeno = farolPequeno;
	}

	@Transient
	public String getData() {
		return data;
	}
	@Transient
	public List<Anomalia> getAnomaliasUsuarios() {
		return anomaliasUsuarios;
	}	
	@Transient
	public Double getPercentualTolerancia(){
		return percentualTolerancia;
	}	
	
	/**
	 * Retorna o percentual real de aproveitamento
	 * 
	 * Se o resultado for abaixo de 0 é retornado 0
	 * Se o resultado for acima de 1 é retornado 1
	 * 
	 * Isso porque o aproveitamento em percentual deve ficar entre 0 e 100
	 */
	@Transient
	public Double getPercentualReal() {
		
		double resultado;
		Double valorReal = getValorReal();		
		Double valorLimiteSuperior = getValorLimiteSuperior();
		Double valorLimiteInferior = getValorLimiteInferior();
		
		if (percentualReal == null || percentualReal.equals(Double.NaN)) {
			// Consistência dos atributos
			if (valorReal == null) {
				percentualReal = null;
				return percentualReal;
			}
			
			// MELHOR_CIMA			
			if (getIndicador().getMelhor().equals(MelhorDoIndicadorEnum.MELHOR_CIMA)) {
				if (valorLimiteSuperior == 0) {
					if (valorReal >= 0) {
						resultado = 100;
					}
					else {
						resultado = 0;
					}
				}
				else {
					resultado = valorReal / valorLimiteSuperior;
				}
			}
			
			// MELHOR_BAIXO			
			else if (getIndicador().getMelhor().equals(MelhorDoIndicadorEnum.MELHOR_BAIXO)) {
				if (valorLimiteInferior == 0) {
					if (valorReal <= 0) {
						resultado = 1;
					}
					else {
						resultado = 0;
					}
				}
				else {
					resultado = (1 - (valorReal / valorLimiteInferior)) + 1;
				}
			}			
			
			// MELHOR_ENTRE_FAIXAS
			else if (getIndicador().getMelhor().equals(MelhorDoIndicadorEnum.MELHOR_ENTRE_FAIXAS)) {
				// Verifica se o valor real está dentro da faixa esperada
				if (valorReal >= valorLimiteInferior && valorReal <= valorLimiteSuperior) {
					resultado = 1;
				}
				else {
					// Valor real não atingiu o limite inferior
					if (valorReal < valorLimiteInferior) {
						resultado = 1 - ((valorLimiteInferior - valorReal) / (valorLimiteSuperior - valorLimiteInferior));
					}
					// Valor real excedeu o limite superior
					else {
						resultado = 1 - ((valorReal - valorLimiteSuperior) / (valorLimiteSuperior - valorLimiteInferior));
					}
				}
			}
			
			else {				
				resultado = 0;
			}			
			
			//Converte para porcentagem
			resultado *= 100;
			
			//Arredonda o resultado de acordo com a precisão
			if (getIndicador().getPrecisao() != null) {
				resultado = GeplanesUtils.round(resultado,getIndicador().getPrecisao());
			}
			
			//Corta os excessos
			if (resultado < 0) {
				resultado = 0;
			}
			if (resultado > 100) {
				resultado = 100;
			}
			percentualReal = resultado;
		}

		return percentualReal;
	}	
	@Transient
	/**
	 * Retorna o farol adequado
	 * 0 - Branco
	 * 1 - Vermelho
	 * 2 - Amarelo
	 * 3 - Verde
	 * 4 - Cinza
	 */
	public Integer getFarol() {
		if (farol == null) {
			Double percentualTolerancia = getPercentualTolerancia();
			Double percentualReal = getPercentualReal();
			
			// CONSISTÊNCIA DOS VALORES
			
			// FAROL CINZA
			if (Boolean.TRUE.equals(getNaoaplicavel())) {
				farol =  4;
				return farol;
			}			
			
			// FAROL BRANCO
			if (percentualReal == null) {
				farol =  0;
				return farol;
			}
			
			// FAROL VERDE
			if (percentualReal >= 100) {
				farol =  3;
			} 
			
			// FAROL AMARELO
			else if (percentualReal >= 100.0 - percentualTolerancia) {
				farol =  2;
			} 
			
			// FAROL VERMELHO
			else {
				farol =  1;	
			}
			
		}
		return farol;
	}
	
	@Transient
	public String getCorFarol(){
		switch(getFarol()){
			case 0: return "branca" + (farolPequeno? "-pq" : "");
			case 1: return "vermelha" + (farolPequeno? "-pq" : "");
			case 2: return "amarela" + (farolPequeno? "-pq" : "");
			case 3: return "verde" + (farolPequeno? "-pq" : "");
			case 4: return "cinza" + (farolPequeno? "-pq" : "");
			default: return "preta" + (farolPequeno? "-pq" : "");
		}
	}
	@Transient
	public String getEpoca() {
		return epoca;
	}
	@Transient
	public Set<Usuario> getUsuariosFaltantes() {
		return usuariosFaltantes;
	}
	@Transient
	public Double getValorRealAcumulado() {
		return valorRealAcumulado;
	}	
	@Transient
	public Double getValorLimiteSuperiorAcumulado() {
		return valorLimiteSuperiorAcumulado;
	}
	@Transient
	public Double getValorLimiteInferiorAcumulado() {
		return valorLimiteInferiorAcumulado;
	}
	@Transient
	public Boolean getPodeAlterar() {
		return podeAlterar;
	}
	
	@Transient
	public Boolean getPodeMostrar() {
		return podeMostrar;
	}
	
	public void setEpoca(String epoca) {
		this.epoca = epoca;
	}
	public void setUsuariosFaltantes(Set<Usuario> usuariosFaltantes) {
		this.usuariosFaltantes = usuariosFaltantes;
	}
	public void setPodeMostrar(Boolean podeMostrar) {
		this.podeMostrar = podeMostrar;
	}
	public void setPodeAlterar(Boolean podeAlterar) {
		this.podeAlterar = podeAlterar;
	}
	public void setPercentualReal(Double percentualReal) {
		this.percentualReal = percentualReal;
	}
	public void setPercentualTolerancia(Double percentualTolerancia) {
		this.percentualTolerancia = percentualTolerancia;
	}	
	public void setValorRealAcumulado(Double valorRealAcumulado) {
		this.valorRealAcumulado = valorRealAcumulado;
	}	
	public void setValorLimiteSuperiorAcumulado(Double valorLimiteSuperiorAcumulado) {
		this.valorLimiteSuperiorAcumulado = valorLimiteSuperiorAcumulado;
	}
	public void setValorLimiteInferiorAcumulado(Double valorLimiteInferiorAcumulado) {
		this.valorLimiteInferiorAcumulado = valorLimiteInferiorAcumulado;
	}
	public void setFarol(Integer farol) {
		this.farol = farol;
	}
	public void setAnomaliasUsuarios(List<Anomalia> anomaliasUsuarios) {
		this.anomaliasUsuarios = anomaliasUsuarios;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	//========================== as String ======================================
	
	private String valorLimiteSuperiorAsString;
	private String valorRealAsString;
	private String valorLimiteInferiorAsString;
	private String valorLimiteSuperiorAcumuladoAsString;
	private String valorLimiteInferiorAcumuladoAsString;
	private String valorRealAcumuladoAsString;
	
	@Transient
	public String getValorLimiteSuperiorAsString() {
		if (valorLimiteSuperiorAsString == null) {
			if (valorLimiteSuperior != null) {
				return new DecimalFormat("#,##0.#####").format(valorLimiteSuperior);
			}
		}
		return valorLimiteSuperiorAsString;
	}
	@Transient
	public String getValorRealAsString() {
		if (valorRealAsString == null) {
			if (valorReal != null) {
				return new DecimalFormat("#,##0.#####").format(valorReal);
			}
		}
		return valorRealAsString;
	}
	@Transient
	public String getValorLimiteInferiorAsString() {
		if (valorLimiteInferiorAsString == null) {
			if (valorLimiteInferior != null) {
				return new DecimalFormat("#,##0.#####").format(valorLimiteInferior);
			}
		}
		return valorLimiteInferiorAsString;
	}
	@Transient
	public String getValorLimiteSuperiorAcumuladoAsString() {
		if (valorLimiteSuperiorAcumuladoAsString == null) {
			if (valorLimiteSuperiorAcumulado != null) {
				return new DecimalFormat("#,##0.#####").format(valorLimiteSuperiorAcumulado);
			}
		}
		return valorLimiteSuperiorAcumuladoAsString;
	}
	@Transient
	public String getValorLimiteInferiorAcumuladoAsString() {
		if (valorLimiteInferiorAcumuladoAsString == null) {
			if (valorLimiteInferiorAcumulado != null) {
				return new DecimalFormat("#,##0.#####").format(valorLimiteInferiorAcumulado);
			}
		}
		return valorLimiteInferiorAcumuladoAsString;
	}
	@Transient
	public String getValorRealAcumuladoAsString() {
		if (valorRealAcumuladoAsString == null) {
			if (valorRealAcumulado != null) {
				return new DecimalFormat("#,##0.#####").format(valorRealAcumulado);
			}
		}
		return valorRealAcumuladoAsString;
	}
	@Transient
	public MelhorDoIndicadorEnum getMelhor() {
		return melhor;
	}

	public void setValorLimiteSuperiorAcumuladoAsString(String valorLimiteSuperiorAcumuladoAsString) {
		this.valorLimiteSuperiorAcumuladoAsString = valorLimiteSuperiorAcumuladoAsString;
	}

	public void setValorLimiteInferiorAcumuladoAsString(String valorLimiteInferiorAcumuladoAsString) {
		this.valorLimiteInferiorAcumuladoAsString = valorLimiteInferiorAcumuladoAsString;
	}
	
	public void setValorLimiteSuperiorAsString(String valorLimiteSuperiorAsString) {
		this.valorLimiteSuperiorAsString = valorLimiteSuperiorAsString;
	}
	
	public void setValorLimiteInferiorAsString(String valorLimiteInferiorAsString) {
		this.valorLimiteInferiorAsString = valorLimiteInferiorAsString;
	}

	public void setValorRealAcumuladoAsString(String valorRealAcumuladoAsString) {
		this.valorRealAcumuladoAsString = valorRealAcumuladoAsString;
	}

	public void setValorRealAsString(String valorRealAsString) {
		this.valorRealAsString = valorRealAsString;
	}
	public void setMelhor(MelhorDoIndicadorEnum melhor) {
		this.melhor = melhor;
	}

}