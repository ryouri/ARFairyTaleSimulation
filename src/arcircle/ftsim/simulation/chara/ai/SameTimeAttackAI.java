package arcircle.ftsim.simulation.chara.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.algorithm.route.Astar;
import arcircle.ftsim.simulation.algorithm.route.Map;
import arcircle.ftsim.simulation.algorithm.route.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class SameTimeAttackAI extends AI {
	Chara chara;
	Field field;
	Characters characters;
	int weaponType;
	boolean attack_flag;


	public SameTimeAttackAI(Chara chara, Field field, Characters characters) {
		this.chara = chara;
		this.field = field;
		this.characters = characters;
		this.weaponType = CalculateMoveAttackRange.judgeAttackWeaponType(chara
				.getItemList());
	}

	@Override
	public void thinkAndDo() {
		CalculateMoveAttackRange cmRange = new CalculateMoveAttackRange(field,
				chara);
		boolean[][] moveRange = cmRange.calculateRange();

		// 攻撃可能キャラの数を調べる
		ArrayList<AttackCharaData> attackCharaArray = getAttackCharaArray();

		// 攻撃フラグがFalseなら、周辺のエネミー(自分含む)の攻撃可能キャラを調べる
		if (!attack_flag) {
			ArrayList<Chara> aroundEnemies = getArroundEnemy(2);
			for (Chara aroundChara1 : aroundEnemies){
				if (((SameTimeAttackAI)aroundChara1.getAI()).getAttackCharaArray().size() != 0){
					for (Chara aroundChara2 : aroundEnemies){
						((SameTimeAttackAI)aroundChara2.getAI()).setAttack_flag(true);
					}
					break;
				}
			}
		}

		// 周辺のエネミーを調べてもフラグが立たなければ待機
		if (!attack_flag) {
			chara.setStand(true);
		// 攻撃可能キャラはいないけど攻撃フラグが立ってたら、最短キャラまで移動
		} else if (attackCharaArray.size() == 0){
			Map map = new Map(field.createMoveCostArray(chara.x, chara.y));
			Chara targetChara = getMostNeighborChara(characters, map);
			moveToOneChara(targetChara, moveRange, map, cmRange);
		// 攻撃可能キャラがいたら、その中でも一番ダメージが大きくなる相手に攻撃
		} else {
			// ここでインスタンス化するクラスによって攻撃対象のAIが変わる
			SelectAttackTarget sat = new SelectHighDamageAttackTarget();
			AttackCharaData attackChara = sat.getAttackTargetCharaData(attackCharaArray);
			arcircle.ftsim.simulation.algorithm.range.Node moveNode =
					cmRange.getNodeByXY(attackChara.attackPoint.x, attackChara.attackPoint.y);
			if (moveNode.pointXArray.size() >= 1) {
				field.setCharaMove(chara, moveNode);
			}
			field.setCharaAttack(chara, attackChara.damageChara.y, attackChara.damageChara.x);
		}
	}

	// 自分から最短のフレンドキャラを探して返す
	// 見つけた対象キャラの周囲に空きがなかったらパス
	private Chara getMostNeighborChara(Characters characters, Map map) {
		int bestCost = 9999;
		Chara bestChara = null;
		for (Chara toChara : characters.characterArray) {
			if (chara.getCamp() == Chara.CAMP_FRIEND) {
				if (toChara.getCamp() == Chara.CAMP_ENEMY) {
				}
			}
			if (chara.getCamp() == Chara.CAMP_ENEMY) {
				if (toChara.getCamp() == Chara.CAMP_FRIEND) {
					int m = toChara.x - chara.x;
					int n = toChara.y - chara.y;
					int cost = (int) Math.sqrt(m * m + n * n);
					if (!map.isHit(toChara.x-1, toChara.y) || !map.isHit(toChara.x+1, toChara.y) ||
							!map.isHit(toChara.x, toChara.y-1) || !map.isHit(toChara.x, toChara.y+1)){
						if (cost < bestCost){
							bestCost = cost;
							bestChara = toChara;
						}
					}
				}
			}
		}
		return bestChara;
	}

	int[][] deviationArray = {  { 1,  0},//right
								{-1,  0},//left
						     	{ 0,  1},//down
						     	{ 0, -1}};//up

	private void moveToOneChara(Chara toChara, boolean[][] moveRange, Map map, CalculateMoveAttackRange cmRange) {
		Astar aStar = new Astar(map);

		int bestCost = 9999;
		int deviationIndex = 0;
		for (int i = 0; i < deviationArray.length; i++) {
			int [] oneDeviation = deviationArray[i];
			if (map.isHit(toChara.x+oneDeviation[0], toChara.y+oneDeviation[1])){
				continue;
			}
			int xSub = Math.abs((toChara.x + oneDeviation[0]) - chara.x);
			int ySub = Math.abs((toChara.y + oneDeviation[1]) - chara.y);
			int cost = (int) Math.sqrt(xSub * ySub + xSub * ySub);
			if (cost < bestCost){
				deviationIndex = i;
			}
		}

		//TODO nodeListがnullだったら探索失敗なので別のターゲットキャラクタでもう一回やる必要がある
		LinkedList<Node> nodeList = aStar.searchPath(
				new Point(chara.x, chara.y), new Point(toChara.x+deviationArray[deviationIndex][0], toChara.y+deviationArray[deviationIndex][1]));

		Iterator<Node> itr = nodeList.descendingIterator();
		while (itr.hasNext()) {
			Node node = itr.next();
			if (moveRange[node.pos.y][node.pos.x]) {
				arcircle.ftsim.simulation.algorithm.range.Node moveNode =
						cmRange.getNodeByXY(node.pos.x, node.pos.y);

				field.setCharaMove(chara, moveNode);
				field.setCharaStand(chara, true);
				break;
			}
		}
	}

	/**
	 * 周囲の同種AI(SameTimeAttackAI)のエネミーを返す(自分も含む)
	 * @return エネミーのリスト
	 */
	private ArrayList<Chara> getArroundEnemy(int aroundMassNum){
		ArrayList<Chara> aroundCharaArray = new ArrayList<Chara>();
		Queue<Chara> unserchedCharaArray = new LinkedList<Chara>();
		ArrayList<Chara> tempAroundCharaArray = new ArrayList<Chara>();
		unserchedCharaArray.add(chara);
		while (!unserchedCharaArray.isEmpty()){
			Chara targetChara = unserchedCharaArray.remove();
			for (Chara tempChara : field.getAroundChara(targetChara, aroundMassNum)){
				if (tempChara.getAI() instanceof SameTimeAttackAI
						&& !aroundCharaArray.contains(tempChara)
						&& !tempChara.equals(chara)
						&& !unserchedCharaArray.contains(tempChara)){
					unserchedCharaArray.add(tempChara);
				}
				aroundCharaArray.add(targetChara);
			}
		}
		return aroundCharaArray;
	}

	/**
	 * 攻撃可能キャラを調べる
	 * @return 攻撃キャラデータリスト
	 */
	public ArrayList<AttackCharaData> getAttackCharaArray(){
		CalculateMoveAttackRange cmRange = new CalculateMoveAttackRange(field,
				chara);
		boolean[][] moveRange = cmRange.calculateRange();
		return Algorithm
				.generateAttackCharaArray(chara, weaponType, moveRange, field,
						characters);
	}

	public boolean isAttack_flag() {
		return attack_flag;
	}

	public void setAttack_flag(boolean attack_flag) {
		this.attack_flag = attack_flag;
	}
}
