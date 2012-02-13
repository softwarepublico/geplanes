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

import java.sql.Date;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.sgm.beans.ModeloAuditoriaGestao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.util.Nomes;

public class AuditoriaGestaoFiltro extends FiltroListagem  {
	
	private Integer id;
	private String descricao;
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private ModeloAuditoriaGestao modeloAuditoriaGestao;
	private Date dataAuditoria1;
	private Date dataAuditoria2;
	private List<UnidadeGerencial> listaUnidadeGerencialDisponivel;
	
	public Integer getId() {
		return id;
	}
	
	@DisplayName("Período avaliado")
	@MaxLength(100)
	public String getDescricao() {
		return descricao;
	}
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	@DisplayName("Modelo de auditoria")
	public ModeloAuditoriaGestao getModeloAuditoriaGestao() {
		return modeloAuditoriaGestao;
	}
	public Date getDataAuditoria1() {
		return dataAuditoria1;
	}
	public Date getDataAuditoria2() {
		return dataAuditoria2;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setModeloAuditoriaGestao(ModeloAuditoriaGestao modeloAuditoriaGestao) {
		this.modeloAuditoriaGestao = modeloAuditoriaGestao;
	}
	public void setDataAuditoria1(Date dataAuditoria1) {
		this.dataAuditoria1 = dataAuditoria1;
	}
	public void setDataAuditoria2(Date dataAuditoria2) {
		this.dataAuditoria2 = dataAuditoria2;
	}

	public List<UnidadeGerencial> getListaUnidadeGerencialDisponivel() {
		return listaUnidadeGerencialDisponivel;
	}

	public void setListaUnidadeGerencialDisponivel(
			List<UnidadeGerencial> listaUnidadeGerencialDisponivel) {
		this.listaUnidadeGerencialDisponivel = listaUnidadeGerencialDisponivel;
	}
	
}
