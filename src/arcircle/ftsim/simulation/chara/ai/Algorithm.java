package arcircle.ftsim.simulation.chara.ai;

import java.awt.Point;
import java.util.ArrayList;

import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Weapon;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class Algorithm {


	public static ArrayList<AttackCharaData> generateAttackCharaArray(Chara chara, int weaponType,
			boolean[][] moveRange, Field field, Characters characters) {
		ArrayList<AttackCharaData> attackCharaArray = new ArrayList<AttackCharaData>();

		Chara[][] charaPut = new Chara[field.getFieldRow()][field.getFieldCol()];

		for (Chara putChara : characters.characterArray) {
			if (chara.getCamp() == Chara.CAMP_FRIEND) {
				if (putChara.getCamp() == Chara.CAMP_ENEMY) {
					charaPut[putChara.y][putChara.x] = putChara;
				}
			}
			if (chara.getCamp() == Chara.CAMP_ENEMY) {
				if (putChara.getCamp() == Chara.CAMP_FRIEND) {
					charaPut[putChara.y][putChara.x] = putChara;
				}
			}
		}

		for (int y = 0; y < field.getFieldRow(); y++) {
			for (int x = 0; x < field.getFieldCol(); x++) {
				if (!moveRange[y][x]) {
					continue;
				}
				addAttackCharaArray(y, x, chara, weaponType, charaPut, field, attackCharaArray);
			}
		}

		return attackCharaArray;
	}

	private static void addAttackCharaArray(int y, int x, Chara chara, int weaponType, Chara[][] charaPut,
			Field field, ArrayList<AttackCharaData> attackCharaArray) {
		int charaX = x;
		int charaY = y;

		final int[][] nearAttackRange = CalculateMoveAttackRange.nearAttackRange;
		final int[][] farAttackRange = CalculateMoveAttackRange.farAttackRange;

		if (weaponType == Weapon.RANGE_NEAR || weaponType == Weapon.RANGE_NEAR_FAR) {
			for (int[] range : nearAttackRange) {
				if (charaX + range[0] < 0 || charaY + range[1] < 0
						|| charaX + range[0] >= field.getFieldCol()
						|| charaY + range[1] >= field.getFieldRow()) {
					continue;
				}
				if (charaPut[charaY + range[1]][charaX + range[0]] != null) {
					AttackCharaData attackCharaData = new AttackCharaData();
					attackCharaData.attackPoint = new Point(x, y);
					attackCharaData.attackChara = chara;
					attackCharaData.damageChara = charaPut[charaY + range[1]][charaX + range[0]];
					attackCharaArray.add(attackCharaData);
				}
			}
		}
		if (weaponType == Weapon.RANGE_FAR || weaponType == Weapon.RANGE_NEAR_FAR) {
			for (int[] range : farAttackRange) {
				if (charaX + range[0] < 0 || charaY + range[1] < 0
						|| charaX + range[0] >= field.getFieldCol()
						|| charaY + range[1] >= field.getFieldRow()) {
					continue;
				}
				if (charaPut[charaY + range[1]][charaX + range[0]] != null) {
					AttackCharaData attackCharaData = new AttackCharaData();
					attackCharaData.attackPoint = new Point(x, y);
					attackCharaData.attackChara = chara;
					attackCharaData.damageChara = charaPut[charaY + range[1]][charaX + range[0]];
					attackCharaArray.add(attackCharaData);
				}
			}
		}
	}
}
