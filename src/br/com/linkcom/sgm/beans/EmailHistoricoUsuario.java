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
package br.com.linkcom.sgm.beans;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.TipoUsuarioEmailEnum;

@Entity
@SequenceGenerator(name = "sq_emailhistoricousuario", sequenceName = "sq_emailhistoricousuario")
public class EmailHistoricoUsuario {
	
	private Integer id;
	private EmailHistorico emailHistorico;
	private String usuarioEmail;
	private TipoUsuarioEmailEnum tipoUsuarioEmail;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_emailhistoricousuario")	
	public Integer getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	public EmailHistorico getEmailHistorico() {
		return emailHistorico;
	}
	
	@DisplayName("Usuário")
	@MaxLength(100)
	@Required	
	public String getUsuarioEmail() {
		return usuarioEmail;
	}
	
	@DisplayName("Tipo")
	@Required	
	public TipoUsuarioEmailEnum getTipoUsuarioEmail() {
		return tipoUsuarioEmail;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEmailHistorico(EmailHistorico emailHistorico) {
		this.emailHistorico = emailHistorico;
	}

	public void setUsuarioEmail(String usuarioEmail) {
		this.usuarioEmail = usuarioEmail;
	}

	public void setTipoUsuarioEmail(TipoUsuarioEmailEnum tipoUsuarioEmail) {
		this.tipoUsuarioEmail = tipoUsuarioEmail;
	}
}
