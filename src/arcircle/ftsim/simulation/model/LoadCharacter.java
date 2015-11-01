package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.field.LoadField;

public class LoadCharacter {
	private Characters characters;

	public LoadCharacter(Characters characters) {
		this.characters = characters;
	}

	public void load() {
		loadCharacters();

		generateAnimation();
	}

	/**
	 * キャラクターを読み込む
	 */
	private void loadCharacters() {
		String charaPath = characters.getSgModel().getStoriesFolder() + "/"
				+ characters.CharactersFolderName;
		File dir = new File(charaPath);
		String[] files = dir.list();
		for (String charaFolderName : files) {
			loadCharaChip(charaPath + charaFolderName + "/", charaFolderName);
			loadCharaParameter(charaPath + charaFolderName + "/", charaFolderName);
		}
	}


	public static String walkFile = "walk.png";
	public static String faceStandardFile = "faceStandard.png";
	/**
	 * キャラクターのマップチップを読み込む
	 * @param charaFolderPath キャラクターのフォルダパス
	 * @param charaFolderName キャラクターの名前
	 */
	private void loadCharaChip(String charaFolderPath, String charaFolderName){
		try {
			characters.walkSheetMap.put(charaFolderName,
					new SpriteSheet(
							new Image(charaFolderPath + walkFile),
							LoadField.MAP_CHIP_SIZE,
							LoadField.MAP_CHIP_SIZE));

			//キャラの顔画像を読み込む
			characters.characterFaceStandardImageMap.put(charaFolderName,
					new Image(charaFolderPath + faceStandardFile).getScaledCopy(1.25f));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	public static String paramaterFile = "parameter.txt";
	/**
	 * あるキャラクターのデータを読み込む
	 * @param charaFolderPath キャラクターのフォルダパス
	 * @param charaFolderName キャラクターの名前
	 */
	private void loadCharaParameter(String charaFolderPath, String charaFolderName) {
		// マップチップ読み込み
		String charaParaPath = charaFolderPath + paramaterFile;
		try {
			File file = new File(charaParaPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			Chara chara = new Chara(charaFolderName, characters);

			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}

				loadCharaParameterLine(line, chara);

				//
				if (chara.status.className == null) {
					chara.status.className = "normal";
				}
			}

			characters.characterData.put(charaFolderName, chara);

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}


	/*
	NAMES,0,1,0
	STATUS,16,6,6,5,5,5,4,4,5,4
	GROW,50,40,40,60,60,60,40,40,0,0
	ITEM,けん
	 */
	public static final String NAMES = "NAMES";
	public static final String STATUS = "STATUS";
	public static final String GROW = "GROW";
	public static final String CLASS = "CLASS";
	public static final String ITEM = "ITEM";
	/**
	 * ファイルの１行を読み込み，キャラクターデータに追記する
	 * @param line ファイルのどこか一行
	 * @param chara データを書き加える対象のキャラクター
	 */
	private void loadCharaParameterLine(String line, Chara chara) {
		String[] charaStrs = line.split(",");

//		NAMES,0,1,0
		if (charaStrs[0].equals(NAMES)) {
			chara.status.name  =	charaStrs[1];
			chara.status.gender = 	Integer.valueOf(charaStrs[2]);
			chara.status.level = 	Integer.valueOf(charaStrs[3]);
			chara.status.exp = 		Integer.valueOf(charaStrs[4]);
		}
//		STATUS,16,6,6,5,5,5,4,4,5,4
		if (charaStrs[0].equals(STATUS)) {
			chara.status.maxHp = 		Integer.valueOf(charaStrs[1]);
			chara.status.setHp(Integer.valueOf(charaStrs[1]));
			chara.status.power = 		Integer.valueOf(charaStrs[2]);
			chara.status.magicPower = 	Integer.valueOf(charaStrs[3]);
			chara.status.speed = 		Integer.valueOf(charaStrs[4]);
			chara.status.tech = 		Integer.valueOf(charaStrs[5]);
			chara.status.luck = 		Integer.valueOf(charaStrs[6]);
			chara.status.defense = 		Integer.valueOf(charaStrs[7]);
			chara.status.magicDefense = Integer.valueOf(charaStrs[8]);
			chara.status.move = 		Integer.valueOf(charaStrs[9]);
			chara.status.physique = 	Integer.valueOf(charaStrs[10]);
		}
//		GROW,50,40,40,60,60,60,40,40,0,0
		if (charaStrs[0].equals(GROW)) {
			chara.status.growRate.hp = 			Integer.valueOf(charaStrs[1]);
			chara.status.growRate.power = 		Integer.valueOf(charaStrs[2]);
			chara.status.growRate.magicPower = 	Integer.valueOf(charaStrs[3]);
			chara.status.growRate.speed = 		Integer.valueOf(charaStrs[4]);
			chara.status.growRate.tech = 		Integer.valueOf(charaStrs[5]);
			chara.status.growRate.luck = 		Integer.valueOf(charaStrs[6]);
			chara.status.growRate.defense = 		Integer.valueOf(charaStrs[7]);
			chara.status.growRate.magicDefense = Integer.valueOf(charaStrs[8]);
			chara.status.growRate.move = 		Integer.valueOf(charaStrs[9]);
			chara.status.growRate.physique = 	Integer.valueOf(charaStrs[10]);
		}
//		CLASS,クラス名
		if (charaStrs[0].equals(CLASS)) {
			if (this.characters.getField().getLoadField().getTerrainManager().existClassMap.containsKey(charaStrs[1])) {
				chara.status.className = charaStrs[1];
			} else {
				System.err.println("Character class settings error");
				System.exit(1);
			}
		}
//		ITEM,けん
		if (charaStrs[0].equals(ITEM)) {
			for (int i = 1; i < charaStrs.length; i++) {
				chara.getItemList().add(characters.itemMap.get(charaStrs[i]));
			}
		}
	}


	private static int[] walkPattern = {0, 1, 2, 1};
	private static int[] duration = {250, 250, 250, 250};
	private static int[] attackDuration = {75, 75, 75, 75};
	private static int[] selectDuration = {100, 100, 700, 100};
	/**
	 * 読み込んだ画像ファイルからAnimationを生成
	 */
	private void generateAnimation() {
		for (String charaName : characters.walkSheetMap.keySet()) {
			SpriteSheet walkSheet = characters.walkSheetMap.get(charaName);

			//4方向の歩くImage配列
			Image[] upImages = generateImageArray(walkSheet, 3);
			Image[] downImages = generateImageArray(walkSheet, 0);
			Image[] leftImages = generateImageArray(walkSheet, 1);
			Image[] rightImages = generateImageArray(walkSheet, 2);

			characters.upWalkAnimeMap.put(charaName, new Animation(upImages, duration, true));
			characters.downWalkAnimeMap.put(charaName, new Animation(downImages, duration, true));
			characters.leftWalkAnimeMap.put(charaName, new Animation(leftImages, duration, true));
			characters.rightWalkAnimeMap.put(charaName, new Animation(rightImages, duration, true));

			characters.upAttackAnimeMap.put(charaName, new Animation(upImages, attackDuration, true));
			characters.downAttackAnimeMap.put(charaName, new Animation(downImages, attackDuration, true));
			characters.leftAttackAnimeMap.put(charaName, new Animation(leftImages, attackDuration, true));
			characters.rightAttackAnimeMap.put(charaName, new Animation(rightImages, attackDuration, true));
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

}
