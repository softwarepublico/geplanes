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
package br.com.linkcom.sgm.beans.enumeration;


public enum ItemControleCadastroEnum {
	
	MAPA_NEGOCIO			(0, "Cadastro do mapa do negócio", "/sgm/process/DefinicaoNegocio"),
	MAPA_ESTRATEGICO		(1, "Cadastro do mapa estratégico", "/sgm/process/DefinicaoEstrategia"),
	MAPA_COMPETENCIA		(2, "Cadastro do mapa de competências", "/sgm/process/DefinicaoCompetencia"),
	MATRIZ_FCS				(3, "Cadastro da matriz de iniciativas x fcs", "/sgm/process/MatrizFCS"),
	INDICADORES				(4, "Cadastro de indicadores", "/sgm/process/DistribuicaoPesosIndicadores"),
	VALORES_BASE			(5, "Cadastro de metas", "/sgm/process/DistribuicaoPesosIndicadores"),
	VALORES_REAIS			(6, "Lançamento de resultados", "/sgm/process/LancamentoValorReal"),
	TRATAMENTO_ANOMALIA		(7, "Tratamento da anomalia", "/sgm/crud/Anomalia"),
	PLANO_ACAO_INICIATIVA	(8, "Plano de ação da iniciativa", "/sgm/process/IniciativaPlanoAcao");
	
	private Integer codigo;
	private String descricao;
	private String path;
	
	ItemControleCadastroEnum(Integer codigo, String descricao, String path) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.path = path;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
	public String getName(){
		return name();
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}