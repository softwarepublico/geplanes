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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.service.UsuarioService;



@Controller(path="/sgm/crud/ParametrosSistema", authorizationModule=CrudAuthorizationModule.class)
public class ParametrosSistemaCrud extends MultiActionController {

	private ParametrosSistemaService parametrosSistemaService;
	private UsuarioService usuarioService;
	public void setParametrosSistemaService(ParametrosSistemaService parametrosSistemaService) {this.parametrosSistemaService = parametrosSistemaService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	
	public ModelAndView testeemail(WebRequestContext request, ParametrosSistema bean) {
		
		String msg = parametrosSistemaService.enviaEmailTeste(request.getServletRequest(), bean);
		request.setAttribute("msgEmail", msg );
		
		if( usuarioService.isUsuarioLogadoAdmin() ){
			request.setAttribute("admin", new Boolean(true));
		}
		
		return new ModelAndView("crud/parametrosSistema", "filtro", bean);
	}
	
	@DefaultAction	
    public ModelAndView executar(WebRequestContext request, ParametrosSistema bean) {
		bean = ParametrosSistemaService.getParametrosSistema();
		
		if( usuarioService.isUsuarioLogadoAdmin() ){
			request.setAttribute("admin", new Boolean(true));
		}
			
		return new ModelAndView("crud/parametrosSistema", "filtro", bean);
	}
	
	public ModelAndView salvar(WebRequestContext request, ParametrosSistema bean) {
		
		int maxWidth  = 160;
		int maxHeight = 100;
		
		if (bean.getImgEmpresa() != null && bean.getImgEmpresa().getSize() > 0) {
			if(bean.getImgEmpresa().getContent().length > 500000){
				request.addError("O tamanho máximo permitido para a imagem da logomarca da empresa utilizada nas páginas iniciais é 500kb.");
				return executar(request, bean);
			}
			try {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(bean.getImgEmpresa().getContent()));
				if (image != null && image.getWidth() > maxWidth) {
					request.addError("O tamanho máximo permitido para a imagem da logomarca da empresa utilizada nas páginas iniciais é de " + maxWidth + "px de largura");
					return executar(request, bean);
				}
				if (image != null && image.getHeight() > maxHeight) {
					request.addError("O tamanho máximo permitido para a imagem da logomarca da empresa utilizada nas páginas iniciais é de " + maxHeight + "px de largura");
					return executar(request, bean);
				}
			} 
			catch (IOException e) {
				request.addError("Não foi possível carregar a imagem da logomarca da empresa utilizada nas páginas iniciais.");
				return executar(request, bean);
			}
		}
		if (bean.getImgEmpresaRelatorio() != null && bean.getImgEmpresaRelatorio().getSize() > 0) {
			if(bean.getImgEmpresaRelatorio().getContent().length > 500000){
				request.addError("O tamanho máximo permitido para a imagem da logomarca da empresa utilizada nos relatórios é 500kb.");
				return executar(request, bean);
			}
			try {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(bean.getImgEmpresaRelatorio().getContent()));
				if (image != null && image.getWidth() > maxWidth) {
					request.addError("O tamanho máximo permitido para a imagem da logomarca da empresa utilizada nos relatórios é de " + maxWidth + "px de largura");
					return executar(request, bean);
				}
				if (image != null && image.getHeight() > maxHeight) {
					request.addError("O tamanho máximo permitido para a imagem da logomarca da empresa utilizada nos relatórios é de " + maxHeight + "px de largura");
					return executar(request, bean);
				}
			} 
			catch (IOException e) {
				request.addError("Não foi possível carregar a imagem da logomarca da empresa utilizada nos relatórios.");
				return executar(request, bean);
			}
		}		
		request.setLastAction("executar");
		bean.setId(1);
		parametrosSistemaService.saveOrUpdate(bean);
		request.addMessage("Alterações salvas com sucesso");
		return executar(request, bean);
	}
	
}
