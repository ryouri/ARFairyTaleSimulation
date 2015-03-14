package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.save.NowStage;
import arcircle.ftsim.simulation.algorithm.range.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.command.CharaCommandWindow;
import arcircle.ftsim.simulation.command.OptionCommandWindow;
import arcircle.ftsim.simulation.event.Event;
import arcircle.ftsim.simulation.event.EventManager;
import arcircle.ftsim.simulation.field.Terrain;
import arcircle.ftsim.simulation.field.TerrainManager;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.model.task.TaskManager;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Field implements KeyListner, Renderer {

	SimGameModel sgModel;
	public SimGameModel getSgModel() {
		return sgModel;
	}

	private int map[][];
	private Terrain terrainMap[][];

	/**
	 * 移動コストが保存される
	 * -1は移動不可能
	 */
	private int moveCostMap[][];

	public static final int MAP_CHIP_ROW = 32;
	public static final int MAP_CHIP_COL = 20;
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

	private int nowTurn;
	public static final int TURN_FRIEND = 0;
	public static final int TURN_ENEMY = 1;

	public TerrainManager terrainManager;

	private String partName;

	public EventManager eventManager;

	private TaskManager taskManager;

	private SoundManager soundManager;

	private SubInfoWindow subInfoWindow;
	public SubInfoWindow getSubInfoWindow() {
		return subInfoWindow;
	}
	public void setSubInfoWindow(SubInfoWindow subInfoWindow) {
		this.subInfoWindow = subInfoWindow;
	}

	/**
	 * あるキャラの周囲のキャラを探索し格納して返す．
	 * @param chara 探索の中心のキャラ，これと同じ所属のキャラを返す
	 * @param aroundMassNum マスいくつ分離れているキャラを探索するか
	 * @return 探索できたキャラが入る
	 */
	public ArrayList<Chara> getAroundChara(Chara chara, int aroundMassNum) {
		ArrayList<Chara> aroundCharaArray = new ArrayList<Chara>();

		for (Chara targetChara : characters.characterArray) {
			if (chara.equals(targetChara)) {
				continue;
			}
			int xDiff = Math.abs(chara.x - targetChara.x);
			int yDiff = Math.abs(chara.y - targetChara.y);

			if (xDiff + yDiff <= aroundMassNum) {
				aroundCharaArray.add(targetChara);
			}
		}

		return aroundCharaArray;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public Field(SimGameModel sgModel, HashMap<String, Item> itemList) {
		this.sgModel = sgModel;
		this.itemList = itemList;
		this.characters = new Characters();
		this.sSheet = null;;
		this.setNowTurn(TURN_FRIEND);
	}

	//TODO:マジックナンバー多発地帯！
	public void init(String subStoryFolderPath) {
		loadMapAndMapChip(subStoryFolderPath + "map.dat", subStoryFolderPath
				+ "mapchip.txt");
		initCursor();
		initCharacters(subStoryFolderPath);
		loadMapName(subStoryFolderPath + "partName.txt");
		loadEvent(subStoryFolderPath + "event.txt");
		loadEndCondition(subStoryFolderPath + "endCondition.txt");

		this.soundManager = new SoundManager(SoundManager.battleSoundFolderPath);

		this.taskManager = new TaskManager(this, characters);
	}

	private void loadEndCondition(String endConditionTxt) {
		eventManager.loadEndConditionTxt(endConditionTxt);
	}

	private void loadEvent(String eventTxt) {
		eventManager = new EventManager(this);
		eventManager.loadEventTxt(eventTxt);
	}

	private void loadMapName(String partNameTxtString) {
		try {
			File file = new File(partNameTxtString);
			BufferedReader br = new BufferedReader(new FileReader(file));

			partName = br.readLine();

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void initCharacters(String subStoryFolderPath) {
		characters.init(sgModel, this, row, col, itemList);
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

	public void changeTurnFriend() {
		setNowTurn(TURN_FRIEND);
		cursor.isVisible = true;
	}

	public void changeTurnEnemy() {
		setNowTurn(TURN_ENEMY);
		cursor.isVisible = false;
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

		if (cursor.isVisible) {
			// カーソルを描く
			renderCursor(g, offsetX, offsetY);
		}

		if (taskManager.existTask()) {
			taskManager.processRender(
					g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}
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

		//存在するタスクを処理する
		if (taskManager.existTask()) {
			taskManager.processUpdate(delta);
			return;
		}

		characters.update(delta);

		if (characters.isGameEnd()) {
			FTSimulationGame.save.getNowStage().selectLogue = NowStage.EPILOGUE;
			sgModel.nextState();
		}

		//TODO:ターンの変化処理はCharactersからこっちに移したい
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
		if (!cursor.isVisible) {
			return;
		}

		if (keyInput.isKeyDown(Input.KEY_UP)) {
			getCursor().startMove(Cursor.UP);
		}
		if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			getCursor().startMove(Cursor.RIGHT);
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			getCursor().startMove(Cursor.DOWN);
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			getCursor().startMove(Cursor.LEFT);
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
				if (chara.isSelect && !chara.isStand() && chara.getCamp() == Chara.CAMP_FRIEND) {
					pushZKey(chara);
				}
			}
		}

		if (keyInput.isKeyDown(Input.KEY_C)) {
			pushCkey(null);
		}
	}

	/**
	 * 現在，isSelectなキャラ（カーソルが乗っかっているキャラ）を返す
	 * 該当するキャラクターがいない場合，nullを返す
	 * @return 選択されているCharaのインスタンス，選択されていなければnullを返す
	 */
	public Chara getSelectedChara() {
		for (Chara chara : characters.characterArray) {
			if (chara.isSelect) {
				return chara;
			}
		}
		return null;
	}

	/**
	 * 現在，カーソルが上に載っているマップチップのImageを返す
	 * @return 現在選択しているマップチップのImage
	 */
	public Image getSelectedMapChip() {
		int y = cursor.y;
		int x = cursor.x;
		int chipX = map[y][x] % MAP_CHIP_COL;
		int chipY = map[y][x] / MAP_CHIP_COL;
		// 各マスのタイルを描画
		return sSheet.getSubImage(chipX, chipY);
	}

	/**
	 * 現在，カーソルが上に載っている地形を表すTerrainインスタンスを返す
	 * @return 現在選択しているTerrainインスタンス
	 */
	public Terrain getSelectedTerrain() {
		int y = cursor.y;
		int x = cursor.x;
		// 各マスのタイルを描画
		return terrainMap[y][x];
	}

	/**
	 * 戦闘中のお話の名前，partNameを返す
	 * @return 戦闘中のお話の名前
	 */
	public String getPartName() {
		return partName;
	}

	private void pushZKey(Chara chara) {
		soundManager.playSound(SoundManager.SOUND_DECISION);
		CharaCommandWindow ccWindow = new CharaCommandWindow(sgModel, this,
				chara);
		sgModel.pushKeyInputStack(ccWindow);
		sgModel.addRendererArray(ccWindow);
	}

	private void pushCkey(Chara chara) {
		soundManager.playSound(SoundManager.SOUND_DECISION);
		OptionCommandWindow oCWindow = new OptionCommandWindow(sgModel, this,
				chara);
		sgModel.pushKeyInputStack(oCWindow);
		sgModel.addRendererArray(oCWindow);
	}

	/**
	 *
	 * @param mapPath
	 * @param mapchipPointerPath
	 */
	public void loadMapAndMapChip(String mapPath, String mapchipPointerPath) {
		//地形情報の読み込み
		terrainManager = new TerrainManager();

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
			// 地形マップの初期化
			terrainMap = new Terrain[row][col];

			moveCostMap = new int[row][col];
			for (int y = 0; y < row; y++) {
				for (int x = 0; x < col; x++) {
					int mapChipNo = in.read();
					map[y][x] = mapChipNo;
					//TODO:moveCostの読み込み，現在は1で初期化
					moveCostMap[y][x] = 1;

					int chipX = mapChipNo % MAP_CHIP_COL;
					int chipY = mapChipNo / MAP_CHIP_COL;

					terrainMap[y][x] = terrainManager.getTerrain(chipX, chipY);
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

		//Mapのコストを格納する
		//TODO: 地形のコストを格納するように変更が必要
		for (int row = 0; row < moveCostArray.length; row++) {
			for (int col = 0 ; col < moveCostArray[0].length; col++) {
				moveCostArray[row][col] = moveCostMap[row][col];
			}
		}

		Chara moveChara = null;
		//移動するキャラを取得する
		for (Chara chara : characters.characterArray) {
			if (charaX == chara.x && charaY == chara.y) {
				moveChara = chara;
				break;
			}
		}

		if (moveChara == null) {
			//エラー！！！
			System.exit(1);
		}

		//今は同じ所属(Camp)のキャラなら通過できるようにする
		//TODO: Friendは友軍のキャラを通過できるようにしたいなら，処理を変えよう
		for (Chara chara : characters.characterArray) {
			if (charaX == chara.x && charaY == chara.y) {
				continue;
			}
			//同じ所属のキャラなら通過可能とする
			if (moveChara.getCamp() == chara.getCamp()) {
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

	public Characters getCharacters() {
		return characters;
	}

	public int getNowTurn() {
		return nowTurn;
	}

	public int setNowTurn(int nowTurn) {
		this.nowTurn = nowTurn;
		return nowTurn;
	}

	public Chara getXYChara(int x, int y) {
		return characters.getXYChara(x, y);
	}

	/**
	 * 勝利条件を渡すメソッド
	 * @return 勝利条件のStringが入った配列を返す， 勝利条件がない場合は，nullを返す
	 */
	public ArrayList<String> getWinConditionString() {
		ArrayList<String> winStringArray = new ArrayList<String>();

		if (eventManager.getWinConditionEachPhaseArray().isEmpty()) {
			return null;
		}

		for (Event event : eventManager.getWinConditionEachPhaseArray().get(0)) {
			winStringArray.add(event.eventID);
		}
		return winStringArray;
	}

	/**
	 * 敗北条件を渡すメソッド
	 * @return 敗北条件のStringが入った配列を返す， 敗北条件がない場合は，nullを返す
	 */
	public ArrayList<String> getLoseConditionString() {
		ArrayList<String> loseStringArray = new ArrayList<String>();

		if (eventManager.getLoseConditionEachPhaseArray().isEmpty()) {
			return null;
		}

		for (Event event : eventManager.getLoseConditionEachPhaseArray().get(0)) {
			loseStringArray.add(event.eventID);
		}
		return loseStringArray;
	}

	public void setCharaAttack(Chara chara, int y, int x) {
		for (Chara damageChara : characters.characterArray) {
			if (damageChara.y == y && damageChara.x == x) {
				taskManager.addAttackTask(chara, damageChara);
				break;
			}
		}
	}

	public void setCharaMove(Chara chara, Node moveNode) {
		taskManager.addMoveTask(chara, moveNode);
	}

	public void setCharaStand(Chara chara, boolean stand) {
		taskManager.addStandCharaTask(chara, stand);
	}

	public void setSubInfoWindowForFieldInfo() {
		subInfoWindow.setSubInfoWindowForFieldInfo();
	}

	public void setSubInfoWindowForAttackInfo(ExpectBattleInfo expectBattleInfo) {
		subInfoWindow.setSubInfoWindowForAttackInfo(expectBattleInfo);
	}
	public SoundManager getSoundManager() {
		return soundManager;
	}
}
