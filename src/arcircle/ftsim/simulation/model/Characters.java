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

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.ai.SimpleAI;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Characters {
	private SimGameModel sgModel;

	private int row;
	private int col;

	public static String CharactersFolderName = "Characters/";

	public HashMap<String, SpriteSheet> walkSheetMap;
	public HashMap<String, SpriteSheet> readySheetMap;
	public HashMap<String, SpriteSheet> attackSheetMap;

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

	public ArrayList<Chara> characterArray;

	private ArrayList<AttackInfo> attackInfoArray;
	private int nowAttackIndex;
	private boolean isAttackChara;

	HashMap<String, Item> itemMap;

	private Field field;

	private Image hpBar;

	public Characters() {
		this.walkSheetMap = new HashMap<String, SpriteSheet>();
		this.readySheetMap = new HashMap<String, SpriteSheet>();
		this.attackSheetMap = new HashMap<String, SpriteSheet>();

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

		this.characterArray = new ArrayList<Chara>();

		this.attackInfoArray = new ArrayList<AttackInfo>();
		this.nowAttackIndex = 0;
		this.isAttackChara = false;

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
	}


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
				String[] charaPuts = charaLine.split(" ");
				if (charaPuts.length == 0) {
					continue;
				}

				Chara chara = new Chara(charaPuts[0]);
				chara.setCamp(Integer.valueOf(charaPuts[1]));
				chara.x = Integer.valueOf(charaPuts[2]);
				chara.y = Integer.valueOf(charaPuts[3]);
				chara.pX = chara.x * Field.MAP_CHIP_SIZE;
				chara.pY = chara.y * Field.MAP_CHIP_SIZE;
				//TODO; キャラクターデータのコピーが未完成 AIの実装もね
				chara.setItemList(characterData.get(chara.status.name).getItemList());
				characterData.get(chara.status.name).status.copyTo(chara.status);

				chara.setAI(new SimpleAI(chara));

				characterArray.add(chara);
			}
		} catch (NumberFormatException | IOException e1) {
			e1.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final Color standColor = new Color(0.5f, 0.5f, 0.5f, 1);

	private void renderAttack(Chara chara, Graphics g, int offsetX, int offsetY) {
		int change = chara.getAttackTime();
		if (change <= 5 || change >= Chara.MAX_ATTACK_TIME - 5) {
			change = 0;
		} else if (change < Chara.MAX_ATTACK_TIME / 2) {
			change -= 5;
		} else if (change >= Chara.MAX_ATTACK_TIME / 2) {
			change = Chara.MAX_ATTACK_TIME - change - 5;
		}

		Animation anime = null;
		if (chara.direction == Chara.UP) {
			anime = upAttackAnimeMap.get(chara.status.name);
		} else if (chara.direction == Chara.RIGHT) {
			anime = rightAttackAnimeMap.get(chara.status.name);
		} else if (chara.direction == Chara.LEFT) {
			anime = leftAttackAnimeMap.get(chara.status.name);
		} else {//(chara.direction == Chara.DOWN) {
			anime = downAttackAnimeMap.get(chara.status.name);
		}

		int changeX = 0;
		int changeY = 0;

		if (chara.direction == Chara.UP) {
			changeY = -change;
		} else if (chara.direction == Chara.RIGHT) {
			changeX = change;
			if (chara.getAttackRightLeftDirection() == Chara.UP) {
				changeY = -change;
			} else if (chara.getAttackRightLeftDirection() == Chara.DOWN) {
				changeY = change;
			}
		} else if (chara.direction == Chara.LEFT) {
			changeX = -change;
			if (chara.getAttackRightLeftDirection() == Chara.UP) {
				changeY = -change;
			} else if (chara.getAttackRightLeftDirection() == Chara.DOWN) {
				changeY = change;
			}
		} else {//(chara.direction == Chara.DOWN) {
			changeY = change;
		}

		anime.draw(
				chara.pX + offsetX + changeX,
				chara.pY + offsetY + changeY);
	}

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
				renderAttack(chara, g, offsetX, offsetY);
			} else if (chara.isStand()) {
				Animation anime = downWalkAnimeMap.get(chara.status.name);
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY,
						standColor);
			} else if (chara.isMoving) {
				Animation anime = null;
				if (chara.direction == Chara.UP) {
					anime = upWalkAnimeMap.get(chara.status.name);
				} else if (chara.direction == Chara.RIGHT) {
					anime = rightWalkAnimeMap.get(chara.status.name);
				} else if (chara.direction == Chara.LEFT) {
					anime = leftWalkAnimeMap.get(chara.status.name);
				} else {//if (chara.direction == Chara.DOWN) {
					anime = downWalkAnimeMap.get(chara.status.name);
				}
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY);
			} else if (chara.isSelect) {
				//Animation anime = selectAnimeMap.get(chara.status.name);
				Animation anime = downAttackAnimeMap.get(chara.status.name);
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY);
			} else {
				//Animation anime = stayAnimeMap.get(chara.status.name);
				Animation anime = downWalkAnimeMap.get(chara.status.name);
				anime.draw(
						chara.pX + offsetX,
						chara.pY + offsetY);
			}

			hpBar.getSubImage(chara.pX + offsetX,
					chara.pY + offsetY,
					(int)(((chara.status.hp * 1.0) / (chara.status.maxHp * 1.0)) * 32),
					4).draw(chara.pX + offsetX,
					chara.pY + offsetY);;
		}
	}

	public void update(int delta) {
		//選択されているキャラの処理
		for (Chara chara : characterArray) {
			chara.isSelect = false;
			if (chara.x == field.getCursor().x && chara.y == field.getCursor().y) {
				chara.isSelect = true;
			}
		}

		//攻撃しているキャラの処理
		if (isAttackChara == false && attackInfoArray.size() > 0) {
			AttackInfo attackInfo = attackInfoArray.get(nowAttackIndex);
			charaAttack(attackInfo.attackChara, attackInfo.damageChara);
			isAttackChara = true;
		} else if (isAttackChara == true && attackInfoArray.size() > 0){
			AttackInfo attackInfo = attackInfoArray.get(nowAttackIndex);
			Chara attackChara = attackInfo.attackChara;
			attackChara.setAttackTime(attackChara.getAttackTime() + 1);
			//攻撃時間が一定以上になったら次のキャラへ
			if (attackChara.getAttackTime() >= Chara.MAX_ATTACK_TIME) {
				//攻撃を受ける側にダメージを与える
				Chara damageChara = attackInfo.damageChara;
				damageChara.status.hp -= attackChara.status.power - damageChara.status.defence;

				//攻撃を受けた側のhpがなくなったら
				if (damageChara.status.hp < 0) {
					characterArray.remove(damageChara);
				}

				attackChara.setAttack(false);
				isAttackChara = false;
				nowAttackIndex++;
				//最後のキャラまで行ったら，もしくはダメージを受けたキャラが死んだら
				if (nowAttackIndex >= attackInfoArray.size() || damageChara.status.hp < 0) {
					AttackInfo standAttackInfo = attackInfoArray.get(0);
					standAttackInfo.attackChara.setStand(true);
					standAttackInfo.damageChara.resetState();
					attackInfoArray.clear();
					field.setCursorVisible(true);
				}
			}
		}

		if (field.getNowTurn() == Field.TURN_FRIEND) {
			boolean standCharaFlag = true;
			//FRIENDキャラが全軍待機していたら敵ターンへ
			for (Chara chara : characterArray) {
				if (chara.getCamp() == Chara.CAMP_FRIEND && !chara.isStand()) {
					standCharaFlag = false;
				}
			}

			if (standCharaFlag == true) {
				field.changeTurnEnemy();
				for (Chara chara : characterArray) {
					if (chara.getCamp() == Chara.CAMP_FRIEND) {
						chara.setStand(false);
					}
				}
			}
		} else if (field.getNowTurn() == Field.TURN_ENEMY) {
			if (attackInfoArray.size() > 0) {
				return;
			}

			//TODO:敵の動作処理
			for (Chara chara : characterArray) {
				if (chara.getCamp() != Chara.CAMP_ENEMY || chara.isStand()) {
					continue;
				}
				chara.getAI().thinkAndDo(field, this);
				break;
			}

			boolean standCharaFlag = true;
			//ENEMYキャラが全軍待機していたら敵ターンへ
			for (Chara chara : characterArray) {
				if (chara.getCamp() == Chara.CAMP_ENEMY && !chara.isStand()) {
					standCharaFlag = false;
				}
			}

			if (standCharaFlag == true) {
				field.changeTurnFriend();
				for (Chara chara : characterArray) {
					if (chara.getCamp() == Chara.CAMP_ENEMY) {
						chara.setStand(false);
					}
				}
			}
			//field.changeTurnFriend();
		}
	}

	private void charaAttack(Chara chara, Chara damageChara) {
		field.setCursorVisible(false);
		chara.setAttack(true);
		if (damageChara.x > chara.x) {
			chara.direction = Chara.RIGHT;

			damageChara.direction = Chara.LEFT;
			damageChara.setMoving(true);

			chara.setAttackRightLeftDirection(Chara.RIGHT);
			if (damageChara.y < chara.y) {
				chara.setAttackRightLeftDirection(Chara.UP);
			} else if (damageChara.y > chara.y) {
				chara.setAttackRightLeftDirection(Chara.DOWN);
			}
		} else if (damageChara.x < chara.x) {
			chara.direction = Chara.LEFT;

			damageChara.direction = Chara.RIGHT;
			damageChara.setMoving(true);

			chara.setAttackRightLeftDirection(Chara.LEFT);
			if (damageChara.y < chara.y) {
				chara.setAttackRightLeftDirection(Chara.UP);
			} else if (damageChara.y > chara.y) {
				chara.setAttackRightLeftDirection(Chara.DOWN);
			}
		} else if (damageChara.y < chara.y) {
			chara.direction = Chara.UP;

			damageChara.direction = Chara.DOWN;
			damageChara.setMoving(true);
		} else if (damageChara.y > chara.y) {
			chara.direction = Chara.DOWN;

			damageChara.direction = Chara.UP;
			damageChara.setMoving(true);
		}
	}

	public void setCharaAttack(Chara chara, Chara damageChara) {
		attackInfoArray.add(new AttackInfo(chara, damageChara));
		attackInfoArray.add(new AttackInfo(damageChara, chara));
		this.nowAttackIndex = 0;
	}

	public boolean isEnd() {
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
}
