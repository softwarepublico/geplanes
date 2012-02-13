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
import br.com.linkcom.sgm.beans.UnidadeMedida;
import br.com.linkcom.sgm.filtro.UnidadeMedidaFiltro;
import br.com.linkcom.sgm.service.UnidadeMedidaService;


@Controller(path="/sgm/crud/UnidadeMedida", authorizationModule=CrudAuthorizationModule.class)
public class UnidadeMedidaCrud extends CrudController<UnidadeMedidaFiltro, UnidadeMedida, UnidadeMedida> {
	
	private UnidadeMedidaService unidadeMedidaService;
	
	public void setUnidadeMedidaService(UnidadeMedidaService unidadeMedidaService) {this.unidadeMedidaService = unidadeMedidaService;}
	
	
	@Override
	protected void validateBean(UnidadeMedida bean, BindException errors) {
		
		String beanNome = (Util.strings.tiraAcento(bean.getNome())).trim();
		String beanSigla = (Util.strings.tiraAcento(bean.getSigla())).trim();
		
		List<UnidadeMedida> listaUnidadeMedida = unidadeMedidaService.findAll();
		
		/*** verifica se a unidade de medida já está cadastrada no banco ***/
		if(listaUnidadeMedida!=null){
			
			for (UnidadeMedida unidadeMedida : listaUnidadeMedida) {
				
				String unidadeMedidaNome  = (Util.strings.tiraAcento(unidadeMedida.getNome())).trim();
				String unidadeMedidaSigla = (Util.strings.tiraAcento(unidadeMedida.getSigla())).trim();
			
				if(!unidadeMedida.getId().equals(bean.getId()) && beanNome.compareToIgnoreCase(unidadeMedidaNome)==0){
					errors.reject("","Cadastro não realizado. Já existe uma unidade de medida com este nome.");
				}
				if( !unidadeMedida.getId().equals(bean.getId()) && beanSigla.compareToIgnoreCase(unidadeMedidaSigla)==0 ){
					errors.reject("","Cadastro não realizado. Já existe uma unidade de medida com esta sigla.");
				}
			
			}
		}
		super.validateBean(bean, errors);
	}
	
	@Override
	protected void salvar(WebRequestContext request, UnidadeMedida bean) throws Exception {
		super.salvar(request, bean);
		request.addMessage("Unidade de medida salva com sucesso", MessageType.INFO);
	}
}
