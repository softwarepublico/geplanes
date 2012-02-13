/* 
		Copyright 2007,2008,2009,2010 da Linkcom Inform�tica Ltda
		
		Este arquivo � parte do programa GEPLANES.
 	   
 	    O GEPLANES � software livre; voc� pode redistribu�-lo e/ou 
		modific�-lo sob os termos da Licen�a P�blica Geral GNU, conforme
 	    publicada pela Free Software Foundation; tanto a vers�o 2 da 
		Licen�a como (a seu crit�rio) qualquer vers�o mais nova.
 	
 	    Este programa � distribu�do na expectativa de ser �til, mas SEM 
		QUALQUER GARANTIA; sem mesmo a garantia impl�cita de 
		COMERCIALIZA��O ou de ADEQUA��O A QUALQUER PROP�SITO EM PARTICULAR. 
		Consulte a Licen�a P�blica Geral GNU para obter mais detalhes.
 	 
 	    Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU  	    
		junto com este programa; se n�o, escreva para a Free Software 
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
