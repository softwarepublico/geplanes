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
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.util.Nomes;

@DisplayName("Indicadores")
public class DistribuicaoPesosIndicadores{
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private Integer id_indicador;
	private Integer id_objetivoMapaEstrategico;
	
	private List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico;
	
	// CHECKLIST
	private List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategicoFiltro;	

	@DisplayName(Nomes.Plano_de_Gestao)
	@Required
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}

	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}

	public Integer getId_indicador() {
		return id_indicador;
	}

	public Integer getId_objetivoMapaEstrategico() {
		return id_objetivoMapaEstrategico;
	}

	public List<PerspectivaMapaEstrategico> getListaPerspectivaMapaEstrategico() {
		return listaPerspectivaMapaEstrategico;
	}
	
	public List<ObjetivoMapaEstrategico> getListaObjetivoMapaEstrategicoFiltro() {
		return listaObjetivoMapaEstrategicoFiltro;
	}	

	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}

	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}

	public void setId_indicador(Integer id_indicador) {
		this.id_indicador = id_indicador;
	}

	public void setId_objetivoMapaEstrategico(Integer id_objetivoMapaEstrategico) {
		this.id_objetivoMapaEstrategico = id_objetivoMapaEstrategico;
	}

	public void setListaPerspectivaMapaEstrategico(List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico) {
		this.listaPerspectivaMapaEstrategico = listaPerspectivaMapaEstrategico;
	}
	
	public void setListaObjetivoMapaEstrategicoFiltro(List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategicoFiltro) {
		this.listaObjetivoMapaEstrategicoFiltro = listaObjetivoMapaEstrategicoFiltro;
	}
}
