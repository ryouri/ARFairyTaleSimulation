package arcircle.ftsim.simulation.talk;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class BattleTextTag {
	//フィールド//////////////////////////////////////////////////////////////////////////////////////
	private String tagName;	//SPEAKなどのテキストタグの種類
	private String leftCharaName;	//左に描画するキャラの番号
	private boolean leftBright;		//左のキャラを明るくするかどうか
	private String rightCharaName;	//右に描画するキャラの番号
	private boolean rightBright;	//右のキャラを明るくするかどうか
	private boolean witchSpeaker;	//true = 左が話し手, false = 右が話し手
	private int expression;
	private String bgmFilePath;
	/*expression =	0 : 普通の表情 faceStandard
	 * 				1 : 笑った表情 faceLaugh
	 * 				2 : 怒った表情 faceAngry
	 * 				1 : 苦しむ表情 faceSuffer
	 */
	private char[] text;	//会話文本体
	//private String[] choice;

	private ArrayList<Sound> seArray = new ArrayList<Sound>();
	private int sePointer = 0;
	private String sePath = "./Stories/SE/";

	//アクセッタ///////////////////////////////////////////////////////////////////////////////////////
	public String getTagName() { return tagName; }
	public void setTagName(String tagName) { this.tagName = tagName; }
	public String getLeftCharaName() { return leftCharaName; }
	public void setLeftCharaName(String leftCharaName) { this.leftCharaName = leftCharaName; }
	public boolean isLeftBright() { return leftBright; }
	public void setLeftBright(boolean leftBright) { this.leftBright = leftBright; }
	public String getRightCharaName() { return rightCharaName; }
	public void setRightCharaName(String rightCharaName) { this.rightCharaName = rightCharaName; }
	public boolean isRightBright() { return rightBright; }
	public void setRightBright(boolean rightBright) { this.rightBright = rightBright; }
	public char[] getText() { return text; }
	public void setText(char[] text) { this.text = text.clone(); }
	public boolean isWitchSpeaker() { return witchSpeaker; }
	public void setWitchSpeaker(boolean witchSpeaker) { this.witchSpeaker = witchSpeaker; }
	public int getExpression() { return expression; }
	public void setExpression(int expression) { this.expression = expression; }
	//public String getChoice(int i) { return choice[i]; }
	//public void setChoice(int i, String choice) { this.choice[i] = choice; }
	public Sound getSE(){ return seArray.get(sePointer);}
	public Sound getNextSE(){
		System.out.println("pointer" + sePointer);
		return seArray.get(sePointer++);
	}
	public void setSE(String seNum){
		try {
			seArray.add(new Sound(sePath + seNum + ".ogg"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public String getBgmFilePath() {
		return bgmFilePath;
	}
	public void setBgmFilePath(String bgmFilePath) {
		this.bgmFilePath = bgmFilePath;
	}

	//SPEAK用コンストラクタ/////////////////////////////////////////////////////////////////////////////////
	public BattleTextTag(String tagName, String leftCharaName, String rightCharaName, String bright, int express, char[] str){
		this.tagName = tagName;
		this.leftCharaName = leftCharaName;
		this.rightCharaName = rightCharaName;
		brightEvaluation(bright);
		this.expression = express;
		this.text = str.clone();
		sePointer = 0;
	}

	//SPEAK用コンストラクタ2(テキストはあとでセットする)/////////////////////////////////////////////////////////////////////////////////
	public BattleTextTag(String tagName, String leftCharaName, String rightCharaName, String bright, int express){
		this.tagName = tagName;
		this.leftCharaName = leftCharaName;
		this.rightCharaName = rightCharaName;
		brightEvaluation(bright);
		this.expression = express;
		sePointer = 0;
	}
	//CHANGEBGMコンストラクタ
	public BattleTextTag(String tagName, String bgmFilePath){
		this.tagName = tagName;
		//CHANGEBGMタグでは何も表示しない
		this.leftCharaName = "@";
		this.rightCharaName = "@";
		brightEvaluation("NR");
		this.expression = 0;
		sePointer = 0;
		//テキスト文の初期化して空白だけ入れとく
		this.text = new char[1];
		text[0] = ' ';
		this.setBgmFilePath(bgmFilePath);
	}

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

	//SELECTSWITCH用コンストラクタ///////////////////////////////////////////////////////////////////////////////////
	public BattleTextTag(String tagName, char[] str, String[] choice){
		this.tagName = tagName;
		this.leftCharaName = "temp";
		this.leftBright = false;
		this.rightCharaName = "temp";
		this.rightBright = false;
		this.text = str.clone();
	}
}
