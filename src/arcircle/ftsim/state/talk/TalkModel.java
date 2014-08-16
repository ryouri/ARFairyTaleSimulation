package arcircle.ftsim.state.talk;

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

	// メッセージを格納する配列
    private String allText = "";
    private String sendMessage;
    private char[] curPageText = new char[MAX_CHARS_PER_PAGE];

    // 次のページがあるか？
    private boolean nextFlag;
    // ウィンドウを隠せるか？（最後まで表示したらtrueになる）
    private boolean hideFlag;

    //

    //セーブデータロード用
    private int chapterID;
    private String chapterName;
    private int subStoryID;
    private String subStoryName;
    private String charaName1,charaName2;

    private int textPointer = 0;

    //アクセッタ/////////////////////////////////////////////////////////////////////////////////////////////////
  	public int getChapterID() { return chapterID; }
  	public void setChapterID(int chapterID) { this.chapterID = chapterID; }

  	public int getSubStoryID() { return subStoryID; }
  	public void setSubStoryID(int subStoryID) { this.subStoryID = subStoryID; }

  	public char[] getcurPageText() { return curPageText; }

	public boolean isNextFlag() { return nextFlag; }
	public void setNextFlag(boolean nextFlag) { this.nextFlag = nextFlag; }

	public boolean isHideFlag() { return hideFlag; }
	public void setHideFlag(boolean hideFlag) { this.hideFlag = hideFlag; }

	public String getChapterName() { return chapterName; }
	public void setChapterName(String chapterName) { this.chapterName = chapterName; }

    //コンストラクタ//////////////////////////////////////////////////////////////////////////////////////
	public TalkModel(TalkState talkState) {
		super();
		this.talkState = talkState;
		textPointer = 0;
		receiveData("little_red_ridding-hood", "おおかみ", "いづな", "ななこ");
		//loadTextData();
	}

	@Override
	//キーインプット------------------------------------------------------------------------------------
	public void keyInput(KeyInput keyInput) {
		if(keyInput.isKeyDown(Input.KEY_Z)) {

		}else{}
	}


    //セーブデータを受け取るメソッド
    public void receiveData(String chapterName, String subStoryName, String charaName1, String charaName2 ){
    	this.chapterName = chapterName;
    	if(chapterName == "little_red_ridding-hood"){
    		chapterID = 1;
    	}
    	this.subStoryName = subStoryName;
    	if(subStoryName == "おおかみ"){
    		chapterID = 3;
    	}
    	this.charaName1 = charaName1;
    	this.charaName2 = charaName2;
    }
    /*
	//テキストデータを一度ロードするメソッド-----------------------------------------------------------------
    private void loadTextData(){
    	try {
    		// 会話ファイルを読み込む
    		BufferedReader br = new BufferedReader(new InputStreamReader(
    			getClass().getClassLoader().getResourceAsStream("./Stories/Story/kaiwa.txt"), "Shift_JIS"));  // ファイルを開く
    		String line;


            nextFlag = false;
            hideFlag = false;


            int p = 0;  // 処理中の文字位置

            while ((line = br.readLine()) != null) {
            	// 空行を読み飛ばす
        		if (line.equals("")){
        			continue;
        		}else if (line.startsWith("#")){
        			continue;
        		}

            	for (int i = 0; i < line.length(); i++) {
            		String Temp =
            		allText =
            	}
            	allText[p] = '/';  // 改行記号
            }
    		br.close();  // ファイルを閉じる

    	} catch (FileNotFoundException ex) {
        ex.printStackTrace();
    	} catch (IOException ex) {
        ex.printStackTrace();
    	}
    }
    */
	/*
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
    }*/
}
