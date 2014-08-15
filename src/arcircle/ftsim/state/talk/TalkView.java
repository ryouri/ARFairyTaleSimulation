package arcircle.ftsim.state.talk;


import java.util.Timer;
import java.util.TimerTask;

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
	TalkModel talkModel;
	private TalkState talkState;

	private Image bgImg;	//背景画像
	private Image leftCharaImg;	//プレイヤーの立ち絵
	private Image rightCharaImg;		//妖精や味方キャラなどの立ち絵
	private Image msgBoxImg;	//メッセージウィンドウの画像

	private int charaPosX, charaPosY;	//右に描画するキャラの位置
	private int msgBoxPosX, msgBoxPosY;	//メッセージウィンドウの位置
	private int nameTextPosX, nameTextPosY, textBoxPosX, textBoxPosY;

    private static final int MAX_CHARS_PER_LINE = 32;	// 1行の最大文字数
    private static final int MAX_LINES_PER_PAGE = 4;	// 1ページに表示できる最大行数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;	// 1ページに表示できる最大文字数
    private static final int MAX_LINES = 128;	// 格納できる最大行数


    private int curPage;

    private String charaName;
	// 現在のページで表示した文字数（最大値: MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE）
    private int curPos;
    // Fontに合わせて変えること
    // M2フォントの16.0fだとこのサイズ
    private static final int FONT_WIDTH = 24;
    private static final int FONT_HEIGHT = 24;

    private char[] wrightText;

    // テキストを流すTimerTask
    private Timer timer;
    private TimerTask task;

	//コンストラクタ//////////////////////////////////////////////////////////////////////////////////////
	public TalkView(TalkModel tModel, TalkState tState) {
		super();
		this.talkModel = tModel;
		this.talkState = tState;
		//textRect = new Rectangle(80, 352, 480, 108);
		try{
			bgImg = new Image("./Image/blueScreen.png");
			leftCharaImg = new Image("./Image/testChara1.png");
			rightCharaImg = new Image("./Image/testChara2.png");
			msgBoxImg = new Image("./Image/Ver1000.png");

		}catch(SlickException e){
			e.printStackTrace();
		}
		charaPosX = FTSimulationGame.WIDTH - rightCharaImg.getWidth();
		charaPosY = FTSimulationGame.HEIGHT - rightCharaImg.getHeight();
		msgBoxPosX = (FTSimulationGame.WIDTH / 2) - (msgBoxImg.getWidth() / 2);
		msgBoxPosY = FTSimulationGame.HEIGHT - msgBoxImg.getHeight();
		nameTextPosX = msgBoxPosX + 45;
		nameTextPosY = msgBoxPosY + 20;
		textBoxPosX = msgBoxPosX + 60;
		textBoxPosY = msgBoxPosY + 55;
	}

	@Override
	//レンダー---------------------------------------------------------------------------------------
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(bgImg, 0, 0);
		g.drawImage(leftCharaImg, 0, charaPosY);
		g.drawImage(rightCharaImg, charaPosX, charaPosY);
		g.drawImage(msgBoxImg, msgBoxPosX, msgBoxPosY);

        g.setColor(Color.white);	//メッセージボックスに描く文字の色は白
        g.setFont(talkState.getFont());	//フォントを設定

        g.drawString(talkModel.getChapterName(), msgBoxPosX + 45, msgBoxPosY + 20);

        /*
        // 現在表示しているページのcurPosまで表示
        // curPosはDrawingTimerTaskで増えていくので流れて表示されるように見える
        for (int i = 0; i < talkModel.getCurPos() ; i++) {
            char c = wrightText[talkModel.getCurPage() * MAX_CHARS_PER_PAGE + i];
            if (c == '/' || c == '%' || c == '!') continue;  // コントロール文字は表示しない
            int dx = textBoxPosX + FONT_WIDTH * (i % MAX_CHARS_PER_LINE);
            int dy = textBoxPosY + FONT_HEIGHT + FONT_HEIGHT * (i / MAX_CHARS_PER_LINE);
            g.drawString(c + "", dx, dy);
        }

        // 最後のページでない場合は▼を表示する
        if (talkModel.isNextFlag()) {
            int dx = textBoxPosX + (MAX_CHARS_PER_LINE / 2) * FONT_WIDTH - 8;
            int dy = textBoxPosY + FONT_HEIGHT + (FONT_HEIGHT * 5);
            g.drawString("▼", dx, dy);
        }*/

		g.setColor(Color.white);

		//int messageWidth = talkState.getFont().getWidth(talkModel.message);
		//
		g.drawString("こんにちは!　私の名前はかぐや姫!　よろしくね!!!", msgBoxPosX+60, msgBoxPosY + 55);
	}



	//会話文をセットする
	public void setMessage(char[] message) {
        //curPos = 0;
        //curPage = 0;
        //nextFlag = false;
        //hideFlag = false;

        // 全角スペースで初期化
        for (int i=0; i<wrightText.length; i++) {
            wrightText[i] = '　';
        }

        int p = 0;  // 処理中の文字位置
        for (int i=0; i < message.length ; i++) {
            char c = message[i];
            if (c == '/') {  // 改行
                wrightText[p] = '/';
                p += MAX_CHARS_PER_LINE;
                p = (p / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
            } else if (c == '%') {  // 改ページ
                wrightText[p] = '%';
                p += MAX_CHARS_PER_PAGE;
                p = (p / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
            } else {
                wrightText[p++] = c;
            }
        }

        wrightText[p] = '!';  // 終端記号

        task = new DrawingMessageTask();
        timer.schedule(task, 0L, 20L);
    }

    private class DrawingMessageTask extends TimerTask {
        public void run() {
            if (!talkModel.isNextFlag()) {
                curPos++;  // 1文字増やす
                // テキスト全体から見た現在位置
                int p = curPage * MAX_CHARS_PER_PAGE + curPos;
                if (wrightText[p] == '/') {
                    curPos += MAX_CHARS_PER_LINE;
                    curPos = (curPos / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
                } else if (wrightText[p] == '%') {
                    curPos += MAX_CHARS_PER_PAGE;
                    curPos = (curPos / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
                } else if (wrightText[p] == '!') {
                    talkModel.setHideFlag(true);
                }

                // 1ページの文字数に達したら▼を表示
                if (curPos % MAX_CHARS_PER_PAGE == 0) {
                    talkModel.setNextFlag(true);
                }
            }
        }
    }

}
