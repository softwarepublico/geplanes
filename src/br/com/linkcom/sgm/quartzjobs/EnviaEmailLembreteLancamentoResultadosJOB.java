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
package br.com.linkcom.sgm.quartzjobs;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.LogProcesso;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.service.AcompanhamentoIndicadorService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.LogProcessoService;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioUnidadeGerencialService;
import br.com.linkcom.sgm.util.CalendarComparator;
import br.com.linkcom.sgm.util.EmailUtil;
import br.com.linkcom.sgm.util.Nomes;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;
import br.com.linkcom.sgm.util.email.EnvioEmail;


public class EnviaEmailLembreteLancamentoResultadosJOB extends EnviaEmailLembreteJOB {
	
	public EnviaEmailLembreteLancamentoResultadosJOB() {
		super();
		setAssunto("[Geplanes] - Data Limite para Lançamento de " + Nomes.Valores_Reais);
		setTexto(
			"<table width='100%'>" +
			"	<tr>" +
			"		<td>" +
			"			<img src='cid:img0'>" +
			"		</td> " +
			"	</tr>" +
			"</table>" +
			"<table width='100%' cellspacing='10' style='border-bottom: 5px solid #CCCCCC; border-top: 5px solid #CCCCCC; background-color: #EEEEEE' >" +
			"	<tr>" +					
			"		<td>" +
			"			<span style='font-size: 12px; font-weight: normal; color: #444444'>" +			
			"			Prezado Usuário --U--,<br>" +
			"			Favor efetuar o lançamento dos " + Nomes.valores_reais +" para os seguintes indicadores:<br><br>--P--" +
			"			</span>" +
			"		</td>" +
			"	</tr>" +
			"</table>" +			
			"<table width='100%' >" +
			"	<tr align='right'>" +
			"		<td><img src='cid:img1'></td>" +
			"	</tr>" +
			"</table>"
		);
	}
	
	public synchronized void enviaEmail() {
		
		// Services
		AcompanhamentoIndicadorService acompanhamentoIndicadorService = Neo.getObject(AcompanhamentoIndicadorService.class);
		UsuarioUnidadeGerencialService usuarioUnidadeGerencialService = Neo.getObject(UsuarioUnidadeGerencialService.class);
		PlanoGestaoService planoGestaoService = Neo.getObject(PlanoGestaoService.class);
		UnidadeGerencialService unidadeGerencialService = Neo.getObject(UnidadeGerencialService.class);
		IndicadorService indicadorService = Neo.getObject(IndicadorService.class);
		LogProcessoService logProcessoService = Neo.getObject(LogProcessoService.class);
		
		// Listas
		List<UnidadeGerencial> listaUnidadeGerencial;
		List<Indicador> listaIndicador;
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
		List<UsuarioUnidadeGerencial> listaUsuarioUnidadeGerencial;
		List<AcompanhamentoIndicador> listaAcompanhamentoUsuario;
		Set<Indicador> setIndicador;
		
		Integer trimestreAcompanhamento;
		Boolean isDtLimLancResExp;
		Boolean isDtTravLancResExp;
		
		Map<Usuario, Set<Indicador>> mapaUsuariosEmail = new HashMap<Usuario, Set<Indicador>>();
		Map<Usuario, List<AcompanhamentoIndicador>> mapaUsuariosAcompanhamento = new HashMap<Usuario, List<AcompanhamentoIndicador>>();
		StringBuilder sbIndicador;
		Usuario usuario;
		
		boolean enviado;
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		// Busca o ano de gestão atual
		PlanoGestao planoGestaoAtual = planoGestaoService.obtemPlanoGestaoAtual();
		
		// Lista as Unidades Gerenciais
		listaUnidadeGerencial = unidadeGerencialService.findWithSiglaNomeByPlanoGestao(planoGestaoAtual);
		if (listaUnidadeGerencial != null) {
			for (UnidadeGerencial unidadeGerencial : listaUnidadeGerencial) {
				
				// Lista os indicadores ativos da UG
				listaIndicador = indicadorService.findAtivosByUnidadeGerencial(unidadeGerencial);
				
				if (listaIndicador != null && !listaIndicador.isEmpty()) {
				
					for (Indicador indicador : listaIndicador) {
						listaAcompanhamentoIndicador = acompanhamentoIndicadorService.obtemAcompanhamentos(indicador);
						if (listaAcompanhamentoIndicador != null && !listaAcompanhamentoIndicador.isEmpty()) {
							for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoIndicador) {
								trimestreAcompanhamento = CalculosAuxiliares.getAcompanhamentoTrimestre(indicador.getFrequencia(), acompanhamentoIndicador.getIndice());
								isDtLimLancResExp = planoGestaoService.isDtLimLancResultadosExpirada(planoGestaoAtual, trimestreAcompanhamento);
								isDtTravLancResExp = planoGestaoService.isDtTravLancResultadosExpirada(planoGestaoAtual, trimestreAcompanhamento);
								
								// Verifica se o resultado foi lançado.
								// Caso negativo, verifica se a data limite foi expirada e a data de travamento ainda não.
								if (acompanhamentoIndicador.getValorReal() == null && isDtLimLancResExp && !isDtTravLancResExp) {
									// Verifica se o email ainda não foi enviado.
									// Caso já tenha sido enviado, verifica se o prazo para reenvio foi atingido.
									if (acompanhamentoIndicador.getDtLembLancRes() == null || acompanhamentoIndicadorService.isPrazoReenvioLembLancResAtingido(acompanhamentoIndicador.getDtLembLancRes(), parametrosSistema.getDiasLembreteLancamentoValoresReais())) {
										// Busca os responsáveis pela unidade gerencial do indicador.
										listaUsuarioUnidadeGerencial = usuarioUnidadeGerencialService.findResponsaveisByUnidadeGerencial(unidadeGerencial);
										
										// Para cada responsável
										if (listaUsuarioUnidadeGerencial != null && !listaUsuarioUnidadeGerencial.isEmpty()) {
											for (UsuarioUnidadeGerencial usuarioUnidadeGerencial : listaUsuarioUnidadeGerencial) {
												usuario = usuarioUnidadeGerencial.getUsuario();
												
												// Verifica se o usuário já existe no mapa para envio de email
												setIndicador = mapaUsuariosEmail.get(usuario);
												if (setIndicador == null) {
													setIndicador = new LinkedHashSet<Indicador>();
												}
												indicador.setUnidadeGerencial(unidadeGerencial);
												setIndicador.add(indicador);
												mapaUsuariosEmail.put(usuario, setIndicador);
												
												
												// Verifica se o usuário já existe no mapa para atualização de acompanhamentos
												listaAcompanhamentoUsuario = mapaUsuariosAcompanhamento.get(usuario);
												if (listaAcompanhamentoUsuario == null) {
													listaAcompanhamentoUsuario = new ArrayList<AcompanhamentoIndicador>();
												}
												listaAcompanhamentoUsuario.add(acompanhamentoIndicador);
												mapaUsuariosAcompanhamento.put(usuarioUnidadeGerencial.getUsuario(), listaAcompanhamentoUsuario);
											}
										}
									}
								}
							}
						}
					}
				}							
			}
		}
		
		if (mapaUsuariosEmail != null && !mapaUsuariosEmail.isEmpty()) {
			Iterator<Usuario> itUsuarios = mapaUsuariosEmail.keySet().iterator();
			while (itUsuarios.hasNext()) {
				usuario = itUsuarios.next();
				setIndicador = mapaUsuariosEmail.get(usuario);
				sbIndicador = new StringBuilder();
				
				for (Indicador indicador : setIndicador) {
					sbIndicador.append("Unidade Gerencial: <b>" + indicador.getUnidadeGerencial().getDescricaoCompleta() + "</b><br>Objetivo Estratégico: <b>" + indicador.getObjetivoMapaEstrategico().getDescricao() + "</b><br>Indicador: <b>" + indicador.getNome() + "</b><br><br>");	
				}
				
				enviado = envia(parametrosSistema.getEmailRemetente(), usuario, sbIndicador);
				
				if (enviado) {
					listaAcompanhamentoUsuario = mapaUsuariosAcompanhamento.get(usuario);
					if (listaAcompanhamentoUsuario != null && !listaAcompanhamentoUsuario.isEmpty()) {
						for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoUsuario) {
							acompanhamentoIndicador.setDtLembLancRes(new Date(CalendarComparator.getDataAtualSemHora().getTime().getTime()));
							acompanhamentoIndicadorService.saveOrUpdate(acompanhamentoIndicador);
						}
					}
				}
			}
		}
		
		// Atualiza no banco de dados a data de execução do processo
		LogProcesso logProcesso = logProcessoService.getLogProcessoAtual();
		logProcesso.setDtLembLancResultado(new Timestamp(System.currentTimeMillis()));
		logProcessoService.saveOrUpdate(logProcesso);		
	}
	
	protected boolean envia(String emailFrom, Usuario usuario, StringBuilder sbIndicadores) {
		boolean enviado = false;		
		String texto = getTexto().replace("--U--",usuario.getNome()).replace("--P--",sbIndicadores.toString()); 
		
		List<String> listaImgs = EmailUtil.getEmailImageList(getApplicationPath());

		if (usuario.getEmail() != null && !usuario.getEmail().trim().equals("")) {			
			enviado = EnvioEmail.getInstance().enviaEmail(emailFrom, usuario.getEmail(),getAssunto(),texto,listaImgs);
		}
		return enviado;
	}

}