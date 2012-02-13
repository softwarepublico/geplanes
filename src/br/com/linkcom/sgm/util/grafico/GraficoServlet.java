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
package br.com.linkcom.sgm.util.grafico;



import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoApresentacaoEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoTipoEnum;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.report.bean.AuditoriaGestaoGraficoBean;


/**
 * @author Rodrigo Alvarenga
 */
public class GraficoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Color COR_LIMITE     = new Color(0x339900);
	private static final Color COR_TOLERANCIA = Color.YELLOW;
	private static final Color COR_RESULTADO  = Color.BLUE;
	
	@SuppressWarnings("unchecked")
	public byte[] geraGrafico(HttpServletRequest request, String tipoGrafico, String apresentacaoGrafico, boolean pdf) {
		try {			
			String tituloGrafico = "";
			String tituloX = "";
			String tituloY = "";
			FrequenciaIndicadorEnum frequencia = null;
			List<GraficoSerie> listaGraficoSerie = new ArrayList<GraficoSerie>();
			GraficoSerie graficoSerie;
			boolean exibirTolerancia;

			JFreeChart chart;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			Indicador indicador = (Indicador) request.getSession().getAttribute("indicadorSelecionado");
			
			//Tipo do Gráfico
			GraficoTipoEnum graficoTipo = null;
			if (tipoGrafico.equals(GraficoTipoEnum.ACOMPANHAMENTO.getName())) {
				graficoTipo = GraficoTipoEnum.ACOMPANHAMENTO;
			}
			else if (tipoGrafico.equals(GraficoTipoEnum.PERCENTUAL.getName())) {
				graficoTipo = GraficoTipoEnum.PERCENTUAL;
			}
			else if (tipoGrafico.equals(GraficoTipoEnum.FAROL.getName())) {
				graficoTipo = GraficoTipoEnum.FAROL;
			}
			
			//Apresentação do Gráfico
			GraficoApresentacaoEnum graficoApresentacao = null;
			if (apresentacaoGrafico.equals(GraficoApresentacaoEnum.LINHA.getName())) {
				graficoApresentacao = GraficoApresentacaoEnum.LINHA;
			}
			else if (apresentacaoGrafico.equals(GraficoApresentacaoEnum.BARRA.getName())) {
				graficoApresentacao = GraficoApresentacaoEnum.BARRA;
			}
			else if (apresentacaoGrafico.equals(GraficoApresentacaoEnum.COLUNA.getName())) {
				graficoApresentacao = GraficoApresentacaoEnum.COLUNA;
			}			
			else if (apresentacaoGrafico.equals(GraficoApresentacaoEnum.PIZZA.getName())) {
				graficoApresentacao = GraficoApresentacaoEnum.PIZZA;
			}			
			
			if (graficoApresentacao.equals(GraficoApresentacaoEnum.LINHA)) { //Gráfico de Linha
		        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

				if (graficoTipo.equals(GraficoTipoEnum.ACOMPANHAMENTO)) { //Acompanhamento
					tituloGrafico = "Gráfico de Acompanhamento de Resultados";
					tituloX = "Período";
					tituloY = "Valor";
					String serieLimInf;
					String serieReal;
					String serieTolerancia;
					String serieToleranciaInf;
					String serieToleranciaSup;
					String serieLimSup;
					
					if (indicador != null) {
						frequencia       = indicador.getFrequencia();
						serieReal        = "Realizado";
						exibirTolerancia = indicador.getTolerancia() != null && indicador.getTolerancia().doubleValue() != 0.0;
						
						switch (indicador.getMelhor()) {
						
							case MELHOR_BAIXO:
								serieTolerancia = "Tolerância";
								serieLimInf     = "Meta";
								
								// Meta
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(0);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Realizado
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(1);
								graficoSerie.setCor(COR_RESULTADO);
								listaGraficoSerie.add(graficoSerie);
								
								// Tolerância
								if (exibirTolerancia) {
									graficoSerie = new GraficoSerie();
									graficoSerie.setNumSerie(2);
									graficoSerie.setCor(COR_TOLERANCIA);
									listaGraficoSerie.add(graficoSerie);
								}
								
								for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
									dataset.addValue(acompanhamentoIndicador.getValorLimiteInferior(), serieLimInf, acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorReal(), serieReal, acompanhamentoIndicador.getEpoca());
									if (exibirTolerancia) {
										if (acompanhamentoIndicador.getValorLimiteInferior() != null) {
											dataset.addValue(acompanhamentoIndicador.getValorLimiteInferior() + (acompanhamentoIndicador.getValorLimiteInferior() * acompanhamentoIndicador.getPercentualTolerancia() / 100.00), serieTolerancia, acompanhamentoIndicador.getEpoca());
										}
									}
								}
								break;
								
							case MELHOR_CIMA:
								serieTolerancia = "Tolerância";
								serieLimSup     = "Meta";
								
								// Meta
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(0);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Realizado
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(1);
								graficoSerie.setCor(COR_RESULTADO);
								listaGraficoSerie.add(graficoSerie);
								
								// Tolerância
								if (exibirTolerancia) {
									graficoSerie = new GraficoSerie();
									graficoSerie.setNumSerie(2);
									graficoSerie.setCor(COR_TOLERANCIA);
									listaGraficoSerie.add(graficoSerie);
								}
								
								for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
									dataset.addValue(acompanhamentoIndicador.getValorLimiteSuperior(), serieLimSup, acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorReal(), serieReal, acompanhamentoIndicador.getEpoca());
									if (exibirTolerancia) {
										if (acompanhamentoIndicador.getValorLimiteSuperior() != null) {
											dataset.addValue(acompanhamentoIndicador.getValorLimiteSuperior() - (acompanhamentoIndicador.getValorLimiteSuperior() * acompanhamentoIndicador.getPercentualTolerancia() / 100.00), serieTolerancia, acompanhamentoIndicador.getEpoca());
										}
									}
								}						
								break;
								
							case MELHOR_ENTRE_FAIXAS:
								serieToleranciaInf = "Tolerância Inferior";
								serieToleranciaSup = "Tolerância Superior";
								serieLimSup        = "Limite Superior";							
								serieLimInf        = "Limite Inferior";
								
								// Limite Inferior
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(0);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Limite Superior
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(1);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Realizado
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(2);
								graficoSerie.setCor(COR_RESULTADO);
								listaGraficoSerie.add(graficoSerie);
								
								// Tolerância
								if (exibirTolerancia) {
									graficoSerie = new GraficoSerie();
									graficoSerie.setNumSerie(3);
									graficoSerie.setCor(COR_TOLERANCIA);
									listaGraficoSerie.add(graficoSerie);
									
									graficoSerie = new GraficoSerie();
									graficoSerie.setNumSerie(4);
									graficoSerie.setCor(COR_TOLERANCIA);
									listaGraficoSerie.add(graficoSerie);
								}								
								
								for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
									dataset.addValue(acompanhamentoIndicador.getValorLimiteInferior(), serieLimInf, acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorLimiteSuperior(), serieLimSup, acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorReal(), serieReal, acompanhamentoIndicador.getEpoca());
									if (exibirTolerancia) {
										if (acompanhamentoIndicador.getValorLimiteSuperior() != null && acompanhamentoIndicador.getValorLimiteInferior() != null) {
											dataset.addValue(acompanhamentoIndicador.getValorLimiteInferior() - ((acompanhamentoIndicador.getValorLimiteSuperior() - acompanhamentoIndicador.getValorLimiteInferior()) * acompanhamentoIndicador.getPercentualTolerancia() / 100.00), serieToleranciaInf, acompanhamentoIndicador.getEpoca());
											dataset.addValue(acompanhamentoIndicador.getValorLimiteSuperior() + ((acompanhamentoIndicador.getValorLimiteSuperior() - acompanhamentoIndicador.getValorLimiteInferior()) * acompanhamentoIndicador.getPercentualTolerancia() / 100.00), serieToleranciaSup, acompanhamentoIndicador.getEpoca());
										}
									}
								}							
								break;
								
							default:
								break;
						}
					}					
				}
				else if (graficoTipo.equals(GraficoTipoEnum.PERCENTUAL)) { //Percentual
					tituloGrafico = "Gráfico de Percentual Realizado";
					tituloX = "Período";
					tituloY = "Percentual(%)";
					String serieReal;
					
					if (indicador != null) {
						frequencia = indicador.getFrequencia();						
						serieReal = "Realizado";
						
						// Meta
						graficoSerie = new GraficoSerie();
						graficoSerie.setNumSerie(0);
						graficoSerie.setCor(COR_LIMITE);
						listaGraficoSerie.add(graficoSerie);
						
						// Realizado
						graficoSerie = new GraficoSerie();
						graficoSerie.setNumSerie(1);
						graficoSerie.setCor(COR_RESULTADO);
						listaGraficoSerie.add(graficoSerie);
						
						for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
							dataset.addValue(new Double(100), "Meta", acompanhamentoIndicador.getEpoca());								
							dataset.addValue(acompanhamentoIndicador.getPercentualReal(), serieReal, acompanhamentoIndicador.getEpoca());								
						}
					}					
				}
				else {
					throw new GeplanesException("Tipo de gráfico inválido para o tipo de apresentação");
				}
		        chart = GraficoLinha.geraGrafico(dataset,tituloGrafico,tituloX,tituloY, true, graficoTipo, frequencia, listaGraficoSerie);		        
		        
		        if (pdf) 
		        	ChartUtilities.writeChartAsPNG(out,chart,640,450);
		        else 
		        	ChartUtilities.writeChartAsPNG(out,chart,640,450);
			}
			
			else if (graficoApresentacao.equals(GraficoApresentacaoEnum.BARRA) || graficoApresentacao.equals(GraficoApresentacaoEnum.COLUNA)) { //Gráfico de Barra / Coluna
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();

				if (graficoTipo.equals(GraficoTipoEnum.ACOMPANHAMENTO)) { //Acompanhamento
					String serieLimInf;
					String serieReal;
					String serieLimSup;					
					
					if (indicador != null) {
						frequencia = indicador.getFrequencia();						
						tituloGrafico = "Gráfico de Acompanhamento de Resultados";
						tituloX = "Período";
						tituloY = "Valor";
						serieReal = "Realizado";
						
						switch (indicador.getMelhor()) {
						
							case MELHOR_BAIXO:
								serieLimInf = "Meta";
								
								// Meta
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(0);
								graficoSerie.setLargura(1.0);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Realizado
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(1);
								graficoSerie.setLargura(0.3);
								graficoSerie.setCor(COR_RESULTADO);
								listaGraficoSerie.add(graficoSerie);
								
								for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
									dataset.addValue(acompanhamentoIndicador.getValorLimiteInferior(),serieLimInf,acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorReal(),serieReal,acompanhamentoIndicador.getEpoca());						
								}
								break;
								
							case MELHOR_CIMA:
								serieLimSup = "Meta";
								
								// Meta
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(0);
								graficoSerie.setLargura(1.0);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Realizado
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(1);
								graficoSerie.setLargura(0.3);
								graficoSerie.setCor(COR_RESULTADO);
								listaGraficoSerie.add(graficoSerie);								
								
								for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
									dataset.addValue(acompanhamentoIndicador.getValorLimiteSuperior(),serieLimSup,acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorReal(),serieReal,acompanhamentoIndicador.getEpoca());						
								}						
								break;
								
							case MELHOR_ENTRE_FAIXAS:
								serieLimSup = "Limite Superior";							
								serieLimInf = "Limite Inferior";
								
								// Limite Inferior
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(0);
								graficoSerie.setLargura(1.0);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Limite Superior
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(1);
								graficoSerie.setLargura(0.6);
								graficoSerie.setCor(COR_LIMITE);
								listaGraficoSerie.add(graficoSerie);
								
								// Realizado
								graficoSerie = new GraficoSerie();
								graficoSerie.setNumSerie(2);
								graficoSerie.setLargura(0.3);
								graficoSerie.setCor(COR_RESULTADO);
								listaGraficoSerie.add(graficoSerie);								
								
								for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
									dataset.addValue(acompanhamentoIndicador.getValorLimiteInferior(),serieLimInf,acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorLimiteSuperior(),serieLimSup,acompanhamentoIndicador.getEpoca());
									dataset.addValue(acompanhamentoIndicador.getValorReal(),serieReal,acompanhamentoIndicador.getEpoca());						
								}							
								break;
								
							default:
								break;
						}
					}						
				}
				else if (graficoTipo.equals(GraficoTipoEnum.PERCENTUAL)) { //Percentual
					if (indicador != null) {
						frequencia = indicador.getFrequencia();						
						tituloGrafico = "Gráfico de Percentual Realizado";
						tituloX = "Período";
						tituloY = "Percentual(%)";
						
						// Meta
						graficoSerie = new GraficoSerie();
						graficoSerie.setNumSerie(0);
						graficoSerie.setLargura(1.0);
						graficoSerie.setCor(COR_LIMITE);
						listaGraficoSerie.add(graficoSerie);
						
						// Realizado
						graficoSerie = new GraficoSerie();
						graficoSerie.setNumSerie(1);
						graficoSerie.setLargura(0.3);
						graficoSerie.setCor(COR_RESULTADO);
						listaGraficoSerie.add(graficoSerie);
						
						for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
							dataset.addValue(new Double(100),"Meta",acompanhamentoIndicador.getEpoca());
							dataset.addValue(acompanhamentoIndicador.getPercentualReal(),"Realizado",acompanhamentoIndicador.getEpoca());						
						}
					}					
				}
				else {
					throw new GeplanesException("Tipo de gráfico inválido para o tipo de apresentação");
				}
		        chart = GraficoBarra.geraGrafico(dataset,tituloGrafico,tituloX,tituloY, graficoApresentacao.equals(GraficoApresentacaoEnum.BARRA) ? PlotOrientation.VERTICAL : PlotOrientation.HORIZONTAL, frequencia, listaGraficoSerie);
		        
		        if (pdf)
		        	ChartUtilities.writeChartAsPNG(out,chart,640,450);
		        else 
		        	ChartUtilities.writeChartAsPNG(out,chart,640,450);
			}
			
			else if (graficoApresentacao.equals(GraficoApresentacaoEnum.PIZZA)) { //Gráfico de Pizza
				DefaultCategoryDataset dsGraficoPizza = new DefaultCategoryDataset();

				if (graficoTipo.equals(GraficoTipoEnum.FAROL)) { //Farol
					tituloGrafico = "Gráfico de Farol";
					int[] numFarois;
					if (indicador != null) {
						numFarois = indicador.getNumFarois();
						dsGraficoPizza.addValue(numFarois[0],indicador.getNome(),"Valor não cadastrado");						
						dsGraficoPizza.addValue(numFarois[1],indicador.getNome(),"Meta não cumprida");						
						dsGraficoPizza.addValue(numFarois[2],indicador.getNome(),"Meta cumprida parcialmente");
						dsGraficoPizza.addValue(numFarois[3],indicador.getNome(),"Meta cumprida em 100%");
						dsGraficoPizza.addValue(numFarois[4],indicador.getNome(),"Meta não aplicável");
					}					
				}					
				else {
					throw new GeplanesException("Tipo de gráfico inválido para o tipo de apresentação");
				}			
				chart = GraficoPizza.criaGrafico(dsGraficoPizza,tituloGrafico,graficoTipo);
		        
				if (pdf)
		        	ChartUtilities.writeChartAsPNG(out,chart,650,400);
		        else 
		        	ChartUtilities.writeChartAsPNG(out,chart,680,400);
			}
			return out.toByteArray();
		}	
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] geraGraficoAuditoriaGestao(Map<AuditoriaGestaoGraficoBean, Integer> mapaAvaliacao, double valorMaximo) {
		try {			
			String tituloGrafico = "Maturidade da Gestão - Indicadores de Desempenho";
			
			JFreeChart chart;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
			Iterator<AuditoriaGestaoGraficoBean> itAuditoriaGestaoGraficoBean = mapaAvaliacao.keySet().iterator();
			
			while (itAuditoriaGestaoGraficoBean.hasNext()) {
				AuditoriaGestaoGraficoBean bean = itAuditoriaGestaoGraficoBean.next();
				dataset.addValue(mapaAvaliacao.get(bean), bean.getNomeIndicador(), bean.getNomeItemAuditoria());
				
			}
			
			chart = GraficoRadar.geraGrafico(dataset, tituloGrafico, valorMaximo);		        
			
			ChartUtilities.writeChartAsPNG(out,chart,1200,600);
			return out.toByteArray();
		}	
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			

		try {
			String graficoTipo = request.getParameter("graficoTipo") != null ? request.getParameter("graficoTipo") : "";
			String graficoApresentacao = request.getParameter("graficoApresentacao") != null ? request.getParameter("graficoApresentacao") : "";
			
			byte[] graficoByte = geraGrafico(request,graficoTipo,graficoApresentacao,false);
			
			// define o tipo de conteúdo.
			response.setContentType("image/png");
			response.setHeader("Content-Disposition", "attachment; filename=\"grafico.png\";");
			response.setHeader("pragma","no-cache");
			response.setHeader("cache-control","no-cache");
			response.setHeader("expires","0");	
			
			ServletOutputStream saida = response.getOutputStream();
			saida.write(graficoByte);
			saida.flush();
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}	
}