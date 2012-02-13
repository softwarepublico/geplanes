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

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.persistence.SaveOrUpdateStrategy;
import br.com.linkcom.sgm.beans.AnexoAnomalia;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;

public class AnexoAnomaliaDAO extends GenericDAO<AnexoAnomalia> {
	
	private ArquivoDAO arquivoDAO;
	
	public void setArquivoDAO(ArquivoDAO arquivoDAO) {
		this.arquivoDAO = arquivoDAO;
	}
	
	public List<AnexoAnomalia> findByAnomalia(Anomalia anomalia) {	
		return query()
			.join("anexoAnomalia.anomalia anomalia")
			.joinFetch("anexoAnomalia.arquivo")
			.where("anomalia = ?", anomalia)
			.list();
	}	

	@Override
	public void updateSaveOrUpdate(final SaveOrUpdateStrategy save) {
		getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus status) {
				arquivoDAO.saveFile(save.getEntity(), "arquivo", false);
				return null;
			}			
		});
	}
	
	@Override
	public void delete(final AnexoAnomalia bean) {
		getTransactionTemplate().execute(new TransactionCallback(){
			public Object doInTransaction(TransactionStatus arg0) {
				AnexoAnomaliaDAO.super.delete(bean);
				return null;
			}});
		
	}	
}
