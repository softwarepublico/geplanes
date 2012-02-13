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

import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.Tela;
import br.com.linkcom.sgm.dao.TelaDAO;


public class TelaService extends GenericService<Tela> {
	
	private TelaDAO telaDAO;
	
	public void setTelaDAO(TelaDAO telaDAO) {
		this.telaDAO = telaDAO;
	}
	
	/**
	 * Referência ao método no dao
	 * @param url
	 * @return 
	 * @author Pedro Gonçalves
	 * @see br.com.linkcom.sgm.dao.TelaDAO#getTelaDescriptionByUrl
	 */
	public String getTelaDescriptionByUrl(String url) {
		return telaDAO.getTelaDescriptionByUrl(url);
	}
	
	/**
	 * Força uma nova busca no banco de dados das descrições das telas.
	 * @see br.com.linkcom.sgm.dao.TelaDAO#refreshTelaDescriptions
	 */
	public void refreshTelaDescriptions() {
		telaDAO.refreshTelaDescriptions();
	}	
}
