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
package br.com.linkcom.sgm.controller.report;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.report.ReportAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.resource.ResourceGenerationException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;
import br.com.linkcom.sgm.controller.filtro.IniciativaPlanoAcaoFiltro;
import br.com.linkcom.sgm.service.PlanoAcaoService;
import br.com.linkcom.sgm.util.FiltroUtils;

@Bean
@Controller(path="/sgm/report/IniciativaPlanoAcao", authorizationModule=ReportAuthorizationModule.class)
public class IniciativaPlanoAcaoReport extends GeplanesReport<IniciativaPlanoAcaoFiltro>{

	private PlanoAcaoService planoAcaoService;
	
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {this.planoAcaoService = planoAcaoService;}
	
	@Override
	public String getTitulo() {
		return "Planos de Ação das Iniciativas";
	}
	
	@DefaultAction
	@Override
	public ModelAndView doFiltro(WebRequestContext request,IniciativaPlanoAcaoFiltro filtro) throws ResourceGenerationException {
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}
		
		Map<String, String> mapaStatus = new LinkedHashMap<String, String>();
		for (StatusPlanoAcaoEnum statusPlanoAcaoEnum : StatusPlanoAcaoEnum.values()) {
			mapaStatus.put(statusPlanoAcaoEnum.getName(), statusPlanoAcaoEnum.toString());
		}
		request.setAttribute("mapaStatus", mapaStatus);
		return super.doFiltro(request, filtro);
	}
	
	@Override
	@Action("gerar")	
	public IReport createReportGeplanes(WebRequestContext request, IniciativaPlanoAcaoFiltro filtro) throws Exception {
		return planoAcaoService.createIniciativaPlanoAcaoReport(filtro);
	}
}
