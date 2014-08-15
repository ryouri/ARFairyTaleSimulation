package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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

	SpriteSheet sSheet;

	private int row;
	private int col;

	public Field(SimGameModel sgModel) {
		this.sgModel = sgModel;
		sSheet = null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		// map[][]に保存されているマップチップ番号をもとに画像を描画する
		for (int y = 0; y < row; y++) {
			for (int x = 0; x < col; x++) {
				int chipY = map[y][x] % MAP_CHIP_COL;
				int chipX = map[y][x] / MAP_CHIP_COL;
				g.drawImage(sSheet.getSubImage(chipY, chipX),
					MAP_CHIP_SIZE * x,
					MAP_CHIP_SIZE * y);
			}
		}
	}

	@Override
	public void keyInput(KeyInput keyInput) {
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
			col = in.read();
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
