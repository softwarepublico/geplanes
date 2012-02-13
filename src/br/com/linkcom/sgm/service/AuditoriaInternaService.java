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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.AuditoriaInterna;
import br.com.linkcom.sgm.beans.ItemAuditoriaInterna;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioAuditoriaInterna;
import br.com.linkcom.sgm.beans.enumeration.OrigemAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.TipoAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.TipoUsuarioAuditoriaInternaEnum;
import br.com.linkcom.sgm.dao.AuditoriaInternaDAO;
import br.com.linkcom.sgm.exception.EnvioEmailException;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AuditoriaInternaFiltro;
import br.com.linkcom.sgm.report.bean.AuditoriaInternaReportBean;
import br.com.linkcom.sgm.util.email.EnvioEmail;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class AuditoriaInternaService extends GenericService<AuditoriaInterna> {
	
	private AuditoriaInternaDAO auditoriaInternaDAO;
	private UsuarioService usuarioService;
	private UnidadeGerencialService unidadeGerencialService;
	private AnomaliaService anomaliaService;
	private NormaService normaService;
	
	public void setAuditoriaInternaDAO(AuditoriaInternaDAO auditoriaInternaDAO) {
		this.auditoriaInternaDAO = auditoriaInternaDAO;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setAnomaliaService(AnomaliaService anomaliaService) {
		this.anomaliaService = anomaliaService;
	}
	
	public void setNormaService(NormaService normaService) {
		this.normaService = normaService;
	}
	
	@Override
	public ListagemResult<AuditoriaInterna> findForListagem(FiltroListagem filtro) {
		AuditoriaInternaFiltro auditoriaInternaFiltro = (AuditoriaInternaFiltro) filtro;
		
		List<UnidadeGerencial> listaUGResponsavel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(auditoriaInternaFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		Boolean usuarioLogadoIsRepQualidade = usuarioService.isUsuarioLogadoVinculadoAreaQualidade(auditoriaInternaFiltro.getPlanoGestao());
		Boolean usuarioLogadoIsAuditorInterno = usuarioService.isUsuarioLogadoVinculadoAreaAuditoriaInterna(auditoriaInternaFiltro.getPlanoGestao());
		
		// UG Responsável
		if (auditoriaInternaFiltro.getUgResponsavel() != null) {
			listaUGResponsavel.add(auditoriaInternaFiltro.getUgResponsavel());
			
			if (auditoriaInternaFiltro.isIncluirSubordinadasResp()) {
				// Busca todas as UGs subordinadas da UG selecionada
				listaUGResponsavel = unidadeGerencialService.getListaDescendencia(auditoriaInternaFiltro.getUgResponsavel(), listaUGResponsavel);
			}
		}
		auditoriaInternaFiltro.setListaUnidadeGerencialResp(listaUGResponsavel);		
		
		// UGs disponíveis para a listagem
		if (!usuarioLogadoIsAdmin && !usuarioLogadoIsRepQualidade && !usuarioLogadoIsAuditorInterno) {
			for (UnidadeGerencial unidadeGerencial : listaUGUsuario) {
				listaUGDisponivel.add(unidadeGerencial);
				listaUGDisponivel = unidadeGerencialService.getListaDescendencia(unidadeGerencial, listaUGDisponivel);
			}
			
			if (listaUGDisponivel.isEmpty()) {
				listaUGDisponivel.add(new UnidadeGerencial(-1));
			}
		}
		auditoriaInternaFiltro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);
		
		return super.findForListagem(auditoriaInternaFiltro);
	}
	
	@Override
	public AuditoriaInterna loadForEntrada(AuditoriaInterna bean) {
		bean = super.loadForEntrada(bean);
		
		if (bean.getUgRegistro() != null) {
			bean.setPlanoGestao(bean.getUgRegistro().getPlanoGestao());
		}
		
		List<ItemAuditoriaInterna> listaNaoConformidades = new ArrayList<ItemAuditoriaInterna>();
		List<ItemAuditoriaInterna> listaOutrasNaoConformidades = new ArrayList<ItemAuditoriaInterna>();
		
		if (bean.getListaItemAuditoriaInterna() != null) {
			for (ItemAuditoriaInterna itemAuditoriaInterna : bean.getListaItemAuditoriaInterna()) {
				if (itemAuditoriaInterna.getRequisitoNorma() != null && itemAuditoriaInterna.getRequisitoNorma().getNorma().equals(bean.getNorma())) {
					listaNaoConformidades.add(itemAuditoriaInterna);
				}
				else {
					listaOutrasNaoConformidades.add(itemAuditoriaInterna);
				}
			}
		}
		bean.setListaNaoConformidades(listaNaoConformidades);
		bean.setListaOutrasNaoConformidades(listaOutrasNaoConformidades);
		
		List<UsuarioAuditoriaInterna> listaEquipeAuditora = new ArrayList<UsuarioAuditoriaInterna>();
		List<UsuarioAuditoriaInterna> listaEquipeAuditada = new ArrayList<UsuarioAuditoriaInterna>();
		
		if (bean.getListaUsuarioAuditoriaInterna() != null) {
			for (UsuarioAuditoriaInterna usuarioAuditoriaInterna : bean.getListaUsuarioAuditoriaInterna()) {
				if (usuarioAuditoriaInterna.getTipo().equals(TipoUsuarioAuditoriaInternaEnum.AUDITOR)) {
					listaEquipeAuditora.add(usuarioAuditoriaInterna);
				}
				else {
					listaEquipeAuditada.add(usuarioAuditoriaInterna);
				}
			}
		}
		bean.setListaEquipeAuditora(listaEquipeAuditora);
		bean.setListaEquipeAuditada(listaEquipeAuditada);		
		
		return bean;
	}
	
	@Override
	public void delete(AuditoriaInterna bean) {
		boolean podeExcuir = true;
		bean = loadForEntrada(bean);
		// Verifica se alguma não conformidade do registro de auditoria interna possui alguma anomalia vinculada.
		if (bean.getListaItemAuditoriaInterna() != null) {
			for (ItemAuditoriaInterna itemAuditoriaInterna : bean.getListaItemAuditoriaInterna()) {
				if (anomaliaService.existeAnomaliaVinculada(itemAuditoriaInterna)) {
					podeExcuir = false;
					break;
				}
			}
		}
		if (podeExcuir) {
			super.delete(bean);
		}
		else {
			throw new GeplanesException("Este registro de auditoria interna não pode ser excluído, pois já existe(m) anomalia(s) vinculada(s) ao(s) seu(s) registro(s) de não conformidade.");
		}
	}
	
	public String salvaAuditoriaInterna(final AuditoriaInterna bean, final boolean solicitarEncerramentoAuditoria, final boolean encerrarAuditoria) {
		
		Object error = getGenericDAO().getTransactionTemplate().execute(new TransactionCallback(){

			public Object doInTransaction(TransactionStatus arg0) {
				
				String errorMessage = null;
				
				// Salva o registro de auditoria interna
				saveOrUpdateWithoutTransaction(bean);

				// Caso seja solicitado o encerramento da auditoria interna, 
				// envia email para os responsáveis da área de qualidade envolvida.
				if (solicitarEncerramentoAuditoria) {
					try {
						enviaEmailSolicitacaoEncerramento(bean);
					}
					catch (GeplanesException e) {
						errorMessage = e.getMessage();
					}
				}
								
				// Caso a auditoria interna seja encerrada, 
				// gera um registro de anomalia para cada não conformidade encontrada.
				else if (encerrarAuditoria) {
					if (bean.getListaItemAuditoriaInterna() != null && !bean.getListaItemAuditoriaInterna().isEmpty()) {
						String erroAnomalia;
						StringBuilder sbErroAnomalia = new StringBuilder("");
						Anomalia anomalia;
						bean.setNorma(normaService.load(bean.getNorma()));
						
						// Obtém somente os itens que ocasionarão a geração de registro de anomalia.
						List<ItemAuditoriaInterna> listaItemAuditoriaInternaFiltrado = new ArrayList<ItemAuditoriaInterna>();
						for (ItemAuditoriaInterna itemAuditoriaInterna : bean.getListaItemAuditoriaInterna()) {
							if (itemAuditoriaInterna.getId() == null || !anomaliaService.existeAnomaliaVinculada(itemAuditoriaInterna)) {
								listaItemAuditoriaInternaFiltrado.add(itemAuditoriaInterna);
							}
						}
						
						int sequencial = 0;
						int i = 0;
												
						for (ItemAuditoriaInterna itemAuditoriaInternaFiltrado : listaItemAuditoriaInternaFiltrado) {
							
							if (i == 0) {
								sequencial = anomaliaService.getProximoSequencial(bean.getUgRegistro(), listaItemAuditoriaInternaFiltrado.size());
							}
	
							// Cria o registro de anomalia
							anomalia = new Anomalia();
							anomalia.setPlanoGestao(bean.getPlanoGestao());
							anomalia.setUgRegistro(bean.getUgRegistro());
							anomalia.setUgResponsavel(itemAuditoriaInternaFiltrado.getUgExterna() != null ? itemAuditoriaInternaFiltrado.getUgExterna() : bean.getUgResponsavel());
							anomalia.setTipo(TipoAnomaliaEnum.NAO_CONFORMIDADE);
							anomalia.setOrigem(OrigemAnomaliaEnum.AUDITORIA_INTERNA);
							anomalia.setTipoAuditoria(bean.getNorma().getNome());
							anomalia.setDataAbertura(new Date(System.currentTimeMillis()));
							anomalia.setStatus(StatusAnomaliaEnum.ABERTA);
							anomalia.setResponsavel(Neo.getUser().toString());
							anomalia.setDescricao(itemAuditoriaInternaFiltrado.getDescricao());
							anomalia.setSequencial(sequencial + i);
							anomalia.setItemAuditoriaInterna(itemAuditoriaInternaFiltrado);
							
							erroAnomalia = anomaliaService.salvaAnomalia(anomalia, false);
							
							if (erroAnomalia != null) {
								sbErroAnomalia
									.append("<br>")
									.append(erroAnomalia);
							}
							i++;
						}
						
						if (!sbErroAnomalia.toString().isEmpty()) {
							errorMessage = sbErroAnomalia.toString();
						}
					}
				}
				return errorMessage;
			}
		});
		
		return error != null ? error.toString() : null;
	}
	
	/**
	 * Remove todas as auditorias internas cuja Unidade Gerencial seja responsável pelo registro ou tratamento.
	 * 
	 * @param unidadeGerencial
	 */
	public void deleteByUnidadeGerencial(UnidadeGerencial bean) {
		
		// Remove as auditorias internas cuja unidade de registro seja igual ao parâmetro passado
		List<AuditoriaInterna> findIdByUGRegistro = findIdByUGRegistro(bean);
		if (findIdByUGRegistro != null && !findIdByUGRegistro.isEmpty()) {
			for (AuditoriaInterna auditoriaInterna : findIdByUGRegistro) {
				auditoriaInterna.setUgRegistro(null);
				this.delete(auditoriaInterna);
			}
		}	

		// Remove as auditorias internas cuja unidade responsável seja igual ao parâmetro passado
		List<AuditoriaInterna> findIdByUGResponsavel = findIdByUGResponsavel(bean);
		if (findIdByUGResponsavel != null && !findIdByUGResponsavel.isEmpty()) {
			for (AuditoriaInterna auditoriaInterna : findIdByUGResponsavel) {
				auditoriaInterna.setUgResponsavel(null);
				this.delete(auditoriaInterna);
			}
		}	
	}
	
	public List<AuditoriaInterna> findIdByUGRegistro(UnidadeGerencial ugRegistro) {
		return auditoriaInternaDAO.findIdByUGRegistro(ugRegistro); 
	}	
	
	public List<AuditoriaInterna> findIdByUGResponsavel(UnidadeGerencial ugResponsavel) {
		return auditoriaInternaDAO.findIdByUGResponsavel(ugResponsavel);
	}
	
	/**
	 * Gera o relatório de Auditoria Interna.
	 * 
	 * @param  AuditoriaInterna
	 * @return IReport
	 */
	public IReport createReportAuditoriaInterna(AuditoriaInterna filtro) {
		
		AuditoriaInternaReportBean reportBean = new AuditoriaInternaReportBean();
		List<AuditoriaInternaReportBean> listaReportBean = new ArrayList<AuditoriaInternaReportBean>();

		AuditoriaInterna auditoriaInterna = loadForEntrada(filtro);
		
		reportBean.setDescUgResponsavel(auditoriaInterna.getUgResponsavel().getDescricaoCompleta());
		reportBean.setData(new SimpleDateFormat("dd/MM/yyyy").format(auditoriaInterna.getDataAuditoria()));
		reportBean.setDescNorma(auditoriaInterna.getNorma().getNome());
		reportBean.setObservacoes(auditoriaInterna.getObservacoes());
		reportBean.setListaEquipeAuditada(auditoriaInterna.getListaEquipeAuditada());
		reportBean.setListaEquipeAuditora(auditoriaInterna.getListaEquipeAuditora());
		reportBean.setListaNaoConformidades(auditoriaInterna.getListaNaoConformidades());
		reportBean.setListaOutrasNaoConformidades(auditoriaInterna.getListaOutrasNaoConformidades());
		
		listaReportBean.add(reportBean);
		
		// Instanciando o Report
		Report report = new Report("../relatorio/auditoriaInterna");
		Report subEquipeAuditada = new Report("../relatorio/auditoriaInterna_subEquipeAuditada");
		Report subEquipeAuditora = new Report("../relatorio/auditoriaInterna_subEquipeAuditora");
		Report subNaoConformidades = new Report("../relatorio/auditoriaInterna_subNaoConformidades");
		Report subOutrasNaoConformidades = new Report("../relatorio/auditoriaInterna_subOutrasNaoConformidades");
		
		
		report.setDataSource(listaReportBean);
		report.addSubReport("SUBREPORTEQUIPEAUDITADA", subEquipeAuditada);
		report.addSubReport("SUBREPORTEQUIPEAUDITORA", subEquipeAuditora);
		report.addSubReport("SUBREPORTNAOCONFORMIDADES", subNaoConformidades);
		report.addSubReport("SUBREPORTOUTRASNAOCONFORMIDADES", subOutrasNaoConformidades);

		return report;
	}
	
	public void enviaEmailSolicitacaoEncerramento(AuditoriaInterna auditoriaInterna) {
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		String from = parametrosSistema.getEmailRemetente();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<Usuario> listaUsuario = findUsuariosQualidadeEnvolvidosAuditoriaInterna(auditoriaInterna);
		StringBuilder sbErro = new StringBuilder("");
		
		if (listaUsuario != null) {
			auditoriaInterna.setPlanoGestao(PlanoGestaoService.getInstance().load(auditoriaInterna.getPlanoGestao()));
			auditoriaInterna.setNorma(normaService.load(auditoriaInterna.getNorma()));
			for (Usuario usuario : listaUsuario) {
				
				String msg = 	
					"Prezado(a) usuário(a) " + usuario.getNome() + ",<br>" +		
					"foi solicitado o encerramento da seguinte auditoria interna:<BR><BR>" +
					"<b>Ano da gestão:</b> "+auditoriaInterna.getPlanoGestao().getAnoExercicio()+"<BR>"+
					"<b>Setor responsável pelo tratamento:</b> "+auditoriaInterna.getUgResponsavel().getSigla() + " - " + auditoriaInterna.getUgResponsavel().getNome()+"<BR>"+
					"<b>Norma:</b> "+auditoriaInterna.getNorma().getNome()+"<BR>"+
					"<b>Data:</b> "+dateFormat.format(auditoriaInterna.getDataAuditoria())+"<BR>"+
					"<b>Observações:</b> "+auditoriaInterna.getObservacoes()+"<BR>";
				
				msg = EnvioEmail.montaCorpoMensagem().replace("--M--", msg);
				
				String[] to = new String[1];
				to[0] = usuario.getEmail();
				
				try {
					EnvioEmail.getInstance().enviaEmail(from, to, "[Geplanes] - Solicitação de encerramento de auditoria interna.", msg);
				}
				catch (EnvioEmailException e) {
					sbErro
						.append("<br>")
						.append(e.getMessage());
				}
			}
		}
		
		if (!sbErro.toString().isEmpty()) {
			throw new GeplanesException(sbErro.toString());
		}
	}
	
	/**
	 * Retorna todos os usuários das áreas de qualidade
	 * que estão envolvidos em uma determinada auditoria interna.
	 * 
	 * @param auditoriaInterna
	 * @return
	 */
	public List<Usuario> findUsuariosQualidadeEnvolvidosAuditoriaInterna(AuditoriaInterna auditoriaInterna) {
		List<Usuario> listaUsuario = new ArrayList<Usuario>();
		
		// UGs da Área de Qualidade
		List<UnidadeGerencial> listaUnidadeGerencialQualidade = unidadeGerencialService.findUGQualidade(auditoriaInterna.getPlanoGestao());
		
		// Os usuários pertencentes aos setores de qualidade serão notificados
		if (listaUnidadeGerencialQualidade != null) {
			for (UnidadeGerencial ugQualidadePresidencia : listaUnidadeGerencialQualidade) {
				listaUsuario.addAll(usuarioService.findByUg(ugQualidadePresidencia));
			}
		}
		
		return listaUsuario;
	}
	
	public Boolean usuarioPodeEncerrarAuditoriaInterna(PlanoGestao planoGestao) {
		return usuarioService.isUsuarioLogadoVinculadoAreaQualidade(planoGestao);		
	}	
	
	private static AuditoriaInternaService instance;
	
	public static AuditoriaInternaService getInstance(){
		if(instance == null){
			instance = Neo.getObject(AuditoriaInternaService.class);
		}
		return instance;
	}	
}
