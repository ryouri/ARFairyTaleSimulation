package arcircle.ftsim.simulation.field;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import arcircle.ftsim.simulation.model.Field;

public class LoadField {
	private int row;
	private int col;
	private int mapHeight;
	private int mapWidth;

	private int map[][][];
	private Image renderArray[][];

	/**
	 * 移動コストが保存される
	 * -1は移動不可能
	 */
	private int moveCostMap[][];

	private Terrain terrainMap[][][];

	TerrainInfoSupplier terrainInfoSupplier;

	SpriteSheet sSheet;

	public static final int MAP_CHIP_LAYER_NUM = 3;

	public static final int MAP_CHIP_ROW = 32;
	public static final int MAP_CHIP_COL = 20;
	public static final int MAP_CHIP_SIZE = 32;

	public static final int MAP_VIEW_WIDTH = 800;
	public static final int MAP_VIEW_HEIGHT = 640;
	public static final int MAP_WIDTH_MASS = 25;
	public static final int MAP_HEIGHT_MASS = 20;


	public static final String AutoTileFloderPath = "image/autotile/";
	public static final int AUTO_TILE_CHIP_DIVIDE_SIZE = 16;

	private HashMap<Integer, SpriteSheet> autoTileSSheetMap;

	public LoadField(String mapPath, String mapchipPointerPath) {
		init(mapPath, mapchipPointerPath);
	}

	public void init(String mapPath, String mapchipPointerPath) {
		//地形情報の読み込み
		terrainInfoSupplier = new TerrainInfoSupplier();

		loadMapAndMapChip(mapPath, mapchipPointerPath);
		generateRenderArray();
	}

	private void generateRenderArray() {


	}

	public void renderMap(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX, int firstTileY, int lastTileY) {
		for (int y = firstTileY; y < lastTileY; y++) {
			for (int x = firstTileX; x < lastTileX; x++) {
				g.drawImage(renderArray[y][x],
						Field.tilesToPixels(x) + offsetX,
						Field.tilesToPixels(y) + offsetY);
//				// 一番左上のタイルを描画
//				g.drawImage(sSheet.getSubImage(0, 0), Field.tilesToPixels(x)
//						+ offsetX, Field.tilesToPixels(y) + offsetY);
//				int chipX = map[y][x] % MAP_CHIP_COL;
//				int chipY = map[y][x] / MAP_CHIP_COL;
//				// 各マスのタイルを描画
//				g.drawImage(sSheet.getSubImage(chipX, chipY), Field.tilesToPixels(x)
//						+ offsetX, Field.tilesToPixels(y) + offsetY);
			}
		}
	}

	/**
	 *
	 * @param mapPath
	 * @param mapchipPointerPath
	 */
	private void loadMapAndMapChip(String mapPath, String mapchipPointerPath) {

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
			map = new int[MAP_CHIP_LAYER_NUM][row][col];
			// 地形マップの初期化
			terrainMap = new Terrain[MAP_CHIP_LAYER_NUM][row][col];
			//最終描画マップの初期化
			renderArray = new Image[row][col];

			moveCostMap = new int[row][col];
			for (int z = 0; z < MAP_CHIP_LAYER_NUM; z++) {
				for (int y = 0; y < row; y++) {
					for (int x = 0; x < col; x++) {
						// こっちが新しいMapEditor用
						byte[] b = new byte[4];
						in.read(b, 0, 4);
						int mapChipNo = fromBytes(b);
						map[z][y][x] = mapChipNo;

						terrainMap[z][y][x] = terrainInfoSupplier
								.getTerrain(mapChipNo);
					}
				}
			}
			in.close();

			loadAutoTile();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void loadAutoTile() throws SlickException {
		String path = "image/autotile/";
		autoTileSSheetMap = new HashMap<Integer, SpriteSheet>();

		for (String fileName : new File(path).list()) {
			String fileNumStr = fileName.split("\\.")[0];
			int fileNum = Integer.parseInt(fileNumStr);
			SpriteSheet sSheet = new SpriteSheet(
					new Image(path + fileName),
					AUTO_TILE_CHIP_DIVIDE_SIZE,
					AUTO_TILE_CHIP_DIVIDE_SIZE);
			autoTileSSheetMap.put(fileNum, sSheet);
		}
	}

	public static int fromBytes(byte[] b) {
		return ByteBuffer.wrap(b).getInt();
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

	public Image getSelectedMapChip(int y, int x) {
		return renderArray[y][x];
	}


	public Terrain getYXTerrain(int y, int x) {
		return terrainInfoSupplier.getTerrain(map[0][y][x]);
	}


	/**
	 * Fieldのコストが入った配列を返すメソッド
	 * @return そのマップのコストが記録されたマップ
	 */
	public int[][] createMoveCostArray(int charaX, int charaY) {
		int [][] moveCostArray = new int[moveCostMap.length][moveCostMap[0].length];

		//Mapのコストを格納する
		//TODO: 地形のコストを格納するように変更が必要
		for (int row = 0; row < moveCostArray.length; row++) {
			for (int col = 0 ; col < moveCostArray[0].length; col++) {
				moveCostArray[row][col] = moveCostMap[row][col];

				if (getYXTerrain(row, col).terrainName.equals("河") ) {
					moveCostArray[row][col] += 10;
				}
			}
		}
		return moveCostArray;
	}

	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public int getMapHeight() {
		return mapHeight;
	}
	public int getMapWidth() {
		return mapWidth;
	}
	public int[][] getMoveCostMap() {
		return moveCostMap;
	}
	public TerrainInfoSupplier getTerrainManager() {
		return terrainInfoSupplier;
	}
	public SpriteSheet getsSheet() {
		return sSheet;
	}
}
