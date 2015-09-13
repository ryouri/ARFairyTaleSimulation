package arcircle.ftsim.simulation.chara.battle;

import java.awt.Point;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.SupportItem;
import arcircle.ftsim.simulation.item.Weapon;

public class ExpectBattleInfo {
	private CharaBattleInfo firstCharaBattleInfo;
	private CharaBattleInfo secondCharaBattleInfo;
	private Chara firstChara;
	private Chara secondChara;
	private Item firstWeapon;
	private Item secondWeapon;
	private Point attackPoint;

	/**
	 * [攻撃側のrange][被攻撃側のrange]で，trueなら攻撃可能
	 * NEAR:0, FAR:1, NEARFAR:2
	 */
	private boolean[][] rangeAttackable =
		{
			{true,  false, true}, //[NEAR][?]
			{false, true,  true}, //[FAR][?]
			{}
		};


	public ExpectBattleInfo(Chara firstChara, Item firstWeapon, SupportInfo firstSupportInfo,
			Chara secondChara, Item secondWeapon, SupportInfo secondSupportInfo, Point attackPoint){
		this.firstChara = firstChara;
		this.secondChara = secondChara;
		this.firstWeapon = firstWeapon;
		this.secondWeapon = secondWeapon;
		this.attackPoint = attackPoint;
		calcBattleInfo(firstChara, firstWeapon, firstSupportInfo, secondChara, secondWeapon, secondSupportInfo);
	}

	/**
	 * 戦闘する2キャラの戦闘予想情報を計算する
	 * @param firstChara
	 * @param firstItem
	 * @param secondChara
	 * @param secondItem
	 */
	public void calcBattleInfo(Chara firstChara, Item firstItem, SupportInfo firstSupportInfo,
			Chara secondChara, Item secondItem, SupportInfo secondSupportInfo) {
		//攻撃側は攻撃できるはずだが，攻撃を受ける側はそうとは限らない
		//そのため，攻撃を受ける側は，攻撃可能かどうかを判定する必要がある

		Weapon firstWeapon = null;
		if (firstItem instanceof Weapon) {
			firstWeapon = (Weapon) firstItem;
			//TODO ここはWeaponの方でちゃんと実装できたら消す, まだ実装できてないため適当に入れてる
			firstWeapon.hitProbability = 100;
			firstWeapon.deadProbability = 3;
		}

		boolean isSecondAttackable = true;
		//サポートアイテムならダメ
		if (secondItem instanceof SupportItem) {
			isSecondAttackable = false;
		} else if (secondItem instanceof Weapon) {
			Weapon secondWeapon = (Weapon)secondItem;
			int distance = Math.abs(attackPoint.x - secondChara.x) + Math.abs(attackPoint.y - secondChara.y);
			//射程距離があっていなければダメ
			if (distance == 1 && secondWeapon.rangeType == Weapon.RANGE_FAR) {
				isSecondAttackable = false;
			} else if (distance == 2 && secondWeapon.rangeType == Weapon.RANGE_NEAR) {
				isSecondAttackable = false;
			}

			System.out.println("distance:" + distance +
					" aX," + attackPoint.x + " aY," + attackPoint.y + " dX," + secondChara.x + " dY," + secondChara.y);
		}

		if (isSecondAttackable) {
			Weapon secondWeapon = (Weapon)secondItem;
			secondWeapon.hitProbability = 100;
			secondWeapon.deadProbability = 3;
		}

		if (firstWeapon == null) {
			System.err.println("最初のキャラの武器がnull");
			return;
		}
		// もろもろ計算
		int firstPower = firstChara.status.power + firstWeapon.power + firstSupportInfo.getPower();
		int firstDefence = firstChara.status.defense + firstSupportInfo.getDefence();
		int firstHitProb = firstWeapon.hitProbability + firstChara.status.tech * 2 + firstChara.status.luck / 2 + firstSupportInfo.getHitProbability();
		int firstAvoidProb = firstChara.status.speed * 2 + firstChara.status.luck + firstSupportInfo.getAvoidProbability();
		int firstDeadProb = firstWeapon.deadProbability + firstChara.status.tech / 2 + firstSupportInfo.getDeadProbability();
		int firstAvoidDeadProb = firstChara.status.luck + firstSupportInfo.getAvoidDeadProbability();

		int secondPower;
		int secondDefence;
		int secondHitProb;
		int secondAvoidProb;
		int secondDeadProb;
		int secondAvoidDeadProb;

		if (isSecondAttackable) {
			Weapon secondWeapon = (Weapon)secondItem;
			secondPower = secondChara.status.power + secondWeapon.power + secondSupportInfo.getPower();
			secondDefence = secondChara.status.defense + secondSupportInfo.getDefence();
			secondHitProb = secondWeapon.hitProbability + secondChara.status.tech * 2 + secondChara.status.luck / 2 + secondSupportInfo.getHitProbability();
			secondAvoidProb = secondChara.status.speed * 2 + secondChara.status.luck + secondSupportInfo.getAvoidProbability();
			secondDeadProb = secondWeapon.deadProbability + secondChara.status.tech / 2 + secondSupportInfo.getDeadProbability();
			secondAvoidDeadProb = secondChara.status.luck + secondSupportInfo.getAvoidDeadProbability();
		} else {
			secondPower = 0;
			secondDefence = secondChara.status.defense + secondSupportInfo.getDefence();
			secondHitProb = 0;
			secondAvoidProb = secondChara.status.speed * 2 + secondChara.status.luck + secondSupportInfo.getAvoidProbability();
			secondDeadProb = 0;
			secondAvoidDeadProb = secondChara.status.luck + secondSupportInfo.getAvoidDeadProbability();
		}

		// 先攻のデータ作成
		firstCharaBattleInfo = new CharaBattleInfo(
				firstChara.status.getHp(),
				firstPower - secondDefence,
				isTwiceAttack(firstChara, secondChara),
				firstHitProb - secondAvoidProb,
				firstDeadProb - secondAvoidDeadProb,
				true);

		boolean isSecondTwiceAttack;
		if (isSecondAttackable) {
			isSecondTwiceAttack = isTwiceAttack(secondChara, firstChara);
		} else {
			isSecondTwiceAttack = false;
		}

		// 後攻のデータ
		secondCharaBattleInfo = new CharaBattleInfo(
				secondChara.status.getHp(),
				secondPower - firstDefence,
				isSecondTwiceAttack,
				secondHitProb - firstAvoidProb,
				secondDeadProb - firstAvoidDeadProb,
				isSecondAttackable);
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

	public Chara getFirstChara() {
		return firstChara;
	}

	public Chara getSecondChara() {
		return secondChara;
	}

	public Item getFirstItem() {
		return firstWeapon;
	}
	public Item getSecondItem() {
		return secondWeapon;
	}
}
