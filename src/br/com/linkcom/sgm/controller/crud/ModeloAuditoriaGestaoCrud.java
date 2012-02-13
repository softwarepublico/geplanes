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


import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.ItemModeloAuditoriaGestao;
import br.com.linkcom.sgm.beans.ModeloAuditoriaGestao;
import br.com.linkcom.sgm.filtro.ModeloAuditoriaGestaoFiltro;
import br.com.linkcom.sgm.service.AuditoriaGestaoService;
import br.com.linkcom.sgm.service.ModeloAuditoriaGestaoService;


@Controller(path="/sgm/crud/ModeloAuditoriaGestao", authorizationModule=CrudAuthorizationModule.class)
public class ModeloAuditoriaGestaoCrud extends CrudController<ModeloAuditoriaGestaoFiltro, ModeloAuditoriaGestao, ModeloAuditoriaGestao> {
	
	private AuditoriaGestaoService auditoriaGestaoService;
	private ModeloAuditoriaGestaoService modeloAuditoriaGestaoService;
	
	public void setModeloAuditoriaGestaoService(
			ModeloAuditoriaGestaoService modeloAuditoriaGestaoService) {
		this.modeloAuditoriaGestaoService = modeloAuditoriaGestaoService;
	}
	public void setAuditoriaGestaoService(
			AuditoriaGestaoService auditoriaGestaoService) {
		this.auditoriaGestaoService = auditoriaGestaoService;
	}
	
	@Override
	protected ModeloAuditoriaGestao criar(WebRequestContext request, ModeloAuditoriaGestao form) throws Exception {
		ModeloAuditoriaGestao bean = super.criar(request, form);
		
		if("true".equals(request.getParameter("copiar"))){
			Integer id = Integer.parseInt(request.getParameter("id"));
			bean.setId(id);
			
			bean = modeloAuditoriaGestaoService.loadForEntrada(bean);
			
			bean.setId(null);
			for (ItemModeloAuditoriaGestao b: bean.getListaItemModeloAuditoriaGestao()) {
				b.setId(null);
			}
		}
		
		return bean;
	}
	
	@Override
	protected void entrada(WebRequestContext request, ModeloAuditoriaGestao form) throws Exception {
		if(form.getId() != null && auditoriaGestaoService.modeloUsado(form)){
			request.setAttribute("nao_editar", Boolean.TRUE);
		}
	}
	
	@Override
	protected void salvar(WebRequestContext request, ModeloAuditoriaGestao bean) throws Exception {
		super.salvar(request, bean);
		request.addMessage("Modelo de auditoria de gestão salvo com sucesso", MessageType.INFO);
	}
}