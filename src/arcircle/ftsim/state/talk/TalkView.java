package arcircle.ftsim.state.talk;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.state.TalkState;



public class TalkView implements Renderer{

	//フィールド////////////////////////////////////////////////////////////////////////////////////////
	private TalkModel talkModel;	//トークモデル
	private TalkState talkState;	//トークステート

	/** 各画像の格納器_背景, メッセージボックス, L.Chara, R.Chara, face */
	private Image[] ImgStorage = new Image[5];
	/** オブジェクトの描画位置,背景, メッセージボックス, L.Chara, R.Chara, face, speakerName, text */
	private int[][] position = new int[7][2];
	private final int BG = 0;	//背景
	private final int MSG = 1;	//メッセージボックス
	private final int LC = 2;	//L_Chara
	private final int RC = 3;	//R_Chara
	private final int FACE = 4;	//face
	private final int NAME = 5;	//speakerName
	private final int TEXT = 6;	//text
	private final int X = 0;
	private final int Y = 1;
	private String speakerName;	//話し手の名前
	private Sound playingSE;	//現在の効果音

	private Image nothingCharaImg;	//透明な画像

	private HashMap<String, Image> charasImg;	//キャラナンバーとキャライメージのほ
	private HashMap<String, String> charasName;

    private static final int MAX_CHARS_PER_LINE = 32;	// 1行の最大文字数
    private static final int MAX_LINES_PER_PAGE = 5;	// 1ページに表示できる最大行数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;	// 1ページに表示できる最大文字数

    private static final String characterPath = "./Stories/Characters";	//Characterフォルダのパス

    private int playerGender = FTSimulationGame.save.getPlayer().gender;	//male = 0, female = 1

    // Fontに合わせて変えること
    private static final int FONT_WIDTH = 24;
    private static final int FONT_HEIGHT = 24;

    private Color filterColor = new Color(0.75f,0.75f,0.75f,0.5f);	//しゃべってないキャラ用の画像薄フィルタ

	//コンストラクタ//////////////////////////////////////////////////////////////////////////////////////
	public TalkView(TalkModel tModel, TalkState tState) {
		super();
		this.talkModel = tModel;
		this.talkState = tState;

		//全キャラの画像を読み込み
		loadChara();

		ImgStorage[LC] = nothingCharaImg;
		ImgStorage[RC] = nothingCharaImg;
		ImgStorage[FACE] = charasImg.get("001FaceStandard");

		//メッセージボックスを描画する位置
		position[MSG][X] = (FTSimulationGame.WIDTH / 2) - (ImgStorage[MSG].getWidth() / 2);
		position[MSG][Y] = FTSimulationGame.HEIGHT - ImgStorage[MSG].getHeight();
		//メッセージボックス内で話し手の名前を描画する位置
		position[NAME][X] = position[MSG][X] + 30;
		position[NAME][Y] = position[MSG][Y] + 30;
		//メッセージボックス内で会話文を描画する位置
		position[TEXT][X] = position[MSG][X] + 235;
		position[TEXT][Y] = position[MSG][Y] + 45;
		//顔画像の描画位置
		position[FACE][X] = position[NAME][X] + 20;
		position[FACE][Y] = position[NAME][Y] + 40;
		//キャラクターの立ち絵を表示する位置
		position[RC][X] = FTSimulationGame.WIDTH - ImgStorage[RC].getWidth();
		position[LC][Y] = position[MSG][Y] - (ImgStorage[LC].getHeight() * 3 / 4);	//キャラ立ち絵は体の半分がメッセージボックス上に出る
		position[RC][Y] = position[MSG][Y] - (ImgStorage[RC].getHeight() * 3 / 4);
	}

	private void loadChara(){

		File characterFile = new File(characterPath);	//CharacterフォルダのFile
		String[] charaName = characterFile.list();
		this.charasImg = new HashMap<String, Image>();
		this.charasName = new HashMap<String, String>();

		//各画像データの読み込み
		try{
			ImgStorage[BG] = new Image("./Image/backGround.png");	//背景画像の読み込み
			ImgStorage[MSG] = new Image("./Image/ver1120.png");	//メッセージボックス画像の読み込み
			nothingCharaImg = new Image("./Image/Transparent.png");	//	キャラ非表示用に透明な画像を読み込み
			playingSE = new Sound("./Stories/SE/decision3.ogg");

			//全キャラを読み込む
			for(int i = 0 ; i < charaName.length ; i++){
				if(charaName[i].length() == 3){
					charasImg.put(charaName[i] + "Stand", new Image(characterPath + "/" + charaName[i] + "/stand.png"));
					if(new File(characterPath + "/" + charaName[i] + "/FaceStandard.png").exists()){
						charasImg.put(charaName[i] + "FaceStandard", (new Image(characterPath + "/" + charaName[i] + "/faceStandard.png").getScaledCopy(1.5f)));
						charasImg.put(charaName[i] + "FaceLaugh", (new Image(characterPath + "/" + charaName[i] + "/faceLaugh.png")).getScaledCopy(1.5f));
						charasImg.put(charaName[i] + "FaceAngry", (new Image(characterPath + "/" + charaName[i] + "/faceAngry.png")).getScaledCopy(1.5f));
						charasImg.put(charaName[i] + "FaceSuffer", (new Image(characterPath + "/" + charaName[i] + "/faceSuffer.png")).getScaledCopy(1.5f));
					}else{
						charasImg.put(charaName[i] + "FaceStandard", nothingCharaImg);
						charasImg.put(charaName[i] + "FaceLaugh", nothingCharaImg);
						charasImg.put(charaName[i] + "FaceAngry", nothingCharaImg);
						charasImg.put(charaName[i] + "FaceSuffer", nothingCharaImg);
					}
					//各キャラの名前をparamater.txtから読み込み
					File file = new File(characterPath + "/" + charaName[i] + "/parameter.txt");
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					while ((line = br.readLine()) != null) {
						// 空行を読み飛ばす
						if (line.equals("")){
							continue;
							//コメントを読み飛ばす
						}else if (line.startsWith("#")){
							continue;
						}
						String[] strs = line.split(",");
						if(!strs[0].equals("")){
							charasName.put(charaName[i] + "Name", strs[0]);
						}else{
							System.out.println("error_TalkView__paramater.txt読み込み");
						}
						break;
					}
					br.close();  // ファイルを閉じる
				}else{
					/* 主人公ImageとNameの読み込み */
					if(charaName[i].equals("playerMale") && playerGender == Status.MALE){
						charasImg.put("playerStand", new Image(characterPath + "/" + charaName[i] + "/stand.png"));
						charasImg.put("playerFaceStandard", (new Image(characterPath + "/" + charaName[i] + "/faceStandard.png").getScaledCopy(1.5f)));
						charasImg.put("playerFaceLaugh", (new Image(characterPath + "/" + charaName[i] + "/faceLaugh.png")).getScaledCopy(1.5f));
						charasImg.put("playerFaceAngry", (new Image(characterPath + "/" + charaName[i] + "/faceAngry.png")).getScaledCopy(1.5f));
						charasImg.put("playerFaceSuffer", (new Image(characterPath + "/" + charaName[i] + "/faceSuffer.png")).getScaledCopy(1.5f));
						charasName.put("playerName", FTSimulationGame.save.getPlayer().name);
					}else if(charaName[i].equals("playerFemale") && playerGender == Status.FEMALE){
						charasImg.put("playerStand", new Image(characterPath + "/" + charaName[i] + "/stand.png"));
						charasImg.put("playerFaceStandard", (new Image(characterPath + "/" + charaName[i] + "/faceStandard.png").getScaledCopy(1.5f)));
						charasImg.put("playerFaceLaugh", (new Image(characterPath + "/" + charaName[i] + "/faceLaugh.png")).getScaledCopy(1.5f));
						charasImg.put("playerFaceAngry", (new Image(characterPath + "/" + charaName[i] + "/faceAngry.png")).getScaledCopy(1.5f));
						charasImg.put("playerFaceSuffer", (new Image(characterPath + "/" + charaName[i] + "/faceSuffer.png")).getScaledCopy(1.5f));
						charasName.put("playerName", FTSimulationGame.save.getPlayer().name);
					}
				}
			}
			}catch(SlickException e){
			e.printStackTrace();
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	//レンダー---------------------------------------------------------------------------------------
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(ImgStorage[BG], 0, 0);	//背景画像の描画

		TextTag curTag = talkModel.getCurTag();	//現在のタグを取得
		update(curTag);	//各キャラの配置などのデータを更新
		draw(g, curTag);	//updateした情報で画面に描画
	}

	/** render内で呼ばれ, キャラやメッセージの描画を行う */
	private void draw(Graphics g, TextTag curTag){
		drawChara(g, curTag);	//左右のキャラの描画
		g.drawImage(ImgStorage[MSG], position[MSG][X], position[MSG][Y]);	//メッセージボックスの描画
		g.drawImage(ImgStorage[FACE], position[FACE][X], position[FACE][Y]);	//顔画像の描画

		g.setColor(Color.white); // メッセージボックスに描く文字の色は白
		g.setFont(talkState.getFont()); // フォントを設定

		//スピーカーネームの描画
		g.drawString(speakerName, position[NAME][X], position[NAME][Y]);

		char[] curPosText = talkModel.getcurText();
		// 現在表示しているページのcurPosまで表示
		// curPosはDrawingTimerTaskで増えていくので流れて表示されるように見える
		for (int i = 0; i < talkModel.getCurPosOfPage(); i++) {
			char c = curPosText[i];

			//効果音を鳴らす
			if(i == (talkModel.getCurPosOfPage()-1) && c == '&' && !playingSE.playing()){
				playingSE = curTag.getNextSE();
				playingSE.play();
			}
			if (c == '/' || c == '%' || c == '&' || c == '\u0000'){
				continue;  // コントロール文字は表示しない
			}

			int dx = position[TEXT][X] + FONT_WIDTH * (i % MAX_CHARS_PER_LINE);
			int dy = position[TEXT][Y] + FONT_HEIGHT * (i / MAX_CHARS_PER_LINE);

			g.drawString(c + "", dx, dy);
		}

		//最後のページでない場合は▼を表示する
		if (talkModel.isNextPageFlag()) {
			int dx = position[TEXT][X] + ((MAX_CHARS_PER_LINE - 2) * FONT_WIDTH);
			int dy = position[TEXT][Y] + (MAX_LINES_PER_PAGE * FONT_HEIGHT);
			g.drawString("次へ", dx, dy);
		}
	}

	/**drawで呼ばれ, 左右のキャラを描画する*/
	private void drawChara(Graphics g, TextTag curTag){
		//左キャラの描画
		if(curTag.isLeftBright()){
			g.drawImage(ImgStorage[LC], 0, position[LC][Y]);	//左に書くキャラの描画
		}else{
			g.drawImage(ImgStorage[LC], 0, position[LC][Y], filterColor);	//左に書くキャラを半透明で描画
		}

		//右キャラの描画
		if(curTag.isRightBright()){
			g.drawImage(ImgStorage[RC], position[RC][X], position[RC][Y]);	//右に書くキャラの描画
		}else{
			g.drawImage(ImgStorage[RC], position[RC][X], position[RC][Y], filterColor);	//右に書くキャラを半透明で描画
		}
	}

	/** render内で呼ばれ, 各配置に描くImageなどを更新する */
	private void update(TextTag curTag){
		//キャラ替え
		if(curTag.getTagName().equals("SPEAK")){
			//キャラの画像をアップデート
			updateChara(curTag);
			updateFace(curTag);
			position[RC][X] = FTSimulationGame.WIDTH - ImgStorage[RC].getWidth();
			position[LC][Y] = position[MSG][Y] - (ImgStorage[LC].getHeight() * 3 / 4);	//キャラ立ち絵は体の半分がメッセージボックス上に出る
			position[RC][Y] = position[MSG][Y] - (ImgStorage[RC].getHeight() * 3 / 4);
			if(ImgStorage[FACE].equals(nothingCharaImg)){
				position[TEXT][X] = position[MSG][X] + 100;
			}else{
				position[TEXT][X] = position[MSG][X] + 235;
			}
		}else if(curTag.getTagName().equals("CHANGEBGM")){
			talkState.changeBGM(curTag.getFilePath());
			talkModel.nextTalk();
		}else if(curTag.getTagName().equals("CHANGEBACKGROUND")){
			try {
				ImgStorage[BG] = new Image(curTag.getFilePath());
			} catch (SlickException e) {
				e.printStackTrace();
			}
			talkModel.nextTalk();
		}
	}

	/**updateから呼ばれ, 左右に配置するキャライメージを更新*/
	private void updateChara(TextTag curTag){
		//左配置のキャラを設定
		if(curTag.getLeftCharaName().equals("@")){	//キャラなしの場合
			ImgStorage[LC] = nothingCharaImg;
		}else if(curTag.getLeftCharaName().equals("*")){	//主人公の場合
			ImgStorage[LC] = charasImg.get("playerStand").getFlippedCopy(true, false);
		}else{
			ImgStorage[LC] = charasImg.get(curTag.getLeftCharaName() + "Stand").getFlippedCopy(true, false);
		}
		//右配置のキャラを設定
		if(curTag.getRightCharaName().equals("@")){	//キャラなしの場合
			ImgStorage[RC] = nothingCharaImg;
		}else if(curTag.getRightCharaName().equals("*")){	//主人公の場合
			ImgStorage[RC] = charasImg.get("playerStand");
		}else{
			ImgStorage[RC] = charasImg.get(curTag.getRightCharaName() + "Stand");
		}
	}

	/** updateから呼ばれ, 話し手の顔画像と名前の更新 */
	private void updateFace(TextTag curTag){
		//顔のキャラを設定
		if(curTag.isWitchSpeaker()){	//左にいるキャラが話し手
			if(curTag.getLeftCharaName().equals("@")){	//キャラなしの場合
				speakerName = "";
				ImgStorage[FACE] = nothingCharaImg;
			}else if(curTag.getLeftCharaName().equals("*")){	//主人公の場合
				updateExpression(curTag.getExpression(), "player");
			}else{	//その他のキャラの場合
				updateExpression(curTag.getExpression(), curTag.getLeftCharaName());
			}
		}else{	//右にいるキャラが話し手
			if(curTag.getRightCharaName().equals("@")){	//キャラなしの場合
				speakerName = "";
				ImgStorage[FACE] = nothingCharaImg;
			}else if(curTag.getRightCharaName().equals("*")){	//主人公の場合
				updateExpression(curTag.getExpression(), "player");
			}else{	//その他のキャラの場合
				updateExpression(curTag.getExpression(), curTag.getRightCharaName());
			}
		}
	}

	/** updateFaceから呼ばれ, 表情による場合分けして更新 */
	private void updateExpression(int expression, String charaName){
		speakerName = charasName.get(charaName + "Name");
		switch(expression){
		case 0:
			ImgStorage[FACE] = charasImg.get(charaName + "FaceStandard");
			break;
		case 1:
			ImgStorage[FACE] = charasImg.get(charaName + "FaceLaugh");
			break;
		case 2:
			ImgStorage[FACE] = charasImg.get(charaName + "FaceAngry");
			break;
		case 3:
			ImgStorage[FACE] = charasImg.get(charaName + "FaceSuffer");
			break;
		default:
			System.out.println("error_TalkView__curTagExpression");
			ImgStorage[FACE] = charasImg.get(charaName + "FaceStandard");
			break;
		}
	}
}
