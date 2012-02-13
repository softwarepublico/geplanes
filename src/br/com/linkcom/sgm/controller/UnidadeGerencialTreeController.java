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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.UnidadeGerencialTreeFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.util.GeplanesUtils;


@Bean
@Controller(path="/util/UnidadeGerencialTreeView")
public class UnidadeGerencialTreeController extends MultiActionController {

	UnidadeGerencialService unidadeGerencialService;
	PlanoGestaoService planoGestaoService;
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	@DefaultAction
	public ModelAndView view(WebRequestContext request, UnidadeGerencialTreeFiltro filtro){
		List<UnidadeGerencial> find = unidadeGerencialService.find(null, 5, filtro.getPlanoGestao());
		//esse código será utilizado se modificar o algoritmo (escolher/limpar) do botao do treeview
//		List<UnidadeGerencial> allParents = unidadeGerencialService.findAllParents(filtro.getUnidadeGerencial());
//		for (UnidadeGerencial unidadeGerencial : allParents) {
//			System.out.println(unidadeGerencial.getNome());
//		}
		StringBuilder builder = new StringBuilder();
		
		UnidadeGerencial pai = null;
		criarEstrutura(find, builder, pai, true);
		builder.append("document.propriedade = '"+request.getParameter("propriedade")+"'");
		request.setAttribute("codigo", builder.toString());
		request.setAttribute("classUnidadeGerencial", UnidadeGerencial.class.getName());
		request.setAttribute("descPlanoGestao", planoGestaoService.load(filtro.getPlanoGestao()).getAnoExercicio());
		return new ModelAndView("unidadeGerencialTreeView");
	}
	/**
	 * Carrega determinados itens via Ajax
	 * @param request
	 * @param filtro
	 * @throws IOException
	 */
	public void load(WebRequestContext request, UnidadeGerencialTreeFiltro filtro) throws IOException{
		List<UnidadeGerencial> unidadesGerenciais = filtro.getUnidadesGerenciais();
		StringBuilder builder = new StringBuilder();
		if (unidadesGerenciais != null) {
			for (UnidadeGerencial unidadePai : unidadesGerenciais) {
				List<UnidadeGerencial> find = unidadeGerencialService.find(unidadePai, 5, filtro.getPlanoGestao());
				criarEstrutura(find, builder, unidadePai, false);
			}
		}
		HttpServletResponse response = request.getServletResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(builder.toString());
	}

	private void criarEstrutura(List<UnidadeGerencial> find, StringBuilder builder, UnidadeGerencial pai, boolean first) {
		for (UnidadeGerencial unidade : find) {
			
			String sigla = GeplanesUtils.escape( unidade.getSigla() );
			String nome = GeplanesUtils.escape( unidade.getNome() );
			
			builder.append("var node"+unidade.getId()+" = new Node('"+unidade.getId()+"');");
			builder.append("node"+unidade.getId()+".info = '"+sigla+" "+nome+"'; ");
			builder.append("node"+unidade.getId()+".label = '"+sigla+"'; ");
			builder.append("var column1"+unidade.getId()+" = node"+unidade.getId()+".newColumn();");
			builder.append("column1"+unidade.getId()+".innerHTML = '<span id =\\'td"+unidade.getId()+"\\' onclick=\\'changeSelectState(findNode("+unidade.getId()+"), event);\\' ondblclick=\\'select(findNode("+unidade.getId()+"), event);selecionar();\\'>"+sigla+" - "+nome+"</span>';");
			//builder.append("column1"+unidade.getId()+".onselect = function(node){changeSelectState(node);};");
			if(unidade.getNumeroFilhos() != null && unidade.getNumeroFilhos() > 0){
				builder.append("node"+unidade.getId()+".hasChild = true;");
			}
			if(first){//first indica se tá rolando o jsp se não é ajax
				if(pai == null){
					builder.append("treeTable.addNode(node"+unidade.getId()+");");
				} else {
					builder.append("node"+pai.getId()+".addChild(node"+unidade.getId()+");");
				}
			} else {
				builder.append("findNode("+pai.getId()+").addChild(node"+unidade.getId()+");");
			}

			if(unidade.getFilhos().size() > 0){
				criarEstrutura(unidade.getFilhos(), builder, unidade, first);
			}
		}
	}
}
