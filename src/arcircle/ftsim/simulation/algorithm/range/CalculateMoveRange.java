package arcircle.ftsim.simulation.algorithm.range;

import java.util.ArrayList;
import java.util.Collections;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;

public class CalculateMoveRange {
	public boolean[][] moveRange;
	public boolean[][] attackRange;

	Field field;
	Chara chara;

	public int startX;
	public int startY;

	public CalculateMoveRange(Field field, Chara chara) {
		super();
		this.field = field;
		this.chara = chara;

		this.startX = chara.x;
		this.startY = chara.y;

		moveRange =   new boolean[field.row][field.col];
		attackRange = new boolean[field.row][field.col];
		searchedNodeArray = new ArrayList<Node>();
		searchingNodeArray = new ArrayList<Node>();
	}

	/**
	 * 探索済みリスト
	 */
	ArrayList<Node> searchedNodeArray = new ArrayList<Node>();
	/**
	 * 探索リスト
	 */
	ArrayList<Node> searchingNodeArray = new ArrayList<Node>();



	public void calculateRange() {
		searchingNodeArray.add(new Node(chara.x, chara.y, chara.status.move));

		while (searchingNodeArray.size() >= 1) {
			//探索ノードリストの先頭を取得・削除
			Node searchNode = searchingNodeArray.remove(0);

			//取得したノードから探索
			search(searchNode);
		}

		setMoveRange();
	}

	private void setMoveRange() {
		for (Node node : searchedNodeArray) {
			moveRange[node.y][node.x] = true;
		}
	}

	private void search(Node searchNode) {
		//探索済みに登録
		searchedNodeArray.add(searchNode);
		Collections.sort(searchedNodeArray);

		//四方向への移動ノードを登録
		addSearchingNodeArray(searchNode.x - 1, searchNode.y,     searchNode, Chara.LEFT);
		addSearchingNodeArray(searchNode.x,     searchNode.y - 1, searchNode, Chara.UP);
		addSearchingNodeArray(searchNode.x + 1, searchNode.y,     searchNode, Chara.RIGHT);
		addSearchingNodeArray(searchNode.x,     searchNode.y + 1, searchNode, Chara.DOWN);
	}

	private void addSearchingNodeArray(int toX, int toY, Node searchNode, int direction) {
		//マップ外の座標なら移動できない
		if (!(0 <= toX && toX < field.col)) {
			return;
		}
		if (!(0 <= toY && toY < field.row)) {
			return;
		}

		//コストが0未満になるなら移動できない
		int leftMove = searchNode.leftMove - field.moveCost[toY][toX];
		if (leftMove < 0) {
			return;
		}

		//移動先のノードを生成
		Node nextNode = new Node(toX, toY, leftMove);

		if (nextNode.x == 0 && nextNode.y == 9) {
			System.out.println("aaa");
		}

		//探索リストに探索中のノードと同座標のノードが登録されているかを検索
		int searchingIndex = Collections.binarySearch(searchingNodeArray, nextNode);
		//探索済みリストに探索中のノードと同座標のノードが登録されているかを検索
		int searchedIndex = Collections.binarySearch(searchedNodeArray, nextNode);

		if (searchingIndex < 0 && searchedIndex < 0) {
			//どちらにも登録されていなければ
		} else if(searchingIndex >= 0) {
			//探索リストに登録されていれば

			Node searchingNode = searchingNodeArray.get(searchingIndex);
			//残り移動力がnextのほうが小さければ終了
			if (searchingNode.leftMove > nextNode.leftMove) {
				return;
			}
			//残り移動力がnextのほうが大きければ
			searchingNodeArray.remove(searchingIndex);
		} else if(searchedIndex >= 0) {
			//探索済みリストに登録されていれば

			Node searchedNode = searchedNodeArray.get(searchedIndex);
			//残り移動力がnextのほうが小さければ終了
			if (searchedNode.leftMove > nextNode.leftMove) {
				return;
			}
			//残り移動力がnextのほうが大きければ
			searchedNodeArray.remove(searchedIndex);
		}

		//向きと座標のListをコピーして
		nextNode.directionArray = new ArrayList<Integer>(searchNode.directionArray);
		nextNode.pointXArray = new ArrayList<Integer>(searchNode.pointXArray);
		nextNode.pointYArray = new ArrayList<Integer>(searchNode.pointYArray);
		//移動先の向きと座標を追加する
		nextNode.directionArray.add(direction);
		nextNode.pointXArray.add(toX);
		nextNode.pointYArray.add(toY);

		//探索中のノードを探索リストに追加
		searchingNodeArray.add(nextNode);
		Collections.sort(searchingNodeArray);
	}
}
