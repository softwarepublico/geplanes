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

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.report.ReportAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.resource.ResourceGenerationException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.sgm.controller.report.filtro.MatrizFCSReportFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.MatrizFCSService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;

@Bean
@Controller(path="/sgm/report/MatrizFCS", authorizationModule=ReportAuthorizationModule.class)
public class MatrizFCSReport extends GeplanesReport<MatrizFCSReportFiltro>{

	private MatrizFCSService matrizFCSService;
	private UnidadeGerencialService unidadeGerencialService;
	private UsuarioService usuarioService;
	
	public void setMatrizFCSService(MatrizFCSService MatrizFCSService) {this.matrizFCSService = MatrizFCSService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService){this.unidadeGerencialService = unidadeGerencialService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}	
	
	@Override
	public String getTitulo() {
		return "Matriz de Iniciativas x Fatores Críticos de Sucesso";
	}
	
	@DefaultAction
	@Override
	public ModelAndView doFiltro(WebRequestContext request,MatrizFCSReportFiltro filtro) throws ResourceGenerationException {
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		}
		
		return super.doFiltro(request, filtro);
	}
	
	@Override
	@Action("gerar")	
	public IReport createReportGeplanes(WebRequestContext request, MatrizFCSReportFiltro filtro) throws Exception {
		
		filtro.setUnidadeGerencial(unidadeGerencialService.load(filtro.getUnidadeGerencial()));
		if (!Boolean.TRUE.equals(filtro.getUnidadeGerencial().getPermitirMatrizFcs())) {
			throw new GeplanesException("Não é permitida a exibição da matriz de iniciativas x fcs para essa unidade gerencial.");
		}
		
		if (!usuarioService.isAcessoConsultaAutorizado(filtro.getUnidadeGerencial())) {
			throw new GeplanesException("Você não tem permissão para acessar os dados dessa unidade gerencial.");		
		}
		
		return matrizFCSService.createMatrizFCSReport(filtro);
	}
}
