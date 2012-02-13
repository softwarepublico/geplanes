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
package br.com.linkcom.sgm.service;

import java.util.Collections;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.Norma;
import br.com.linkcom.sgm.util.neo.service.GenericService;


public class NormaService extends GenericService<Norma> {
	
	private static NormaService instance;
	
	public static NormaService getInstance(){
		if(instance == null){
			instance = Neo.getObject(NormaService.class);
		}
		return instance;
	}	
	
	@Override
	public Norma loadForEntrada(Norma bean) {
		Norma norma = super.loadForEntrada(bean);
		
		// Ordena os requisitos da norma
		Collections.sort(norma.getListaRequisitoNorma());
		
		return norma;
	}
}
