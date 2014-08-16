package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import arcircle.ftsim.simulation.chara.Character;
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

	public ArrayList<Character> characterArray;

	public Characters(SimGameModel sgModel, int row, int col) {
		this.sgModel = sgModel;
		this.row = row;
		this.col = col;
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

		this.characterArray = new ArrayList<Character>();
	}

	public void init() {
		String charaPath = sgModel.getStoriesFolder() + "/"
				+ charactersFolderPath;
		File dir = new File(charaPath);
		String[] files = dir.list();
		for (String charaName : files) {
			loadCharaChip(charaPath + charaName + "/", charaName);
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

				Character chara = new Character(charaPuts[0]);
				chara.x = Integer.valueOf(charaPuts[1]);
				chara.y = Integer.valueOf(charaPuts[2]);

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

	public void render(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX,
			int firstTileY, int lastTileY) {
		for (Character chara : characterArray) {
			//範囲内でなければ
			if (!(firstTileX <= chara.x && chara.x <= lastTileX
				&& firstTileY <= chara.y && chara.y <= lastTileY)) {
				continue;
			}
			Animation anime = stayAnimeMap.get(chara.name);

			if (chara.isSelect) {
				anime = attackAnimeMap.get(chara.name);
			}

			anime.draw(
					Field.tilesToPixels(chara.x) + offsetX,
					Field.tilesToPixels(chara.y) + offsetY);
		}
	}
}
