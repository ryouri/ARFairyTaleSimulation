package arcircle.ftsim.state.talk;

public class TextTag {
	//フィールド//////////////////////////////////////////////////////////////////////////////////////
	private String tagName;
	private String leftCharaName;
	private boolean leftBright;
	private String rightCharaName;
	private boolean rightBright;
	private char[] text;
	private String[] choice;

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
	public void setText(char[] text) { this.text = text; }
	public String getChoice(int i) { return choice[i]; }
	public void setChoice(int i, String choice) { this.choice[i] = choice; }

	//SPEAK用コンストラクタ/////////////////////////////////////////////////////////////////////////////////
	public TextTag(String tagName, String leftCharaName, int leftBright, String rightCharaName, int rightBright,char[] str){
		this.tagName = tagName;
		this.leftCharaName = leftCharaName;
		if(leftBright == 0){
			this.leftBright = false;
		}else{
			this.leftBright = true;
		}
		this.rightCharaName = rightCharaName;
		if(rightBright == 0){
			this.rightBright = false;
		}else{
			this.rightBright = true;
		}
		this.text = str.clone();
	}

	//SELECTSWITCH用コンストラクタ///////////////////////////////////////////////////////////////////////////////////
	public TextTag(String tagName, char[] str, String[] choice){
		this.tagName = tagName;
		this.leftCharaName = "temp";
		this.leftBright = false;
		this.rightCharaName = "temp";
		this.rightBright = false;
		this.text = str.clone();
	}
}
