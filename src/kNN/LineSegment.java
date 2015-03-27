package kNN;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class LineSegment {

	
	private Point2D start;
	private Point2D end;
	
	public LineSegment(Point2D s, Point2D e){
		start = s;
		end = e;
	}
	
	public Point2D getClosestPoint(Point2D q){
		
		// q is the query point.
		
		// Let the point x be the point on the infinite extension of the line segment
		// such that the line joining it to the query point is perpendicular to the
		// line segment. Setting x = start + lambda * (end - start), we solve the
		// equation (x - q) dot (end - start) = 0, for lambda.
		
		
		float lambda = (dot(q, end) - dot(q, start) - dot(start, end) + dot(start, start)) / 
				(dot(end, end) - 2 * dot(start, end) + dot(start, start));
		
		// Sub in the solved lambda value to get the point x.
		
		Point2D x = new Point2D.Float((float) (start.getX() + lambda * (end.getX() - start.getX())), 
				(float) (start.getY() + lambda * (end.getY() - start.getY())));
		
		// If x is contained within the segment, return x.
		
		if(lambda <= 1 && lambda >=0){
			return x;
		} else{
		
		// Otherwise, return the closest of the two end points.
			
		Point2D[] points = new Point2D[2];
		points[0] = start;
		points[1] = end;
		
		Arrays.sort(points, new PointsComparator(q));
		
		return points[0];
		}
	}
	
	public Point2D getStart(){
		return start;
	}
	
	public Point2D getEnd(){
		return end;
	}
	
	private float dot(Point2D p1, Point2D p2){
		return (float) (p1.getX() * p2.getX() + p1.getY() * p2.getY());
	}

	
}
