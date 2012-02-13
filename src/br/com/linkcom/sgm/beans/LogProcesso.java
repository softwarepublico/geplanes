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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;

@Entity
@SequenceGenerator(name = "sq_logprocesso", sequenceName = "sq_logprocesso")
public class LogProcesso {
	
	private Integer id;
	private Timestamp dtLembCriacaoIndicador;	
	private Timestamp dtLembLancResultado;
	private Timestamp dtAtualizaStatusAnomalia;
	private Timestamp dtAtualizaLancamentoResultados;
	private Timestamp dtLembTratAnomalia;
	
	/* Transientes */
	protected String item;
	protected Timestamp dtHrAtualizacao;
	protected String acao;
	
	public LogProcesso() {
		
	}
	
	public LogProcesso(Timestamp dtHrAtualizacao, String item, String acao) {
		this.item = item;
		this.dtHrAtualizacao = dtHrAtualizacao;
		this.acao = acao;
	}	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_logprocesso")	
	public Integer getId() {
		return id;
	}
	
	@DisplayName("Lembrete de criação de indicadores")
	public Timestamp getDtLembCriacaoIndicador() {
		return dtLembCriacaoIndicador;
	}

	@DisplayName("Lembrete de lançamento de resultados")
	public Timestamp getDtLembLancResultado() {
		return dtLembLancResultado;
	}

	@DisplayName("Atualização de status de anomalias")
	public Timestamp getDtAtualizaStatusAnomalia() {
		return dtAtualizaStatusAnomalia;
	}
	
	@DisplayName("Atualização de lançamento de resultados")
	public Timestamp getDtAtualizaLancamentoResultados() {
		return dtAtualizaLancamentoResultados;
	}

	@DisplayName("Lembrete de tratamento de anomalias")
	public Timestamp getDtLembTratAnomalia() {
		return dtLembTratAnomalia;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDtLembCriacaoIndicador(Timestamp dtLembCriacaoIndicador) {
		this.dtLembCriacaoIndicador = dtLembCriacaoIndicador;
	}
	
	public void setDtLembLancResultado(Timestamp dtLembLancResultado) {
		this.dtLembLancResultado = dtLembLancResultado;
	}

	public void setDtAtualizaStatusAnomalia(Timestamp dtAtualizaStatusAnomalia) {
		this.dtAtualizaStatusAnomalia = dtAtualizaStatusAnomalia;
	}
	
	public void setDtAtualizaLancamentoResultados(Timestamp dtAtualizaLancamentoResultados) {
		this.dtAtualizaLancamentoResultados = dtAtualizaLancamentoResultados;
	}

	public void setDtLembTratAnomalia(Timestamp dtLembTratAnomalia) {
		this.dtLembTratAnomalia = dtLembTratAnomalia;
	}

	@Transient
	public String getItem() {
		return item;
	}

	@Transient
	public Timestamp getDtHrAtualizacao() {
		return dtHrAtualizacao;
	}

	@Transient
	public String getAcao() {
		return acao;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setDtHrAtualizacao(Timestamp dtHrAtualizacao) {
		this.dtHrAtualizacao = dtHrAtualizacao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	@Transient
	public String getStrDtHrAtualizacao() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Timestamp dtHrAtualizacao = getDtHrAtualizacao();
		return dtHrAtualizacao != null ? simpleDateFormat.format(dtHrAtualizacao) : "-";
	}	
	
}
