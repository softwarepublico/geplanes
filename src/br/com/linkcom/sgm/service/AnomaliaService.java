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
package br.com.linkcom.sgm.service;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.util.NeoFormater;
import br.com.linkcom.neo.util.NeoImageResolver;
import br.com.linkcom.sgm.beans.AnexoAnomalia;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.CausaEfeito;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ItemAuditoriaInterna;
import br.com.linkcom.sgm.beans.Ocorrencia;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoAcao;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.LocalAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusTratamentoAnomaliaEnum;
import br.com.linkcom.sgm.controller.report.filtro.AnomaliaListagemReportFiltro;
import br.com.linkcom.sgm.controller.report.filtro.AnomaliaSinteticoPorStatusReportFiltro;
import br.com.linkcom.sgm.dao.AnomaliaDAO;
import br.com.linkcom.sgm.exception.EnvioEmailException;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AnomaliaFiltro;
import br.com.linkcom.sgm.report.MergeReport;
import br.com.linkcom.sgm.report.bean.AnomaliaSinteticoPorStatusReportBean;
import br.com.linkcom.sgm.util.DateUtil;
import br.com.linkcom.sgm.util.EmailUtil;
import br.com.linkcom.sgm.util.GeplanesUtils;
import br.com.linkcom.sgm.util.email.EnvioEmail;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class AnomaliaService extends GenericService<Anomalia> {
	
	private UnidadeGerencialService unidadeGerencialService;
	private AnomaliaDAO anomaliaDAO;
	private UsuarioService usuarioService;
	private AcompanhamentoIndicadorService acompanhamentoIndicadorService;
	private CausaEfeitoService causaEfeitoService;
	private NeoImageResolver neoImageResolver;
	private UsuarioUnidadeGerencialService usuarioUnidadeGerencialService;
	private PlanoAcaoService planoAcaoService;
	private AnexoAnomaliaService anexoAnomaliaService;
	
	public void setPlanoAcaoService(PlanoAcaoService planoAcaoService){this.planoAcaoService = planoAcaoService;}
	public void setUsuarioUnidadeGerencialService(UsuarioUnidadeGerencialService usuarioUnidadeGerencialService) {this.usuarioUnidadeGerencialService = usuarioUnidadeGerencialService;}
	public void setNeoImageResolver(NeoImageResolver neoImageResolver) {this.neoImageResolver = neoImageResolver;}
	public void setCausaEfeitoService(CausaEfeitoService causaEfeitoService) {this.causaEfeitoService = causaEfeitoService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setAnomaliaDAO(AnomaliaDAO anomaliaDAO) {this.anomaliaDAO = anomaliaDAO;}	
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}	
	public void setAcompanhamentoIndicadorService(AcompanhamentoIndicadorService acompanhamentoIndicadorService) {this.acompanhamentoIndicadorService = acompanhamentoIndicadorService;}
	public void setAnexoAnomaliaService(AnexoAnomaliaService anexoAnomaliaService) {this.anexoAnomaliaService = anexoAnomaliaService;}
	
	@Override
	public ListagemResult<Anomalia> findForListagem(FiltroListagem filtro) {
		AnomaliaFiltro anomaliaFiltro = (AnomaliaFiltro) filtro;
		
		List<UnidadeGerencial> listaUGRegistro = new ArrayList<UnidadeGerencial>();		
		List<UnidadeGerencial> listaUGResponsavel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(anomaliaFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsRepQualidade = usuarioService.isUsuarioLogadoVinculadoAreaQualidade(anomaliaFiltro.getPlanoGestao());
		
		// UG de Registro
		if (anomaliaFiltro.getUgRegistro() != null) {
			listaUGRegistro.add(anomaliaFiltro.getUgRegistro());
			
			if (anomaliaFiltro.isIncluirSubordinadasReg()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGRegistro = unidadeGerencialService.getListaDescendencia(anomaliaFiltro.getUgRegistro(), listaUGRegistro);
			}
		}
		anomaliaFiltro.setListaUnidadeGerencialReg(listaUGRegistro);
		
		// UG Responsável
		if (anomaliaFiltro.getUgResponsavel() != null) {
			listaUGResponsavel.add(anomaliaFiltro.getUgResponsavel());
			
			if (anomaliaFiltro.isIncluirSubordinadasResp()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGResponsavel = unidadeGerencialService.getListaDescendencia(anomaliaFiltro.getUgResponsavel(), listaUGResponsavel);
			}
		}
		anomaliaFiltro.setListaUnidadeGerencialResp(listaUGResponsavel);		
		
		// UGs disponíveis para a listagem
		if (!usuarioLogadoIsAdmin && !usuarioLogadoIsRepQualidade) {
			for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
				listaUGDisponivel.add(unidadeGerencial);
				listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
			}			
		}
		anomaliaFiltro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		
		return super.findForListagem(anomaliaFiltro);
	}
	
	public String salvaAnomalia(Anomalia bean, boolean useTransaction) {
		
		String errorMessage = null;
		
		//Só envia email se for uma nova anomalia e se o parâmetro de envio de email esteja habilitado
		Boolean enviarEmailEnvolvidoAnomalia = (bean.getId() == null && ParametrosSistemaService.getParametrosSistema().getNotificarEnvolvidosAnomalia());			
		
		// Calcula se a anomalia possui causa interna ou externa
		bean.setLocal(calculaLocalAnomalia(bean));
		
		// Caso seja um encerramento, encerra também as anomalias superiores
		if (bean.getStatus().equals(StatusAnomaliaEnum.ENCERRADA)) {
			Anomalia anomaliaPai = this.loadParent(bean);
			if (anomaliaPai != null) {
				if (anomaliaPai.getDataEncerramento() == null) {
					anomaliaPai.setDataEncerramento(new Date(System.currentTimeMillis()));
				}
				anomaliaPai.setStatus(StatusAnomaliaEnum.ENCERRADA);
				
				if (useTransaction) {
					saveOrUpdate(anomaliaPai);
				}
				else {
					saveOrUpdateWithoutTransaction(anomaliaPai);
				}				
			}
		}
		
		if (bean.getPlanosAcao() != null && !bean.getPlanosAcao().isEmpty()) {
			PlanoAcao planoAcaoDB;
			Date dataAtual = new Date(System.currentTimeMillis());
			for (PlanoAcao planoAcao : bean.getPlanosAcao()) {		
				// Verifica se houve mudança no status.
				// Caso afirmativo, grava a data atual.				
				if (planoAcao.getId() != null) {
					planoAcaoDB = planoAcaoService.load(planoAcao);
					if (!planoAcaoDB.getStatus().equals(planoAcao.getStatus())) {
						planoAcao.setDtAtualizacaoStatus(dataAtual);
					}
				}
			}
		}
		
		if (useTransaction) {
			saveOrUpdate(bean);
		}
		else {
			saveOrUpdateWithoutTransaction(bean);
		}
		
		if (enviarEmailEnvolvidoAnomalia) {
			try {
				this.enviarEmailEnvolvidosAnomalia(bean);
			}
			catch (GeplanesException e) {
				errorMessage = e.getMessage();
			}			
		}
		
		return errorMessage;
	}	
	
	public UnidadeGerencial verificaUgOrigem(String idStringUgOrigem, Anomalia anomalia){
		UnidadeGerencial ugOrigem = new UnidadeGerencial();
		try {
			Integer idUgOrigem = Integer.parseInt(idStringUgOrigem);
			ugOrigem.setId(idUgOrigem);
			ugOrigem = unidadeGerencialService.loadWithPlanoGestao(ugOrigem);
		} catch (Exception e) {}
		return ugOrigem;
	}
	
	public Date geraDataAtual(){
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * Método responsável por criar o relatório de anomalias.
	 * Foi necessário usar a Classe MergeReport para "concatenar" dois reports que estão no modo de exibição RETRATO com o 
	 * diagrama de Causa e Efeito que esta no mode exibição PAISAGEM.
	 * @author Matheus Melo Gonçalves
	 * @param Anomalia
	 * @return mergeReport.getResource()
	 */
	@SuppressWarnings("unchecked")
	public Resource gerarRelatorioTratamentoAnomalia(Anomalia bean){
		bean = this.loadForEntrada(bean);
		//Setando valores na anomalia escolhida pelo usuário.
		bean.setEfeito(causaEfeitoService.findByAnomalia(bean));
		bean.setPlanosAcao(new ListSet<PlanoAcao>(PlanoAcao.class,planoAcaoService.findByAnomalia(bean)));
		//Listas
		List<Anomalia> listaAnomalia = new ArrayList<Anomalia>();
		List<Report> listaReport = new ArrayList<Report>();
		
		listaAnomalia.add(bean);
		//Objetos
		Report report = new Report("../relatorio/tratamentoAnomaliaMae");
		MergeReport mergeReport = new MergeReport("Tratamento de Anomalias");
		Image image1 = null;
		Image image2 = null;
		Image image4 = null;
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		//Recuperando as imagens do relatório.
		try {
			image1 = neoImageResolver.getImage("/images/img_sgm_relatorio.png");
			image2 = neoImageResolver.getImage("/images/fd_rodape.gif");
			if (parametrosSistema.getImgEmpresaRelatorio() != null) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(parametrosSistema.getImgEmpresaRelatorio().getContent());
				image4 = ImageIO.read(byteArrayInputStream);
			}
			else {
				image4 = neoImageResolver.getImage("/images/img_empresa_relatorio.png");
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Primeiro Sub-relatório
		report.addSubReport("primeiroSub", new Report("../relatorio/tratamentoAnomalia"));
		report.addParameter("numero", bean.getSequencial());
		if (bean.getLocal() != null){
			report.addParameter("local", bean.getLocal().toString().toUpperCase());
		}		
		if (bean.getOrigem() != null){
			report.addParameter("origem", bean.getOrigem().toString().toUpperCase());
		}		
		report.addParameter("dataAnomalia", bean.getDataAbertura());
		report.addParameter("ocorrenciaAnomalia", bean.getUgResponsavel().getDescricao());
		report.addParameter("origemAnomalia", bean.getUgRegistro().getDescricao());
		report.addParameter("responsavel", bean.getResponsavel() == null ? "" : bean.getResponsavel());
		report.addParameter("descAnomalia", bean.getDescricao() == null ? "" : bean.getDescricao());
		report.addParameter("contramedidasImediatas", bean.getContraMedidasImediatas() == null ? "" : bean.getContraMedidasImediatas());
		report.addParameter("obsComplementares", bean.getObservacoes() == null ? "" : bean.getObservacoes());
		if(bean.getClassificacao() != null){
			report.addParameter("classificacao", bean.getClassificacao().toString().toUpperCase());
		}	
		if(bean.getTipo() != null){
			report.addParameter("tipo", bean.getTipo().toString().toUpperCase());
		}	
		report.addParameter("LOGO", image1);
		report.addParameter("IMG_RODAPE", image2);
		report.addParameter("LOGO_EMPRESA", image4);
		report.addParameter("NEOFORMATER", NeoFormater.getInstance());
		report.addParameter("TITULO", "RELATÓRIO TRATAMENTO DE ANOMALIA");
		report.addParameter("DATA",new Date(System.currentTimeMillis()));
		report.addParameter("HORA", System.currentTimeMillis());
		report.addParameter("USUARIOLOGADO", ((Usuario)Neo.getUser()).getNome());
		report.addParameter("HEADER", "SISTEMA DE GESTÃO INTEGRADA");
		report.setDataSource(listaAnomalia);
		
		//Segundo sub.
		report.addSubReport("segundoSub", new Report("../relatorio/tratamentoAnomaliaSub"));
		report.addParameter("analiseCausas", bean.getAnaliseCausas() == null ? "" : bean.getAnaliseCausas());
		report.addParameter("verificacao", bean.getVerificacao() == null ? "" : bean.getVerificacao());
		report.addParameter("padronizacao", bean.getPadronizacao() == null ? "" : bean.getPadronizacao());
		report.addParameter("conclusao", bean.getConclusao() == null ? "" : bean.getConclusao());
		
		bean.setCausasEfeito(GeplanesUtils.listToSet(causaEfeitoService.preencheListaCausaEfeito(bean), CausaEfeito.class));
		Report diagramaReport = makeDiagramaReport(bean, image1, image2, image4);
		
		//Adicionando os dos três relatórios a uma lista que será passada a mergeReport.
		//Adicionando o primeiro report que junta os dois relatórios no modo retrato.
		listaReport.add(report);
		//Adicionando o relatório de Causa e Efeito.
		listaReport.add(diagramaReport);
		mergeReport.setReportlist(listaReport);
		
		try {
			return mergeReport.generateResource();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Report makeDiagramaReport(Anomalia bean, Image image1, Image image2, Image image4) {
		
		Report diagramaReport = new Report("../relatorio/diagramaCausaEfeito");
		//Variáveis de controle.
		int contNivel1 = 0;
		int contNivel2 = 0;
		
		//Setando os parametros do diagrama.
		if(bean.getEfeito() != null){
			diagramaReport.addParameter("causaEfeito", bean.getEfeito().getDescricao().equals("") ? null : bean.getEfeito().getDescricao());
			for (CausaEfeito nivel1 : bean.getCausasEfeito()){
				contNivel1++;
				contNivel2 = 0;
				diagramaReport.addParameter("N1-"+contNivel1, nivel1.getDescricao().equals("") ? null : nivel1.getDescricao());
				for (CausaEfeito nivel2 : nivel1.getListaCausaEfeito()){
					contNivel2++;
					diagramaReport.addParameter("N1-"+contNivel1+"-N2-"+contNivel2, nivel2.getDescricao().equals("") ? null : nivel2.getDescricao());
					for (CausaEfeito nivel3 : nivel2.getListaCausaEfeito()) {
						diagramaReport.addParameter("N1-"+contNivel1+"-N2-"+contNivel2+"-N3-"+1, nivel3.getDescricao().equals("") ? null : nivel3.getDescricao());
					}
				}
			}
		}
		if(image1 != null || image2 != null || image4 != null){
			diagramaReport.addParameter("LOGO", image1);
			diagramaReport.addParameter("IMG_RODAPE", image2);
			diagramaReport.addParameter("LOGO_EMPRESA", image4);
		}
		diagramaReport.addParameter("NEOFORMATER", NeoFormater.getInstance());
		diagramaReport.addParameter("TITULO", "ANÁLISE DA ANOMALIA - DIAGRAMA DE CAUSA E EFEITO - (Métodos dos Porque's)");
		diagramaReport.addParameter("DATA",new Date(System.currentTimeMillis()));
		diagramaReport.addParameter("HORA", System.currentTimeMillis());
		diagramaReport.addParameter("USUARIOLOGADO", ((Usuario)Neo.getUser()).getNome());
		diagramaReport.addParameter("HEADER", "SISTEMA INTEGRADA");
		
		return diagramaReport;
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param ugRegistro
	 * @param ugResponsavel
	 * @param descricao
	 * @return
	 */
	public List<Anomalia> findForReportAnomalia(UnidadeGerencial ugRegistro, UnidadeGerencial ugResponsavel, String descricao) {
		return anomaliaDAO.findForReportAnomalia(ugRegistro, ugResponsavel, descricao);
	}
	
	@Override
	public void delete(Anomalia bean) {
		bean.setEfeito(causaEfeitoService.findByAnomalia(bean));
		acompanhamentoIndicadorService.setNullAnomalia(bean);
		if(bean.getEfeito() != null)
		causaEfeitoService.excluirCausaEfeito(bean);
		super.delete(bean);
	}
	
	/**
	 * Remove todas as anomalias cuja Unidade Gerencial seja responsável pelo registro ou tratamento.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial bean) {
		
		// Remove as anomalias cuja unidade de registro seja igual ao parâmetro passado
		List<Anomalia> findIdByUGRegistro = findIdByUGRegistro(bean);
		if (findIdByUGRegistro != null && !findIdByUGRegistro.isEmpty()) {
			for (Anomalia anomalia : findIdByUGRegistro) {
				anomalia.setUgRegistro(null);
				this.delete(anomalia);
			}
		}	

		// Remove as anomalias cuja unidade responsável seja igual ao parâmetro passado
		List<Anomalia> findIdByUGResponsavel = findIdByUGResponsavel(bean);
		if (findIdByUGResponsavel != null && !findIdByUGResponsavel.isEmpty()) {
			for (Anomalia anomalia : findIdByUGResponsavel) {
				anomalia.setUgResponsavel(null);
				this.delete(anomalia);
			}
		}	
	}
	
	/**
	 * Retorna uma lista de anomalias de um indicador
	 * @author Rodrigo Duarte
	 * @param indicador
	 * @return
	 */
	public List<Anomalia> findByIndicador(Indicador indicador) {
		return anomaliaDAO.findByIndicador(indicador);
	}
	
	@Override
	public Anomalia loadForEntrada(Anomalia bean) {
		Anomalia retorno = super.loadForEntrada(bean);
		PlanoGestao planoGestao = null;
		if (retorno.getUgRegistro() != null) {
			planoGestao = retorno.getUgRegistro().getPlanoGestao();
		}
		else if (retorno.getUgResponsavel() != null) {
			planoGestao = retorno.getUgResponsavel().getPlanoGestao();
		}
		retorno.setPlanoGestao(planoGestao);
		
		// Busca os anexos da anomalia
		retorno.setAnexosAnomalia(new ListSet<AnexoAnomalia>(AnexoAnomalia.class,anexoAnomaliaService.findByAnomalia(retorno)));
		
		return retorno;
	}
	
	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial) {
		return anomaliaDAO.getProximoSequencial(unidadeGerencial);
	}
	
	public Integer getProximoSequencial(UnidadeGerencial unidadeGerencial, int offset) {
		return anomaliaDAO.getProximoSequencial(unidadeGerencial, offset);
	}
	
	/**
	 * Envia um e-mail avisando que uma nova anomalia foi registrada para cada responsável da
	 * unidade gerencial responsável pelo tratamento.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 */
	public void enviarEmailEnvolvidosAnomalia(Anomalia anomalia){
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		//Carregando os objetos que fazem parte da anomalia.
		anomalia.setPlanoGestao(PlanoGestaoService.getInstance().load(anomalia.getPlanoGestao()));
		anomalia.setUgRegistro(unidadeGerencialService.load(anomalia.getUgRegistro()));
		anomalia.setUgResponsavel(unidadeGerencialService.load(anomalia.getUgResponsavel()));
		
		//Objetos
		EnvioEmail envioEmail = EnvioEmail.getInstance();
		String mensagem = gerarMensagemEmailAnomalia(anomalia);
		StringBuilder sbErro = new StringBuilder("");
		
		//Listas
		Set<UnidadeGerencial> listaUnidades = new ListSet<UnidadeGerencial>(UnidadeGerencial.class);
		List<String> listaImgs = EmailUtil.getEmailImageList(GeplanesUtils.getApplicationPath(NeoWeb.getRequestContext().getServletRequest()));
		Set<UsuarioUnidadeGerencial> setUsuario = new HashSet<UsuarioUnidadeGerencial>();
		
		//Adicionando a UG diretamente responsável pela anomalia.
		listaUnidades.add(anomalia.getUgResponsavel());

		//Buscando os usuários responsáveis de cada UG.
		for (UnidadeGerencial Ug : listaUnidades){
			setUsuario.addAll(usuarioUnidadeGerencialService.findResponsaveisByUnidadeGerencial(Ug));
		}
		
		//Enviando e-mail para cada usuário responsável da UG.
		for (UsuarioUnidadeGerencial usuarioUnidadeGerencial : setUsuario) {
			
			try {
				envioEmail.enviaEmail(parametrosSistema.getEmailRemetente(), usuarioUnidadeGerencial.getUsuario().getEmail(), "[Geplanes] Nova anomalia registrada", mensagem, listaImgs);
			}
			catch (EnvioEmailException e) {
				sbErro
					.append("<br>")
					.append(e.getMessage());
			}			
		}
		
		if (!sbErro.toString().isEmpty()) {
			throw new GeplanesException(sbErro.toString());
		}		
	}
	/**
	 * Retorna uma String com a mensagem do e-mail que será repassada para cada responsável pela UG envolvida na anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return (String)Texto no formato HTML
	 */
	public String gerarMensagemEmailAnomalia(Anomalia anomalia) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		String msg = 
			"		<p style='text-decoration:underline;font-size:14px;text-transform: uppercase;'> <b>Dados da anomalia:</b> </p>"+
			"		<p> <b>Número:</b> "+anomalia.getSequencial()+"</p>"+
			"		<p> <b>Ano da gestão:</b> "+anomalia.getPlanoGestao().getAnoExercicio()+"</p>"+
			"		<p> <b>Setor de registro:</b> "+anomalia.getUgRegistro().getSigla() + " - " +anomalia.getUgRegistro().getNome()+"</p>"+
			"		<p> <b>Setor responsável pelo tratamento:</b> "+anomalia.getUgResponsavel().getSigla() + " - " + anomalia.getUgResponsavel().getNome()+"</p>"+
			"		<p> <b>Causa:</b> "+(anomalia.getLocal() != null ? anomalia.getLocal() : "")+"</p>"+
			"		<p> <b>Data:</b> "+dateFormat.format(anomalia.getDataAbertura())+"</p>"+
			"		<p> <b>Responsável:</b> "+anomalia.getResponsavel()+"</p>"+
			"		<p> <b>Classificação:</b> "+(anomalia.getClassificacao() != null ? anomalia.getClassificacao() : "")+"</p>"+
			"		<p> <b>Tipo:</b> "+(anomalia.getTipo() != null ? anomalia.getTipo() : "")+"</p>"+
			"		<p> <b>Origem:</b> "+(anomalia.getOrigem() != null ? anomalia.getOrigem() : "")+"</p>"+
			(anomalia.getTipoAuditoria() != null && !anomalia.getTipoAuditoria().equals("") ? "		<p> <b>Tipo de auditoria:</b> "+ anomalia.getTipoAuditoria() : "")+"</p>"+
			"		<p> <b>Descrição da anomalia:</b> "+anomalia.getDescricao()+"</p>"+
			"		<p> <b>Correção:</b> "+(anomalia.getContraMedidasImediatas() != null ? anomalia.getContraMedidasImediatas() : "")+"</p>"+
			"		<p> <b>Observações:</b> "+(anomalia.getObservacoes() != null ? anomalia.getObservacoes() : "")+"</p>";			
		
		return EnvioEmail.montaCorpoMensagem().replace("--M--", msg);
	}
	
	/**
	 * Obtem a ocorrencia de uma determinada anomalia.
	 * @author Matheus Melo 
	 * @param anomalia
	 * @return anomalia
	 */
	public Anomalia obtemOcorrencia(Anomalia anomalia) {
		return anomaliaDAO.obtemOcorrencia(anomalia);
	}
	
	/**
	 * Verifica se esta ocorrência faz parte de alguma anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param ocorrencia
	 * @return boolean 
	 */
	public Boolean fazParteAnomalia(Ocorrencia ocorrencia){
		return anomaliaDAO.fazParteAnomalia(ocorrencia);
	}
	/**
	 * Procurar anomalia pela ocorrencia
	 * @author Matheus Melo Gonçalves
	 * @param ocorrencia
	 * @return Anomalia 
	 */
	public Anomalia findByOcorrencia(Ocorrencia ocorrencia){
		return anomaliaDAO.findByOcorrencia(ocorrencia);
	}
	
	/**
	 * Verifica se existe alguma anomalia vinculada ao registro de auditoria interna passado como parâmetro.
	 * 
	 * @param itemAuditoriaInterna
	 * @return boolean 
	 */
	public Boolean existeAnomaliaVinculada(ItemAuditoriaInterna itemAuditoriaInterna) {
		return anomaliaDAO.existeAnomaliaVinculada(itemAuditoriaInterna);
	}
	
	/**
	 * Carrega o registro de anomalia com a informação da auditoria interna à
	 * qual está vinculada, caso exista.
	 * 
	 * @param anomalia
	 * @return anomalia
	 */
	public Anomalia loadWithItemAuditoriaInterna(Anomalia anomalia) {
		return anomaliaDAO.loadWithItemAuditoriaInterna(anomalia);
	}
	
	/**
	 * Retorna a anomalia vinculada à auditoria interna passada como parâmetro.
	 * 
	 * @param itemAuditoriaInterna
	 * @return Anomalia 
	 */
	public Anomalia loadByItemAuditoriaInterna(ItemAuditoriaInterna itemAuditoriaInterna) {
		return anomaliaDAO.loadByItemAuditoriaInterna(itemAuditoriaInterna);
	}	
	
	/**
	 * Cria o relatório de listagem de anomalias.
	 *
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	public IReport createAnomaliaListagemReport(AnomaliaListagemReportFiltro filtro) {
		Report report = new Report("../relatorio/anomaliaListagem");
		
		List<UnidadeGerencial> listaUGRegistro = new ArrayList<UnidadeGerencial>();		
		List<UnidadeGerencial> listaUGResponsavel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(filtro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin  = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsRepQualidade = usuarioService.isUsuarioLogadoVinculadoAreaQualidade(filtro.getPlanoGestao());
		
		// UG de Registro
		if (filtro.getUgRegistro() != null) {
			listaUGRegistro.add(filtro.getUgRegistro());
			
			if (filtro.isIncluirSubordinadasReg()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGRegistro = unidadeGerencialService.getListaDescendencia(filtro.getUgRegistro(), listaUGRegistro);
			}
		}
		filtro.setListaUnidadeGerencialReg(listaUGRegistro);
		
		// UG Responsável
		if (filtro.getUgResponsavel() != null) {
			listaUGResponsavel.add(filtro.getUgResponsavel());
			
			if (filtro.isIncluirSubordinadasResp()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGResponsavel = unidadeGerencialService.getListaDescendencia(filtro.getUgResponsavel(), listaUGResponsavel);
			}
		}
		filtro.setListaUnidadeGerencialResp(listaUGResponsavel);		
		
		// UGs disponíveis para a listagem
		if (!usuarioLogadoIsAdmin && !usuarioLogadoIsRepQualidade) {
			for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
				listaUGDisponivel.add(unidadeGerencial);
				listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
			}			
		}
		filtro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);		
		
		List<Anomalia> listaAnomalia = this.findForReportListagemAnomalia(filtro);
		
		report.setDataSource(listaAnomalia);
		
		String tituloUgRegistro = null;
		if (filtro.getUgRegistro() != null) {
			tituloUgRegistro = unidadeGerencialService.load(filtro.getUgRegistro()).getDescricao();
			
			if (filtro.isIncluirSubordinadasReg()) {
				tituloUgRegistro += " (incluindo subordinadas)";
			}
		}
		
		String tituloUgResponsavel = null;
		if (filtro.getUgResponsavel() != null) {
			tituloUgResponsavel = unidadeGerencialService.load(filtro.getUgResponsavel()).getDescricao();
			
			if (filtro.isIncluirSubordinadasReg()) {
				tituloUgResponsavel += " (incluindo subordinadas)";
			}
		}
		
		report.addParameter("PLANOGESTAO", filtro.getPlanoGestao() != null ? PlanoGestaoService.getInstance().load(filtro.getPlanoGestao()).getAnoExercicio().toString() : null);
		report.addParameter("UGREGISTRO", tituloUgRegistro);
		report.addParameter("UGRESPONSAVEL", tituloUgResponsavel);
		report.addParameter("STATUS", filtro.getStatus() != null ? filtro.getStatus().toString() : null);
		report.addParameter("STATUSTRATAMENTO", filtro.getStatusTratamento() != null ? filtro.getStatusTratamento().toString() : null);
		
		return report;
	}
	
	/**
	 * Cria o relatório sintético de anomalias por status.
	 *
	 * @param filtro
	 * @return
	 */
	public IReport createAnomaliaSinteticoPorStatusReport(AnomaliaSinteticoPorStatusReportFiltro filtro) {
		Report report = new Report("../relatorio/anomaliaSinteticoPorStatus");
		
		List<UnidadeGerencial> listaUGRegistro = new ArrayList<UnidadeGerencial>();		
		List<UnidadeGerencial> listaUGResponsavel = new ArrayList<UnidadeGerencial>();
		
		// UG de Registro
		if (filtro.getUgRegistro() != null) {
			listaUGRegistro.add(filtro.getUgRegistro());
			
			if (filtro.isIncluirSubordinadasReg()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGRegistro = unidadeGerencialService.getListaDescendencia(filtro.getUgRegistro(), listaUGRegistro);
			}
		}
		filtro.setListaUnidadeGerencialReg(listaUGRegistro);
		
		// UG Responsável
		if (filtro.getUgResponsavel() != null) {
			listaUGResponsavel.add(filtro.getUgResponsavel());
			
			if (filtro.isIncluirSubordinadasResp()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGResponsavel = unidadeGerencialService.getListaDescendencia(filtro.getUgResponsavel(), listaUGResponsavel);
			}
		}
		filtro.setListaUnidadeGerencialResp(listaUGResponsavel);		
		
		List<AnomaliaSinteticoPorStatusReportBean> listaReportBean = new ArrayList<AnomaliaSinteticoPorStatusReportBean>();
		AnomaliaSinteticoPorStatusReportBean reportBean;
		Integer quantidade;
		
		
		for (StatusAnomaliaEnum statusAnomaliaEnum : StatusAnomaliaEnum.values()) {
			quantidade = findForAnomaliaSinteticoPorStatusReport(filtro, statusAnomaliaEnum);
			if (quantidade == null) {
				quantidade = 0;
			}
			reportBean = new AnomaliaSinteticoPorStatusReportBean();
			reportBean.setStatus(statusAnomaliaEnum.toString());
			reportBean.setQuantidade(quantidade);
			
			listaReportBean.add(reportBean);
		}
		
		report.setDataSource(listaReportBean);
		
		String tituloUgRegistro = null;
		if (filtro.getUgRegistro() != null) {
			tituloUgRegistro = unidadeGerencialService.load(filtro.getUgRegistro()).getDescricao();
			
			if (filtro.isIncluirSubordinadasReg()) {
				tituloUgRegistro += " (incluindo subordinadas)";
			}
		}
		
		String tituloUgResponsavel = null;
		if (filtro.getUgResponsavel() != null) {
			tituloUgResponsavel = unidadeGerencialService.load(filtro.getUgResponsavel()).getDescricao();
			
			if (filtro.isIncluirSubordinadasReg()) {
				tituloUgResponsavel += " (incluindo subordinadas)";
			}
		}
		
		report.addParameter("PLANOGESTAO", filtro.getPlanoGestao() != null ? PlanoGestaoService.getInstance().load(filtro.getPlanoGestao()).getAnoExercicio().toString() : null);
		report.addParameter("UGREGISTRO", tituloUgRegistro);
		report.addParameter("UGRESPONSAVEL", tituloUgResponsavel);
		
		return report;
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.sgm.dao.AnomaliaDAO#findForReportListagemAnomalia
	 * 
	 * @param filtro
	 * @return
	 * @author Rodrigo Freitas
	 */
	private List<Anomalia> findForReportListagemAnomalia(AnomaliaListagemReportFiltro filtro) {
		return anomaliaDAO.findForReportListagemAnomalia(filtro);
	}
	
	/**
	 * Verifica se a data para travamento de uma anomalia por falta de tratamento
	 * está expirada.
	 * 
	 * @param anomalia
	 * @param diasTravamentoAnomalia
	 * @return
	 */
	public Boolean isDataTravamentoAnomaliaExpirada(Anomalia anomalia, int diasTravamentoAnomalia) {
		if (anomalia != null && anomalia.getDataAbertura() != null) {
			Date dataAtual = new Date(System.currentTimeMillis());
			int diferencaDias;

			if (anomalia.getDataDestravamento() != null) {
				diferencaDias = DateUtil.diferencaDias(dataAtual, anomalia.getDataDestravamento());	
			}
			else {
				diferencaDias = DateUtil.diferencaDias(dataAtual, anomalia.getDataAbertura());
			}
			
			return diferencaDias > diasTravamentoAnomalia;
		}
		return false;
	}
	
	/**
	 * Verifica se a data para encerramento de uma anomalia está expirada.
	 * 
	 * @param anomalia
	 * @param diasEncerramentoAnomalia
	 * @return
	 */
	public Boolean isDataEncerramentoAnomaliaExpirada(Anomalia anomalia, int diasEncerramentoAnomalia) {
		if (anomalia != null && anomalia.getDataSolicitacaoEncerramento() != null) {
			Date dataAtual = new Date(System.currentTimeMillis());
			int diferencaDias = DateUtil.diferencaDias(dataAtual, anomalia.getDataSolicitacaoEncerramento());
			
			return diferencaDias > diasEncerramentoAnomalia;
		}
		return false;
	}
	
	/**
	 * Verifica se a data para lembrete do tratamento de uma anomalia foi atingida.
	 * 
	 * @param anomalia
	 * @param diasLembreteTratamentoAnomalia
	 * @return
	 */
	public Boolean isDataLembreteTratamentoAnomaliaAtingida(Anomalia anomalia, int diasLembreteTratamentoAnomalia) {
		if (anomalia != null) {
			Date dataAtual = new Date(System.currentTimeMillis());
			int diferencaDias;
			
			if (anomalia.getDataDestravamento() != null) {
				diferencaDias = DateUtil.diferencaDias(dataAtual, anomalia.getDataDestravamento());	
			}
			else {
				diferencaDias = DateUtil.diferencaDias(dataAtual, anomalia.getDataAbertura());
			}
			
			return diferencaDias > diasLembreteTratamentoAnomalia;
		}
		return false;
	}
	
	/**
	 * Retorna a data limite para tratamento da anomalia, antes do seu travamento.
	 * 
	 * @param anomalia
	 * @param diasTravamentoAnomalia
	 * @return
	 */
	public Date getDataLimiteParaTratamento(Anomalia anomalia, int diasTravamentoAnomalia) {
		if (anomalia != null) {
			if (anomalia.getDataDestravamento() != null) {
				return DateUtil.addDiasData(anomalia.getDataDestravamento(), diasTravamentoAnomalia);	
			}
			else {
				return DateUtil.addDiasData(anomalia.getDataAbertura(), diasTravamentoAnomalia);	
			}
		}
		return null;
	}
	
	/**
	 * Retorna as anomalias não encerradas de um determinado ano de gestão.
	 * 
	 * @param planoGestao
	 * @return
	 */
	public List<Anomalia> findAnomaliasNaoEncerradas(PlanoGestao planoGestao) {
		return anomaliaDAO.findAnomaliasNaoEncerradas(planoGestao);
	}
	
	/**
	 * Verifica se uma anomalia foi corretamente tratada.
	 * Considerar-se-á para verificação do tratamento da anomalia o preenchimento obrigatório dos seguintes campos: 
	 * 	Análise de Causas
	 * 	Plano de Ação – O que 
	 * 	Plano de Ação – Como
	 * 	Plano de Ação – Por que 
	 * 	Plano de Ação – Quem
	 * 	Plano de Ação – Quando 
	 * 	Plano de Ação - Status
	 * 
	 * @param anomalia
	 * @return
	 */
	public Boolean isAnomaliaTratada(Anomalia anomalia) {
		
		if (anomalia != null) {
			if (anomalia.getAnaliseCausas() != null && !anomalia.getAnaliseCausas().equals("")) {
				if (anomalia.getPlanosAcao() != null && !anomalia.getPlanosAcao().isEmpty()) {
					for (PlanoAcao planoAcao : anomalia.getPlanosAcao()) {
						if (planoAcao.getTexto() != null && !planoAcao.getTexto().equals("") &&
							planoAcao.getTextoComo() != null && !planoAcao.getTextoComo().equals("") &&
							planoAcao.getTextoPorque() != null && !planoAcao.getTextoPorque().equals("") &&
							planoAcao.getTextoQuem() != null && !planoAcao.getTextoQuem().equals("") &&
							planoAcao.getDtPlano() != null &&
							planoAcao.getStatus() != null) {
							
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public void atualizaEnvioLembreteTratamentoAnomalia(Anomalia anomalia) {
		anomaliaDAO.atualizaEnvioLembreteTratamentoAnomalia(anomalia);
	}
	
	public StatusAnomaliaEnum getStatusAnomalia(Anomalia anomalia, int diasTravamentoAnomalia, int diasEncerramentoAnomalia) {
		if (anomalia != null) {
			
			if (anomalia.getStatus() != null) {
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.ABERTA)) {
					if (isAnomaliaTratada(anomalia)) {
						return StatusAnomaliaEnum.TRATADA;
					}
					if (isDataTravamentoAnomaliaExpirada(anomalia, diasTravamentoAnomalia)) {
						return StatusAnomaliaEnum.BLOQUEADA;
					}
					return StatusAnomaliaEnum.ABERTA;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.BLOQUEADA)) {
					if (anomalia.getDataDestravamento() != null && !isDataTravamentoAnomaliaExpirada(anomalia, diasTravamentoAnomalia)) {
						return StatusAnomaliaEnum.TRATAMENTO_PENDENTE;
					}					
					return StatusAnomaliaEnum.BLOQUEADA;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.CUMPRIMENTO_PENDENTE)) {
					if (isAnomaliaTratada(anomalia)) {
						return StatusAnomaliaEnum.TRATADA;
					}					
					return StatusAnomaliaEnum.CUMPRIMENTO_PENDENTE;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRADA)) {
					if (anomalia.getDataEncerramento() != null) {
						return StatusAnomaliaEnum.ENCERRADA;
					}
					return StatusAnomaliaEnum.ABERTA;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_PENDENTE)) {
					if (anomalia.getDataEncerramento() != null) {
						return StatusAnomaliaEnum.ENCERRADA;
					}					
					return StatusAnomaliaEnum.ENCERRAMENTO_PENDENTE;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO)) {
					if (anomalia.getDataEncerramento() != null) {
						return StatusAnomaliaEnum.ENCERRADA;
					}
					if (isDataEncerramentoAnomaliaExpirada(anomalia, diasEncerramentoAnomalia)) {
						return StatusAnomaliaEnum.ENCERRAMENTO_PENDENTE;
					}
					return StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.REANALISE)) {
					return StatusAnomaliaEnum.REANALISE;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.TRATADA)) {
					if (anomalia.getDataSolicitacaoEncerramento() != null) {
						return StatusAnomaliaEnum.ENCERRAMENTO_SOLICITADO;
					}
					return StatusAnomaliaEnum.TRATADA;
				}
				
				if (anomalia.getStatus().equals(StatusAnomaliaEnum.TRATAMENTO_PENDENTE)) {
					if (isAnomaliaTratada(anomalia)) {
						return StatusAnomaliaEnum.TRATADA;
					}
					if (isDataTravamentoAnomaliaExpirada(anomalia, diasTravamentoAnomalia)) {
						return StatusAnomaliaEnum.BLOQUEADA;
					}					
					return StatusAnomaliaEnum.TRATAMENTO_PENDENTE;
				}
			}
			return StatusAnomaliaEnum.ABERTA;
		}
		return null;
	}
	
	public StatusTratamentoAnomaliaEnum getStatusTratamentoAnomalia(Anomalia anomalia) {
		if (anomalia != null) {
			if (isAnomaliaTratada(anomalia)) {
				if (anomalia.getDataDestravamento() != null) {
					return StatusTratamentoAnomaliaEnum.FORA_DO_PRAZO;
				}
				else {
					return StatusTratamentoAnomaliaEnum.DENTRO_DO_PRAZO;
				}
			}
		}
		return null;
	}
	
	/**
	 * Percorre a lista de UGs e retorna a unidade gerencial que possui 
	 * o mesmo id da ug passada como parâmetro.
	 *  
	 * @param listaUG
	 * @param ug
	 * @return
	 */
	public UnidadeGerencial obtemUG(List<UnidadeGerencial> listaUG, UnidadeGerencial ug) {
		if (listaUG != null && ug != null) {
			for (UnidadeGerencial unidadeGerencial : listaUG) {
				if (unidadeGerencial.getId().equals(ug.getId())) {
					return unidadeGerencial;
				}
			}
		}
		return null;
	}
	
	/**
	 * Percorre a hierarquia de UGs e retorna a unidade gerencial que possui 
	 * o mesmo id da ug passada como parâmetro.
	 *  
	 * @param ugRaiz
	 * @param ug
	 * @return
	 */
	public UnidadeGerencial obtemUGHierarquia(UnidadeGerencial ugRaiz, UnidadeGerencial ug) {
		if (ugRaiz != null && ug != null) {
			if (ugRaiz.getId().equals(ug.getId())) {
				return ugRaiz;
			}
			if (ugRaiz.getFilhos() != null) {
				for (UnidadeGerencial ugFilha : ugRaiz.getFilhos()) {
					UnidadeGerencial ugNeta = obtemUGHierarquia(ugFilha, ug);
					if (ugNeta != null) {
						return ugNeta;
					}
				}
			}
		}
		return null;
	}
	
	public List<Anomalia> findForAtualizacaoStatus() {
		return anomaliaDAO.findForAtualizacaoStatus();
	}
	
	public void atualizaStatusAnomalia(Anomalia anomalia, StatusAnomaliaEnum status) {
		anomaliaDAO.atualizaStatusAnomalia(anomalia, status);
	}
	
	public Integer findForAnomaliaSinteticoPorStatusReport(AnomaliaSinteticoPorStatusReportFiltro filtro, StatusAnomaliaEnum statusAnomaliaEnum) {
		return anomaliaDAO.findForAnomaliaSinteticoPorStatusReport(filtro, statusAnomaliaEnum);
	}
	
	/**
	 * Retorna todos os usuários das áreas de qualidade
	 * que estão envolvidos em uma determinada anomalia.
	 * 
	 * @param anomalia
	 * @return
	 */
	public List<Usuario> findUsuariosQualidadeEnvolvidosAnomalia(Anomalia anomalia) {
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		
		// UGs da Área de Qualidade
		List<UnidadeGerencial> listaUnidadeGerencialQualidade = unidadeGerencialService.findUGQualidade(anomalia.getPlanoGestao());
		
		// Os usuários pertencentes aos setores de qualidade serão notificados
		if (listaUnidadeGerencialQualidade != null) {
			for (UnidadeGerencial ugQualidadePresidencia : listaUnidadeGerencialQualidade) {
				listaUsuario.addAll(usuarioService.findByUg(ugQualidadePresidencia));
			}
		}
		
		return listaUsuario;
	}
	
	public void enviaEmailSolicitacaoEncerramento(Anomalia anomalia) {
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		String from = parametrosSistema.getEmailRemetente();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<Usuario> listaUsuario = findUsuariosQualidadeEnvolvidosAnomalia(anomalia);
		
		if (listaUsuario != null) {
			anomalia.setPlanoGestao(PlanoGestaoService.getInstance().load(anomalia.getPlanoGestao()));
			for (Usuario usuario : listaUsuario) {
				
				String msg = 	
					"Prezado(a) usuário(a) " + usuario.getNome() + ",<br>" +		
					"foi solicitado o encerramento da seguinte anomalia:<BR><BR>" +
					"<b>Número:</b> "+anomalia.getSequencial()+"<BR>"+
					"<b>Ano da gestão:</b> "+anomalia.getPlanoGestao().getAnoExercicio()+"<BR>"+
					"<b>Setor de Registro:</b> "+anomalia.getUgRegistro().getSigla() + " - " +anomalia.getUgRegistro().getNome()+"<BR>"+
					"<b>Setor Responsável pelo Tratamento:</b> "+anomalia.getUgResponsavel().getSigla() + " - " + anomalia.getUgResponsavel().getNome()+"<BR>"+
					"<b>Causa:</b> "+(anomalia.getLocal() != null ? anomalia.getLocal() : "")+"<BR>"+
					"<b>Data:</b> "+dateFormat.format(anomalia.getDataAbertura())+"<BR>"+
					"<b>Responsável:</b> "+anomalia.getResponsavel()+"<BR>"+
					"<b>Classificação:</b> "+(anomalia.getClassificacao() != null ? anomalia.getClassificacao() : "")+"<BR>"+
					"<b>Tipo:</b> "+(anomalia.getTipo() != null ? anomalia.getTipo() : "")+"<BR>"+
					"<b>Origem:</b> "+(anomalia.getOrigem() != null ? anomalia.getOrigem() : "")+"<BR>"+
					"<b>Descrição da anomalia:</b> "+anomalia.getDescricao()+"<BR>"+
					"<b>Correção:</b> "+anomalia.getContraMedidasImediatas()+"<BR>"+
					"<b>Observações:</b> "+anomalia.getObservacoes()+"<BR>";
				
				msg = EnvioEmail.montaCorpoMensagem().replace("--M--", msg);
				
				String[] to = new String[1];
				to[0] = usuario.getEmail();
				
				EnvioEmail.getInstance().enviaEmail(from, to, "[Geplanes] - Solicitação de encerramento de anomalia.", msg);		
			}
		}
	}
	
	public void enviaEmailNotificacaoCumprimentoPendente(Anomalia anomalia) {
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		String from = parametrosSistema.getEmailRemetente();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<UsuarioUnidadeGerencial> listaUsuarioUG = usuarioUnidadeGerencialService.findResponsaveisByUnidadeGerencial(anomalia.getUgResponsavel());
		
		if (listaUsuarioUG != null && !listaUsuarioUG.isEmpty()) {
			anomalia.setPlanoGestao(PlanoGestaoService.getInstance().load(anomalia.getPlanoGestao()));
			for (UsuarioUnidadeGerencial usuarioUnidadeGerencial : listaUsuarioUG) {
				Usuario usuario = usuarioUnidadeGerencial.getUsuario();		
		
				String msg = 	
					"Prezado(a) usuário(a) " + usuario.getNome() + ",<br>" +		
					"foi notificado o cumprimento pendente do plano de ação para a seguinte anomalia:<BR><BR>" +
					"<b>Número:</b> "+anomalia.getSequencial()+"<BR>"+
					"<b>Ano da gestão:</b> "+anomalia.getPlanoGestao().getAnoExercicio()+"<BR>"+
					"<b>Setor de Registro:</b> "+anomalia.getUgRegistro().getSigla() + " - " +anomalia.getUgRegistro().getNome()+"<BR>"+
					"<b>Setor Responsável pelo Tratamento:</b> "+anomalia.getUgResponsavel().getSigla() + " - " + anomalia.getUgResponsavel().getNome()+"<BR>"+
					"<b>Causa:</b> "+(anomalia.getLocal() != null ? anomalia.getLocal() : "")+"<BR>"+
					"<b>Data:</b> "+dateFormat.format(anomalia.getDataAbertura())+"<BR>"+
					"<b>Responsável:</b> "+anomalia.getResponsavel()+"<BR>"+
					"<b>Classificação:</b> "+(anomalia.getClassificacao() != null ? anomalia.getClassificacao() : "")+"<BR>"+
					"<b>Tipo:</b> "+(anomalia.getTipo() != null ? anomalia.getTipo() : "")+"<BR>"+
					"<b>Origem:</b> "+(anomalia.getOrigem() != null ? anomalia.getOrigem() : "")+"<BR>"+
					"<b>Descrição da anomalia:</b> "+anomalia.getDescricao()+"<BR>"+
					"<b>Correção:</b> "+anomalia.getContraMedidasImediatas()+"<BR>"+
					"<b>Observações:</b> "+anomalia.getObservacoes()+"<BR>";
				
				msg = EnvioEmail.montaCorpoMensagem().replace("--M--", msg);
				
				String[] to = new String[1];
				to[0] = usuario.getEmail();
				
				EnvioEmail.getInstance().enviaEmail(from, to, "[Geplanes] - Notificação de cumprimento pendente de plano de ação.", msg);		
			}
		}
	}
	
	public Anomalia loadParent(Anomalia anomalia) {
		Anomalia anomaliaComPai = anomaliaDAO.loadWithParent(anomalia);
		Anomalia anomaliaPai = null;
		if (anomaliaComPai != null && anomaliaComPai.getSubordinacao() != null) {
			anomaliaPai = anomaliaComPai.getSubordinacao();
			anomaliaPai = this.loadForEntrada(anomaliaPai);
			anomaliaPai.setPlanosAcao(new ListSet<PlanoAcao>(PlanoAcao.class,planoAcaoService.findByAnomalia(anomaliaPai)));
		}
		return anomaliaPai;
	}
	
	public List<Anomalia> findIdByUGRegistro(UnidadeGerencial ugRegistro) {
		return anomaliaDAO.findIdByUGRegistro(ugRegistro);
	}
	
	public List<Anomalia> findIdByUGResponsavel(UnidadeGerencial ugResponsavel) {
		return anomaliaDAO.findIdByUGResponsavel(ugResponsavel);
	}

	/**
	 * Retorna as anomalias NÃO ENCERRADAS cuja unidade gerencial responsável pelo tratamento
	 * seja a passada como parâmetro. 
	 * Serão preenchidos somente os campos necessários para a verificação do tratamento da anomalia.
	 * 
	 * @param ugResponsavel
	 * @return
	 */
	public List<Anomalia> findByUGResponsavelForControlePendencia(UnidadeGerencial ugResponsavel) {
		return anomaliaDAO.findByUGResponsavelForControlePendencia(ugResponsavel);
	}
	
	public List<Anomalia> findSubordinadas(Anomalia anomaliaPai) {
		return anomaliaDAO.findSubordinadas(anomaliaPai);
	}
	
	/**
	 * Calcula se a causa da anomalia foi interna ou externa.
	 * 
	 * @param anomalia
	 * @return
	 */
	public LocalAnomaliaEnum calculaLocalAnomalia(Anomalia anomalia) {
		Anomalia anomaliaAud;
		
		if (anomalia.getId() != null) {
			// Verifica se a anomalia foi gerada por um processo de auditoria interna.
			anomaliaAud = loadWithItemAuditoriaInterna(anomalia);
		}
		else {
			anomaliaAud = anomalia;
		}
		
		if (anomaliaAud.getItemAuditoriaInterna() != null) {
			// Verifica se a não conformidade encontrada pela auditoria interna possui causa externa.
			if (anomaliaAud.getItemAuditoriaInterna().getUgExterna() == null ) {
				return LocalAnomaliaEnum.INTERNA;
			}
			else {
				return LocalAnomaliaEnum.EXTERNA;
			}			
		}
		else {
			if (anomalia.getUgResponsavel().getId().equals(anomalia.getUgRegistro().getId())) {
				return LocalAnomaliaEnum.INTERNA;
			}
			else {
				return LocalAnomaliaEnum.EXTERNA;
			}			
		}
	}	
}
