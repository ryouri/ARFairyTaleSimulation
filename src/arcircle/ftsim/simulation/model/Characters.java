package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Characters {
	private SimGameModel sgModel;

	private int row;
	private int col;

	public static String charactersFolderPath = "Characters/";

	public HashMap<String, SpriteSheet> walkSheetMap;
	public HashMap<String, SpriteSheet> readySheetMap;
	public HashMap<String, SpriteSheet> attackSheetMap;

	public HashMap<String, Animation> upWalkAnimeMap;
	public HashMap<String, Animation> downWalkAnimeMap;
	public HashMap<String, Animation> leftWalkAnimeMap;
	public HashMap<String, Animation> rightWalkAnimeMap;

	public HashMap<String, Animation> stayAnimeMap;
	public HashMap<String, Animation> cursorAnimeMap;

	public HashMap<String, Animation> attackAnimeMap;

	public HashMap<String, Chara> characterData;

	public ArrayList<Chara> characterArray;

	HashMap<String, Item> itemList;

	public Characters() {
		this.walkSheetMap = new HashMap<String, SpriteSheet>();
		this.readySheetMap = new HashMap<String, SpriteSheet>();
		this.attackSheetMap = new HashMap<String, SpriteSheet>();

		this.upWalkAnimeMap = new HashMap<String, Animation>();
		this.downWalkAnimeMap = new HashMap<String, Animation>();
		this.leftWalkAnimeMap = new HashMap<String, Animation>();
		this.rightWalkAnimeMap = new HashMap<String, Animation>();
		this.stayAnimeMap = new HashMap<String, Animation>();
		this.cursorAnimeMap = new HashMap<String, Animation>();
		this.attackAnimeMap = new HashMap<String, Animation>();

		this.characterArray = new ArrayList<Chara>();

		this.characterData = new HashMap<String, Chara>();
	}

	public void init(SimGameModel sgModel, int row, int col, HashMap<String, Item> itemList) {
		this.sgModel = sgModel;
		this.row = row;
		this.col = col;
		this.itemList = itemList;

		String charaPath = sgModel.getStoriesFolder() + "/"
				+ charactersFolderPath;
		File dir = new File(charaPath);
		String[] files = dir.list();
		for (String charaName : files) {
			loadCharaChip(charaPath + charaName + "/", charaName);
			loadCharaParameter(charaPath + charaName + "/", charaName);
		}
		generateAnimation();
	}

	private static int[] walkPattern = {0, 1, 2, 1};
	private static int[] duration = {250, 250, 250, 250};
	private static int[] attackDuration = {100, 100, 700, 100};
	private void generateAnimation() {
		for (String charaName : walkSheetMap.keySet()) {
			SpriteSheet walkSheet = walkSheetMap.get(charaName);

			//4方向の歩くImage配列
			Image[] upImages = generateImageArray(walkSheet, 3);
			Image[] downImages = generateImageArray(walkSheet, 0);
			Image[] leftImages = generateImageArray(walkSheet, 1);
			Image[] rightImages = generateImageArray(walkSheet, 2);

			SpriteSheet readySheet = readySheetMap.get(charaName);
			//
			Image[] stayImages = generateImageArray(readySheet, 0);
			Image[] cursorImages = generateImageArray(readySheet, 3);

			SpriteSheet attackSheet = attackSheetMap.get(charaName);
			Image[] nearAttackImages = generateImageArray(attackSheet, 0);

			upWalkAnimeMap.put(charaName, new Animation(upImages, duration, true));
			downWalkAnimeMap.put(charaName, new Animation(downImages, duration, true));
			leftWalkAnimeMap.put(charaName, new Animation(leftImages, duration, true));
			rightWalkAnimeMap.put(charaName, new Animation(rightImages, duration, true));

			stayAnimeMap.put(charaName, new Animation(stayImages, duration, true));
			cursorAnimeMap.put(charaName, new Animation(cursorImages, duration, true));

			attackAnimeMap.put(charaName, new Animation(nearAttackImages, attackDuration, true));
		}
	}

	/**
	 * RPGツクール形式の画像のシートから
	 * subImageYのy座標の画像をwalkPatternにしたがってImage[4]に直して返す
	 * @param sheet
	 * @param subImageY
	 * @return
	 */
	private Image[] generateImageArray(SpriteSheet sheet, int subImageY) {
		Image[] images = new Image[4];
		int imageIndex = 0;
		for (int pattern : walkPattern) {
			images[imageIndex++] = sheet.getSubImage(pattern, subImageY);
		}
		return images;
	}

	public static String readyFile = "ready.png";
	public static String walkFile = "walk.png";
	public static String attackFile = "attack.png";
	private void loadCharaChip(String charaFolderPath, String charaName){
		try {
			walkSheetMap.put(charaName,
					new SpriteSheet(
							new Image(charaFolderPath + walkFile),
							Field.MAP_CHIP_SIZE,
							Field.MAP_CHIP_SIZE));
			readySheetMap.put(charaName,
					new SpriteSheet(
							new Image(charaFolderPath + readyFile),
							Field.MAP_CHIP_SIZE,
							Field.MAP_CHIP_SIZE));
			attackSheetMap.put(charaName,
					new SpriteSheet(
							new Image(charaFolderPath + attackFile),
							Field.MAP_CHIP_SIZE,
							Field.MAP_CHIP_SIZE));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static String paramaterFile = "parameter.txt";
	private void loadCharaParameter(String charaFolderPath, String charaName) {
		// マップチップ読み込み
		String charaParaPath = charaFolderPath + paramaterFile;
		try {
			File file = new File(charaParaPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			Chara chara = new Chara(charaName);

			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}

				loadCharaParameterLine(line, chara);
			}

			characterData.put(charaName, chara);

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	/*
	NAMES,0,1,0
	STATUS,20,8,5,9,6,10,5,4,6,8
	GROW,50,40,40,60,60,60,40,40,0,0
	ITEM,けん
	 */
	public static final String NAMES = "NAMES";
	public static final String STATUS = "STATUS";
	public static final String GROW = "GROW";
	public static final String ITEM = "ITEM";
	private void loadCharaParameterLine(String line, Chara chara) {
		String[] charaStrs = line.split(",");

//		NAMES,0,1,0
		if (charaStrs[0].equals(NAMES)) {
			chara.status.gender = 	Integer.valueOf(charaStrs[1]);
			chara.status.level = 	Integer.valueOf(charaStrs[2]);
			chara.status.exp = 		Integer.valueOf(charaStrs[3]);
		}
//		STATUS,20,8,5,9,6,10,5,4,6,8
		if (charaStrs[0].equals(STATUS)) {
			chara.status.hp = 			Integer.valueOf(charaStrs[1]);
			chara.status.power = 		Integer.valueOf(charaStrs[2]);
			chara.status.magicPower = 	Integer.valueOf(charaStrs[3]);
			chara.status.spead = 		Integer.valueOf(charaStrs[4]);
			chara.status.tech = 		Integer.valueOf(charaStrs[5]);
			chara.status.luck = 		Integer.valueOf(charaStrs[6]);
			chara.status.defence = 		Integer.valueOf(charaStrs[7]);
			chara.status.magicDefence = Integer.valueOf(charaStrs[8]);
			chara.status.move = 		Integer.valueOf(charaStrs[9]);
			chara.status.physique = 	Integer.valueOf(charaStrs[10]);
		}
//		GROW,50,40,40,60,60,60,40,40,0,0
		if (charaStrs[0].equals(GROW)) {
			chara.growRateStatus.hp = 			Integer.valueOf(charaStrs[1]);
			chara.growRateStatus.power = 		Integer.valueOf(charaStrs[2]);
			chara.growRateStatus.magicPower = 	Integer.valueOf(charaStrs[3]);
			chara.growRateStatus.spead = 		Integer.valueOf(charaStrs[4]);
			chara.growRateStatus.tech = 		Integer.valueOf(charaStrs[5]);
			chara.growRateStatus.luck = 		Integer.valueOf(charaStrs[6]);
			chara.growRateStatus.defence = 		Integer.valueOf(charaStrs[7]);
			chara.growRateStatus.magicDefence = Integer.valueOf(charaStrs[8]);
			chara.growRateStatus.move = 		Integer.valueOf(charaStrs[9]);
			chara.growRateStatus.physique = 	Integer.valueOf(charaStrs[10]);
		}
//		ITEM,けん
		if (charaStrs[0].equals(ITEM)) {
			for (int i = 1; i < charaStrs.length; i++) {
				chara.getItemList().add(itemList.get(charaStrs[i]));
			}
		}
	}

	public void addCharacters(String putCharacterPath) {
		BufferedReader br = null;
		try {
			File file = new File(putCharacterPath);
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println(e);
			return;
		} catch (IOException e) {
			System.out.println(e);
			return;
		}

		try {
			String charaLine;
			while ((charaLine = br.readLine()) != null) {
				String[] charaPuts = charaLine.split(" ");
				if (charaPuts.length == 0) {
					continue;
				}

				Chara chara = new Chara(charaPuts[0]);
				chara.x = Integer.valueOf(charaPuts[1]);
				chara.y = Integer.valueOf(charaPuts[2]);
				chara.pX = chara.x * Field.MAP_CHIP_SIZE;
				chara.pY = chara.y * Field.MAP_CHIP_SIZE;
				//TODO; キャラクターデータのコピーが未完成
				chara.setItemList(characterData.get(chara.status.name).getItemList());
				characterData.get(chara.status.name).status.copyTo(chara.status);

				characterArray.add(chara);
			}
		} catch (NumberFormatException | IOException e1) {
			e1.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final Color standColor = new Color(0.5f, 0.5f, 0.5f, 1);

	public void render(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX,
			int firstTileY, int lastTileY) {
		for (Chara chara : characterArray) {
			//範囲内でなければ
			if (!(firstTileX <= chara.x && chara.x <= lastTileX
				&& firstTileY <= chara.y && chara.y <= lastTileY)) {
				continue;
			}

			if (chara.isStand()) {
				Animation anime = downWalkAnimeMap.get(chara.status.name);
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY,
						standColor);
			} else if (chara.isMoving) {
				Animation anime = null;
				if (chara.direction == Chara.UP) {
					anime = upWalkAnimeMap.get(chara.status.name);
				} else if (chara.direction == Chara.RIGHT) {
					anime = rightWalkAnimeMap.get(chara.status.name);
				} else if (chara.direction == Chara.LEFT) {
					anime = leftWalkAnimeMap.get(chara.status.name);
				} else {//if (chara.direction == Chara.DOWN) {
					anime = downWalkAnimeMap.get(chara.status.name);
				}
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY);
			} else if (chara.isSelect) {
				Animation anime = attackAnimeMap.get(chara.status.name);
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY);
			} else {
				Animation anime = stayAnimeMap.get(chara.status.name);
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY);
			}
		}
	}
}
