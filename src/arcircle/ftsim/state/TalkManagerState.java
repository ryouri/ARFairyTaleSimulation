package arcircle.ftsim.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.state.talk.LoadTalkGraphics;

public abstract class TalkManagerState extends KeyInputState {

	public TalkManagerState(int state) {
		super(state);
	}
    /**キャラクターの立ち絵と顔絵をロードして格納しておく*/
    private LoadTalkGraphics talkGraphics;

	private Sound newBGM;	//BGM切り替え用格納器
	private boolean isBGM = true;	//bgmとnewBGMのどちらを鳴らしているかの判定に使う

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		//全キャラクターのロード
		talkGraphics = new LoadTalkGraphics(characterPath);
	}

	//フィールド------------------------------------------------------------------------------------------------------
	/**Characterフォルダのパス*/
    private static final String characterPath = "./Stories/Characters";

    //アクセッタ------------------------------------------------------------------------------------------------------
    /**トークグラフィックスを取得するメソッド*/
    public LoadTalkGraphics getTalkGraphics(){ return talkGraphics;}

	//BGMの切り替えを行うメソッド(TalkViewに呼び出される)--------------------------------------
	public void changeBGM(String bgmFilePath) {
		//bgmからnewBGMに切り替え
		if(isBGM){
			/* bgmに格納されているBGMを再生中の場合 */
			System.out.println(bgmFilePath);	//
			try {
				//今使ってないnewBGMに次に流すBGMをロード
				newBGM = new Sound(bgmFilePath);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			//今鳴らしているBGMを止める
			bgm.stop();
			//次に鳴らすBGMをスタート
			newBGM.loop();
			//今鳴らしているBGMがnewBGMであることを示すためにisBGMをfalseに
			isBGM = false;
		//newBGMからbgmに切り替え
		}else{
			/* newBGMに格納されているBGMを再生中の場合 */
			try {
				//今使ってないbgmに次に流すBGMをロード
				bgm = new Sound(bgmFilePath);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			//今鳴らしているBGMを止める
			newBGM.stop();
			//次に鳴らすBGMをスタート
			bgm.loop();
			//今鳴らしているBGMがbgmであることを示すためにisBGMをtrueに
			isBGM = true;
		}
	}
}
