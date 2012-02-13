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
package br.com.linkcom.sgm.dao;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.sgm.beans.Norma;
import br.com.linkcom.sgm.filtro.NormaFiltro;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

@DefaultOrderBy("nome")
public class NormaDAO extends GenericDAO<Norma> {

	@Override
	public void updateListagemQuery(QueryBuilder<Norma> query, FiltroListagem _filtro) {
		NormaFiltro filtro = (NormaFiltro) _filtro;
		query.whereLikeIgnoreAll("norma.nome", filtro.getNome());
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<Norma> query) {
		query.leftOuterJoinFetch("norma.listaRequisitoNorma requisitoNorma");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaRequisitoNorma");
	}
}
