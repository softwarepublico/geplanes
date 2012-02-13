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

import java.sql.Timestamp;
import java.util.Calendar;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.sgm.beans.EmailHistorico;
import br.com.linkcom.sgm.filtro.EmailHistoricoFiltro;

@DefaultOrderBy("data DESC")
public class EmailHistoricoDAO extends GenericDAO<EmailHistorico> {

	@Override
	public void updateListagemQuery(QueryBuilder<EmailHistorico> query, FiltroListagem _filtro) {
		EmailHistoricoFiltro filtro = (EmailHistoricoFiltro) _filtro;
		Timestamp tsInicio = null;
		Timestamp tsFim = null;
		
		if (filtro.getDtInicio() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(filtro.getDtInicio());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			tsInicio = new Timestamp(cal.getTimeInMillis());
		}
		if (filtro.getDtFim() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(filtro.getDtFim());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			tsFim = new Timestamp(cal.getTimeInMillis());
		}		
		
		query
			.whereLikeIgnoreAll("remetente",filtro.getRemetente())
			.whereLikeIgnoreAll("assunto",filtro.getAssunto())
			.where("data >= ?", tsInicio)
			.where("data <= ?", tsFim);
	}
	
	@Override
	public void updateEntradaQuery(QueryBuilder<EmailHistorico> query) {
		query
			.joinFetch("emailHistorico.listaEmailHistoricoUsuario");
	}
	
	@Override
	public void updateSaveOrUpdate(SaveOrUpdateStrategy save) {
		save.saveOrUpdateManaged("listaEmailHistoricoUsuario");
	}
}
