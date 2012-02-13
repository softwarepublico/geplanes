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
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;
import br.com.linkcom.sgm.util.Nomes;

public class IniciativaPlanoAcaoFiltro extends FiltroListagem  {
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private boolean incluirSubordinadas;
	private List<UnidadeGerencial> listaUnidadeGerencial;
	private List<UnidadeGerencial> listaUnidadeGerencialDisponivel;
	private PerspectivaMapaEstrategico perspectivaMapaEstrategico;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	private Iniciativa iniciativa;
	private List<PlanoAcao> listaPlanoAcao;
	private Boolean expirado;
	private List<StatusPlanoAcaoEnum> listaStatusPlanoAcaoEnum;
	private String listaStatus;	
	
	@Required
	@DisplayName(Nomes.Plano_de_Gestao)
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

	@DisplayName("Iniciativa")
	public Iniciativa getIniciativa() {
		return iniciativa;
	}
	
	public List<PlanoAcao> getListaPlanoAcao() {
		return listaPlanoAcao;
	}
	
	@DisplayName("Incluir subordinadas")
	public boolean isIncluirSubordinadas() {
		return incluirSubordinadas;
	}

	public List<UnidadeGerencial> getListaUnidadeGerencial() {
		return listaUnidadeGerencial;
	}

	public List<UnidadeGerencial> getListaUnidadeGerencialDisponivel() {
		return listaUnidadeGerencialDisponivel;
	}
	
	public Boolean getExpirado() {
		return expirado;
	}

	public List<StatusPlanoAcaoEnum> getListaStatusPlanoAcaoEnum() {
		return listaStatusPlanoAcaoEnum;
	}

	@DisplayName("Status")
	public String getListaStatus() {
		return listaStatus;
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

	public void setIniciativa(Iniciativa iniciativa) {
		this.iniciativa = iniciativa;
	}
	
	public void setListaPlanoAcao(List<PlanoAcao> listaPlanoAcao) {
		this.listaPlanoAcao = listaPlanoAcao;
	}

	public void setIncluirSubordinadas(boolean incluirSubordinadas) {
		this.incluirSubordinadas = incluirSubordinadas;
	}

	public void setListaUnidadeGerencial(
			List<UnidadeGerencial> listaUnidadeGerencial) {
		this.listaUnidadeGerencial = listaUnidadeGerencial;
	}

	public void setListaUnidadeGerencialDisponivel(
			List<UnidadeGerencial> listaUnidadeGerencialDisponivel) {
		this.listaUnidadeGerencialDisponivel = listaUnidadeGerencialDisponivel;
	}

	public void setExpirado(Boolean expirado) {
		this.expirado = expirado;
	}

	public void setListaStatusPlanoAcaoEnum(List<StatusPlanoAcaoEnum> listaStatusPlanoAcaoEnum) {
		this.listaStatusPlanoAcaoEnum = listaStatusPlanoAcaoEnum;
	}

	public void setListaStatus(String listaStatus) {
		this.listaStatus = listaStatus;
	}
	
}
