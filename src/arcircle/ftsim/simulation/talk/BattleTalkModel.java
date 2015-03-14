package arcircle.ftsim.simulation.talk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.simgame.SimGameModel;

public class BattleTalkModel implements KeyListner {

	//フィールド////////////////////////////////////////////////////////////////////////////////////////////
	private SimGameModel sgModel;

	private static final int MAX_CHARS_PER_LINE = 32;	// 1行の最大文字数
    private static final int MAX_LINES_PER_PAGE = 5;	// 1ページに表示できる最大行数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;	// 1ページに表示できる最大文字数
    private static final int MAX_LINES = 256;	// 格納できる最大行数

    // メッセージを格納する配列
    private char[] curText = new char[MAX_CHARS_PER_PAGE];
    private char[] curTagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];
    private BattleTextTag[] tags = new BattleTextTag[256];

    private boolean nextPageFlag = false;	//次のページに進めるか？
    private boolean nextTalkFlag = false;	//次の会話(タグ)に進めるか？

    // ウィンドウを隠せるか？（最後まで表示したらtrueになる）
    private boolean nextStateFlag = false;

    private String playerName = "";
    private String nowStoryName = "";	//
    private String nowSubStoryName = "";	//
    private int nowLogue = 0;

    HashMap<Integer,String> map = new HashMap<Integer,String>();

    private int curPosOfPage = 0;	//現在のページ内での文字表示位置
    private int curPage = 0;		//現在の表示ページ
	private int curTagPointer = 0;	//現在の表示タグ

	private int tagP = 0;	//タグ生成時に用いるポインタ

	// テキストを流すTimerTask
    private Timer timer;
    private TimerTask task;

    private boolean isEnd;

    public boolean isEnd() {
		return isEnd;
	}

	//アクセッタ/////////////////////////////////////////////////////////////////////////////////////////////////
  	public BattleTextTag getCurTag() { return tags[curTagPointer]; }
  	public void incrementCurTagPointer(){ curTagPointer++; }
  	public char[] getcurText() {
  		for(int i = 0 ; i < curPosOfPage ; i++){
  			curText[i] = curTagText[curPage * MAX_CHARS_PER_PAGE + i];
  		}
  		return curText;
  	}

  	//話し手の名前を右か左かを調べて返す
  	public String getCurTagSpeakerName(){
  		if(tags[curTagPointer].isLeftBright()){
  			return tags[curTagPointer].getLeftCharaName();
  		}else{
  			return tags[curTagPointer].getRightCharaName();
  		}
  	}
  	public boolean isNextPageFlag() { return nextPageFlag; }
	public void setNextPageFlag(boolean nextPageFlag) { this.nextPageFlag = nextPageFlag; }

	public boolean isNextTalkFlag() { return nextTalkFlag; }
	public void setNextTalkFlag(boolean nextTalkFlag) { this.nextTalkFlag = nextTalkFlag; }

	public int getCurPosOfPage() { return curPosOfPage; }
	public int getCurPage() { return curPage; }

    //コンストラクタ//////////////////////////////////////////////////////////////////////////////////////
	public BattleTalkModel(SimGameModel sgModel, String battleTalkTxtPath) {
		super();	//おまじない
		this.sgModel = sgModel;
		readSaveData();//セーブデータを読み込み
		timer = new Timer();	//タイマーをセット(メッセージが流れるように表示させるため)
		loadTextData("Stories/"
				+ nowStoryName + "/"
				+ nowSubStoryName
				+ "/" + battleTalkTxtPath);		//会話文テキストを読み込んで各タグを生成する
		curTagPointer = 0;	//現在のタグを示すポインタ
		curTagText = tags[curTagPointer].getText();	//
		task = new DrawingMessageTask();
        timer.schedule(task, 0L, 20L);	//最後の引数を変化させるとメッセージの流れるスピードが変わる
	}

	private void readSaveData(){
		readPlayerName();
		nowStoryName = FTSimulationGame.save.getNowStage().storyName;
		nowSubStoryName = FTSimulationGame.save.getNowStage().subStoryNum;
		nowLogue = FTSimulationGame.save.getNowStage().selectLogue;
	}

	//セーブデータにあるプレイヤーネームを読み込み-----------------------------------------------------------------
	private void readPlayerName(){
		String savePlayerName = FTSimulationGame.save.getPlayer().name;
		int count = 0;

		for(int i = (savePlayerName.length() - 1) ; i > -1 ; i--){
			if(savePlayerName.charAt(i) == '　'){
				count++;
			}else{
				break;
			}
		}
		for(int i = 0 ; i < (savePlayerName.length()-count) ; i++){
			playerName += savePlayerName.charAt(i);
		}
	}

	//テキストデータを一度ロードするメソッド-----------------------------------------------------------------
    private void loadTextData(String filePath){
    	try {
    		BufferedReader br;
    		// 会話ファイルを読み込む
    		File file = new File(filePath);
    		br = new BufferedReader(new FileReader(file));

    		String line;	//データを1行ずつ読み込んでいく

    		nextPageFlag = false;
    		nextTalkFlag = false;
            nextStateFlag = false;

            int p = 0;  // 処理中の文字位置

            char[] tagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];

    		String tagName = "";
    		String leftCharaName = "";
    		String rightCharaName = "";
    		String bright = "";
    		int express = -1;

    		String[] choice = new String[4];	//選択肢(4つまで)

            while ((line = br.readLine()) != null) {
            	// 空行を読み飛ばす
        		if (line.equals("")){
        			continue;
        		//コメントを読み飛ばす
        		}else if (line.startsWith("#")){
        			continue;
        		}

        		String[] strs = line.split("_");	//読み込んだ一行を'_'で区切る

        		if(strs[0].equals("SPEAK")){
        			tagName = strs[0];			//タグの名前をSPEAKにする
        			leftCharaName = strs[1];	//左に配置するキャラの名前を格納("*"などもそのまま格納)
        			rightCharaName = strs[2];	//右に配置するキャラの名前を格納("*"などもそのまま格納)
        			bright = strs[3]; 			//左右キャラの明るさ
        			express = Integer.valueOf(strs[4]);	//話し手の表情
        			//テキストタグの作成
        			tags[tagP] = new BattleTextTag(tagName, leftCharaName, rightCharaName, bright, express);

        		}else if(strs[0].equals("SPEAKEND")){
        			tagText[++p] = '$';	//テキストの終端記号

//        			//テキストタグの作成
        			tags[tagP++].setText(tagText);

        			tagName = "";
        			leftCharaName = "";
            		rightCharaName = "";
            		bright = "";
            		express = -1;
        			p = 0;
        			tagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];

        		}else if(strs[0].equals("SELECT")){
        			tagName = strs[0];
        			//choiceの初期化
        			for(int i = 0 ; i < 4 ; i++){
        				choice[i] = "";
        			}
        		}else if(strs[0].equals("CHOICE1")){
        			choice[0] = strs[1];
        		}else if(strs[0].equals("CHOICE2")){
        			choice[1] = strs[1];
        		}else if(strs[0].equals("CHOICE3")){
        			choice[2] = strs[1];
        		}else if(strs[0].equals("CHOICE4")){
        			choice[3] = strs[1];
        		}else if(strs[0].equals("SELECTEND")){
        			tagText[p++] = '$';	//テキストの終端記号
        			//テキストタグの作成
        			tags[tagP++] = new BattleTextTag(tagName, tagText, choice);
        			tagName = "";
        			leftCharaName = "";
            		rightCharaName = "";
            		bright = "";
            		express = -1;
        			p = 0;
        			tagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];

        		//BGM切り替え用タグの生成
        		}else if(strs[0].equals("CHANGEBGM")){
        			tags[tagP++] = new BattleTextTag(strs[0], "./Stories/BGM/" + strs[1]);
        			System.out.println(strs[1]);
        		}


        		else{
        			for (int i = 0; i < line.length(); i++) {
        				char c = line.charAt(i);
        				if (c == '/') {  // 改行
        					tagText[p] = '/';
        					p += MAX_CHARS_PER_LINE;
        					p = (p / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
        				} else if (c == '%') {  // 改ページ
        					tagText[p] = '%';
        					p += MAX_CHARS_PER_PAGE;
        					p = (p / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
        				} else if(c == '*'){
        					char[] player = playerName.toCharArray();
        					for(int j = 0 ; j < player.length ; j++){
        						tagText[p++] = player[j];
        					}
        				}else if(c == '['){
        					String seFileName = "";
        					c = line.charAt(++i);
        					while(c != ']'){
        						seFileName += c;
        						c = line.charAt(++i);
        					}
        					System.out.println(seFileName);
        					tags[tagP].setSE(seFileName);
        					tagText[p++] = '&';
        				}else{
        					tagText[p++] = c;
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

    private class DrawingMessageTask extends TimerTask {
        public void run() {
            if (!nextPageFlag && !nextTalkFlag) {
                curPosOfPage++;  // 1文字増やす
                // テキスト全体から見た現在位置
                int p = curPage * MAX_CHARS_PER_PAGE + curPosOfPage;
                //System.out.println(curTagText[p]);
                if (curTagText[p] == '/') {
                    curPosOfPage += MAX_CHARS_PER_LINE;
                    curPosOfPage = (curPosOfPage / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
                } else if (curTagText[p] == '%') {
                    curPosOfPage += MAX_CHARS_PER_PAGE;
                    curPosOfPage = (curPosOfPage / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
                } else if (curTagText[p] == '$') {
                	nextTalkFlag = true;
                }

                // 1ページの文字数に達したら▼を表示
                if (curPosOfPage % MAX_CHARS_PER_PAGE == 0) {
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
			sgModel.removeRendererArrayEnd();
			sgModel.removeKeyInputStackFirst();
			isEnd = true;
		}
		if(keyInput.isKeyDown(Input.KEY_Z)){
			//次のステートへ
			if(nextTalkFlag && nextStateFlag == true){
				sgModel.removeRendererArrayEnd();
				sgModel.removeKeyInputStackFirst();
				isEnd = true;
				//sgState.nextState();
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

	//ページ送りメソッド
	private void nextPage(){
		curPage++;
		curPosOfPage = 0;
		nextPageFlag = false;
	}

	//nextPageFlag = true となって次のトークに進む
	public void nextTalk(){
		curTagPointer++;
		curTagText = tags[curTagPointer].getText();
		curPosOfPage = 0;
		curPage = 0;
		nextTalkFlag = false;
		if(curTagPointer == (tagP-1)){
			nextStateFlag = true;
		}
	}

}
