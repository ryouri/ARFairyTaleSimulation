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
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.state.TalkState;

public class TalkView implements Renderer{

	//フィールド////////////////////////////////////////////////////////////////////////////////////////
	private TalkModel talkModel;
	private TalkState talkState;

	private Image bgImg;	//背景画像
	private Image leftCharaImg;	//左側(メイン)の立ち絵
	//private Image lastLeftCharaImg;
	private Image rightCharaImg;		//右側(サブ)の立ち絵
	//private Image lastRightCharaImg;
	private Image msgBoxImg;	//メッセージウィンドウの画像
	private Image playerImg;	//主人公の画像
	private Image[] playerFaceImg = new Image[4];
	private Image faceImg;	//話し手の顔画像

	private HashMap<String, Image> charasImg;	//
	private HashMap<String, String> charasName;
	private Image nothingCharaImg;
	private int charaPosX, charaLPosY, charaRPosY;	//描画するキャラの位置
	private int msgBoxPosX, msgBoxPosY;	//メッセージウィンドウの位置
	private int nameTextPosX, nameTextPosY, textBoxPosX, textBoxPosY;

    private static final int MAX_CHARS_PER_LINE = 32;	// 1行の最大文字数
    private static final int MAX_LINES_PER_PAGE = 5;	// 1ページに表示できる最大行数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;	// 1ページに表示できる最大文字数
    //private static final int MAX_LINES = 256;	// 格納できる最大行数

    private static final String characterPath = "./Stories/Characters";	//Characterフォルダのパス

    private int playerGender = FTSimulationGame.save.getPlayer().gender;	//male = 0, female = 1
    private String playerName = FTSimulationGame.save.getPlayer().name;
    private String speakerName;

    // Fontに合わせて変えること
    private static final int FONT_WIDTH = 24;
    private static final int FONT_HEIGHT = 24;

    private Color filterColor = new Color(0.75f,0.75f,0.75f,0.5f);	//しゃべってないキャラ用の画像薄フィルタ

	//コンストラクタ//////////////////////////////////////////////////////////////////////////////////////
	public TalkView(TalkModel tModel, TalkState tState) {
		super();
		this.talkModel = tModel;
		this.talkState = tState;

		File characterFile = new File(characterPath);
		String[] charaName = characterFile.list();
		this.charasImg = new HashMap<String, Image>();
		this.charasName = new HashMap<String, String>();

		
		//デバッグ用
		for(int i = 0 ; i < charaName.length ; i++){
			System.out.println(charaName[i]);
		}

		//各画像データの読み込み
		try{
			bgImg = new Image("./Image/backGround.png");	//背景画像の読み込み
			msgBoxImg = new Image("./Image/ver1120.png");	//メッセージボックス画像の読み込み
			nothingCharaImg = new Image("./Image/Transparent.png");	//	キャラ非表示用に透明な画像を読み込み

			//主人公イメージの読み込み
			if(playerGender == Status.MALE){
				playerImg = new Image(characterPath + "/playerMale/stand.png");
				playerFaceImg[0] = (new Image(characterPath + "/playerMale/faceStandard.png")).getScaledCopy(1.5f);
				playerFaceImg[1] = (new Image(characterPath + "/playerMale/faceLaugh.png")).getScaledCopy(1.5f);
				playerFaceImg[2] = (new Image(characterPath + "/playerMale/faceAngry.png")).getScaledCopy(1.5f);
				playerFaceImg[3] = (new Image(characterPath + "/playerMale/faceSuffer.png")).getScaledCopy(1.5f);
			}else if(playerGender == Status.FEMALE){
				playerImg = new Image(characterPath + "/playerFemale/stand.png");
				playerFaceImg[0] = (new Image(characterPath + "/playerFemale/faceStandard.png")).getScaledCopy(1.5f);
				playerFaceImg[1] = (new Image(characterPath + "/playerFemale/faceLaugh.png")).getScaledCopy(1.5f);
				playerFaceImg[2] = (new Image(characterPath + "/playerFemale/faceAngry.png")).getScaledCopy(1.5f);
				playerFaceImg[3] = (new Image(characterPath + "/playerFemale/faceSuffer.png")).getScaledCopy(1.5f);
			}else{
				System.out.println("error_TalkView__playerGender_incorrect");
				playerImg = new Image(characterPath + "/playerMale/stand.png");
				playerFaceImg[0] = (new Image(characterPath + "/playerMale/faceStandard.png")).getScaledCopy(1.5f);
				playerFaceImg[1] = (new Image(characterPath + "/playerMale/faceLaugh.png")).getScaledCopy(1.5f);
				playerFaceImg[2] = (new Image(characterPath + "/playerMale/faceAngry.png")).getScaledCopy(1.5f);
				playerFaceImg[3] = (new Image(characterPath + "/playerMale/faceSuffer.png")).getScaledCopy(1.5f);
			}

			//全キャラを読み込む
			for(int i = 1 ; i < 9 ; i++){
				String Num = "";
				if(i > 0 && i < 10 ){
					Num = "00" + i;
				}else if (i >= 10 && i < 100){
					Num = "0"+ i;
				}else if (i >= 100 && i < 1000){
					Num = "" + i;
				}else{
					System.out.println("キャラ数が多すぎます.");
				}
				charasImg.put(Num + "Stand", new Image(characterPath + "/" + Num + "/stand.png"));
				charasImg.put(Num + "FaceStandard", (new Image(characterPath + "/" + Num + "/faceStandard.png").getScaledCopy(1.5f)));
				charasImg.put(Num + "FaceLaugh", (new Image(characterPath + "/" + Num + "/faceLaugh.png")).getScaledCopy(1.5f));
				charasImg.put(Num + "FaceAngryW", (new Image(characterPath + "/" + Num + "/faceAngry.png")).getScaledCopy(1.5f));
				charasImg.put(Num + "FaceSuffer", (new Image(characterPath + "/" + Num + "/faceSuffer.png")).getScaledCopy(1.5f));
				
				//各キャラの名前をparamater.txtから読み込み
				File file = new File(characterPath + "/" + Num + "/parameter.txt");
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
	        			charasName.put(Num + "Name", strs[0]);
	        		}else{
	        			System.out.println("error_TalkView__paramater.txt読み込み");
	        		}
	        		break;
	    		}
	    		
	    		br.close();  // ファイルを閉じる
			}
		}catch(SlickException e){
			e.printStackTrace();
		}catch (FileNotFoundException ex) {
	        ex.printStackTrace();
	    }catch (IOException ex) {
	        ex.printStackTrace();
	    }
		
		rightCharaImg = nothingCharaImg;
		leftCharaImg = nothingCharaImg;
		faceImg = charasImg.get("01FaceStandard");
		
		//メッセージボックスを描画する位置
		msgBoxPosX = (FTSimulationGame.WIDTH / 2) - (msgBoxImg.getWidth() / 2);
		msgBoxPosY = FTSimulationGame.HEIGHT - msgBoxImg.getHeight();
		//メッセージボックス内で話し手の名前を描画する位置
		nameTextPosX = msgBoxPosX + 30;
		nameTextPosY = msgBoxPosY + 30;
		//メッセージボックス内で会話文を描画する位置
		textBoxPosX = msgBoxPosX + 235;
		textBoxPosY = msgBoxPosY + 45;
		//キャラクターの立ち絵を表示する位置
		charaPosX = FTSimulationGame.WIDTH - rightCharaImg.getWidth();
		charaLPosY = msgBoxPosY - (leftCharaImg.getHeight() * 3 / 4);	//キャラ立ち絵は体の半分がメッセージボックス上に出る
		charaRPosY = msgBoxPosY - (rightCharaImg.getHeight() * 3 / 4);
	}

	@Override
	//レンダー---------------------------------------------------------------------------------------
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(bgImg, 0, 0);	//背景画像の描画

		TextTag curTag = talkModel.getCurTag();	//現在のタグを取得

		//キャラ替え
		if(curTag.getTagName().equals("SPEAK")){
			//左配置のキャラを設定
			if(curTag.getLeftCharaName().equals("@")){	//キャラなしの場合
				leftCharaImg = nothingCharaImg;
			}else if(curTag.getLeftCharaName().equals("*")){	//主人公の場合
				leftCharaImg = playerImg.getFlippedCopy(true, false);
			}else{
				leftCharaImg = charasImg.get(curTag.getLeftCharaName() + "Stand").getFlippedCopy(true, false);
			}

			//右配置のキャラを設定
			if(curTag.getRightCharaName().equals("@")){	//キャラなしの場合
				rightCharaImg = nothingCharaImg;
			}else if(curTag.getRightCharaName().equals("*")){	//主人公の場合
				rightCharaImg = playerImg;
			}else{
				rightCharaImg = charasImg.get(curTag.getRightCharaName() + "Stand");
			}
			
			charaPosX = FTSimulationGame.WIDTH - rightCharaImg.getWidth();
			charaLPosY = msgBoxPosY - (leftCharaImg.getHeight() * 3 / 4);	//キャラ立ち絵は体の半分がメッセージボックス上に出る
			charaRPosY = msgBoxPosY - (rightCharaImg.getHeight() * 3 / 4);
			
			//顔のキャラを設定
			if(curTag.isWitchSpeaker()){	//左にいるキャラが話し手
				if(curTag.getLeftCharaName().equals("@")){	//キャラなしの場合
					speakerName = "";
					faceImg = nothingCharaImg;
				}else if(curTag.getLeftCharaName().equals("*")){	//主人公の場合
					speakerName = playerName;
					switch(curTag.getExpression()){
						case 0:
							faceImg = playerFaceImg[0];
							break;
						case 1:
							faceImg = playerFaceImg[1];
							break;
						case 2:
							faceImg = playerFaceImg[2];
							break;
						case 3:
							faceImg = playerFaceImg[3];
							break;
						default:
							System.out.println("error_TalkView__curTagExpression");
							faceImg = playerFaceImg[0];
							break;
					}
				}else{	//その他のキャラの場合
					speakerName = charasName.get(curTag.getLeftCharaName() + "Name");
					switch(curTag.getExpression()){
						case 0:
							faceImg = charasImg.get(curTag.getLeftCharaName() + "FaceStandard");
							break;
						case 1:
							faceImg = charasImg.get(curTag.getLeftCharaName() + "FaceLaugh");
							break;
						case 2:	
							faceImg = charasImg.get(curTag.getLeftCharaName() + "FaceAngryW");
							break;
						case 3:
							faceImg = charasImg.get(curTag.getLeftCharaName() + "FaceSuffer");
							break;
						default:
							System.out.println("error_TalkView__curTagExpression");
							faceImg = charasImg.get(curTag.getLeftCharaName() + "FaceStandard");
							break;
					}
				}
			}else{	//右にいるキャラが話し手
				
				
				
				if(curTag.getRightCharaName().equals("@")){	//キャラなしの場合
					speakerName = "";
					faceImg = nothingCharaImg;
				}else if(curTag.getRightCharaName().equals("*")){	//主人公の場合
					speakerName = playerName;
					switch(curTag.getExpression()){
						case 0:
							faceImg = playerFaceImg[0];
							break;
						case 1:
							faceImg = playerFaceImg[1];
							break;
						case 2:
							faceImg = playerFaceImg[2];
							break;
						case 3:
							faceImg = playerFaceImg[3];
							break;
						default:
							System.out.println("error_TalkView__curTagExpression");
							faceImg = playerFaceImg[0];
							break;
					}
				}else{	//その他のキャラの場合
					speakerName = charasName.get(curTag.getRightCharaName() + "Name");
					switch(curTag.getExpression()){
						case 0:
							faceImg = charasImg.get(curTag.getRightCharaName() + "FaceStandard");
							break;
						case 1:
							faceImg = charasImg.get(curTag.getRightCharaName() + "FaceLaugh");
							break;
						case 2:	
							faceImg = charasImg.get(curTag.getRightCharaName() + "FaceAngryW");
							break;
						case 3:
							faceImg = charasImg.get(curTag.getRightCharaName() + "FaceSuffer");
							break;
						default:
							System.out.println("error_TalkView__curTagExpression");
							faceImg = charasImg.get(curTag.getRightCharaName() + "FaceStandard");
							break;
					}
				}
			}
			
		}else{	
			
		}
		
		//左キャラの描画
		if(curTag.isLeftBright()){
			g.drawImage(leftCharaImg, 0, charaLPosY);	//左に書くキャラの描画
		}else{
			g.drawImage(leftCharaImg, 0, charaLPosY, filterColor);	//左に書くキャラを半透明で描画
		}
		
		//右キャラの描画
		if(curTag.isRightBright()){
			g.drawImage(rightCharaImg, charaPosX, charaRPosY);	//右に書くキャラの描画
		}else{
			g.drawImage(rightCharaImg, charaPosX, charaRPosY, filterColor);	//右に書くキャラを半透明で描画
		}
		
		g.drawImage(msgBoxImg, msgBoxPosX, msgBoxPosY);	//メッセージボックスの描画
		g.drawImage(faceImg, nameTextPosX+20, nameTextPosY+40);	//顔画像の描画

		g.setColor(Color.white); // メッセージボックスに描く文字の色は白
		g.setFont(talkState.getFont()); // フォントを設定
		
		//スピーカーネームの描画
		g.drawString(speakerName, nameTextPosX, nameTextPosY);
				
		char[] curPosText = talkModel.getcurText();
		// 現在表示しているページのcurPosまで表示
		// curPosはDrawingTimerTaskで増えていくので流れて表示されるように見える
		for (int i = 0; i < talkModel.getCurPosOfPage(); i++) {
			//char c = curPosText[talkModel.getCurPage() * MAX_CHARS_PER_PAGE + i];
			char c = curPosText[i];
            if (c == '/' || c == '%' || c == '\u0000'){
            	continue;  // コントロール文字は表示しない
            }
            int dx = textBoxPosX + FONT_WIDTH * (i % MAX_CHARS_PER_LINE);
            int dy = textBoxPosY + FONT_HEIGHT * (i / MAX_CHARS_PER_LINE);

            g.drawString(c + "", dx, dy);
        }

        //最後のページでない場合は▼を表示する
        if (talkModel.isNextPageFlag()) {
            int dx = textBoxPosX + ((MAX_CHARS_PER_LINE - 2) * FONT_WIDTH);
            int dy = textBoxPosY + (MAX_LINES_PER_PAGE * FONT_HEIGHT);
            g.drawString("次へ", dx, dy);
        }
	}
}
