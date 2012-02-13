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
package br.com.linkcom.sgm.service;

import java.util.List;

import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.dao.MatrizFCSIniciativaDAO;

public class MatrizFCSIniciativaService extends GenericService<MatrizFCSIniciativa>{

	private MatrizFCSIniciativaDAO matrizFCSIniciativaDAO;
	
	public void setMatrizFCSIniciativaDAO(MatrizFCSIniciativaDAO matrizFCSIniciativaDAO) {
		this.matrizFCSIniciativaDAO = matrizFCSIniciativaDAO;
	}

	public List<MatrizFCSIniciativa> findByMatrizFCS(MatrizFCS matrizFCS){
		return matrizFCSIniciativaDAO.findByMatrizFCS(matrizFCS);
	}
	
	/**
	 * Retorna uma lista com as iniciativas prioritárias para uma determinada
	 * unidade gerencial.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<MatrizFCSIniciativa> findPrioritariasByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return matrizFCSIniciativaDAO.findPrioritariasByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Retorna uma lista com as iniciativas prioritárias para uma determinada
	 * unidade gerencial e um determinado objetivo estratégico.
	 * 
	 * @param unidadeGerencial
	 * @param objetivoMapaEstrategico
	 * @return
	 */	
	public List<MatrizFCSIniciativa> findPrioritariasByUnidadeGerencialObjetivoEstrategico(UnidadeGerencial unidadeGerencial, ObjetivoMapaEstrategico objetivoMapaEstrategico) {
		return matrizFCSIniciativaDAO.findPrioritariasByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico);
	}
	
	/**
	 * Retorna a iniciativa, bem como o plano de ação associado a ela.
	 * 
	 * @param matrizFCSIniciativa
	 * @return
	 */
	public MatrizFCSIniciativa loadWithPlanoAcao(MatrizFCSIniciativa matrizFCSIniciativa) {
		return matrizFCSIniciativaDAO.loadWithPlanoAcao(matrizFCSIniciativa);
	}
}
