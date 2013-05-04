package ru.ifmo.avt.browser.GUI.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import ru.ifmo.avt.browser.interfaces.Browserable;

public class PaintComponent extends JComponent {
    private static final long serialVersionUID = 939473172725933313L;

    public PaintComponent(Browserable browserableObject) {
	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent event) {
		Point point = event.getPoint();

		for (Point2D[] points : pointsList) {
		    for (int i = 0; i < points.length; i++) {
			double x = points[i].getX() - SIZE / 2;
			double y = points[i].getY() - SIZE / 2;
			Rectangle2D shape = new Rectangle2D.Double(x, y, SIZE, SIZE);
			if (shape.contains(point)) {
			    current = i;
			    currentShape = points;
			    return;
			}
		    }
		    if (currentShape != null)
			return;
		}
	    }

	    @Override
	    public void mouseReleased(MouseEvent event) {
		current = -1;
		currentShape = null;
	    }
	});

	addMouseMotionListener(new MouseMotionAdapter() {
	    @Override
	    public void mouseDragged(MouseEvent event) {
		if (current == -1)
		    return;

		Point point = event.getPoint();

		if (point.getX() > 0 && point.getY() > 0) {
		    currentShape[current] = event.getPoint();
		    repaint();
		}
	    }
	});

	current = -1;
	currentShape = null;

	setShapeMaker(browserableObject);
    }

    public void setShapeMaker(Browserable browserableObject) {
	this.browserableObject = browserableObject;
	pointsList.add(browserableObject.getPeak());
	repaint();

	if (browserableObject.getBrowserableObjects() != null)
	    for (Browserable object : browserableObject.getBrowserableObjects())
		setShapeMaker(object);
    }

    @Override
    protected void paintComponent(Graphics g) {
	if (pointsList == null)
	    return;

	Graphics2D g2 = (Graphics2D) g;

	for (Point2D[] points : pointsList) {
	    for (int i = 0; i < points.length; i++) {
		double x = points[i].getX() - SIZE / 2;
		double y = points[i].getY() - SIZE / 2;
		g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
	    }

	    g2.draw(browserableObject.makeShape(points));
	}
    }

    private List<Point2D[]> pointsList = new LinkedList<Point2D[]>();;
    private static int SIZE = 4;
    private int current;
    private Point2D[] currentShape;
    private Browserable browserableObject;
}
