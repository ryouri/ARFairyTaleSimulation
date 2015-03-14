package arcircle.ftsim.simulation.chara;

public class GrowRateStatus {
	//成長率
	public int hp;
	public int power;
	public int magicPower;
	public int speed;
	public int tech;
	public int luck;
	public int defence;
	public int magicDefence;
	public int move;
	public int physique;

	public void copyTo (GrowRateStatus status) {
		status.hp = 			this.hp;
		status.power = 			this.power;
		status.magicPower = 	this.magicPower;
		status.speed = 			this.speed;
		status.tech = 			this.tech;
		status.luck = 			this.luck;
		status.defence = 		this.defence;
		status.magicDefence = 	this.magicDefence;
		status.move = 			this.move;
		status.physique = 		this.physique;
	}
}
