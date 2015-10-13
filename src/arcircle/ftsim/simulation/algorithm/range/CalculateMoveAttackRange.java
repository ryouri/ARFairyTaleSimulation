package arcircle.ftsim.simulation.algorithm.range;

import java.util.ArrayList;
import java.util.Collections;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.RangedItem;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class CalculateMoveAttackRange {
	public boolean[][] moveRange;

	Field field;
	Chara chara;

	public int startX;
	public int startY;

	private int[][] moveCost;

	public CalculateMoveAttackRange(Field field, Chara chara) {
		super();
		this.field = field;
		this.chara = chara;

		this.startX = chara.x;
		this.startY = chara.y;

		moveCost = field.createMoveCostArray(startX, startY);

		moveRange =   new boolean[field.getFieldRow()][field.getFieldCol()];
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

	//x, y
	public static int[][] nearAttackRange = {
		{  0, -1},
		{  1,  0},
		{  0,  1},
		{ -1,  0},
	};

	//x, y
	public static int[][] farAttackRange = {
		{  0, -2},
		{  1, -1},
		{  2,  0},
		{  1,  1},
		{  0,  2},
		{ -1,  1},
		{ -2,  0},
		{ -1, -1},
	};

	public Node getNodeByXY(int x, int y) {
		Node node = new Node(x, y, 0);
		int index = Collections.binarySearch(searchedNodeArray, node);
		if (index >= 0) {
			return searchedNodeArray.get(index);
		} else {
			return null;
		}
	}

	private void setMoveRange() {
		for (Node node : searchedNodeArray) {
			moveRange[node.y][node.x] = true;
		}
	}

	public boolean[][] calculateRange() {
		searchingNodeArray.add(new Node(chara.x, chara.y, chara.status.move));

		while (searchingNodeArray.size() >= 1) {
			//探索ノードリストの先頭を取得・削除
			Node searchNode = searchingNodeArray.remove(0);

			//取得したノードから探索
			search(searchNode);
		}

		setMoveRange();
		alreadyCharaNotStand();

		return moveRange;
	}

	/**
	 * キャラが存在している場所は待機不可
	 */
	private void alreadyCharaNotStand() {
		for (Chara targetChara : field.getCharacters().characterArray) {
			//同じ所属のキャラなら待機不可能とする
			if (targetChara.getCamp() == chara.getCamp()
					&& (targetChara.x != chara.x || targetChara.y != chara.y)) {
				moveRange[targetChara.y][targetChara.x] = false;
			}
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
		if (!(0 <= toX && toX < field.getFieldCol())) {
			return;
		}
		if (!(0 <= toY && toY < field.getFieldRow())) {
			return;
		}

		//移動不可なら移動できない
		if (moveCost[toY][toX] == -1) {
			return;
		}

		//コストが0未満になるなら移動できない
		int leftMove = searchNode.leftMove - moveCost[toY][toX];
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

	public static boolean[][] createJudgeAttackArray(Field field, Chara chara, boolean[][] moveRange) {
		boolean[][] attackRange = new boolean[field.getFieldRow()][field.getFieldCol()];

		int weaponType = CalculateMoveAttackRange.judgeAttackRangedType(chara.getItemList());

		for (int y = 0; y < field.getFieldRow(); y++) {
			for (int x = 0; x < field.getFieldCol(); x++) {
				if (!moveRange[y][x]) {
					continue;
				}
				CalculateMoveAttackRange.calculateAttackRange(x, y, attackRange, weaponType, field);
			}
		}

		return attackRange;
		//attackJudge = CalculateMoveAttackRange.calculateJudgeAttack(field, attackRange, chara);
	}

	public static int judgeAttackRangedType(ArrayList<Item> itemList) {
		boolean nearAttack = false;
		boolean farAttack = false;

		for (Item item : itemList) {
			if (!(item instanceof RangedItem)) {
				continue;
			}

			RangedItem rangedItem = (RangedItem) item;

			if (rangedItem.rangeType == RangedItem.RANGE_NEAR) {
				nearAttack = true;
			} else if (rangedItem.rangeType == RangedItem.RANGE_FAR) {
				farAttack = true;
			} else if (rangedItem.rangeType == RangedItem.RANGE_NEAR_FAR) {
				return RangedItem.RANGE_NEAR_FAR;
			}
		}

		if (nearAttack == true && farAttack == true) {
			return RangedItem.RANGE_NEAR_FAR;
		} else if (nearAttack == true) {
			return RangedItem.RANGE_NEAR;
		} else if (farAttack == true) {
			return RangedItem.RANGE_FAR;
		} else {
			return RangedItem.RANGE_NONE;
		}
	}

	public static void calculateAttackRange(int x, int y, boolean[][] attackRange,
			int rangedType, Field field) {
		int charaX = x;
		int charaY = y;

		if (rangedType == RangedItem.RANGE_NEAR || rangedType == RangedItem.RANGE_NEAR_FAR) {
			for (int[] range : nearAttackRange) {
				if (charaX + range[0] < 0 || charaY + range[1] < 0
						|| charaX + range[0] >= field.getFieldCol()
						|| charaY + range[1] >= field.getFieldRow()) {
					continue;
				}
				attackRange[charaY + range[1]][charaX + range[0]] = true;
			}
		}
		if (rangedType == RangedItem.RANGE_FAR || rangedType == RangedItem.RANGE_NEAR_FAR) {
			for (int[] range : farAttackRange) {
				if (charaX + range[0] < 0 || charaY + range[1] < 0
						|| charaX + range[0] >= field.getFieldCol()
						|| charaY + range[1] >= field.getFieldRow()) {
					continue;
				}
				attackRange[charaY + range[1]][charaX + range[0]] = true;
			}
		}
	}

	public static boolean[][] calculateJudgeAttack(Field field, boolean[][] attackRange, Chara chara) {
		boolean[][] attackJudge = new boolean[field.getFieldRow()][field.getFieldCol()];
		boolean[][] charaPut = new boolean[field.getFieldRow()][field.getFieldCol()];
		Characters characters = field.getCharacters();

		for (Chara putChara : characters.characterArray) {
			if (chara.getCamp() == Chara.CAMP_FRIEND) {
				if (putChara.getCamp() == Chara.CAMP_ENEMY) {
					charaPut[putChara.y][putChara.x] = true;
				}
			}
			if (chara.getCamp() == Chara.CAMP_ENEMY) {
				if (putChara.getCamp() == Chara.CAMP_FRIEND) {
					charaPut[putChara.y][putChara.x] = true;
				}
			}
		}

		for (int y = 0; y < field.getFieldRow(); y++) {
			for (int x = 0; x < field.getFieldCol(); x++) {
				if (charaPut[y][x] == true && attackRange[y][x] == true) {
					attackJudge[y][x] = true;
				}
			}
		}

		return attackJudge;
	}
}
