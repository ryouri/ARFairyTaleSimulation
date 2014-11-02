package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.simgame.SimGameModel;
import arcircle.ftsim.state.simgame.SimGameView;


public class SimGameState extends KeyInputState {
	private SimGameModel sgModel;
	private SimGameView sgView;
//	private BattleTalkModel btModel;
//	private BattleTalkView btView;

	public String sectionPath;
	public String subStoryPath;
	public int sectionNum;
	public int subStoryNum;
	//親クラスのbgmと, このクラスのnewBGMを使ってBGMを切り替える
	private Sound newBGM;	//BGM切り替え用格納器
	private boolean isBGM = true;	//bgmとnewBGMのどちらを鳴らしているかの判定に使う


	public SimGameState(int state) {
		super(state);
	}

	public void nextState() {

		TalkState talkState = (TalkState)stateGame.getState(StateConst.TALK);
		talkState.setLastBGM(bgm);
		talkState.setStageNumber(1);
		stateGame.enterState(StateConst.TALK,
				new FadeOutTransition(Color.black, 500),
				new FadeInTransition(Color.black, 500));

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		sgModel = new SimGameModel(this);
		sgModel.setReadFilePath(sectionPath, subStoryPath,
				sectionNum, subStoryNum);
		sgModel.init();
		sgView = new SimGameView(sgModel, this);

//		btModel = new BattleTalkModel(this);
//		btView = new BattleTalkView(btModel, this);
//
//		System.out.println("Enter Talk State");
//		keyInputStack.clear();
//		keyInputStack.push(btModel);
//		rendererArray.clear();
//		rendererArray.add(btView);

		try {
			bgm = new Sound("./Stories/BGM/FTSim012.ogg");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		lastBGM.stop();
		bgm.loop();

		System.out.println("Enter Sim Game State");
	}



	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);
		sgModel.update(container, game, delta);
	}

	public void setReadFilePath(String sectionPath, String subStoryPath,
			int sectionNum, int subStoryNum) {
			this.sectionPath = sectionPath;
			this.subStoryPath = subStoryPath;
			this.sectionNum = sectionNum;
			this.subStoryNum = subStoryNum;
	}

	//BGMの切り替えを行うメソッド(TalkViewに呼び出される)--------------------------------------
	public void changeBGM(String bgmFilePath) {
		// bgmからnewBGMに切り替え
		if (isBGM) {
			System.out.println(bgmFilePath);
			try {
				newBGM = new Sound(bgmFilePath);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			bgm.stop();
			newBGM.loop();
			isBGM = false;
			// newBGMからbgmに切り替え
		} else {
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
