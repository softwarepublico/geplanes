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
package br.com.linkcom.sgm.beans.DTO;

public class ApresentacaoResultadosReportDTO {

	Integer indicadorid;
	String indicador;
	String data;
	String epoca;	
	String valorLimiteInferior;
	String valorLimiteSuperior;
	String valorReal;
	String percentual;
	String farol;
	String numFarois;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getEpoca() {
		return epoca;
	}
	public void setEpoca(String epoca) {
		this.epoca = epoca;
	}
	public String getFarol() {
		return farol;
	}
	public void setFarol(String farol) {
		this.farol = farol;
	}
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	public Integer getIndicadorid() {
		return indicadorid;
	}
	public void setIndicadorid(Integer indicadorid) {
		this.indicadorid = indicadorid;
	}
	public String getNumFarois() {
		return numFarois;
	}
	public void setNumFarois(String numFarois) {
		this.numFarois = numFarois;
	}
	public String getPercentual() {
		return percentual;
	}
	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}
	public String getValorReal() {
		return valorReal;
	}
	public void setValorReal(String valorReal) {
		this.valorReal = valorReal;
	}
	public String getValorLimiteInferior() {
		return valorLimiteInferior;
	}
	public void setValorLimiteInferior(String valorLimiteInferior) {
		this.valorLimiteInferior = valorLimiteInferior;
	}
	public String getValorLimiteSuperior() {
		return valorLimiteSuperior;
	}
	public void setValorLimiteSuperior(String valorLimiteSuperior) {
		this.valorLimiteSuperior = valorLimiteSuperior;
	}
}
