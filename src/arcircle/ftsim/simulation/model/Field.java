package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
import arcircle.ftsim.state.simgame.SimGameModel;

public class Field implements KeyListner, Renderer {

	SimGameModel sgModel;

	private int map[][];

	public static final int MAP_CHIP_ROW = 20;
	public static final int MAP_CHIP_COL = 10;
	public static final int MAP_CHIP_SIZE = 32;

	public static final int MAP_VIEW_WIDTH  = 800;
	public static final int MAP_VIEW_HEIGHT = 640;
	public static final int MAP_WIDTH_MASS  = 25;
	public static final int MAP_HEIGHT_MASS = 20;

	SpriteSheet sSheet;

	public int row;
	public int col;

	public int mapWidth;
	public int mapHeight;

	//カーソル関連のデータ
	Cursor cursor;
	Image[] cursorImage;
	int[] cursorDuration;
	Animation cursorAnime;

	//キャラクターを管理するクラス
	Characters characters;


	public Field(SimGameModel sgModel) {
		this.sgModel = sgModel;
		sSheet = null;
	}

	public void init(String mapPath, String mapchipPointerPath) {
		loadMapAndMapChip(mapPath, mapchipPointerPath);
		initCursor();
		initCharacters();
		sgModel.rendererArrayAdd(characters);
	}

	private void initCharacters() {
		this.characters = new Characters(sgModel, row, col);
		characters.init();
	}

	private void initCursor() {
		cursor = new Cursor(this);
		cursorImage = new Image[2];
		try {
			cursorImage[0] = new Image("image/cursor/simGameStateCursor1.png");
			cursorImage[1] = new Image("image/cursor/simGameStateCursor2.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		cursorDuration = new int[2];
		cursorDuration[0] = 400;
		cursorDuration[1] = 400;
		cursorAnime = new Animation(cursorImage, cursorDuration, true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		// X方向のオフセットを計算
		int offsetX = MAP_VIEW_WIDTH / 2 - cursor.pX;
		// マップの端ではスクロールしないようにする
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, MAP_VIEW_WIDTH - mapWidth);

		// Y方向のオフセットを計算
		int offsetY = MAP_VIEW_HEIGHT / 2 - cursor.pY;
		// マップの端ではスクロールしないようにする
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, MAP_VIEW_HEIGHT - mapHeight);

		// マップを描く
		renderMap(g, offsetX, offsetY);

		// 勇者を描く
		renderCursor(g, offsetX, offsetY);
	}

	private void renderMap(Graphics g, int offsetX, int offsetY) {
        // オフセットを元に描画範囲を求める
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(MAP_VIEW_WIDTH) + 2;
        // 描画範囲がマップの大きさより大きくならないように調整
        lastTileX = Math.min(lastTileX, col);

        int firstTileY = pixelsToTiles(-offsetY);
        int lastTileY = firstTileY + pixelsToTiles(MAP_VIEW_HEIGHT) + 1;
        // 描画範囲がマップの大きさより大きくならないように調整
        lastTileY = Math.min(lastTileY, row);

        for (int y = firstTileY; y < lastTileY; y++) {
            for (int x = firstTileX; x < lastTileX; x++) {
            	//一番左上のタイルを描画
				g.drawImage(sSheet.getSubImage(0, 0),
					tilesToPixels(x) + offsetX,
					tilesToPixels(y) + offsetY);
				int chipY = map[y][x] % MAP_CHIP_COL;
				int chipX = map[y][x] / MAP_CHIP_COL;
				//各マスのタイルを描画
				g.drawImage(sSheet.getSubImage(chipY, chipX),
					tilesToPixels(x) + offsetX,
					tilesToPixels(y) + offsetY);
            }
        }
	}
	private void renderCursor(Graphics g, int offsetX, int offsetY) {
		cursorAnime.draw(cursor.pX + offsetX - 4,
				cursor.pY + offsetY - 4);
	}
	public void update(GameContainer container, StateBasedGame game, int delta) {
		cursorAnime.update(delta);
		cursor.update();
	}

    /**
     * ピクセル単位をマス単位に変更する
     * @param pixels ピクセル単位
     * @return マス単位
     */
    public static int pixelsToTiles(double pixels) {
        return (int)Math.floor(pixels / MAP_CHIP_SIZE);
    }
    /**
     * マス単位をピクセル単位に変更する
     * @param tiles マス単位
     * @return ピクセル単位
     */
    public static int tilesToPixels(int tiles) {
        return tiles * MAP_CHIP_SIZE;
    }

	@Override
	public void keyInput(KeyInput keyInput) {
		if (keyInput.isKeyDown(Input.KEY_UP)) {
			cursor.move(Cursor.UP);
		}
		if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			cursor.move(Cursor.RIGHT);
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			cursor.move(Cursor.DOWN);
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			cursor.move(Cursor.LEFT);
		}

		if (keyInput.isKeyPressed(Input.KEY_UP)) {
			cursor.pressed(Cursor.UP);
		}
		if (keyInput.isKeyPressed(Input.KEY_RIGHT)) {
			cursor.pressed(Cursor.RIGHT);
		}
		if (keyInput.isKeyPressed(Input.KEY_DOWN)) {
			cursor.pressed(Cursor.DOWN);
		}
		if (keyInput.isKeyPressed(Input.KEY_LEFT)) {
			cursor.pressed(Cursor.LEFT);
		}
	}

	/**
	 *
	 * @param mapPath
	 * @param mapchipPointerPath
	 */
	public void loadMapAndMapChip(String mapPath, String mapchipPointerPath) {
		//マップチップ読み込み
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

		//マップの読み込み
		try {
			FileInputStream in = new FileInputStream(mapPath);
			// 行数・列数を読み込む
			row = in.read();
			mapHeight = MAP_CHIP_SIZE * row;
			col = in.read();
			mapWidth = MAP_CHIP_SIZE * col;
			// マップを読み込む
			map = new int[row][col];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					map[i][j] = in.read();
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * マップチップイメージをロード
	 * @param mapChipPath mapChipのパス
	 */
	private void loadMapChip(String mapChipPath) {
		try {
			sSheet = new SpriteSheet(new Image(mapChipPath), MAP_CHIP_SIZE,
					MAP_CHIP_SIZE);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
