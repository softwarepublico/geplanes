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

import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.sgm.beans.AuditoriaGestaoIndicador;
import br.com.linkcom.sgm.beans.Indicador;


public class AuditoriaGestaoIndicadorDAO extends GenericDAO<AuditoriaGestaoIndicador> {

	@Override
	public void updateListagemQuery(QueryBuilder<AuditoriaGestaoIndicador> query, FiltroListagem _filtro) {
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<AuditoriaGestaoIndicador> query) {
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaAuditoriaGestaoIndicadorItem");
	}
	
	public List<AuditoriaGestaoIndicador> findByIndicador(Indicador indicador) { 
		return query()
			.join("auditoriaGestaoIndicador.indicador indicador")
			.where("indicador = ?", indicador)
			.list();
	}	
}
