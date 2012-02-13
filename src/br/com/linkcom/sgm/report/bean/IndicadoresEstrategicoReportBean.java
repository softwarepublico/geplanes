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
package br.com.linkcom.sgm.report.bean;

public class IndicadoresEstrategicoReportBean {
	String anoGestao;
	String unidadeGerencial;
	String objetivoEstrategico;
	String nome;
	String melhor;
	String unidadeMedida;
	String precisao;
	String tolerancia;
	String frequencia;
	Boolean cancelado;
	String tipoIndicador;
	
	public String getAnoGestao() {
		return anoGestao;
	}
	public String getUnidadeGerencial() {
		return unidadeGerencial;
	}
	public String getObjetivoEstrategico() {
		return objetivoEstrategico;
	}
	public String getNome() {
		return nome;
	}
	public String getMelhor() {
		return melhor;
	}
	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public String getPrecisao() {
		return precisao;
	}
	public String getTolerancia() {
		return tolerancia;
	}
	public String getFrequencia() {
		return frequencia;
	}
	public Boolean getCancelado() {
		return cancelado;
	}
	public String getTipoIndicador() {
		return tipoIndicador;
	}
	public void setTipoIndicador(String tipoIndicador) {
		this.tipoIndicador = tipoIndicador;
	}
	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}
	public void setAnoGestao(String anoGestao) {
		this.anoGestao = anoGestao;
	}
	public void setUnidadeGerencial(String unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setObjetivoEstrategico(String objetivoEstrategico) {
		this.objetivoEstrategico = objetivoEstrategico;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setMelhor(String melhor) {
		this.melhor = melhor;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public void setPrecisao(String precisao) {
		this.precisao = precisao;
	}
	public void setTolerancia(String tolerancia) {
		this.tolerancia = tolerancia;
	}
	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}
}
