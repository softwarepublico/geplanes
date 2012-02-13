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

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.controller.ResourceModelAndView;
import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.view.DownloadFileServlet;
import br.com.linkcom.neo.view.code.ClassTag;
import br.com.linkcom.sgm.beans.AnexoIndicador;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.controller.report.PainelControleReportGenerator;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.PainelControleFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.Nomes;
import br.com.linkcom.sgm.util.calculos.CalculosPainelControle;

import com.lowagie.text.DocumentException;

@Bean
@Controller(path="/sgm/process/PainelControle", authorizationModule=ProcessAuthorizationModule.class)
public class PainelControle extends MultiActionController {
	
	public static final String ARVORE_PAINEL_CONTROLE = "arvore_painel_controle" ;
	private PlanoGestaoService planoGestaoService;
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}

	@SuppressWarnings("unchecked")
	@DefaultAction
	public ModelAndView principal(WebRequestContext request, PainelControleFiltro filtro) {
		
		try {
			if (!"true".equals(request.getParameter("reload"))) {
				FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);		
			}		
			
			if(filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
				request.setAttribute("itens", findItens(filtro.getPlanoGestao(), filtro.getUnidadeGerencial(), true));	
			} 
			else {
				request.setAttribute("itens", new ArrayList());
			}
		}
		catch (GeplanesException e) {
			request.addError(e.getMessage());
		}
		
		return new ModelAndView("process/painelControle", "filtro", filtro);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView reset(WebRequestContext request, PainelControleFiltro filtro){
		
		try {
			if (!"true".equals(request.getParameter("reload"))) {
				FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);		
			}
			
			if(filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
				request.setAttribute("itens", findItens(filtro.getPlanoGestao(), filtro.getUnidadeGerencial(), false));
			} 
			else {
				request.setAttribute("itens", new ArrayList());
			}	
		}
		catch (GeplanesException e) {
			request.addError(e.getMessage());
		}
		
		return new ModelAndView("process/painelControle", "filtro", filtro);
	}
	
	public ModelAndView ajaxArvore(WebRequestContext request, PainelControleFiltro filtro){		
		
		request.getServletResponse().setCharacterEncoding("UTF-8");
		
		request.setAttribute(ClassTag.RUN_METHOD_ATTRIBUTE, "imprimeItens");
		request.setAttribute("itens", findById(request.getServletRequest().getParameterValues("id"), filtro.getPlanoGestao(), filtro.getUnidadeGerencial()));
		
		return new ModelAndView("forward:/WEB-INF/jsp/sgm/process/painelControle.jsp");
	}
	
	@Input("principal")
	public ModelAndView relatorio(WebRequestContext request, PainelControleFiltro filtro) throws MalformedURLException, DocumentException, IOException{
		byte[] relatorio = new PainelControleReportGenerator(filtro.getPlanoGestao(), filtro.getUnidadeGerencial(), filtro.isApurados()).criarRelatorio();
		
		Resource resource = new Resource("application/pdf", "Geplanes_PainelControle_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".pdf", relatorio);
		return new ResourceModelAndView(resource);
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemPainelControleDTO> carregarUnidades(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencialRaiz, boolean useCache) {
		
		if (planoGestao == null) {
			throw new NullPointerException("O "+ Nomes.plano_de_gestao +" não foi informado");
		}
		
		if (planoGestao.getAnoExercicio() == null) {
			planoGestao = planoGestaoService.obtemAnoExercicio(planoGestao);
		}
		List<ItemPainelControleDTO> listaDTOs = null;
		
		//Table das arvores
		Map<Integer, List<ItemPainelControleDTO>> mapaDTO = getMapaDTO();
		
		//Tenta obter arvore de um ano exercício
		if (mapaDTO.containsKey(planoGestao.getAnoExercicio()) && useCache) {
			listaDTOs = mapaDTO.get(planoGestao.getAnoExercicio());
		}else{
			listaDTOs = new CalculosPainelControle().obtemHierarquiaCompleta(planoGestao, unidadeGerencialRaiz, false);
			mapaDTO.put( planoGestao.getAnoExercicio(), listaDTOs);
		}
		
		for (ItemPainelControleDTO dto : listaDTOs) {
			registrarArquivos(dto);
		}
		
		
		return listaDTOs;
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, List<ItemPainelControleDTO>> getMapaDTO() {
		Map<Integer, List<ItemPainelControleDTO>> mapaDTO;
		try {
			mapaDTO = (Map<Integer, List<ItemPainelControleDTO>>) NeoWeb
					.getRequestContext().getSession().getAttribute(
							ARVORE_PAINEL_CONTROLE);

			//Se nao existe mapa, cria
			if (mapaDTO == null) {
				mapaDTO = new HashMap<Integer, List<ItemPainelControleDTO>>();
				NeoWeb.getRequestContext().getSession().setAttribute(
						ARVORE_PAINEL_CONTROLE, mapaDTO);
			}
		} catch (ClassCastException e) {
			// essa exceção ocorrera em j2se...
			//em j2se nao precisamos do mapa do usuario
			return new HashMap<Integer, List<ItemPainelControleDTO>>();
		}
		return mapaDTO;
	}

	/**
	 * Registra os arquivos para poder fazer download
	 * @param dto
	 */
	private void registrarArquivos(ItemPainelControleDTO dto) {
		if(dto.getAnexos() != null && dto.getAnexos().size() > 0){
			List<AnexoIndicador> anexos = dto.getAnexos();
			for (AnexoIndicador anexo : anexos) {
				DownloadFileServlet.addCdfile(NeoWeb.getRequestContext().getSession(), anexo.getArquivo().getCdfile());
			}
		}
		List<ItemPainelControleDTO> filhos = dto.getFilhos();
		if(filhos != null && filhos.size() > 0){
			for (ItemPainelControleDTO filho : filhos) {
				registrarArquivos(filho);
			}
		}
	}

	private List<ItemPainelControleDTO>  findById(String[] ids, PlanoGestao planoGestao, UnidadeGerencial unidadeGerencialRaiz) {
		//Captura arvore completa
		List<ItemPainelControleDTO> list = carregarUnidades(planoGestao, unidadeGerencialRaiz, true);
		
		List<ItemPainelControleDTO> itensRequisitados = new ArrayList<ItemPainelControleDTO>();
		
		
		//List<String> temp = new ArrayList<String>();
		//for (String id : ids) {
			//if (!id.equals("a522")) {
			//	temp.add(id);
			//}
		//}
		
		//ids = temp.toArray( new String[] {} );
		
		// tem que organizar os ids porque o findItensRequisitados efetua um binarySearch
		Arrays.sort(ids);
		
		
		
		findItensRequisitados(itensRequisitados, list, ids);
		return itensRequisitados;
	}


	private void findItensRequisitados(List<ItemPainelControleDTO> itensRequisitados, List<ItemPainelControleDTO> list, String[] ids) {
		for (ItemPainelControleDTO item : list) {
			if(Arrays.binarySearch(ids, item.getId()) >= 0){//se é um dos requisitados preencher os filhos na lista
				itensRequisitados.addAll(resumir(item.getFilhos(), 2));
			} else {
				List<ItemPainelControleDTO> filhos = item.getFilhos();
				if(filhos != null){
					findItensRequisitados(itensRequisitados, filhos, ids);
				}
			}
		}
	}

	private List<ItemPainelControleDTO> findItens(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencialRaiz, boolean useCache) {
		
		//Captura arvore completa
		List<ItemPainelControleDTO> list = carregarUnidades(planoGestao, unidadeGerencialRaiz, useCache);
		
		//carregar somente 2 níveis
		List<ItemPainelControleDTO> listaResumida = resumir(list, 2);
		
		//carrega somente até as metas
//		List<ItemPainelControleDTO> listaResumida = resumirMeta(list);
		
		return listaResumida;
	}
	
//	private List<ItemPainelControleDTO> resumirMeta(List<ItemPainelControleDTO> list){
//		List<ItemPainelControleDTO> lista = new ArrayList<ItemPainelControleDTO>();
//		if(list != null){
//			for (ItemPainelControleDTO controleDTO : list) {
//				ItemPainelControleDTO itemCopy = controleDTO.cloneNoChildren();
//				lista.add(itemCopy);
//				if(!itemCopy.getIcone().equals("meta")){
//					itemCopy.setFilhos(resumirMeta(controleDTO.getFilhos()));
//				}
//			}
//		}
//		return list;
//	}

	private List<ItemPainelControleDTO> resumir(List<ItemPainelControleDTO> list, int i) {
		List<ItemPainelControleDTO> lista = new ArrayList<ItemPainelControleDTO>();
		if(i >= 1 && list != null){
			for (ItemPainelControleDTO controleDTO : list) {
				ItemPainelControleDTO itemCopy = controleDTO.cloneNoChildren();
				lista.add(itemCopy);
				itemCopy.setFilhos(resumir(controleDTO.getFilhos(), i - 1));
			}
		}
		return lista;
	}
	
}
