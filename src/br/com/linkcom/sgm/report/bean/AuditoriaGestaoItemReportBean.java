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


public class AuditoriaGestaoItemReportBean {
	protected Integer idIndicador;
	protected String nomeIndicador;
	protected String nomeItemAuditoria;
	protected String descItemAuditoria;
	protected Integer valorItemAvaliacao;
	protected String descItemAvaliacao;
	protected String descAvaliacao;
	
	public Integer getIdIndicador() {
		return idIndicador;
	}
	
	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}
	
	public String getNomeIndicador() {
		return nomeIndicador;
	}
	
	public void setNomeIndicador(String nomeIndicador) {
		this.nomeIndicador = nomeIndicador;
	}
	
	public String getNomeItemAuditoria() {
		return nomeItemAuditoria;
	}
	public void setNomeItemAuditoria(String nomeItemAuditoria) {
		this.nomeItemAuditoria = nomeItemAuditoria;
	}
	
	public String getDescItemAuditoria() {
		return descItemAuditoria;
	}
	
	public void setDescItemAuditoria(String descItemAuditoria) {
		this.descItemAuditoria = descItemAuditoria;
	}
	
	public Integer getValorItemAvaliacao() {
		return valorItemAvaliacao;
	}
	
	public void setValorItemAvaliacao(Integer valorItemAvaliacao) {
		this.valorItemAvaliacao = valorItemAvaliacao;
	}
	
	public String getDescItemAvaliacao() {
		return descItemAvaliacao;
	}
	
	public void setDescItemAvaliacao(String descItemAvaliacao) {
		this.descItemAvaliacao = descItemAvaliacao;
	}
	
	public String getDescAvaliacao() {
		return descAvaliacao;
	}
	
	public void setDescAvaliacao(String descAvaliacao) {
		this.descAvaliacao = descAvaliacao;
	}	
}
