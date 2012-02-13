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
package br.com.linkcom.sgm.filtro;

import java.sql.Date;
import java.util.Calendar;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.validation.annotation.MaxLength;

public class EmailHistoricoFiltro extends FiltroListagem{

	private String remetente;
	private String assunto;
	private Date dtInicio;
	private Date dtFim;
	
	public EmailHistoricoFiltro() {
		Calendar cal = Calendar.getInstance();
		dtFim = new Date(cal.getTimeInMillis());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		dtInicio = new Date(cal.getTimeInMillis());
	}
	
	@DisplayName("Remetente")
	@MaxLength(60)
	public String getRemetente() {
		return remetente;
	}
	
	@DisplayName("Assunto")
	@MaxLength(100)	
	public String getAssunto() {
		return assunto;
	}
	
	@DisplayName("Data inicial")
	public Date getDtInicio() {
		return dtInicio;
	}
	
	@DisplayName("Data final")
	public Date getDtFim() {
		return dtFim;
	}
	
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}
	
	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}
}
