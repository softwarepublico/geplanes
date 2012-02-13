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

import org.springframework.validation.BindException;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.sgm.beans.Perspectiva;
import br.com.linkcom.sgm.filtro.PerspectivaFiltro;
import br.com.linkcom.sgm.service.PerspectivaService;


@Controller(path="/sgm/crud/Perspectiva", authorizationModule=CrudAuthorizationModule.class)
public class PerspectivaCrud extends CrudController<PerspectivaFiltro, Perspectiva, Perspectiva> {
	
	private PerspectivaService perspectivaService;
	
	public void setPerspectivaService(PerspectivaService perspectivaService) {
		this.perspectivaService = perspectivaService;
	}
	
	@Override
	protected void validateBean(Perspectiva bean, BindException errors) {
		
		String beanDescricao = (Util.strings.tiraAcento(bean.getDescricao())).trim();
		
		List<Perspectiva> listaPerspectiva = perspectivaService.findAll();
		
		/*** verifica se a perspectiva já está cadastrada no banco ***/
		if(listaPerspectiva!=null){
			
			for (Perspectiva perspectiva : listaPerspectiva) {
				
				String perspectivaDescricao  = (Util.strings.tiraAcento(perspectiva.getDescricao())).trim();
			
				if (!perspectiva.getId().equals(bean.getId()) && beanDescricao.compareToIgnoreCase(perspectivaDescricao) == 0) {
					errors.reject("","Cadastro não realizado. Já existe uma perspectiva cadastrada com este nome.");
				}
			
			}
		}
		super.validateBean(bean, errors);
	}
	
	@Override
	protected void salvar(WebRequestContext request, Perspectiva bean) throws Exception {
		super.salvar(request, bean);
		request.addMessage("Perspectiva salva com sucesso", MessageType.INFO);
	}
}
