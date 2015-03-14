package arcircle.ftsim.simulation.chara.ai;

import java.util.ArrayList;

public abstract class SelectAttackTarget {

	// 一番いいアタックキャラデータを頼む
	public AttackCharaData getAttackTargetCharaData(ArrayList<AttackCharaData> attackCharaArray){
		int minCost = 999999;
		AttackCharaData minAttackCharaData = null;
		for (AttackCharaData acd : attackCharaArray){
			int cost = calculateCost(acd);
			if (cost < minCost){
				minCost = cost;
				minAttackCharaData = acd;
			}
		}
		return minAttackCharaData;
	}

	// ヒューリスティック関数は抽象化
	abstract protected int calculateCost(AttackCharaData attackCharaData);

}
