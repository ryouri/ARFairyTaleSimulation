package arcircle.ftsim.simulation.chara.ai;

public class SelectLowerDefenceAttackTarget extends SelectAttackTarget {

	//ディフェンスが低い方がコストが低い
	@Override
	protected int calculateCost(AttackCharaData attackCharaData) {
		return attackCharaData.damageChara.status.defence;
	}

}
