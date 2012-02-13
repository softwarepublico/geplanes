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

import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;

public class UnidadeGerencialTreeFiltro {

	PlanoGestao planoGestao;
	
	UnidadeGerencial unidadeGerencial;//unidade gerencialSelecionada .. esse parametro nao vem via ajax
	
	List<UnidadeGerencial> unidadesGerenciais;
	
	String q;

	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}

	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}

	public List<UnidadeGerencial> getUnidadesGerenciais() {
		return unidadesGerenciais;
	}
	
	public String getQ() {
		return q;
	}
	
	public void setQ(String q) {
		this.q = q;
	}

	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}

	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}

	public void setUnidadesGerenciais(List<UnidadeGerencial> unidadesGerenciais) {
		this.unidadesGerenciais = unidadesGerenciais;
	}
}
