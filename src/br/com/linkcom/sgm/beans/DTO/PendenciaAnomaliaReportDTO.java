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


public class PendenciaAnomaliaReportDTO{
	private String descricaoObjetivoEstrategico;
	private String descricaoIndicador;
	private String descricaoEpoca;
	private Double percentual;
	
	public String getDescricaoObjetivoEstrategico() {
		return descricaoObjetivoEstrategico;
	}
	
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	
	public String getDescricaoEpoca() {
		return descricaoEpoca;
	}
	
	public Double getPercentual() {
		return percentual;
	}
	
	public void setDescricaoObjetivoEstrategico(String descricaoObjetivoEstrategico) {
		this.descricaoObjetivoEstrategico = descricaoObjetivoEstrategico;
	}
	
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	
	public void setDescricaoEpoca(String descricaoEpoca) {
		this.descricaoEpoca = descricaoEpoca;
	}
	
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
}