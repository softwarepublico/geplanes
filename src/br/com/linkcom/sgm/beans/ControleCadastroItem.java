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
package br.com.linkcom.sgm.beans;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.sgm.beans.enumeration.ItemControleCadastroEnum;

@DisplayName("Item do controle de cadastro")
public class ControleCadastroItem{
	private ItemControleCadastroEnum itemControleCadastroEnum;
	private String descricao;
	private String url;
	private Boolean pendente;
	private boolean exibirLink;
	
	public ItemControleCadastroEnum getItemControleCadastroEnum() {
		return itemControleCadastroEnum;
	}
	public String getDescricao() {
		return descricao;
	}
	public String getUrl() {
		return url;
	}
	public Boolean getPendente() {
		return pendente;
	}
	public boolean isExibirLink() {
		return exibirLink;
	}
	public void setItemControleCadastroEnum(ItemControleCadastroEnum itemControleCadastroEnum) {
		this.itemControleCadastroEnum = itemControleCadastroEnum;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}
	public void setExibirLink(boolean exibirLink) {
		this.exibirLink = exibirLink;
	}
}
