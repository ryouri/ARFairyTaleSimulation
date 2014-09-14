package arcircle.ftsim.state.talk;


import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.TalkState;

public class TalkView implements Renderer{

	//フィールド////////////////////////////////////////////////////////////////////////////////////////
	private TalkModel talkModel;
	private TalkState talkState;

	private Image bgImg;	//背景画像
	private Image leftCharaImg;	//プレイヤーの立ち絵
	private Image lastLeftCharaImg;
	private Image rightCharaImg;		//妖精や味方キャラなどの立ち絵
	private Image lastRightCharaImg;
	private Image msgBoxImg;	//メッセージウィンドウの画像
	private Image heroImg;

	private HashMap<String, Image> charaImg;
	private Image nothingCharaImg;
	private int charaPosX, charaLPosY, charaRPosY;	//右に描画するキャラの位置
	private int msgBoxPosX, msgBoxPosY;	//メッセージウィンドウの位置
	private int nameTextPosX, nameTextPosY, textBoxPosX, textBoxPosY;

    private static final int MAX_CHARS_PER_LINE = 32;	// 1行の最大文字数
    private static final int MAX_LINES_PER_PAGE = 5;	// 1ページに表示できる最大行数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;	// 1ページに表示できる最大文字数
    private static final int MAX_LINES = 256;	// 格納できる最大行数 
    
    private static final String characterPath = "./Stories/Characters";	//Characterフォルダのパス

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
		this.charaImg = new HashMap<String, Image>();

		//デバッグ
		for(int i = 0 ; i < charaName.length ; i++){
			System.out.println(charaName[i]);
		}
		
		try{
			bgImg = new Image("./Image/blueScreen.png");	//背景画像の読み込み
			msgBoxImg = new Image("./Image/Ver1000.png");	//メッセージボックス画像の読み込み
			nothingCharaImg = new Image("./Image/Transparent.png");	//	キャラ非表示用に透明な画像を読み込み
			heroImg = new Image(characterPath + "/HeroMale/tachie.png");
			
			//全キャラを読み込む
			for(int i = 0 ; i < charaName.length ; i++){
				charaImg.put(charaName[i], new Image(characterPath + "/" + charaName[i] + "/tachie.png"));
			}
		}catch(SlickException e){
			e.printStackTrace();
		}
		
		rightCharaImg = nothingCharaImg;
		leftCharaImg = nothingCharaImg;
		
		charaPosX = FTSimulationGame.WIDTH - rightCharaImg.getWidth();
		charaLPosY = FTSimulationGame.HEIGHT - leftCharaImg.getHeight();
		charaRPosY = FTSimulationGame.HEIGHT - rightCharaImg.getHeight();
		msgBoxPosX = (FTSimulationGame.WIDTH / 2) - (msgBoxImg.getWidth() / 2);
		msgBoxPosY = FTSimulationGame.HEIGHT - msgBoxImg.getHeight();
		nameTextPosX = msgBoxPosX + 45;
		nameTextPosY = msgBoxPosY + 20;
		textBoxPosX = msgBoxPosX + 60;
		textBoxPosY = msgBoxPosY + 45;
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
				leftCharaImg = heroImg;
			}else{
				leftCharaImg = charaImg.get(curTag.getLeftCharaName());
			}
			
			//右配置のキャラを設定
			if(curTag.getRightCharaName().equals("@")){	//キャラなしの場合
				rightCharaImg = nothingCharaImg;
			}else{
				rightCharaImg = charaImg.get(curTag.getRightCharaName());
			}
		}else{}

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

		g.setColor(Color.white); // メッセージボックスに描く文字の色は白
		g.setFont(talkState.getFont()); // フォントを設定
		g.drawString(talkModel.getCurTagSpeakerName(), nameTextPosX, nameTextPosY);	//スピーカーネームの描画

		char[] curPosText = talkModel.getcurText();
		// 現在表示しているページのcurPosまで表示
		// curPosはDrawingTimerTaskで増えていくので流れて表示されるように見える
		for (int i = 0; i < talkModel.getCurPosOfPage(); i++) {
			char c = curPosText[talkModel.getCurPage() * MAX_CHARS_PER_PAGE + i];
            if (c == '/' || c == '%' || c == '$' || c == '\u0000'){
            	continue;  // コントロール文字は表示しない
            }
            int dx = textBoxPosX + FONT_WIDTH * (i % MAX_CHARS_PER_LINE);
            int dy = textBoxPosY + FONT_HEIGHT * (i / MAX_CHARS_PER_LINE);

            g.drawString(c + "", dx, dy);
        }

        //最後のページでない場合は▼を表示する
        if (talkModel.isNextFlag()) {
            int dx = textBoxPosX + ((MAX_CHARS_PER_LINE - 2) * FONT_WIDTH);
            int dy = textBoxPosY + (MAX_LINES_PER_PAGE * FONT_HEIGHT);

            g.drawString("次へ", dx, dy);
        }

		//g.setColor(Color.white);

		//int messageWidth = talkState.getFont().getWidth(talkModel.message);
		//
		//g.drawString("こんにちは!　私の名前はかぐや姫!　よろしくね!!!", msgBoxPosX+60, msgBoxPosY + 55);
	}
}
