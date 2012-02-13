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


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.AuditoriaGestao;
import br.com.linkcom.sgm.beans.AuditoriaGestaoIndicador;
import br.com.linkcom.sgm.beans.AuditoriaGestaoIndicadorItem;
import br.com.linkcom.sgm.beans.FatorAvaliacao;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ItemModeloAuditoriaGestao;
import br.com.linkcom.sgm.beans.ModeloAuditoriaGestao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AuditoriaGestaoFiltro;
import br.com.linkcom.sgm.service.AuditoriaGestaoIndicadorItemService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.ItemFatorAvaliacaoService;
import br.com.linkcom.sgm.service.ItemModeloAuditoriaGestaoService;
import br.com.linkcom.sgm.service.ModeloAuditoriaGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.GeplanesUtils;


@Controller(path="/sgm/crud/AuditoriaGestao", authorizationModule=CrudAuthorizationModule.class)
public class AuditoriaGestaoCrud extends CrudController<AuditoriaGestaoFiltro, AuditoriaGestao, AuditoriaGestao> {
	
	private UnidadeGerencialService unidadeGerencialService;
	private ModeloAuditoriaGestaoService modeloAuditoriaGestaoService;
	private IndicadorService indicadorService;
	private ItemModeloAuditoriaGestaoService itemModeloAuditoriaGestaoService;
	private ItemFatorAvaliacaoService itemFatorAvaliacaoService;
	private AuditoriaGestaoIndicadorItemService auditoriaGestaoIndicadorItemService;
	
	public void setAuditoriaGestaoIndicadorItemService(
			AuditoriaGestaoIndicadorItemService auditoriaGestaoIndicadorItemService) {
		this.auditoriaGestaoIndicadorItemService = auditoriaGestaoIndicadorItemService;
	}
	public void setItemFatorAvaliacaoService(
			ItemFatorAvaliacaoService itemFatorAvaliacaoService) {
		this.itemFatorAvaliacaoService = itemFatorAvaliacaoService;
	}
	public void setItemModeloAuditoriaGestaoService(
			ItemModeloAuditoriaGestaoService itemModeloAuditoriaGestaoService) {
		this.itemModeloAuditoriaGestaoService = itemModeloAuditoriaGestaoService;
	}
	public void setIndicadorService(IndicadorService indicadorService) {
		this.indicadorService = indicadorService;
	}
	public void setModeloAuditoriaGestaoService(
			ModeloAuditoriaGestaoService modeloAuditoriaGestaoService) {
		this.modeloAuditoriaGestaoService = modeloAuditoriaGestaoService;
	}
	public void setUnidadeGerencialService(
			UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	@Override
	protected void entrada(WebRequestContext request, AuditoriaGestao form) throws Exception {
		
		if (form.getUnidadeGerencial() != null && form.getUnidadeGerencial().getPlanoGestao() != null) {
			form.setPlanoGestao(form.getUnidadeGerencial().getPlanoGestao());
		}
		
		UnidadeGerencial unidadeGerencial = form.getUnidadeGerencial();
		ModeloAuditoriaGestao modeloAuditoriaGestao = form.getModeloAuditoriaGestao();
		PlanoGestao planoGestao = form.getPlanoGestao();
		
		if(unidadeGerencial != null && modeloAuditoriaGestao != null){
			request.setAttribute("cadastro_detalhe", Boolean.TRUE);
			
			if(unidadeGerencial.getSigla() == null || planoGestao == null || planoGestao.getAnoExercicio() == null){
				unidadeGerencial = unidadeGerencialService.loadWithSiglaNomeAno(unidadeGerencial);
				form.setUnidadeGerencial(unidadeGerencial);
				form.setPlanoGestao(unidadeGerencial.getPlanoGestao());
			}
			
			if(modeloAuditoriaGestao.getNome() == null){
				modeloAuditoriaGestao = modeloAuditoriaGestaoService.loadWithNome(modeloAuditoriaGestao);
				form.setModeloAuditoriaGestao(modeloAuditoriaGestao);
			}
			
			List<Indicador> listaIndicadores = indicadorService.findAtivosByUnidadeGerencial(unidadeGerencial);
			request.setAttribute("listaIndicadores", listaIndicadores);
			
			if ("true".equals(request.getParameter("reload"))) {
				List<ItemModeloAuditoriaGestao> listaItemModeloAuditoriaGestao = itemModeloAuditoriaGestaoService.findByModelo(modeloAuditoriaGestao);
				
				FatorAvaliacao fatorAvaliacao;
				for (ItemModeloAuditoriaGestao item : listaItemModeloAuditoriaGestao) {
					fatorAvaliacao = item.getFatorAvaliacao();
					if(fatorAvaliacao != null){
						fatorAvaliacao.setListaItemFatorAvaliacao(itemFatorAvaliacaoService.findByFatorAvaliacao(fatorAvaliacao));
					}
				}
				
				List<AuditoriaGestaoIndicador> listaAuditoriaGestaoIndicador = new ArrayList<AuditoriaGestaoIndicador>();
				AuditoriaGestaoIndicador auditoriaGestaoIndicador = null;
				
				List<AuditoriaGestaoIndicadorItem> listaAuditoriaGestaoIndicadorItem = null;
				AuditoriaGestaoIndicadorItem auditoriaGestaoIndicadorItem = null;
				
				for (Indicador indicador : listaIndicadores) {
					listaAuditoriaGestaoIndicadorItem = new ArrayList<AuditoriaGestaoIndicadorItem>();
						
					for (ItemModeloAuditoriaGestao itemModeloAuditoriaGestao : listaItemModeloAuditoriaGestao) {
						auditoriaGestaoIndicadorItem = new AuditoriaGestaoIndicadorItem();
						auditoriaGestaoIndicadorItem.setItemModeloAuditoriaGestao(itemModeloAuditoriaGestao);
						
						listaAuditoriaGestaoIndicadorItem.add(auditoriaGestaoIndicadorItem);
					}
					
					auditoriaGestaoIndicador = new AuditoriaGestaoIndicador();
					auditoriaGestaoIndicador.setIndicador(indicador);
					auditoriaGestaoIndicador.setListaAuditoriaGestaoIndicadorItem(listaAuditoriaGestaoIndicadorItem);
					
					listaAuditoriaGestaoIndicador.add(auditoriaGestaoIndicador);
				}
				form.setListaAuditoriaGestaoIndicador(listaAuditoriaGestaoIndicador);
			} 
			else {
				if (form.getId() != null) {
					FatorAvaliacao fatorAvaliacao;
					for (AuditoriaGestaoIndicador bean : form.getListaAuditoriaGestaoIndicador()) {
						bean.setListaAuditoriaGestaoIndicadorItem(auditoriaGestaoIndicadorItemService.findByAuditoriaIndicador(bean));
						
						for (AuditoriaGestaoIndicadorItem item : bean.getListaAuditoriaGestaoIndicadorItem()) {
							fatorAvaliacao = item.getItemModeloAuditoriaGestao().getFatorAvaliacao();
							if(fatorAvaliacao != null){
								fatorAvaliacao.setListaItemFatorAvaliacao(itemFatorAvaliacaoService.findByFatorAvaliacao(fatorAvaliacao));
							}
						}
					}
				}
			}
			
		}
	}
	
	@Override
	public ModelAndView doListagem(WebRequestContext request, AuditoriaGestaoFiltro filtro) throws CrudException {
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		
		request.setAttribute("podeGerarRelatorio", GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/report/AuditoriaGestao", "generate", request.getServletRequest()));
		return super.doListagem(request, filtro);		
	}
	
	@Override
	protected void salvar(WebRequestContext request, AuditoriaGestao bean) throws Exception {
		if (bean.getListaAuditoriaGestaoIndicador() == null || bean.getListaAuditoriaGestaoIndicador().isEmpty()) {
			throw new GeplanesException("Não existe nenhum indicador a ser avaliado nesta Unidade Gerencial.");
		}
		else {
			super.salvar(request, bean);
		}
	}	
}