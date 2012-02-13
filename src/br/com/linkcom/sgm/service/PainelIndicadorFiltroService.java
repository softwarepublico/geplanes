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

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.PainelIndicadorFiltro;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.dao.PainelIndicadorFiltroDAO;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class PainelIndicadorFiltroService extends GenericService<PainelIndicadorFiltro>{

	private PainelIndicadorFiltroDAO painelIndicadorFiltroDAO;
	
	public void setPainelIndicadorFiltroDAO(PainelIndicadorFiltroDAO painelIndicadorFiltroDAO) {
		this.painelIndicadorFiltroDAO = painelIndicadorFiltroDAO;
	}
	
	/**
	 * Retorna uma lista com os objetivos estratégicos que serão disponibilizados
	 * na tela de Painel de Indicadores.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */	
	public List<PainelIndicadorFiltro> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return painelIndicadorFiltroDAO.findByUnidadeGerencial(unidadeGerencial);
	}
	
	/**
	 * Remove todos os filtros do painel de indicadores de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 */	
	public void deleteByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		painelIndicadorFiltroDAO.deleteByUnidadeGerencial(unidadeGerencial);
	}
	
	private static PainelIndicadorFiltroService instance;
	
	public static PainelIndicadorFiltroService getInstance() {
		if (instance == null) {
			instance = Neo.getObject(PainelIndicadorFiltroService.class);
		}
		return instance;
	}	
}