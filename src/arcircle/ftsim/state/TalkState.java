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
import arcircle.ftsim.state.talk.TalkModel;
import arcircle.ftsim.state.talk.TalkView;



public class TalkState extends KeyInputState {
	//フィールド//////////////////////////////////////////////////////////////////////////////////////////////////////
	private TalkModel talkModel;
	private TalkView talkView;
	private int stageNumber = 0;
	//親クラスのbgmと, このクラスのnewBGMを使ってBGMを切り替える
	private Sound newBGM;	//BGM切り替え用格納器
    private boolean isBGM = true;	//bgmとnewBGMのどちらを鳴らしているかの判定に使う

	//private int chapterID;	//現在の章
	//private int subStoryID;	//現在の話数

	public int getStageNumber() {
		return stageNumber;
	}

	public void setStageNumber(int stageNumber) {
		this.stageNumber = stageNumber;
	}
	
	//コンストラクタ////////////////////////////////////////////////////////////////////////////////////////////////
	//TODO:stageStateはあとでセーブデータから読み込む
	public TalkState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		super.render(container, game, g);
	}

	//@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);
	}

	//次の状態へ行くメソッド-------------------------------------------------------------------------------------------
	public void nextState() {
		SimGameState simGameState = (SimGameState)stateGame.getState(StateConst.SIM_GAME);
		simGameState.setLastBGM(bgm);
		
		//stateGame.enterState(StateConst.SELECT_GENDER,
		GameState sbGame = stateGame.getState(StateConst.SIM_GAME);
		SimGameState sgState = (SimGameState) sbGame;
		sgState.setReadFilePath(FTSimulationGame.save.getNowStage().storyName, FTSimulationGame.save.getNowStage().subStoryNum, 1, 1);
		stateGame.enterState(StateConst.SIM_GAME,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	//TalkStateに入るときに呼び出されるメソッド-----------------------------------------------------------------------
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		//BGMの切り替え処理
		try {
			bgm = new Sound("./Stories/BGM/FTSim004.ogg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		lastBGM.stop();
		bgm.loop();
		
		talkModel = new TalkModel(this);
		talkView = new TalkView(talkModel, this);
		
		System.out.println("Enter Talk State");
		keyInputStack.clear();
		keyInputStack.push(talkModel);
		rendererArray.clear();
		rendererArray.add(talkView);
	}
	
	//BGMの切り替えを行うメソッド(TalkViewに呼び出される)--------------------------------------
	public void changeBGM(String bgmFilePath) {
		//bgmからnewBGMに切り替え
		if(isBGM){
			System.out.println(bgmFilePath);
			try {
				newBGM = new Sound(bgmFilePath);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			bgm.stop();
			newBGM.loop();
			isBGM = false;
		//newBGMからbgmに切り替え
		}else{
			try {
				bgm = new Sound(bgmFilePath);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			newBGM.stop();
			bgm.loop();
			isBGM = true;
		}
	}
}
