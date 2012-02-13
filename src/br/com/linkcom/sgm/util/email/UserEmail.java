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

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class UserEmail extends Authenticator{
	private String smtp_username;
	private String smtp_senha;
	
	public UserEmail(String smtp_username, String smtp_senha) {
		this.smtp_username = smtp_username;
		this.smtp_senha = smtp_senha;
	}
	
	public String getSmtp_senha() {
		return smtp_senha;
	}
	public String getSmtp_username() {
		return smtp_username;
	}
	
	protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(smtp_username,smtp_senha);
    }
}