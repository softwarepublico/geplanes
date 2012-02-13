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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Command;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.DTO.ApresentacaoResultadosDTO;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoApresentacaoEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoResultadoEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoTipoEnum;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.Nomes;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;
import br.com.linkcom.sgm.util.calculos.CalculosPainelControle;



/**
 * @author Marcus Abreu
 */
@Controller(path="/sgm/process/ApresentacaoResultados", authorizationModule=ProcessAuthorizationModule.class)
@Bean
public class ApresentacaoResultadosProcess extends MultiActionController{
	
	private IndicadorService indicadorService;
	private PlanoGestaoService planoGestaoService;
	private UsuarioService usuarioService;
	
	public void setIndicadorService(IndicadorService indicadorService) {
		this.indicadorService = indicadorService;
	}
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	

	@DefaultAction
	@Command(session=true)	
	public ModelAndView inicializar(WebRequestContext request, ApresentacaoResultadosDTO bean) {
		// init
		request.setLastAction("inicializar");
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(bean);
		}		
		
		if (bean.getUnidadeGerencial() != null && !usuarioService.isAcessoConsultaAutorizado(bean.getUnidadeGerencial())) {
			request.setAttribute("acessoNaoAutorizado",true);
			request.addError("Você não tem permissão para acessar os dados dessa unidade gerencial.");		
		}
		else {
			if ( bean.getPlanoGestao() != null ) {
				bean.setPlanoGestao(planoGestaoService.load(bean.getPlanoGestao()));
				indicadorService.defineDatas(bean);
			}
			else {
				bean.setDataInicial(null);
				bean.setDataFinal(null);
			}
			
			
			if (bean.getTipoGrafico() == null) {
				bean.setTipoGrafico(GraficoTipoEnum.ACOMPANHAMENTO);
			}
			if (bean.getFormaApresentacao() == null) {
				bean.setFormaApresentacao(GraficoApresentacaoEnum.LINHA);
			}
			
			bean = setListaIndicadores(bean);
			
			request.setAttribute("mapaTipoGrafico",montaMapaTipoGrafico());
			request.setAttribute("mapaFormaApresentacao",montaMapaFormaApresentacao());
		}
		return new ModelAndView("process/apresentacaoResultados", "filtro", bean);
	}
	
	
    public ModelAndView executar(WebRequestContext request, ApresentacaoResultadosDTO bean) {
		bean = setListaIndicadores(bean);
		request.setAttribute("mapaTipoGrafico",montaMapaTipoGrafico());
		request.setAttribute("mapaFormaApresentacao",montaMapaFormaApresentacao());
		return new ModelAndView("process/apresentacaoResultados", "filtro", bean);
    }
	
	@Override	
	protected void validate(Object obj, BindException errors, String acao) {
		ApresentacaoResultadosDTO bean = (ApresentacaoResultadosDTO) obj;
		if ( bean.getPlanoGestao() != null ) {
			bean.setPlanoGestao(planoGestaoService.load(bean.getPlanoGestao()));
		}
		if (bean.getDataInicial().compareTo(bean.getDataFinal()) >= 0) {
			errors.reject("","A Data final dever ser maior que a Data inicial.");
		}
		else if (bean.getDataInicial().get(Calendar.YEAR) != bean.getPlanoGestao().getAnoExercicio()) {
			errors.reject("","A Data inicial deve ser do mesmo ano que o " + Nomes.plano_de_gestao);
		}
		else if (bean.getDataFinal().get(Calendar.YEAR) != bean.getPlanoGestao().getAnoExercicio()) {
			errors.reject("","A Data final deve ser do mesmo ano que o " + Nomes.plano_de_gestao);
		}
		super.validate(obj, errors, acao);
	}

	@SuppressWarnings("unchecked")
	@Command(validate=true)
	@Input("executar")
    public ModelAndView exibirResultado(WebRequestContext request, ApresentacaoResultadosDTO bean) {
    	Set<String> listaEpoca = new LinkedHashSet<String>();
    	Map<String,Calendar> mapaEpocaDataInicial = new HashMap<String, Calendar>();
		List<ItemPainelControleDTO> listaDTOs;
		ItemPainelControleDTO itemPainelControleDTO;
    	List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
    	Set<AcompanhamentoIndicador>  listSetAcompanhamentoIndicador;
    	
    	bean.setPlanoGestao(planoGestaoService.load(bean.getPlanoGestao()));
    	bean = setListaIndicadores(bean);
    	request.setAttribute("mapaTipoGrafico",montaMapaTipoGrafico());
    	request.setAttribute("mapaFormaApresentacao",montaMapaFormaApresentacao());
    	
    	if (bean.getIdIndicadorSelecionado() != null) {
    		bean.setIndicadorSelecionado(new Indicador(bean.getIdIndicadorSelecionado()));
    		
    		// Seta o tipo do Resultado
    		if (bean.getIdIndicadorSelecionado().intValue() > 0) {
    			bean.setTipoResultado(GraficoResultadoEnum.POR_INDICADOR);
    		}
    		else {
    			bean.setTipoResultado(GraficoResultadoEnum.POR_UNIDADEGERENCIAL);
    		}
    	}    	
    	
    	if (GraficoResultadoEnum.POR_INDICADOR.equals(bean.getTipoResultado())) {
        	// Preenche as informações do indicador selecionado
    		if (bean.getIndicadorSelecionado() != null) {
    			bean.setIndicadorSelecionado(indicadorService.obtemSomatorioColecaoAcompanhamento(bean.getIndicadorSelecionado(),bean.getDataInicial(),bean.getDataFinal()));
    			
    			if (bean.getIndicadorSelecionado() != null && bean.getIndicadorSelecionado().getAcompanhamentosIndicador() != null && !bean.getIndicadorSelecionado().getAcompanhamentosIndicador().isEmpty()) {
					CalculosAuxiliares.nomeiaEpocas(bean.getIndicadorSelecionado().getFrequencia(), bean.getIndicadorSelecionado().getAcompanhamentosIndicador());	    				
    				for (AcompanhamentoIndicador acompanhamentoIndicador : bean.getIndicadorSelecionado().getAcompanhamentosIndicador()) {						
    					listaEpoca.add(acompanhamentoIndicador.getEpoca());
    					mapaEpocaDataInicial.put(acompanhamentoIndicador.getEpoca(),acompanhamentoIndicador.getDataInicial());
					}
    				
    	        	// Preenche os Acompanhamentos dos Indicadores com Valores Vazios, 
    	    		// caso não exista Acompanhamento para a Época Indicada
    	        	AcompanhamentoIndicador acompanhamentoIndicadorNovo;
    	        	Boolean encontrouAcompanhamento;
    	        	
    	    		listaAcompanhamentoIndicador = new ArrayList<AcompanhamentoIndicador>(bean.getIndicadorSelecionado().getAcompanhamentosIndicador());
    	        	for (String epoca : listaEpoca) {
    	        		encontrouAcompanhamento = false;
    	        		
    	        		for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoIndicador) {
    						if (epoca.equals(acompanhamentoIndicador.getEpoca())) {
    							encontrouAcompanhamento = true;
    							break;
    						}
    					}
    	        		
    	        		if (!encontrouAcompanhamento) {
    	    				acompanhamentoIndicadorNovo = new AcompanhamentoIndicador();
    	    				acompanhamentoIndicadorNovo.setEpoca(epoca);
    	    				acompanhamentoIndicadorNovo.setDataInicial(mapaEpocaDataInicial.get(epoca));
    	    				listaAcompanhamentoIndicador.add(acompanhamentoIndicadorNovo);
    	        		}
    	    		}
    	        	Collections.sort(listaAcompanhamentoIndicador, new BeanComparator("dataInicial"));	        	
    	        	CalculosAuxiliares.agruparPorFator(listaAcompanhamentoIndicador, bean.getIndicadorSelecionado().getFrequencia().getFatorDivisao(), bean.getIndicadorSelecionado().getPercentualTolerancia());
    	        	listSetAcompanhamentoIndicador = new ListSet<AcompanhamentoIndicador>(AcompanhamentoIndicador.class);
    	        	listSetAcompanhamentoIndicador.addAll(listaAcompanhamentoIndicador);
    	        	bean.getIndicadorSelecionado().setAcompanhamentosIndicador(listSetAcompanhamentoIndicador);
    			}    			
    		}
    		request.setAttribute("tituloIndicador","Indicador");        	
    	}
    	else {
    		// Calcula os percentuais dos acompanhamentos de todos os indicadores da UG .
    		listaDTOs = new CalculosPainelControle().obtemHierarquiaCompleta(bean.getPlanoGestao(),bean.getUnidadeGerencial(), true);
    		
    		if (listaDTOs != null && !listaDTOs.isEmpty()) {
    			// Obtém somente os dados referentes à UG em questão.			
    			itemPainelControleDTO = listaDTOs.get(0);
    			
    			listaAcompanhamentoIndicador = itemPainelControleDTO.getAcompanhamentos();
    			
    			bean.getIndicadorSelecionado().setNome(bean.getUnidadeGerencial().getSigla());
    			listSetAcompanhamentoIndicador = new ListSet<AcompanhamentoIndicador>(AcompanhamentoIndicador.class);
    			listSetAcompanhamentoIndicador.addAll(listaAcompanhamentoIndicador);
    			bean.getIndicadorSelecionado().setAcompanhamentosIndicador(listSetAcompanhamentoIndicador);
    			
    			CalculosAuxiliares.nomeiaEpocas(FrequenciaIndicadorEnum.TRIMESTRAL, bean.getIndicadorSelecionado().getAcompanhamentosIndicador());
				for (AcompanhamentoIndicador acompanhamentoIndicador : bean.getIndicadorSelecionado().getAcompanhamentosIndicador()) {
					acompanhamentoIndicador.setDataInicial(Calendar.getInstance());
					listaEpoca.add(acompanhamentoIndicador.getEpoca());
					mapaEpocaDataInicial.put(acompanhamentoIndicador.getEpoca(),acompanhamentoIndicador.getDataInicial());
				}
    		}
    		request.setAttribute("tituloIndicador","Alcance de Metas Institucionais (AMI)");
    	}
    	
    	if (bean.getIndicadorSelecionado() != null) {
	    	request.setAttribute("melhorDoIndicador", bean.getIndicadorSelecionado().getMelhor());
	    	request.setAttribute("listaEpoca",listaEpoca);    	
	    	request.getSession().setAttribute("indicadorSelecionado",bean.getIndicadorSelecionado());
	    	
	    	List<Indicador> listaIndicadorSelecionado = new ArrayList<Indicador>();
	    	listaIndicadorSelecionado.add(bean.getIndicadorSelecionado());
	    	request.setAttribute("listaIndicadorSelecionado",listaIndicadorSelecionado);
					
			// Verifica o Tipo de Gráfico Selecionado
			switch (bean.getTipoGrafico()) {
				case ACOMPANHAMENTO:
					request.setAttribute("tipoAcompanhamento",Boolean.TRUE);
					break;
				case PERCENTUAL:
					request.setAttribute("tipoPercentual",Boolean.TRUE);			
					break;			
				case FAROL:
					request.setAttribute("tipoFarol",Boolean.TRUE);			
					break;
				default:
					break;
				}
    	}
    	else {
	    	request.setAttribute("semDados",Boolean.TRUE);
    	}
    	request.setAttribute("exibirResultado",Boolean.TRUE);
		return new ModelAndView("process/apresentacaoResultados", "filtro", bean);
    }
    
    public ApresentacaoResultadosDTO setListaIndicadores(ApresentacaoResultadosDTO bean) {
		if (bean.getUnidadeGerencial() != null && bean.getPlanoGestao() != null) {
			bean.setListaIndicadores(indicadorService.findAtivosByUnidadeGerencial(bean.getUnidadeGerencial()));
			Indicador indicadorAMI = new Indicador();
			indicadorAMI.setId(0);
			indicadorAMI.setNome("ALCANCE DE METAS INSTITUCIONAIS (AMI)");
			indicadorAMI.setMelhor(null);
			indicadorAMI.setFrequencia(null);
			bean.getListaIndicadores().add(indicadorAMI);
		}
		else {
			bean.setListaIndicadores(null) ;
			bean.setIdIndicadorSelecionado(null);
			bean.setIndicadorSelecionado(null);
		}
		return bean;
    }
    
	private Map<String,String> montaMapaTipoGrafico() {
		Map<String,String> mapaTipoGrafico = new LinkedHashMap<String, String>();
		
		GraficoTipoEnum[] values = GraficoTipoEnum.values();
		for (GraficoTipoEnum graficoTipoEnum : values) {
			mapaTipoGrafico.put(graficoTipoEnum.getName(), graficoTipoEnum.toString());
		}
		
		return mapaTipoGrafico;
	}
	
	private Map<String,String> montaMapaFormaApresentacao() {
		Map<String,String> mapaFormaApresentacao = new LinkedHashMap<String, String>();
		
		GraficoApresentacaoEnum[] values = GraficoApresentacaoEnum.values();
		for (GraficoApresentacaoEnum graficoApresentacaoEnum : values) {
			mapaFormaApresentacao.put(graficoApresentacaoEnum.getName(), graficoApresentacaoEnum.toString());
		}
		
		return mapaFormaApresentacao;
	}
}