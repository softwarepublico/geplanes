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
package br.com.linkcom.sgm.controller.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.Arquivo;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.controller.filtro.EnvioEmailFiltro;
import br.com.linkcom.sgm.controller.process.bean.EnvioEmailBean;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.util.email.EnvioEmail;



@Controller(path="/sgm/process/EnvioEmail", authorizationModule=ProcessAuthorizationModule.class)
@Bean
public class EnvioEmailProcess extends MultiActionController{
	
	@DefaultAction
	public ModelAndView carregarPagina(WebRequestContext request, EnvioEmailFiltro bean) {
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		bean.setRemetente(parametrosSistema.getEmailRemetente());
		
		return new ModelAndView("process/envioEmail", "filtro", bean);
	}
	
	public ModelAndView enviarMailling(WebRequestContext request, EnvioEmailFiltro bean) {
		
		List<Arquivo> listaArquivo = null;
		List<EnvioEmailBean> lista = bean.getListaArquivo();
		if(lista != null){
			for (EnvioEmailBean b : lista) {
				if(listaArquivo == null) listaArquivo = new ArrayList<Arquivo>();
				listaArquivo.add(b.getAnexo());
			}
		}
		
		List<EnvioEmailBean> listaDestinatario = bean.getListaDestinatario();
		List<EnvioEmailBean> listaCc = bean.getListaCc();
		List<EnvioEmailBean> listaCco = bean.getListaCco();
		EnvioEmail envioEmail = EnvioEmail.getInstance();
		
		String[] listTo = null;
		if(listaDestinatario != null && listaDestinatario.size() > 0){
			listTo = new String[listaDestinatario.size()];
			int i = 0;
			for (EnvioEmailBean b : listaDestinatario) {
				listTo[i] = b.getEmail();
				i++;
			}
		}
		
		String[] listCc = null;
		if(listaCc != null && listaCc.size() > 0){
			listCc = new String[listaCc.size()];
			int i = 0;
			for (EnvioEmailBean b : listaCc) {
				listCc[i] = b.getEmail();
				i++;
			}
		}
		
		String[] listCco = null;
		if(listaCco != null && listaCco.size() > 0){
			listCco = new String[listaCco.size()];
			int i = 0;
			for (EnvioEmailBean b : listaCco) {
				listCco[i] = b.getEmail();
				i++;
			}
		}
		
		try {
			if(envioEmail.enviaEmail(bean.getRemetente(), listTo, listCc, listCco, bean.getAssunto(), bean.getMensagem(), null, listaArquivo)){
				request.addMessage("E-mail(s) enviado(s) com sucesso.");
			} else {
				request.addError("E-mail(s) não enviado(s).");
			}
		}
		catch (Exception e) {
			request.addError(e.getMessage());
		}
		
		return new ModelAndView("redirect:/sgm/process/EnvioEmail");
	}
	
}