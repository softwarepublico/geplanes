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


public class PainelControleReportItemDTO {

	private Integer idUnidadeGerencial;
	private Integer idMeta;
	private Integer idMedida;
	private Integer idArea;
	private Integer idIndicador;

	
	private String unidadeGerencial;
	private String meta;
	private String medida;
	private String area;
	private String indicador;

	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getIdArea() {
		return idArea;
	}
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
	public Integer getIdIndicador() {
		return idIndicador;
	}
	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}
	public Integer getIdMedida() {
		return idMedida;
	}
	public void setIdMedida(Integer idMedida) {
		this.idMedida = idMedida;
	}
	public Integer getIdMeta() {
		return idMeta;
	}
	public void setIdMeta(Integer idMeta) {
		this.idMeta = idMeta;
	}
	public Integer getIdUnidadeGerencial() {
		return idUnidadeGerencial;
	}
	public void setIdUnidadeGerencial(Integer idUnidadeGerencial) {
		this.idUnidadeGerencial = idUnidadeGerencial;
	}
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	public String getMedida() {
		return medida;
	}
	public void setMedida(String medida) {
		this.medida = medida;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public String getUnidadeGerencial() {
		return unidadeGerencial;
	}
	public void setUnidadeGerencial(String unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
}
