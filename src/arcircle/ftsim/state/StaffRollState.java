package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.staffroll.StaffRollModel;
import arcircle.ftsim.state.staffroll.StaffRollView;


public class StaffRollState extends KeyInputState {
	public StaffRollState(int state) {
		super(state);
	}
	private StaffRollModel srModel;
	private StaffRollView srView;

	public void nextState() {
		bgm.stop();
		// ゲーム開始画面へ
		stateGame.enterState(StateConst.GAME_START,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		srModel = new StaffRollModel(this);
		srView = new StaffRollView(srModel, this);
		try {
			bgm = new Sound("./Stories/BGM/FTSim003.ogg");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		lastBGM.stop();
		bgm.loop();

		keyInputStack.clear();
		keyInputStack.push(srModel);
		rendererArray.clear();
		rendererArray.add(srView);
	}
}
