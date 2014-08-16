package arcircle.ftsim.state.talk;

public class TextTag {
	//フィールド//////////////////////////////////////////////////////////////////////////////////////
	private String tagName;
	private String speakerName;
	private char[] text;
	
	//アクセッタ///////////////////////////////////////////////////////////////////////////////////////
	public String getTagName() { return tagName; }
	public void setTagName(String tagName) { this.tagName = tagName; }
	public char[] getText() { return text; }
	public void setText(char[] text) { this.text = text; }
	public String getSpeakerName() { return speakerName; }
	public void setSpeakerName(String speakerName) { this.speakerName = speakerName; }
	
	//コンストラクタ/////////////////////////////////////////////////////////////////////////////////
	public TextTag(String tagName, String speakerName, char[] str){
		this.tagName = tagName;
		this.speakerName = speakerName;
		this.text = str.clone();
	}
	
	
}
