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
import br.com.linkcom.sgm.beans.AnexoAnomalia;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.dao.AnexoAnomaliaDAO;

public class AnexoAnomaliaService extends GenericService<AnexoAnomalia> {
	AnexoAnomaliaDAO anexoAnomaliaDAO;
	
	public void setAnexoAnomaliaDAO(AnexoAnomaliaDAO anexoAnomaliaDAO) {
		this.anexoAnomaliaDAO = anexoAnomaliaDAO;
	}
	
	public List<AnexoAnomalia> findByAnomalia(Anomalia anomalia) {
		return anexoAnomaliaDAO.findByAnomalia(anomalia);
	}
	
	public void excluiAnexoAnomaliaDetalhe(Anomalia anomalia) {
		List<AnexoAnomalia> listaAnexoAnomalia = findByAnomalia(anomalia);
		for (AnexoAnomalia anexoAnomalia : listaAnexoAnomalia) {
			if (!anomalia.getAnexosAnomalia().contains(anexoAnomalia)) {
				anexoAnomaliaDAO.delete(anexoAnomalia);
			}
		}
	}
	
	public void salvaAnexoAnomaliaDetalhe(Anomalia anomalia) {
		for (AnexoAnomalia anexoAnomalia : anomalia.getAnexosAnomalia()) {
			anexoAnomalia.setAnomalia(anomalia);
			anexoAnomaliaDAO.saveOrUpdate(anexoAnomalia);
		}		
	}
	
	public boolean tamanhoLimiteExcedido(AnexoAnomalia anexoAnomalia) {
		if (anexoAnomalia.getArquivo().getSize() > 5000000) {
			return true;
		}
		return false;
	}
}
