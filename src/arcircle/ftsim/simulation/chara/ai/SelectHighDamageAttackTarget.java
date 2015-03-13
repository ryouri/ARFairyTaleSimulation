package arcircle.ftsim.simulation.chara.ai;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.CharaBattleInfo;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.chara.battle.SupportInfo;
import arcircle.ftsim.simulation.item.Weapon;

public class SelectHighDamageAttackTarget extends SelectAttackTarget {

	//バトル予測の結果から、コストを計算、コストは低いほうがいいのでマイナスをつける
	//コスト = -(命中率*ダメージ*攻撃回数-HP)
	@Override
	protected int calculateCost(AttackCharaData attackCharaData) {
		Chara firstChara = attackCharaData.attackChara;
		Weapon firstWeapon = firstChara.getEquipedWeapon();
		SupportInfo firstInfo = new SupportInfo();
		Chara secondChara = attackCharaData.damageChara;
		Weapon secondWeapon = secondChara.getEquipedWeapon();
		SupportInfo secondInfo = new SupportInfo();

		ExpectBattleInfo expectInfo = new ExpectBattleInfo(firstChara, firstWeapon, firstInfo, secondChara, secondWeapon, secondInfo);
		CharaBattleInfo firstCharaInfo = expectInfo.getFirstCharaBattleInfo();
		CharaBattleInfo secondCharaInfo = expectInfo.getSecondCharaBattleInfo();

		int attack_num = firstCharaInfo.isTwiceAttack() ? 2 : 1;
		int cost = -(firstCharaInfo.getHitProbability() * firstCharaInfo.getDamage() * attack_num - secondCharaInfo.getHp());
		return cost;
	}

}
