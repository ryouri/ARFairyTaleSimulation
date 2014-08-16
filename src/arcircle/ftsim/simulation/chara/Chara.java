package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import org.newdawn.slick.tests.xml.Item;

public class Chara {
	public int x;
	public int y;

	public Status status;

	GrowRateStatus growRateStatus;

	public ArrayList<Item> itemList;

	public int direction;
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public boolean isSelect;

	public Chara(String name) {
		this.status = new Status();
		this.growRateStatus = new GrowRateStatus();
		this.status.name = name;
	}
}