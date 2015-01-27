package arcircle.ftsim.simulation.chara.battle;

/**
 * @author misawa
 *
 */
public class SupportInfo {
	private int power;
	private int defence;
	private int hitProbability;
	private int deadProbability;

	public SupportInfo() {
		this.power = 0;
		this.defence = 0;
		this.hitProbability = 0;
		this.deadProbability = 0;
	}

	public SupportInfo(int power, int defence, int hitProbability, int deadProbability) {
		this.power = power;
		this.defence = defence;
		this.hitProbability = hitProbability;
		this.deadProbability = deadProbability;
	}
}
