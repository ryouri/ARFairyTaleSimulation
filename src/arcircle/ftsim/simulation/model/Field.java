package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Field implements KeyListner, Renderer {

	SimGameModel sgModel;

	private int map[][];
	/**
	 * 移動コストが保存される
	 * -1は移動不可能
	 */
	private int moveCostMap[][];

	public static final int MAP_CHIP_ROW = 20;
	public static final int MAP_CHIP_COL = 10;
	public static final int MAP_CHIP_SIZE = 32;

	public static final int MAP_VIEW_WIDTH = 800;
	public static final int MAP_VIEW_HEIGHT = 640;
	public static final int MAP_WIDTH_MASS = 25;
	public static final int MAP_HEIGHT_MASS = 20;

	SpriteSheet sSheet;

	public int row;
	public int col;

	public int mapWidth;
	public int mapHeight;

	// カーソル関連のデータ
	private Cursor cursor;
	Image[] cursorImage;
	int[] cursorDuration;
	Animation cursorAnime;

	// キャラクターを管理するクラス
	Characters characters;

	HashMap<String, Item> itemList;

	public Field(SimGameModel sgModel, HashMap<String, Item> itemList) {
		this.sgModel = sgModel;
		this.itemList = itemList;
		this.characters = new Characters();
		sSheet = null;
	}

	public void init(String subStoryFolderPath) {
		loadMapAndMapChip(subStoryFolderPath + "map.dat", subStoryFolderPath
				+ "mapchip.txt");
		initCursor();
		initCharacters(subStoryFolderPath);
	}

	private void initCharacters(String subStoryFolderPath) {
		characters.init(sgModel, row, col, itemList);
		characters.addCharacters(subStoryFolderPath + "putCharacter.txt");
	}

	private void initCursor() {
		setCursor(new Cursor(this));
		cursorImage = new Image[2];
		try {
			cursorImage[0] = new Image("image/cursor/simGameStateCorsor1.png");
			cursorImage[1] = new Image("image/cursor/simGameStateCorsor2.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		cursorDuration = new int[2];
		cursorDuration[0] = 600;
		cursorDuration[1] = 600;
		cursorAnime = new Animation(cursorImage, cursorDuration, true);
	}

	public int offsetX;
	public int offsetY;
	public int firstTileX;
	public int lastTileX;
	public int firstTileY;
	public int lastTileY;

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		// X方向のオフセットを計算
		offsetX = MAP_VIEW_WIDTH / 2 - getCursor().pX;
		// マップの端ではスクロールしないようにする
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, MAP_VIEW_WIDTH - mapWidth);

		// Y方向のオフセットを計算
		offsetY = MAP_VIEW_HEIGHT / 2 - getCursor().pY;
		// マップの端ではスクロールしないようにする
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, MAP_VIEW_HEIGHT - mapHeight);

		// オフセットを元に描画範囲を求める
		firstTileX = pixelsToTiles(-offsetX);
		lastTileX = firstTileX + pixelsToTiles(MAP_VIEW_WIDTH) + 2;
		// 描画範囲がマップの大きさより大きくならないように調整
		lastTileX = Math.min(lastTileX, col);

		firstTileY = pixelsToTiles(-offsetY);
		lastTileY = firstTileY + pixelsToTiles(MAP_VIEW_HEIGHT) + 1;
		// 描画範囲がマップの大きさより大きくならないように調整
		lastTileY = Math.min(lastTileY, row);

		// マップを描く
		renderMap(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY,
				lastTileY);

		// キャラクターを描く
		characters.render(g, offsetX, offsetY, firstTileX, lastTileX,
				firstTileY, lastTileY);

		// カーソルを描く
		renderCursor(g, offsetX, offsetY);
	}

	private void renderMap(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX, int firstTileY, int lastTileY) {
		for (int y = firstTileY; y < lastTileY; y++) {
			for (int x = firstTileX; x < lastTileX; x++) {
				// 一番左上のタイルを描画
				g.drawImage(sSheet.getSubImage(0, 0), tilesToPixels(x)
						+ offsetX, tilesToPixels(y) + offsetY);
				int chipX = map[y][x] % MAP_CHIP_COL;
				int chipY = map[y][x] / MAP_CHIP_COL;
				// 各マスのタイルを描画
				g.drawImage(sSheet.getSubImage(chipX, chipY), tilesToPixels(x)
						+ offsetX, tilesToPixels(y) + offsetY);
			}
		}
	}

	private void renderCursor(Graphics g, int offsetX, int offsetY) {
		cursorAnime.draw(getCursor().pX + offsetX - 4, getCursor().pY + offsetY - 4);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		cursorAnime.update(delta);
		getCursor().update();

		for (Chara chara : characters.characterArray) {
			chara.isSelect = false;
			if (chara.x == getCursor().x && chara.y == getCursor().y) {
				chara.isSelect = true;
			}
		}
	}

	/**
	 * ピクセル単位をマス単位に変更する
	 *
	 * @param pixels
	 *            ピクセル単位
	 * @return マス単位
	 */
	public static int pixelsToTiles(double pixels) {
		return (int) Math.floor(pixels / MAP_CHIP_SIZE);
	}

	/**
	 * マス単位をピクセル単位に変更する
	 *
	 * @param tiles
	 *            マス単位
	 * @return ピクセル単位
	 */
	public static int tilesToPixels(int tiles) {
		return tiles * MAP_CHIP_SIZE;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if (keyInput.isKeyDown(Input.KEY_UP)) {
			getCursor().move(Cursor.UP);
		}
		if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			getCursor().move(Cursor.RIGHT);
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			getCursor().move(Cursor.DOWN);
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			getCursor().move(Cursor.LEFT);
		}

		if (keyInput.isKeyPressed(Input.KEY_UP)) {
			getCursor().pressed(Cursor.UP);
		}
		if (keyInput.isKeyPressed(Input.KEY_RIGHT)) {
			getCursor().pressed(Cursor.RIGHT);
		}
		if (keyInput.isKeyPressed(Input.KEY_DOWN)) {
			getCursor().pressed(Cursor.DOWN);
		}
		if (keyInput.isKeyPressed(Input.KEY_LEFT)) {
			getCursor().pressed(Cursor.LEFT);
		}

		// 決定キーが押されたとき
		if (keyInput.isKeyDown(Input.KEY_Z)) {
			for (Chara chara : characters.characterArray) {
				if (chara.isSelect && !chara.isStand()) {
					pushZKey(chara);
				}
			}
		}
	}

	private void pushZKey(Chara chara) {
		CharaCommandWindow ccWindow = new CharaCommandWindow(sgModel, this,
				chara);
		sgModel.keyInputStackPush(ccWindow);
		sgModel.rendererArrayAdd(ccWindow);
	}

	/**
	 *
	 * @param mapPath
	 * @param mapchipPointerPath
	 */
	public void loadMapAndMapChip(String mapPath, String mapchipPointerPath) {
		// マップチップ読み込み
		String mapChipPath = null;
		try {
			File file = new File(mapchipPointerPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			mapChipPath = br.readLine();

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		loadMapChip(mapChipPath);

		// マップの読み込み
		try {
			FileInputStream in = new FileInputStream(mapPath);
			// 行数・列数を読み込む
			row = in.read();
			mapHeight = MAP_CHIP_SIZE * row;
			col = in.read();
			mapWidth = MAP_CHIP_SIZE * col;
			// マップを読み込む
			map = new int[row][col];
			moveCostMap = new int[row][col];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					map[i][j] = in.read();
					//TODO:moveCostの読み込み，現在は1で初期化
					moveCostMap[i][j] = 1;
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * マップチップイメージをロード
	 *
	 * @param mapChipPath
	 *            mapChipのパス
	 */
	private void loadMapChip(String mapChipPath) {
		try {
			sSheet = new SpriteSheet(new Image(mapChipPath), MAP_CHIP_SIZE,
					MAP_CHIP_SIZE);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fieldのコストが入った配列を返すメソッド
	 * @return そのマップのコストが記録されたマップ
	 */
	public int[][] createMoveCostArray(int charaX, int charaY) {
		int [][] moveCostArray = new int[moveCostMap.length][moveCostMap[0].length];

		for (int row = 0; row < moveCostArray.length; row++) {
			for (int col = 0 ; col < moveCostArray[0].length; col++) {
				moveCostArray[row][col] = moveCostMap[row][col];
			}
		}

		for (Chara chara : characters.characterArray) {
			if (charaX == chara.x && charaY == chara.y) {
				continue;
			}
			moveCostArray[chara.y][chara.x] = -1;
		}

		return moveCostArray;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
}
