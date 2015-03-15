package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import arcircle.ftsim.simulation.item.Item;

public class Status {
	//セーブデータで利用
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public int gender;
	public String name;

	//TODO:クラス（職業）を表示したいので，そこに気をつけて描画！

	//セーブデータで利用
	public int level;
	public int exp;

	//セーブデータで利用
	//パラメータ
	//HP
	private int hp;
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
		if (this.hp < 0) {
			this.hp = 0;
		} else if(this.hp > this.maxHp) {
			this.hp = this.maxHp;
		}
	}

	//MHP
	public int maxHp;
	//ちから
	public int power;
	//まりょく
	public int magicPower;
	//はやさ
	public int speed;
	//わざ
	public int tech;
	//運
	public int luck;
	//ぼうぎょ
	public int defense;
	//まぼう　（まほうぼうぎょりょく）
	public int magicDefense;
	//いどう
	public int move;
	//たいかく
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
		status.hp = 			this.maxHp;
		status.maxHp = 			this.maxHp;
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
