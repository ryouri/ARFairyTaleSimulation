package arcircle.ftsim.simulation.chara.battle;

public class CharaBattleInfo {
	public CharaBattleInfo(int hp, int damage, boolean twiceAttack, int hitProbability, int deadProbability){
		this.hp = hp;
		this.damage = damage;
		this.twiceAttack = twiceAttack;
		this.hitProbability = hitProbability;
		this.deadProbability = deadProbability;
	}

	private int hp;

	private int damage;
	private boolean twiceAttack;

	private int hitProbability;

	private int deadProbability;

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int power) {
		this.damage = power;
	}

	public boolean isTwiceAttack() {
		return twiceAttack;
	}

	public void setTwiceAttack(boolean twiceAttack) {
		this.twiceAttack = twiceAttack;
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



}
