package arcircle.ftsim.simulation.chara.ai;

import java.util.ArrayList;

public abstract class SelectAttackTarget {

	// 一番いいアタックキャラデータを頼む
	public AttackCharaData getAttackTargetCharaData(ArrayList<AttackCharaData> attackCharaArray){
		int minCost = 99999;
		AttackCharaData minAttackCharaData = null;
		for (AttackCharaData acd : attackCharaArray){
			int cost = calculateCost(acd);
			if (cost < minCost){
				minCost = cost;
				minAttackCharaData = acd;
			}
		}
		System.out.println("Selected:"+minCost);
		return minAttackCharaData;
	}

	// ヒューリスティック関数は抽象化
	abstract protected int calculateCost(AttackCharaData attackCharaData);

}
