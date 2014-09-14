package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import arcircle.ftsim.simulation.item.Item;

public class Status {
	//セーブデータで利用
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public int gender;
	public String name;

	//セーブデータで利用
	public int level;
	public int exp;

	//セーブデータで利用
	//パラメータ
	public int hp;
	public int power;
	public int magicPower;
	public int spead;
	public int tech;
	public int luck;
	public int defence;
	public int magicDefence;
	public int move;
	public int physique;

	public Status() {
		super();

		itemList = new ArrayList<Item>();
	}

	private ArrayList<Item> itemList;

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}

	public void copyTo (Status status) {
		status.hp = 			this.hp;
		status.power = 			this.power;
		status.magicPower = 	this.magicPower;
		status.spead = 			this.spead;
		status.tech = 			this.tech;
		status.luck = 			this.luck;
		status.defence = 		this.defence;
		status.magicDefence = 	this.magicDefence;
		status.move = 			this.move;
		status.physique = 		this.physique;
	}
}
