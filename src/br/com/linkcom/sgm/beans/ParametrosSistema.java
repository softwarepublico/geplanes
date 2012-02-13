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
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@SequenceGenerator(name = "sq_parametrossistema", sequenceName = "sq_parametrossistema")
public class ParametrosSistema {

	private Integer id;

	private Integer diasLembreteCriacaoMetasIndicadores;
	private Integer diasLembreteLancamentoValoresReais;
	private Boolean notificarEnvolvidosAnomalia;

	private Integer diasTravTratAnomalia;
	private Integer diasLembTratAnomalia;
	private Integer diasEncerramentoAnomalia;

	private Boolean emailNeedAuth;
	private String emailServidorSMTP;
	private Integer emailPortaSMTP;
	private Boolean emailServidorUsaSSL;
	private String emailUsuarioDominio;
	private String emailSenha;
	private String emailRemetente;
	private Arquivo imgEmpresa;
	private Arquivo imgEmpresaRelatorio;

	// =========================Get e Set==================================//

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_parametrossistema")
	public Integer getId() {
		return id;
	}

	@Required
	@DisplayName("Dias de antecedência para envio do lembrete de criação de indicadores")
	public Integer getDiasLembreteCriacaoMetasIndicadores() {
		return diasLembreteCriacaoMetasIndicadores;
	}

	@Required
	@DisplayName("Dias para reenvio do lembrete de lançamento de "
			+ Nomes.valores_reais)
	public Integer getDiasLembreteLancamentoValoresReais() {
		return diasLembreteLancamentoValoresReais;
	}

	@Required
	@DisplayName("Notificar os envolvidos na anomalia")
	public Boolean getNotificarEnvolvidosAnomalia() {
		return notificarEnvolvidosAnomalia;
	}

	@Required
	@DisplayName("Servidor SMTP")
	public String getEmailServidorSMTP() {
		return emailServidorSMTP;
	}
	
	@Required
	@DisplayName("Porta")
	public Integer getEmailPortaSMTP() {
		return emailPortaSMTP;
	}

	@Required
	@DisplayName("Requer autenticação?")
	public Boolean getEmailNeedAuth() {
		return emailNeedAuth;
	}

	@DisplayName("Usuário para autenticação")
	public String getEmailUsuarioDominio() {
		return emailUsuarioDominio;
	}

	@DisplayName("Senha do usuário para autenticação")
	public String getEmailSenha() {
		return emailSenha;
	}

	@Required
	@DisplayName("E-mail do remetente")
	public String getEmailRemetente() {
		return emailRemetente;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@DisplayName("Logomarca da empresa utilizada nas páginas iniciais")
	public Arquivo getImgEmpresa() {
		return imgEmpresa;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@DisplayName("Logomarca da empresa utilizada nos relatórios")
	public Arquivo getImgEmpresaRelatorio() {
		return imgEmpresaRelatorio;
	}
	
	@DisplayName("Utiliza SSL?")
	public Boolean getEmailServidorUsaSSL() {
		return emailServidorUsaSSL;
	}

	public void setEmailNeedAuth(Boolean emailNeedAuth) {
		this.emailNeedAuth = emailNeedAuth;
	}

	public void setEmailSenha(String emailSenha) {
		this.emailSenha = emailSenha;
	}

	public void setEmailServidorSMTP(String emailServidorSMTP) {
		this.emailServidorSMTP = emailServidorSMTP;
	}
	
	public void setEmailPortaSMTP(Integer emailPortaSMTP) {
		this.emailPortaSMTP = emailPortaSMTP;
	}

	public void setEmailUsuarioDominio(String emailUsuarioDominio) {
		this.emailUsuarioDominio = emailUsuarioDominio;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}

	public void setDiasLembreteCriacaoMetasIndicadores(
			Integer diasLembreteCriacaoMetasIndicadores) {
		this.diasLembreteCriacaoMetasIndicadores = diasLembreteCriacaoMetasIndicadores;
	}

	public void setDiasLembreteLancamentoValoresReais(
			Integer diasLembreteLancamentoValoresReais) {
		this.diasLembreteLancamentoValoresReais = diasLembreteLancamentoValoresReais;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNotificarEnvolvidosAnomalia(
			Boolean notificarEnvolvidosAnomalia) {
		this.notificarEnvolvidosAnomalia = notificarEnvolvidosAnomalia;
	}
	
	public void setEmailServidorUsaSSL(Boolean emailServidorUsaSSL) {
		this.emailServidorUsaSSL = emailServidorUsaSSL;
	}

	// /////////// TRANSIENTE //////////////////

	private String emailTeste;

	@Transient
	@DisplayName("E-mail destino para  o teste")
	public String getEmailTeste() {
		return emailTeste;
	}

	public void setEmailTeste(String emailTeste) {
		this.emailTeste = emailTeste;
	}

	@DisplayName("Dias para travamento do tratamento da anomalia")
	@Required
	public Integer getDiasTravTratAnomalia() {
		return diasTravTratAnomalia;
	}

	@DisplayName("Dias para envio do lembrete do tratamento da anomalia")
	@Required
	public Integer getDiasLembTratAnomalia() {
		return diasLembTratAnomalia;
	}

	@DisplayName("Dias para encerramento da anomalia")
	@Required
	public Integer getDiasEncerramentoAnomalia() {
		return diasEncerramentoAnomalia;
	}

	public void setDiasTravTratAnomalia(Integer diasTravTratAnomalia) {
		this.diasTravTratAnomalia = diasTravTratAnomalia;
	}

	public void setDiasLembTratAnomalia(Integer diasLembTratAnomalia) {
		this.diasLembTratAnomalia = diasLembTratAnomalia;
	}

	public void setDiasEncerramentoAnomalia(Integer diasEncerramentoAnomalia) {
		this.diasEncerramentoAnomalia = diasEncerramentoAnomalia;
	}

	public void setImgEmpresa(Arquivo imgEmpresa) {
		this.imgEmpresa = imgEmpresa;
	}

	public void setImgEmpresaRelatorio(Arquivo imgEmpresaRelatorio) {
		this.imgEmpresaRelatorio = imgEmpresaRelatorio;
	}

}
