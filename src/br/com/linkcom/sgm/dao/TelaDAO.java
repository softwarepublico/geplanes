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

import java.util.HashMap;
import java.util.List;

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.sgm.beans.Tela;


public class TelaDAO extends GenericDAO<Tela> {

	protected HashMap<String, String> mapTelas;
	
	/**
	 * Captura a descrição cadastrada no banco a partir de uma url
	 * A url deve estar no formado /modulo/tela ( a mesma cadastrada no @controller na propriedade path)
	 * @param url
	 * @return Descrição da tela cadastrada no banco
	 * @author Pedro Gonçalves
	 */
	public String getTelaDescriptionByUrl(String url) {
		if(mapTelas == null){
			List<Tela> telas = findAll();
			HashMap<String, String> mapa = new HashMap<String, String>();
			for (Tela tela : telas) {
				mapa.put(tela.getPath(), tela.getNome());
			}
			mapTelas = mapa;
		}
		return mapTelas.get(url);
	}	
	
	/**
	 * Força uma nova busca no banco de dados das descrições das telas.
	 */
	public void refreshTelaDescriptions() {
		mapTelas = null;
	}
}
