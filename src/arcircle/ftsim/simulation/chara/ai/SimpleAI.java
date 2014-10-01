package arcircle.ftsim.simulation.chara.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.algorithm.root.Astar;
import arcircle.ftsim.simulation.algorithm.root.Map;
import arcircle.ftsim.simulation.algorithm.root.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class SimpleAI extends AI {
	Chara chara;

	public SimpleAI(Chara chara) {
		this.chara = chara;
	}

	// @Override
	// public void thinkAndDo(Characters characters) {
	// chara.x += 2;
	// chara.pX = chara.x * Field.MAP_CHIP_SIZE;
	// chara.setStand(true);
	// }

	@Override
	public void thinkAndDo(Field field, Characters characters) {

		CalculateMoveAttackRange cmRange = new CalculateMoveAttackRange(field,
				chara);
		boolean[][] moveRange = cmRange.calculateRange();

		// boolean[][] attackRange = new boolean[field.row][field.col];
		// boolean[][] attackJudge = new boolean[field.row][field.col];
		//
		int weaponType = CalculateMoveAttackRange.judgeAttackWeaponType(chara
				.getItemList());
		// CalculateMoveAttackRange.calculateAttackRange(chara.x, chara.y,
		// attackRange, weaponType, field);;
		// attackJudge = CalculateMoveAttackRange.calculateJudgeAttack(field,
		// attackRange, chara);
		// TODO:最新だよー，ここでAI作れば，後は何とかなりそう

		// 攻撃可能キャラの数を調べる
		ArrayList<AttackCharaData> attackCharaArray = Algorithm
				.generateAttackCharaArray(chara, weaponType, moveRange, field,
						characters);

		// 攻撃可能キャラがいないため，移動のみとする
		if (attackCharaArray.size() == 0) {
			//TODO:攻撃範囲内にキャラがいなければとりあえず待機
			chara.setStand(true);
			//serachMoveToOneChara(field, characters, moveRange);
		} else {
			Collections.shuffle(attackCharaArray);
			AttackCharaData attackChara = attackCharaArray.get(0);

			chara.x = attackChara.attackPoint.x;
			chara.y = attackChara.attackPoint.y;
			chara.pX = chara.x * Field.MAP_CHIP_SIZE;
			chara.pY = chara.y * Field.MAP_CHIP_SIZE;

			field.charaAttack(chara, attackChara.damageChara.y, attackChara.damageChara.x);
		}
	}

	private void serachMoveToOneChara(Field field, Characters characters,
			boolean[][] moveRange) {
		for (Chara toChara : characters.characterArray) {
			if (chara.getCamp() == Chara.CAMP_FRIEND) {
				if (toChara.getCamp() == Chara.CAMP_ENEMY) {
				}
			}
			if (chara.getCamp() == Chara.CAMP_ENEMY) {
				if (toChara.getCamp() == Chara.CAMP_FRIEND) {
					moveToOneChara(toChara, field, characters, moveRange);
				}
			}
		}
	}

	private void moveToOneChara(Chara toChara, Field field,
			Characters characters, boolean[][] moveRange) {
		Map map = new Map(field.createMoveCostArray(chara.x, chara.y));
		Astar aStar = new Astar(map);

		LinkedList<Node> nodeList = aStar.searchPath(
				new Point(chara.x, chara.y), new Point(toChara.x, toChara.y));

		Iterator<Node> itr = nodeList.descendingIterator();
		while (itr.hasNext()) {
			Node node = itr.next();

			if (moveRange[node.pos.y][node.pos.x]) {
				chara.x = node.pos.x;
				chara.y = node.pos.y;
				chara.pX = chara.x * Field.MAP_CHIP_SIZE;
				chara.pY = chara.y * Field.MAP_CHIP_SIZE;
				chara.setStand(true);
				break;
			}
		}
	}
}
