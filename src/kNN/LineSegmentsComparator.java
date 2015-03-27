package kNN;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class LineSegmentsComparator implements Comparator<LineSegment>{

	private Point2D mQuery = new Point2D.Float(0.0f, 0.0f);
	
	public LineSegmentsComparator(Point2D query){
		mQuery = query;
	}
	
	@Override
	public int compare(LineSegment ls1, LineSegment ls2) {
		// TODO Auto-generated method stub
		
		Point2D p1 = ls1.getClosestPoint(mQuery);
		Point2D p2 = ls2.getClosestPoint(mQuery);
		
		double p1x = p1.getX();
		double p1y = p1.getY();
		double p2x = p2.getX();
		double p2y = p2.getY();
		double qx = mQuery.getX();
		double qy = mQuery.getY();
		
		if(Point2D.distance(qx, qy, p1x, p1y) - Point2D.distance(qx, qy, p2x, p2y) > 0){
		return 1;
		} else if(Point2D.distance(qx, qy, p1x, p1y) - Point2D.distance(qx, qy, p2x, p2y) < 0){
			return -1;
		} else{
			return 0;
		}
	}
	
	public void setQuery(Point2D query){
		mQuery = query;
	}

}
