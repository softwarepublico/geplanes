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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Command;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo.view.ajax.View;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.AnexoIndicador;
import br.com.linkcom.sgm.beans.DistribuicaoPesosIndicadores;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PainelIndicadorFiltro;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.service.AcompanhamentoIndicadorService;
import br.com.linkcom.sgm.service.AnexoIndicadorService;
import br.com.linkcom.sgm.service.DistribuicaoPesosIndicadoresService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.IniciativaService;
import br.com.linkcom.sgm.service.ObjetivoMapaEstrategicoService;
import br.com.linkcom.sgm.service.PainelIndicadorFiltroService;
import br.com.linkcom.sgm.service.PerspectivaMapaEstrategicoService;
import br.com.linkcom.sgm.service.PlanoAcaoService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.Nomes;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;



/**
 * @author Rodrigo Duarte
 *
 */
@Controller(path="/sgm/process/DistribuicaoPesosIndicadores", authorizationModule=ProcessAuthorizationModule.class)
@Bean
public class DistribuicaoPesosIndicadoresProcess extends MultiActionController{
	
	private PlanoGestaoService planoGestaoService;
	private UsuarioService usuarioService;
	private DistribuicaoPesosIndicadoresService distribuicaoPesosIndicadoresService;
	private IndicadorService indicadorService;
	private AnexoIndicadorService anexoIndicadorService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	private UnidadeGerencialService unidadeGerencialService;
	private PlanoAcaoService planoAcaoService;
	private IniciativaService iniciativaService;
	private PainelIndicadorFiltroService painelIndicadorFiltroService;
	
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	public void setAnexoIndicadorService(AnexoIndicadorService anexoIndicadorService) {this.anexoIndicadorService = anexoIndicadorService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setDistribuicaoPesosIndicadoresService(DistribuicaoPesosIndicadoresService distribuicaoPesosIndicadoresService) {this.distribuicaoPesosIndicadoresService = distribuicaoPesosIndicadoresService;}
	public void setIndicadorService(IndicadorService indicadorService) { this.indicadorService = indicadorService;}
	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;}
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {this.planoAcaoService = planoAcaoService;}
	public void setIniciativaService(IniciativaService iniciativaService) {this.iniciativaService = iniciativaService;}
	public void setPainelIndicadorFiltroService(PainelIndicadorFiltroService painelIndicadorFiltroService) {this.painelIndicadorFiltroService = painelIndicadorFiltroService;}
	
	
	@DefaultAction	
    public ModelAndView executar(WebRequestContext request, DistribuicaoPesosIndicadores bean) {
		
		request.setLastAction("executar");
		
		if (bean.getId_indicador() != null) {
			Indicador indicador = indicadorService.loadWithUnidadePlanoGestao(new Indicador(bean.getId_indicador()));

			UnidadeGerencial unidadeGerencial = indicador.getUnidadeGerencial();
			bean.setPlanoGestao(unidadeGerencial.getPlanoGestao());
			bean.setUnidadeGerencial(unidadeGerencial);
		}
		
		if (bean.getId_objetivoMapaEstrategico() != null){
			ObjetivoMapaEstrategico objetivoMapaEstrategico = objetivoMapaEstrategicoService.loadWithIDsUnidadePlanoGestao(new ObjetivoMapaEstrategico(bean.getId_objetivoMapaEstrategico()));
			
			UnidadeGerencial unidadeGerencial = objetivoMapaEstrategico.getPerspectivaMapaEstrategico().getMapaEstrategico().getUnidadeGerencial();
			bean.setPlanoGestao(unidadeGerencial.getPlanoGestao());
			bean.setUnidadeGerencial(unidadeGerencial);
		}
		
		/*** Seta valores default para o filtro ***/		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(bean);
			bean.setListaPerspectivaMapaEstrategico(null);
		}		
				
		if (bean.getPlanoGestao() != null && bean.getUnidadeGerencial() != null) {
			
			bean.setListaPerspectivaMapaEstrategico(distribuicaoPesosIndicadoresService.carregaElementosPainelIndicador(bean.getUnidadeGerencial()));			
			
			/*** insere informações no request ***/
			distribuicaoPesosIndicadoresService.configuraParametrosTela(request, bean);	
		}
		
		return new ModelAndView("process/distribuicaoPesosIndicadores", "filtro", bean);
    }
	
	
	public ModelAndView error(WebRequestContext request, DistribuicaoPesosIndicadores bean) {

    	if (bean.getPlanoGestao() != null && bean.getUnidadeGerencial() != null) {    		
    		distribuicaoPesosIndicadoresService.configuraParametrosTela(request, bean);    		
    	}
    	
    	return new ModelAndView("process/distribuicaoPesosIndicadores", "filtro", bean);
    }
 
	@Override
	@Input("executar")
	protected void validate(Object obj, BindException errors, String acao) {
		
		DistribuicaoPesosIndicadores bean = (DistribuicaoPesosIndicadores) obj;		
		
		if (bean.getListaPerspectivaMapaEstrategico().isEmpty()) {
			errors.reject("", "Não é possível salvar, pois não há objetivo estratégico para esta unidade gerencial no "+ Nomes.plano_de_gestao +" selecionado");
		}
			
		PlanoGestao planoGestao = planoGestaoService.load( bean.getPlanoGestao() );		
		
		if (!usuarioService.isUsuarioLogadoAdmin() && !usuarioService.isUsuarioLogadoResponsavelUG(bean.getUnidadeGerencial())) {
			errors.reject("", "Somente administradores e o responsável pela UG têm permissão para alterar esta tela");
		}
		
		/*** A data limite nao pode ser ultrapassada, exceto se o usuário for administrador ***/		
		if (!usuarioService.isUsuarioLogadoAdmin() && !planoGestaoService.dataLimiteCriacaoMetasIndicadoresNaoExpirada(planoGestao)) {
			errors.reject("", "A data limite para criação de indicadores/iniciativa/planos de ação está ultrapassada.");
		}
				
		/*** Para um mesmo objetivo estratégico, a soma dos pesos dos indicadores deve ser igual a 100 ***/		
		distribuicaoPesosIndicadoresService.confereSomaPesoIndicadores(bean, errors);		
	}
	
	@Command(validate=true)
	@Input("error")
	public ModelAndView salvar(WebRequestContext request, DistribuicaoPesosIndicadores bean){		
	
		distribuicaoPesosIndicadoresService.salvar(bean);		
		
		request.setLastAction("executar");
		request.addMessage("Indicadores cadastrados com sucesso", MessageType.INFO);
		return executar(request, bean);
	}
	
	@Input("executar")
	public ModelAndView popUpConfiguraFiltro(WebRequestContext request, DistribuicaoPesosIndicadores filtro) {
		
		String idStringUnidadeGerencial = request.getParameter("idUnidadeGerencial");
		
		if (idStringUnidadeGerencial != null && !idStringUnidadeGerencial.equals("")) {
			filtro.setUnidadeGerencial(new UnidadeGerencial(Integer.valueOf(idStringUnidadeGerencial)));
		
			// Lista os objetivos estratégicos selecionados
			List<ObjetivoMapaEstrategico> findAllSelected = new ArrayList<ObjetivoMapaEstrategico>();
			List<PainelIndicadorFiltro> listaPainelIndicadorFiltro = painelIndicadorFiltroService.findByUnidadeGerencial(filtro.getUnidadeGerencial());
			if (listaPainelIndicadorFiltro != null && !listaPainelIndicadorFiltro.isEmpty()) {
				for (PainelIndicadorFiltro painelIndicadorFiltro : listaPainelIndicadorFiltro) {
					findAllSelected.add(painelIndicadorFiltro.getObjetivoMapaEstrategico());
				}
			}
			filtro.setListaObjetivoMapaEstrategicoFiltro(findAllSelected);		
			
			// Lista os objetivos estratégicos possíveis
			List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(filtro.getUnidadeGerencial(),true,"perspectivaMapaEstrategico.ordem");
			if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
					List<ObjetivoMapaEstrategico> lista = objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(filtro.getUnidadeGerencial(), perspectivaMapaEstrategico, true, true, false, false, false, "objetivoMapaEstrategico.id");
					
					// Caso um objetivo estratégico já tenha um indicador ou uma iniciativa associada não poderá ser desmarcado
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : lista) {
						if ((objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) || 
							(objetivoMapaEstrategico.getListaIniciativa() != null && !objetivoMapaEstrategico.getListaIniciativa().isEmpty())) {
							objetivoMapaEstrategico.setSomenteLeitura(true);
						}
					}
					
					perspectivaMapaEstrategico.setListaObjetivoMapaEstrategico(lista);
				}
			}			
			request.setAttribute("listaPerspectivaMapaEstrategico", listaPerspectivaMapaEstrategico);
		}
		return new ModelAndView("direct:popup/popupConfiguraFiltroPainelIndicador").addObject("filtro", filtro);
	}
	
	@Input("executar")
	public void salvaPopUpConfiguraFiltro(WebRequestContext request, DistribuicaoPesosIndicadores filtro) {
		
		if (filtro.getUnidadeGerencial() != null) {
			
			// Remove os itens não selecionados da lista
			List<ObjetivoMapaEstrategico> listaAPP = new ArrayList<ObjetivoMapaEstrategico>();
			if (filtro.getListaObjetivoMapaEstrategicoFiltro() != null && !filtro.getListaObjetivoMapaEstrategicoFiltro().isEmpty()) {
				for (ObjetivoMapaEstrategico objetivoMapaEstrategicoFiltro : filtro.getListaObjetivoMapaEstrategicoFiltro()) {
					if (objetivoMapaEstrategicoFiltro.getId() != null) {
						listaAPP.add(objetivoMapaEstrategicoFiltro);
					}
				}
			}
			
			// Busca os objetivos estratégicos selecionados do Banco de Dados
			List<PainelIndicadorFiltro> listaDB = painelIndicadorFiltroService.findByUnidadeGerencial(filtro.getUnidadeGerencial());
			
			List<PainelIndicadorFiltro> listaForInsert = new ArrayList<PainelIndicadorFiltro>();
			List<PainelIndicadorFiltro> listaForDelete = new ArrayList<PainelIndicadorFiltro>();
			
			boolean found;
			
			if (listaAPP != null) {
				for (ObjetivoMapaEstrategico objetivoMapaEstrategicoAPP : listaAPP) {
					found = false;
					if (listaDB != null) {
						for (PainelIndicadorFiltro painelIndicadorFiltroDB : listaDB) {
							if (objetivoMapaEstrategicoAPP.equals(painelIndicadorFiltroDB.getObjetivoMapaEstrategico())) {
								found = true;
								break;
							}
						}
					}
					
					if (!found) {
						listaForInsert.add(new PainelIndicadorFiltro(filtro.getUnidadeGerencial(), objetivoMapaEstrategicoAPP));
					}
				}
			}		
			
			if (listaDB != null) {
				for (PainelIndicadorFiltro painelIndicadorFiltroDB : listaDB) {
					found = false;
					if (listaAPP != null) {
						for (ObjetivoMapaEstrategico objetivoMapaEstrategicoAPP : listaAPP) {
							if (painelIndicadorFiltroDB.getObjetivoMapaEstrategico().equals(objetivoMapaEstrategicoAPP)) {
								found = true;
								break;
							}
						}
					}
					
					if (!found) {
						listaForDelete.add(painelIndicadorFiltroDB);
					}
				}
			}
			
			// Insere os novos registros
			for (PainelIndicadorFiltro painelIndicadorFiltroForInsert : listaForInsert) {
				painelIndicadorFiltroService.saveOrUpdate(painelIndicadorFiltroForInsert);
			}
			
			// Remove os registros não mais selecionados
			for (PainelIndicadorFiltro painelIndicadorFiltroForDelete : listaForDelete) {
				painelIndicadorFiltroForDelete.setUnidadeGerencial(null);
				painelIndicadorFiltroService.delete(painelIndicadorFiltroForDelete);
			}			
		}
		
		request.getServletResponse().setContentType("text/html");
		View.getCurrent().println("<script>parent.recarregarTela(); parent.mostraDivCarregando(); parent.$.akModalRemove(true);</script>");		
	}	
	
	@Input("executar")
	public ModelAndView popUpPlanoAcao(WebRequestContext request, Iniciativa bean) throws CrudException {
		String idStringUnidadeGerencial = request.getParameter("idUnidadeGerencial");
		String idStringObjetivoMapaEstrategico = request.getParameter("idObjetivoMapaEstrategico");
		String idStringIniciativa = request.getParameter("idIniciativa");
		
		Iniciativa iniciativa = new Iniciativa();
		iniciativa.setIdxIniciativa(request.getParameter("idxIniciativa"));
		iniciativa.setIdxObjetivoMapaEstrategico(request.getParameter("idxObjetivoMapaEstrategico"));
		iniciativa.setIdxPerspectivaMapaEstrategico(request.getParameter("idxPerspectivaMapaEstrategico"));
		iniciativa.setListaPlanoAcao(new ArrayList<PlanoAcao>());
		UnidadeGerencial unidadeGerencial = null;
		ObjetivoMapaEstrategico objetivoMapaEstrategico = null;
		
		if (idStringUnidadeGerencial != null && !idStringUnidadeGerencial.equals("") && idStringObjetivoMapaEstrategico != null && !idStringObjetivoMapaEstrategico.equals("")) {
			unidadeGerencial = new UnidadeGerencial(Integer.valueOf(idStringUnidadeGerencial));
			unidadeGerencial = unidadeGerencialService.loadWithPlanoGestao(unidadeGerencial);
			
			if (idStringObjetivoMapaEstrategico != null && !idStringObjetivoMapaEstrategico.equals("")) {
				objetivoMapaEstrategico = new ObjetivoMapaEstrategico(Integer.parseInt(idStringObjetivoMapaEstrategico));
			}
			
			if (idStringIniciativa != null && !idStringIniciativa.equals("")) {
				iniciativa.setId(Integer.valueOf(idStringIniciativa));
				iniciativa.setListaPlanoAcao(planoAcaoService.findByUGIniciativa(unidadeGerencial, iniciativa));
			}			
			
			Boolean isUsuarioAdmin = usuarioService.isUsuarioLogadoAdmin();
			Boolean isUsuarioResponsavelUG = usuarioService.isUsuarioLogadoResponsavelUG(unidadeGerencial);
			
			Boolean dtLimCrIndNaoExp = planoGestaoService.dataLimiteCriacaoMetasIndicadoresNaoExpirada(unidadeGerencial.getPlanoGestao());
			if ((dtLimCrIndNaoExp && distribuicaoPesosIndicadoresService.usuarioTemPermissaoEscrita(isUsuarioAdmin, isUsuarioResponsavelUG)) || isUsuarioAdmin) {
				request.setAttribute("showButton", true);
			}
			else {
				request.setAttribute("showButton", false);
			}
			
			request.setAttribute("unidadeGerencialID", unidadeGerencial.getId());
			request.setAttribute("objetivoMapaEstrategicoID", objetivoMapaEstrategico.getId());
			request.setAttribute("iniciativa", iniciativa);
		}
		return new ModelAndView("forward:/popup/popUpPlanoAcao.jsp");
	}	
	
	@Input("executar")
	public ModelAndView popUpDetalhamentoIndicador(WebRequestContext request, Indicador bean) throws CrudException {
		
		Indicador indicador;
		PlanoGestao planoGestao = null;
		
		if (bean.getReloadTela() != null && bean.getReloadTela()) {
			indicador = bean;
		} 
		else {
			indicador = new Indicador();
			indicador.setStatus(StatusIndicadorEnum.APROVADO);
			
			if (request.getParameter("idIndicador") != null && !request.getParameter("idIndicador").equals("")) {
				indicador.setId(Integer.parseInt(request.getParameter("idIndicador")));
				indicador = indicadorService.loadForEntrada(indicador);
			} 
			else if(request.getParameter("idObjetivoMapaEstrategico") != null && !request.getParameter("idObjetivoMapaEstrategico").equals("")) {
				ObjetivoMapaEstrategico objetivoMapaEstrategico = new ObjetivoMapaEstrategico(Integer.parseInt(request.getParameter("idObjetivoMapaEstrategico")));
				indicador.setObjetivoMapaEstrategico(objetivoMapaEstrategicoService.loadWithIDsUnidadePlanoGestao(objetivoMapaEstrategico));
			}
			
			indicador.setIdxIndicador(request.getParameter("idxIndicador"));
			indicador.setIdxObjetivoMapaEstrategico(request.getParameter("idxObjetivoMapaEstrategico"));
			indicador.setIdxPerspectivaMapaEstrategico(request.getParameter("idxPerspectivaMapaEstrategico"));
			
			if (request.getParameter("idUnidadeGerencial") != null && !request.getParameter("idUnidadeGerencial").equals("")) {
				UnidadeGerencial unidadeGerencial = new UnidadeGerencial(Integer.parseInt(request.getParameter("idUnidadeGerencial")));
				unidadeGerencial = unidadeGerencialService.loadWithPlanoGestao(unidadeGerencial);
				indicador.setUnidadeGerencial(unidadeGerencial);
			}			
		}

		planoGestao = indicador.getUnidadeGerencial().getPlanoGestao();
		if (planoGestao == null || planoGestao.getAnoExercicio() == null) {
			planoGestao = planoGestaoService.obtemPlanoGestaoAtual();
			request.addMessage("Não foi possível encontrar o " + Nomes.plano_de_gestao + " do indicador, então será utilizado o " + Nomes.plano_de_gestao + " atual.", MessageType.WARN);
		}
		
		if (indicador.getFrequencia() != null) {
			resolveAcompanhamentos(planoGestao, indicador);
			CalculosAuxiliares.nomeiaEpocas(indicador.getAcompanhamentosIndicador());
		}
		else {
			indicador.setAcompanhamentosIndicador(new HashSet<AcompanhamentoIndicador>());
		}
		
		this.configuraParametrosTela(request, indicador);
		
		request.setAttribute("indicador", indicador);
		
		// INÍCIO - INFORMAÇÕES NECESSÁRIAS PARA A TAG <t:entrada>		
		request.setAttribute("TEMPLATE_beanNameUncaptalized", Util.strings.uncaptalize(Indicador.class.getSimpleName()));
		request.setAttribute("TEMPLATE_beanName", Util.strings.uncaptalize(Indicador.class.getSimpleName()));
		request.setAttribute("TEMPLATE_enviar", "salvar");
		request.setAttribute("TEMPLATE_voltar", "listagem");
		request.setAttribute("TEMPLATE_beanDisplayName", Neo.getApplicationContext().getBeanDescriptor(null, Indicador.class).getDisplayName());
		request.setAttribute("TEMPLATE_beanClass", Indicador.class);
		// TÉRMINO - INFORMAÇÕES NECESSÁRIAS PARA A TAG <t:entrada>
		
		return new ModelAndView("forward:/popup/popUpDetalhamentoIndicador.jsp");
	}
	
	private void configuraParametrosTela(WebRequestContext request, Indicador indicador) {
		Boolean isUsuarioAdmin         = usuarioService.isUsuarioLogadoAdmin();
		Boolean isUsuarioResponsavelUG = usuarioService.isUsuarioLogadoResponsavelUG(indicador.getUnidadeGerencial());
		
		request.setAttribute("usuarioAdministrador", isUsuarioAdmin);		
		request.setAttribute("usuarioResponsavel", isUsuarioResponsavelUG);
		
		if (isUsuarioAdmin || isUsuarioResponsavelUG) {
			request.setAttribute("podeRegistrarMeta", true);
		}
		else {
			request.setAttribute("podeRegistrarMeta", false);
		}
	}	
	
	private void resolveAcompanhamentos(PlanoGestao planoGestao, Indicador indicador) {
		List<AcompanhamentoIndicador> existentes = acompanhamentoIndicadorService.obtemAcompanhamentos(indicador);
		LinkedHashSet<AcompanhamentoIndicador> hashSetAcomanhamentos = new LinkedHashSet<AcompanhamentoIndicador>(existentes);
		indicador.setAcompanhamentosIndicador(hashSetAcomanhamentos);

		if (indicador.getAcompanhamentosIndicador().size() == 0) {
			Set<AcompanhamentoIndicador> acompanhamentos = CalculosAuxiliares.gerarAcompanhamento(indicador.getFrequencia(),planoGestao.getAnoExercicio());
			for (AcompanhamentoIndicador acompanhamento : acompanhamentos) {
				acompanhamento.setValorBaseOK(false);
				acompanhamento.setIndicador(indicador);
			}
			indicador.setAcompanhamentosIndicador( acompanhamentos);			
		}
		else if (indicador.getFrequencia().getFatorDivisao() != indicador.getAcompanhamentosIndicador().size()) {
			//remove antigos
			if (indicador.getFrequencia().getFatorDivisao() != indicador.getAcompanhamentosIndicador().size()) {
				for (AcompanhamentoIndicador acompanhamento : indicador.getAcompanhamentosIndicador()) {
					acompanhamentoIndicadorService.delete(acompanhamento);
				}
			}
			Set<AcompanhamentoIndicador> acompanhamentos = CalculosAuxiliares.gerarAcompanhamento(indicador.getFrequencia(),planoGestao.getAnoExercicio());
			for (AcompanhamentoIndicador acompanhamento : acompanhamentos) {
				acompanhamento.setValorBaseOK(false);
				acompanhamento.setIndicador(indicador);
			}
			indicador.setAcompanhamentosIndicador( acompanhamentos);		
		}		
	}
	
	public ModelAndView salvarPopUpDetalhamentoIndicador(WebRequestContext request, Indicador indicador){
		
		if(!this.validaForm(indicador, request)){
			request.setAttribute("erroMsg", true);
			
			request.setAttribute("indicador", indicador);
			
			// INÍCIO - INFORMAÇÕES NECESSÁRIAS PARA A TAG <t:entrada>		
			request.setAttribute("TEMPLATE_beanNameUncaptalized", Util.strings.uncaptalize(Indicador.class.getSimpleName()));
			request.setAttribute("TEMPLATE_beanName", Util.strings.uncaptalize(Indicador.class.getSimpleName()));
			request.setAttribute("TEMPLATE_enviar", "salvar");
			request.setAttribute("TEMPLATE_voltar", "listagem");
			request.setAttribute("TEMPLATE_beanDisplayName", Neo.getApplicationContext().getBeanDescriptor(null, Indicador.class).getDisplayName());
			request.setAttribute("TEMPLATE_beanClass", Indicador.class);
			// TÉRMINO - INFORMAÇÕES NECESSÁRIAS PARA A TAG <t:entrada>
			
			return new ModelAndView("forward:/popup/popUpDetalhamentoIndicador.jsp");
		}
		
		// Grava as informações dos indicadores
		indicadorService.saveOrUpdate(indicador);
		
		// Grava as informações dos acompanhamentos dos indicadores
		acompanhamentoIndicadorService.updateValorBase(indicador);
		
		request.getServletResponse().setContentType("text/html");
		String string = "<script language=\"Javascript\">" +
						"window.opener.setarIndicador(" +
						"'" + indicador.getIdxPerspectivaMapaEstrategico() + "', " +
						"'" + indicador.getIdxObjetivoMapaEstrategico() + "', " +
						"'" + indicador.getIdxIndicador() + "', " +
						"'" + (indicador.getPeso() != null ? indicador.getPeso().intValue() : "0") + "', " +
						"'" + indicador.getNome() + "', " +
						"'" + indicador.getMelhor().getName().toUpperCase() + "', " +
						"'" + indicador.getId() + "');" +
						"window.close();</script>";
		
		try {
			request.getServletResponse().getWriter().println(string);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ModelAndView salvarPopUpPlanoAcao(WebRequestContext request, Iniciativa iniciativa) {
		
		String idStringUnidadeGerencial = request.getParameter("unidadeGerencialID");
		UnidadeGerencial unidadeGerencial = null;
		
		if (idStringUnidadeGerencial != null && !idStringUnidadeGerencial.equals("")) {
			unidadeGerencial = new UnidadeGerencial(Integer.valueOf(idStringUnidadeGerencial));
		}
		
		String idStringObjetivoMapaEstrategico = request.getParameter("objetivoMapaEstrategicoID");
		ObjetivoMapaEstrategico objetivoMapaEstrategico = null;
		
		if (idStringObjetivoMapaEstrategico != null && !idStringObjetivoMapaEstrategico.equals("")) {
			objetivoMapaEstrategico = new ObjetivoMapaEstrategico(Integer.valueOf(idStringObjetivoMapaEstrategico));
		}
		
		// Salva a iniciativa com os planos de ação associados.
		iniciativaService.atualizaIniciativaListaPlanoAcaoDaIniciativa(unidadeGerencial, objetivoMapaEstrategico, iniciativa);
		
		request.getServletResponse().setContentType("text/html");
		String string = 
			"<script language=\"Javascript\">" +
				"window.opener.setarIniciativa(" +
				"'" + iniciativa.getIdxPerspectivaMapaEstrategico() + "', " +
				"'" + iniciativa.getIdxObjetivoMapaEstrategico() + "', " +
				"'" + iniciativa.getIdxIniciativa() + "', " +
				"'" + iniciativa.getId() + "');" +
				"window.close();</script>";			
		try {
			request.getServletResponse().getWriter().println(string);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	private boolean validaForm(Indicador indicador, WebRequestContext request) {
		if (!indicadorService.usuarioPodeAlterar(indicador)){
			request.addError("O usuário logado no sistema não é administrador do sistema nem é responsável pela unidade gerencial deste indicador.");
			return false;
		}
		
		if (!isAdministradoOuDataLimiteNaoExpirada(indicador)){
			request.addError("Data limite para criação de indicadores está ultrapassada.");
			return false;
		}
		
		for (AnexoIndicador anexoIndicador : indicador.getAnexosIndicador()) {
			if (anexoIndicador.getId() == null && anexoIndicador.getArquivo().getSize().longValue() == 0) {
				request.addError("Escolha o arquivo novamente.");
				return false;
			}
			if (anexoIndicadorService.tamanhoLimiteExcedido(anexoIndicador)){
				request.addError("Tamanho Limite (5000KB) excedido.");
				return false;
			}
			if ( Util.strings.isEmpty( anexoIndicador.getNome() ) ){
				request.addError("Escolha um nome para o anexo do indicador.");
				return false;
			}
		}
		
		if (indicador.getTolerancia() < 0 || indicador.getTolerancia() > 100) {
			request.addError("O valor da tolerância deve estar compreendido entre 0 e 100");
			return false;
		}
		
		return true;
	}
	
	private boolean isAdministradoOuDataLimiteNaoExpirada(Indicador indicador) {
		return (usuarioService.isUsuarioLogadoAdmin() || indicadorService.dataLimiteCriacaoNaoExpirada(indicador));
	}
}
