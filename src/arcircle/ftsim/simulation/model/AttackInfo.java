package arcircle.ftsim.simulation.model;

import arcircle.ftsim.simulation.chara.Chara;

public class AttackInfo {
	public AttackInfo(Chara chara, Chara damageChara) {
		this.attackChara = chara;
		this.damageChara = damageChara;
	}
	public Chara attackChara;
	public Chara damageChara;
}
