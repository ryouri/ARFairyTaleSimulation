package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.save.NowStage;
import arcircle.ftsim.state.talk.TalkModel;
import arcircle.ftsim.state.talk.TalkView;

/**会話するシーンを処理するステート
 * @author ゆきねこ*/
// [リファクタリング] コメントつけ終わったよ
// とりあえずステートはこのままでいいや

public class TalkState extends TalkManagerState {

	/**トークモデル：.txtの処理*/
	private TalkModel talkModel;
	/**トークビュー：会話文を描画する*/
	private TalkView talkView;
	/**ステージ番号，
	 * 桃太郎=0, かぐや姫=1, 赤ずきん=2, ジャックと豆の木=3, シンデレラ=4, ３匹の仔豚=5*/
	private int stageNumber = 0;
	//private int chapterID;	//現在の章		現在非対応なのでコメントアウト
	//private int subStoryID;	//現在の話数	現在非対応なのでコメントアウト

    /** ステージ番号を取得する */
    public int getStageNumber() { return stageNumber; }
	/** ステージ番号をセットする*/
	public void setStageNumber(int stageNumber) { this.stageNumber = stageNumber; }

	//コンストラクタ------------------------------------------------------------------------------------------------
	//TODO:stageStateはあとでセーブデータから読み込む
	public TalkState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
	}

	//@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);
	}

	@Override
	//次の状態へ行くメソッド-------------------------------------------------------------------------------------------
	public void nextState() {
		// NowStageクラスを取得
		NowStage nowStage = FTSimulationGame.save.getNowStage();
		// enterをする前に，次に入るステートのパスを指定する．
		if(nowStage.selectLogue == 0){
			/* 流したローグはプロローグ */
			// SimGameStateへ
			// SimGameStateを取得
			GameState sbGame = stateGame.getState(StateConst.SIM_GAME);
			SimGameState sgState = (SimGameState) sbGame;
			// 現Stateで鳴らしているBGMをSimGameStateのlastBGMとして一時保存
			sgState.setLastBGM(bgm);
			sgState.setReadFilePath(FTSimulationGame.save.getNowStage().storyName,
					FTSimulationGame.save.getNowStage().subStoryNum, 1, 1);
			stateGame.enterState(StateConst.SIM_GAME,
					new FadeOutTransition(Color.black, 100),
					new FadeInTransition(Color.black, 100));
		}else if(nowStage.selectLogue == 1){
			/* 流したローグはエピローグ */
			// SelectStoryStateへ
			// SelectStoryStateを取得
			SelectStoryState selectStoryState = (SelectStoryState)stateGame.getState(StateConst.SELECT_STORY);
			// 現Stateで鳴らしているBGMをSelectStoryStateのlastBGMとして保存
			selectStoryState.setLastBGM(bgm);
			// SelectStoryStateへ
			stateGame.enterState(StateConst.SELECT_STORY,
					new FadeOutTransition(Color.black, 100),
					new FadeInTransition(Color.black, 100));
		}

	}

	@Override
	//TalkStateに入るときに呼び出されるメソッド-----------------------------------------------------------------------
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		//KeyInputStateのenter処理
		super.enter(container, game);
		//BGMの切り替え処理
		try {
			//現ステートで流すBGMをロード
			bgm = new Sound("./Stories/BGM/FTSim004.ogg");
		} catch (SlickException e) {
			//SoundLoad エラー処理
			e.printStackTrace();
		}
		//前のステートから鳴っていたBGMを止める
		lastBGM.stop();
		//現ステートで鳴らすBGMをスタート
		bgm.loop();

		//トークモデルインスタンスを作成
		talkModel = new TalkModel();
		talkModel.init(this);
		//トークビューインスタンスを作成
		talkView = new TalkView(talkModel, this);

		System.out.println("Enter Talk State");		//トークステートに入ったことをコンソールに表示

		//おまじない的ななにか(misawa担当)
		keyInputStack.clear();
		keyInputStack.push(talkModel);
		rendererArray.clear();
		rendererArray.add(talkView);
	}
}
