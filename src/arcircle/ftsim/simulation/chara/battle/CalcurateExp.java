package arcircle.ftsim.simulation.chara.battle;

import java.util.Random;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.GrowRateStatus;

/**
 * 計算値の加算後のレベルアップ値の計算を行う
 * 実際のレベルアップ処理は他のTaskで行う
 */
public class CalcurateExp {
	private GrowRateStatus levelUpStatus;
	private int beforeUpExp;
	private int afterUpExp;
	private int addExp;
	private int upLevel;
	private Chara chara;

	public GrowRateStatus getLevelUpStatus() {
		return levelUpStatus;
	}
	public int getAfterUpExp() {
		return afterUpExp;
	}
	public int getUpLevel() {
		return upLevel;
	}
	public int getBeforeUpExp() {
		return beforeUpExp;
	}
	public int getAddExp() {
		return addExp;
	}
	public Chara getChara() {
		return chara;
	}

	private Random random;

	public CalcurateExp(Chara chara, int addExp) {
		levelUpStatus = new GrowRateStatus();
		this.chara = chara;
		this.addExp = addExp;
		addExp(chara, addExp);
	}

	private void addExp (Chara chara, int addExp) {
		beforeUpExp = chara.status.exp;
		int addedExp = chara.status.exp + addExp;
		upLevel = addedExp / 100;
		afterUpExp = addedExp % 100;

		if (upLevel > 0) {
			random = new Random();
		}

		for (int i = 0; i < upLevel; i++) {
			levelUpStatus.hp           += calcUpStatus(chara.growRateStatus.hp);
			levelUpStatus.power        += calcUpStatus(chara.growRateStatus.power);
			levelUpStatus.magicPower   += calcUpStatus(chara.growRateStatus.magicPower);
			levelUpStatus.speed        += calcUpStatus(chara.growRateStatus.speed);
			levelUpStatus.tech         += calcUpStatus(chara.growRateStatus.tech);
			levelUpStatus.luck         += calcUpStatus(chara.growRateStatus.luck);
			levelUpStatus.defense      += calcUpStatus(chara.growRateStatus.defense);
			levelUpStatus.magicDefense += calcUpStatus(chara.growRateStatus.magicDefense);
			levelUpStatus.move         += calcUpStatus(chara.growRateStatus.move);
			levelUpStatus.physique     += calcUpStatus(chara.growRateStatus.physique);
		}
	}

	public static final int GROW_RATE_ONE = 100;

	private int calcUpStatus(int growRate) {
		int growNum = 0;

		while(growRate > 0) {
			if (growRate >= random.nextInt(GROW_RATE_ONE) + 1) {
				growNum++;
			}

			growRate -= 100;
		}

		return growNum;
	}
}
