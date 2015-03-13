package arcircle.ftsim.simulation.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.Weapon;

//

/**戦闘画面右のウィンドウ画面内を描画するクラス
 * 普通は選択したキャラのステータスを表示
 * 戦闘を選択した場合は画面を切り替えて戦闘前画面にする
 * @author ゆきねこ */
public class SubInfoWindow implements Renderer{
	public static int START_WIDTH  = 800;
	public static int START_HEIGHT = 0;
	public static int WIDTH  = 320;
	public static int HEIGHT = 640;
	private final int SIWOffsetX = 10;	//画面左余白	//SIW = SubInfoWindow の略
	private final int SIWOffsetY = 10;	//画面上余白
	private final int CHAR_SIZE = 24;	//文字のサイズ
	private final int LINE_INTERVAL = 5;	//行間のサイズ
	private final int IMAGE_SIZE = 64;	//キャラ画像のサイズ
	private final int CHIP_SIZE = 32;
	private final int HPBAR_WIDTH = 140;
	private final int HPBAR_HEIGHT = 20;


	//現在描画している情報
	public static final int FIELD_TERRAIN_INFO = 0;
	public static final int ATTACK_INFO = 1;
	private ExpectBattleInfo expectBattleInfo;
	/** 上の２つの定数で描画する情報を変更 */
	private int viewMode;
	private boolean twice = true;

	private UnicodeFont font = FTSimulationGame.font;

	private Image backGround;
	private Image topBackGround;
	private Image underBackGround;
	private String gender;
	private String weaponName;
	private String itemName;

	private HashMap<String, Animation> upWalkAnimeMap;
	private HashMap<String, Animation> downWalkAnimeMap;
	private HashMap<String, Animation> leftWalkAnimeMap;
	private HashMap<String, Animation> rightWalkAnimeMap;
	private HashMap<String, Point> objectPos_status;
	private HashMap<String, Point> objectPos_battle;

	private HashMap<String, Image> faceImageMap;

	private Animation[] IMAGEAnime;

	private Field field;
	private Image selectedMapChip;
	/** 地形の名前 */
	private String terrainName;
	private int avoidPoint;
	private int defencePoint;
	private int itemPoint = 0;
	private String range = "";

	/**サブインフォウィンドウのコンストラクタ
	 * @param field */
	public SubInfoWindow(Field field) {
		super();
		this.field = field;
		this.viewMode = FIELD_TERRAIN_INFO;
		//キャラ画像の部分は4パターンの歩行アニメーションを表示して64×64を埋める
		IMAGEAnime = new Animation[4];
		//ステータス画面の各オブジェクトの描画位置
		objectPos_status = new HashMap<String, Point>();
		//戦闘前画面の各オブジェクトの描画位置
		objectPos_battle = new HashMap<String, Point>();
		//キャラ画像に用いるアニメーションの読み込み
		upWalkAnimeMap = field.characters.upWalkAnimeMap;
		downWalkAnimeMap = field.characters.downWalkAnimeMap;
		leftWalkAnimeMap = field.characters.leftWalkAnimeMap;
		rightWalkAnimeMap = field.characters.rightWalkAnimeMap;
		//各オブジェクトの情報画面の位置を設定する
		setStatusObjectPos();
		setBattleObjectPos();

		try {
			backGround = new Image("image/subInfoWindow/subInfoWindow.png");
			topBackGround = new Image("image/subInfoWindow/subInfoWin_Follow.png");
			underBackGround = new Image("image/subInfoWindow/subInfoWin_Enemy.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setFont(font);	//フォントのセット(これをやらないと文字描画されない)
		if(viewMode == FIELD_TERRAIN_INFO){
			drawStatusWindow(g);
		} else if(viewMode == ATTACK_INFO){
			drawBattleWindow(g);
		}
	}

	private void drawStatusWindow(Graphics g){
		g.drawImage(backGround, START_WIDTH, START_HEIGHT);
		//ステージ名の描画
		//TODO もしかしてstoryNameの取得はコンストラクタでよい？
		String partName = field.getPartName();
		g.drawString("ステージ:" + partName,
				objectPos_status.get("STAGE_NAME").x, objectPos_status.get("STAGE_NAME").y);	//ステージ名の描画
		//勝利条件の描画
		//TODO クリア目標で表示するストリングの取得
		g.drawString("勝利条件",
				objectPos_status.get("WIN_TERM").x, objectPos_status.get("WIN_TERM").y);	//ステージ名の描画
		//勝利条件内容の描画
		g.drawString("・" + "勝利条件内容",
				objectPos_status.get("WIN_STRING").x, objectPos_status.get("WIN_STRING").y);
		//敗北条件
		g.drawString("敗北条件",
				objectPos_status.get("LOSE_TERM").x, objectPos_status.get("LOSE_TERM").y);
		//敗北条件内容
		g.drawString("・" + "敗北条件内容",
				objectPos_status.get("LOSE_STRING").x, objectPos_status.get("LOSE_STRING").y);
		//地形情報の描画
		selectedMapChip = field.getSelectedMapChip();
		g.drawImage(selectedMapChip.getScaledCopy(2), objectPos_status.get("MAP_INFOIMAGE").x, objectPos_status.get("MAP_INFOIMAGE").y);
		//地形の名前
		terrainName = field.getSelectedTerrain().terrainName;
		g.drawString(terrainName,
				objectPos_status.get("MAP_NAME").x, objectPos_status.get("MAP_NAME").y);
		//地形の回避,防御値
		avoidPoint = field.getSelectedTerrain().avoidPoint;
		defencePoint = field.getSelectedTerrain().defencePoint;
		g.drawString("回避:" + avoidPoint + "% 防御:" + defencePoint + "%",
				objectPos_status.get("MAP_ABOID").x, objectPos_status.get("MAP_ABOID").y);
		//情報を描画する対象のCharaを取得
		//TODO: 下で取得したキャラの情報を描画してくれればOK
		//CharaからStatusとItemを取得して描画すればOK
		Chara renderInfoChara = field.getSelectedChara();
		if(renderInfoChara == null){
			return;
		}
		Status charaStatus = renderInfoChara.status;

		if(charaStatus.gender == Status.MALE){
			gender = "おとこ";
		}else if(charaStatus.gender == Status.FEMALE){
			gender = "おんな";
		}
		//名前の描画
		g.drawString("名前 : " + charaStatus.name,
				objectPos_status.get("CHARA_NAME").x, objectPos_status.get("CHARA_NAME").y);	//名前の描画
		//性別の描画
		g.drawString("性別 : " + gender,
				objectPos_status.get("CHARA_GENDER").x, objectPos_status.get("CHARA_GENDER").y);	//名前の描画
		//選択キャラのアニメーションの取得
		IMAGEAnime[0] = downWalkAnimeMap.get(charaStatus.name);
		IMAGEAnime[1] = upWalkAnimeMap.get(charaStatus.name);
		IMAGEAnime[2] = leftWalkAnimeMap.get(charaStatus.name);
		IMAGEAnime[3] = rightWalkAnimeMap.get(charaStatus.name);
		//アニメーションの表示
		IMAGEAnime[0].draw(objectPos_status.get("CHARA_IMAGE").x , objectPos_status.get("CHARA_IMAGE").y);
		IMAGEAnime[1].draw(objectPos_status.get("CHARA_IMAGE").x + CHIP_SIZE, objectPos_status.get("CHARA_IMAGE").y);
		IMAGEAnime[2].draw(objectPos_status.get("CHARA_IMAGE").x , objectPos_status.get("CHARA_IMAGE").y + CHIP_SIZE);
		IMAGEAnime[3].draw(objectPos_status.get("CHARA_IMAGE").x + CHIP_SIZE , objectPos_status.get("CHARA_IMAGE").y + CHIP_SIZE);

		//HP/MAXHPの表示
		g.drawString("HP : " + charaStatus.hp + " / " +  charaStatus.maxHp,
				objectPos_status.get("CHARA_HP").x, objectPos_status.get("CHARA_HP").y);
		//レベルの表示
		g.drawString("レベル : " + charaStatus.level,
				objectPos_status.get("CHARA_LEVEL").x, objectPos_status.get("CHARA_LEVEL").y);
		//ちから
		g.drawString("ちから : " + charaStatus.power,
				objectPos_status.get("CHARA_POWER").x, objectPos_status.get("CHARA_POWER").y);
		//まりょく
		g.drawString("まりょく : " + charaStatus.magicPower,
				objectPos_status.get("CHARA_MAGICPOWER").x, objectPos_status.get("CHARA_MAGICPOWER").y);
		//はやさ
		g.drawString("はやさ : " + charaStatus.speed,
				objectPos_status.get("CHARA_SPEED").x, objectPos_status.get("CHARA_SPEED").y);
		//わざ
		g.drawString("わざ : " + charaStatus.tech,
				objectPos_status.get("CHARA_TECH").x, objectPos_status.get("CHARA_TECH").y);
		//運
		g.drawString("運 : " + charaStatus.luck,
				objectPos_status.get("CHARA_LUCK").x, objectPos_status.get("CHARA_LUCK").y);
		//ぼうぎょ
		g.drawString("ぼうぎょ : " + charaStatus.defence,
				objectPos_status.get("CHARA_DEFENCE").x, objectPos_status.get("CHARA_DEFENCE").y);
		//まぼう
		g.drawString("まぼう : " + charaStatus.magicDefence,
				objectPos_status.get("CHARA_MAGICDEFENCE").x, objectPos_status.get("CHARA_MAGICDEFENCE").y);
		//いどう
		g.drawString("いどう : " + charaStatus.move,
				objectPos_status.get("CHARA_MOVE").x, objectPos_status.get("CHARA_MOVE").y);
		//次のレベルまで
		g.drawString("EXP : " + charaStatus.exp + "/100",
				objectPos_status.get("NEXT_EXP").x, objectPos_status.get("NEXT_EXP").y);
		//武器,アイテム
		if(charaStatus.getItemList().isEmpty()){
			weaponName = "";
			itemName = "";
		}else{
			weaponName = "";
			itemName = "";
			for(int i = 0 ; i < charaStatus.getItemList().size() ; i++){
				if(charaStatus.getItemList().get(i) instanceof Weapon){
					Weapon weapon = (Weapon)charaStatus.getItemList().get(i);
					if(weapon.rangeType == -1){
						range = "(無)";
					}else if(weapon.rangeType == 0){
						range = "(近)";
					}else if(weapon.rangeType == 1){
						range = "(遠)";
					}else if(weapon.rangeType == 2){
						range = "(遠近)";
					}else{
						range = "";
					}
					itemPoint = weapon.power;
					weaponName = charaStatus.getItemList().get(i).name;
					break;
				}
			}
			for(int i = 0 ; i < charaStatus.getItemList().size() ; i++){
				if(!(charaStatus.getItemList().get(i) instanceof Weapon)){
					itemName = charaStatus.getItemList().get(i).name;
					break;
				}
			}
		}
		g.drawString("武器:" + weaponName + range,
				objectPos_status.get("WEAPON").x, objectPos_status.get("WEAPON").y);
		g.drawString("攻撃力:" + itemPoint,
				objectPos_status.get("ATTACK_POWER").x, objectPos_status.get("ATTACK_POWER").y);
		g.drawString("アイテム:" + itemName,
				objectPos_status.get("ITEM").x, objectPos_status.get("ITEM").y);
	}

	/**戦闘前画面の描画を行う*/
	private void drawBattleWindow(Graphics g){
		g.drawImage(topBackGround, START_WIDTH, START_HEIGHT);
		g.drawImage(underBackGround, START_WIDTH, START_HEIGHT + (HEIGHT/2));

		//戦闘を行うキャラが取得できるはず?
		//TODO ちゃんと戦闘を行うキャラの取得
		Chara battleChara = expectBattleInfo.getFirstChara();
		faceImageMap = field.characters.characterFaceStandardImageMap;
		//キャライメージの描画
		g.drawImage(faceImageMap.get(battleChara.status.name),
				objectPos_battle.get("CHARA_IMAGE").x, objectPos_battle.get("CHARA_IMAGE").y);
		//キャラ名の描画
		g.drawString(battleChara.status.name,
				objectPos_battle.get("CHARA_NAME").x, objectPos_battle.get("CHARA_NAME").y);
		//HPの描画
		g.drawString("HP",
				objectPos_battle.get("HP").x, objectPos_battle.get("HP").y);
		//HPバーの描画
		g.setColor(Color.red);
		g.fillRect(objectPos_battle.get("HP_BAR").x, objectPos_battle.get("HP_BAR").y, HPBAR_WIDTH, HPBAR_HEIGHT);
		int hp_bar = HPBAR_WIDTH * battleChara.status.hp / battleChara.status.maxHp;
		g.setColor(Color.green);
		g.fillRect(objectPos_battle.get("HP_BAR").x, objectPos_battle.get("HP_BAR").y, hp_bar, HPBAR_HEIGHT);
		g.setColor(Color.white);
		//NOWHP/MAXHPの描画
		g.drawString(battleChara.status.hp + " / " + battleChara.status.maxHp,
				objectPos_battle.get("HP_VALUE").x, objectPos_battle.get("HP_VALUE").y);
		//レベルの描画
		g.drawString("Lv." + battleChara.status.level,
				objectPos_battle.get("LEVEL").x, objectPos_battle.get("LEVEL").y);
		//武器名の描画
		ArrayList<Item> itemList = battleChara.status.getItemList();
		if(itemList.isEmpty()){
			weaponName = "";
			itemName = "";
		}else{
			weaponName = "";
			itemName = "";
			for(int i = 0 ; i < itemList.size() ; i++){
				if(itemList.get(i) instanceof Weapon){
					Weapon weapon = (Weapon)itemList.get(i);
					if(weapon.rangeType == -1){
						range = "(無)";
					}else if(weapon.rangeType == 0){
						range = "(近)";
					}else if(weapon.rangeType == 1){
						range = "(遠)";
					}else if(weapon.rangeType == 2){
						range = "(遠近)";
					}else{
						range = "";
					}
					itemPoint = weapon.power;
					weaponName = itemList.get(i).name;
					break;
				}
			}
			for(int i = 0 ; i < itemList.size() ; i++){
				if(!(itemList.get(i) instanceof Weapon)){
					itemName = itemList.get(i).name;
					break;
				}
			}
		}
		g.drawString("武器:" + weaponName,
				objectPos_battle.get("WEAPON_ICON").x, objectPos_battle.get("WEAPON_ICON").y);
		//攻撃力の描画
		//TODO 攻撃力の計算
		g.drawString("威力",
				objectPos_battle.get("ATTACK").x, objectPos_battle.get("ATTACK").y);
		//攻撃力値の描画
		//三澤：このように，expectBattleInfoのデータを利用すれば良い
		g.drawString(expectBattleInfo.getFirstCharaBattleInfo().getDamage() + "",
				objectPos_battle.get("ATTACK_VALUE").x, objectPos_battle.get("ATTACK_VALUE").y);
		//×２の描画
		if(expectBattleInfo.getFirstCharaBattleInfo().isTwiceAttack()){
			g.drawString("×2",
					objectPos_battle.get("×2").x, objectPos_battle.get("×2").y);
		}
		//命中
		g.drawString("命中  ***",
				objectPos_battle.get("HIT").x, objectPos_battle.get("HIT").y);
		//必殺
		g.drawString("必殺  ***",
				objectPos_battle.get("KILLER").x, objectPos_battle.get("KILLER").y);
//		g.drawLine(START_WIDTH, (HEIGHT/2), (START_WIDTH + WIDTH), (HEIGHT/2));
	}



	/** ステータス画面における各オブジェクトの位置を設定する */
	private void setStatusObjectPos(){
		//tempX,tempYは位置計算用の一時格納器
		//ステージ名_一番左上
		int tempX = START_WIDTH + SIWOffsetX;
		int tempY = START_HEIGHT + SIWOffsetY;
		objectPos_status.put("STAGE_NAME", new Point(tempX, tempY));	//ok
		//勝利条件
		//TODO 目標のStringをどこかから持ってきて代入
		tempX = objectPos_status.get("STAGE_NAME").x;
		tempY = objectPos_status.get("STAGE_NAME").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("WIN_TERM", new Point(tempX, tempY));	//ok
		//勝利条件内容
		tempX = objectPos_status.get("WIN_TERM").x;
		tempY = objectPos_status.get("WIN_TERM").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("WIN_STRING", new Point(tempX, tempY));	//ok
		//敗北条件
		tempX = objectPos_status.get("WIN_STRING").x;
		tempY = objectPos_status.get("WIN_STRING").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("LOSE_TERM", new Point(tempX, tempY));	//ok
		//敗北条件内容
		tempX = objectPos_status.get("LOSE_TERM").x;
		tempY = objectPos_status.get("LOSE_TERM").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("LOSE_STRING", new Point(tempX, tempY));	//ok
		//地形の画像
		tempX = objectPos_status.get("LOSE_STRING").x;
		tempY = objectPos_status.get("LOSE_STRING").y + (CHAR_SIZE * 2);
		objectPos_status.put("MAP_INFOIMAGE", new Point(tempX, tempY));
		//地形の名前
		tempX = objectPos_status.get("MAP_INFOIMAGE").x + IMAGE_SIZE + LINE_INTERVAL;
		tempY = objectPos_status.get("MAP_INFOIMAGE").y;
		objectPos_status.put("MAP_NAME", new Point(tempX, tempY));
		//地形_回避_防御
		tempX = objectPos_status.get("MAP_NAME").x;
		tempY = objectPos_status.get("MAP_NAME").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("MAP_ABOID", new Point(tempX, tempY));
		//名前
		tempX = objectPos_status.get("MAP_INFOIMAGE").x;
		tempY = objectPos_status.get("MAP_INFOIMAGE").y + IMAGE_SIZE + (CHAR_SIZE/2);
		objectPos_status.put("CHARA_NAME", new Point(tempX, tempY));	//ok
		//性別
		tempX = objectPos_status.get("CHARA_NAME").x;
		tempY = objectPos_status.get("CHARA_NAME").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("CHARA_GENDER", new Point(tempX, tempY));	//ok
		//キャラ画像_名前の下
		tempX = objectPos_status.get("CHARA_GENDER").x;
		tempY = objectPos_status.get("CHARA_GENDER").y + CHAR_SIZE + (LINE_INTERVAL * 2);
		objectPos_status.put("CHARA_IMAGE", new Point(tempX, tempY));	//ok
		//HP/MAXHP_キャラ画像の右
		tempX = objectPos_status.get("CHARA_IMAGE").x + IMAGE_SIZE + LINE_INTERVAL;
		tempY = objectPos_status.get("CHARA_IMAGE").y;
		objectPos_status.put("CHARA_HP", new Point(tempX, tempY));	//ok
		//レベル_HP/MAXHPの下
		tempX = objectPos_status.get("CHARA_HP").x;
		tempY = objectPos_status.get("CHARA_HP").y +  CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("CHARA_LEVEL", new Point(tempX, tempY));	//ok
		//次のレベルまで_運の下
		tempX = objectPos_status.get("CHARA_IMAGE").x;
		tempY = objectPos_status.get("CHARA_IMAGE").y + IMAGE_SIZE;
		objectPos_status.put("NEXT_EXP", new Point(tempX, tempY));
		//ちから_キャラ画像の下
		tempX = objectPos_status.get("CHARA_IMAGE").x;
		tempY = objectPos_status.get("NEXT_EXP").y + CHAR_SIZE + (LINE_INTERVAL * 2);
		objectPos_status.put("CHARA_POWER", new Point(tempX, tempY));	//ok
		//まりょく_ちからの右
		tempX = objectPos_status.get("CHARA_POWER").x + (WIDTH / 2);
		tempY = objectPos_status.get("CHARA_POWER").y;
		objectPos_status.put("CHARA_MAGICPOWER", new Point(tempX, tempY));
		//ぼうぎょ_ちからの下
		tempX = objectPos_status.get("CHARA_POWER").x;
		tempY = objectPos_status.get("CHARA_POWER").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("CHARA_DEFENCE", new Point(tempX, tempY));
		//まぼう_ぼうぎょの右
		tempX = objectPos_status.get("CHARA_DEFENCE").x + (WIDTH / 2);
		tempY = objectPos_status.get("CHARA_DEFENCE").y;
		objectPos_status.put("CHARA_MAGICDEFENCE", new Point(tempX, tempY));
		//はやさ_ぼうぎょの下
		tempX = objectPos_status.get("CHARA_DEFENCE").x;
		tempY = objectPos_status.get("CHARA_DEFENCE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("CHARA_SPEED", new Point(tempX, tempY));	//ok
		//わざ_はやさの右
		tempX = objectPos_status.get("CHARA_SPEED").x + (WIDTH / 2);
		tempY = objectPos_status.get("CHARA_SPEED").y;
		objectPos_status.put("CHARA_TECH", new Point(tempX, tempY));
		//運_はやさの下
		tempX = objectPos_status.get("CHARA_SPEED").x;
		tempY = objectPos_status.get("CHARA_SPEED").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("CHARA_LUCK", new Point(tempX, tempY));
		//いどう_運の右
		tempX = objectPos_status.get("CHARA_LUCK").x + (WIDTH / 2);
		tempY = objectPos_status.get("CHARA_LUCK").y;
		objectPos_status.put("CHARA_MOVE", new Point(tempX, tempY));
		//武器_運の下
		tempX = objectPos_status.get("CHARA_LUCK").x;
		tempY = objectPos_status.get("CHARA_LUCK").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("WEAPON", new Point(tempX, tempY));
		//攻撃力_武器の下
		tempX = objectPos_status.get("WEAPON").x;
		tempY = objectPos_status.get("WEAPON").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("ATTACK_POWER", new Point(tempX, tempY));
		//アイテム_武器の下
		tempX = objectPos_status.get("ATTACK_POWER").x;
		tempY = objectPos_status.get("ATTACK_POWER").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_status.put("ITEM", new Point(tempX, tempY));
	}

	/** 戦闘前画面における各オブジェクトの位置を設定する */
	private void setBattleObjectPos(){
		//味方キャラ
		//tempX,tempYは位置計算用の一時格納器
		//ステージ名_一番左上
		int tempX = START_WIDTH + SIWOffsetX;
		int tempY = START_HEIGHT + SIWOffsetY;
		objectPos_battle.put("CHARA_IMAGE", new Point(tempX, tempY));	//ok
		//キャラ名
		//TODO 目標のStringをどこかから持ってきて代入
		tempX = objectPos_battle.get("CHARA_IMAGE").x + 120 + LINE_INTERVAL;
		tempY = objectPos_battle.get("CHARA_IMAGE").y;
		objectPos_battle.put("CHARA_NAME", new Point(tempX, tempY));	//ok
		//レベル
		tempX = objectPos_battle.get("CHARA_NAME").x;
		tempY = objectPos_battle.get("CHARA_NAME").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_battle.put("LEVEL", new Point(tempX, tempY));
		//武器アイコン
		tempX = objectPos_battle.get("LEVEL").x;
		tempY = objectPos_battle.get("LEVEL").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_battle.put("WEAPON_ICON", new Point(tempX, tempY));
		//武器名
		tempX = objectPos_battle.get("WEAPON_ICON").x + (CHAR_SIZE * 3);
		tempY = objectPos_battle.get("WEAPON_ICON").y;
		objectPos_battle.put("WEAPON", new Point(tempX, tempY));
		//HP
		tempX = objectPos_battle.get("CHARA_IMAGE").x;
		tempY = objectPos_battle.get("CHARA_IMAGE").y + 120 + LINE_INTERVAL;
		objectPos_battle.put("HP", new Point(tempX, tempY));	//ok
		//HPバー
		tempX = objectPos_battle.get("HP").x + CHAR_SIZE + (LINE_INTERVAL * 2) + 4;
		tempY = objectPos_battle.get("HP").y + 9;
		objectPos_battle.put("HP_BAR", new Point(tempX, tempY));	//ok
		//HP値
		tempX = objectPos_battle.get("HP_BAR").x + HPBAR_WIDTH + (LINE_INTERVAL*2);
		tempY = objectPos_battle.get("HP").y;
		objectPos_battle.put("HP_VALUE", new Point(tempX, tempY));	//ok
		//攻撃力
		tempX = objectPos_battle.get("HP").x + 40;
		tempY = objectPos_battle.get("HP_VALUE").y + (CHAR_SIZE*2) + LINE_INTERVAL;
		objectPos_battle.put("ATTACK", new Point(tempX, tempY));	//ok
		//攻撃力値
		tempX = objectPos_battle.get("ATTACK").x + (CHAR_SIZE*3);
		tempY = objectPos_battle.get("ATTACK").y;
		objectPos_battle.put("ATTACK_VALUE", new Point(tempX, tempY));	//ok
		//×2
		tempX = objectPos_battle.get("ATTACK_VALUE").x + (CHAR_SIZE*2);
		tempY = objectPos_battle.get("ATTACK_VALUE").y;
		objectPos_battle.put("×2", new Point(tempX, tempY));	//ok
		//命中
		tempX = objectPos_battle.get("ATTACK").x;
		tempY = objectPos_battle.get("ATTACK").y  + CHAR_SIZE + (LINE_INTERVAL*2);
		objectPos_battle.put("HIT", new Point(tempX, tempY));	//ok
		//必殺
		tempX = objectPos_battle.get("HIT").x;
		tempY = objectPos_battle.get("HIT").y +  CHAR_SIZE + (LINE_INTERVAL*2);
		objectPos_battle.put("KILLER", new Point(tempX, tempY));	//ok

		//敵キャラ
		//tempX,tempYは位置計算用の一時格納器
		//キャラ画像
		tempX = START_WIDTH + WIDTH - SIWOffsetX;
		tempY = START_HEIGHT + HEIGHT - SIWOffsetY;
		objectPos_battle.put("ENEMY_IMAGE", new Point(tempX, tempY));	//ok
		//キャラ名
		//TODO 目標のStringをどこかから持ってきて代入
		tempX = objectPos_battle.get("ENEMY_IMAGE").x - 120 - LINE_INTERVAL;
		tempY = objectPos_battle.get("ENEMY_IMAGE").y;
		objectPos_battle.put("ENEMY_NAME", new Point(tempX, tempY));	//ok
		//HP
		tempX = objectPos_battle.get("ENEMY_NAME").x;
		tempY = objectPos_battle.get("ENEMY_NAME").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_battle.put("ENEMY_HP", new Point(tempX, tempY));	//ok
		//HPバー
		tempX = objectPos_battle.get("ENEMY_HP").x - CHAR_SIZE - (LINE_INTERVAL * 2) - 4;
		tempY = objectPos_battle.get("ENEMY_HP").y + 9;
		objectPos_battle.put("ENEMY_HP_BAR", new Point(tempX, tempY));	//ok
		//HP値
		tempX = objectPos_battle.get("ENEMY_HP").x - 50;
		tempY = objectPos_battle.get("ENEMY_HP").y + CHAR_SIZE + 2;
		objectPos_battle.put("ENEMY_HP_VALUE", new Point(tempX, tempY));	//ok
		//レベル
		tempX = objectPos_battle.get("ENEMY_IMAGE").x;
		tempY = objectPos_battle.get("ENEMY_IMAGE").y + 120 + LINE_INTERVAL;
		objectPos_battle.put("ENEMY_LEVEL", new Point(tempX, tempY));
		//武器アイコン
		tempX = objectPos_battle.get("LEVEL").x;
		tempY = objectPos_battle.get("LEVEL").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos_battle.put("ENEMY_WEAPON_ICON", new Point(tempX, tempY));
		//武器名
		tempX = objectPos_battle.get("ENEMY_WEAPON_ICON").x - (CHAR_SIZE * 3);
		tempY = objectPos_battle.get("ENEMY_WEAPON_ICON").y;
		objectPos_battle.put("ENEMY_WEAPON", new Point(tempX, tempY));
		//攻撃力
		tempX = objectPos_battle.get("ENEMY_WEAPON_ICON").x - 40;
		tempY = objectPos_battle.get("ENEMY_WEAPON_ICON").y + CHAR_SIZE + (LINE_INTERVAL*2);
		objectPos_battle.put("ENEMY_ATTACK", new Point(tempX, tempY));	//ok
		//攻撃力値
		tempX = objectPos_battle.get("ENEMY_ATTACK").x - (CHAR_SIZE+5);
		tempY = objectPos_battle.get("ENEMY_ATTACK").y + CHAR_SIZE + (LINE_INTERVAL*2);
		objectPos_battle.put("ENEMY_ATTACK_VALUE", new Point(tempX, tempY));	//ok
//		//×2
//		tempX = objectPos_battle.get("ENEMY_ATTACK").x - CHAR_SIZE;
//		tempY = objectPos_battle.get("ENEMY_ATTACK_VALUE").y + CHAR_SIZE + (LINE_INTERVAL);
//		objectPos_battle.put("×2", new Point(tempX, tempY));	//ok
//		//命中
//		tempX = START_WIDTH + (WIDTH/2);
//		tempY = objectPos_battle.get("ATTACK").y;
//		objectPos_battle.put("HIT", new Point(tempX, tempY));	//ok
////		//必殺
//		tempX = objectPos_battle.get("HIT").x;
//		tempY = objectPos_battle.get("HIT").y +  CHAR_SIZE + LINE_INTERVAL;
//		objectPos_battle.put("KILLER", new Point(tempX, tempY));	//ok


	}

	public void setSubInfoWindowForFieldInfo() {
		viewMode = FIELD_TERRAIN_INFO;
	}

	public void setSubInfoWindowForAttackInfo(ExpectBattleInfo expectBattleInfo) {
		viewMode = ATTACK_INFO;
		this.expectBattleInfo = expectBattleInfo;
	}


}
