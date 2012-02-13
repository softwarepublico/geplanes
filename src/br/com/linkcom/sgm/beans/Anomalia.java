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


import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.enumeration.ClassificacaoAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.LocalAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.OrigemAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.StatusTratamentoAnomaliaEnum;
import br.com.linkcom.sgm.beans.enumeration.TipoAnomaliaEnum;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@SequenceGenerator(name = "sq_anomalia", sequenceName = "sq_anomalia")
public class Anomalia {
	
	private Integer id;
	
	private String descricao;
	private String contraMedidasImediatas;
	private String conclusao;
	private Date dataAbertura;
	private Date dataEncerramento;
	private Integer sequencial;
	private String observacoes;
	private ClassificacaoAnomaliaEnum classificacao;
	private String responsavel;
	private String verificacao;
	private String padronizacao;
	private String analiseCausas;
	private Date dataDestravamento;
	private Date dataSolicitacaoEncerramento;
	private Boolean lembreteEnviado;
	private TipoAnomaliaEnum tipo;
	private OrigemAnomaliaEnum origem;
	private String tipoAuditoria;
	private LocalAnomaliaEnum local;
	private UnidadeGerencial ugRegistro;
	private UnidadeGerencial ugResponsavel;
	private Ocorrencia ocorrencia;
	private ItemAuditoriaInterna itemAuditoriaInterna;
	
	private StatusAnomaliaEnum status;
	private StatusTratamentoAnomaliaEnum statusTratamento;
	
	private Anomalia subordinacao;
	
	private Set<CausaEfeito> causasEfeito = new ListSet<CausaEfeito>(CausaEfeito.class);
	private Set<PlanoAcao> planosAcao = new ListSet<PlanoAcao>(PlanoAcao.class);
	private Set<AnexoAnomalia> anexosAnomalia = new ListSet<AnexoAnomalia>(AnexoAnomalia.class);
	
	//=========================Construtor==================================//
	
	public Anomalia() {
	}
	
	public Anomalia(int id) {
		this.id = id;
	}
	
	//=========================Get e Set==================================//
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_anomalia")
	public Integer getId() {
		return id;
	}
	@DisplayName("Conclusão da Área de Qualidade")
	public String getConclusao() {
		return conclusao;
	}
	@MaxLength(1500)
	@DisplayName("Correção")
	public String getContraMedidasImediatas() {
		return contraMedidasImediatas;
	}
	@Required
	@DisplayName("Data de abertura")
	public Date getDataAbertura() {
		return dataAbertura;
	}
	@DisplayName("Data de encerramento")
	public Date getDataEncerramento() {
		return dataEncerramento;
	}
	@DisplayName("Descrição")
	@MaxLength(1800)
	@DescriptionProperty
	@Required
	public String getDescricao() {
		return descricao;
	}
	@DisplayName("Causa")
	public LocalAnomaliaEnum getLocal() {
		return local;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	@DisplayName("Setor de registro")
	public UnidadeGerencial getUgRegistro() {
		return ugRegistro;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@Required
	@DisplayName("Setor responsável pelo tratamento")
	public UnidadeGerencial getUgResponsavel() {
		return ugResponsavel;
	}
	@DisplayName("Número")
	@Required
	public Integer getSequencial() {
		return sequencial;
	}
	@DisplayName("Observações")
	@MaxLength(1500)	
	public String getObservacoes() {
		return observacoes;
	}
	@DisplayName("Classificação")
	public ClassificacaoAnomaliaEnum getClassificacao() {
		return classificacao;
	}
	@DisplayName("Responsável")
	@MaxLength(100)	
	public String getResponsavel() {
		return responsavel;
	}
	@OneToMany(mappedBy="anomalia")
	@DisplayName("Planos de Ação")	
	public Set<PlanoAcao> getPlanosAcao() {
		return planosAcao;
	}
	@DisplayName("Padronização")
	@MaxLength(1500)
	public String getPadronizacao() {
		return padronizacao;
	}
	@OneToMany(mappedBy = "anomalia")
	@DisplayName("Causa")
	public Set<CausaEfeito> getCausasEfeito() {
		return causasEfeito;
	}
	@DisplayName("Verificação")
	@MaxLength(1500)
	public String getVerificacao() {
		return verificacao;
	}
	@DisplayName("Nº diário de bordo")
	@ManyToOne(fetch= FetchType.LAZY)
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}
	
	@DisplayName("Auditoria interna")
	@ManyToOne(fetch= FetchType.LAZY)	
	public ItemAuditoriaInterna getItemAuditoriaInterna() {
		return itemAuditoriaInterna;
	}	
	
	@DisplayName("Tipo de auditoria")
	@MaxLength(255)
	public String getTipoAuditoria() {
		return tipoAuditoria;
	}
	public void setTipoAuditoria(String tipoAuditoria) {
		this.tipoAuditoria = tipoAuditoria;
	}	

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	
	public void setItemAuditoriaInterna(ItemAuditoriaInterna itemAuditoriaInterna) {
		this.itemAuditoriaInterna = itemAuditoriaInterna;
	}
	
	public void setUgRegistro(UnidadeGerencial ugRegistro) {
		this.ugRegistro = ugRegistro;
	}
	public void setUgResponsavel(UnidadeGerencial ugResponsavel) {
		this.ugResponsavel = ugResponsavel;
	}
	public void setConclusao(String conclusao) {
		this.conclusao = conclusao;
	}
	public void setContraMedidasImediatas(String contraMedidasImediatas) {
		this.contraMedidasImediatas = contraMedidasImediatas;
	}
	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setLocal(LocalAnomaliaEnum local) {
		this.local = local;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public void setClassificacao(ClassificacaoAnomaliaEnum classificacao) {
		this.classificacao = classificacao;
	}
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	public void setPadronizacao(String padronizacao) {
		this.padronizacao = padronizacao;
	}
	public void setCausasEfeito(Set<CausaEfeito> causasEfeito) {
		this.causasEfeito = causasEfeito;
	}
	public void setVerificacao(String verificacao) {
		this.verificacao = verificacao;
	}
	@OneToMany(mappedBy="anomalia")
	@DisplayName("Anexos")	
	public Set<AnexoAnomalia> getAnexosAnomalia() {
		return anexosAnomalia;
	}
	public void setAnexosAnomalia(Set<AnexoAnomalia> anexosAnomalia) {
		this.anexosAnomalia = anexosAnomalia;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subordinacao_id")
	public Anomalia getSubordinacao() {
		return subordinacao;
	}
	
	public void setSubordinacao(Anomalia subordinacao) {
		this.subordinacao = subordinacao;
	}
	
	//=================================Transiente===============================//
	private PlanoGestao planoGestao;
	private AcompanhamentoIndicador acompanhamentoIndicador;
	private CausaEfeito efeito;
	private Boolean podeEditar;
	private Boolean podeExcluir;
	private Boolean podeImprimir;
	private Boolean encerrado;
	@Transient
	@Required
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	@Transient
	public AcompanhamentoIndicador getAcompanhamentoIndicador() {
		return acompanhamentoIndicador;
	}
	@Transient
	public CausaEfeito getEfeito() {
		return efeito;
	}
	@Transient
	public Boolean getPodeEditar() {
		return podeEditar;
	}
	@Transient
	public Boolean getPodeExcluir() {
		return podeExcluir;
	}
	@Transient
	public Boolean getPodeImprimir() {
		return podeImprimir;
	}
	@Transient
	public Boolean getEncerrado() {
		return encerrado;
	}
	public void setEncerrado(Boolean encerrado) {
		this.encerrado = encerrado;
	}
	public void setPodeImprimir(Boolean podeImprimir) {
		this.podeImprimir = podeImprimir;
	}
	public void setPodeEditar(Boolean podeEditar) {
		this.podeEditar = podeEditar;
	}
	public void setPodeExcluir(Boolean podeExcluir) {
		this.podeExcluir = podeExcluir;
	}
	public void setEfeito(CausaEfeito efeito) {
		this.efeito = efeito;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setAcompanhamentoIndicador(AcompanhamentoIndicador acompanhamentoIndicador) {
		this.acompanhamentoIndicador = acompanhamentoIndicador;
	}
	public void setPlanosAcao(Set<PlanoAcao> planosAcao) {
		this.planosAcao = planosAcao;
	}
	@MaxLength(1500)
	@DisplayName("Análise de Causas")
	public String getAnaliseCausas() {
		return analiseCausas;
	}
	public void setAnaliseCausas(String analiseCausas) {
		this.analiseCausas = analiseCausas;
	}
	@DisplayName("Data de destravamento")
	public Date getDataDestravamento() {
		return dataDestravamento;
	}
	public Date getDataSolicitacaoEncerramento() {
		return dataSolicitacaoEncerramento;
	}
	public Boolean getLembreteEnviado() {
		return lembreteEnviado;
	}
	public void setDataDestravamento(Date dataDestravamento) {
		this.dataDestravamento = dataDestravamento;
	}
	public void setDataSolicitacaoEncerramento(Date dataSolicitacaoEncerramento) {
		this.dataSolicitacaoEncerramento = dataSolicitacaoEncerramento;
	}
	public void setLembreteEnviado(Boolean lembreteEnviado) {
		this.lembreteEnviado = lembreteEnviado;
	}
	@Required
	@DisplayName("Status")
	public StatusAnomaliaEnum getStatus() {
		return status;
	}
	@DisplayName("Status do tratamento")
	public StatusTratamentoAnomaliaEnum getStatusTratamento() {
		return statusTratamento;
	}
	public void setStatus(StatusAnomaliaEnum status) {
		this.status = status;
	}
	public void setStatusTratamento(StatusTratamentoAnomaliaEnum statusTratamento) {
		this.statusTratamento = statusTratamento;
	}
	@DisplayName("Tipo")
	public TipoAnomaliaEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoAnomaliaEnum tipo) {
		this.tipo = tipo;
	}
	@DisplayName("Origem")
	public OrigemAnomaliaEnum getOrigem() {
		return origem;
	}
	public void setOrigem(OrigemAnomaliaEnum origem) {
		this.origem = origem;
	}
}
