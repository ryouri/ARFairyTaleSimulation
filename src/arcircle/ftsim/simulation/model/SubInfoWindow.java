package arcircle.ftsim.simulation.model;

import java.awt.Point;
import java.util.HashMap;

import org.newdawn.slick.Animation;
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

//

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

	private UnicodeFont font = FTSimulationGame.font;

	private Image backGround;
	private String gender;

	private HashMap<String, Animation> upWalkAnimeMap;
	private HashMap<String, Animation> downWalkAnimeMap;
	private HashMap<String, Animation> leftWalkAnimeMap;
	private HashMap<String, Animation> rightWalkAnimeMap;
	private HashMap<String, Point> objectPos;

	private Animation[] IMAGEAnime;

	private Field field;
	private Image selectedMapChip;

	public SubInfoWindow(Field field) {
		super();
		this.field = field;

		//キャラ画像の部分は4パターンの歩行アニメーションを表示して64×64を埋める
		IMAGEAnime = new Animation[4];
		
		objectPos = new HashMap<String, Point>();

		//キャラ画像に用いるアニメーションの読み込み
		upWalkAnimeMap = field.characters.upWalkAnimeMap;
		downWalkAnimeMap = field.characters.downWalkAnimeMap;
		leftWalkAnimeMap = field.characters.leftWalkAnimeMap;
		rightWalkAnimeMap = field.characters.rightWalkAnimeMap;

		//各オブジェクトの情報画面の位置を設定する
		setObjectPos();

		try {
			backGround = new Image("image/subInfoWindow/subInfoWindow.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(backGround, START_WIDTH, START_HEIGHT);
		g.setFont(font);	//フォントのセット(これをやらないと文字描画されない)
		
		//ステージ名の描画
		//TODO もしかしてstoryNameの取得はコンストラクタでよい？
		String storyName = FTSimulationGame.save.getNowStage().storyName;
		g.drawString("ステージ:STAGENAME",
				objectPos.get("STAGE_NAME").x, objectPos.get("STAGE_NAME").y);	//ステージ名の描画
		//勝利条件の描画
		//TODO クリア目標で表示するストリングの取得
		g.drawString("勝利条件",
				objectPos.get("WIN_TERM").x, objectPos.get("WIN_TERM").y);	//ステージ名の描画
		//勝利条件内容の描画
		g.drawString("・" + "勝利条件内容",
				objectPos.get("WIN_STRING").x, objectPos.get("WIN_STRING").y);
		//敗北条件
		g.drawString("敗北条件",
				objectPos.get("LOSE_TERM").x, objectPos.get("LOSE_TERM").y);
		//敗北条件内容
		g.drawString("・" + "敗北条件内容",
				objectPos.get("LOSE_STRING").x, objectPos.get("LOSE_STRING").y);
		//地形情報の描画
		selectedMapChip = field.getSelectedMapChip();
		g.drawImage(selectedMapChip.getScaledCopy(2), objectPos.get("MAP_INFOIMAGE").x, objectPos.get("MAP_INFOIMAGE").y);
		//地形の名前
		g.drawString("MAP_NAME",
				objectPos.get("MAP_NAME").x, objectPos.get("MAP_NAME").y);
		//地形の回避,防御値
		g.drawString("回避:" + 10 + "% 防御:" + 10 + "%",
				objectPos.get("MAP_ABOID").x, objectPos.get("MAP_ABOID").y);
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
				objectPos.get("CHARA_NAME").x, objectPos.get("CHARA_NAME").y);	//名前の描画
		//性別の描画
		g.drawString("性別 : " + gender,
				objectPos.get("CHARA_GENDER").x, objectPos.get("CHARA_GENDER").y);	//名前の描画
		//選択キャラのアニメーションの取得
		IMAGEAnime[0] = downWalkAnimeMap.get(charaStatus.name);
		IMAGEAnime[1] = upWalkAnimeMap.get(charaStatus.name);
		IMAGEAnime[2] = leftWalkAnimeMap.get(charaStatus.name);
		IMAGEAnime[3] = rightWalkAnimeMap.get(charaStatus.name);
		//アニメーションの表示
		IMAGEAnime[0].draw(objectPos.get("CHARA_IMAGE").x , objectPos.get("CHARA_IMAGE").y);
		IMAGEAnime[1].draw(objectPos.get("CHARA_IMAGE").x + CHIP_SIZE, objectPos.get("CHARA_IMAGE").y);
		IMAGEAnime[2].draw(objectPos.get("CHARA_IMAGE").x , objectPos.get("CHARA_IMAGE").y + CHIP_SIZE);
		IMAGEAnime[3].draw(objectPos.get("CHARA_IMAGE").x + CHIP_SIZE , objectPos.get("CHARA_IMAGE").y + CHIP_SIZE);

		//HP/MAXHPの表示
		g.drawString("HP : " + charaStatus.hp + " / " +  charaStatus.maxHp,
				objectPos.get("CHARA_HP").x, objectPos.get("CHARA_HP").y);
		//レベルの表示
		g.drawString("レベル : " + charaStatus.level,
				objectPos.get("CHARA_LEVEL").x, objectPos.get("CHARA_LEVEL").y);
		//ちから
		g.drawString("ちから : " + charaStatus.power,
				objectPos.get("CHARA_POWER").x, objectPos.get("CHARA_POWER").y);
		//まりょく
		g.drawString("まりょく : " + charaStatus.magicPower,
				objectPos.get("CHARA_MAGICPOWER").x, objectPos.get("CHARA_MAGICPOWER").y);
		//はやさ
		g.drawString("はやさ : " + charaStatus.speed,
				objectPos.get("CHARA_SPEED").x, objectPos.get("CHARA_SPEED").y);
		//わざ
		g.drawString("わざ : " + charaStatus.tech,
				objectPos.get("CHARA_TECH").x, objectPos.get("CHARA_TECH").y);
		//運
		g.drawString("運 : " + charaStatus.luck,
				objectPos.get("CHARA_LUCK").x, objectPos.get("CHARA_LUCK").y);
		//ぼうぎょ
		g.drawString("ぼうぎょ : " + charaStatus.defence,
				objectPos.get("CHARA_DEFENCE").x, objectPos.get("CHARA_DEFENCE").y);
		//まぼう
		g.drawString("まぼう : " + charaStatus.magicDefence,
				objectPos.get("CHARA_MAGICDEFENCE").x, objectPos.get("CHARA_MAGICDEFENCE").y);
		//いどう
		g.drawString("いどう : " + charaStatus.move,
				objectPos.get("CHARA_MOVE").x, objectPos.get("CHARA_MOVE").y);
		//次のレベルまで
		g.drawString("EXP : " + charaStatus.exp + "/100",
				objectPos.get("NEXT_EXP").x, objectPos.get("NEXT_EXP").y);
	}

	//各オブジェクトの場所を設定
		private void setObjectPos(){
			//tempX,tempYは位置計算用の一時格納器
			//ステージ名_一番左上
			int tempX = START_WIDTH + SIWOffsetX;
			int tempY = START_HEIGHT + SIWOffsetY;
			objectPos.put("STAGE_NAME", new Point(tempX, tempY));	//ok
			//勝利条件
			//TODO 目標のStringをどこかから持ってきて代入
			tempX = objectPos.get("STAGE_NAME").x;
			tempY = objectPos.get("STAGE_NAME").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("WIN_TERM", new Point(tempX, tempY));	//ok
			//勝利条件内容
			tempX = objectPos.get("WIN_TERM").x;
			tempY = objectPos.get("WIN_TERM").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("WIN_STRING", new Point(tempX, tempY));	//ok
			//敗北条件
			tempX = objectPos.get("WIN_STRING").x;
			tempY = objectPos.get("WIN_STRING").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("LOSE_TERM", new Point(tempX, tempY));	//ok
			//敗北条件内容
			tempX = objectPos.get("LOSE_TERM").x;
			tempY = objectPos.get("LOSE_TERM").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("LOSE_STRING", new Point(tempX, tempY));	//ok
			
			//地形の画像
			tempX = objectPos.get("LOSE_STRING").x;
			tempY = objectPos.get("LOSE_STRING").y + (CHAR_SIZE * 2);
			objectPos.put("MAP_INFOIMAGE", new Point(tempX, tempY));
			//地形の名前
			tempX = objectPos.get("MAP_INFOIMAGE").x + IMAGE_SIZE + LINE_INTERVAL;
			tempY = objectPos.get("MAP_INFOIMAGE").y;
			objectPos.put("MAP_NAME", new Point(tempX, tempY));
			//地形_回避_防御
			tempX = objectPos.get("MAP_NAME").x;
			tempY = objectPos.get("MAP_NAME").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("MAP_ABOID", new Point(tempX, tempY));
			//名前
			tempX = objectPos.get("MAP_INFOIMAGE").x;
			tempY = objectPos.get("MAP_INFOIMAGE").y + IMAGE_SIZE + CHAR_SIZE;
			objectPos.put("CHARA_NAME", new Point(tempX, tempY));	//ok
			//性別
			tempX = objectPos.get("CHARA_NAME").x;
			tempY = objectPos.get("CHARA_NAME").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("CHARA_GENDER", new Point(tempX, tempY));	//ok
			//キャラ画像_名前の下
			tempX = objectPos.get("CHARA_GENDER").x;
			tempY = objectPos.get("CHARA_GENDER").y + CHAR_SIZE + (LINE_INTERVAL * 2);
			objectPos.put("CHARA_IMAGE", new Point(tempX, tempY));	//ok
			//HP/MAXHP_キャラ画像の右
			tempX = objectPos.get("CHARA_IMAGE").x + IMAGE_SIZE + LINE_INTERVAL;
			tempY = objectPos.get("CHARA_IMAGE").y;
			objectPos.put("CHARA_HP", new Point(tempX, tempY));	//ok
			//レベル_HP/MAXHPの下
			tempX = objectPos.get("CHARA_HP").x;
			tempY = objectPos.get("CHARA_HP").y +  CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("CHARA_LEVEL", new Point(tempX, tempY));	//ok
			//ちから_キャラ画像の下
			tempX = objectPos.get("CHARA_IMAGE").x;
			tempY = objectPos.get("CHARA_IMAGE").y + IMAGE_SIZE + LINE_INTERVAL;
			objectPos.put("CHARA_POWER", new Point(tempX, tempY));	//ok
			//まりょく_ちからの右
			tempX = objectPos.get("CHARA_POWER").x + (WIDTH / 2);
			tempY = objectPos.get("CHARA_POWER").y;
			objectPos.put("CHARA_MAGICPOWER", new Point(tempX, tempY));
			//ぼうぎょ_ちからの下
			tempX = objectPos.get("CHARA_POWER").x;
			tempY = objectPos.get("CHARA_POWER").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("CHARA_DEFENCE", new Point(tempX, tempY));
			//まぼう_ぼうぎょの右
			tempX = objectPos.get("CHARA_DEFENCE").x + (WIDTH / 2);
			tempY = objectPos.get("CHARA_DEFENCE").y;
			objectPos.put("CHARA_MAGICDEFENCE", new Point(tempX, tempY));
			//はやさ_ぼうぎょの下
			tempX = objectPos.get("CHARA_DEFENCE").x;
			tempY = objectPos.get("CHARA_DEFENCE").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("CHARA_SPEED", new Point(tempX, tempY));	//ok
			//わざ_はやさの右
			tempX = objectPos.get("CHARA_SPEED").x + (WIDTH / 2);
			tempY = objectPos.get("CHARA_SPEED").y;
			objectPos.put("CHARA_TECH", new Point(tempX, tempY));
			//運_はやさの下
			tempX = objectPos.get("CHARA_SPEED").x;
			tempY = objectPos.get("CHARA_SPEED").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("CHARA_LUCK", new Point(tempX, tempY));
			//いどう_運の右
			tempX = objectPos.get("CHARA_LUCK").x + (WIDTH / 2);
			tempY = objectPos.get("CHARA_LUCK").y;
			objectPos.put("CHARA_MOVE", new Point(tempX, tempY));
			//次のレベルまで_運の下
			tempX = objectPos.get("CHARA_LUCK").x;
			tempY = objectPos.get("CHARA_LUCK").y + CHAR_SIZE + LINE_INTERVAL;
			objectPos.put("NEXT_EXP", new Point(tempX, tempY));
		}
}
