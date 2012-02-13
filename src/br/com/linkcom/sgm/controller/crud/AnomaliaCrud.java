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


import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Action;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.CausaEfeito;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.Ocorrencia;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusPlanoAcaoEnum;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AnomaliaFiltro;
import br.com.linkcom.sgm.service.AcompanhamentoIndicadorService;
import br.com.linkcom.sgm.service.AnomaliaService;
import br.com.linkcom.sgm.service.CausaEfeitoService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.OcorrenciaService;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.service.PlanoAcaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;
import br.com.linkcom.sgm.util.GeplanesUtils;


@Controller(path="/sgm/crud/Anomalia", authorizationModule=CrudAuthorizationModule.class)
public class AnomaliaCrud extends CrudController<AnomaliaFiltro, Anomalia, Anomalia> {
	
	private UsuarioService usuarioService;
	private AnomaliaService anomaliaService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private IndicadorService indicadorService;
	private UnidadeGerencialService unidadeGerencialService;
	private PlanoAcaoService planoAcaoService;
	private CausaEfeitoService  causaEfeitoService;
	private OcorrenciaService ocorrenciaService;
	
	public void setOcorrenciaService(OcorrenciaService ocorrenciaService) {this.ocorrenciaService = ocorrenciaService;}
	public void setCausaEfeitoService(CausaEfeitoService causaEfeitoService) {this.causaEfeitoService = causaEfeitoService;}
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService) {this.planoAcaoService = planoAcaoService;}
	public void setAnomaliaService(AnomaliaService anomaliaService) {this.anomaliaService = anomaliaService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) { this.unidadeGerencialService = unidadeGerencialService;}
		
	@Override
	public ModelAndView doListagem(WebRequestContext request,AnomaliaFiltro filtro) throws CrudException {
		
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		
		return super.doListagem(request, filtro);
	}
	
	@Override
	protected ListagemResult<Anomalia> getLista(WebRequestContext request, AnomaliaFiltro filtro) {
		ListagemResult<Anomalia> result = super.getLista(request, filtro);
		List<Anomalia> listaAnomalia = result.list();
		
		// Permissões de Tela
		Boolean temPermissaoEdicao    = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/crud/Anomalia", CrudController.EDITAR, request.getServletRequest());
		Boolean temPermissaoExclusao  = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/crud/Anomalia", CrudController.EXCLUIR, request.getServletRequest());
		Boolean temPermissaoRelatorio = GeplanesUtils.hasAuthorization(request.getServletRequest().getContextPath()+"/sgm/report/Anomalia", "generate", request.getServletRequest());		
		
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsRepQualidade = usuarioService.isUsuarioLogadoVinculadoAreaQualidade(filtro.getPlanoGestao());
		Boolean usuarioLogadoIsParticipanteUgRegistro;
		Boolean usuarioLogadoIsParticipanteUgResponsavel;
		Boolean usuarioPodeEncerrarAnomalia;
		Boolean usuarioPodeDestravarAnomalia;
		Boolean dtTravamentoExpirada;
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		for (Anomalia anomalia : listaAnomalia) {
			usuarioLogadoIsParticipanteUgRegistro = usuarioService.isUsuarioLogadoParticipanteUG(anomalia.getUgRegistro());
			usuarioLogadoIsParticipanteUgResponsavel = usuarioService.isUsuarioLogadoParticipanteUG(anomalia.getUgResponsavel());
			
			if (anomalia.getDataEncerramento()!=null) {
				anomalia.setEncerrado(true); 
			}
			else {
				anomalia.setEncerrado(false); 
			}
			if (usuarioLogadoIsAdmin || usuarioLogadoIsParticipanteUgRegistro || usuarioLogadoIsParticipanteUgResponsavel) {
				anomalia.setPodeEditar(temPermissaoEdicao);
				
				if (usuarioLogadoIsAdmin || (usuarioLogadoIsParticipanteUgRegistro && anomalia.getStatus().equals(StatusAnomaliaEnum.ABERTA))) {
					anomalia.setPodeExcluir(temPermissaoExclusao);
				}
				else {
					anomalia.setPodeExcluir(false);
				}
				
				anomalia.setPodeImprimir(temPermissaoRelatorio);
			}
			else {
				dtTravamentoExpirada = anomaliaService.isDataTravamentoAnomaliaExpirada(anomalia, parametrosSistema.getDiasTravTratAnomalia());
				usuarioPodeEncerrarAnomalia = usuarioPodeEncerrarAnomalia(anomalia, usuarioLogadoIsAdmin, usuarioLogadoIsRepQualidade);
				usuarioPodeDestravarAnomalia = usuarioPodeDestravarAnomalia(anomalia, dtTravamentoExpirada, usuarioLogadoIsAdmin, usuarioLogadoIsRepQualidade);				
				
				if (usuarioPodeEncerrarAnomalia || usuarioPodeDestravarAnomalia) {
					anomalia.setPodeEditar(temPermissaoEdicao);
					anomalia.setPodeExcluir(false);
					anomalia.setPodeImprimir(temPermissaoRelatorio);
				}
				else {
					anomalia.setPodeEditar(false);
					anomalia.setPodeExcluir(false);
					anomalia.setPodeImprimir(true);
				}
			}
		}
		
		return result;
	}
	
	@Override
	protected void entrada(WebRequestContext request, Anomalia form) throws Exception {
		
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(form, "ugRegistro");		
		
		//Objetos
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsParticipanteUgRegistro = usuarioService.isUsuarioLogadoParticipanteUG(form.getUgRegistro());
		Boolean usuarioLogadoIsParticipanteUgResponsavel = usuarioService.isUsuarioLogadoParticipanteUG(form.getUgResponsavel());
		Boolean usuarioLogadoIsRepQualidade = usuarioService.isUsuarioLogadoVinculadoAreaQualidade(form.getPlanoGestao());
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		Boolean dtTravamentoExpirada = anomaliaService.isDataTravamentoAnomaliaExpirada(form, parametrosSistema.getDiasTravTratAnomalia());
		Boolean usuarioPodeEncerrarAnomalia = usuarioPodeEncerrarAnomalia(form, usuarioLogadoIsAdmin, usuarioLogadoIsRepQualidade);
		Boolean usuarioPodeDestravarAnomalia = usuarioPodeDestravarAnomalia(form, dtTravamentoExpirada, usuarioLogadoIsAdmin, usuarioLogadoIsRepQualidade);
		Boolean usuarioPodeConsultarAnomalia = usuarioPodeConsultarAnomalia(form, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgRegistro, usuarioLogadoIsParticipanteUgResponsavel, (usuarioPodeEncerrarAnomalia || usuarioPodeDestravarAnomalia));
		Boolean usuarioPodeTratarAnomalia = usuarioPodeTratarAnomalia(form, dtTravamentoExpirada, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgResponsavel);
		Boolean usuarioPodeCriarPlanoAcao = usuarioPodeCriarPlanoAcao(form, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgResponsavel, dtTravamentoExpirada);
		Boolean usuarioPodeEditarAnomalia;
		Boolean podeEditarPlanoAcao = false;
		
		// Criação de Anomalia
		if (form.getId() == null) {
			usuarioPodeEditarAnomalia = true;
			form.setStatus(StatusAnomaliaEnum.ABERTA);
			form.setResponsavel(Neo.getUser().toString());
			form.setDataAbertura(anomaliaService.geraDataAtual());
			//Verifica se a anomalia foi requisitada de algum lugar
			String idStringUgOrigem = request.getParameter("idUgOrigem");
			if(idStringUgOrigem != null && !idStringUgOrigem.equals("")){
				UnidadeGerencial ugRegistro = anomaliaService.verificaUgOrigem(idStringUgOrigem,form);
				form.setUgRegistro(ugRegistro);
				form.setPlanoGestao(ugRegistro.getPlanoGestao());
			}
		}
		// Edição de Anomalia
		else {
			usuarioPodeEditarAnomalia = usuarioPodeEditarAnomalia(form, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgRegistro);
			form.setEfeito(causaEfeitoService.findByAnomalia(form));
			form.setOcorrencia(anomaliaService.obtemOcorrencia(form).getOcorrencia());
			
			form.setPlanosAcao(new ListSet<PlanoAcao>(PlanoAcao.class,planoAcaoService.findByAnomalia(form)));
			
			if (form.getPlanosAcao() != null) {
				for (PlanoAcao planoAcao : form.getPlanosAcao()) {
					if (StatusAnomaliaEnum.REANALISE.equals(form.getStatus())) {
						planoAcao.setPodeEditarTexto(false);
						planoAcao.setPodeEditarStatus(false);						
					}
					else {					
						if (usuarioPodeTratarAnomalia) {
							planoAcao.setPodeEditarTexto(true);
							planoAcao.setPodeEditarStatus(true);
							podeEditarPlanoAcao = true;
						}
						else {
							if (!usuarioLogadoIsParticipanteUgResponsavel || StatusPlanoAcaoEnum.CONCLUIDO.equals(planoAcao.getStatus())) {
								planoAcao.setPodeEditarTexto(false);
								planoAcao.setPodeEditarStatus(false);
							}
							else {
								planoAcao.setPodeEditarTexto(false);
								planoAcao.setPodeEditarStatus(true);
								podeEditarPlanoAcao = true;
							}
						}
					}
				}
			}			
		}
		
		// Monta o Diagrama de Causa-Efeito
		montaDiagramaCausaEfeito(form);		
		
		if (usuarioPodeConsultarAnomalia) {
			/*** Coloca na tela o acompanhamento indicador ***/	
			AcompanhamentoIndicador acompanhamentoIndicador = this.getAcompanhamentoIndicador(request);
					
			if( acompanhamentoIndicador!=null ){
				form.setAcompanhamentoIndicador(acompanhamentoIndicador);
				Indicador indicador = acompanhamentoIndicador.getIndicador();
				indicador= indicadorService.load(indicador);
				request.addMessage("Tratamento de anomalia para acompanhamento do indicador "+
						indicador.getNome()+" no período de "+ 
						this.getStringDate( acompanhamentoIndicador.getDataInicial())+" até "+ 
						this.getStringDate(acompanhamentoIndicador.getDataFinal())+"." );
			}

			// Setando a ocorrência caso a entrada venha da tela de Diário de Bordo.
			if (request.getParameter("ocorrencia.id") != null) {
				Ocorrencia ocorrencia = new Ocorrencia();
				ocorrencia.setId(Integer.parseInt(request.getParameter("ocorrencia.id")));
				ocorrencia = ocorrenciaService.load(ocorrencia);
				ocorrencia.setUnidadeGerencial(unidadeGerencialService.load(ocorrencia.getUnidadeGerencial()));
				form.setOcorrencia(ocorrencia);
				form.setResponsavel(usuarioService.load(ocorrencia.getRelator()).getNome());
				form.setDescricao(ocorrencia.getDescricao());
				form.setUgRegistro(ocorrencia.getUnidadeGerencial());
				form.setContraMedidasImediatas(ocorrencia.getContraMedidasImediatas());
				form.setPlanoGestao(unidadeGerencialService.obtemPlanoGestao(ocorrencia.getUnidadeGerencial()).getPlanoGestao());
			}
		}
		else {
			request.addMessage("Você não tem permissão para acessar os dados dessa Anomalia.", MessageType.WARN);
		}
		
		// Se a anomalia estiver com o status REANÁLISE, bloquear a edição.
		if (StatusAnomaliaEnum.REANALISE.equals(form.getStatus())) {
			usuarioPodeEditarAnomalia    = false;
			usuarioPodeDestravarAnomalia = false;
			usuarioPodeCriarPlanoAcao   = false;
			usuarioPodeEncerrarAnomalia  = false;
			usuarioPodeTratarAnomalia    = false;
		}
		
		request.setAttribute("podeConsultarAnomalia", usuarioPodeConsultarAnomalia);
		request.setAttribute("podeEditarAnomalia", usuarioPodeEditarAnomalia);
		request.setAttribute("podeTratarAnomalia", usuarioPodeTratarAnomalia);
		request.setAttribute("podeEncerrarAnomalia", usuarioPodeEncerrarAnomalia);
		request.setAttribute("podeDestravarAnomalia", usuarioPodeDestravarAnomalia);
		request.setAttribute("podeCriarPlanoAcao", usuarioPodeCriarPlanoAcao);
		request.setAttribute("podeEditarPlanoAcao", podeEditarPlanoAcao);
		request.setAttribute("exibirBotaoSolicitarEncerramento", exibirBotaoSolicitarEncerramento(form, usuarioLogadoIsAdmin, usuarioLogadoIsParticipanteUgResponsavel));
		request.setAttribute("exibirBotaoNotificarCumprimentoPendente", exibirBotaoNotificarCumprimentoPendente(form, usuarioPodeEncerrarAnomalia));
		request.setAttribute("exibirBotaoSolicitarReanalise", exibirBotaoSolicitarReanalise(form, usuarioLogadoIsAdmin, usuarioPodeEncerrarAnomalia));
		super.entrada(request, form);		
	}
	
	private void montaDiagramaCausaEfeito(Anomalia form) {
		
		//Objetos
		CausaEfeito causaEfeito1;
		CausaEfeito causaEfeito2;
		CausaEfeito causaEfeito3;
		
		// Listas
		Set<CausaEfeito> listaCausaEfeito1 = new ListSet<CausaEfeito>(CausaEfeito.class);
		Set<CausaEfeito> listaCausaEfeito2 = new ListSet<CausaEfeito>(CausaEfeito.class);
		Set<CausaEfeito> listaCausaEfeito3 = new ListSet<CausaEfeito>(CausaEfeito.class);		
		
		//Se for uma nova será setado uma estrutura em branco.
		if(form.getEfeito() == null){
			for(int nivelHum = 0; nivelHum < 6; nivelHum++){
				causaEfeito1 = new CausaEfeito();
				listaCausaEfeito2 = new ListSet<CausaEfeito>(CausaEfeito.class);
				for (int nivelDois = 0; nivelDois < 4; nivelDois++) {
					causaEfeito2 = new CausaEfeito();
					listaCausaEfeito3 = new ListSet<CausaEfeito>(CausaEfeito.class);
					for (int nivelTres = 0; nivelTres < 1; nivelTres++) {
						causaEfeito3 = new CausaEfeito();
						listaCausaEfeito3.add(causaEfeito3);
					}
					causaEfeito2.setListaCausaEfeito(listaCausaEfeito3);
					listaCausaEfeito2.add(causaEfeito2);
					causaEfeito1.setListaCausaEfeito(listaCausaEfeito2);
				}
				listaCausaEfeito1.add(causaEfeito1);
				form.setCausasEfeito(listaCausaEfeito1);
			}
		}
		
		// Senão será setado os valores cadastrados no banco de dados.
		else {
			listaCausaEfeito1 = new ListSet<CausaEfeito>(CausaEfeito.class,causaEfeitoService.findByCausaFilha(form.getEfeito()));
			for (CausaEfeito causaEfeito : listaCausaEfeito1) {
				causaEfeito.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,causaEfeitoService.findByCausaFilha(causaEfeito)));
				for (CausaEfeito nivel2 : causaEfeito.getListaCausaEfeito()) {
					nivel2.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,causaEfeitoService.findByCausaFilha(nivel2)));
					for (CausaEfeito nivel3 : nivel2.getListaCausaEfeito()) {
						nivel3.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,causaEfeitoService.findByCausaFilha(nivel3)));
					}
				}
			}
			//Setando os dados que estavam no banco de dados.
			form.setCausasEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,listaCausaEfeito1));
		}		
	}
		
	private String getStringDate(Calendar date) {
		
		String dataString="";
		
		if( date.get(Calendar.DAY_OF_MONTH)<0)
			dataString+="0";
		
		dataString+=date.get(Calendar.DAY_OF_MONTH)+"/";
		dataString+=(date.get(Calendar.MONTH)+1)+"/";
		dataString+=date.get(Calendar.YEAR);
		
		
		return dataString;
		
	}
	
	private AcompanhamentoIndicador getAcompanhamentoIndicador(WebRequestContext request) {
		
		String acompanhamentoIndicadorString = request.getParameter("acompanhamentoIndicador.id");	
		
		if(acompanhamentoIndicadorString!=null){			
			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			acompanhamentoIndicador.setId(Integer.parseInt(acompanhamentoIndicadorString));
			return acompanhamentoIndicadorService.load(acompanhamentoIndicador);			
		}
		
		return null;
	}
	
	@Input(ENTRADA)
	@Override
	protected void salvar(WebRequestContext request, Anomalia anomalia) throws Exception {
		
		//Busca o próximo sequencial
		if (anomalia.getSequencial() == null) {
			anomalia.setSequencial(anomaliaService.getProximoSequencial(anomalia.getUgRegistro()));
		}
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		anomalia.setStatus(anomaliaService.getStatusAnomalia(anomalia, parametrosSistema.getDiasTravTratAnomalia(), parametrosSistema.getDiasEncerramentoAnomalia()));
		anomalia.setStatusTratamento(anomaliaService.getStatusTratamentoAnomalia(anomalia));
		
		try {
			String errorMessage = anomaliaService.salvaAnomalia(anomalia, true);
		
			AcompanhamentoIndicador acompanhamentoIndicador = anomalia.getAcompanhamentoIndicador();
		
			if (acompanhamentoIndicador!=null) {	
				acompanhamentoIndicador.setAnomalia(anomalia);
				acompanhamentoIndicadorService.updateAnomalia(acompanhamentoIndicador, anomalia);			
			}
			
			if (errorMessage == null || errorMessage.equals("")) {
				request.addMessage("Tratamento da anomalia salvo com sucesso", MessageType.INFO);
			}
			else {
				request.addMessage("Tratamento da anomalia salvo com pendência(s): " + errorMessage, MessageType.WARN);
			}
		}
		catch (Exception e) {
			throw new GeplanesException(e.getMessage());
		}		
	}
	
	@Input(ENTRADA)
	@Action("destravar")
	public ModelAndView doDestravar(WebRequestContext request, Anomalia anomalia) throws Exception {
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		anomalia.setDataDestravamento(new Date(System.currentTimeMillis()));
		anomalia.setLembreteEnviado(false);
		anomalia.setStatus(anomaliaService.getStatusAnomalia(anomalia, parametrosSistema.getDiasTravTratAnomalia(), parametrosSistema.getDiasEncerramentoAnomalia()));
		anomalia.setStatusTratamento(anomaliaService.getStatusTratamentoAnomalia(anomalia));
		super.salvar(request, anomalia);
		request.addMessage("Destravamento realizado com sucesso", MessageType.INFO);
		
		return getSalvarModelAndView(request, anomalia);
	}
	
	@Input(ENTRADA)
	@Action("solicitarEncerramento")
	public ModelAndView doSolicitarEncerramento(WebRequestContext request, Anomalia anomalia) throws Exception {
		anomalia.setDataSolicitacaoEncerramento(new Date(System.currentTimeMillis()));
		anomalia.setStatus(StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO);
		super.salvar(request, anomalia);
		
		try {
			// Envia um email aos responsáveis pelo encerramento da anomalia
			anomaliaService.enviaEmailSolicitacaoEncerramento(anomalia);
			request.addMessage("Solicitação de encerramento realizada com sucesso", MessageType.INFO);
		}
		catch (Exception e) {
			request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
		}
		
		
		return getSalvarModelAndView(request, anomalia);
	}
	
	@Input(ENTRADA)
	@Action("notificarCumprimentoPendente")
	public ModelAndView doNotificarCumprimentoPendente(WebRequestContext request, Anomalia anomalia) throws Exception {
		anomalia.setDataSolicitacaoEncerramento(null);
		anomalia.setStatus(StatusAnomaliaEnum.CUMPRIMENTO_PENDENTE);
		
		if (anomalia.getPlanosAcao() != null) {
			for (PlanoAcao planoAcao : anomalia.getPlanosAcao()) {
				if (planoAcao.getStatus().equals(StatusPlanoAcaoEnum.CONCLUIDO)) {
					planoAcao.setDtAtualizacaoStatus(new Date(System.currentTimeMillis()));
					planoAcao.setStatus(StatusPlanoAcaoEnum.EM_ANDAMENTO);
				}
			}
		}
		super.salvar(request, anomalia);
		
		try {
			// Envia um email aos responsáveis pelo cumprimento pendente da anomalia
			anomaliaService.enviaEmailNotificacaoCumprimentoPendente(anomalia);
			request.addMessage("Notificação de cumprimento pendente realizada com sucesso", MessageType.INFO);
		}
		catch (Exception e) {
			request.addError("Erro ao tentar enviar e-mail: " + e.getMessage());
		}			
		
		
		return getSalvarModelAndView(request, anomalia);
	}
	
	@Input(ENTRADA)
	@Action("solicitarReanalise")
	public ModelAndView doSolicitarReanalise(WebRequestContext request, Anomalia anomalia) throws Exception {
		anomalia.setStatus(StatusAnomaliaEnum.REANALISE);
		
		super.salvar(request, anomalia);
		
		// Cria uma nova anomalia com as mesmas informações da anomalia salva, porém com os campos de tratamento em branco.
		Integer cdAnomaliaReanalise = anomalia.getId();
		
		Anomalia novaAnomalia = anomalia;
		novaAnomalia.setId(null);
		novaAnomalia.setStatus(null);
		novaAnomalia.setStatusTratamento(null);
		novaAnomalia.setSequencial(null);
		novaAnomalia.setSubordinacao(new Anomalia(cdAnomaliaReanalise));
		novaAnomalia.setAnaliseCausas(null);
		novaAnomalia.setPlanosAcao(null);
		novaAnomalia.setVerificacao(null);
		novaAnomalia.setPadronizacao(null);
		novaAnomalia.setDataAbertura(new Date(System.currentTimeMillis()));
		novaAnomalia.setDataDestravamento(null);
		novaAnomalia.setDataSolicitacaoEncerramento(null);
		novaAnomalia.setDataEncerramento(null);
		novaAnomalia.setConclusao(null);
		novaAnomalia.setCausasEfeito(null);
		novaAnomalia.setEfeito(null);
		novaAnomalia.setLembreteEnviado(false);
		
		request.addMessage("Solicitação de reanálise de anomalia realizada com sucesso", MessageType.INFO);
		
		return continueOnAction("salvar", novaAnomalia);
	}
	
	@Input(ENTRADA)
	@Override
	protected void validateBean(Anomalia bean, BindException errors) {
		if (bean.getDataAbertura() != null && bean.getDataEncerramento() != null) {
			if(bean.getDataEncerramento().before(bean.getDataAbertura())){
				errors.reject("A data de encerramento deve ser maior que a data de abertura.","A data de encerramento deve ser maior que a data de abertura.");
			}
		}
		super.validateBean(bean, errors);
	}
	
	private Boolean usuarioPodeConsultarAnomalia(Anomalia anomalia, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgRegistro, Boolean usuarioLogadoIsParticipanteUgResponsavel, Boolean usuarioPodeEncerrarOuDestravarAnomalia) {
		Boolean podeConsultarAnomalia = false;
		
		if (anomalia.getId() == null) {
			podeConsultarAnomalia = true;
		}
		else {
			if (usuarioLogadoIsAdmin || usuarioLogadoIsParticipanteUgRegistro || usuarioLogadoIsParticipanteUgResponsavel || usuarioPodeEncerrarOuDestravarAnomalia) {
				podeConsultarAnomalia = true;
			}
		}
		
		return podeConsultarAnomalia;
	}
	
	private Boolean usuarioPodeEditarAnomalia(Anomalia anomalia, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgRegistro) {
		Boolean podeEditarAnomalia = false;
		
		// Usuário admin
		if (usuarioLogadoIsAdmin) {
			podeEditarAnomalia = true;
		}
		else {
			// Usuário participante da UG de Registro, desde que a anomalia esteja com status ABERTA
			if (anomalia.getStatus().equals(StatusAnomaliaEnum.ABERTA)) {
				if (usuarioLogadoIsParticipanteUgRegistro) {
					podeEditarAnomalia = true;
				}
			}
		}
		
		return podeEditarAnomalia;
	}
	
	private Boolean usuarioPodeTratarAnomalia(Anomalia anomalia, Boolean dtTravamentoExpirada, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgResponsavel) {
		if (anomalia.getId() != null) {
			// Independente do status da anomalia, caso o usuário logado seja administrador,
			// poderá tratá-la
			if (usuarioLogadoIsAdmin) {
				return true;
			}
		
			if ((anomalia.getStatus().equals(StatusAnomaliaEnum.ABERTA)  || 
				anomalia.getStatus().equals(StatusAnomaliaEnum.TRATADA) ||
				anomalia.getStatus().equals(StatusAnomaliaEnum.TRATAMENTO_PENDENTE)) && !dtTravamentoExpirada) {
	
				if (usuarioLogadoIsParticipanteUgResponsavel) {
					return true;
				}
			}
		}
		return false;		
	}
	
	private Boolean usuarioPodeEncerrarAnomalia(Anomalia anomalia, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsRepQualidade) {
		Boolean usuarioPodeEncerrarAnomalia = false;
		
		if (anomalia.getId() == null) {
			usuarioPodeEncerrarAnomalia = false;
		}
		else {
			if (usuarioLogadoIsAdmin || (usuarioLogadoIsRepQualidade && (anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO) || anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_PENDENTE)))) {
				usuarioPodeEncerrarAnomalia = true;
			}
		}
		
		return usuarioPodeEncerrarAnomalia;
	}	
	
	private Boolean usuarioPodeCriarPlanoAcao(Anomalia anomalia, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgResponsavel, Boolean dtTravamentoExpirada) {
		Boolean podeEditarPlanoAcao = false;

		// Usuário admin
		if (usuarioLogadoIsAdmin) {
			podeEditarPlanoAcao = true;
		}
		// Usuário participante de UG responsável, desde que a anomalia esteja ABERTA, TRATADA ou PENDENTE DE TRATAMENTO 
		// e o prazo para tratamento não esteja expirado.
		else {
			if (usuarioLogadoIsParticipanteUgResponsavel) {
				if ((anomalia.getStatus().equals(StatusAnomaliaEnum.ABERTA) || 
					anomalia.getStatus().equals(StatusAnomaliaEnum.TRATADA) || 
					anomalia.getStatus().equals(StatusAnomaliaEnum.TRATAMENTO_PENDENTE)) && !dtTravamentoExpirada) {
					
					podeEditarPlanoAcao = true;
				}
			}
		}
		return podeEditarPlanoAcao;		
	}
	
	private Boolean usuarioPodeDestravarAnomalia (Anomalia anomalia, Boolean dtTravamentoExpirada, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsRepQualidade) {
		if (anomalia.getDataEncerramento() != null || !dtTravamentoExpirada || !anomalia.getStatus().equals(StatusAnomaliaEnum.BLOQUEADA)) {
			return false;
		}
		if (usuarioLogadoIsAdmin || usuarioLogadoIsRepQualidade) {
			return true;
		}
		return false;
	}
	
	private Boolean exibirBotaoSolicitarEncerramento(Anomalia anomalia, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsParticipanteUgResponsavel) {
		
		if (usuarioLogadoIsAdmin || usuarioLogadoIsParticipanteUgResponsavel) {
			// O botão de solicitar encerramento só é exibido quando a anomalia está tratada e todos os planos de ação estão com o status CONCLUÍDO
			if (anomalia.getStatus().equals(StatusAnomaliaEnum.TRATADA)) {
				if (anomalia.getPlanosAcao() != null && !anomalia.getPlanosAcao().isEmpty()) {
					for (PlanoAcao planoAcao : anomalia.getPlanosAcao()) {
						if (!planoAcao.getStatus().equals(StatusPlanoAcaoEnum.CONCLUIDO)) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;		
	}
	
	private Boolean exibirBotaoNotificarCumprimentoPendente(Anomalia anomalia, Boolean usuarioPodeEncerrarAnomalia) {
		
		if (usuarioPodeEncerrarAnomalia) {
			// O botão de notificar cumprimento pendente só é exibido para os usuários que podem 
			// encerrar uma anomalia e quando ela está com status ENCERRAMENTO SOLICITADO ou ENCERRAMENTO PENDENTE
			if (anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO) || anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_PENDENTE)) {
				return true;
			}
		}
		return false;		
	}
	
	private Boolean exibirBotaoSolicitarReanalise(Anomalia anomalia, Boolean usuarioLogadoIsAdmin, Boolean usuarioLogadoIsRepQualidade) {
		
		if (usuarioLogadoIsAdmin || usuarioLogadoIsRepQualidade) {
			// O botão de solicitar reanálise só é exibido para os usuários que podem 
			// encerrar uma anomalia e quando ela está com status ENCERRAMENTO SOLICITADO ou ENCERRAMENTO PENDENTE
			if (anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO) || anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_PENDENTE)) {
				return true;
			}
			// Caso a anomalia esteja em REANÁLISE, mas não foi criada uma nova anomalia vinculada à atual, o botão deverá ser exibido.
			if (anomalia.getStatus().equals(StatusAnomaliaEnum.REANALISE)) {
				 List<Anomalia> listaSubordinadas = anomaliaService.findSubordinadas(anomalia);
				 if (listaSubordinadas == null || listaSubordinadas.isEmpty()) {
					 return true;
				 }
			}			
		}
		return false;		
	}	
}