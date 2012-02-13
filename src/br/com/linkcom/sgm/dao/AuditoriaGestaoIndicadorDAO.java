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
