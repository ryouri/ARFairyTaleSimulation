package arcircle.ftsim.simulation.talk;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.state.talk.TalkModel;

public class BattleTalkModel extends TalkModel {
	boolean isEnd;
	String eventFileName;

	public BattleTalkModel(String eventFileName) {
		super();
		this.eventFileName = eventFileName;
	}

	@Override
	protected void generateTagData() {
    	//会話文のあるストーリー章のフォルダパス
    	String logueFilePath = "";
		/* プロローグの場合 */
		logueFilePath = "Stories/" + nowStoryName + "/" + nowSubStoryName + "/" + eventFileName;

    	loadTextData(logueFilePath);
	}


	@Override
	//キーインプット------------------------------------------------------------------------------------
	public void keyInput(KeyInput keyInput) {
		//デバッグ用キー
		if(keyInput.isKeyDown(Input.KEY_D)){
			keyInputState.removeRendererArrayEnd();
			keyInputState.removeKeyInputStackFirst();
			isEnd = true;
		}
		if(keyInput.isKeyDown(Input.KEY_Z)){
			//次のステートへ
			if(nextTalkFlag && nextStateFlag == true){
				keyInputState.removeRendererArrayEnd();
				keyInputState.removeKeyInputStackFirst();
				isEnd = true;
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

	public boolean isEnd() {
		return isEnd;
	}
}
