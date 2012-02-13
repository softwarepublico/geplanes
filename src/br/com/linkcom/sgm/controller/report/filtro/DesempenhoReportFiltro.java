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
package br.com.linkcom.sgm.controller.report.filtro;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.ObjetivoEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.util.Nomes;


public class DesempenhoReportFiltro extends FiltroListagem {
	private PlanoGestao planoGestao;
	private ObjetivoEstrategico objetivoEstrategico;
	private UnidadeGerencial unidadeGerencial;
	private int tipoRelatorio; // 1 -> Por Objetivo Estratégico; 2 -> Por Unidade Gerencial;
	
	@Required
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	
	@DisplayName(Nomes.Estrategia)
	public ObjetivoEstrategico getObjetivoEstrategico() {
		return objetivoEstrategico;
	}
	
	@DisplayName("Unidade Gerencial")
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@Required
	@DisplayName("Tipo")
	public int getTipoRelatorio() {
		return tipoRelatorio;
	}
	
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	
	public void setObjetivoEstrategico(ObjetivoEstrategico objetivoEstrategico) {
		this.objetivoEstrategico = objetivoEstrategico;
	}
	
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	
	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
}
