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
import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;

public class GraficoRadar {

	public static JFreeChart geraGrafico(CategoryDataset dataset, String tituloGrafico, double valorMaximo) {		
		
		SpiderWebPlot plot = new MySpiderWebPlot(dataset);
        plot.setInteriorGap(0.15);
        plot.setAxisLabelGap(0.08);
        plot.setHeadPercent(0.015);
                
        plot.setSeriesPaint(0, new Color(0x330066));
        plot.setSeriesPaint(1, Color.green);
        plot.setSeriesPaint(2, Color.red);
        plot.setSeriesPaint(3, Color.blue);
        plot.setSeriesPaint(4, Color.orange);
        plot.setSeriesPaint(5, Color.cyan);
        plot.setSeriesPaint(6, Color.magenta);
        plot.setSeriesPaint(7, Color.pink);
        plot.setSeriesPaint(8, Color.yellow);
        plot.setSeriesPaint(9, Color.darkGray);
        
        plot.setLabelFont(new Font("Verdana", Font.PLAIN, 12));
        plot.setMaxValue(valorMaximo);
        
        JFreeChart chart = new JFreeChart(tituloGrafico, TextTitle.DEFAULT_FONT, plot, false);
        LegendTitle legend = new LegendTitle(plot);
        legend.setPosition(RectangleEdge.RIGHT);
        chart.addSubtitle(legend);
        
        return chart;
	}
}

