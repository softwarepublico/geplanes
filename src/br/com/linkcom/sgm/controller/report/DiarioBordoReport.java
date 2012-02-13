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

import br.com.linkcom.neo.authorization.report.ReportAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.sgm.filtro.OcorrenciaFiltro;
import br.com.linkcom.sgm.service.OcorrenciaService;


@Bean
@Controller(path="/sgm/report/DiarioBordo", authorizationModule=ReportAuthorizationModule.class)
public class DiarioBordoReport extends GeplanesReport<OcorrenciaFiltro>{

	private OcorrenciaService ocorrenciaService;
	
	public void setOcorrenciaService(OcorrenciaService ocorrenciaService) {this.ocorrenciaService = ocorrenciaService;}
	
	@Override
	@DefaultAction
	public IReport createReportGeplanes(WebRequestContext request, OcorrenciaFiltro filtro) throws Exception {
		return ocorrenciaService.createReportDiarioBordo(filtro);		
	}


	@Override
	public String getTitulo() {
		return "Diário de Bordo";
	}

}
