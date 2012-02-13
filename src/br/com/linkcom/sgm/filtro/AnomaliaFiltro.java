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
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.OrigemAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.util.Nomes;


public class AnomaliaFiltro extends FiltroListagem  {
	private Integer sequencial;
	private String descricao;
	private PlanoGestao planoGestao;
	private UnidadeGerencial ugResponsavel;
	private UnidadeGerencial ugRegistro;
	private StatusAnomaliaEnum status;
	private boolean incluirSubordinadasReg;
	private List<UnidadeGerencial> listaUnidadeGerencialReg;
	private boolean incluirSubordinadasResp;
	private List<UnidadeGerencial> listaUnidadeGerencialResp;
	private List<UnidadeGerencial> listaUnidadeGerencialDisponivel;
	private OrigemAnomaliaEnum origem;
	private String tipoAuditoria;
	//=========================Get e Set==================================//
	@DisplayName("Número")
	public Integer getSequencial() {
		return sequencial;
	}
	@DisplayName("Descrição")
	@MaxLength(100)
	public String getDescricao() {
		return descricao;
	}
	@DisplayName(Nomes.Plano_de_Gestao)
	@Required
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	@DisplayName("Setor de registro da anomalia")
	public UnidadeGerencial getUgRegistro() {
		return ugRegistro;
	}
	@DisplayName("Setor responsável pelo tratamento da anomalia")
	public UnidadeGerencial getUgResponsavel() {
		return ugResponsavel;
	}
	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setUgRegistro(UnidadeGerencial ugRegistro) {
		this.ugRegistro = ugRegistro;
	}
	public void setUgResponsavel(UnidadeGerencial ugResponsavel) {
		this.ugResponsavel = ugResponsavel;
	}
	@DisplayName("Status")
	public StatusAnomaliaEnum getStatus() {
		return status;
	}
	public void setStatus(StatusAnomaliaEnum status) {
		this.status = status;
	}
	@DisplayName("Incluir subordinadas")
	public boolean isIncluirSubordinadasReg() {
		return incluirSubordinadasReg;
	}
	public List<UnidadeGerencial> getListaUnidadeGerencialReg() {
		return listaUnidadeGerencialReg;
	}
	@DisplayName("Incluir subordinadas")
	public boolean isIncluirSubordinadasResp() {
		return incluirSubordinadasResp;
	}
	public List<UnidadeGerencial> getListaUnidadeGerencialResp() {
		return listaUnidadeGerencialResp;
	}
	public void setIncluirSubordinadasReg(boolean incluirSubordinadasReg) {
		this.incluirSubordinadasReg = incluirSubordinadasReg;
	}
	public void setListaUnidadeGerencialReg(List<UnidadeGerencial> listaUnidadeGerencialReg) {
		this.listaUnidadeGerencialReg = listaUnidadeGerencialReg;
	}
	public void setIncluirSubordinadasResp(boolean incluirSubordinadasResp) {
		this.incluirSubordinadasResp = incluirSubordinadasResp;
	}
	public void setListaUnidadeGerencialResp(List<UnidadeGerencial> listaUnidadeGerencialResp) {
		this.listaUnidadeGerencialResp = listaUnidadeGerencialResp;
	}
	public List<UnidadeGerencial> getListaUnidadeGerencialDisponivel() {
		return listaUnidadeGerencialDisponivel;
	}
	public void setListaUnidadeGerencialDisponivel(List<UnidadeGerencial> listaUnidadeGerencialDisponivel) {
		this.listaUnidadeGerencialDisponivel = listaUnidadeGerencialDisponivel;
	}
	
	@DisplayName("Origem")
	public OrigemAnomaliaEnum getOrigem() {
		return origem;
	}
	@DisplayName("Tipo de auditoria")
	@MaxLength(255)
	public String getTipoAuditoria() {
		return tipoAuditoria;
	}
	public void setOrigem(OrigemAnomaliaEnum origem) {
		this.origem = origem;
	}
	public void setTipoAuditoria(String tipoAuditoria) {
		this.tipoAuditoria = tipoAuditoria;
	}	
}
