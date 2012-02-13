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
package br.com.linkcom.sgm.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.sgm.beans.CompetenciaOrganizacional;
import br.com.linkcom.sgm.service.CompetenciaOrganizacionalService;

@Controller(path="/sgm/Ajaxcompetenciaorganizacional")
	
@Bean
public class AjaxCompetenciaOrganizacionalController implements org.springframework.web.servlet.mvc.Controller{
	
	private CompetenciaOrganizacionalService competenciaOrganizacionalService;
	
	public void setCompetenciaOrganizacionalService(CompetenciaOrganizacionalService competenciaOrganizacionalService) {
		this.competenciaOrganizacionalService = competenciaOrganizacionalService;
	}
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CompetenciaOrganizacional comp  = new CompetenciaOrganizacional();
		String desc = "";
		try {
			Integer id    = getParametro(request,response,"id");
			if (id != null) {
				comp.setId(id);
				comp = competenciaOrganizacionalService.load(comp);
				desc = comp.getDescricao() != null ? comp.getDescricao().trim() : "";
			}
		} 
		catch (Exception e) {
			response.getOutputStream().print("javascript:alert('" + e.getMessage() + "');");
		}
		response.setContentType("text/html; charset=ISO-8859-1");
		response.getOutputStream().print("var desc = '" + desc + "';");
		return null;
	}

	private Integer getParametro(HttpServletRequest request, HttpServletResponse response, String campo) {
		Integer cdparametro = new Integer(0);
		String parametro = request.getParameter(campo) != null ? request.getParameter(campo) : "";
		if (!parametro.contains("null")) {
			Matcher casamento = Pattern.compile(".{0,60}?\\["+campo+"=(\\d+)\\]").matcher(parametro);
			if (casamento.matches()) {
				cdparametro = new Integer(casamento.group(1));
			}
			return cdparametro;
		}
		return null;
	}
}