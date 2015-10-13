package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
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
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.field.Terrain;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.model.effect.EffectManager;
import arcircle.ftsim.simulation.model.task.TaskManager;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Field implements KeyListner, Renderer {

	SimGameModel sgModel;
	public SimGameModel getSgModel() {
		return sgModel;
	}

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

	private String partName;

	public EventManager eventManager;

	private TaskManager taskManager;

	private SoundManager soundManager;

	private EffectManager effectManager;

	private SubInfoWindow subInfoWindow;

	private LoadField loadField;

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
		this.setNowTurn(TURN_FRIEND);
	}

	//TODO:マジックナンバー多発地帯！
	public void init(String subStoryFolderPath) {
		loadField = new LoadField(subStoryFolderPath + "map.dat", subStoryFolderPath
				+ "mapchip.txt");

		initCursor();
		initCharacters(subStoryFolderPath);
		loadMapName(subStoryFolderPath + "partName.txt");
		loadEvent(subStoryFolderPath + "event.txt");
		loadEndCondition(subStoryFolderPath + "endCondition.txt");

		this.soundManager = new SoundManager(SoundManager.battleSoundFolderPath);

		this.taskManager = new TaskManager(this, characters);

		this.effectManager = new EffectManager();
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
		characters.init(sgModel, this, loadField.getRow(), loadField.getCol(), itemList);
		characters.addCharacters(subStoryFolderPath + "putCharacter.txt");
	}

	private void initCursor() {
		setCursor(new Cursor(this, loadField));
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
		offsetX = LoadField.MAP_VIEW_WIDTH / 2 - getCursor().pX;
		// マップの端ではスクロールしないようにする
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, LoadField.MAP_VIEW_WIDTH - loadField.getMapWidth());

		// Y方向のオフセットを計算
		offsetY = LoadField.MAP_VIEW_HEIGHT / 2 - getCursor().pY;
		// マップの端ではスクロールしないようにする
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, LoadField.MAP_VIEW_HEIGHT - loadField.getMapHeight());

		// オフセットを元に描画範囲を求める
		firstTileX = pixelsToTiles(-offsetX);
		lastTileX = firstTileX + pixelsToTiles(LoadField.MAP_VIEW_WIDTH) + 2;
		// 描画範囲がマップの大きさより大きくならないように調整
		lastTileX = Math.min(lastTileX, loadField.getCol());

		firstTileY = pixelsToTiles(-offsetY);
		lastTileY = firstTileY + pixelsToTiles(LoadField.MAP_VIEW_HEIGHT) + 1;
		// 描画範囲がマップの大きさより大きくならないように調整
		lastTileY = Math.min(lastTileY, loadField.getRow());

		// マップを描く
		loadField.renderMap(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY,
				lastTileY);

		// キャラクターを描く
		characters.render(g, offsetX, offsetY, firstTileX, lastTileX,
				firstTileY, lastTileY);

		if (cursor.isVisible) {
			// カーソルを描く
			renderCursor(g, offsetX, offsetY);
		}

		if (effectManager.existEffect()) {
			effectManager.render(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}

		if (taskManager.existTask()) {
			taskManager.processRender(
					g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}
	}

	private void renderCursor(Graphics g, int offsetX, int offsetY) {
		cursorAnime.draw(getCursor().pX + offsetX - 4, getCursor().pY + offsetY - 4);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		cursorAnime.update(delta);
		getCursor().update();

		if (effectManager.existEffect()) {
			effectManager.update();
		}

		if (taskManager.existTask()) {
			taskManager.processUpdate(delta);
			return;
		}

		characters.update(delta);

		if (characters.isGameEnd()) {
			FTSimulationGame.save.getNowStage().selectLogue = NowStage.EPILOGUE;
			sgModel.nextState();
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
		return (int) Math.floor(pixels / LoadField.MAP_CHIP_SIZE);
	}

	/**
	 * マス単位をピクセル単位に変更する
	 *
	 * @param tiles
	 *            マス単位
	 * @return ピクセル単位
	 */
	public static int tilesToPixels(int tiles) {
		return tiles * LoadField.MAP_CHIP_SIZE;
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
		return loadField.getSelectedMapChip(y, x);
	}

	/**
	 * 現在，カーソルが上に載っている地形を表すTerrainインスタンスを返す
	 * @return 現在選択しているTerrainインスタンス
	 */
	public Terrain getSelectedTerrain() {
		int y = cursor.y;
		int x = cursor.x;
		return loadField.getYXTerrain(y, x);
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
		cursor.stop();
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

	public Terrain getYXTerrain(int y, int x) {
		return loadField.getYXTerrain(y, x);
	}

	/**
	 * Fieldのコストが入った配列を返すメソッド
	 * @return そのマップのコストが記録されたマップ
	 */
	public int[][] createMoveCostArray(int charaX, int charaY) {
		int [][] moveCostArray = loadField.createMoveCostArray(charaX, charaY);

		Chara moveChara = null;
		//移動するキャラを取得する
		for (Chara chara : characters.characterArray) {
			if (charaX == chara.x && charaY == chara.y) {
				moveChara = chara;
				break;
			}
		}

		if (moveChara == null) {
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

	public void addAttackTask(Chara chara, java.awt.Point attackPoint, java.awt.Point damagePoint) {
		for (Chara damageChara : characters.characterArray) {
			if (damageChara.y == damagePoint.y && damageChara.x == damagePoint.x) {
				taskManager.addAttackTask(chara, damageChara, attackPoint);
				break;
			}
		}
	}

	public void addHealTask(Chara healChara, int healedY, int healedX) {
		for (Chara healedChara : characters.characterArray) {
			if (healedChara.y == healedY && healedChara.x == healedX) {
				taskManager.addHealTask(healChara, healedChara);
				break;
			}
		}
	}

	public void addMoveTask(Chara chara, Node moveNode) {
		taskManager.addMoveTask(chara, moveNode);
	}

	public void addStandCharaTask(Chara chara, boolean stand) {
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

	public void occurEffect(int px, int py, String effectName) {
		effectManager.addEffect(px, py, effectName);
	}

	public void saveData() {
		for (Chara chara : characters.characterArray) {
			if (chara.getCamp() != Chara.CAMP_ALLIES) {
				continue;
			}
			FTSimulationGame.save.putCharaStatus(chara.getFolderName(), chara.status);
		}
	}

	public int getFieldRow() {
		return loadField.getRow();
	}

	public int getFieldCol() {
		return loadField.getCol();
	}
}
