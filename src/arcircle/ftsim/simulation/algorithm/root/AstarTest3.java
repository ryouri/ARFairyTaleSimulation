package arcircle.ftsim.simulation.algorithm.root;

import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JFrame;

/*
 * Created on 2005/04/23
 *
 */

/**
 * A*経路探索のサンプルプログラム
 * @author mori
 */
public class AstarTest3 extends JFrame {
	
	public static final int[][] MAP = 
		{ 
			{1,  1,  1,  1, 1},
			{1,  1,  1,  1, 1},
			{1, -1, -1, -1, 1},
			{1,  1,  1,  1, 1},
			{1,  1,  1,  1, 1},
		};
	
	
    public AstarTest3() {
    	Map map = new Map(MAP);
    	Astar aStar = new Astar(map);
    	
    	LinkedList<Node> nodeList = aStar.searchPath(new Point(2, 0), new Point(2, 4));
    	
    	nodeList.clone();
    	
    }

    public static void main(String[] args) {
        AstarTest3 frame = new AstarTest3();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
