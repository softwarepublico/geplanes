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

import java.io.InputStream;
import java.util.List;

public class AuditoriaGestaoReportBean {
	protected String descPlanoGestao;
	protected String descUnidadeGerencial;
	protected String dataAuditoria;
	protected String periodoAvaliado;
	protected String responsavel;
	protected InputStream grafico;
	protected List<AuditoriaGestaoItemReportBean> listaAuditoriaGestaoItemReportBean;
	
	public String getDescPlanoGestao() {
		return descPlanoGestao;
	}
	
	public void setDescPlanoGestao(String descPlanoGestao) {
		this.descPlanoGestao = descPlanoGestao;
	}
	
	public String getDescUnidadeGerencial() {
		return descUnidadeGerencial;
	}
	
	public void setDescUnidadeGerencial(String descUnidadeGerencial) {
		this.descUnidadeGerencial = descUnidadeGerencial;
	}
	
	public String getDataAuditoria() {
		return dataAuditoria;
	}
	
	public void setDataAuditoria(String dataAuditoria) {
		this.dataAuditoria = dataAuditoria;
	}
	
	public String getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	
	public InputStream getGrafico() {
		return grafico;
	}
	
	public void setGrafico(InputStream grafico) {
		this.grafico = grafico;
	}
	
	public String getPeriodoAvaliado() {
		return periodoAvaliado;
	}
	
	public void setPeriodoAvaliado(String periodoAvaliado) {
		this.periodoAvaliado = periodoAvaliado;
	}
	
	public List<AuditoriaGestaoItemReportBean> getListaAuditoriaGestaoItemReportBean() {
		return listaAuditoriaGestaoItemReportBean;
	}
	
	public void setListaAuditoriaGestaoItemReportBean(
			List<AuditoriaGestaoItemReportBean> listaAuditoriaGestaoItemReportBean) {
		this.listaAuditoriaGestaoItemReportBean = listaAuditoriaGestaoItemReportBean;
	}
}
