
package arcircle.ftsim.simulation.chara.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.algorithm.route.Astar;
import arcircle.ftsim.simulation.algorithm.route.Map;
import arcircle.ftsim.simulation.algorithm.route.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class SimpleAI extends AI {
	Chara chara;
	Field field;
	Characters characters;
	int weaponType;
	boolean attack_flag;

	public SimpleAI(Chara chara, Field field, Characters characters) {
		this.chara = chara;
		this.field = field;
		this.characters = characters;
		this.weaponType = CalculateMoveAttackRange.judgeAttackRangedType(chara
				.getItemList());
	}

	// @Override
	// public void thinkAndDo(Characters characters) {
	// chara.x += 2;
	// chara.pX = chara.x * Field.MAP_CHIP_SIZE;
	// chara.setStand(true);
	// }

	@Override
	public void thinkAndDo() {

		CalculateMoveAttackRange cmRange = new CalculateMoveAttackRange(field,
				chara);
		boolean[][] moveRange = cmRange.calculateRange();

		// boolean[][] attackRange = new boolean[field.getFieldRow()][field.getFieldCol()];
		// boolean[][] attackJudge = new boolean[field.getFieldRow()][field.getFieldCol()];
		//
		// CalculateMoveAttackRange.calculateAttackRange(chara.x, chara.y,
		// attackRange, weaponType, field);;
		// attackJudge = CalculateMoveAttackRange.calculateJudgeAttack(field,
		// attackRange, chara);
		// TODO:最新だよー，ここでAI作れば，後は何とかなりそう

		// 攻撃可能キャラの数を調べる
		ArrayList<AttackCharaData> attackCharaArray = Algorithm
				.generateAttackCharaArray(chara, weaponType, moveRange, field,
						characters);

		// 攻撃可能キャラがいないため，最短のキャラを選択し、そこを目指して移動する
		if (attackCharaArray.size() == 0) {
			//chara.setStand(true);
			Map map = new Map(field.createMoveCostArray(chara.x, chara.y));
			Chara targetChara = getMostNeighborChara(characters, map);
			moveToOneChara(targetChara, moveRange, map, cmRange);
		// 攻撃可能キャラがいたら、その中でも防御の低いやつを狙う
		} else {
//			Collections.shuffle(attackCharaArray);
//			AttackCharaData attackChara = attackCharaArray.get(0);
			SelectAttackTarget sat = new SelectLowerDefenceAttackTarget();
			AttackCharaData attackCharaData = sat.getAttackTargetCharaData(attackCharaArray);

//			chara.x = attackChara.attackPoint.x;
//			chara.y = attackChara.attackPoint.y;
//			chara.pX = chara.x * Field.MAP_CHIP_SIZE;
//			chara.pY = chara.y * Field.MAP_CHIP_SIZE;

			arcircle.ftsim.simulation.algorithm.range.Node moveNode =
					cmRange.getNodeByXY(attackCharaData.attackPoint.x, attackCharaData.attackPoint.y);
			if (moveNode.pointXArray.size() >= 1) {
				field.addMoveTask(chara, moveNode);
			}
			field.addAttackTask(chara,
					attackCharaData.attackPoint,
					new Point(attackCharaData.damageChara.x, attackCharaData.damageChara.y));
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
		System.out.println("SelectChara:" + toChara.id + " x:" + toChara.x + " y:" + toChara.y);
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

				field.addMoveTask(chara, moveNode);
				field.addStandCharaTask(chara, true);
				break;
			}
		}
	}
}