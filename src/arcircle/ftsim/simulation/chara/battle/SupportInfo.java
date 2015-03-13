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
	private int avoidProbability;
	private int avoidDeadProbability;

	public SupportInfo() {
		this.power = 0;
		this.defence = 0;
		this.hitProbability = 0;
		this.deadProbability = 0;
		this.avoidDeadProbability = 0;
		this.avoidProbability = 0;
	}

	public SupportInfo(int power, int defence, int hitProbability, int deadProbability, int avoidProbability, int avoidDeadProbability) {
		this.power = power;
		this.defence = defence;
		this.hitProbability = hitProbability;
		this.deadProbability = deadProbability;
		this.avoidProbability = avoidProbability;
		this.avoidDeadProbability = avoidDeadProbability;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getHitProbability() {
		return hitProbability;
	}

	public void setHitProbability(int hitProbability) {
		this.hitProbability = hitProbability;
	}

	public int getDeadProbability() {
		return deadProbability;
	}

	public void setDeadProbability(int deadProbability) {
		this.deadProbability = deadProbability;
	}

	public int getAvoidProbability() {
		return avoidProbability;
	}

	public void setAvoidProbability(int avoidProbability) {
		this.avoidProbability = avoidProbability;
	}

	public int getAvoidDeadProbability() {
		return avoidDeadProbability;
	}

	public void setAvoidDeadProbability(int avoidDeadProbability) {
		this.avoidDeadProbability = avoidDeadProbability;
	}



}
