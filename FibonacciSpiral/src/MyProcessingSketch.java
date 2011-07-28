import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import processing.core.*;
import processing.video.*;

public class MyProcessingSketch extends PApplet {

	private static final long serialVersionUID = 1854372891164114958L;
	private Iterator<Point> pointsInteratorA;
	private Iterator<Point> pointsInteratorB;
	private Iterator<Point> pointsInteratorC;
	private Iterator<Point> pointsInteratorD;

	private int[] colors = {0, 0, 15, 39, 54, 67, 83, 119, 136, 154, 169};
	
	private MovieMaker mm;

	public void setup() {
		size(1280, 720, P2D);
		mm = new MovieMaker(this, width, height, "../drawing.mov",
                30, MovieMaker.ANIMATION, MovieMaker.HIGH);
		
		background(0);

		colorMode(HSB, 360, 100, 100);

		stroke(255);

		pointsInteratorA = generatePoints(0).iterator();
		pointsInteratorB = generatePoints(1).iterator();
		pointsInteratorC = generatePoints(2).iterator();
		pointsInteratorD = generatePoints(3).iterator();
	}

	private List<Point> generatePoints(int i) {
		int initialRadius = 10;
		int cx = getWidth() / 2;
		int cy = getHeight() / 2;
		int lastFib = 1;
		List<Integer> fibs = generateFibs();
		List<Point> points = new ArrayList<MyProcessingSketch.Point>();
		int[][] mods = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
		int j = 0;
		for (int fib : fibs) {
			cx = (initialRadius * (mods[i % 4][0] * (fib - lastFib))) + cx;
			cy = (initialRadius * (mods[i % 4][1] * (fib - lastFib))) + cy;
			points.addAll(myCircle(cx, cy, initialRadius * fib, (i % 4) + 1, colors[j]));
			lastFib = fib;
			i++;
			j++;
		}
		return points;
	}

	public void draw() {
		if (frameCount < 50) {
			return;
		}
		// if (mousePressed) {
		// line(mouseX,mouseY,pmouseX,pmouseY);
		// }
		if (pointsInteratorA.hasNext()) {
			Point p = pointsInteratorA.next();
			stroke(p.color, 100, 100);
			point(p.x, p.y);
		}
		if (pointsInteratorB.hasNext()) {
			Point p = pointsInteratorB.next();
			point(p.x, p.y);
		}
		if (pointsInteratorC.hasNext()) {
			Point p = pointsInteratorC.next();
			point(p.x, p.y);
		}
		if (pointsInteratorD.hasNext()) {
			Point p = pointsInteratorD.next();
			point(p.x, p.y);
			mm.addFrame();
		} else {
			mm.finish();
			println("finished");
		}
	}

	public List<Integer> generateFibs() {
		int fn1 = 1, fn2 = 1;
		List<Integer> fibs = new ArrayList<Integer>();
		fibs.add(fn1);
		fibs.add(fn2);

		for (int i = 1; i <= 8; i++) {
			int t = fn1;
			fn1 = fn2;
			fn2 = t + fn1;
			fibs.add(fn2);
		}
		return fibs;
	}

	/**
	 * 
	 * 1 2 O 4 3
	 * 
	 * @return
	 */
	public List<Point> myCircle(int x0, int y0, int radius, int corner, int color) {
		int f = 1 - radius;
		int ddF_x = 1;
		int ddF_y = -2 * radius;
		int x = 0;
		int y = radius;

		List<Point> firstPoints = new ArrayList<MyProcessingSketch.Point>();
		List<Point> secondPoints = new ArrayList<MyProcessingSketch.Point>();

		while (x < y) {
			if (f >= 0) {
				y--;
				ddF_y += 2;
				f += ddF_y;
			}
			x++;
			ddF_x += 2;
			f += ddF_x;

			if (corner == 1) {
				secondPoints.add(new Point(x0 - x, y0 - y, color));
				firstPoints.add(new Point(x0 - y, y0 - x, color));
			} else if (corner == 2) {
				firstPoints.add(new Point(x0 + x, y0 - y, color));
				secondPoints.add(new Point(x0 + y, y0 - x, color));
			} else if (corner == 3) {
				secondPoints.add(new Point(x0 + x, y0 + y, color));
				firstPoints.add(new Point(x0 + y, y0 + x, color));
			} else if (corner == 4) {
				firstPoints.add(new Point(x0 - x, y0 + y, color));
				secondPoints.add(new Point(x0 - y, y0 + x, color));
			}
		}

		Collections.reverse(secondPoints);

		firstPoints.addAll(secondPoints);

		if (corner == 1) {
			firstPoints.add(0, new Point(x0 - radius, y0, color));
			firstPoints.add(new Point(x0, y0 - radius, color));
		} else if (corner == 2) {
			firstPoints.add(0, new Point(x0, y0 - radius, color));
			firstPoints.add(new Point(x0 + radius, y0, color));
		} else if (corner == 3) {
			firstPoints.add(0, new Point(x0 + radius, y0, color));
			firstPoints.add(new Point(x0, y0 + radius, color));
		} else if (corner == 4) {
			firstPoints.add(0, new Point(x0, y0 + radius, color));
			firstPoints.add(new Point(x0 - radius, y0, color));
		}

		return firstPoints;
	}

	class Point {
		int x, y, color;

		public Point(int x, int y, int color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
	}
}
