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

public class IniciativaPlanoAcaoReportBean {
	
	private String anoGestao;
	private String unidadeGerencial;
	private String objetivoEstrategico;
	private String iniciativa;
	private String textoOque;
	private String textoComo;
	private String textoPorque;
	private String textoQuem;
	private String data;
	private String status;
	public String getAnoGestao() {
		return anoGestao;
	}
	public String getUnidadeGerencial() {
		return unidadeGerencial;
	}
	public String getObjetivoEstrategico() {
		return objetivoEstrategico;
	}
	public String getIniciativa() {
		return iniciativa;
	}
	public String getTextoOque() {
		return textoOque;
	}
	public String getTextoComo() {
		return textoComo;
	}
	public String getTextoPorque() {
		return textoPorque;
	}
	public String getTextoQuem() {
		return textoQuem;
	}
	public String getData() {
		return data;
	}
	public String getStatus() {
		return status;
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
	public void setIniciativa(String iniciativa) {
		this.iniciativa = iniciativa;
	}
	public void setTextoOque(String textoOque) {
		this.textoOque = textoOque;
	}
	public void setTextoComo(String textoComo) {
		this.textoComo = textoComo;
	}
	public void setTextoPorque(String textoPorque) {
		this.textoPorque = textoPorque;
	}
	public void setTextoQuem(String textoQuem) {
		this.textoQuem = textoQuem;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
