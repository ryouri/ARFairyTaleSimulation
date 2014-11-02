package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.Weapon;
import arcircle.ftsim.state.simgame.SimGameModel;

public class World {
	/**
	 * ゲームはFieldを大元に構成される
	 */
	Field field;

	/**
	 * 様々な機能にアクセスするために持っておく
	 */
	SimGameModel sgModel;


	/**
	 * 右側に表示される情報ウィンドウ
	 */
	SubInfoWindow subInfoWindow;

	/**
	 * ゲームに共通な，アイテムのリストを保持しておく
	 */
	public HashMap<String, Item> itemList;

	public World(SimGameModel simGameModel) {
		this.itemList = new HashMap<String, Item>();
		loadItemList();

		this.sgModel = simGameModel;
		this.field = new Field(simGameModel, itemList);

		String subStoryFolderPath = sgModel.getStoriesFolder() + "/"
				+ sgModel.sectionPath + "/"
				+ sgModel.subStoryPath + "/";
		field.init(subStoryFolderPath);

		this.subInfoWindow = new SubInfoWindow(field);

		sgModel.pushKeyInputStack(field);
		sgModel.addRendererArray(field);
		sgModel.addRendererArray(subInfoWindow);
	}

	public void loadItemList() {
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
	}

	//けん,TYPE_WEAPON,RANGE_NEAR,5,4,longsword.png
	public Item loadItem(String itemStr) {
		String[] itemStrs = itemStr.split(",");

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
			String itemIconPath = "image/item/" + itemStrs[5];
			try {
				weapon.itemIcon = new Image(itemIconPath);
			} catch (SlickException e) {
				e.printStackTrace();
			}

			return weapon;
		}
		if (itemStrs[1].equals(Item.TYPE_SUPPORT_STR)) {
		}
		if (itemStrs[1].equals(Item.TYPE_USE_STR)) {
		}

		return null;
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		field.update(container, game, delta);
	}
}
