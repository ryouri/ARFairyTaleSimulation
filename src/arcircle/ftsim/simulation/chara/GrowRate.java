package arcircle.ftsim.simulation.chara;

import java.io.Serializable;

public class GrowRate implements Serializable {
	//成長率
	public int hp;
	public int power;
	public int magicPower;
	public int speed;
	public int tech;
	public int luck;
	public int defense;
	public int magicDefense;
	public int move;
	public int physique;

	public void copyTo (GrowRate status) {
		status.hp = 			this.hp;
		status.power = 			this.power;
		status.magicPower = 	this.magicPower;
		status.speed = 			this.speed;
		status.tech = 			this.tech;
		status.luck = 			this.luck;
		status.defense = 		this.defense;
		status.magicDefense = 	this.magicDefense;
		status.move = 			this.move;
		status.physique = 		this.physique;
	}
}
