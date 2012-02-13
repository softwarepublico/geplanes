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

import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.sgm.util.Nomes;

@DisplayName("Controle de cadastro")
public class ControleCadastro{
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	
	private List<ControleCadastroItem> listaControleCadastroItem;

	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}

	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}

	public List<ControleCadastroItem> getListaControleCadastroItem() {
		return listaControleCadastroItem;
	}

	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}

	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}

	public void setListaControleCadastroItem(List<ControleCadastroItem> listaControleCadastroItem) {
		this.listaControleCadastroItem = listaControleCadastroItem;
	}
	
	/**
	 * Retorna pendente desde que pelo menos um item da lista esteja com o status pendente.
	 * 
	 * @return
	 */
	public boolean isPendente() {
		if (listaControleCadastroItem != null && !listaControleCadastroItem.isEmpty()) {
			for (ControleCadastroItem controleCadastroItem : listaControleCadastroItem) {
				if (Boolean.TRUE.equals(controleCadastroItem.getPendente())) {
					return true;
				}
			}
		}
		return false;
	}	
}
