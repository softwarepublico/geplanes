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
package br.com.linkcom.sgm.controller.filtro;

import java.util.List;

import br.com.linkcom.neo.validation.annotation.Email;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.controller.process.bean.EnvioEmailBean;



public class EnvioEmailFiltro{
	private String remetente;
	private String assunto;
	private String mensagem;
	private List<EnvioEmailBean> listaDestinatario;
	private List<EnvioEmailBean> listaCc;
	private List<EnvioEmailBean> listaCco;
	private List<EnvioEmailBean> listaArquivo;
	
	@Required
	@Email
	public String getRemetente() {
		return remetente;
	}
	@Required
	public String getAssunto() {
		return assunto;
	}	
	public String getMensagem() {
		return mensagem;
	}
	public List<EnvioEmailBean> getListaDestinatario() {
		return listaDestinatario;
	}
	public List<EnvioEmailBean> getListaArquivo() {
		return listaArquivo;
	}
	public List<EnvioEmailBean> getListaCc() {
		return listaCc;
	}
	public List<EnvioEmailBean> getListaCco() {
		return listaCco;
	}
	public void setListaCc(List<EnvioEmailBean> listaCc) {
		this.listaCc = listaCc;
	}
	public void setListaCco(List<EnvioEmailBean> listaCco) {
		this.listaCco = listaCco;
	}
	public void setListaArquivo(List<EnvioEmailBean> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public void setListaDestinatario(List<EnvioEmailBean> listaDestinatario) {
		this.listaDestinatario = listaDestinatario;
	}
}
