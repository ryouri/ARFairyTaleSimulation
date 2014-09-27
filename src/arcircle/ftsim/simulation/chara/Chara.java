package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import arcircle.ftsim.simulation.item.Item;

/**
 * 現在，耐久値を設定できるようになっていない
 * 設定できるようにしたら，itemListへのアイテムのコピーの実装を変更する必要がある
 */
public class Chara {
	public int x;
	public int y;

	public int pX;
	public int pY;

	public boolean isMoving;

	public Status status;

	public GrowRateStatus growRateStatus;

	public int direction;
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public boolean isSelect;

	public ArrayList<Item> getItemList() {
		return status.getItemList();
	}

	public void setItemList(ArrayList<Item> itemList) {
		status.setItemList(itemList);
	}

	public Chara(String name) {
		this.status = new Status();
		this.growRateStatus = new GrowRateStatus();
		this.status.setItemList(new ArrayList<Item>());
		this.status.name = name;
	}
}