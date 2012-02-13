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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.LogProcesso;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.service.AnomaliaService;
import br.com.linkcom.sgm.service.LogProcessoService;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UsuarioUnidadeGerencialService;
import br.com.linkcom.sgm.util.EmailUtil;
import br.com.linkcom.sgm.util.email.EnvioEmail;

public class EnviaEmailLembreteTratamentoAnomaliaJOB extends EnviaEmailLembreteJOB {
	
	public EnviaEmailLembreteTratamentoAnomaliaJOB() {
		super();
		setAssunto("[Geplanes] - Data Limite para Tratamento de Anomalia");
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
				"			Até a presente data, não foi cadastrado no sistema o tratamento da(s) seguinte(s) anomalia(s):<br><br>--P--" +
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
		AnomaliaService anomaliaService = Neo.getObject(AnomaliaService.class);
		UsuarioUnidadeGerencialService usuarioUnidadeGerencialService = Neo.getObject(UsuarioUnidadeGerencialService.class);
		PlanoGestaoService planoGestaoService = Neo.getObject(PlanoGestaoService.class);
		LogProcessoService logProcessoService = Neo.getObject(LogProcessoService.class);
		
		// Listas
		List<Anomalia> listaAnomalia;
		List<UsuarioUnidadeGerencial> listaUsuarioUnidadeGerencial;
		Set<Anomalia> setAnomalia;
		Map<Usuario, Set<Anomalia>> mapaUsuariosEmail = null;
		Boolean anomaliaTratada;
		StringBuilder sbAnomalia;
		Usuario usuario;
		boolean enviado;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		int diasLembrete   = parametrosSistema.getDiasLembTratAnomalia();
		int diasTravamento = parametrosSistema.getDiasTravTratAnomalia();
		
		// Busca o ano de gestão atual
		PlanoGestao planoGestaoAtual = planoGestaoService.obtemPlanoGestaoAtual();
		
		// Lista as Anomalias não encerradas do ano de gestão atual
		listaAnomalia = anomaliaService.findAnomaliasNaoEncerradas(planoGestaoAtual);
		
		List<UsuarioUnidadeGerencial> listaUsuarioUnidadeGerencialAreaQualidade = usuarioUnidadeGerencialService.findUsuariosQualidadeEnvolvidoAnomalia(planoGestaoAtual);
		
		if (listaAnomalia != null) {
			for (Anomalia anomalia : listaAnomalia) {
				
				mapaUsuariosEmail = new HashMap<Usuario, Set<Anomalia>>();
				
				// Verifica se a anomalia foi tratada
				anomaliaTratada = anomaliaService.isAnomaliaTratada(anomalia);
				
				if (!anomaliaTratada) {
					// Caso não tenha sido tratada, verifica se o prazo para disparo do email de lembrete foi atingido e a anomalia já não esteja travada
					if (anomaliaService.isDataLembreteTratamentoAnomaliaAtingida(anomalia, diasLembrete) && !anomaliaService.isDataTravamentoAnomaliaExpirada(anomalia, diasTravamento)) {
						// Caso afirmativo, verifica se o email ainda não foi enviado
						if (!Boolean.TRUE.equals(anomalia.getLembreteEnviado())) {
							// Busca os usuários que serão notificados
							
							// Primeiro, os responsáveis pela UG de tratamento da anomalia
							listaUsuarioUnidadeGerencial = usuarioUnidadeGerencialService.findResponsaveisByUnidadeGerencial(anomalia.getUgResponsavel());
							
							// Depois, os representantes da área de qualidade envolvida
							listaUsuarioUnidadeGerencial.addAll(listaUsuarioUnidadeGerencialAreaQualidade);
							
							// Para cada responsável
							if (listaUsuarioUnidadeGerencial != null && !listaUsuarioUnidadeGerencial.isEmpty()) {
								for (UsuarioUnidadeGerencial usuarioUnidadeGerencial : listaUsuarioUnidadeGerencial) {
									usuario = usuarioUnidadeGerencial.getUsuario();
									
									// Verifica se o usuário já existe no mapa para envio de email
									setAnomalia = mapaUsuariosEmail.get(usuario);
									if (setAnomalia == null) {
										setAnomalia = new LinkedHashSet<Anomalia>();
									}
									setAnomalia.add(anomalia);
									mapaUsuariosEmail.put(usuario, setAnomalia);
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
				setAnomalia = mapaUsuariosEmail.get(usuario);
				sbAnomalia = new StringBuilder();
				
				for (Anomalia anomalia : setAnomalia) {
					sbAnomalia.append("<b>Setor de Registro: </b>" + anomalia.getUgRegistro().getDescricao() + "<br><b>Setor Responsável pelo Tratamento: </b>" + anomalia.getUgResponsavel().getDescricao() + "<br><b>Descrição da Anomalia: </b>" + anomalia.getDescricao() + "<br><b>Data Limite para Tratamento: <font color='red'>" + simpleDateFormat.format(anomaliaService.getDataLimiteParaTratamento(anomalia, diasTravamento)) + "</font></b><br><br>");	
				}
				
				enviado = envia(parametrosSistema.getEmailRemetente(), usuario, sbAnomalia);
				
				if (enviado) {
					for (Anomalia anomalia : setAnomalia) {
						anomaliaService.atualizaEnvioLembreteTratamentoAnomalia(anomalia);
					}
				}
			}
		}
		
		// Atualiza no banco de dados a data de execução do processo
		LogProcesso logProcesso = logProcessoService.getLogProcessoAtual();
		logProcesso.setDtLembTratAnomalia(new Timestamp(System.currentTimeMillis()));
		logProcessoService.saveOrUpdate(logProcesso);		
	}
	
	protected boolean envia(String emailFrom, Usuario usuario, StringBuilder sbAnomalias) {
		boolean enviado = false;		
		String texto = getTexto().replace("--U--",usuario.getNome()).replace("--P--",sbAnomalias.toString()); 
		
		List<String> listaImgs = EmailUtil.getEmailImageList(getApplicationPath());

		if (usuario.getEmail() != null && !usuario.getEmail().trim().equals("")) {			
			enviado = EnvioEmail.getInstance().enviaEmail(emailFrom, usuario.getEmail(),getAssunto(),texto,listaImgs);
		}
		return enviado;
	}
	
}