package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.talk.TalkModel;
import arcircle.ftsim.state.talk.TalkView;



public class TalkState extends KeyInputState {
	//フィールド//////////////////////////////////////////////////////////////////////////////////////////////////////
	private TalkModel talkModel;
	private TalkView talkView;

	//private int chapterID;	//現在の章
	//private int subStoryID;	//現在の話数

	//コンストラクタ////////////////////////////////////////////////////////////////////////////////////////////////
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
		//stateGame.enterState(StateConst.SELECT_GENDER,
		GameState sbGame = stateGame.getState(StateConst.SIM_GAME);
		SimGameState sgState = (SimGameState) sbGame;
		sgState.setReadFilePath("01_Story", "01", 1, 1);
		stateGame.enterState(StateConst.SIM_GAME,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	//TalkStateに入るときに呼び出されるメソッド-----------------------------------------------------------------------
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		talkModel = new TalkModel(this);
		talkView = new TalkView(talkModel, this);
		System.out.println("Enter Talk State");
		keyInputStack.clear();
		keyInputStack.push(talkModel);
		rendererArray.clear();
		rendererArray.add(talkView);
	}

}
