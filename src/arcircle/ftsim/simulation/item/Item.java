package arcircle.ftsim.simulation.item;

import org.newdawn.slick.Image;

public class Item {
	public String name;

	public int type;
	//武器
	public static int TYPE_WEAPON = 0;
	public static String TYPE_WEAPON_STR = "TYPE_WEAPON";
	//補助
	public static int TYPE_SUPPORT = 1;
	public static String TYPE_SUPPORT_STR = "TYPE_SUPPORT";
	//使う
	public static int TYPE_USE = 2;
	public static String TYPE_USE_STR = "TYPE_USE";

	public Image itemIcon;
}
