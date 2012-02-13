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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.util.EmailUtil;
import br.com.linkcom.sgm.util.GeplanesUtils;

public class EnviaEmailTeste {
	
	public EnviaEmailTeste() {
	}
	
	public String enviaEmail(HttpServletRequest request, ParametrosSistema bean) {
		String assunto = "[Geplanes] E-mail de teste";
		String texto = 
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
			"			E-mail de teste.<br>" +
			"			Suas configurações do e-mail estão corretas!" +
			"			</span>" +
			"		</td>" +
			"	</tr>" +
			"</table>" +			
			"<table width='100%' >" +
			"	<tr align='right'>" +
			"		<td><img src='cid:img1'></td>" +
			"	</tr>" +
			"</table>";
		
		List<String> listaImgs = EmailUtil.getEmailImageList(GeplanesUtils.getApplicationPath(request));

		try {
			boolean ok = EnvioEmail.getInstance().enviaEmail(bean.getEmailServidorSMTP(), bean.getEmailPortaSMTP(), bean.getEmailServidorUsaSSL(), bean.getEmailNeedAuth(), bean.getEmailUsuarioDominio(), bean.getEmailSenha(), bean.getEmailRemetente(), bean.getEmailTeste(), assunto, texto, listaImgs);
			return ok ? "E-MAIL ENVIADO COM SUCESSO PARA " + bean.getEmailTeste() + "." : "ERRO AO ENVIAR E-MAIL.";
		}
		catch(Exception ex) {
			return "ERRO AO ENVIAR E-MAIL DE TESTE: " + ex.getMessage();
		}
	}

}