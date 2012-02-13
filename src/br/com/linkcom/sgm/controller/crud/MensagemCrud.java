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
package br.com.linkcom.sgm.controller.crud;

import java.util.Calendar;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.Mensagem;
import br.com.linkcom.sgm.filtro.MensagemFiltro;



@Controller(path="/sgm/crud/Mensagem", authorizationModule=CrudAuthorizationModule.class)
public class MensagemCrud extends CrudController<MensagemFiltro, Mensagem, Mensagem> {
	
	@Override
	protected void entrada(WebRequestContext request, Mensagem bean) throws Exception {
		if (bean.getId() == null) {
			bean.setDataLancamento( new java.sql.Date(new java.util.Date().getTime()) );
			bean.setVisivel(Boolean.TRUE);
		}
	}
	
	@Override
	public ModelAndView doListagem(WebRequestContext request, MensagemFiltro filtro) throws CrudException {
		if (filtro.getDataInicio() == null) {
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -1);
			java.util.Date d1 = c1.getTime();
			filtro.setDataInicio( new java.sql.Date(d1.getTime()) );
		}
		if (filtro.getDataFim() == null) {
			filtro.setDataFim( new java.sql.Date(new java.util.Date().getTime()) );
		}
		
		return super.doListagem(request, filtro);
	}
	
	@Override
	protected void salvar(WebRequestContext request, Mensagem bean) throws Exception {
		super.salvar(request, bean);
		request.addMessage("Mensagem salva com sucesso", MessageType.INFO);
	}

}