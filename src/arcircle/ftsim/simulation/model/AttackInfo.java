package arcircle.ftsim.simulation.model;

import java.util.Random;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.CharaBattleInfo;

public class AttackInfo {

	public Chara attackChara;
	public Chara damageChara;
	public CharaBattleInfo charaBattleInfo;
	private boolean isHit;
	public boolean isHit() {
		return isHit;
	}
	private boolean isDead;
	public boolean isDead() {
		return isDead;
	}

	public AttackInfo(Chara chara, Chara damageChara, CharaBattleInfo charaBattleInfo) {
		this.attackChara = chara;
		this.damageChara = damageChara;
		this.charaBattleInfo = charaBattleInfo;

		calcAttackInfo();
	}

	private void calcAttackInfo() {
		Random random = new Random();
		//必殺の処理
		if (charaBattleInfo.getDeadProbability() >= random.nextInt(100) + 1) {
			isDead = true;
			isHit = true;
		//命中の処理
		} else if (charaBattleInfo.getHitProbability() >= random.nextInt(100) + 1) {
			isDead = false;
			isHit = true;
		}
	}
}
