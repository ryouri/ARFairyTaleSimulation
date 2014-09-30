package arcircle.ftsim.simulation.algorithm.root;

import java.awt.Point;

/*
 * Created on 2005/04/23
 *
 */

/**
 * @author mori
 *  
 */
public class Map {
    // チップセットのサイズ（単位：ピクセル）
    private static final int CS = 16;

    // マップ
    private int[][] mapCost;
    // 行、列数（マス）
    private int row;
    private int col;
    
    /**
     * コンストラクタ。
     * 
     * @param filename マップデータのファイル名
     */
    public Map(int [][] costArray) {
    	this.mapCost = costArray;
    	this.row = costArray.length;
    	this.col = costArray[0].length;
    }

    /**
     * (x,y)にぶつかるものがあるか調べる。
     * 
     * @param x マップのx座標
     * @param y マップのy座標
     * @return (x,y)にぶつかるものがあったらtrueを返す。
     */
    public boolean isHit(int x, int y) {
    	if (x < 0 || x >= row || y < 0 || y >= col) {
    		return true;
    	}
    	
        // (x,y)に壁があったらぶつかる
        if (mapCost[y][x] == -1) {
            return true;
        }

        // なければぶつからない
        return false;
    }

    /**
     * 行数を返す
     * 
     * @return 行数
     */
    public int getRow() {
        return row;
    }

    /**
     * 列数を返す
     * 
     * @return 列数
     */
    public int getCol() {
        return col;
    }

    /**
     * 地形コストを返す
     * @param pos 座標
     * @return 地形コスト
     */
    public int getCost(Point pos) {
    	if (pos.x < 0 || pos.x >= row || pos.y < 0 || pos.y >= col) {
    		return 999;
    	}
    	
        // (x,y)に壁があったらぶつかる
        if (mapCost[pos.y][pos.x] == -1) {
            return 999;
        }
    	
        return mapCost[pos.y][pos.x];
    }
}