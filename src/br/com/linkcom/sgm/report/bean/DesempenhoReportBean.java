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

import java.util.Map;

public class DesempenhoReportBean {
	protected String descObjetivoEstrategico;
	protected Integer idUnidadeGerencial;
	protected String descUnidadeGerencial;
	protected String descMeta;
	protected String descIndicador;
	Map<Integer,Double> mapaPercentualEpoca;
	
	public String getDescObjetivoEstrategico() {
		return descObjetivoEstrategico;
	}
	
	public void setDescObjetivoEstrategico(String descObjetivoEstrategico) {
		this.descObjetivoEstrategico = descObjetivoEstrategico;
	}
	
	public Integer getIdUnidadeGerencial() {
		return idUnidadeGerencial;
	}
	
	public void setIdUnidadeGerencial(Integer idUnidadeGerencial) {
		this.idUnidadeGerencial = idUnidadeGerencial;
	}
	
	public String getDescUnidadeGerencial() {
		return descUnidadeGerencial;
	}
	
	public void setDescUnidadeGerencial(String descUnidadeGerencial) {
		this.descUnidadeGerencial = descUnidadeGerencial;
	}
	
	public String getDescMeta() {
		return descMeta;
	}
	
	public void setDescMeta(String descMeta) {
		this.descMeta = descMeta;
	}
	
	public String getDescIndicador() {
		return descIndicador;
	}
	
	public void setDescIndicador(String descIndicador) {
		this.descIndicador = descIndicador;
	}
	
	public Map<Integer, Double> getMapaPercentualEpoca() {
		return mapaPercentualEpoca;
	}
	
	public void setMapaPercentualEpoca(Map<Integer, Double> mapaPercentualEpoca) {
		this.mapaPercentualEpoca = mapaPercentualEpoca;
	}
}
