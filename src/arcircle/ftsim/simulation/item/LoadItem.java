package arcircle.ftsim.simulation.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import arcircle.ftsim.state.simgame.SimGameModel;

public class LoadItem {

	public static HashMap<String, Item> loadItemList() {
		HashMap<String, Item> itemList = new HashMap<String, Item>();

		// マップチップ読み込み
		String itemListPath = SimGameModel.storiesFolder + "/itemlist.txt";
		try {
			File file = new File(itemListPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String itemStr;

			while ((itemStr = br.readLine()) != null) {
				if (itemStr.length() == 0) {
					continue;
				}

				Item item = loadItem(itemStr);
				if (item != null) {
					itemList.put(item.name, item);
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return itemList;
	}

	public static Item loadItem(String itemStr) {
		String[] itemStrs = itemStr.split(",");

		//けん,TYPE_WEAPON,RANGE_NEAR,5,4,longsword.png
		if (itemStrs[1].equals(Item.TYPE_WEAPON_STR)) {
			Weapon weapon = new Weapon();
			weapon.name = itemStrs[0];
			weapon.type = Item.TYPE_WEAPON;
			if (itemStrs[2].equals(Weapon.RANGE_NEAR_STR)) {
				weapon.rangeType = Weapon.RANGE_NEAR;
			} else if (itemStrs[2].equals(Weapon.RANGE_FAR_STR)) {
				weapon.rangeType = Weapon.RANGE_FAR;
			} else if (itemStrs[2].equals(Weapon.RANGE_NEAR_FAR_STR)) {
				weapon.rangeType = Weapon.RANGE_NEAR_FAR;
			}
			weapon.power = Integer.valueOf(itemStrs[3]);
			weapon.weight = Integer.valueOf(itemStrs[4]);
			//直列化出来ないため、いったん削除
//			weapon.itemIcon = loadItemIcon(itemStrs[5]);
			weapon.effectName = itemStrs[6];

			return weapon;
		}

		//つえ,TYPE_SUPPORT,RANGE_NEAR,5
		if (itemStrs[1].equals(Item.TYPE_SUPPORT_STR)) {
			SupportItem supportItem = new SupportItem();
			supportItem.name = itemStrs[0];
			supportItem.type = Item.TYPE_SUPPORT;
			if (itemStrs[2].equals(SupportItem.RANGE_NEAR_STR)) {
				supportItem.rangeType = SupportItem.RANGE_NEAR;
			}
			supportItem.power = Integer.valueOf(itemStrs[3]);
			//直列化出来ないため、いったん削除
//			supportItem.itemIcon = loadItemIcon(itemStrs[4]);
			supportItem.effectName = itemStrs[5];

			return supportItem;
		}
		if (itemStrs[1].equals(Item.TYPE_USE_STR)) {
		}

		return null;
	}

	private static Image loadItemIcon(String itemIconName) {
		String itemIconPath = "image/item/" + itemIconName;
		Image image = null;
		try {
			image = new Image(itemIconPath);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return image;
	}
}
