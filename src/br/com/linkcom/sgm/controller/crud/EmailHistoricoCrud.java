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

import java.util.List;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.sgm.beans.EmailHistorico;
import br.com.linkcom.sgm.filtro.EmailHistoricoFiltro;
import br.com.linkcom.sgm.service.EmailHistoricoUsuarioService;


@Controller(path="/sgm/crud/EmailHistorico", authorizationModule=CrudAuthorizationModule.class)
public class EmailHistoricoCrud extends CrudController<EmailHistoricoFiltro, EmailHistorico, EmailHistorico> {
	
	private EmailHistoricoUsuarioService emailHistoricoUsuarioService;
	
	public void setEmailHistoricoUsuarioService(EmailHistoricoUsuarioService emailHistoricoUsuarioService) {
		this.emailHistoricoUsuarioService = emailHistoricoUsuarioService;
	}
	
	@Override
	protected ListagemResult<EmailHistorico> getLista(WebRequestContext request, EmailHistoricoFiltro filtro) {
		ListagemResult<EmailHistorico> listagemResult = super.getLista(request, filtro);
		
		List<EmailHistorico> lista = listagemResult.list();
		
		if (lista != null) {
			for (EmailHistorico emailHistorico : lista) {
				emailHistorico.setListaEmailHistoricoUsuario(emailHistoricoUsuarioService.findByEmailHistorico(emailHistorico));
			}
		}
		
		return listagemResult;
	}
}
