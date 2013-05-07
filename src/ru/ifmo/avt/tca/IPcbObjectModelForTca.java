package ru.ifmo.avt.tca;

import java.awt.Point;

public interface IPcbObjectModelForTca {
	public double getThermalConduct();
	
	public double getWidth();
	
	public double getHeight();
	
	public double getDepth();
	
	public Point getLocation();
}
