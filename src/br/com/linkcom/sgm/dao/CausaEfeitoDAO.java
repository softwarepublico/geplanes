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

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.CausaEfeito;


public class CausaEfeitoDAO extends GenericDAO<CausaEfeito> {
	
	/**
	 * Retorna o efeito de uma determinada anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return CausaEfeito
	 */
	public CausaEfeito findByAnomalia(Anomalia anomalia){
		return 
			query()
			.where("causaEfeito.anomalia = ?", anomalia)
			.unique();
	}
	
	/**
	 * Retorna uma lista com as causas relacionadas a uma causa pai.
	 * @author Matheus Melo Gonçalves
	 * @param CausaEfeito Pai
	 * @return List<CausaEfeito> Filha(s)
	 */
	public List<CausaEfeito> findByCausaFilha(CausaEfeito causaEfeito){
		return 
			query()
			.where("causaEfeito.efeito = ?", causaEfeito)
			.list();
	}
}
