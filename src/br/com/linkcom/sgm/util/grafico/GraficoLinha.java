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
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;

import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.beans.enumeration.GraficoTipoEnum;

public class GraficoLinha {

	public static JFreeChart geraGrafico(CategoryDataset dataset, String tituloGrafico, String tituloX, String tituloY, boolean exibirLegenda, GraficoTipoEnum graficoTipo, FrequenciaIndicadorEnum frequencia, List<GraficoSerie> listaGraficoSerie){		
		
		JFreeChart chart = ChartFactory.createLineChart(
	            tituloGrafico, 		// title
	            tituloX,            // x-axis label
	            tituloY,   			// y-axis label
	            dataset,           // data
	            PlotOrientation.VERTICAL,
	            exibirLegenda,      // create legend?
	            true,               // generate tooltips?
	            false               // generate URLs?
	        );
		
	        chart.setBackgroundPaint(Color.white);

	        CategoryPlot plot = (CategoryPlot) chart.getPlot();
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
	        plot.setRangeCrosshairVisible(true);
	        
	        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
	        renderer.setBaseShapesVisible(true);
	        renderer.setBaseShapesFilled(true);
	        
	        if (listaGraficoSerie != null && !listaGraficoSerie.isEmpty()) {
	        	for (GraficoSerie graficoSerie : listaGraficoSerie) {
	        		renderer.setSeriesPaint(graficoSerie.getNumSerie(), graficoSerie.getCor());
				}
	        }	        

	        if (graficoTipo.equals(GraficoTipoEnum.PERCENTUAL)) { //Percentual
		        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		        rangeAxis.setRange(0,101);
		        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        }	        
	        
	        CategoryAxis domainAxis = (CategoryAxis) plot.getDomainAxis();
	        
	        if (FrequenciaIndicadorEnum.QUINZENAL.equals(frequencia)) {
	        	domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
	        }
	        
            
	        return chart;
	}
}
