package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import org.newdawn.slick.tests.xml.Item;

public class Character {
	public int x;
	public int y;

	public String name;
	public int gender;

	public int level;
	public int exp;

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

	//成長率
	public int growRateHP;
	public int growRatePower;
	public int growRateMagicPower;
	public int growRateSpead;
	public int growRateTech;
	public int growRateLuck;
	public int growRateDefence;
	public int growRateMagicDefence;
	public int growRateMove;
	public int growRatePhysique;

	public ArrayList<Item> itemList;

	public int direction;
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public boolean isSelect;

	public Character(String name) {
		this.name = name;
	}
}