package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.selectstory.SelectStoryModel;
import arcircle.ftsim.state.selectstory.SelectStoryView;

public class SelectStoryState extends KeyInputState {

	private SelectStoryModel ssModel;
	private SelectStoryView ssView;

	public static final int STORY_NUM = 7;
	public boolean[] isClearStage;

	public SelectStoryState(int state) {
		super(state);
	}

	public void nextState() {
		TalkState talkState = (TalkState)stateGame.getState(StateConst.TALK);
		talkState.setLastBGM(bgm);
		stateGame.enterState(StateConst.TALK,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

		ssModel = new SelectStoryModel(this);
		ssView = new SelectStoryView(ssModel, this);

		isClearStage = FTSimulationGame.save.isClearStages();

		// 全部クリアしてたらスタッフロールへ
		if (FTSimulationGame.save.isAllCleared()) {
			StaffRollState staffRollState = (StaffRollState)stateGame.getState(StateConst.STAFF_ROLL);
			staffRollState.setLastBGM(lastBGM);
			stateGame.enterState(StateConst.STAFF_ROLL,
					new FadeOutTransition(Color.black, 100),
					new FadeInTransition(Color.black, 100));
			return;
		}

		isClearStage[2] = true;

		try {
			bgm = new Sound("./Stories/BGM/FTSim003.ogg");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		lastBGM.stop();
		bgm.loop();
		System.out.println("Enter Select Story State");

		keyInputStack.clear();
		keyInputStack.push(ssModel);
		rendererArray.clear();
		rendererArray.add(ssView);
	}
	//前のステートで流していたBGMを受け取るためのメソッド
	public void receiveBGM(Sound lastBGM){
		this.lastBGM = lastBGM;
	}
}
