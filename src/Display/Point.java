package Display;

public class Point {
	public double x;
	public double y;
	public double alpha;
	
	public Point() {
		x = 0;
		y = 0;
		alpha = 1.0;
	}
	
	public Point(double a, double b) {
		x = a;
		y = b;
		alpha = 1.0;
	}
	public Point(double a, double b, double c) {
		x = a;
		y = b;
		alpha = c;
	}
}
