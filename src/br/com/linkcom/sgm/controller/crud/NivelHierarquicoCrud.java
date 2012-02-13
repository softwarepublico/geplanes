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
import br.com.linkcom.sgm.beans.NivelHierarquico;
import br.com.linkcom.sgm.filtro.NivelHierarquicoFiltro;
import br.com.linkcom.sgm.service.NivelHierarquicoService;


@Controller(path="/sgm/crud/NivelHierarquico", authorizationModule=CrudAuthorizationModule.class)
public class NivelHierarquicoCrud extends CrudController<NivelHierarquicoFiltro, NivelHierarquico, NivelHierarquico> {
	
	private NivelHierarquicoService nivelHierarquicoService;
	
	public void setNivelHierarquicoService(NivelHierarquicoService nivelHierarquicoService) {
		this.nivelHierarquicoService = nivelHierarquicoService;
	}
	
	@Override
	protected void validateBean(NivelHierarquico bean, BindException errors) {
		
		String beanDescricao = (Util.strings.tiraAcento(bean.getDescricao())).trim();
		
		List<NivelHierarquico> listaNivelHierarquico = nivelHierarquicoService.findAll();
		
		/*** verifica se o nível hierárquico já está cadastrado no banco ***/
		if(listaNivelHierarquico!=null){
			
			for (NivelHierarquico nivelHierarquico : listaNivelHierarquico) {
				
				String estrategiaDescricao  = (Util.strings.tiraAcento(nivelHierarquico.getDescricao())).trim();
			
				if (!nivelHierarquico.getId().equals(bean.getId()) && beanDescricao.compareToIgnoreCase(estrategiaDescricao) == 0) {
					errors.reject("","Cadastro não realizado. Já existe um nível hierárquico cadastrado com esta descrição.");
				}
			
			}
		}
		super.validateBean(bean, errors);
	}
	
	@Override
	protected void salvar(WebRequestContext request, NivelHierarquico bean) throws Exception {
		super.salvar(request, bean);
		request.addMessage("Nível hierárquico salvo com sucesso", MessageType.INFO);
	}
}
