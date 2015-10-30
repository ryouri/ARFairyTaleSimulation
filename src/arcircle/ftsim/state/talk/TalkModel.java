package arcircle.ftsim.state.talk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.save.NowStage;
import arcircle.ftsim.state.KeyInputState;

public class TalkModel implements KeyListner {
	//TODO CHANGEBACKGROUND, SELECT タグを使えるようにする
	//TODO TalkViewはタグのテキストとテキスト中の位置を示すポインタで描画するように改変

	//フィールド-----------------------------------------------------------------------------------------------------
	/** 1行の最大文字数 */
	private final int maxCharsPerLine = T_Const.MAX_CHARS_PER_LINE;
    /** メッセージウィンドウ1ページに表示できる最大文字数*/
    private final int maxCharsPerPage = T_Const.MAX_CHARS_PER_PAGE;
    /** テキストタグのテキストに保存できる最大文字数 */
    private final int maxChars = T_Const.MAX_CHARS;
    /** BGM切り替え用のフォルダパス */
    private final String BGMFolderPath = "./Stories/BGM";
    /** 背景切り替え用のフォルダパス */
    private final String BGFolderPath = "./Stories/BackGroun/";

    /** メッセージを格納する配列 */
    private char[] curText = new char[maxCharsPerPage];
    /** 現在のタグのテキスト */
    protected char[] curTagText = new char[maxChars];

    /**トークステート(相互) */
	protected KeyInputState keyInputState;
    /** テキストタグを格納した配列 */
    protected TextTag[] talkTagArray = new TextTag[256];
    /** 次のページに進めるか？ */
    protected boolean nextPageFlag = false;
    /** 次の会話(タグ)に進めるか？ */
    protected boolean nextTalkFlag = false;
    /** ウィンドウを隠せるか？（最後まで表示したらtrueになる）*/
    protected boolean nextStateFlag = false;

    /** プレイヤー(主人公)の名前 */
    private String playerName = "";
    /** 現在のストーリーステージの名前 */
    protected String nowStoryName = "";
    /** 現在のストーリー章の名前 */
    protected String nowSubStoryName = "";
    /** プロローグかエピローグかの判断 */
    private int nowLogue = 0;

    /** 現在のページ内での文字表示位置 */
    private int curPosOfPage = 0;
    /** 現在の表示ページ */
    private int curPage = 0;
	/** 現在の表示タグ */
	protected int curTagPointer = 0;

	/** タグ生成時に用いるポインタ */
	private int makingTagP = 0;
    /** テキストを流すTimerTask */
    protected Timer timer;
    /** タイマータスク */
    protected TimerTask task;

    //アクセッタ------------------------------------------------------------------------------------------------------
  	public TextTag getCurTag() { return talkTagArray[curTagPointer]; }
  	public void incrementCurTagPointer(){ curTagPointer++; }
  	public char[] getcurText() {
  		for(int i = 0 ; i < curPosOfPage ; i++){
  			curText[i] = curTagText[curPage * maxCharsPerPage + i];
  		}
  		return curText;
  	}
  	//話し手の名前を右か左かを調べて返す
  	public String getCurTagSpeakerName(){
  		if(talkTagArray[curTagPointer].isLeftBright()){
  			return talkTagArray[curTagPointer].getLeftCharaName();
  		}else{
  			return talkTagArray[curTagPointer].getRightCharaName();
  		}
  	}
  	public boolean isNextPageFlag() { return nextPageFlag; }
	public void setNextPageFlag(boolean nextPageFlag) { this.nextPageFlag = nextPageFlag; }
	public boolean isNextTalkFlag() { return nextTalkFlag; }
	public void setNextTalkFlag(boolean nextTalkFlag) { this.nextTalkFlag = nextTalkFlag; }
	public int getCurPosOfPage() { return curPosOfPage; }
	public int getCurPage() { return curPage; }

    //-------------------------------------------------------------------------------------------------------------
	/**コンストラクタ
	 * @param talkState トークステート(相互) */
	public TalkModel() {
		super();	//おまじない
	}

	public void init(KeyInputState talkState) {
		this.keyInputState = talkState;
		//セーブデータを読み込み
		readSaveData();
		// 各フラッグの初期化
		nextPageFlag = false;
		nextTalkFlag = false;
		nextStateFlag = false;
		//会話文テキスト(plorogue/epilogue)を読み込んで各タグを生成する
		generateTagData();
		//現在のタグ番号
		curTagPointer = 0;
		//現在のタグの会話文を取得する
		curTagText = talkTagArray[curTagPointer].getText();
		//タイマーをセット(指定した時間毎にActionEventを発行する)
		timer = new Timer();
		//timerで指定した時間毎に行うタスク(具体的にはDrawingMessageTaskに記述)
		task = new DrawingMessageTask();
		//最後の引数を変化させるとメッセージの流れるスピードが変わる
        timer.schedule(task, 0L, 20L);
	}

	//-------------------------------------------------------------------------------------------------------------
	/** セーブデータにある内容を取得する，[playerName, storyName, subStoryName, selectLogue] */
	protected void readSaveData(){
		//セーブデータからプレーヤー(主人公)の名前を取得
		readPlayerName();
		//セーブデータから今のストーリーステージの名前を取得
		nowStoryName = FTSimulationGame.save.getNowStage().storyName;
		//セーブデータから今のストーリー章の名前を取得
		nowSubStoryName = FTSimulationGame.save.getNowStage().subStoryNum;
		//セーブデータから
		nowLogue = FTSimulationGame.save.getNowStage().selectLogue;
	}

	//--------------------------------------------------------------------------------------------------------------
	/**セーブデータにあるプレーヤーネームから後ろの全角スペースを抜いて
	 * フィールド変数playerNameに保存 */
	private void readPlayerName(){
		//セーブデータからプレーヤーの名前を取得
		String savePlayerName = FTSimulationGame.save.getPlayer().name;

		//プレーヤーの名前は8文字で固定されていて，余っているところには全角スペースが挿入されている
		//このままだと流れるようにメッセージを出力する場合に意味のない空白が出力されてしまう．
		//したがって，あらかじめプレーヤーネームから無意味な全角スペースを排除する
		int count = 0;		//後ろから全角スペースいくつがあるのかカウント
		for(int i = (savePlayerName.length() - 1) ; i > -1 ; i--){
			if(savePlayerName.charAt(i) == '　'){
				count++;
			}else{
				break;
			}
		}
		//プレーヤーネームを後ろの全角スペースの数を抜いた分だけplayerNameにコピー
		for(int i = 0 ; i < (savePlayerName.length()-count) ; i++){
			playerName += savePlayerName.charAt(i);
		}
	}

	protected void generateTagData() {
    	//会話文のあるストーリー章のフォルダパス
    	String logueFilePath = "";
		//プロローグかエピローグかの判定
    	if(nowLogue == NowStage.PROLOGUE){
    		/* プロローグの場合 */
    		logueFilePath = "Stories/" + nowStoryName + "/" + nowSubStoryName + "/prologue.txt";
    	}else if(nowLogue == NowStage.EPILOGUE){
    		/* エピローグの場合 */
    		logueFilePath = "Stories/" + nowStoryName + "/" + nowSubStoryName + "/epilogue.txt";
    	}else{
    		System.out.println("ERROR_TalkModel_Logue");
    	}
//    	logueFilePath = "Stories/" + nowStoryName + "/" + nowSubStoryName + "/kaiwa.txt";
    	loadTextData(logueFilePath);
	}

	//------------------------------------------------------------------------------------------------------------
    /** 会話用テキストデータをロードして，テキストタグを生成していくメソッド */
    protected void loadTextData(String logueFilePath){
    	try {
    		// バッファリーダーの作成
    		BufferedReader br;
    		// 会話ファイルを読み込む
    		File file = new File(logueFilePath);
    		br = new BufferedReader(new FileReader(file));
    		//データを1行ずつ読み込んでいくための格納器
    		String line;

            // 処理中のタグテキスト文字位置
            int charPointer = 0;
            // テキストタグに入れるテキスト
            char[] tagText = new char[maxChars];

//    		String tagName = "";
//    		String leftCharaName = "";
//    		String rightCharaName = "";
//    		String bright = "";
//    		int express = -1;
//    		String[] choice = new String[4];	//選択肢(4つまで)

            while ((line = br.readLine()) != null) {
            	// 空行を読み飛ばす
        		if (line.equals("")){ continue; }
        		//コメントを読み飛ばす
        		if (line.startsWith("#")){ continue; }

        		//読み込んだ一行を'_'で区切る
        		String[] strs = line.split("_");

        		if(strs[0].equals(T_Const.SPEAK)){
        			/* SPEAKタグの生成条件を検知 */
        			// テキストタグを作成
        			talkTagArray[makingTagP] = new TextTag(T_Const.SPEAK);
        			// 左キャラの名前を作成したタグに設定
        			talkTagArray[makingTagP].setLeftCharaName(strs[1]);
        			// 右キャラの名前を作成したタグに設定
        			talkTagArray[makingTagP].setRightCharaName(strs[2]);
        			// 左右のキャラの明るさを作成したタグに設定
        			talkTagArray[makingTagP].setBright(strs[3]);
        			// 話し手の表情を作成したタグに設定
        			talkTagArray[makingTagP].setExpression(Integer.valueOf(strs[4]));
        		}else if(strs[0].equals("SPEAKEND")){
        			/* SPEAKタグの終了条件を検知 */
        			// タグに収める前にテキストの終端に終端記号を付与する
        			tagText[++charPointer] = '$';
        			// 処理し終わったテキストをテキストタグにセットする
        			// (一つのテキストタグを作り終えたのでmaingTagPを1増やす)
        			talkTagArray[makingTagP++].setText(tagText);
        			// 処理中の文字位置を初期化
        			charPointer = 0;
        			// タグテキストを初期化
        			tagText = new char[maxChars];

        		}else if(strs[0].equals(T_Const.CHANGE_BGM)){
        			/* BGM切り替え用タグ生成条件を検知 */
        			//生成
        			talkTagArray[makingTagP++] = new TextTag(strs[0], BGMFolderPath + "/" + strs[1]);
        			System.out.println(strs[1]);
        		}
//        		else if(strs[0].equals("CHANGEBACKGROUND")){
//        			/* 背景変更用タグの生成 */
//        			talkTagArray[makingTagP++] = new TextTag(strs[0], BGFolderPath + "/" + strs[1]);
//        			System.out.println(strs[1]);
//        		}else if(strs[0].equals("SELECT")){
//        			tagName = strs[0];
//        			//choiceの初期化
//        			for(int i = 0 ; i < 4 ; i++){
//        				choice[i] = "";
//        			}
//        		}else if(strs[0].equals("CHOICE1")){
//        			choice[0] = strs[1];
//        		}else if(strs[0].equals("CHOICE2")){d
//        			choice[1] = strs[1];
//        		}else if(strs[0].equals("CHOICE3")){
//        			choice[2] = strs[1];
//        		}else if(strs[0].equals("CHOICE4")){
//        			choice[3] = strs[1];
//        		}else if(strs[0].equals("SELECTEND")){
//        			tagText[charPointer++] = '$';	//テキストの終端記号
//        			//テキストタグの作成
//        			talkTagArray[makingTagP++] = new TextTag(tagName, tagText, choice);
//        			tagName = "";
//        			leftCharaName = "";
//            		rightCharaName = "";
//            		bright = "";
//            		express = -1;
//        			charPointer = 0;
//        			tagText = new char[maxChars];
//        		}
        		else{
        			/* タグテキストの予備処理 */
        			//lineを一文字づつchar型に変換して処理する
        			for (int i = 0; i < line.length(); i++) {
        				char c = line.charAt(i);
        				if (c == '/') {  // 改行
        					/* 改行文字'/'を検知した場合 */
        					// DrawingMessageTaskで文字表示時間処理に用いるため'/'は残す[時間的な処理用]
        					tagText[charPointer] = '/';
        					// 表示位置を調節するため，次からの文字は改行したところに入れる[グラフィック的な処理用]
        					charPointer = (charPointer / maxCharsPerLine + 1) * maxCharsPerLine;
        				} else if (c == '%') {
        					/* 改ページ文字'%'を検知した場合 */
        					// DrawingMessageTaskで文字表示時間処理に用いるため'%'は残す[時間的な処理用]
        					tagText[charPointer] = '%';
        					// 表示位置を調節するため，次からの文字は改行したところに入れる[グラフィック的な処理用]
        					charPointer = (charPointer / maxCharsPerPage + 1) * maxCharsPerPage;
        				} else if(c == '*'){
        					/* 主人公の名前を示す文字'*'を検知した場合 */
        					//playerNameをchar型に変換してタグテキストに入れる
        					char[] player = playerName.toCharArray();
        					for(int j = 0 ; j < player.length ; j++){
        						tagText[charPointer++] = player[j];
        					}
        				}else if(c == '['){
        					/* 効果音再生条件'['を検知した場合 */
        					// 効果音は会話文内に"[効果音ファイル名]"と書くことで再生指定したファイルの音楽が再生される
        					// 効果音ファイル名をString型としてseFileNameに格納する
        					String seFileName = "";
        					c = line.charAt(++i);
        					while(c != ']'){
        						seFileName += c;
        						c = line.charAt(++i);
        					}
        					// 効果音ファイル名をテキストタグにセット
        					talkTagArray[makingTagP].setSE(seFileName);
        					// 効果音を鳴らすタイミングを知らせるための効果音文字'&'を挿入する
        					tagText[charPointer++] = '&';
        				}else{
        					/* cが処理の必要な特殊文字でない場合 */
        					tagText[charPointer++] = c;
        				}
        			}
        		}
            }
    		br.close();  // ファイルを閉じる
    	} catch (FileNotFoundException ex) {
        ex.printStackTrace();
    	} catch (IOException ex) {
        ex.printStackTrace();
    	}
    }

    //-------------------------------------------------------------------------------------------------------------
    /** timerで設定した時間毎に行われるタスク */
    public class DrawingMessageTask extends TimerTask {
        public void run() {
            if (!nextPageFlag && !nextTalkFlag) {
                curPosOfPage++;  // 1文字増やす
                // テキスト全体から見た現在位置
                int p = curPage * maxCharsPerPage + curPosOfPage;
                if (curTagText[p] == '/') {
                    curPosOfPage += maxCharsPerLine;
                    curPosOfPage = (curPosOfPage / maxCharsPerLine) * maxCharsPerLine;
                } else if (curTagText[p] == '%') {
                    curPosOfPage += maxCharsPerPage;
                    curPosOfPage = (curPosOfPage / maxCharsPerPage) * maxCharsPerPage;
                } else if (curTagText[p] == '$') {
                	nextTalkFlag = true;
                }

                // 1ページの文字数に達したら▼を表示
                if (curPosOfPage % maxCharsPerPage == 0) {
                    nextPageFlag = true;
                }
            }
        }
    }

	@Override
	//キーインプット------------------------------------------------------------------------------------
	public void keyInput(KeyInput keyInput) {
		//デバッグ用キー
		if(keyInput.isKeyDown(Input.KEY_D)){
			keyInputState.nextState();
		}
		if(keyInput.isKeyDown(Input.KEY_Z)){
			//次のステートへ
			if(nextTalkFlag && nextStateFlag == true){
				keyInputState.nextState();
			//ページ送り処理
			}else if(nextPageFlag && !nextTalkFlag){
				nextPage();
			//次の会話へ()
			}else if(nextTalkFlag){
				nextTalk();
			}else{
				//nextPage();
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------
	/** ページ送りメソッド */
	protected void nextPage(){
		curPage++;
		curPosOfPage = 0;
		nextPageFlag = false;
	}

	// -----------------------------------------------------------------------------------------------------------
	/** nextPageFlag = true となって次のトークに進む */
	public void nextTalk(){
		curTagPointer++;
		curTagText = talkTagArray[curTagPointer].getText();
		curPosOfPage = 0;
		curPage = 0;
		nextTalkFlag = false;
		if(curTagPointer == (makingTagP-1)){
			nextStateFlag = true;
		}
	}
}
