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
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.sgm.beans.Papel;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.util.Nomes;


public class UsuarioFiltro extends FiltroListagem {
	private String nome;
	private String email;
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private String login;
	private Papel papel;
	private boolean incluirSubordinadas;
	private Boolean bloqueado;
	private List<UnidadeGerencial> listaUnidadeGerencial;
	
	//===============================get e set=========================//
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	@DisplayName("E-mail")
	@MaxLength(50)
	public String getEmail() {
		return email;
	}
	@MaxLength(15)
	public String getLogin() {
		return login;
	}
	@MaxLength(60)
	public String getNome() {
		return nome;
	}
	@DisplayName("Nível de acesso")
	public Papel getPapel() {
		return papel;
	}
	public Boolean getBloqueado() {
		return bloqueado;
	}
	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	@DisplayName("Incluir subordinadas")
	public boolean isIncluirSubordinadas() {
		return incluirSubordinadas;
	}
	public void setIncluirSubordinadas(boolean incluirSubordinadas) {
		this.incluirSubordinadas = incluirSubordinadas;
	}
	public List<UnidadeGerencial> getListaUnidadeGerencial() {
		return listaUnidadeGerencial;
	}
	public void setListaUnidadeGerencial(List<UnidadeGerencial> listaUnidadeGerencial) {
		this.listaUnidadeGerencial = listaUnidadeGerencial;
	}
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
}
