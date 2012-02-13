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

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;

public class GraficoBarra {
	public static JFreeChart geraGrafico(DefaultCategoryDataset dataset1, String tituloGrafico, String tituloX, String tituloY, PlotOrientation orientacao, FrequenciaIndicadorEnum frequencia, List<GraficoSerie> listaGraficoSerie) {
		
		CategoryDataset dataset2 = dataset1;			
	
		final CategoryAxis categoryAxis = new CategoryAxis(tituloX);
	    final ValueAxis valueAxis = new NumberAxis(tituloY);
        final CategoryPlot plot = new CategoryPlot(dataset2,
	                                             categoryAxis, 
	                                             valueAxis, 
	                                             new LayeredBarRenderer());
        
        plot.setOrientation(orientacao);
        final JFreeChart chart = new JFreeChart(
            tituloGrafico, 
            JFreeChart.DEFAULT_TITLE_FONT, 
            plot, 
            true
        );
        
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final LayeredBarRenderer renderer = (LayeredBarRenderer) plot.getRenderer();

        renderer.setItemMargin(0.01);
        
        if (listaGraficoSerie != null && !listaGraficoSerie.isEmpty()) {
        	for (GraficoSerie graficoSerie : listaGraficoSerie) {
        		renderer.setSeriesBarWidth(graficoSerie.getNumSerie(), graficoSerie.getLargura());
        		renderer.setSeriesPaint(graficoSerie.getNumSerie(), graficoSerie.getCor());
			}
        }

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.25);
        domainAxis.setUpperMargin(0.05);
        domainAxis.setLowerMargin(0.05);
        
        if (FrequenciaIndicadorEnum.QUINZENAL.equals(frequencia)) {
        	domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }        

		return chart;
	}	
}
