package arcircle.ftsim.simulation.chara.ai;

import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class SimpleAI extends AI {
	Chara chara;
	
	public SimpleAI(Chara chara) {
		this.chara = chara;
	}

//	@Override
//	public void thinkAndDo(Characters characters) {
//		chara.x += 2;
//		chara.pX = chara.x * Field.MAP_CHIP_SIZE;
//		chara.setStand(true);
//	}
	
	@Override
	public void thinkAndDo(Field field, Characters characters) {
		
		CalculateMoveAttackRange cmRange = new CalculateMoveAttackRange(field, chara);
		boolean[][] moveRange = cmRange.calculateRange();
		boolean[][] attackRange = new boolean[field.row][field.col];
		boolean[][] attackJudge = new boolean[field.row][field.col];
		
		int weaponType = CalculateMoveAttackRange.judgeAttackWeaponType(chara.getItemList());
		CalculateMoveAttackRange.calculateAttackRange(chara.x, chara.y, attackRange, weaponType, field);;
		attackJudge = CalculateMoveAttackRange.calculateJudgeAttack(field, attackRange, chara);
		//TODO:最新だよー，ここでAI作れば，後は何とかなりそう
		
		chara.x += 2;
		chara.pX = chara.x * Field.MAP_CHIP_SIZE;
		chara.setStand(true);
	}
}
