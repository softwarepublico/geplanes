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
import br.com.linkcom.sgm.beans.FatorAvaliacao;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.MatrizFCSFator;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.util.Nomes;

@DisplayName("Matriz de Iniciativas x FCS")
public class MatrizFCSFiltro {
	private PerspectivaMapaEstrategico perspectivaMapaEstrategico;
	private ObjetivoMapaEstrategico objetivoMapaEstrategico;
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private FatorAvaliacao fatorAvaliacao;
	private MatrizFCS matrizFCS;

	// Campos utilizados para a gravação dos dados da matriz
	List<MatrizFCSIniciativa> listaMatrizFCSIniciativaForDelete;
	List<MatrizFCSFator> listaMatrizFCSFatorForDelete;
	
	@DisplayName(Nomes.Plano_de_Gestao)	
	@Required
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	
	@DisplayName("Unidade Gerencial")
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	
	@DisplayName("Sistema de pontuação")
	@Required
	public FatorAvaliacao getFatorAvaliacao() {
		return fatorAvaliacao;
	}	
	
	@DisplayName("Perspectiva")
	@Required
	public PerspectivaMapaEstrategico getPerspectivaMapaEstrategico() {
		return perspectivaMapaEstrategico;
	}
	
	@DisplayName(Nomes.Estrategia)	
	@Required
	public ObjetivoMapaEstrategico getObjetivoMapaEstrategico() {
		return objetivoMapaEstrategico;
	}
	
	public MatrizFCS getMatrizFCS() {
		return matrizFCS;
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
	
	public void setFatorAvaliacao(FatorAvaliacao fatorAvaliacao) {
		this.fatorAvaliacao = fatorAvaliacao;
	}
	
	public void setMatrizFCS(MatrizFCS matrizFCS) {
		this.matrizFCS = matrizFCS;
	}

	public List<MatrizFCSIniciativa> getListaMatrizFCSIniciativaForDelete() {
		return listaMatrizFCSIniciativaForDelete;
	}

	public List<MatrizFCSFator> getListaMatrizFCSFatorForDelete() {
		return listaMatrizFCSFatorForDelete;
	}

	public void setListaMatrizFCSIniciativaForDelete(List<MatrizFCSIniciativa> listaMatrizFCSIniciativaForDelete) {
		this.listaMatrizFCSIniciativaForDelete = listaMatrizFCSIniciativaForDelete;
	}

	public void setListaMatrizFCSFatorForDelete(List<MatrizFCSFator> listaMatrizFCSFatorForDelete) {
		this.listaMatrizFCSFatorForDelete = listaMatrizFCSFatorForDelete;
	}
}
