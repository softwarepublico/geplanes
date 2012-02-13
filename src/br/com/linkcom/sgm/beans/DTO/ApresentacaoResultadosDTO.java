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

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import br.com.linkcom.neo.bean.annotation.DisplayName;
import br.com.linkcom.neo.validation.annotation.Required;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.GraficoApresentacaoEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoResultadoEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoTipoEnum;
import br.com.linkcom.sgm.util.Nomes;


/**
 * @author Marcus Abreu
 */
public class ApresentacaoResultadosDTO{
	
	private Calendar dataInicial;
	private Calendar dataFinal;
	private GraficoTipoEnum tipoGrafico;
	private GraficoApresentacaoEnum formaApresentacao;
	private GraficoResultadoEnum tipoResultado;
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private List<Indicador> listaIndicadores;
	private Integer idIndicadorSelecionado;
	private Indicador indicadorSelecionado; 
	private InputStream grafico;
	
	@Required
	public UnidadeGerencial getUnidadeGerencial() {
		return unidadeGerencial;
	}
	@Required
	@DisplayName(Nomes.Plano_de_Gestao)
	public PlanoGestao getPlanoGestao() {
		return planoGestao;
	}
	@Required
	@DisplayName("Data final")
	public Calendar getDataFinal() {
		return dataFinal;
	}
	@Required
	@DisplayName("Data inicial")
	public Calendar getDataInicial() {
		return dataInicial;
	}
	@Required
	@DisplayName("Tipo do gráfico")
	public GraficoTipoEnum getTipoGrafico() {
		return tipoGrafico;
	}
	@Required
	@DisplayName("Forma de apresentação")
	public GraficoApresentacaoEnum getFormaApresentacao() {
		return formaApresentacao;
	}
	@DisplayName("Tipo de resultado")
	public GraficoResultadoEnum getTipoResultado() {
		return tipoResultado;
	}
	public Integer getIdIndicadorSelecionado() {
		return idIndicadorSelecionado;
	}
	public Indicador getIndicadorSelecionado() {
		return indicadorSelecionado;
	}
	public InputStream getGrafico() {
		return grafico;
	}
	public List<Indicador> getListaIndicadores() {
		return listaIndicadores;
	}
	public void setListaIndicadores(List<Indicador> listaUsuario) {
		this.listaIndicadores = listaUsuario;
	}
	public void setFormaApresentacao(GraficoApresentacaoEnum formaApresentacao) {
		this.formaApresentacao = formaApresentacao;
	}
	public void setTipoGrafico(GraficoTipoEnum tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}
	public void setTipoResultado(GraficoResultadoEnum tipoResultado) {
		this.tipoResultado = tipoResultado;
	}
	public void setDataFinal(Calendar dataFinal) {
		this.dataFinal = dataFinal;
	}
	public void setDataInicial(Calendar dataInicial) {
		this.dataInicial = dataInicial;
	}
	public void setPlanoGestao(PlanoGestao planoGestao) {
		this.planoGestao = planoGestao;
	}
	public void setUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		this.unidadeGerencial = unidadeGerencial;
	}
	public void setIdIndicadorSelecionado(Integer idIndicadorSelecionado) {
		this.idIndicadorSelecionado = idIndicadorSelecionado;
	}
	public void setIndicadorSelecionado(Indicador indicadorSelecionado) {
		this.indicadorSelecionado = indicadorSelecionado;
	}
	public void setGrafico(InputStream grafico) {
		this.grafico = grafico;
	}
}