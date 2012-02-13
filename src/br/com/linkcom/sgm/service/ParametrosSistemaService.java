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

import javax.servlet.http.HttpServletRequest;

import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.dao.ParametrosSistemaDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.util.email.EnviaEmailTeste;


public class ParametrosSistemaService extends GenericService<ParametrosSistema> {

	public static ParametrosSistema getParametrosSistema(){
		List<ParametrosSistema> listaParamentrosSistema = ParametrosSistemaDAO.getInstance().findAll();
		if (listaParamentrosSistema == null  || listaParamentrosSistema.isEmpty()) {
			throw new GeplanesException("Não existe um conjunto de parâmetros gerais para o sistema.");
		}
		if (listaParamentrosSistema.size() != 1 ) {
			throw new GeplanesException("Existe mais de um conjunto de parâmetros gerais para o sistema.");
		}
		
		ParametrosSistema parametrosSistema = listaParamentrosSistema.get(0);
		
		return parametrosSistema;
	}
	
	public String enviaEmailTeste(HttpServletRequest request, ParametrosSistema bean){
		EnviaEmailTeste emailTeste = new EnviaEmailTeste();
		return emailTeste.enviaEmail(request, bean);
	}
}