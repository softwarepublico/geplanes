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

import java.util.List;

import br.com.linkcom.sgm.beans.ItemAuditoriaInterna;
import br.com.linkcom.sgm.beans.UsuarioAuditoriaInterna;

public class AuditoriaInternaReportBean {
	protected String descUgResponsavel;
	protected String descNorma;
	protected String data;
	protected String observacoes;
	protected List<UsuarioAuditoriaInterna> listaEquipeAuditada;
	protected List<UsuarioAuditoriaInterna> listaEquipeAuditora;
	protected List<ItemAuditoriaInterna> listaNaoConformidades;
	protected List<ItemAuditoriaInterna> listaOutrasNaoConformidades;
	
	public String getDescUgResponsavel() {
		return descUgResponsavel;
	}
	
	public String getDescNorma() {
		return descNorma;
	}
	
	public String getData() {
		return data;
	}
	
	public String getObservacoes() {
		return observacoes;
	}
	
	public List<UsuarioAuditoriaInterna> getListaEquipeAuditada() {
		return listaEquipeAuditada;
	}
	
	public List<UsuarioAuditoriaInterna> getListaEquipeAuditora() {
		return listaEquipeAuditora;
	}
	
	public List<ItemAuditoriaInterna> getListaNaoConformidades() {
		return listaNaoConformidades;
	}
	
	public List<ItemAuditoriaInterna> getListaOutrasNaoConformidades() {
		return listaOutrasNaoConformidades;
	}
	
	public void setDescUgResponsavel(String descUgResponsavel) {
		this.descUgResponsavel = descUgResponsavel;
	}
	
	public void setDescNorma(String descNorma) {
		this.descNorma = descNorma;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	public void setListaEquipeAuditada(List<UsuarioAuditoriaInterna> listaEquipeAuditada) {
		this.listaEquipeAuditada = listaEquipeAuditada;
	}
	
	public void setListaEquipeAuditora(List<UsuarioAuditoriaInterna> listaEquipeAuditora) {
		this.listaEquipeAuditora = listaEquipeAuditora;
	}
	
	public void setListaNaoConformidades(List<ItemAuditoriaInterna> listaNaoConformidades) {
		this.listaNaoConformidades = listaNaoConformidades;
	}
	
	public void setListaOutrasNaoConformidades(List<ItemAuditoriaInterna> listaOutrasNaoConformidades) {
		this.listaOutrasNaoConformidades = listaOutrasNaoConformidades;
	}
}
