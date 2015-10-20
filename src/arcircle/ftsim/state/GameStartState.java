package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.gamestart.GameStartModel;
import arcircle.ftsim.state.gamestart.GameStartView;

public class GameStartState extends KeyInputState {

	private GameStartModel gsModel;
	private GameStartView gsView;

	public GameStartState(int state) {
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

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		super.update(container, game, delta);
	}

	public void nextState() {
		// 開始を選択
		if (gsModel.state == 0){
			SelectGenderState selectGenderState = (SelectGenderState)stateGame.getState(StateConst.SELECT_GENDER);
			selectGenderState.setLastBGM(bgm);
			stateGame.enterState(StateConst.SELECT_GENDER,
					new FadeOutTransition(Color.black, 100),
					new FadeInTransition(Color.black, 100));
		}
		// ロードを選択
		if (gsModel.state == 1){
			LoadSaveState loadSaveState = (LoadSaveState)stateGame.getState(StateConst.LOAD_SAVE_DATA);
			loadSaveState.setLastBGM(bgm);
			stateGame.enterState(StateConst.LOAD_SAVE_DATA,
					new FadeOutTransition(Color.black, 100),
					new FadeInTransition(Color.black, 100));
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)throws SlickException {
		super.enter(container, game);
		gsModel = new GameStartModel(this);
		gsView = new GameStartView(gsModel, this);
		try {
			bgm = new Sound("./Stories/BGM/FTSim001_bpm120.ogg");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		bgm.loop();
		System.out.println("Enter Game Start State");
		keyInputStack.clear();
		keyInputStack.push(gsModel);
		rendererArray.clear();
		rendererArray.add(gsView);
	}
}
