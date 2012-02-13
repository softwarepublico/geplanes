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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.MaxLength;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.dao.UnidadeGerencialDAO;
import br.com.linkcom.sgm.util.Nomes;

@Entity
@SequenceGenerator(name = "sq_unidadegerencial", sequenceName = "sq_unidadegerencial")
public class UnidadeGerencial implements Hierarquizavel<UnidadeGerencial, PerspectivaMapaEstrategico> {
	
	private Integer id;
	
	private String nome;
	private String sigla;
	
	private PlanoGestao planoGestao;	
	private UnidadeGerencial subordinacao;
	private NivelHierarquico nivelHierarquico;
	private int nivelNum;
	
	// Identifica áreas representantes da qualidade. 
	// Usuários vinculados a essas UGs poderão consultar (EDITAR SEM ALTERAR NADA) 
	// todas as anomalias cadastradas pela ou para sua respectiva diretoria, 
	// bem como seus setores subordinados.	
	private Boolean areaQualidade;
	
	// Identifica áreas que podem fazer auditoria interna. 
	// Usuários vinculados a essas UGs poderão incluir/editar/excluir registros de auditoria interna.	
	private Boolean areaAuditoriaInterna;	
	
	private Integer seqAnomalia;
	private Integer seqAcaoPreventiva;
	private Integer seqOcorrencia;
	
	/* Controle dos mapas cujo preenchimento será permitido e cujo não preenchimento será cobrado */
	private Boolean permitirMapaNegocio;
	private Boolean permitirMapaCompetencia;
	private Boolean permitirMapaEstrategico;
	private Boolean permitirMatrizFcs;
	
	private MapaNegocio mapaNegocio;
	private MapaCompetencia mapaCompetencia;
	private MapaEstrategico mapaEstrategico;
	private Set<UsuarioUnidadeGerencial> usuariosUnidadeGerencial = new ListSet<UsuarioUnidadeGerencial>(UsuarioUnidadeGerencial.class);
	private List<Indicador> listaIndicador = new ListSet<Indicador>(Indicador.class);
	
	public UnidadeGerencial() {
	}
	
	public UnidadeGerencial(Integer id) {
		this();
		this.id = id;
	}
	
	//=========================Get e Set==================================//

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_unidadegerencial")
	public Integer getId() {
		return id;
	}
	
	@Required
	@MaxLength(100)
	public String getNome() {
		return nome;
	}
	
	@Required
	@MaxLength(20)
	@DescriptionProperty
	public String getSigla() {
		if(sigla == null && nome==null && id != null){
			Neo.getObject(UnidadeGerencialDAO.class).loadSiglaNome(this);
		}
		return sigla;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	
	@DisplayName("Nível hierárquico")
	@ManyToOne(fetch=FetchType.LAZY)
	public NivelHierarquico getNivelHierarquico() {
		return nivelHierarquico;
	}
	
	@Required
	public int getNivelNum() {
		return nivelNum;
	}
	
	@DisplayName("Área de qualidade")
	@Required
	public Boolean getAreaQualidade() {
		return areaQualidade;
	}
	
	@DisplayName("Área de auditoria interna")
	@Required	
	public Boolean getAreaAuditoriaInterna() {
		return areaAuditoriaInterna;
	}	
	
	@OneToMany(mappedBy="unidadeGerencial")
	@DisplayName("Usuários vinculados")
	public Set<UsuarioUnidadeGerencial> getUsuariosUnidadeGerencial() {
		return usuariosUnidadeGerencial;
	}
	
	@OneToMany(mappedBy="unidadeGerencial")
	public List<Indicador> getListaIndicador() {
		return listaIndicador;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subordinacao_id")
	@DisplayName("Subordinação")
	public UnidadeGerencial getSubordinacao() {
		return subordinacao;
	}
	
	@OneToOne(mappedBy="unidadeGerencial", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@DisplayName("Mapa do negócio")
	public MapaNegocio getMapaNegocio() {
		return mapaNegocio;
	}
	
	@OneToOne(mappedBy="unidadeGerencial", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@DisplayName("Mapa de competências")
	public MapaCompetencia getMapaCompetencia() {
		return mapaCompetencia;
	}
	
	@OneToOne(mappedBy="unidadeGerencial", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@DisplayName("Mapa estratégico")	
	public MapaEstrategico getMapaEstrategico() {
		return mapaEstrategico;
	}
	
	@DisplayName("Permitir o cadastro do mapa do negócio")
	public Boolean getPermitirMapaNegocio() {
		return permitirMapaNegocio;
	}

	@DisplayName("Permitir o cadastro do mapa de competências")
	public Boolean getPermitirMapaCompetencia() {
		return permitirMapaCompetencia;
	}

	@DisplayName("Permitir o cadastro do mapa estratégico")
	public Boolean getPermitirMapaEstrategico() {
		return permitirMapaEstrategico;
	}

	@DisplayName("Permitir o cadastro da matriz de iniciativas x fcs")
	public Boolean getPermitirMatrizFcs() {
		return permitirMatrizFcs;
	}

	public void setMapaEstrategico(MapaEstrategico mapaEstrategico) {
		this.mapaEstrategico = mapaEstrategico;
	}	

	public void setSubordinacao(UnidadeGerencial subordinacao) {
		this.subordinacao = subordinacao;
	}
	public void setUsuariosUnidadeGerencial(Set<UsuarioUnidadeGerencial> usuariosUnidadeGerencial) {
		this.usuariosUnidadeGerencial = usuariosUnidadeGerencial;
	}
	public void setListaIndicador(List<Indicador> listaIndicador) {
		this.listaIndicador = listaIndicador;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNivelHierarquico(NivelHierarquico nivelHierarquico) {
		this.nivelHierarquico = nivelHierarquico;
	}
	public void setNivelNum(int nivelNum) {
		this.nivelNum = nivelNum;
	}
	public void setAreaQualidade(Boolean areaQualidade) {
		this.areaQualidade = areaQualidade;
	}
	public void setAreaAuditoriaInterna(Boolean areaAuditoriaInterna) {
		this.areaAuditoriaInterna = areaAuditoriaInterna;
	}	
	public void setMapaNegocio(MapaNegocio mapaNegocio) {
		this.mapaNegocio = mapaNegocio;
	}
	public void setMapaCompetencia(MapaCompetencia mapaCompetencia) {
		this.mapaCompetencia = mapaCompetencia;
	}
	
	public Integer getSeqAnomalia() {
		return seqAnomalia;
	}
	
	public Integer getSeqAcaoPreventiva() {
		return seqAcaoPreventiva;
	}

	public Integer getSeqOcorrencia() {
		return seqOcorrencia;
	}

	public void setSeqAnomalia(Integer seqAnomalia) {
		this.seqAnomalia = seqAnomalia;
	}
	
	public void setSeqAcaoPreventiva(Integer seqAcaoPreventiva) {
		this.seqAcaoPreventiva = seqAcaoPreventiva;
	}

	public void setSeqOcorrencia(Integer seqOcorrencia) {
		this.seqOcorrencia = seqOcorrencia;
	}	
	
	public void setPermitirMapaNegocio(Boolean permitirMapaNegocio) {
		this.permitirMapaNegocio = permitirMapaNegocio;
	}

	public void setPermitirMapaCompetencia(Boolean permitirMapaCompetencia) {
		this.permitirMapaCompetencia = permitirMapaCompetencia;
	}

	public void setPermitirMapaEstrategico(Boolean permitirMapaEstrategico) {
		this.permitirMapaEstrategico = permitirMapaEstrategico;
	}
	
	public void setPermitirMatrizFcs(Boolean permitirMatrizFcs) {
		this.permitirMatrizFcs = permitirMatrizFcs;
	}
	
	//========================= TRANSIENT ==================================//
	private Boolean podeEditar;
	private Boolean podeImprimirMapaNegocio;
	private Integer numeroFilhos;
	private List<UnidadeGerencial> filhos = new ArrayList<UnidadeGerencial>();
	private List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico;
	
	@Transient
	public String getDescricao() {
		return getSigla() + " - " + getNome();
	}
	
	@Transient
	public Boolean getPodeEditar() {
		return podeEditar;
	}
	
	public void setPodeEditar(Boolean podeEditar) {
		this.podeEditar = podeEditar;
	}
	
	@Transient
	public Boolean getPodeImprimirMapaNegocio() {
		return podeImprimirMapaNegocio;
	}
	
	public void setPodeImprimirMapaNegocio(Boolean podeImprimirMapaNegocio) {
		this.podeImprimirMapaNegocio = podeImprimirMapaNegocio;
	}
	
	@Transient
	public Integer getNumeroFilhos() {
		return numeroFilhos;
	}
	public void setNumeroFilhos(Integer numeroFilhos) {
		this.numeroFilhos = numeroFilhos;
	}
	
	@Transient
	public List<UnidadeGerencial> getFilhos() {
		return filhos;
	}
	public void setFilhos(List<UnidadeGerencial> filhos) {
		this.filhos = filhos;
	}
	
	@Transient
	public List<PerspectivaMapaEstrategico> getListaPerspectivaMapaEstrategico() {
		return listaPerspectivaMapaEstrategico;
	}
	
	public void setListaPerspectivaMapaEstrategico(List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico) {
		this.listaPerspectivaMapaEstrategico = listaPerspectivaMapaEstrategico;
	}
	
    //========================= IMPLEMENTAÇÕES ==================================//
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof UnidadeGerencial) ) return false;
		UnidadeGerencial unidadeGerencial = (UnidadeGerencial) obj;
		if (this.id == null || unidadeGerencial.getId() == null ) return false; 
		return this.id.equals(unidadeGerencial.getId());
	}

    //========================= INTERFACE ==================================//
	
	@Transient
	public List<PerspectivaMapaEstrategico> getChildren() {
		List<PerspectivaMapaEstrategico> retorno = new ListSet<PerspectivaMapaEstrategico>(PerspectivaMapaEstrategico.class);
		retorno.addAll( getListaPerspectivaMapaEstrategico() );
		return retorno;
	}
	
	@Transient
	public String getTipo() {
		return "ug";
	}
	
	@Transient
	public String getDescricaoCompleta() {
		return getSigla() != null ? getSigla() : "<SEM NOME>";
	}
	
	@Transient
	public UnidadeGerencial getParent() {
		return null;
	}
	
	@Transient
	public void setParent(UnidadeGerencial parent) {
		
	}
	
	@Transient
	public Double getPeso() {
		return null;
	}

	@Transient
	public Double getPercentualTolerancia() {
		return null;
	}
}
