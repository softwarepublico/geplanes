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

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.SolicitacaoCancelamentoIndicador;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.AprovacaoEnum;
import br.com.linkcom.sgm.util.Nomes;


public class SolicitacaoCancelamentoIndicadorFiltro{
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private PerspectivaMapaEstrategico perspectivaMapaEstrategico;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	private AprovacaoEnum status;
	private List<SolicitacaoCancelamentoIndicador> listaSolicitacaoCancelamentoIndicador;
	private List<UnidadeGerencial> listaUnidadeGerencialDisponivel;
	
	@DisplayName(Nomes.Plano_de_Gestao)
	@Required	
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	@DisplayName("Unidade Gerencial")
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	@DisplayName("Perspectiva")
	public PerspectivaMapaEstrategico getPerspectivaMapaEstrategico() {
		return perspectivaMapaEstrategico;
	}
	@DisplayName(Nomes.Estrategia)
	public ObjetivoMapaEstrategico getObjetivoMapaEstrategico() {
		return objetivoMapaEstrategico;
	}
	@DisplayName("Status da solicitação")
	public AprovacaoEnum getStatus() {
		return status;
	}
	public List<SolicitacaoCancelamentoIndicador> getListaSolicitacaoCancelamentoIndicador() {
		return listaSolicitacaoCancelamentoIndicador;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setPerspectivaMapaEstrategico(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		this.perspectivaMapaEstrategico = perspectivaMapaEstrategico;
	}
	public void setObjetivoMapaEstrategico(ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		this.objetivoMapaEstrategico = objetivoMapaEstrategico;
	}
	public void setStatus(AprovacaoEnum status) {
		this.status = status;
	}
	public void setListaSolicitacaoCancelamentoIndicador(List<SolicitacaoCancelamentoIndicador> listaSolicitacaoCancelamentoIndicador) {
		this.listaSolicitacaoCancelamentoIndicador = listaSolicitacaoCancelamentoIndicador;
	}
	
	// AUXILIARES
	private Integer id;
	private Boolean aprovar;
	
	public Integer getId() {
		return id;
	}
	public Boolean getAprovar() {
		return aprovar;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setAprovar(Boolean aprovar) {
		this.aprovar = aprovar;
	}
	public List<UnidadeGerencial> getListaUnidadeGerencialDisponivel() {
		return listaUnidadeGerencialDisponivel;
	}
	public void setListaUnidadeGerencialDisponivel(
			List<UnidadeGerencial> listaUnidadeGerencialDisponivel) {
		this.listaUnidadeGerencialDisponivel = listaUnidadeGerencialDisponivel;
	}	
}
