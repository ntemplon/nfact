/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package graph;

import com.jupiter.ganymede.math.function.Function.FuncPoint;
import com.jupiter.ganymede.math.function.SingleVariableRealFunction;
import static util.Util.doubleEquals;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author Nathan Templon
 */
public class Plot2D extends JPanel {

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Enumerations -------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    public enum FunctionType {

        POINT,
        LINE,
        POINT_AND_LINE
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  State Variables ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    private final Collection<Collection<FuncPoint>> pointCollections;
    private final Collection<SingleVariableRealFunction> functions;
    private final Map<SingleVariableRealFunction, Collection<FuncPoint>> pointFuncMap;
    private final Map<SingleVariableRealFunction, ArrayList<FuncPoint>> lineFuncMap;

    private final DecimalFormat format;

    private Color lineColor = Color.BLACK;
    private Color pointColor = Color.BLACK;

    private final int width = 800;
    private final int height = 400;
    private int padding = 10;
    private int leftLabelPadding = 50;
    private int bottomLabelPadding = 25;
    private int rightSidePadding = 5;
    private int topPadding = 15;
    private int graphPixelWidth;
    private int graphPixelHeight;
    private int smallFontSize;
    private int bigFontSize;
    private int titleFontSize;

    private Font standardFont;
    private Font boldFont;
    private Font bigFont;
    private Font titleFont;

    private FontMetrics standardMetrics;
    private FontMetrics boldMetrics;
    private FontMetrics bigMetrics;
    private FontMetrics titleMetrics;

    private final Color gridColor = new Color(200, 200, 200, 200);

    private double xMin = -20.0;
    private double xMax = 30;
    private double yMin = -0.8;
    private double yMax = 1.6;
    private int xOfYAxis;
    private int yOfXAxis;

    private double scrollOutSens = 0.05;
    private double scrollInSens = 1.0 - (1.0 / (1.0 + scrollOutSens));

    private String xAxisTitle = "Horizontal Axis";
    private String yAxisTitle = "Vertical Axis";
    private String title = "TITLE";

    private double xWidth;
    private double yWidth;

    private double pixelsPerUnitX;
    private double pixelsPerUnitY;

    private int numXGridlines = 5;
    private int numYGridlines = 5;

    private int xTicksPerGridline = 5;
    private int yTicksPerGridline = 5;
    private int tickSize = 5;

    private int leftX;
    private int rightX;
    private int topY;
    private int bottomY;

    private int pointRadius = 4;

    private int numFunctionPoints = 15;

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    /**
     * The default constructor, which creates a new, blank Plot2D
     */
    public Plot2D() {
        format = new DecimalFormat("0.0000E0");

        pointCollections = new ArrayList<>();
        functions = new ArrayList<>();
        pointFuncMap = new HashMap<>();
        lineFuncMap = new HashMap<>();

        GraphicsMouseAdapter adapt = new GraphicsMouseAdapter();
        this.addMouseListener(adapt);
        this.addMouseWheelListener(adapt);
        this.addMouseMotionListener(adapt);
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Painting Methods  --------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    /**
     * Paints the component
     *
     * @param g the graphics object that can paint this component
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Call the super paintComponent
        super.paintComponent(g);

        // Calculate paddings and such
        calcMetrics();

        // Customize and configure our graphics object
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Instantiate our fonts and font metrics
        initFonts(graphics);
        graphics.setFont(standardFont);

        // Draw a white background
        drawBackground(graphics);

        // Prepare to draw grid lines
        graphics.setColor(gridColor);

        // Draw x grid lines
        // TODO - change to move with X and Y axis while panning
        double xGridlineStep = ((double) graphPixelWidth) / ((double) (numXGridlines + 1));
        for (int index = 1; index <= numXGridlines; index++) {
            int gridlineX = leftX + (int) Math.round(xGridlineStep * index);
            graphics.drawLine(gridlineX, bottomY, gridlineX, topY);
        }

        // Draw y grid lines
        double yGridlineStep = ((double) graphPixelHeight) / ((double) (numYGridlines + 1));
        for (int index = 1; index <= numYGridlines; index++) {
            int gridlineY = topY + (int) Math.round(yGridlineStep * index);
            graphics.drawLine(leftX, gridlineY, rightX, gridlineY);
        }

        // Return the graphics to normal colors
        graphics.setColor(Color.BLACK);

        // Create x and y axes 
        drawAxes(graphics);

        // Set font to bold
        graphics.setFont(boldFont);

        // Draw x labels
        int numXLabels = numXGridlines + 2;
        double xLabelStep = ((double) graphPixelWidth) / ((double) (numXLabels - 1));
        for (int index = 0; index < numXLabels; index++) {
            int labelPos = leftX + (int) Math.round(xLabelStep * index);
            double xValueAtLine = ((index * xLabelStep) / pixelsPerUnitX) + xMin;
            String xLabel = formatedForLabel(xValueAtLine);
            int labelWidth = boldMetrics.stringWidth(xLabel);
            graphics.drawString(xLabel, labelPos - labelWidth / 2, bottomY + boldMetrics.getHeight() + 3);
        }

        // Set the font for the axis title
        graphics.setFont(bigFont);

        //Draw x Axis Title
        int centerX = (int) Math.round(((double) (rightX - leftX)) / 2.0) + leftX;
        int xTitleLength = bigMetrics.stringWidth(xAxisTitle);
        graphics.drawString(xAxisTitle, centerX - xTitleLength / 2, bottomY + bigMetrics.getHeight() + boldMetrics.getHeight() + 5);

        // Set font to bold
        graphics.setFont(boldFont);

        // Draw y labels
        int numYLabels = numYGridlines + 2;
        double yLabelStep = ((double) graphPixelHeight) / ((double) (numYLabels - 1));
        for (int index = 0; index < numYLabels; index++) {
            int labelPos = bottomY - (int) Math.round(yLabelStep * index);
            double yValueAtLine = ((index * yLabelStep) / pixelsPerUnitY) + yMin;
            String yLabel = formatedForLabel(yValueAtLine);
            int labelWidth = boldMetrics.stringWidth(yLabel);
            graphics.drawString(yLabel, leftX - labelWidth - 5, labelPos + (boldMetrics.getHeight() / 2) - 3);
        }

        // Set font to big for the axis titles
        graphics.setFont(bigFont);

        // Draw y Axis Title
        int centerY = (int) Math.round(((double) (bottomY - topY)) / 2.0) + topY;
        int yTitleLength = standardMetrics.stringWidth(yAxisTitle);
        AffineTransform origTrans = graphics.getTransform();
        graphics.rotate(-1 * Math.PI / 2.0);
        graphics.drawString(yAxisTitle, -1 * centerY - (yTitleLength / 2), leftX - boldMetrics.stringWidth("0.0000E0") - 15);
        graphics.setTransform(origTrans);

        // Draw the graph's title
        titleMetrics = graphics.getFontMetrics(titleFont);
        graphics.setFont(titleFont);
        graphics.drawString(title, centerX - titleMetrics.stringWidth(title) / 2, titleMetrics.getHeight());

        // Return the font to normal
        graphics.setFont(standardFont);

        // Draw x tick marks
        int numXTicks = ((numXGridlines + 1) * xTicksPerGridline) - 1;
        double xTickStep = ((double) graphPixelWidth) / ((double) (numXTicks + 1));
        for (int index = 1; index <= numXTicks; index++) {
            int tickX = leftX + (int) Math.round(xTickStep * index);
            graphics.drawLine(tickX, yOfXAxis, tickX, yOfXAxis - tickSize);
        }

        // Draw y tick marks
        int numYTicks = ((numYGridlines + 1) * yTicksPerGridline) - 1;
        double yTickStep = ((double) graphPixelHeight) / ((double) (numYTicks + 1));
        for (int index = 1; index <= numYTicks; index++) {
            int tickY = topY + (int) Math.round(yTickStep * index);
            graphics.drawLine(xOfYAxis, tickY, xOfYAxis + tickSize, tickY);
        }

        // Draw the points
        Color oldColor = graphics.getColor();
        graphics.setColor(this.pointColor);
        int shiftToCenter = (int) ((double) pointRadius / 2.0);
        for (Collection<FuncPoint> points : pointCollections) {
            for (FuncPoint point : points) {
                if (point.x > xMax || point.y > yMax || point.x < xMin || point.y < yMin) {
                    continue;
                }

                double relX = point.x - xMin;
                double relY = point.y - yMin;

                int pointX = leftX + (int) Math.round(pixelsPerUnitX * relX) - shiftToCenter;
                int pointY = bottomY - (int) Math.round(pixelsPerUnitY * relY) - shiftToCenter;

                graphics.fillOval(pointX, pointY, pointRadius, pointRadius);
            }
        }

        // Draw the point functions
        for (SingleVariableRealFunction func : pointFuncMap.keySet()) {
            Collection<FuncPoint> points = pointFuncMap.get(func);
            for (FuncPoint point : points) {
                if (point.x > xMax || point.y > yMax || point.x < xMin || point.y < yMin) {
                    continue;
                }

                double relX = point.x - xMin;
                double relY = point.y - yMin;

                int pointX = leftX + (int) Math.round(pixelsPerUnitX * relX) - shiftToCenter;
                int pointY = bottomY - (int) Math.round(pixelsPerUnitY * relY) - shiftToCenter;

                graphics.fillOval(pointX, pointY, pointRadius, pointRadius);
            }
        }

        // Draw the line functions
        graphics.setColor(lineColor);
        for (SingleVariableRealFunction func : lineFuncMap.keySet()) {
            ArrayList<FuncPoint> points = lineFuncMap.get(func);
            int index = 0;
            for (FuncPoint point : points) {

                if (index >= points.size() - 1) {
                    index++;
                    continue;
                }

                FuncPoint point2 = points.get(index + 1);

                if (point.x > xMax || point.y > yMax || point.x < xMin || point.y < yMin) {
                    if (!(point2.x > xMax || point2.y > yMax || point2.x < xMin || point2.y < yMin)) {
                        // Draw line from this point to point2, but only the part that is inside of the graph
                        // This point is not on the graph.
                        double slope = (point2.y - point.y) / (point2.x - point.x);
                        double xAtYMax = point.x + ((yMax - point.y) / slope);

                        // Then the line from this point to point 2 hits the top of the graph
                        if (point.x < xAtYMax && point2.x > xAtYMax) {
                            int pointX = leftX + (int) Math.round(pixelsPerUnitX * (xAtYMax - xMin));
                            int pointY = topY;

                            double relX2 = point2.x - xMin;
                            double relY2 = point2.y - yMin;
                            int pointX2 = leftX + (int) Math.round(pixelsPerUnitX * relX2);
                            int pointY2 = bottomY - (int) Math.round(pixelsPerUnitY * relY2);

                            graphics.drawLine(pointX, pointY, pointX2, pointY2);
                        }
                    }
                    index++;
                    continue;
                }

                else if (point2.x > xMax || point2.y > yMax || point2.x < xMin || point2.y < yMin) {
                    // Draw a line from this point to point2, but only the part that is inside of the graph.
                    // This point is on the graph.
                    index++;
                    continue;
                }

                double relX = point.x - xMin;
                double relY = point.y - yMin;
                double relX2 = point2.x - xMin;
                double relY2 = point2.y - yMin;

                int pointX = leftX + (int) Math.round(pixelsPerUnitX * relX);
                int pointY = bottomY - (int) Math.round(pixelsPerUnitY * relY);
                int pointX2 = leftX + (int) Math.round(pixelsPerUnitX * relX2);
                int pointY2 = bottomY - (int) Math.round(pixelsPerUnitY * relY2);

                graphics.drawLine(pointX, pointY, pointX2, pointY2);
                index++;
            }
        }

        graphics.setColor(oldColor);
    }

    /**
     * Gives the component's preferred size
     *
     * @return Returns the component's preferred size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Painting Helpers  --------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    private String formatedForLabel(double d) {
        return format.format(d);
    }

    private void calcMetrics() {
        // Calculate font sizes
        bigFontSize = (int) (getWidth() / 100.0) + 4;
        smallFontSize = (int) (bigFontSize * 0.2) + 8;
        titleFontSize = (int) (1.2 * bigFontSize);

        // Calculate padding
        padding = 15 + (int) (getWidth() * 0.004);
        rightSidePadding = 5 + (int) (getWidth() * 0.001);
        bottomLabelPadding = (int) (1.4 * bigFontSize) + 10;
        leftLabelPadding = (int) (1.05 * bigFontSize) + 45;
        topPadding = (int) (1.9 * titleFontSize);

        // Calculate the position of the four corners
        leftX = padding + leftLabelPadding;
        rightX = getWidth() - (padding + rightSidePadding);
        bottomY = getHeight() - (padding + bottomLabelPadding);
        topY = topPadding;

        // Calculate the height of the graph in pixels
        graphPixelWidth = rightX - leftX;
        graphPixelHeight = bottomY - topY;

        // Calculate the width and height of the graph in units
        xWidth = xMax - xMin;
        yWidth = yMax - yMin;

        // Calculate how many pixels are in one unit of each scales
        pixelsPerUnitX = ((double) graphPixelWidth) / xWidth;
        pixelsPerUnitY = ((double) graphPixelHeight) / yWidth;

        // Decide how large to make the tick marks
        tickSize = (int) Math.round((double) Math.min(graphPixelWidth, graphPixelHeight) * 0.01);
    }

    private void initFonts(Graphics2D graphics) {
        standardFont = new Font("Default", Font.PLAIN, smallFontSize);
        boldFont = standardFont.deriveFont(Font.BOLD);
        bigFont = new Font(standardFont.getFontName(), Font.PLAIN, bigFontSize);
        titleFont = new Font(standardFont.getFontName(), Font.BOLD, titleFontSize);

        standardMetrics = graphics.getFontMetrics(standardFont);
        boldMetrics = graphics.getFontMetrics(boldFont);
        bigMetrics = graphics.getFontMetrics(bigFont);
        titleMetrics = graphics.getFontMetrics(titleFont);
    }

    private void drawBackground(Graphics2D graphics) {
        Color oldColor = graphics.getColor();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(leftX, topY, graphPixelWidth, graphPixelHeight);
        graphics.setColor(oldColor);
    }

    private void drawAxes(Graphics2D graphics) {
        // create x and y axes 
        xOfYAxis = leftX + Math.max(0, (int) Math.round(pixelsPerUnitX * -1 * xMin));
        xOfYAxis = Math.min(xOfYAxis, rightX);

        yOfXAxis = bottomY - Math.max(0, (int) Math.round(pixelsPerUnitY * -1 * yMin));
        yOfXAxis = Math.max(yOfXAxis, topY);

        Stroke oldStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.drawLine(xOfYAxis, bottomY, xOfYAxis, topY);
        graphics.drawLine(leftX, yOfXAxis, rightX, yOfXAxis);
        graphics.setStroke(oldStroke);
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Function/Point Assigments ------------------------------------------
    // -------------------------------------------------------------------------------------------------
    public void addPoints(Collection<FuncPoint> points) {
        this.pointCollections.add(points);
    }

    public void addFunction(SingleVariableRealFunction func, FunctionType type) {
        functions.add(func);

        ArrayList<FuncPoint> points = new ArrayList<>();

        // Calculate the minimum and maximum values to display
        double maxValue;
        double minValue;
//        if (func.hasFiniteDomain()) {
//            maxValue = Math.min(this.xMax, func.domainMax);
//            minValue = Math.max(this.xMin, func.domainMin);
//        }
//        else {
            maxValue = this.xMax;
            minValue = this.xMin;
//        }

        double step = (maxValue - minValue) / ((double) numFunctionPoints);
        double index = minValue;
        while (index < maxValue) {
            double funcValue = func.evaluate(index);
            points.add(new FuncPoint(index, funcValue));
            index = index + step;
        }
        // Get the last point, from when the loop above exists
        points.add(new FuncPoint(index, func.evaluate(maxValue)));

        if (type == FunctionType.POINT) {
            pointFuncMap.put(func, points);
        }
        else if (type == FunctionType.LINE) {
            System.out.println("Putting!");
            lineFuncMap.put(func, points);
        }
        else if (type == FunctionType.POINT_AND_LINE) {
            pointFuncMap.put(func, points);
            lineFuncMap.put(func, points);
        }
    }

    public void updateFunctions() {
        Map<SingleVariableRealFunction, Collection<FuncPoint>> updatedPoints = new HashMap<>();
        for (SingleVariableRealFunction func : pointFuncMap.keySet()) {
            ArrayList<FuncPoint> points = new ArrayList<>();

            double max;
            double min;
//            if (func.hasFiniteDomain) {
//                max = Math.min(this.xMax, func.domainMax);
//                min = Math.max(this.xMin, func.domainMin);
//            }
//            else {
                max = this.xMax;
                min = this.xMin;
//            }

            double step = (max - min) / ((double) (numFunctionPoints - 1.0));
            double val = min;

            if (doubleEquals(min, max) || min > max) {
                continue;
            }

            boolean drawnLastPoint = false;
            while (!drawnLastPoint) {
                if (val > max) {
                    val = max;
                    drawnLastPoint = true;
                }
                double funcValue = func.evaluate(val);
                points.add(new FuncPoint(val, funcValue));
                val += step;
            }
            updatedPoints.put(func, points);
//            pointFuncMap.remove(func);
//            pointFuncMap.put(func, points);
        }
        for (SingleVariableRealFunction func : updatedPoints.keySet()) {
            pointFuncMap.remove(func);
            pointFuncMap.put(func, updatedPoints.get(func));
        }

        Map<SingleVariableRealFunction, ArrayList<FuncPoint>> updatedLines = new HashMap<>();
        for (SingleVariableRealFunction func : lineFuncMap.keySet()) {
            ArrayList<FuncPoint> points = new ArrayList<>();

            double max;
            double min;
//            if (func.hasFiniteDomain) {
//                max = Math.min(this.xMax, func.domainMax);
//                min = Math.max(this.xMin, func.domainMin);
//            }
//            else {
                max = this.xMax;
                min = this.xMin;
//            }

            double step = (max - min) / ((double) (numFunctionPoints - 1));
            double val = min;

            if (doubleEquals(min, max) || min > max) {
                continue;
            }

            boolean drawnLastPoint = false;
            while (!drawnLastPoint) {
                if (val > max) {
                    val = max;
                    drawnLastPoint = true;
                }
                double funcValue = func.evaluate(val);
                points.add(new FuncPoint(val, funcValue));
                val += step;
            }
            updatedLines.put(func, points);
        }
        for (SingleVariableRealFunction func : updatedLines.keySet()) {
            lineFuncMap.remove(func);
            lineFuncMap.put(func, updatedLines.get(func));
        }
    }

    public void addFunction(SingleVariableRealFunction func) {
        addFunction(func, FunctionType.POINT);
    }

    public void removeFunction(SingleVariableRealFunction func) {
        pointFuncMap.remove(func);
    }

    public void removePoints(Collection<FuncPoint> points) {
        if (pointCollections.contains(points)) {
            this.pointCollections.remove(points);
        }
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Property Getters/Setters -------------------------------------------
    // -------------------------------------------------------------------------------------------------
    public void setDomain(double xMin, double xMax) {
        this.xMin = xMin;
        this.xMax = xMax;
    }

    public double xMin() {
        return this.xMin;
    }

    public double xMax() {
        return this.xMax;
    }

    public void setRange(double yMin, double yMax) {
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public double yMin() {
        return this.yMin;
    }

    public double yMax() {
        return this.yMax;
    }

    public void setXTicksPerGridline(int ticks) {
        this.xTicksPerGridline = ticks;
    }

    public int getXTicksPerGridline() {
        return xTicksPerGridline;
    }

    public void setYTicksPerGridline(int ticks) {
        this.yTicksPerGridline = ticks;
    }

    public int getYTicksPerGridline() {
        return yTicksPerGridline;
    }

    public void setPointRadius(int r) {
        this.pointRadius = r;
    }

    public int getPointRadius() {
        return pointRadius;
    }

    public void setNumFuncPoints(int points) {
        this.numFunctionPoints = points;
        updateFunctions();
    }

    public int getNumFuncPoints() {
        return numFunctionPoints;
    }

    public void setXAxisTitle(String title) {
        this.xAxisTitle = title;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setYAxisTitle(String title) {
        this.yAxisTitle = title;
    }

    public String getYAxisTitle() {
        return yAxisTitle;
    }

    public void setLineColor(Color color) {
        this.lineColor = color;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setPointColor(Color color) {
        this.pointColor = color;
    }

    public Color getPointColor() {
        return pointColor;
    }

    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Adapters and Auxilary GUIs -----------------------------------------
    // -------------------------------------------------------------------------------------------------
    class GraphRightClickMenu extends JPopupMenu {

        JMenuItem item1;
        JMenuItem item2;

        public GraphRightClickMenu() {
            item1 = new JMenuItem("Item 1");
            item2 = new JMenuItem("Item 2");
            add(item1);
            add(item2);
        }
    }

    class GraphicsMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (!perfOper && shouldShowMenu(e)) {
                doPopupMenu(e);
            }
            else if (!perfOper && isCenterClick(e)) {
                startPan(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!perfOper && shouldShowMenu(e)) {
                doPopupMenu(e);
            }
            else if (isCenterClick(e)) {
                endPan(e);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (panning) {
                int diffX = e.getX() - panStartX;
                int diffY = e.getY() - panStartY;

                double xShift = -1.0 * ((double) diffX) / pixelsPerUnitX;
                double yShift = ((double) diffY) / pixelsPerUnitY;

                xMin = panStartXMin + xShift;
                xMax = panStartXMax + xShift;
                yMin = panStartYMin + yShift;
                yMax = panStartYMax + yShift;

                updateFunctions();
                repaint();
            }
        }

        private void doPopupMenu(MouseEvent e) {
            GraphRightClickMenu menu = new GraphRightClickMenu();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

        private boolean shouldShowMenu(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                int x = e.getX();
                int y = e.getY();
                return (x > leftX && x < rightX && y < bottomY && y > topY);
            }
            return false;
        }

        private boolean isCenterClick(MouseEvent e) {
            return e.getButton() == MouseEvent.BUTTON2;
        }

        private void startPan(MouseEvent e) {
            perfOper = true;
            panning = true;
            panStartX = e.getX();
            panStartY = e.getY();
            panStartXMin = xMin;
            panStartXMax = xMax;
            panStartYMin = yMin;
            panStartYMax = yMax;
        }

        private void endPan(MouseEvent e) {
            panning = false;
            perfOper = false;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();

            double centerX = (xMax + xMin) / 2.0;
            double halfWidth = xMax - centerX;

            double centerY = (yMax + yMin) / 2.0;
            double halfHeight = yMax - centerY;

            if (notches > 0) {
                xMin = centerX - ((1.0 + (scrollOutSens * notches)) * halfWidth);
                xMax = centerX + ((1.0 + (scrollOutSens * notches)) * halfWidth);
                yMin = centerY - ((1.0 + (scrollOutSens * notches)) * halfHeight);
                yMax = centerY + ((1.0 + (scrollOutSens * notches)) * halfHeight);
            }
            else {
                xMin = centerX - ((1.0 + (scrollInSens * notches)) * halfWidth);
                xMax = centerX + ((1.0 + (scrollInSens * notches)) * halfWidth);
                yMin = centerY - ((1.0 + (scrollInSens * notches)) * halfHeight);
                yMax = centerY + ((1.0 + (scrollInSens * notches)) * halfHeight);
            }
            updateFunctions();
            repaint();
        }

        private boolean panning;
        private int panStartX;
        private int panStartY;
        private double panStartXMin;
        private double panStartXMax;
        private double panStartYMin;
        private double panStartYMax;

        private boolean perfOper = false; // Stores if we are currently performing an operation, such as zooming or panning
    }
}
