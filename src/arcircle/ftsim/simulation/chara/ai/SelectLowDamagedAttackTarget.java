package arcircle.ftsim.simulation.chara.ai;

import java.awt.Point;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.CharaBattleInfo;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.chara.battle.SupportInfo;
import arcircle.ftsim.simulation.item.Item;

public class SelectLowDamagedAttackTarget extends SelectAttackTarget {

	//バトル予測の結果から一番ダメージを受けなさそうなのを選ぶ
	//コスト = 相手の命中率*相手のダメージ*相手の攻撃回数 - HP
	@Override
	protected int calculateCost(AttackCharaData attackCharaData) {
		Chara firstChara = attackCharaData.attackChara;
		Item firstWeapon = firstChara.getEquipedWeapon();
		SupportInfo firstInfo = new SupportInfo();
		Chara secondChara = attackCharaData.damageChara;
		Item secondWeapon = secondChara.getEquipedWeapon();
		SupportInfo secondInfo = new SupportInfo();

		Point attackPoint = attackCharaData.attackPoint;

		ExpectBattleInfo expectInfo =
				new ExpectBattleInfo(firstChara, firstWeapon, firstInfo, secondChara, secondWeapon, secondInfo, attackPoint);
		CharaBattleInfo firstCharaInfo = expectInfo.getFirstCharaBattleInfo();
		CharaBattleInfo secondCharaInfo = expectInfo.getSecondCharaBattleInfo();

		int attack_num = secondCharaInfo.isTwiceAttack() ? 2 : 1;
		int cost = secondCharaInfo.getHitProbability() * secondCharaInfo.getDamage() * attack_num - firstCharaInfo.getHp();
		return cost;
	}

}
