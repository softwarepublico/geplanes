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
package br.com.linkcom.sgm.util.email;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.Arquivo;
import br.com.linkcom.sgm.beans.EmailHistorico;
import br.com.linkcom.sgm.beans.EmailHistoricoUsuario;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.enumeration.TipoUsuarioEmailEnum;
import br.com.linkcom.sgm.exception.EnvioEmailException;
import br.com.linkcom.sgm.service.EmailHistoricoService;
import br.com.linkcom.sgm.service.ParametrosSistemaService;

public class EnvioEmail{
	
	private String servidor_smtp;
	private int porta_smtp;
	private boolean needAuth;
	private boolean useSSL;
	private UserEmail user;
	
	private static EnvioEmail instancia;
	
	private EnvioEmail() {
		
	}
	
	public static EnvioEmail getInstance(){
		if (instancia == null) {
			instancia = new EnvioEmail();
		}
		instancia.init();
		return instancia;
	}
	
	public void init(){
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		this.servidor_smtp = parametrosSistema.getEmailServidorSMTP();
		this.porta_smtp = parametrosSistema.getEmailPortaSMTP();
		this.needAuth = parametrosSistema.getEmailNeedAuth();
		this.useSSL = parametrosSistema.getEmailServidorUsaSSL() != null && parametrosSistema.getEmailServidorUsaSSL();
		this.user = new UserEmail(parametrosSistema.getEmailUsuarioDominio(), parametrosSistema.getEmailSenha());
	}
	
	public static String montaCorpoMensagem() {
		return "<table width='100%'>" +
			"	<tr>" +
			"		<td>" +
			"			<img src='cid:img0'>" +
			"		</td>" +
			"	</tr>" +
			"</table>" +
			"<table width='100%' cellspacing='10' style='border-bottom: 5px solid #CCCCCC; border-top: 5px solid #CCCCCC; background-color: #EEEEEE' >" +
			"	<tr>" +				
			"		<td>" +
			"			<span style='font-size: 12px; font-weight: normal; color: #444444'>" +
			"			--M--" +	
			"			</span>" +
			"		</td>" +
			"	</tr>" +
			"</table>" +
			"<table width='100%' >" +
			"	<tr align='right'>" +
			"		<td><img src='cid:img1'></td>" +
			"	</tr>" +
			"</table>";		
	}
	
	public boolean enviaEmail(String emailFrom, String[] emailTo, String assunto, String texto){
		return enviaEmail(emailFrom, emailTo, null, null, assunto, texto, null, null);
	}
	
	public boolean enviaEmail(String emailFrom, String emailTo, String assunto, String texto, List<String> listaImgs){
		return enviaEmail(emailFrom, emailTo, assunto, texto, listaImgs, null);
	}
	
	public boolean enviaEmail(String servidorSmtp, int portaSmtp, boolean useSSL, boolean needAuth, String user, String password, String emailFrom, String emailTo, String assunto, String texto, List<String> listaImgs) {
		this.servidor_smtp = servidorSmtp;
		this.porta_smtp = portaSmtp;
		this.needAuth = needAuth;
		this.useSSL = useSSL;
		this.user = new UserEmail(user, password);
		
		return enviaEmail(emailFrom, emailTo, assunto, texto, listaImgs);
	}	
	
	public boolean enviaEmail(String emailFrom, String emailTo, String assunto, String texto, List<String> listaImgs, List<Arquivo> listaArquivo){
		return enviaEmail(emailFrom, new String[]{emailTo}, null, null, assunto, texto, listaImgs, listaArquivo);
	}

	public boolean enviaEmail(String emailFrom, String[] emailTo, String[] emailCc, String[] emailCco, String assunto, String texto, List<String> listaImgs, List<Arquivo> listaArquivo){
		
		Session session;
		Properties mailProps = new Properties();
		
		mailProps.put("mail.smtp.host", this.servidor_smtp);
		mailProps.put("mail.smtp.port", String.valueOf(this.porta_smtp));
		mailProps.put("mail.user", this.user.getSmtp_username());
		mailProps.put("mail.pwd", this.user.getSmtp_senha());
		
		if (useSSL) {
			mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		
		if (!needAuth) { 
			// [#] No authorization needed for this mail server
            mailProps.put("mail.smtp.auth", "false");
            session = Session.getInstance(mailProps);
        } else {
            // [#] Authorization needed for this mail server
            mailProps.put("mail.smtp.auth", "true");                
            session = Session.getInstance(mailProps, this.user);
        }
		session.setDebug(false);
		
		MimeMultipart mimeMultipart = new MimeMultipart("related");
		BodyPart messageBodyPart = new MimeBodyPart();
		BodyPart imagePart;
		
		
		Message msg = new MimeMessage(session);
		boolean enviado = false;
		try {
			//Parte 1 - Corpo do Email
			messageBodyPart.setContent(texto,"text/html");
			mimeMultipart.addBodyPart(messageBodyPart);
			
			//Parte 2 - Imagens Anexas
			DataSource dataSource;
			int i = 0;
			if (listaImgs != null) {
				for (String img : listaImgs) {
					imagePart = new MimeBodyPart();
					dataSource = new URLDataSource(new URL(img));
					imagePart.setFileName("img" + i + ".gif");				
					imagePart.setDataHandler(new DataHandler(dataSource));
					imagePart.setHeader("Content-ID","<img" + i+">");
					mimeMultipart.addBodyPart(imagePart);
					i++;
				}
			}
			
			//Parte 3 - Arquivos anexos
			if(listaArquivo != null){
				 BodyPart anexoPart;
				 InternetHeaders headers;
				for (Arquivo arquivo : listaArquivo) {
					headers = new InternetHeaders();
					headers.addHeader("Content-Type", arquivo.getContenttype());
					anexoPart = new MimeBodyPart(headers, Base64.encodeBase64(arquivo.getContent()));
				    anexoPart.setFileName(arquivo.getName());
				    anexoPart.setDisposition(Part.ATTACHMENT);
				    mimeMultipart.addBodyPart(anexoPart);
				}
			}
			
			
			
			
			
			msg.setFrom(new InternetAddress(emailFrom));
			
			if(emailTo != null && emailTo.length > 0){
				InternetAddress[] listTo = new InternetAddress[emailTo.length];
				for (int j = 0; j < emailTo.length; j++) {
					listTo[j] = new InternetAddress(emailTo[j]);
				}
				msg.setRecipients(Message.RecipientType.TO, listTo);
			} else {
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailFrom));
			}
			
			if(emailCc != null && emailCc.length > 0){
				InternetAddress[] listCc = new InternetAddress[emailCc.length];
				for (int j = 0; j < emailCc.length; j++) {
					listCc[j] = new InternetAddress(emailCc[j]);
				}
				msg.setRecipients(Message.RecipientType.CC, listCc);
			}
			
			if(emailCco != null && emailCco.length > 0){
				InternetAddress[] listCco = new InternetAddress[emailCco.length];
				for (int j = 0; j < emailCco.length; j++) {
					listCco[j] = new InternetAddress(emailCco[j]);
				}
				msg.setRecipients(Message.RecipientType.BCC, listCco);
			}
			
			msg.setSubject(assunto);			
			msg.setContent(mimeMultipart);
			//msg.setText(texto);
			
			//ENVIO
			Transport.send(msg);
			enviado = true;
			
			System.out.println("E-MAIL ENVIADO.");
			
			EmailHistoricoService emailHistoricoService = Neo.getObject(EmailHistoricoService.class);
			EmailHistorico emailHistorico = new EmailHistorico();
			emailHistorico.setRemetente(emailFrom);
			
			List<EmailHistoricoUsuario> listaEmailHistoricoUsuario = new ArrayList<EmailHistoricoUsuario>();
			EmailHistoricoUsuario emailHistoricoUsuario;
			
			if (emailTo != null) {
				List<String> listaDestinatario = Arrays.asList(emailTo);
				if (listaDestinatario != null) {
					for (String emailStr : listaDestinatario) {
						emailHistoricoUsuario = new EmailHistoricoUsuario();
						emailHistoricoUsuario.setUsuarioEmail(emailStr);
						emailHistoricoUsuario.setTipoUsuarioEmail(TipoUsuarioEmailEnum.DESTINATARIO);
						listaEmailHistoricoUsuario.add(emailHistoricoUsuario);
					}
				}
			}
			
			if (emailCc != null) {
				List<String> listaCopia = Arrays.asList(emailCc);
				if (listaCopia != null) {
					for (String emailStr : listaCopia) {
						emailHistoricoUsuario = new EmailHistoricoUsuario();
						emailHistoricoUsuario.setUsuarioEmail(emailStr);
						emailHistoricoUsuario.setTipoUsuarioEmail(TipoUsuarioEmailEnum.COPIA);
						listaEmailHistoricoUsuario.add(emailHistoricoUsuario);
					}
				}
			}
			
			if (emailCco != null) {
				List<String> listaCopiaOculta = Arrays.asList(emailCco);
				if (listaCopiaOculta != null) {
					for (String emailStr : listaCopiaOculta) {
						emailHistoricoUsuario = new EmailHistoricoUsuario();
						emailHistoricoUsuario.setUsuarioEmail(emailStr);
						emailHistoricoUsuario.setTipoUsuarioEmail(TipoUsuarioEmailEnum.COPIA_OCULTA);
						listaEmailHistoricoUsuario.add(emailHistoricoUsuario);
					}
				}			
			}
			
			emailHistorico.setListaEmailHistoricoUsuario(listaEmailHistoricoUsuario);

			emailHistorico.setAssunto(assunto);
			emailHistorico.setMensagem(texto);
			emailHistorico.setData(new Timestamp(System.currentTimeMillis()));
			emailHistoricoService.saveOrUpdate(emailHistorico);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EnvioEmailException("Erro ao enviar email para " + ArrayUtils.toString(emailTo));
		}
		return enviado;
	}

}