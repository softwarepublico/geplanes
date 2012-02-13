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

import java.io.IOException;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.filtro.IndicadorFiltro;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Controller(path={"/util/crud/Indicador"}, authorizationModule=CrudAuthorizationModule.class)
public class IndicadorCrud extends CrudController<IndicadorFiltro, Indicador, Indicador> {
	private IndicadorService indicadorService;
	private UnidadeGerencialService unidadeGerencialService;
	
	public void setIndicadorService(IndicadorService indicadorService) {
		this.indicadorService = indicadorService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	@Override
	public ModelAndView doListagem(WebRequestContext request, IndicadorFiltro filtro) throws CrudException {
		
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		
		if (request.getParameter("unidadeGerencialId") != null) {
			request.setAttribute("unidadeGerencialId", request.getParameter("unidadeGerencialId"));
		}
		
		if (request.getParameter("objetivoMapaEstrategicoId") != null) {
			request.setAttribute("objetivoMapaEstrategicoId", request.getParameter("objetivoMapaEstrategicoId"));
		}
		
		return super.doListagem(request, filtro);
	}
	
	@Override
	protected ListagemResult<Indicador> getLista(WebRequestContext request, IndicadorFiltro filtro) {
		ListagemResult<Indicador> listagemResult = super.getLista(request, filtro);
		List<Indicador> listaIndicador = listagemResult.list();
		if (filtro.getPlanoGestao() == null || filtro.getUnidadeGerencial() == null) {
			listaIndicador.clear();
		}
		return listagemResult;
	}
	
    public ModelAndView copiar(WebRequestContext request, Indicador indicador) {
    	
    		UnidadeGerencial unidadeGerencialNova = new UnidadeGerencial();
    		unidadeGerencialNova.setId(Integer.valueOf(request.getParameter("unidadeGerencialId").toString()));
    		unidadeGerencialNova = unidadeGerencialService.loadWithSiglaNomeAno(unidadeGerencialNova);
    		
    		ObjetivoMapaEstrategico objetivoMapaEstrategicoNovo = new ObjetivoMapaEstrategico(); 
    		objetivoMapaEstrategicoNovo.setId(Integer.valueOf(request.getParameter("objetivoMapaEstrategicoId").toString()));
    		
    		indicador = indicadorService.load(indicador);
    	
    		request.getServletResponse().setContentType("text/html");
			String string = 
				"<script language=\"Javascript\">" +
				"window.opener.recarregarTela();" +
				"window.close();</script>";
			
			try {
				indicadorService.copiaIndicador(unidadeGerencialNova, objetivoMapaEstrategicoNovo, indicador, true);
				request.getServletResponse().getWriter().println(string);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;		
	}
}
