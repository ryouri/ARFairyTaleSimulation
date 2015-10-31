package arcircle.ftsim.simulation.algorithm.range;

import java.util.ArrayList;

public class Node implements Comparable<Node>{
	public int x;
	public int y;

	public int leftMove;

	public ArrayList<Integer> directionArray;
	public ArrayList<Integer> pointXArray;
	public ArrayList<Integer> pointYArray;

	public Node(int x, int y, int leftMove) {
		super();
		this.x = x;
		this.y = y;
		this.leftMove = leftMove;

		this.directionArray = new ArrayList<Integer>();
		this.pointXArray	= new ArrayList<Integer>();
		this.pointYArray	= new ArrayList<Integer>();
	}

	@Override
	public int compareTo(Node o) {
		int dx = this.x - o.x;
		int dy = this.y - o.y;

		if (dx == 0 && dy == 0) {
			// xもyも等しければ
			return 0;
		} else {
			if (dx != 0) {
				// xが等しくなければ
				return dx;
			} else {
				// yが等しくなければ
				return dy;
			}
		}
	}
}
