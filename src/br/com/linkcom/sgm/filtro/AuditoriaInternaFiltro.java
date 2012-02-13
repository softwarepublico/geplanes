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
package br.com.linkcom.sgm.filtro;

import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.Norma;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusAuditoriaInternaEnum;
import br.com.linkcom.sgm.util.Nomes;


public class AuditoriaInternaFiltro extends FiltroListagem  {
	private PlanoGestao planoGestao;
	private UnidadeGerencial ugResponsavel;
	private boolean incluirSubordinadasResp;
	private List<UnidadeGerencial> listaUnidadeGerencialResp;
	private List<UnidadeGerencial> listaUnidadeGerencialDisponivel;
	private Norma norma;
	private StatusAuditoriaInternaEnum status;
	
	//=========================Get e Set==================================//

	@DisplayName(Nomes.Plano_de_gestao)
	@Required
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	
	@DisplayName("Setor responsável pelo tratamento")
	public UnidadeGerencial getUgResponsavel() {
		return ugResponsavel;
	}

	@DisplayName("Incluir subordinadas")
	public boolean isIncluirSubordinadasResp() {
		return incluirSubordinadasResp;
	}

	public List<UnidadeGerencial> getListaUnidadeGerencialResp() {
		return listaUnidadeGerencialResp;
	}

	public List<UnidadeGerencial> getListaUnidadeGerencialDisponivel() {
		return listaUnidadeGerencialDisponivel;
	}

	public Norma getNorma() {
		return norma;
	}
	
	public StatusAuditoriaInternaEnum getStatus() {
		return status;
	}

	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}

	public void setUgResponsavel(UnidadeGerencial ugResponsavel) {
		this.ugResponsavel = ugResponsavel;
	}

	public void setIncluirSubordinadasResp(boolean incluirSubordinadasResp) {
		this.incluirSubordinadasResp = incluirSubordinadasResp;
	}

	public void setListaUnidadeGerencialResp(List<UnidadeGerencial> listaUnidadeGerencialResp) {
		this.listaUnidadeGerencialResp = listaUnidadeGerencialResp;
	}

	public void setListaUnidadeGerencialDisponivel(List<UnidadeGerencial> listaUnidadeGerencialDisponivel) {
		this.listaUnidadeGerencialDisponivel = listaUnidadeGerencialDisponivel;
	}

	public void setNorma(Norma norma) {
		this.norma = norma;
	}
	
	public void setStatus(StatusAuditoriaInternaEnum status) {
		this.status = status;
	}
}
