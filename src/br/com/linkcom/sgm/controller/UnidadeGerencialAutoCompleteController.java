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

import java.util.List;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.view.ajax.View;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.UnidadeGerencialTreeFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;


@Bean
@Controller(path="/util/UnidadeGerencialAutoComplete")
public class UnidadeGerencialAutoCompleteController extends MultiActionController {

	UnidadeGerencialService unidadeGerencialService;
	PlanoGestaoService planoGestaoService;
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	@DefaultAction
	public void view(WebRequestContext request, UnidadeGerencialTreeFiltro filtro){
		List<UnidadeGerencial> find = unidadeGerencialService.findAutocomplete(filtro.getQ(), filtro.getPlanoGestao());
		request.getServletResponse().setCharacterEncoding("UTF-8");
		View.getCurrent().eval(criarEstrutura(find));
	}

	private String criarEstrutura(List<UnidadeGerencial> find) {
		StringBuilder builder = new StringBuilder();
		builder.append("<script>");
		if(find != null && find.size() > 0){
			for (UnidadeGerencial ug : find) {
				builder.append(ug.getSigla() + "|" + ug.getId() + "\n");
			}
//			builder.delete(builder.length() - 2, builder.length());
		}
		builder.append("</script>");
		return builder.toString();
	}

}
