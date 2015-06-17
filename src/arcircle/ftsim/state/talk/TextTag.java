package arcircle.ftsim.state.talk;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/** トークステートで使用される
 *  各会話ごとにタグ付けして保存する
 * @author ゆきねこ */
public class TextTag {
	//フィールド---------------------------------------------------------------------------------------------------
	/**効果音があるフォルダのパス */
	private final String seFolderPath = "./Stories/SE/";
	/**効果音のファイルパス*/
	private String seFilePath;
	/**SPEAKなどのテキストタグの種類*/
	private String tagName;			//
	/** 左に描画するキャラの番号(フォルダの名前) */
	private String leftCharaName;
	/** 左のキャラを明るくするかどうか True(1):画像のまま, False(0):透過させる*/
	private boolean leftBright;
	/** 右に描画するキャラの番号(フォルダの名前) */
	private String rightCharaName;	//
	/** 右のキャラを明るくするかどうかTrue(1):画像のまま, False(0):透過させる */
	private boolean rightBright;	//
	/** true = 左が話し手, false = 右が話し手 */
	private boolean witchSpeaker;
	/**キャラの表情
	 * 0 : 普通の表情 faceStandard，
	 * 1 : 笑った表情 faceLaugh，
	 * 2 : 怒った表情 faceAngry，
	 * 3 : 苦しむ表情 faceSuffer*/
	private int expression;
	/** 会話文本体 */
	private char[] text;
	//private String[] choice;
	/** 効果音を順に格納しておくためのリスト */
	private ArrayList<Sound> seList = new ArrayList<Sound>();
	/** 次に出す効果音がseListのどの位置かを指すポインタ */
	private int sePointer = 0;

	//アクセッタ------------------------------------------------------------------------------------------------------
	public String getTagName() { return tagName; }
	public String getLeftCharaName() { return leftCharaName; }
	public void setLeftCharaName(String leftCharaName) { this.leftCharaName = leftCharaName; }
	public boolean isLeftBright() { return leftBright; }
	public String getRightCharaName() { return rightCharaName; }
	public void setRightCharaName(String rightCharaName) { this.rightCharaName = rightCharaName; }
	public boolean isRightBright() { return rightBright; }
	public char[] getText() { return text; }
	public void setText(char[] text) { this.text = text.clone(); }
	public boolean isWitchSpeaker() { return witchSpeaker; }
	public int getExpression() { return expression; }
	public void setExpression(int expression) { this.expression = expression; }
	public void setBright(String bright) { brightEvaluation(bright); }
	
	//public String getChoice(int i) { return choice[i]; }
	//public void setChoice(int i, String choice) { this.choice[i] = choice; }
	public Sound getSE(){ return seList.get(sePointer);}
	public Sound getNextSE(){
		System.out.println("pointer" + sePointer);
		return seList.get(sePointer++);
	}
	public void setSE(String seNum){
		try {
			seList.add(new Sound(seFolderPath + seNum + ".ogg"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public String getFilePath() { return seFilePath; }
	public void setFilePath(String filePath) { this.seFilePath = filePath; }

	//---------------------------------------------------------------------------------------------------------------
	/** テキストタグ作成のみに用いるコンストラクタ
	 * @param tagName SPEAKなどのテキストタグの種類 */
	public TextTag(String tagName){
		this.tagName = tagName;
		this.leftCharaName = "未設定";
		this.leftBright = false;
		this.rightCharaName = "未設定";
		this.rightBright = false;
		this.expression = 0;
		sePointer = 0;
	}
	
	//---------------------------------------------------------------------------------------------------------------
	/**SPEAK用(普通の会話文)コンストラクタ
	 * @param tagName SPEAKなどのテキストタグの種類
	 * @param leftCharaName 左に描画するキャラの番号(フォルダの名前)
	 * @param rightCharaName 右に描画するキャラの番号(フォルダの名前)
	 * @param bright
	 * @param express
	 * @param str */
	public TextTag(String tagName, String leftCharaName, String rightCharaName, String bright, int express, char[] str){
		this.tagName = tagName;
		this.leftCharaName = leftCharaName;
		this.rightCharaName = rightCharaName;
		brightEvaluation(bright);
		this.expression = express;
		this.text = str.clone();
		sePointer = 0;
	}

	//---------------------------------------------------------------------------------------------------------------
	/** SPEAK用(普通の会話文)コンストラクタ2(テキストはあとでセットする)
	 * @param tagName SPEAKなどのテキストタグの種類
	 * @param leftCharaName 左に描画するキャラの番号(フォルダの名前)
	 * @param rightCharaName 右に描画するキャラの番号(フォルダの名前)
	 * @param bright
	 * @param express */
	public TextTag(String tagName, String leftCharaName, String rightCharaName, String bright, int express){
		this.tagName = tagName;
		this.leftCharaName = leftCharaName;
		this.rightCharaName = rightCharaName;
		brightEvaluation(bright);
		this.expression = express;
		sePointer = 0;
	}

	//---------------------------------------------------------------------------------------------------------------
	/**CHANGE用コンストラクタ, tagName_BGM:CHANGEBGM, BackGround:CHANGEBACKGROUND
	 * @param tagName SPEAKなどのテキストタグの種類
	 * @param filePath */
	public TextTag(String tagName, String filePath){
		this.tagName = tagName;
		//CHANGEBGMタグでは何も表示しない
		this.leftCharaName = "@";
		this.rightCharaName = "@";
		brightEvaluation("NR");
		this.expression = 0;
		sePointer = 0;
		//テキスト文の初期化して空白だけ入れとく
		this.text = new char[100];
		for(int i =0 ; i < text.length ; i++){
			text[i] = ' ';
		}
		this.seFilePath = filePath;
	}

	//--------------------------------------------------------------------------------------------------------------
	/**SELECTSWITCH(選択肢)用コンストラクタ
	 * @param tagName SPEAKなどのテキストタグの種類
	 * @param str
	 * @param choice */
	public TextTag(String tagName, char[] str, String[] choice){
		this.tagName = tagName;
		this.leftCharaName = "temp";
		this.leftBright = false;
		this.rightCharaName = "temp";
		this.rightBright = false;
		this.text = str.clone();
	}

	//---------------------------------------------------------------------------------------------------------------
	/** キャラの明暗の評価メソッド */
	private void brightEvaluation(String bright){
		if(bright.equals("L")){			//左が話し手, 左明るい, 右暗い
			this.leftBright = true;
			this.rightBright = false;
			this.witchSpeaker = true;
		}else if(bright.equals("R")){	//右が話し手, 左暗い, 右明るい
			this.leftBright = false;
			this.rightBright = true;
			this.witchSpeaker = false;
		}else if(bright.equals("NL")){	//左が話し手, 左右暗い
			this.leftBright = false;
			this.rightBright = false;
			this.witchSpeaker = true;
		}else if(bright.equals("NR")){	//右が話し手, 左右暗い
			this.leftBright = false;
			this.rightBright = false;
			this.witchSpeaker = false;
		}else if(bright.equals("WL")){	//左が話し手, 左右明るい
			this.leftBright = true;
			this.rightBright = true;
			this.witchSpeaker = true;
		}else if(bright.equals("WR")){	//右が話し手, 左右明るい
			this.leftBright = true;
			this.rightBright = true;
			this.witchSpeaker = false;
		}else{
			System.out.println("error_TextTag__bright");
			this.leftBright = true;
			this.rightBright = true;
			this.witchSpeaker = true;
		}
	}
}
