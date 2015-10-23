package arcircle.ftsim.simulation.field;

import java.awt.Point;
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

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;

public class LoadField {
	private int row;
	private int col;
	private int mapHeight;
	private int mapWidth;

	private int mapChipNoArray[][][];
	private DrawPoint drawPointArray[][][];

	private Image renderArray[][];

	/**
	 * 移動コストが保存される
	 * -1は移動不可能
	 */
	private int moveCostMap[][];

	private Terrain tempTerrainMap[][][];

	private Terrain terrainArray[][];

	TerrainInfoSupplier terrainInfoSupplier;

	SpriteSheet sSheet;

	public static final int MAP_CHIP_LAYER_NUM = 3;

	public static final int MAP_CHIP_ROW = 32;
	public static final int MAP_CHIP_COL = 20;
	public static final int MAP_CHIP_SIZE = 32;
	public static final int MAP_CHIP_HALF_SIZE = MAP_CHIP_SIZE / 2;

	public static final int MAP_VIEW_WIDTH = 800;
	public static final int MAP_VIEW_HEIGHT = 640;
	public static final int MAP_WIDTH_MASS = 25;
	public static final int MAP_HEIGHT_MASS = 20;

	public static final String AutoTileFloderPath = "image/autotile/";
	public static final int AUTO_TILE_CHIP_DIVIDE_SIZE = 16;

	private boolean edge_close = false;

	private HashMap<Integer, SpriteSheet> autoTileSSheetMap;

	public LoadField(String mapPath, String mapchipPointerPath) {
		init(mapPath, mapchipPointerPath);
	}

	public void init(String mapPath, String mapchipPointerPath) {
		//地形情報の読み込み
		terrainInfoSupplier = new TerrainInfoSupplier();

		loadMapAndMapChip(mapPath, mapchipPointerPath);

		generateTerrainArray();

		generateDrawPointArray();
	}

	private void generateTerrainArray() {
		for (int z = 0; z < MAP_CHIP_LAYER_NUM; z++) {
			for (int y = 0; y < row; y++) {
				for (int x = 0; x < col; x++) {
					if (tempTerrainMap[z][y][x] == null) {
						continue;
					}
					terrainArray[y][x] = tempTerrainMap[z][y][x];
				}
			}
		}
	}

	private void generateDrawPointArray() {
		drawPointArray = new DrawPoint[MAP_CHIP_LAYER_NUM][row][col];
		for (int z = 0; z < MAP_CHIP_LAYER_NUM; z++) {
			for (int y = 0; y < row; y++) {
				for (int x = 0; x < col; x++) {
					if (mapChipNoArray[z][y][x] > TerrainInfoSupplier.NONE_CHIP_NUM){
						boolean[][] aroundInfo = generateAroundInfoArray(y, x, mapChipNoArray[z]);
						Point[][] drawPoint = AutoTileUtil.aroudTileArrayToDrawTileArray(aroundInfo);
						drawPointArray[z][y][x] = new DrawPoint(drawPoint);
					}
				}
			}
		}
	}

	private boolean[][] generateAroundInfoArray(int y, int x, int[][] map) {
		int chipID = map[y][x];
		boolean left_up 	= y == 0 		? edge_close : x == 0 		? edge_close : chipID != map[y-1][x-1];
		boolean left  		= x == 0 		? edge_close : chipID != map[y][x-1];
		boolean left_down 	= y == row - 1 	? edge_close : x == 0 		? edge_close : chipID != map[y+1][x-1];
		boolean right_up 	= y == 0 		? edge_close : x == col - 1 ? edge_close : chipID != map[y-1][x+1];
		boolean right 		= x == col - 1 	? edge_close : chipID != map[y][x+1];
		boolean right_down 	= y == row - 1 	? edge_close : x == col - 1 ? edge_close : chipID != map[y+1][x+1];
		boolean up 			= y == 0 		? edge_close : chipID != map[y-1][x];
		boolean down 		= y == row - 1 	? edge_close : chipID != map[y+1][x];
		boolean[][] around_info = {
			{left_up, up, right_up},
			{left, false, right},
			{left_down, down, right_down}
		};
		return around_info;
	}

	private void renderMapChip(Graphics g, int z, int y, int x, int drawX,
			int drawY) {
		if (mapChipNoArray[z][y][x] < TerrainInfoSupplier.NONE_CHIP_NUM) {
			//通常のマップチップなら
			int chipX = mapChipNoArray[z][y][x] % MAP_CHIP_COL;
			int chipY = mapChipNoArray[z][y][x] / MAP_CHIP_COL;
			g.drawImage(sSheet.getSubImage(chipX, chipY), drawX, drawY);
		} else if (mapChipNoArray[z][y][x] > TerrainInfoSupplier.NONE_CHIP_NUM && drawPointArray[z][y][x] != null){
			//AutoTileなら
			SpriteSheet autoTileSSheet = autoTileSSheetMap.get(mapChipNoArray[z][y][x]);
			Point[][] drawPoint = drawPointArray[z][y][x].drawPoint;
			for (int pointY = 0; pointY < drawPoint.length; pointY++) {
				for (int pointX = 0; pointX < drawPoint[pointY].length; pointX++) {
					int iY = drawPoint[pointY][pointX].y;
					int iX = drawPoint[pointY][pointX].x;
					Image drawedImage = autoTileSSheet.getSubImage(iX, iY);
					g.drawImage(
							drawedImage,
							drawX + pointX * MAP_CHIP_HALF_SIZE,
							drawY + pointY * MAP_CHIP_HALF_SIZE);
				}
			}
		}
	}

	public void renderMap(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX, int firstTileY, int lastTileY) {
		for (int z = 0; z < MAP_CHIP_LAYER_NUM; z++) {
			for (int y = firstTileY; y < lastTileY; y++) {
				for (int x = firstTileX; x < lastTileX; x++) {
					int drawX = Field.tilesToPixels(x) + offsetX;
					int drawY = Field.tilesToPixels(y) + offsetY;
					renderMapChip(g, z, y, x, drawX, drawY);
				}
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

			initDependentRowColArray();

			for (int z = 0; z < MAP_CHIP_LAYER_NUM; z++) {
				for (int y = 0; y < row; y++) {
					for (int x = 0; x < col; x++) {
						// こっちが新しいMapEditor用
						byte[] b = new byte[4];
						in.read(b, 0, 4);
						int mapChipNo = fromBytes(b);
						mapChipNoArray[z][y][x] = mapChipNo;

						tempTerrainMap[z][y][x] = terrainInfoSupplier
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

	private void initDependentRowColArray () {
		// マップを読み込む
		mapChipNoArray = new int[MAP_CHIP_LAYER_NUM][row][col];
		// 地形マップの初期化
		tempTerrainMap = new Terrain[MAP_CHIP_LAYER_NUM][row][col];
		//地形配列の初期化
		terrainArray = new Terrain[row][col];

		initMoveCostArray();
	}

	private void initMoveCostArray() {
		moveCostMap = new int[row][col];
		for (int y = 0; y < row; y++) {
			for (int x = 0; x < col; x++) {
				moveCostMap[y][x] = 1;
			}
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

	/**
	 * 通常のマップチップのみ渡せる
	 * @param y
	 * @param x
	 * @return
	 */
	public Image getSelectedMapChip(int y, int x) {
		for (int z = mapChipNoArray.length - 1; z >= 0; z--) {
			if (mapChipNoArray[z][y][x] == TerrainInfoSupplier.NONE_CHIP_NUM) {
				continue;
			} else if (mapChipNoArray[z][y][x] > TerrainInfoSupplier.NONE_CHIP_NUM) {
				break;
			}
			//通常のマップチップなら
			int chipX = mapChipNoArray[z][y][x] % MAP_CHIP_COL;
			int chipY = mapChipNoArray[z][y][x] / MAP_CHIP_COL;
			return sSheet.getSubImage(chipX, chipY);
		}
		return null;
	}


	public Terrain getYXTerrain(int y, int x) {
		return terrainArray[y][x];
	}


	/**
	 * Fieldのコストが入った配列を返すメソッド
	 * @param chara
	 * @return そのマップのコストが記録されたマップ
	 */
	public int[][] createMoveCostArray(int charaX, int charaY, Chara chara) {
		int [][] moveCostArray = new int[moveCostMap.length][moveCostMap[0].length];

		//Mapのコストを格納する
		//TODO: 地形のコストを格納するように変更が必要
		for (int y = 0; y < moveCostArray.length; y++) {
			for (int x = 0 ; x < moveCostArray[0].length; x++) {
				moveCostArray[y][x] = moveCostMap[y][x];

				Terrain terrain = getYXTerrain(y, x);

				//TODO; キャラのクラスが実装されていないので、テストクラスでコストを計算している
				if (terrain != null) {
					moveCostArray[y][x] = terrain.classNameCostMap.get(chara.status.className);
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

	class DrawPoint {
		public Point[][] drawPoint;
		public DrawPoint(Point[][] drawPoint) {
			this.drawPoint = drawPoint;
		}
	}
}
