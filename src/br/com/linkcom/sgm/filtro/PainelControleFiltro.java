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

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.util.Nomes;


public class PainelControleFiltro {

	PlanoGestao planoGestao;
	UnidadeGerencial unidadeGerencial;
	String apuradosAcumulados;
	
	List<ItemPainelControleDTO> itens = new ArrayList<ItemPainelControleDTO>();
	List<UnidadeGerencial> todasUnidades = new ArrayList<UnidadeGerencial>();
	List<UnidadeGerencial> unidades = new ArrayList<UnidadeGerencial>();

	//============================get e set===============================//
	public List<ItemPainelControleDTO> getItens() {
		return itens;
	}
	@DisplayName(Nomes.Plano_de_Gestao)
	@Required
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	public List<UnidadeGerencial> getTodasUnidades() {
		return todasUnidades;
	}
	public List<UnidadeGerencial> getUnidades() {
		return unidades;
	}
	public void setItens(List<ItemPainelControleDTO> itens) {
		this.itens = itens;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setTodasUnidades(List<UnidadeGerencial> todasUnidades) {
		this.todasUnidades = todasUnidades;
	}
	public void setUnidades(List<UnidadeGerencial> unidades) {
		this.unidades = unidades;
	}
	public String getApuradosAcumulados() {
		return apuradosAcumulados;
	}
	public void setApuradosAcumulados(String apuradosAcumulados) {
		this.apuradosAcumulados = apuradosAcumulados;
	}
	
	public boolean isApurados(){
		return "apurados".equals(apuradosAcumulados);
	}
}
