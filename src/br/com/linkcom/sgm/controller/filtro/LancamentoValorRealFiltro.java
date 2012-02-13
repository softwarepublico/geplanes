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

import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;


public class LancamentoValorRealFiltro extends PlanoGestaoUnidadeGerencialFiltro {
	private FrequenciaIndicadorEnum frequencia;
	private List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico;
	private List<Anomalia> listaAnomalias;
	private Boolean alternar = new Boolean(false);	
	
	//============================get e set==============================//
	public List<PerspectivaMapaEstrategico> getListaPerspectivaMapaEstrategico() {
		return listaPerspectivaMapaEstrategico;
	}
	
	public FrequenciaIndicadorEnum getFrequencia() {
		return frequencia;
	}
	
	public void setFrequencia(FrequenciaIndicadorEnum frequencia) {
		this.frequencia = frequencia;
	}
	
	public void setListaPerspectivaMapaEstrategico(List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico) {
		this.listaPerspectivaMapaEstrategico = listaPerspectivaMapaEstrategico;
	}
	
	public List<Anomalia> getListaAnomalias() {
		return listaAnomalias;
	}
	
	public Boolean getAlternar() {
		return alternar;
	}
		
	public void setListaAnomalias(List<Anomalia> listaAnomalias) {
		this.listaAnomalias = listaAnomalias;
	}	
	
	public void setAlternar(Boolean alternar) {
		this.alternar = alternar;
	}	
}
