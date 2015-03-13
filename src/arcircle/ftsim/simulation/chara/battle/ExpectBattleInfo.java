package arcircle.ftsim.simulation.chara.battle;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Weapon;

public class ExpectBattleInfo {
	private CharaBattleInfo firstCharaBattleInfo;
	private CharaBattleInfo secondCharaBattleInfo;

	public ExpectBattleInfo(Chara firstChara, Weapon firstWeapon, SupportInfo firstSupportInfo,
			Chara secondChara, Weapon secondWeapon, SupportInfo secondSupportInfo){
		calcBattleInfo(firstChara, firstWeapon, firstSupportInfo, secondChara, secondWeapon, secondSupportInfo);
	}

	/**
	 * 戦闘する2キャラの戦闘予想情報を計算する
	 * @param firstChara
	 * @param firstWeapon
	 * @param secondChara
	 * @param secondWeapon
	 */
	public void calcBattleInfo(Chara firstChara, Weapon firstWeapon, SupportInfo firstSupportInfo,
			Chara secondChara, Weapon secondWeapon, SupportInfo secondSupportInfo) {

		//TODO ここはWeaponの方でちゃんと実装できたら消す, まだ実装できてないため適当に入れてる
		firstWeapon.hitProbability = 10;
		secondWeapon.hitProbability = 10;
		firstWeapon.deadProbability = 10;
		secondWeapon.deadProbability = 10;

		// もろもろ計算
		int firstPower = firstChara.status.power + firstWeapon.power + firstSupportInfo.getPower();
		int secondPower = secondChara.status.power + secondWeapon.power + secondSupportInfo.getPower();
		int firstDefence = firstChara.status.defence + firstSupportInfo.getDefence();
		int secondDefence = secondChara.status.defence + secondSupportInfo.getDefence();
		int firstHitProb = firstWeapon.hitProbability + firstChara.status.tech * 2 + firstChara.status.luck / 2 + firstSupportInfo.getHitProbability();
		int secondHitProb = secondWeapon.hitProbability + secondChara.status.tech * 2 + secondChara.status.luck / 2 + secondSupportInfo.getHitProbability();
		int firstAvoidProb = firstChara.status.speed * 2 + firstChara.status.luck + firstSupportInfo.getAvoidProbability();
		int secondAvoidProb = secondChara.status.speed * 2 + secondChara.status.luck + secondSupportInfo.getAvoidProbability();
		int firstDeadProb = firstWeapon.deadProbability + firstChara.status.tech / 2 + firstSupportInfo.getDeadProbability();
		int secondDeadProb = secondWeapon.deadProbability + secondChara.status.tech / 2 + secondSupportInfo.getDeadProbability();
		int firstAvoidDeadProb = firstChara.status.luck + firstSupportInfo.getAvoidDeadProbability();
		int secondAvoidDeadProb = secondChara.status.luck + secondSupportInfo.getAvoidDeadProbability();

		// 先攻のデータ作成
		firstCharaBattleInfo = new CharaBattleInfo(
				firstChara.status.hp,
				firstPower - secondDefence,
				isTwiceAttack(firstChara, secondChara),
				firstHitProb - secondAvoidProb,
				firstDeadProb - secondAvoidDeadProb);

		// 後攻のデータ
		secondCharaBattleInfo = new CharaBattleInfo(
				secondChara.status.hp,
				secondPower - firstDefence,
				isTwiceAttack(secondChara, firstChara),
				secondHitProb - firstAvoidProb,
				secondDeadProb - firstAvoidDeadProb);

	}

	private boolean isTwiceAttack(Chara firstChara, Chara secondChara){
		if (firstChara.status.speed - secondChara.status.speed >= 4){
			return true;
		}
		return false;
	}

	public CharaBattleInfo getFirstCharaBattleInfo() {
		return firstCharaBattleInfo;
	}

	public void setFirstCharaBattleInfo(CharaBattleInfo firstCharaBattleInfo) {
		this.firstCharaBattleInfo = firstCharaBattleInfo;
	}

	public CharaBattleInfo getSecondCharaBattleInfo() {
		return secondCharaBattleInfo;
	}

	public void setSecondCharaBattleInfo(CharaBattleInfo secondCharaBattleInfo) {
		this.secondCharaBattleInfo = secondCharaBattleInfo;
	}



}
