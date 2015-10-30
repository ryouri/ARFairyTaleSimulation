package arcircle.ftsim.state.talk;


import java.awt.Point;
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



/**トークビュー 会話ステートの描画を担当する
 * @author ゆきねこ */
public class TalkView implements Renderer{
	//フィールド-----------------------------------------------------------------------------------------------------
	/** 1行の最大文字数 */
	private final int maxCharsPerLine = T_Const.MAX_CHARS_PER_LINE;
    /** 1ページに表示できる最大行数 */
    private final int maxLinesPerPage = T_Const.MAX_LINES_PER_PAGE;
    /** 1文字のフォント幅(目安) */
    private static final int FONT_WIDTH = 24;
    /** 1文字のフォント高さ(目安) */
    private static final int FONT_HEIGHT = 24;
    // 各オブジェクトを示す名前(ハッシュのキー)
    /** "BackGround" 背景 */
    private final String BG = "BackGround";
    /** "MessageBox" メッセージボックス */
    private final String MB = "MessageBox";
    /** "LeftChara" 左に配置されるキャラ */
    private final String LC = "LeftChara";
    /** "RightChara" 右に配置されるキャラ */
    private final String RC = "RightChara";
    /** "SpeakerFace" 話し手の顔画像 */
    private final String SF = "SpeakerFace";
    /** "SpeakerName" 話し手の名前 */
    private final String SN = "SpeakerName";
    /** "TextBox" テキスト */
    private final String TB = "TextBox";

    /** プレイヤーの性別 male = 0, female = 1 */
    private final int playerGender = FTSimulationGame.save.getPlayer().gender;

	/**トークモデル */
	private TalkModel talkModel;
	/** トークステート*/
	private TalkState talkState;
	/** 各オブジェクトの画像 - BG, MSB, LC, RC, SF */
	private HashMap<String, Image> objectImg = new HashMap<String, Image>();
	/** 各オブジェクトの描画位置 - BG, MSB, LC, RC, SN, SF, TB */
	private HashMap<String, Point> objectPos = new HashMap<String, Point>();

	/** 話し手の名前 */
	private String speakerName;

    /** しゃべってないキャラを透過させるための画像フィルタ */
    private Color filterColor = new Color(0.75f,0.75f,0.75f,0.5f);
	/** 現在の効果音 */
	private Sound playingSE;

	/** 全キャライメージ, key:キャラフォルダネーム, from TalkState */
	private HashMap<String, Image> allCharasImg;
	/** 全キャラネーム，key:キャラフォルダネーム from TalkState*/
	private HashMap<String, String> allCharasName;

	//コンストラクタ--------------------------------------------------------------------------------------------------
	public TalkView(TalkModel tModel, TalkState tState) {
		super();
		this.talkModel = tModel;
		this.talkState = tState;
		//各画像の準備
		setImage();
		//各オブジェクトの描画位置を設定する
		setObjectPos();
	}

	//----------------------------------------------------------------------------------------------------------------
	/**トークステートにおける各オブジェクトの描画位置を設定する */
	private void setObjectPos(){
		int tempPosX;
		int tempPosY;
		//メッセージボックスを描画する位置
		tempPosX = (FTSimulationGame.WIDTH / 2) - (objectImg.get(MB).getWidth() / 2);
		tempPosY = FTSimulationGame.HEIGHT - objectImg.get(MB).getHeight();
		objectPos.put(MB, new Point(tempPosX, tempPosY));
		//メッセージボックス内で話し手の名前を描画する位置
		tempPosX = objectPos.get(MB).x + 30;
		tempPosY = objectPos.get(MB).y + 30;
		objectPos.put(SN, new Point(tempPosX, tempPosY));
		//メッセージボックス内で会話文を描画する位置
		tempPosX = objectPos.get(MB).x + 235;
		tempPosY = objectPos.get(MB).y + 45;
		objectPos.put(TB, new Point(tempPosX, tempPosY));
		//顔画像の描画位置
		tempPosX = objectPos.get(SN).x + 20;
		tempPosY = objectPos.get(SN).y + 40;
		objectPos.put(SF, new Point(tempPosX, tempPosY));
		//左に配置するキャラクターの立ち絵を表示する位置
		tempPosX = 0;
		tempPosY = objectPos.get(MB).y - (objectImg.get(LC).getHeight() * 3 / 4);
		objectPos.put(LC, new Point(tempPosX, tempPosY));
		//右に配置するキャラクターの立ち絵を表示する位置
		tempPosX = FTSimulationGame.WIDTH - objectImg.get(RC).getWidth();
		tempPosY = objectPos.get(MB).y - (objectImg.get(RC).getHeight() * 3 / 4);
		objectPos.put(RC, new Point(tempPosX, tempPosY));
	}

	//----------------------------------------------------------------------------------------------------------------
	/** 画像の読み込み，効果音の読み込み，各オブジェクトへのセット */
	private void setImage(){
		//全キャラの画像参照を取得
		allCharasImg = talkState.getTalkGraphics().getAllCharaImageMap();
		allCharasName = talkState.getTalkGraphics().getAllCharaNameMap();
		//各画像データの読み込み
		try{
			// 背景画像の読み込み
			objectImg.put(BG, new Image("./Image/backGround.png"));
			// メッセージボックス画像の読み込み
			objectImg.put(MB, new Image("./Image/ver1120.png"));
			//効果音の読み込み
			playingSE = new Sound("./Stories/SE/decision3.ogg");
		}catch(SlickException e){
			e.printStackTrace();
		}
		// 左キャラの初期画像:透明
		objectImg.put(LC, allCharasImg.get(T_Const.NO_STAND));
		// 右キャラの初期画像:透明
		objectImg.put(RC, allCharasImg.get(T_Const.NO_STAND));
		// 話し手の初期顔画像設置
		objectImg.put(SF, allCharasImg.get(T_Const.NO_FACE));
	}

	@Override
	//レンダー---------------------------------------------------------------------------------------
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		//背景画像の描画
		g.drawImage(objectImg.get(BG), 0, 0);
		//現在のタグを取得,(update/drawで用いるため，先に取得)
		TextTag curTag = talkModel.getCurTag();
		//各キャラの配置などのデータを更新
		update(curTag);
		//updateした情報で画面に描画
		draw(g, curTag);
	}

	//----------------------------------------------------------------------------------------------------------------
	/** render内で呼ばれ, キャラやメッセージの描画を行う */
	private void draw(Graphics g, TextTag curTag){
		// 左右のキャラの描画
		drawChara(g, curTag);
		// メッセージボックスの描画
		g.drawImage(objectImg.get(MB), objectPos.get(MB).x, objectPos.get(MB).y);
		// 顔画像の描画
		g.drawImage(objectImg.get(SF), objectPos.get(SF).x, objectPos.get(SF).y);

		// メッセージボックスに描く文字の色は白
		g.setColor(Color.white);
		// フォントを設定
		g.setFont(talkState.getFont());

		//スピーカーネームの描画
		g.drawString(speakerName, objectPos.get(SN).x, objectPos.get(SN).y);

		////////////////////////////////////////////////////////////////////////////ここからは未リファクタリング
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

			int dx = objectPos.get(TB).x + FONT_WIDTH * (i % maxCharsPerLine);
			int dy = objectPos.get(TB).y + FONT_HEIGHT * (i / maxCharsPerLine);

			g.drawString(c + "", dx, dy);
		}

		//最後のページでない場合は▼を表示する
		if (talkModel.isNextTalkFlag() || talkModel.isNextPageFlag()) {
			int dx = objectPos.get(MB).x + ((maxCharsPerLine+4) * FONT_WIDTH);
			int dy = objectPos.get(MB).y + ((maxLinesPerPage+2) * FONT_HEIGHT);
			g.drawString("次へ", dx, dy);
		}
	}

	//----------------------------------------------------------------------------------------------------------------
	/**drawで呼ばれ, 左右のキャラを描画する*/
	private void drawChara(Graphics g, TextTag curTag){
		//左キャラの描画
		if(curTag.isLeftBright()){
			//左に書くキャラを普通に描画
			g.drawImage(objectImg.get(LC), objectPos.get(LC).x, objectPos.get(LC).y);
		}else{
			//左に書くキャラを半透明で描画
			g.drawImage(objectImg.get(LC), objectPos.get(LC).x, objectPos.get(LC).y, filterColor);
		}

		//右キャラの描画
		if(curTag.isRightBright()){
			//右に書くキャラを普通に描画
			g.drawImage(objectImg.get(RC), objectPos.get(RC).x, objectPos.get(RC).y);
		}else{
			//右に書くキャラを半透明で描画
			g.drawImage(objectImg.get(RC), objectPos.get(RC).x, objectPos.get(RC).y, filterColor);
		}
	}

	//--------------------------------------------------------------------------------------------------------------
	/** render内で呼ばれ, 各配置に描くImageなどを更新する */
	private void update(TextTag curTag){
		//キャラ替え
		if(curTag.getTagName().equals(T_Const.SPEAK)){
			//現在のタグの種類が"SPEAK"の場合
			//キャラの立ち絵画像をアップデート
			updateChara(curTag);
			//キャラの顔画像をアップデート
			updateFace(curTag);
			//立ち絵の位置を調整
			updateObjectPos();

		}else if(curTag.getTagName().equals(T_Const.CHANGE_BGM)){
			/* 現在のタグの種類が"CHANGEBGM"の場合 */
			//トークステートで管理しているBGMをタグに格納されているファイルパスの音楽に変更する
			talkState.changeBGM(curTag.getFilePath());
			// タグを次に進める
			talkModel.nextTalk();
		}
//		else if(curTag.getTagName().equals("CHANGEBACKGROUND")){
//			/* 現在のタグの種類が"CHANGEBACKGROUND"の場合 */
//			//背景をタグに格納されているファイルパスの画像に変更する
//			try {
//				objectImg.put(BG, new Image(curTag.getFilePath()));
//			} catch (SlickException e) {
//				e.printStackTrace();
//			}
//			// タグを次に進める
//			talkModel.nextTalk();
//		}
	}
	//---------------------------------------------------------------------------------------------------------------
	/**updateから呼ばれ，各オブジェクト(変更される可能性のあるものだけ)の位置を更新する*/
	private void updateObjectPos(){
		//左配置の立ち絵の位置調整
		objectPos.get(LC).x = 100;
		objectPos.get(LC).y = objectPos.get(MB).y - (objectImg.get(LC).getHeight() * 3 / 4);
		//右配置の立ち絵の位置調整
		objectPos.get(RC).x = FTSimulationGame.WIDTH - objectImg.get(RC).getWidth() - 100;
		objectPos.get(RC).y = objectPos.get(MB).y - (objectImg.get(RC).getHeight() * 3 / 4);
		//テキストボックスの位置調整
		if(objectImg.get(SF).equals(allCharasImg.get(T_Const.NO_STAND))){
			/* 顔画像をあえて表示しない場合(顔画像がない場合はその時用の画像あり) */
			//メッセージボックス全体を使ってテキストを表示
			objectPos.get(TB).x = objectPos.get(MB).x + 100;
		}else{
			/* 顔画像を表示する場合 */
			//顔画像の分をあけてテキストを表示
			objectPos.get(TB).x = objectPos.get(MB).x + 235;
		}
	}


	//---------------------------------------------------------------------------------------------------------------
	/**updateから呼ばれ, 左右に配置するキャライメージを更新*/
	private void updateChara(TextTag curTag){
		//左配置のキャラを設定
		if(curTag.getLeftCharaName().equals("@")){
			/* 左キャラがキャラなしの場合 */
			//左配置のオブジェクトを透明画像にする
			objectImg.put(LC, allCharasImg.get(T_Const.NO_STAND));
		}else if(curTag.getLeftCharaName().equals("*")){
			/* 左キャラが主人公の場合 */
			if(playerGender == Status.MALE){
				/* 主人公が男の場合*/
				// 左配置のキャラを男主人公画像にする(左配置の立ち絵は反転させる)
				objectImg.put(LC, allCharasImg.get(T_Const.PLAYER_M + T_Const.STAND));
			}else if(playerGender == Status.FEMALE){
				/* 主人公が女の場合*/
				// 左配置のキャラを女主人公画像にする(左配置の立ち絵は反転させる)
				objectImg.put(LC, allCharasImg.get(T_Const.PLAYER_F + T_Const.STAND));
			}else{
				//エラー
				System.out.println("ERROR_TalkView_playerGender");
			}
		}else{
			/* 左キャラがIDで管理されるキャラの場合 */
			// 左配置のキャラを指定された立ち絵画像にする(左配置の立ち絵は反転させる)
			objectImg.put(LC, allCharasImg.get(curTag.getLeftCharaName() + T_Const.STAND));
		}

		//右配置のキャラを設定
		if(curTag.getRightCharaName().equals("@")){
			/* 右キャラがキャラなしの場合 */
			// 右配置のオブジェクトを透明画像にする
			objectImg.put(RC, allCharasImg.get(T_Const.NO_STAND));
		}else if(curTag.getRightCharaName().equals("*")){
			/* 右キャラが主人公の場合 */
			if(playerGender == Status.MALE){
				/* 主人公が男の場合*/
				// 右配置のキャラを男主人公画像にする
				objectImg.put(RC, allCharasImg.get(T_Const.PLAYER_M + T_Const.STAND).getFlippedCopy(true, false));
			}else if(playerGender == Status.FEMALE){
				/* 主人公が女の場合*/
				objectImg.put(RC, allCharasImg.get(T_Const.PLAYER_F + T_Const.STAND).getFlippedCopy(true, false));
			}else{
				//エラー
				System.out.println("ERROR_TalkView_playerGender");
			}
		}else{
			/* 右キャラがIDで管理されるキャラの場合 */
			// 右配置のキャラを指定された立ち絵画像にする
			objectImg.put(RC, allCharasImg.get(curTag.getRightCharaName() + T_Const.STAND).getFlippedCopy(true, false));
		}
	}

	//---------------------------------------------------------------------------------------------------------------
	/** updateから呼ばれ, 話し手の顔画像と名前の更新 */
	private void updateFace(TextTag curTag){
		//顔のキャラを設定
		if(curTag.isWitchSpeaker()){
			/* 左にいるキャラが話し手 */
			if(curTag.getLeftCharaName().equals("@")){
				/* 左キャラがキャラなしの場合 */
				//spealerNameの初期化
				speakerName = "";
				objectImg.put(SF, allCharasImg.get(T_Const.NO_STAND));
			}else if(curTag.getLeftCharaName().equals("*")){
				/* 左キャラが主人公の場合 */
				if(playerGender == Status.MALE){
					/* 主人公が男の場合 */
					updateExpression(curTag.getExpression(), T_Const.PLAYER_M);
				}else if(playerGender == Status.FEMALE){
					/* 主人公が女の場合 */
					updateExpression(curTag.getExpression(), T_Const.PLAYER_F);
				}else{
					// エラー
					System.out.println("ERROR_TalkView_playerGender");
				}
			}else{
				/* 左キャラがIDで管理されるキャラの場合 */
				updateExpression(curTag.getExpression(), curTag.getLeftCharaName());
			}
		}else{
			/* 右にいるキャラが話し手 */
			if(curTag.getRightCharaName().equals("@")){
				/* 右キャラがキャラなしの場合 */
				//spealerNameの初期化
				speakerName = "";
				objectImg.put(SF, allCharasImg.get(T_Const.NO_STAND));
			}else if(curTag.getRightCharaName().equals("*")){
				/* 右キャラが主人公の場合 */
				if(playerGender == Status.MALE){
					/* 主人公が男の場合 */
					updateExpression(curTag.getExpression(), T_Const.PLAYER_M);
				}else if(playerGender == Status.FEMALE){
					/* 主人公が女の場合 */
					updateExpression(curTag.getExpression(), T_Const.PLAYER_F);
				}else{
					/* エラー */
					System.out.println("ERROR_TalkView_playerGender");
				}
			}else{
				/* 右キャラがIDで管理されるキャラの場合 */
				updateExpression(curTag.getExpression(), curTag.getRightCharaName());
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------------
	/** updateFaceから呼ばれ, 表情による場合分けして更新 */
	private void updateExpression(int expression, String charaName){
		// 話し手の名前をアップデート
		speakerName = allCharasName.get(charaName + "Name");
		//話し手の顔画像をアップデート
		switch(expression){
		case 0:
			/* 普通の顔の場合 */
			objectImg.put(SF, allCharasImg.get(charaName + T_Const.FACE_ST));
			break;
		case 1:
			/* 笑った顔の場合 */
			objectImg.put(SF, allCharasImg.get(charaName + T_Const.FACE_LA));
			break;
		case 2:
			/* 怒った顔の場合 */
			objectImg.put(SF, allCharasImg.get(charaName + T_Const.FACE_AN));
			break;
		case 3:
			/* 苦しんだ顔の場合 */
			objectImg.put(SF, allCharasImg.get(charaName + T_Const.FACE_SU));
			break;
		default:
			System.out.println("error_TalkView__curTagExpression");
			objectImg.put(SF, allCharasImg.get(charaName + T_Const.FACE_ST));
			break;
		}
	}
}
