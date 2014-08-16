package arcircle.ftsim.state.talk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.crypto.Data;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.TalkState;

public class TalkModel implements KeyListner {
	//フィールド////////////////////////////////////////////////////////////////////////////////////////////
	private TalkState talkState;

	private static final int MAX_CHARS_PER_LINE = 32;	// 1行の最大文字数
    private static final int MAX_LINES_PER_PAGE = 4;	// 1ページに表示できる最大行数
    private static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;	// 1ページに表示できる最大文字数
    private static final int MAX_LINES = 256;	// 格納できる最大行数
    //private static final int MAX_CHARS =
	// メッセージを格納する配列

    private char[] curText = new char[MAX_CHARS_PER_PAGE];
    private char[] curTagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];
    private TextTag[] tags = new TextTag[256];

    // 次のページがあるか？
    private boolean nextFlag = false;
    // ウィンドウを隠せるか？（最後まで表示したらtrueになる）
    private boolean nextStateFlag = false;

    //セーブデータロード用
    private int chapterID;
    private String chapterName;
    private int subStoryID;
    private String subStoryName;
    private String charaName1,charaName2;

    private int curPosOfPage = 0;
    private int curPage = 0;

	private int curTagPointer = 0;

	private int tagP = 0;

	// テキストを流すTimerTask
    private Timer timer;
    private TimerTask task;

    //アクセッタ/////////////////////////////////////////////////////////////////////////////////////////////////
  	public int getChapterID() { return chapterID; }
  	public void setChapterID(int chapterID) { this.chapterID = chapterID; }

  	public int getSubStoryID() { return subStoryID; }
  	public void setSubStoryID(int subStoryID) { this.subStoryID = subStoryID; }

  	public char[] getcurText() {
  		for(int i = 0 ; i < curPosOfPage ; i++){
  			curText[i] = curTagText[curPage * MAX_CHARS_PER_PAGE + i];
  		}
  		return curText;
  	}
  	public String getCurTagSpeakerName(){ return tags[curTagPointer].getSpeakerName(); }

	public boolean isNextFlag() { return nextFlag; }
	public void setNextFlag(boolean nextFlag) { this.nextFlag = nextFlag; }

	//public String getChapterName() { return chapterName; }
	//public void setChapterName(String chapterName) { this.chapterName = chapterName; }
	public int getCurPosOfPage() { return curPosOfPage; }
	public int getCurPage() { return curPage; }


    //コンストラクタ//////////////////////////////////////////////////////////////////////////////////////
	public TalkModel(TalkState talkState) {
		super();
		this.talkState = talkState;
		timer = new Timer();
		loadTextData();
		curTagPointer = 0;
		curTagText = tags[curTagPointer].getText();
		receiveData("little_red_ridding-hood", "おおかみ", "いづな", "ななこ");

		task = new DrawingMessageTask();
		task = new DrawingMessageTask();
        timer.schedule(task, 0L, 30L);

	}

	@Override
	//キーインプット------------------------------------------------------------------------------------
	public void keyInput(KeyInput keyInput) {
		if(keyInput.isKeyDown(Input.KEY_Z)){
			if(nextFlag && nextStateFlag == true){
				//System.out.println(curTagPointer + "," + tagP);
				talkState.nextState();
			} else if(nextFlag){
				curTagPointer++;
				curTagText = tags[curTagPointer].getText();
				curPosOfPage = 0;
				curPage = 0;
				nextFlag = false;

				if(curTagPointer == tagP){
					System.out.println(curTagPointer + "," + tagP);
					nextStateFlag = true;
				}
			}
		}
	}


    //セーブデータを受け取るメソッド
    public void receiveData(String chapterName, String subStoryName, String charaName1, String charaName2 ){
    	this.chapterName = chapterName;
    	if(chapterName.equals("little_red_ridding-hood")){
    		chapterID = 1;
    	}
    	this.subStoryName = subStoryName;
    	if(subStoryName.equals("おおかみ")){
    		chapterID = 3;
    	}
    	this.charaName1 = charaName1;
    	this.charaName2 = charaName2;
    }

	//テキストデータを一度ロードするメソッド-----------------------------------------------------------------
    private void loadTextData(){
    	try {
    		// 会話ファイルを読み込む
    		File file = new File("Stories/01_Story/01/kaiwa.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
    		String line;

    		nextFlag = false;
            nextStateFlag = false;

            int p = 0;  // 処理中の文字位置

            char[] tagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];

    		String tagName = "";
    		String speakerName = "";
    		//String boxText

            while ((line = br.readLine()) != null) {

            	// 空行を読み飛ばす
        		if (line.equals("")){
        			continue;
        		//コメントを読み飛yばす
        		}else if (line.startsWith("#")){
        			continue;
        		}

        		String[] strs = line.split("_");

        		//System.out.println("tagName : " + tagName);

        		if(strs[0].equals("SPEAK")){
        			tagName = strs[0];
        			if(strs[1].equals("*")){
        				strs[1] = "主人公";
        			}else{
        				speakerName = strs[1];
        			}
        		}else if(strs[0].equals("SPEAKEND")){
        			tagText[p++] = '$';
        			tags[tagP] = new TextTag(tagName, speakerName, tagText);
        			tagP++;
        			tagName = "";
        			speakerName = "";
        			p = 0;
        			tagText = new char[MAX_LINES * MAX_CHARS_PER_LINE];

        		}else if(strs[0].equals("SELECT_SWITCH")){
        			tagName = strs[0];
        			speakerName = strs[1];
        		}else{
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
        				} else {
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
            if (!nextFlag) {
                curPosOfPage++;  // 1文字増やす
                // テキスト全体から見た現在位置
                int p = curPage * MAX_CHARS_PER_PAGE + curPosOfPage;
                if (curTagText[p] == '/') {
                    curPosOfPage += MAX_CHARS_PER_LINE;
                    curPosOfPage = (curPosOfPage / MAX_CHARS_PER_LINE) * MAX_CHARS_PER_LINE;
                } else if (curTagText[p] == '%') {
                    curPosOfPage += MAX_CHARS_PER_PAGE;
                    curPosOfPage = (curPosOfPage / MAX_CHARS_PER_PAGE) * MAX_CHARS_PER_PAGE;
                } else if (curTagText[p] == '$') {
                	nextFlag = true;
                    //hideFlag=true;
                }

                // 1ページの文字数に達したら▼を表示
                if (curPosOfPage % MAX_CHARS_PER_PAGE == 0) {
                    nextFlag = true;
                }
            }
        }
    }

	
	//セーブデータをロードするメソッド-----------------------------------------------------------------
	//作成途中
    private Data[] loadSaveData(){

    	int countData = 0;
    	try {
    		// SaveDataファイルを読み込む
    		BufferedReader br = new BufferedReader(new InputStreamReader(
    			getClass().getClassLoader().getResourceAsStream("./Stories/SaveData.txt"), "Shift_JIS"));  // ファイルを開く
    		String line;

    		while ((line = br.readLine()) != null) {  // 1行ずつ読み込み
    			// 空行を読み飛ばす
    			if (line.equals("")){
    				continue;
    			}
    			// コメント行を読み飛ばす
    			if (line.startsWith("#")){
    				continue;
    			}

    			StringTokenizer st = new StringTokenizer(line, " ");
    			String Type1 = st.nextToken();
    			if(Type1 == "CREAR"){
    				line = br.readLine();
    				String Type2 = st.nextToken();
    				if(Type2 == "StoryName1"){

    				}
    				dataLoadTAG = CREARTAG;
    			}
    			int x = Integer.parseInt(st.nextToken());
    			int y = Integer.parseInt(st.nextToken());

    			tempName[countData] = st.nextToken();
    			tempData[countData][0] = x;
    			tempData[countData][1] = y;

    			countData++;
    		}
    		br.close();  // ファイルを閉じる
    	} catch (FileNotFoundException ex) {
        ex.printStackTrace();
    	} catch (IOException ex) {
        ex.printStackTrace();
    	}

    	//データ数Nをセット
    	N = countData;
    	Status.setN(countData);

    	Data[] data = new Data[N];
    	for (int i = 0 ; i < N ; i++){
    		data[i] = new Data(tempData[i][0], tempData[i][1] , i);
    		data[i].setClusterID(rand.nextInt(K));
    		if(tempName[i] != ""){
    			data[i].setName(tempName[i]);
    		}
    	}
    	return data;
    }
}
