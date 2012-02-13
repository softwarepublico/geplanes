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

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.report.ReportAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.resource.ResourceGenerationException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.report.filtro.PendenciaCadastroReportFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Bean
@Controller(path="/sgm/report/PendenciaCadastro", authorizationModule=ReportAuthorizationModule.class)
public class PendenciaCadastroReport extends GeplanesReport<PendenciaCadastroReportFiltro>{

	private PlanoGestaoService planoGestaoService;
	private UnidadeGerencialService unidadeGerencialService;
		
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	
	@Override
	public ModelAndView doFiltro(WebRequestContext request,	PendenciaCadastroReportFiltro filtro) throws ResourceGenerationException {
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		
		return super.doFiltro(request, filtro);
	}
	
	@Override
	@Action("gerar")
	public IReport createReportGeplanes(WebRequestContext request, PendenciaCadastroReportFiltro filtro) throws Exception {
		List<UnidadeGerencial> listaUnidadeGerencialFiltro = unidadeGerencialService.montaListaUnidadeGerencial(filtro.getPlanoGestao(), filtro.getUnidadeGerencial(), filtro.isIncluirSubordinadas());
		filtro.setListaUnidadeGerencial(listaUnidadeGerencialFiltro);		
		return planoGestaoService.createReportPendenciaCadastro(filtro.getPlanoGestao(), filtro.getListaUnidadeGerencial());
	}

	@Override
	public String getTitulo() {
		return "Pendência no Cadastro";
	}
}
