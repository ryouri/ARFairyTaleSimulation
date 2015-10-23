package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.algorithm.range.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.command.CharaCommandWindow;
import arcircle.ftsim.simulation.command.OptionCommandWindow;
import arcircle.ftsim.simulation.event.EventManager;
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.field.Terrain;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.LoadItem;
import arcircle.ftsim.simulation.model.effect.EffectManager;
import arcircle.ftsim.simulation.model.task.TaskManager;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Field implements KeyListner, Renderer {

	SimGameModel sgModel;
	public SimGameModel getSgModel() {
		return sgModel;
	}

	private int nowTurn;
	public static final int TURN_FRIEND = 0;
	public static final int TURN_ENEMY = 1;

	private static int[][] RELATED_KEY_CURSOR = {
			{Input.KEY_UP, Cursor.UP},
			{Input.KEY_RIGHT, Cursor.RIGHT},
			{Input.KEY_DOWN, Cursor.DOWN},
			{Input.KEY_LEFT, Cursor.LEFT},
		};

	private String partName;

	private Cursor cursor;

	Characters characters;

	HashMap<String, Item> itemList;

	public EventManager eventManager;

	private TaskManager taskManager;

	private SoundManager soundManager;

	private EffectManager effectManager;

	private SubInfoWindow subInfoWindow;

	private LoadField loadField;

	public int offsetX;
	public int offsetY;
	public int firstTileX;
	public int lastTileX;
	public int firstTileY;
	public int lastTileY;

	public SubInfoWindow getSubInfoWindow() {
		return subInfoWindow;
	}
	public void setSubInfoWindow(SubInfoWindow subInfoWindow) {
		this.subInfoWindow = subInfoWindow;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public Field(SimGameModel sgModel) {
		this.sgModel = sgModel;
		this.characters = new Characters();
		this.setNowTurn(TURN_FRIEND);
	}

	//TODO:マジックナンバー多発地帯！
	public void init(String subStoryFolderPath) {
		itemList = LoadItem.loadItemList();

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
	}

	public void changeTurnFriend() {
		setNowTurn(TURN_FRIEND);
		cursor.setVisible(true);
	}

	public void changeTurnEnemy() {
		setNowTurn(TURN_ENEMY);
		cursor.setVisible(false);
	}

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

		if (cursor.isVisible()) {
			// カーソルを描く
			cursor.draw(g, offsetX, offsetY);
		}

		if (effectManager.existEffect()) {
			effectManager.render(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}

		if (taskManager.existTask()) {
			taskManager.processRender(
					g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		getCursor().update(container, game, delta);

		if (effectManager.existEffect()) {
			effectManager.update();
		}

		//タスクがあるときはカーソルは不可視
		if (taskManager.existTask()) {
			getCursor().setVisible(false);
			taskManager.processUpdate(delta);
			return;
		}

		//タスクがないときはターンによってカーソルの可視を変える
		if (nowTurn == TURN_FRIEND) {
			getCursor().setVisible(true);
		} else {
			getCursor().setVisible(false);
		}

		characters.update(delta);
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if (!cursor.isVisible()) {
			return;
		}

		for (int i = 0; i < RELATED_KEY_CURSOR.length; i++) {
			if (keyInput.isKeyDown(RELATED_KEY_CURSOR[i][0])) {
				getCursor().startMove(RELATED_KEY_CURSOR[i][1]);
			}
			if (keyInput.isKeyPressed(RELATED_KEY_CURSOR[i][0])) {
				getCursor().pressed(RELATED_KEY_CURSOR[i][1]);
			}
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
		int [][] moveCostArray = loadField.createMoveCostArray(charaX, charaY, getXYChara(charaX, charaY));

		return characters.modifyMoveCostArray(charaX, charaY, moveCostArray);
	}

	/**
	 * あるキャラの周囲のキャラを探索し格納して返す．
	 * @param chara 探索の中心のキャラ，これと同じ所属のキャラを返す
	 * @param aroundMassNum マスいくつ分離れているキャラを探索するか
	 * @return 探索できたキャラが入る
	 */
	public ArrayList<Chara> getAroundChara(Chara chara, int aroundMassNum) {
		return characters.getAroundChara(chara, aroundMassNum);
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
		return eventManager.getWinConditionString();
	}

	/**
	 * 敗北条件を渡すメソッド
	 * @return 敗北条件のStringが入った配列を返す， 敗北条件がない場合は，nullを返す
	 */
	public ArrayList<String> getLoseConditionString() {
		return eventManager.getLoseConditionString();
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
			if (chara.getCamp() != Chara.CAMP_FRIEND) {
				continue;
			}
			FTSimulationGame.save.putCharaStatus(chara.getFolderName(), chara.status);
		}
		FTSimulationGame.save.clearNowStage();
	}

	public int getFieldRow() {
		return loadField.getRow();
	}

	public int getFieldCol() {
		return loadField.getCol();
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

	public LoadField getLoadField() {
		return loadField;
	}

}
