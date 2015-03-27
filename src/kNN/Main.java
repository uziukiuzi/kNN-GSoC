package kNN;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JApplet;

public class Main extends JApplet{

	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	public static final int k = 10;
	public static final int NUM_SEEDS = 100;
	//public static final int n = (int) (Math.sqrt(((double) (NUM_SEEDS)) / ((double) k)));
	public static final int n = 6;
	
	Point2D query = new Point2D.Float(270.0f, 270.0f);
	
	public void init() {
        setSize(WIDTH, HEIGHT);
    }
	
	public void paint(Graphics g){
		super.paint(g);
		
		// Split the space into an nxn grid and initialise a list in each grid cell
		// to store the line segments within that grid cell
		
		ArrayList<LineSegment>[][] mGrid = (ArrayList<LineSegment>[][]) new ArrayList[n][n];
		
		for (int i = 0; i < n; i++) {
		    for (int j = 0; j < n; j++){
		         mGrid[i][j]=new ArrayList<LineSegment>(); 
		    }
		}
		
		int xFloor;
		int yFloor;
		Point2D s, e, p;
		
		
		// Initialise seed data
		
		LineSegment[] seeds = new LineSegment[NUM_SEEDS];
		for(LineSegment ls: seeds){
			s = new Point2D.Float( (float) Math.random() * WIDTH, (float) Math.random() * HEIGHT);
			e = new Point2D.Float( (float) Math.random() * WIDTH, (float) Math.random() * HEIGHT);
			ls = new LineSegment(s, e);
			
			g.setColor(Color.BLACK);
			g.drawLine((int) s.getX(), (int) s.getY(), (int) e.getX(), (int) e.getY());
			
			
			// Add this seed to the appropriate grid cell's list
			
			p = ls.getClosestPoint(query);
			g.setColor(Color.BLUE);
			g.drawOval((int) p.getX(), (int) p.getY(), 2, 2);
			
			xFloor = (int) Math.floor((double) p.getX() * n / WIDTH);
			yFloor = (int) Math.floor((double) p.getY() * n / HEIGHT);
			mGrid[xFloor][yFloor].add(ls);
		}
		

		
		// Start kNN algorithm
		
		g.setColor(Color.BLUE);
		g.drawOval((int) query.getX() - 4, (int) query.getY() - 4, 8, 8);
		
		// Find the grid cell the query is in (a grid cell is represented by its index)
		int gridCellX = (int) Math.floor((double) query.getX() * n / WIDTH);
		int gridCellY = (int) Math.floor((double) query.getX() * n / HEIGHT);
		
		
		ArrayList<Float> distances = new ArrayList<Float>();
		ArrayList<LineSegment> nearestNeighbours = new ArrayList<LineSegment>();
		float[] distancesArray = null;
		LineSegment[] nearestNeighboursArray = null;
		double x1, y1, x2, y2;
		boolean firstCellChecked = false;
		boolean[][] hasBeenChecked = new boolean[n][n];
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				hasBeenChecked[i][j] = false;
			}
		}
		
	
		if(mGrid[gridCellX][gridCellY].isEmpty()){
			// There are no seed points in the query point's grid cell
			System.out.println("First cell is empty, resize n");
		} else{
			int startX = gridCellX;
			int startY = gridCellY;
			int endX = gridCellX;
			int endY = gridCellY;
			Point2D cp;
						
			
			while(startX >= gridCellX - 3 && startY >= gridCellY - 3 &&
					endX < gridCellX + 3 && endY < gridCellY + 3){	
					
			
			for(int x = Math.max(startX, 0); x <= Math.min(endX, n - 1); x++){	
				for(int y = Math.max(startY, 0); y <= Math.min(endY, n - 1); y++){
					
					if(!hasBeenChecked[x][y]){
						if(!firstCellChecked){
							if(x == startX && y == startY) {
	
						
						for(LineSegment ls: mGrid[x][y]){
							cp = ls.getClosestPoint(query);
							x1 = (double) cp.getX();
							y1 = (double) cp.getY();
							x2 = (double) query.getX();
							y2 = (double) query.getY();
							distances.add((float) Point2D.distance(x1, y1, x2, y2));
							nearestNeighbours.add(ls);
						}
						
						if(mGrid[gridCellX][gridCellY].size() >= k){
							firstCellChecked = true;
							System.out.println("firstCellChecked = true");
						}
							} else{
								break;
							}
						} else{
							
							// Use the following two lines to draw the grid cells being checked to clarify what's going on
//							g.setColor(Color.MAGENTA);
//							g.drawRect(x * WIDTH / n, y * HEIGHT / n, WIDTH / n, HEIGHT / n);
							
							for(LineSegment ls: mGrid[x][y]){
								cp = ls.getClosestPoint(query);
								x1 = (double) cp.getX();
								y1 = (double) cp.getY();
								x2 = (double) query.getX();
								y2 = (double) query.getY();
								float dist = (float) Point2D.distance(x1, y1, x2, y2);
								if(dist < Math.sqrt(2.0) * WIDTH / n){
								distances.add((float) Point2D.distance(x1, y1, x2, y2));
								nearestNeighbours.add(ls);
								}
								
							}
						}
						
						hasBeenChecked[x][y] = true;
						
					}
					
				}
			}
			
			startX--;
			startY--;
			endX++;
			endY++;
			
			if(mGrid[gridCellX][gridCellY].size() < k){
				System.out.println("Need more seeds in centre cell, please try again");
				break;
			}
			
		}
		
			nearestNeighboursArray = new LineSegment[nearestNeighbours.size()];
			distancesArray = new float[distances.size()];
			
			for(int i = 0; i < distances.size(); i++){
				distancesArray[i] = distances.get(i);
			}
			
			for(int i = 0; i < nearestNeighbours.size(); i++){
				nearestNeighboursArray[i] = nearestNeighbours.get(i);
			}
			
			
			Arrays.sort(distancesArray);
			
			LineSegmentsComparator comparator = new LineSegmentsComparator(query);
			
			Arrays.sort(nearestNeighboursArray, comparator);
			
			double X1, Y1, X2, Y2;
			X1 = query.getX();
			Y1 = query.getY();
			
			
			System.out.println();
			System.out.println("k shortest distances:");
			System.out.println();
			
			Point2D start, end;
			
			for(int i = 0; i < Math.min(nearestNeighbours.size(), k); i++){
				
				X2 = nearestNeighboursArray[i].getClosestPoint(query).getX();
				Y2 = nearestNeighboursArray[i].getClosestPoint(query).getY();
				
				start = nearestNeighboursArray[i].getStart();
				end = nearestNeighboursArray[i].getEnd();
				
				
			System.out.println(String.valueOf(i + 1) + ": " + String.valueOf(Point2D.distance(X1, Y1, X2, Y2)));
			g.setColor(Color.RED);
			g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
			g.drawOval((int) nearestNeighboursArray[i].getClosestPoint(query).getX() - 2,
					(int) nearestNeighboursArray[i].getClosestPoint(query).getY() - 2, 4, 4);
			}
			System.out.println(String.valueOf(nearestNeighbours.size()));
			System.out.println("Number of points in query cell:  " + String.valueOf(mGrid[gridCellX][gridCellY].size()));
		}
		
		
		
	}
	
	
}
