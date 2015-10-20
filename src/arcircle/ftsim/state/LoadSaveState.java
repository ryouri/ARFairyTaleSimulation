package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.loadsave.LoadSaveModel;
import arcircle.ftsim.state.loadsave.LoadSaveView;

public class LoadSaveState extends KeyInputState {

	private LoadSaveModel lsModel;
	private LoadSaveView lsView;

	public LoadSaveState(int state) {
		super(state);
	}

	public void nextState() {
		SelectStoryState selectStoryState = (SelectStoryState)stateGame.getState(StateConst.SELECT_STORY);
		// 現Stateで鳴らしているBGMをSelectStoryStateのlastBGMとして保存
		selectStoryState.setLastBGM(bgm);
		// SelectStoryStateへ
		stateGame.enterState(StateConst.SELECT_STORY,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		lsModel = new LoadSaveModel(this);
		lsView = new LoadSaveView(lsModel, this);

		keyInputStack.clear();
		keyInputStack.push(lsModel);
		rendererArray.clear();
		rendererArray.add(lsView);



	}
}
