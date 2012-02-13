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



import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.TableOrder;

public class MySpiderWebPlot extends SpiderWebPlot {

	private static final long serialVersionUID = 1L;
	
    public MySpiderWebPlot(CategoryDataset dataset) {
        super(dataset);
    }
    
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
        // adjust for insets...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        if (info != null) {
            info.setPlotArea(area);
            info.setDataArea(area);
        }

        drawBackground(g2, area);
        drawOutline(g2, area);

        Shape savedClip = g2.getClip();

        g2.clip(area);
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                getForegroundAlpha()));

        if (!DatasetUtilities.isEmptyOrNull(this.getDataset())) {
            int seriesCount = 0, catCount = 0;

            if (this.getDataExtractOrder() == TableOrder.BY_ROW) {
                seriesCount = this.getDataset().getRowCount();
                catCount = this.getDataset().getColumnCount();
            }
            else {
                seriesCount = this.getDataset().getColumnCount();
                catCount = this.getDataset().getRowCount();
            }

            // ensure we have a maximum value to use on the axes
            if (this.getMaxValue() == DEFAULT_MAX_VALUE)
                calculateMaxValue(seriesCount, catCount);

            // Next, setup the plot area

            // adjust the plot area by the interior spacing value

            double gapHorizontal = area.getWidth() * getInteriorGap();
            double gapVertical = area.getHeight() * getInteriorGap();

            double X = area.getX() + gapHorizontal / 2;
            double Y = area.getY() + gapVertical / 2;
            double W = area.getWidth() - gapHorizontal;
            double H = area.getHeight() - gapVertical;

            double headW = area.getWidth() * this.headPercent;
            double headH = area.getHeight() * this.headPercent;

            // make the chart area a square
            double min = Math.min(W, H) / 2;
            X = (X + X + W) / 2 - min;
            Y = (Y + Y + H) / 2 - min;
            W = 2 * min;
            H = 2 * min;

            Point2D  centre = new Point2D.Double(X + W / 2, Y + H / 2);
            Rectangle2D radarArea = new Rectangle2D.Double(X, Y, W, H);

            // draw the axis and category label
            for (int cat = 0; cat < catCount; cat++) {
                double angle = getStartAngle()
                        + (getDirection().getFactor() * cat * 360 / catCount);

                Point2D endPoint = getWebPoint(radarArea, angle, 1);
                                                     // 1 = end of axis
                Line2D  line = new Line2D.Double(centre, endPoint);
                g2.setPaint(this.getAxisLinePaint());
                g2.setStroke(this.getAxisLineStroke());
                g2.draw(line);
                drawLabel(g2, radarArea, 0.0, cat, angle, 360.0 / catCount);
            }
            
            // Plot gridlines
            for (int i = 0; i <= getMaxValue(); i++) {
            	drawRadarGridLines(g2, radarArea, Double.valueOf(i), catCount, headH, headW);
            }

            // Now actually plot each of the series polygons..
            for (int series = 0; series < seriesCount; series++) {
                drawRadarPoly(g2, radarArea, centre, info, series, catCount, headH, headW);
            }
        }
        else {
            drawNoDataMessage(g2, area);
        }
        g2.setClip(savedClip);
        g2.setComposite(originalComposite);
        drawOutline(g2, area);
    }
    
    /**
     * loop through each of the series to get the maximum value
     * on each category axis
     *
     * @param seriesCount  the number of series
     * @param catCount  the number of categories
     */
    private void calculateMaxValue(int seriesCount, int catCount) {
        double v = 0;
        Number nV = null;

        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
            for (int catIndex = 0; catIndex < catCount; catIndex++) {
                nV = getPlotValue(seriesIndex, catIndex);
                if (nV != null) {
                    v = nV.doubleValue();
                    if (v > this.getMaxValue()) {
                        this.setMaxValue(v);
                    }
                }
            }
        }
    }    

    @Override
    protected void drawRadarPoly(Graphics2D g2, Rectangle2D plotArea, Point2D centre, PlotRenderingInfo info, int series, int catCount, double headH, double headW) {
        
    	Polygon polygon = new Polygon();
        
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        
        // plot the data...
        for (int cat = 0; cat < catCount; cat++) {

            Number dataValue = getPlotValue(series, cat);

            if (dataValue != null) {
                double value = dataValue.doubleValue();

                if (value >= 0) { // draw the polygon series...

                    // Finds our starting angle from the centre for this axis

                    double angle = getStartAngle() + (getDirection().getFactor() * cat * 360 / catCount);

                    // The following angle calc will ensure there isn't a top
                    // vertical axis - this may be useful if you don't want any
                    // given criteria to 'appear' move important than the
                    // others..
                    //  + (getDirection().getFactor()
                    //        * (cat + 0.5) * 360 / catCount);

                    // find the point at the appropriate distance end point
                    // along the axis/angle identified above and add it to the
                    // polygon

                    Point2D point = getWebPoint(plotArea, angle, value / this.getMaxValue());
                    polygon.addPoint((int) point.getX(), (int) point.getY());

                    // put an elipse at the point being plotted..

                    Paint paint = getSeriesPaint(series);
                    Paint outlinePaint = getSeriesOutlinePaint(series);
                    Stroke outlineStroke = getSeriesOutlineStroke(series);

                    Ellipse2D head = new Ellipse2D.Double(point.getX() - headW / 2, point.getY() - headH / 2, headW, headH);
                    g2.setPaint(paint);
                    g2.fill(head);
                    g2.setStroke(outlineStroke);
                    g2.setPaint(outlinePaint);
                    g2.draw(head);

                    if (entities != null) {
                        int row = 0; int col = 0;
                        if (this.getDataExtractOrder() == TableOrder.BY_ROW) {
                            row = series;
                            col = cat;
                        }
                        else {
                            row = cat;
                            col = series;
                        }
                        String tip = null;
                        if (this.getToolTipGenerator() != null) {
                            tip = this.getToolTipGenerator().generateToolTip(this.getDataset(), row, col);
                        }

                        String url = null;
                        if (this.getURLGenerator() != null) {
                            url = this.getURLGenerator().generateURL(this.getDataset(), row, col);
                        }

                        Shape area = new Rectangle(
                                (int) (point.getX() - headW),
                                (int) (point.getY() - headH),
                                (int) (headW * 2), (int) (headH * 2));
                        CategoryItemEntity entity = new CategoryItemEntity(
                                area, tip, url, this.getDataset(),
                                this.getDataset().getRowKey(row),
                                this.getDataset().getColumnKey(col));
                        entities.add(entity);
                    }

                }
            }
        }
        // Plot the polygon

        Paint paint = getSeriesPaint(series);
        g2.setPaint(paint);
        g2.setStroke(getSeriesOutlineStroke(series));
        g2.draw(polygon);

        // Lastly, fill the web polygon if this is required

        if (this.isWebFilled()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            g2.fill(polygon);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getForegroundAlpha()));
        }
    }
    
    protected void drawRadarGridLines(Graphics2D g2, Rectangle2D plotArea, double value, int catCount, double headH, double headW) {
        
    	Polygon polygon = new Polygon();
        DecimalFormat formatter = new DecimalFormat("#0.#");

        // Para cada uma das categorias...
        for (int cat = 0; cat < catCount; cat++) {

            double angle = getStartAngle() + (getDirection().getFactor() * cat * 360 / catCount);

            Point2D point = getWebPoint(plotArea, angle, value / this.getMaxValue());
            polygon.addPoint((int) point.getX(), (int) point.getY());

            g2.setPaint(Color.BLACK);
            g2.setPaint(Color.BLACK);
            
            // Imprime os valores do gráfico (somente em um vértice)
            if (cat == 0) {
	            double labelLocationX = point.getX() - headW / 2 - 2;
	            double labelLocationY = point.getY() - headH / 2 - 5;
	            g2.drawString(formatter.format(value), (float) labelLocationX, (float) labelLocationY);
            }
        }
        
        // Desenha as linhas do grid.
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(.275f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, new float[] {3f, 7f}, 0.0f)); // Tracejado
        g2.draw(polygon);
    }    

}
