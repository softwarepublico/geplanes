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
package br.com.linkcom.sgm.beans.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.AnexoIndicador;
import br.com.linkcom.sgm.beans.Hierarquizavel;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;

@SuppressWarnings("unchecked")
public class ItemPainelControleDTO implements Comparable<ItemPainelControleDTO> {

	private String id;
	private String icone;
	private String descricao;
	private Integer precisao;
	private boolean mostraPeso;
	private boolean mostraFarol;
	
	private Integer idOriginal;
	private String tipo;
	private Double peso;
	private List<AcompanhamentoIndicador> acompanhamentos = new ArrayList<AcompanhamentoIndicador>();
	private List<AcompanhamentoIndicador> acompanhamentosAcumulados = new ArrayList<AcompanhamentoIndicador>();
	private List<AnexoIndicador> anexos = new ArrayList<AnexoIndicador>();
	
	private Map<Integer, List<AcompanhamentoIndicador>> acompanhamentosPorFatorDeDivisao = new HashMap<Integer, List<AcompanhamentoIndicador>>();
	private Map<Integer, List<AcompanhamentoIndicador>> acompanhamentosAcumuladosPorFatorDeDivisao = new HashMap<Integer, List<AcompanhamentoIndicador>>();
	
	private ItemPainelControleDTO parent;
	private List<ItemPainelControleDTO> filhos = new ArrayList<ItemPainelControleDTO>();
	private boolean temFilhos;
	private Hierarquizavel hierarquizavel;
	private Double percentualTolerancia;
	

	
	public Hierarquizavel getHierarquizavel() {
		return hierarquizavel;
	}

	@SuppressWarnings("unchecked")
	public void setHierarquizavel(Hierarquizavel hierarquizavel) {
		if(hierarquizavel instanceof Indicador){
			Indicador indicador = (Indicador) hierarquizavel;
			if(indicador.getAnexosIndicador() != null && indicador.getAnexosIndicador().size() > 0){
				this.anexos.addAll(indicador.getAnexosIndicador());	
			}
		}
		if(hierarquizavel instanceof PerspectivaMapaEstrategico || hierarquizavel instanceof Indicador) {
			mostraPeso = true;
		}
		if(hierarquizavel instanceof ObjetivoMapaEstrategico) {
			mostraFarol = false;
		} else {
			mostraFarol = true;
		}
		
		this.hierarquizavel = hierarquizavel;
		this.percentualTolerancia = hierarquizavel.getPercentualTolerancia();
	}

	public ItemPainelControleDTO(){ 	}
	
	@SuppressWarnings("unchecked")
	public ItemPainelControleDTO(Hierarquizavel h, List<ItemPainelControleDTO> dtosFilhos){
		this.descricao = h.getDescricaoCompleta();
		this.tipo = h.getTipo();
		this.idOriginal = h.getId();
		this.peso = h.getPeso();
		
		this.icone = h.getTipo();
		if (h instanceof Indicador) {
			Indicador ind = (Indicador) h;
			this.precisao = ind.getPrecisao();
			this.icone = h.getTipo() + "_" + ind.getMelhor().name();
		}
		
		this.filhos = dtosFilhos;
		this.temFilhos = ( dtosFilhos != null && dtosFilhos.size() > 0 ) ;
		
		if (h instanceof PerspectivaMapaEstrategico) {
			PerspectivaMapaEstrategico perspectiva = (PerspectivaMapaEstrategico) h;
			perspectiva.setItemPainelControleDTO(this);
		}
		
		
		setHierarquizavel(h);
	}
	
	public String getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
	public List<ItemPainelControleDTO> getFilhos() {
		return filhos;
	}
	public String getIcone() {
		return icone;
	}
	public ItemPainelControleDTO getParent() {
		return parent;
	}
	public List<AcompanhamentoIndicador> getAcompanhamentos() {
		return acompanhamentos;
	}
	public String getTipo() {
		return tipo;
	}
	public Double getPeso() {
		return peso;
	}
	public Integer getIdOriginal() {
		return idOriginal;
	}
	public List<AnexoIndicador> getAnexos() {
		//código para testes
//		AnexoIndicador anexoIndicador = new AnexoIndicador();
//		anexoIndicador.setDescricao("blablabla");
//		anexoIndicador.setId(1);
//		Arquivo arquivo = new Arquivo();
//		arquivo.setId(1);
//		anexoIndicador.setArquivo(arquivo);
//		anexos.add(anexoIndicador);
		
		return anexos;
	}
	public String[] getTitulosDetalhe(){
		if(acompanhamentos.size() == 4){
			return new String[]{"1° Trimestre","2° Trimestre","3° Trimestre","4° Trimestre"};
		}
		if(acompanhamentos.size() == 12){
			return new String[]{"JAN","FEV","MAR","ABR","MAI","JUN","JUL","AGO","SET","OUT","NOV","DEZ"};
		}
		if(acompanhamentos.size() == 24){
			return new String[]{"J1","J2","F1","F2","M1","M2","A1","A2","M1","M2","J1","J2","J1","J2","A1","A2","S1","S2","O1","O2","N1","N2","D1","D2"};
		}
		return new String[0];
	}
	public int getTamanhoAcompanhamento(){
		int size = acompanhamentos.size();
		if(size == 0){
			return 500;
		}
		if(size == 4){
			return 113;
		} else if(size == 12){
			return 37;
		} else {
			return 18;
		}
	}
	public boolean isTemFilhos() {
		return temFilhos;
	}
	public Integer getPrecisao() {
		return precisao;
	}
	
	public void setPrecisao(Integer precisao) {
		this.precisao = precisao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setFilhos(List<ItemPainelControleDTO> filhos) {
		this.filhos = filhos;
	}
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAcompanhamentos(List<AcompanhamentoIndicador> acompanhamentos) {
		this.acompanhamentos = acompanhamentos;
	}
	public void setAnexos(List<AnexoIndicador> anexos) {
		this.anexos = anexos;
	}
	public void setIdOriginal(Integer idOriginal) {
		this.idOriginal = idOriginal;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}	
	public void setParent(ItemPainelControleDTO parent) {
		this.parent = parent;
	}	
	public void setTemFilhos(boolean temFilhos) {
		this.temFilhos = temFilhos;
	}
	
	
	public ItemPainelControleDTO clone(){
		ItemPainelControleDTO clone = new ItemPainelControleDTO();
		
		clone.setIcone(this.icone);
		clone.setDescricao(this.descricao);
		clone.setIdOriginal(this.idOriginal);
		clone.setTipo(this.tipo);
		clone.setPeso(this.peso);
		clone.setAcompanhamentos(this.acompanhamentos);
		clone.setAcompanhamentosAcumulados(this.acompanhamentosAcumulados);
		clone.setTemFilhos(this.temFilhos);
		clone.setHierarquizavel(this.hierarquizavel);
		clone.setId(this.id);
		clone.setParent(this.parent);
		clone.setMostraPeso(this.mostraPeso);
		clone.setMostraFarol(this.mostraFarol);
		
		List<ItemPainelControleDTO> filhosClonados = new ArrayList<ItemPainelControleDTO>();
		if (this.filhos != null && this.filhos.size() > 0) {
			for (ItemPainelControleDTO dtoFilho : this.filhos) {
				//if (dtoFilho.getTipo().equals("perspectiva")) {
					filhosClonados.add(dtoFilho.clone());
				//}				
			}
		}
		clone.setFilhos(filhosClonados);
		
		return clone;
	}
	
	public ItemPainelControleDTO cloneNoChildren(){
		ItemPainelControleDTO clone = new ItemPainelControleDTO();
		
		clone.setIcone(this.icone);
		clone.setDescricao(this.descricao);
		clone.setIdOriginal(this.idOriginal);
		clone.setTipo(this.tipo);
		clone.setPeso(this.peso);
		clone.setAcompanhamentos(this.acompanhamentos);
		clone.setAcompanhamentosAcumulados(this.acompanhamentosAcumulados);
		clone.setTemFilhos(this.temFilhos);
		clone.setHierarquizavel(this.hierarquizavel);
		clone.setId(this.id);
		clone.setParent(this.parent);
		clone.setMostraPeso(this.mostraPeso);
		clone.setMostraFarol(this.mostraFarol);
		
		return clone;
	}
	
	@Override
	public String toString() {
		String idPai = getParent() != null ? getParent().getId() : "";
		return icone + " " +descricao + "[ " + id + ":" + idPai + "] peso: " + peso;
	}

	public Map<Integer, List<AcompanhamentoIndicador>> getAcompanhamentosPorFatorDeDivisao() {
		return acompanhamentosPorFatorDeDivisao;
	}
	
	public void setAcompanhamentosPorFatorDeDivisao(Map<Integer, List<AcompanhamentoIndicador>> acompanhamentosPorFatorDeDivisao) {
		this.acompanhamentosPorFatorDeDivisao = acompanhamentosPorFatorDeDivisao;
	}

	/**
	 * Retorna os acompanhamentos por fator de divisao, se já tiverem sido calculados
	 * Se não tiver sido calculado retorna null
	 * @param fator
	 * @return
	 */
	public List<AcompanhamentoIndicador> getAcompanhamentosPorFator(Integer fator){
		return acompanhamentosPorFatorDeDivisao.get(fator);
	}
	
	public List<AcompanhamentoIndicador> getAcompanhamentosAcumuladosPorFator(Integer fator){
		return acompanhamentosAcumuladosPorFatorDeDivisao.get(fator);
	}

	public Double getPercentualTolerancia() {
		return percentualTolerancia;
	}

	public void setPercentualTolerancia(Double percentualTolerancia) {
		this.percentualTolerancia = percentualTolerancia;
	}

	public List<AcompanhamentoIndicador> getAcompanhamentosAcumulados() {
		return acompanhamentosAcumulados;
	}

	public void setAcompanhamentosAcumulados(
			List<AcompanhamentoIndicador> acompanhamentosAcumulados) {
		this.acompanhamentosAcumulados = acompanhamentosAcumulados;
	}

	public Map<Integer, List<AcompanhamentoIndicador>> getAcompanhamentosAcumuladosPorFatorDeDivisao() {
		return acompanhamentosAcumuladosPorFatorDeDivisao;
	}

	public void setAcompanhamentosAcumuladosPorFatorDeDivisao(
			Map<Integer, List<AcompanhamentoIndicador>> acompanhamentosAcumuladosPorFatorDeDivisao) {
		this.acompanhamentosAcumuladosPorFatorDeDivisao = acompanhamentosAcumuladosPorFatorDeDivisao;
	}

	public boolean isMostraPeso() {
		return mostraPeso;
	}

	public void setMostraPeso(boolean mostraPeso) {
		this.mostraPeso = mostraPeso;
	}

	@SuppressWarnings("unchecked")
	public int compareTo(ItemPainelControleDTO that) {		
		Hierarquizavel hierarquizavelThis = this.getHierarquizavel();
		Hierarquizavel hierarquizavelThat = that.getHierarquizavel();

		if(hierarquizavelThis instanceof PerspectivaMapaEstrategico){
			if(hierarquizavelThat instanceof PerspectivaMapaEstrategico){
				return (((PerspectivaMapaEstrategico)hierarquizavelThis).getDescricao()).compareTo(((PerspectivaMapaEstrategico)hierarquizavelThat).getDescricao());
			} else {
				return -1;
			}
		} else if(hierarquizavelThat instanceof PerspectivaMapaEstrategico){
			return 1;
		}
		
		if(hierarquizavelThis instanceof UnidadeGerencial){
			if(hierarquizavelThat instanceof UnidadeGerencial){
				return ((UnidadeGerencial)hierarquizavelThis).getSigla().compareTo(((UnidadeGerencial)hierarquizavelThat).getSigla());
			} else {
				return -1;
			}
		} else if(hierarquizavelThat instanceof UnidadeGerencial){
			return 1;
		}	

		if(hierarquizavelThis instanceof ObjetivoMapaEstrategico){
			if(hierarquizavelThat instanceof ObjetivoMapaEstrategico){
				return (((ObjetivoMapaEstrategico)hierarquizavelThis).getDescricao()).compareTo(((ObjetivoMapaEstrategico)hierarquizavelThat).getDescricao());
			} else {
				return -1;
			}
		} else if(hierarquizavelThat instanceof ObjetivoMapaEstrategico){
			return 1;
		}
		if(hierarquizavelThis instanceof Indicador){
			if(hierarquizavelThat instanceof Indicador){
				return (((Indicador)hierarquizavelThis).getNome()).compareTo(((Indicador)hierarquizavelThat).getNome());
			}
		}
		return 0;
	}

	public boolean isMostraFarol() {
		return mostraFarol;
	}

	public void setMostraFarol(boolean mostraFarol) {
		this.mostraFarol = mostraFarol;
	}

}
