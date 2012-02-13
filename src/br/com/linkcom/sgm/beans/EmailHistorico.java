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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.TipoUsuarioEmailEnum;

@Entity
@SequenceGenerator(name = "sq_emailhistorico", sequenceName = "sq_emailhistorico")
public class EmailHistorico {
	
	private Integer id;
	private String remetente;
	private String assunto;
	private String mensagem;
	private Timestamp data;
	private List<EmailHistoricoUsuario> listaEmailHistoricoUsuario;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_emailhistorico")	
	public Integer getId() {
		return id;
	}
	
	@DisplayName("Remetente")
	@MaxLength(60)
	@Required
	public String getRemetente() {
		return remetente;
	}

	@DisplayName("Assunto")
	@MaxLength(100)
	@Required	
	public String getAssunto() {
		return assunto;
	}
	
	@DisplayName("Mensagem")
	@MaxLength(3000)
	public String getMensagem() {
		return mensagem;
	}
	
	@DisplayName("Data")
	@Required	
	public Timestamp getData() {
		return data;
	}
	
	@OneToMany(mappedBy="emailHistorico")
	public List<EmailHistoricoUsuario> getListaEmailHistoricoUsuario() {
		return listaEmailHistoricoUsuario;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public void setData(Timestamp data) {
		this.data = data;
	}
	
	public void setListaEmailHistoricoUsuario(List<EmailHistoricoUsuario> listaEmailHistoricoUsuario) {
		this.listaEmailHistoricoUsuario = listaEmailHistoricoUsuario;
	}
	
	@Transient
	public String getStrData() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return simpleDateFormat.format(data);
	}
	
	/* ************ */
	/* TRANSIENTES */
	/* *********** */

	@Transient
	@DisplayName("Destinatário")
	public String getDestinatario() {
		StringBuilder sbUsuario = new StringBuilder();
		if (listaEmailHistoricoUsuario != null) {
			for (EmailHistoricoUsuario emailHistoricoUsuario : listaEmailHistoricoUsuario) {
				if (emailHistoricoUsuario.getTipoUsuarioEmail().equals(TipoUsuarioEmailEnum.DESTINATARIO)) {
					sbUsuario.append(emailHistoricoUsuario.getUsuarioEmail()).append(", ");
				}
			}
			if (sbUsuario.length() > 2) {
				sbUsuario.delete(sbUsuario.length() - 2, sbUsuario.length() - 1);
			}
		}
		return sbUsuario.toString();
	}

	@Transient
	@DisplayName("Destinatário")
	public String getDestinatarioResumido() {
		String destinatarios = getDestinatario();
		if (destinatarios.length() > 200) {
			return destinatarios.substring(0, 200) + " ...";
		}
		return destinatarios;
	}

	@Transient
	@DisplayName("Cópia")
	public String getCopia() {
		StringBuilder sbUsuario = new StringBuilder();
		if (listaEmailHistoricoUsuario != null) {
			for (EmailHistoricoUsuario emailHistoricoUsuario : listaEmailHistoricoUsuario) {
				if (emailHistoricoUsuario.getTipoUsuarioEmail().equals(TipoUsuarioEmailEnum.COPIA)) {
					sbUsuario.append(emailHistoricoUsuario.getUsuarioEmail()).append(", ");
				}
			}
			if (sbUsuario.length() > 2) {
				sbUsuario.delete(sbUsuario.length() - 2, sbUsuario.length() - 1);
			}
		}
		return sbUsuario.toString();
	}

	@Transient
	@DisplayName("Cópia Oculta")
	public String getCopiaoculta() {
		StringBuilder sbUsuario = new StringBuilder();
		if (listaEmailHistoricoUsuario != null) {
			for (EmailHistoricoUsuario emailHistoricoUsuario : listaEmailHistoricoUsuario) {
				if (emailHistoricoUsuario.getTipoUsuarioEmail().equals(TipoUsuarioEmailEnum.COPIA_OCULTA)) {
					sbUsuario.append(emailHistoricoUsuario.getUsuarioEmail()).append(", ");
				}
			}
			if (sbUsuario.length() > 2) {
				sbUsuario.delete(sbUsuario.length() - 2, sbUsuario.length() - 1);
			}
		}
		return sbUsuario.toString();
	}


}
