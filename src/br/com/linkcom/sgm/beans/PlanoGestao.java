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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@SequenceGenerator(name = "sq_planogestao", sequenceName = "sq_planogestao")
public class PlanoGestao {
	
	private Integer id;
	
	private String descricao;
	private Integer anoExercicio;
	private Date limiteCriacaoMetasIndicadores;
	private Date limiteCriacaoMapaNegocio;	
	private Date limiteCriacaoMapaEstrategico;	
	private Date limiteCriacaoMapaCompetencia;	
	private Date limiteCriacaoMatrizFcs;	
	private Boolean lembreteCriacaoMetasIndicadores;
	
	/* Datas-limite para o lançamento de resultados por trimestre. 
	 * A partir de cada uma dessas datas, os responsáveis por cada UG 
	 * onde tenha algum indicador com resultado faltante serão notificados 
	 * através de email. 
	 * Os emails serão enviados com uma periodicidade definida através
	 * do parâmetro 'diasLembreteLancamentoValoresReais' até a respectiva
	 * data de travamento do lançamento de resultados, a partir da qual 
	 * somente os administradores poderão lançar / alterar os resultados */
	private Date dtLimLancRes1T;
	private Date dtLimLancRes2T;
	private Date dtLimLancRes3T;
	private Date dtLimLancRes4T;
	
	/* Datas para o travamento do lançamento de resultados por trimestre. 
	 * A partir de cada uma dessas datas, com exceção dos administradores,
	 * os usuários ficarão impedidos de lançar / alterar os resultados. */	
	private Date dtTravLancRes1T;
	private Date dtTravLancRes2T;
	private Date dtTravLancRes3T;
	private Date dtTravLancRes4T;
	
	private List<UnidadeGerencial> unidadesGerenciais = new ArrayList<UnidadeGerencial>();	
	
	public PlanoGestao() {
	}
	
	public PlanoGestao(Integer id) {
		this.id = id;
	}
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_planogestao")
	public Integer getId() {
		return id;
	}
	
	@DescriptionProperty
	@DisplayName("Ano do exercício")
	@Required
	@MaxLength(4)
	public Integer getAnoExercicio() {
		return anoExercicio;
	}
	
	@DisplayName("Descrição")
	@MaxLength(500)
	public String getDescricao() {
		return descricao;
	}
	
	@DisplayName("Data limite para criação de indicadores/iniciativas/planos de ação")
	@Required
	public Date getLimiteCriacaoMetasIndicadores() {
		return limiteCriacaoMetasIndicadores;
	}
	@DisplayName("Data limite para lançamento de " + Nomes.valores_reais + " - 1ºT")
	@Required	
	public Date getDtLimLancRes1T() {
		return dtLimLancRes1T;
	}
	@DisplayName("Data limite para lançamento de " + Nomes.valores_reais + " - 2ºT")
	@Required
	public Date getDtLimLancRes2T() {
		return dtLimLancRes2T;
	}
	@DisplayName("Data limite para lançamento de " + Nomes.valores_reais + " - 3ºT")
	@Required
	public Date getDtLimLancRes3T() {
		return dtLimLancRes3T;
	}
	@DisplayName("Data limite para lançamento de " + Nomes.valores_reais + " - 4ºT")
	@Required
	public Date getDtLimLancRes4T() {
		return dtLimLancRes4T;
	}
	@DisplayName("Data para travamento do lançamento de " + Nomes.valores_reais + " - 1ºT")
	@Required
	public Date getDtTravLancRes1T() {
		return dtTravLancRes1T;
	}
	@DisplayName("Data para travamento do lançamento de " + Nomes.valores_reais + " - 2ºT")
	@Required
	public Date getDtTravLancRes2T() {
		return dtTravLancRes2T;
	}
	@DisplayName("Data para travamento do lançamento de " + Nomes.valores_reais + " - 3ºT")
	@Required
	public Date getDtTravLancRes3T() {
		return dtTravLancRes3T;
	}
	@DisplayName("Data para travamento do lançamento de " + Nomes.valores_reais + " - 4ºT")
	@Required
	public Date getDtTravLancRes4T() {
		return dtTravLancRes4T;
	}
	@DisplayName("Data limite para criação do mapa do negócio")
	@Required	
	public Date getLimiteCriacaoMapaNegocio() {
		return limiteCriacaoMapaNegocio;
	}
	@DisplayName("Lembrete de data limite para criação de indicadores enviado")
	public Boolean getLembreteCriacaoMetasIndicadores() {
		return lembreteCriacaoMetasIndicadores;
	}

	@OneToMany(mappedBy="planoGestao")
	public List<UnidadeGerencial> getUnidadesGerenciais() {
		return unidadesGerenciais;
	}
	@DisplayName("Data limite para criação do mapa estratégico")
	@Required
	public Date getLimiteCriacaoMapaEstrategico() {
		return limiteCriacaoMapaEstrategico;
	}
	@DisplayName("Data limite para criação do mapa de competências")
	@Required
	public Date getLimiteCriacaoMapaCompetencia() {
		return limiteCriacaoMapaCompetencia;
	}
	@DisplayName("Data limite para criação da matriz de iniciativas x fcs")
	@Required
	public Date getLimiteCriacaoMatrizFcs() {
		return limiteCriacaoMatrizFcs;
	}
	public void setAnoExercicio(Integer anoExercicio) {
		this.anoExercicio = anoExercicio;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setLimiteCriacaoMetasIndicadores(
			Date limiteCriacaoMetasIndicadores) {
		this.limiteCriacaoMetasIndicadores = limiteCriacaoMetasIndicadores;
	}
	public void setDtLimLancRes1T(Date dtLimLancRes1T) {
		this.dtLimLancRes1T = dtLimLancRes1T;
	}
	public void setDtLimLancRes2T(Date dtLimLancRes2T) {
		this.dtLimLancRes2T = dtLimLancRes2T;
	}
	public void setDtLimLancRes3T(Date dtLimLancRes3T) {
		this.dtLimLancRes3T = dtLimLancRes3T;
	}
	public void setDtLimLancRes4T(Date dtLimLancRes4T) {
		this.dtLimLancRes4T = dtLimLancRes4T;
	}
	public void setDtTravLancRes1T(Date dtTravLancRes1T) {
		this.dtTravLancRes1T = dtTravLancRes1T;
	}
	public void setDtTravLancRes2T(Date dtTravLancRes2T) {
		this.dtTravLancRes2T = dtTravLancRes2T;
	}
	public void setDtTravLancRes3T(Date dtTravLancRes3T) {
		this.dtTravLancRes3T = dtTravLancRes3T;
	}
	public void setDtTravLancRes4T(Date dtTravLancRes4T) {
		this.dtTravLancRes4T = dtTravLancRes4T;
	}
	public void setLimiteCriacaoMapaNegocio(Date limiteCriacaoMapaNegocio) {
		this.limiteCriacaoMapaNegocio = limiteCriacaoMapaNegocio;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setLembreteCriacaoMetasIndicadores(
			Boolean lembreteCriacaoMetasIndicadores) {
		this.lembreteCriacaoMetasIndicadores = lembreteCriacaoMetasIndicadores;
	}
	public void setUnidadesGerenciais(List<UnidadeGerencial> unidadesGerenciais) {
		this.unidadesGerenciais = unidadesGerenciais;
	}
	public void setLimiteCriacaoMapaEstrategico(Date limiteCriacaoMapaEstrategico) {
		this.limiteCriacaoMapaEstrategico = limiteCriacaoMapaEstrategico;
	}
	public void setLimiteCriacaoMapaCompetencia(Date limiteCriacaoMapaCompetencia) {
		this.limiteCriacaoMapaCompetencia = limiteCriacaoMapaCompetencia;
	}
	public void setLimiteCriacaoMatrizFcs(Date limiteCriacaoMatrizFcs) {
		this.limiteCriacaoMatrizFcs = limiteCriacaoMatrizFcs;
	}	

	
	//=========================Transientes==================================//
	private Boolean copia;
	
	@Transient
	public Boolean getCopia() {
		return copia;
	}
	
	public void setCopia(Boolean copia) {
		this.copia = copia;
	}
}
