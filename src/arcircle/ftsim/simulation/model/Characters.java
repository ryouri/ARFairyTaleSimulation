package arcircle.ftsim.simulation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.simulation.chara.ai.SameTimeAttackAI;
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.talk.BattleTalkModel;
import arcircle.ftsim.state.simgame.SimGameModel;

/**
 * @author misawa
 *
 */
public class Characters {
	private SimGameModel sgModel;

	private int row;
	private int col;

	public static String CharactersFolderName = "Characters/";

	public HashMap<String, SpriteSheet> walkSheetMap;

	public HashMap<String, Animation> upWalkAnimeMap;
	public HashMap<String, Animation> downWalkAnimeMap;
	public HashMap<String, Animation> leftWalkAnimeMap;
	public HashMap<String, Animation> rightWalkAnimeMap;

	public HashMap<String, Animation> upAttackAnimeMap;
	public HashMap<String, Animation> downAttackAnimeMap;
	public HashMap<String, Animation> leftAttackAnimeMap;
	public HashMap<String, Animation> rightAttackAnimeMap;

	public HashMap<String, Animation> stayAnimeMap;
	public HashMap<String, Animation> cursorAnimeMap;

	public HashMap<String, Animation> selectAnimeMap;

	public HashMap<String, Chara> characterData;

	public HashMap<String, Image> characterFaceStandardImageMap;

	public ArrayList<Chara> characterArray;

	HashMap<String, Item> itemMap;

	private Field field;

	private Image hpBar;

	private final static Color enemyHP = Color.red;
	private final static Color friendHP = Color.blue;
	private final static int HP_BAR_HEIGHT = 3;

//	private TaskManager taskManager;

	public void checkStandEvent(Chara chara) {
		getField().eventManager.checkStandEvent(chara);
	}

	public Characters() {
		this.walkSheetMap = new HashMap<String, SpriteSheet>();

		this.upWalkAnimeMap = new HashMap<String, Animation>();
		this.downWalkAnimeMap = new HashMap<String, Animation>();
		this.leftWalkAnimeMap = new HashMap<String, Animation>();
		this.rightWalkAnimeMap = new HashMap<String, Animation>();

		this.upAttackAnimeMap = new HashMap<String, Animation>();
		this.downAttackAnimeMap = new HashMap<String, Animation>();
		this.leftAttackAnimeMap = new HashMap<String, Animation>();
		this.rightAttackAnimeMap = new HashMap<String, Animation>();

		this.stayAnimeMap = new HashMap<String, Animation>();
		this.cursorAnimeMap = new HashMap<String, Animation>();
		this.selectAnimeMap = new HashMap<String, Animation>();

		this.characterFaceStandardImageMap = new HashMap<String, Image>();

		this.characterArray = new ArrayList<Chara>();

		this.characterData = new HashMap<String, Chara>();
	}

	public void init(SimGameModel sgModel, Field field, int row, int col, HashMap<String, Item> itemMap) {
		this.setSgModel(sgModel);
		this.field = field;
		this.row = row;
		this.col = col;
		this.itemMap = itemMap;

		try {
			this.hpBar = new Image("image/hpBar.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		LoadCharacter loadCharacter = new LoadCharacter(this);
		loadCharacter.load();

//		this.taskManager = new TaskManager(field, this);
	}

	/**
	 * putCharacterのファイルから，キャラクターを追加する
	 * @param putCharacterPath キャラクターの配置のテキストファイルが入っているパス
	 */
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
				addCharacter(charaLine);
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addCharacter(String charaLine) {
		String[] charaPuts = charaLine.split(",");
		if (charaPuts.length == 0) {
			return;
		}

		String folderName = charaPuts[1];
		Chara chara = new Chara(folderName, this);
		chara.id = charaPuts[0];
		chara.setCamp(Integer.valueOf(charaPuts[2]));
		chara.x = Integer.valueOf(charaPuts[3]);
		chara.y = Integer.valueOf(charaPuts[4]);
		chara.pX = chara.x * LoadField.MAP_CHIP_SIZE;
		chara.pY = chara.y * LoadField.MAP_CHIP_SIZE;

		setCharaData(folderName, chara);

		//TODO: 現在は，デバッグのために新しいAIを試す
		if (chara.getCamp() == Chara.CAMP_ENEMY) {
			chara.setAI(new SameTimeAttackAI(chara, getField(), this));
		}

		characterArray.add(chara);
	}

	public static final String PLAYER_NAME = "player";

	private void setCharaData(String folderName, Chara chara) {
		Status charaStatus = FTSimulationGame.save.getCharaStatus(folderName);

		//セーブのないキャラなら
		if (charaStatus == null) {
			//playerの処理
			if (folderName.length() >= 6 && folderName.substring(0, 6).equals(PLAYER_NAME)) {
				String playerFolderName = PLAYER_NAME;
				//男
				if (FTSimulationGame.save.getPlayer().gender == Status.MALE) {
					playerFolderName = playerFolderName + "Male";
				} else if (FTSimulationGame.save.getPlayer().gender == Status.FEMALE) {
					playerFolderName = playerFolderName + "Female";
				} else {
					System.err.println("Player Gender Error");
				}
				chara.setItemList(characterData.get(playerFolderName).getItemList());
				characterData.get(playerFolderName).status.copyTo(chara.status);
				characterData.get(playerFolderName).status.growRate.copyTo(chara.status.growRate);

				chara.status.name = FTSimulationGame.save.getPlayer().name;

				return;
			}

			//TODO: アイテムデータのコピーが未完成
			chara.setItemList(characterData.get(folderName).getItemList());
			characterData.get(folderName).status.copyTo(chara.status);
			characterData.get(folderName).status.growRate.copyTo(chara.status.growRate);
		} else { //セーブのあるキャラなら
			//TODO: アイテムデータのコピーが未完成
			chara.setItemList(charaStatus.getItemList());
			charaStatus.copyTo(chara.status);
			charaStatus.growRate.copyTo(chara.status.growRate);
		}

	}

	public static final Color standColor = new Color(0.5f, 0.5f, 0.5f, 1);

	public void render(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX,
			int firstTileY, int lastTileY) {

		for (Chara chara : characterArray) {
			//範囲内でなければ
			if (!(firstTileX <= chara.x && chara.x <= lastTileX
				&& firstTileY <= chara.y && chara.y <= lastTileY)) {
				continue;
			}

			if (chara.isAttack()) {
				continue;
				//renderAttack(chara, g, offsetX, offsetY);
			} else if (chara.isStand()) {
				Animation anime = downWalkAnimeMap.get(chara.getFolderName());
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY,
						standColor);
			} else if (chara.isMoving) {
				Animation anime = null;
				if (chara.direction == Chara.UP) {
					anime = upWalkAnimeMap.get(chara.getFolderName());
				} else if (chara.direction == Chara.RIGHT) {
					anime = rightWalkAnimeMap.get(chara.getFolderName());
				} else if (chara.direction == Chara.LEFT) {
					anime = leftWalkAnimeMap.get(chara.getFolderName());
				} else {	//if (chara.direction == Chara.DOWN) {
					anime = downWalkAnimeMap.get(chara.getFolderName());
				}
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY,
						new Color(chara.getColor().getRed(),
								chara.getColor().getGreen(),
								chara.getColor().getBlue(),
								chara.getAlpha()));
			} else if (chara.isSelect) {
				//Animation anime = selectAnimeMap.get(chara.status.name);
				Animation anime = downAttackAnimeMap.get(chara.getFolderName());
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY,
						new Color(chara.getColor().getRed(),
								chara.getColor().getGreen(),
								chara.getColor().getBlue(),
								chara.getAlpha()));
			} else {
				//Animation anime = stayAnimeMap.get(chara.status.name);
				Animation anime = downWalkAnimeMap.get(chara.getFolderName());
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY,
						new Color(chara.getColor().getRed(),
								chara.getColor().getGreen(),
								chara.getColor().getBlue(),
								chara.getAlpha()));
			}

			if (chara.getCamp() == Chara.CAMP_FRIEND) {
				g.setColor(friendHP);
				g.fillRect(chara.pX + offsetX + 1, chara.pY + offsetY,
						(int)(((chara.status.getHp() * 1.0) / (chara.status.maxHp * 1.0)) * 30),
						HP_BAR_HEIGHT);
			} else if (chara.getCamp() == Chara.CAMP_ENEMY) {
				g.setColor(enemyHP);
				g.fillRect(chara.pX + offsetX + 1, chara.pY + offsetY,
						(int)(((chara.status.getHp() * 1.0) / (chara.status.maxHp * 1.0)) * 30),
						HP_BAR_HEIGHT);
			}
			g.setColor(Color.white);
		}

//		if (taskManager.existTask()) {
//			taskManager.processRender(
//					g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
//		}
	}

	public void update(int delta) {
		//選択されているキャラを早く動かす
		if (getField().getNowTurn() == Field.TURN_FRIEND && !getField().getTaskManager().existTask()) {
			for (Chara chara : characterArray) {
				chara.isSelect = false;
				if (chara.x == getField().getCursor().x
						&& chara.y == getField().getCursor().y) {
					chara.isSelect = true;
				}
			}
		}

		if (getField().getSgModel().getKeyInputStackByFirst() instanceof BattleTalkModel) {
			return;
		}

		if (getField().getNowTurn() == Field.TURN_FRIEND) {
			//FRIENDキャラが全軍待機していたら敵ターンへ
			boolean standCharaFlag = checkCharaStand(Chara.CAMP_FRIEND);

			if (standCharaFlag == true) {
				getField().getTaskManager().addTurnEndTask(this, Chara.CAMP_FRIEND);
			}
		} else if (getField().getNowTurn() == Field.TURN_ENEMY) {
			//タスクがなければ，AIを一キャラ分動作させる
			if (getField().getTaskManager().existTask()) {
				return;
			}

			//TODO:敵の動作処理
			for (Chara chara : characterArray) {
				if (chara.getCamp() != Chara.CAMP_ENEMY || chara.isStand()) {
					continue;
				}
				chara.getAI().thinkAndDo();
				break;
			}

			boolean standCharaFlag = checkCharaStand(Chara.CAMP_ENEMY);

			if (standCharaFlag == true) {
				getField().getTaskManager().addTurnEndTask(this, Chara.CAMP_ENEMY);
			}
		}
	}

	/**
	 * 指定された所属のキャラを全て行動可能にする
	 */
	public void standForAllCampChara(int charaCamp) {
		for (Chara chara : characterArray) {
			if (chara.getCamp() == charaCamp) {
				chara.setStand(false);
			}
		}
	}

	/**
	 * @param charaCamp すべての動作が完了しているか，チェックしたい所属
	 * @return 待機が完了していればtrue，そうじゃなきゃfalse
	 */
	private boolean checkCharaStand(int charaCamp) {
		//ENEMYキャラが全軍待機していたら味方ターンへ
		for (Chara chara : characterArray) {
			if (chara.getCamp() == charaCamp && !chara.isStand()) {
				return false;
			}
		}
		return true;
	}

	public boolean isGameEnd() {
		boolean endFlag = true;
		for (Chara chara : characterArray) {
			if (chara.getCamp() == Chara.CAMP_ENEMY) {
				endFlag = false;
			}
		}

		return endFlag;
	}

	public SimGameModel getSgModel() {
		return sgModel;
	}

	public void setSgModel(SimGameModel sgModel) {
		this.sgModel = sgModel;
	}

	public void removeChara(Chara chara) {
		characterArray.remove(chara);
	}

	public Chara getXYChara(int x, int y) {
		for (Chara chara : characterArray) {
			if (chara.x == x && chara.y == y) {
				return chara;
			}
		}
		return null;
	}

	public Chara getAnyChara() {
		for (Chara chara : characterArray) {
			return chara;
		}
		return null;
	}

	public Field getField() {
		return field;
	}

	public int [][] modifyMoveCostArray(int charaX, int charaY, int[][] moveCostArray) {
		Chara moveChara = null;
		//移動するキャラを取得する
		for (Chara chara : characterArray) {
			if (charaX == chara.x && charaY == chara.y) {
				moveChara = chara;
				break;
			}
		}

		if (moveChara == null) {
			System.err.println("移動コスト計算でエラー 移動キャラが取得できない");
			System.exit(1);
		}

		//今は同じ所属(Camp)のキャラなら通過できるようにする
		//TODO: Friendは友軍のキャラを通過できるようにしたいなら，処理を変えよう
		for (Chara chara : characterArray) {
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

	/**
	 * あるキャラの周囲のキャラを探索し格納して返す．
	 * @param chara 探索の中心のキャラ，これと同じ所属のキャラを返す
	 * @param aroundMassNum マスいくつ分離れているキャラを探索するか
	 * @return 探索できたキャラが入る
	 */
	public ArrayList<Chara> getAroundChara(Chara chara, int aroundMassNum) {
		ArrayList<Chara> aroundCharaArray = new ArrayList<Chara>();

		for (Chara targetChara : characterArray) {
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
}
