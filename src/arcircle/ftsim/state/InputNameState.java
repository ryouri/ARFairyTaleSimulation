package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.inputname.InputNameModel;
import arcircle.ftsim.state.inputname.InputNameView;

public class InputNameState extends KeyInputState {

	private InputNameModel inModel;
	private InputNameView inView;

	public Image [] sprite = new Image[1];

	public InputNameState(int state) {
		super(state);
	}

	public void nextState() {

		//misawa
//		GameState sbGame = stateGame.getState(StateConst.SIM_GAME);
//		SimGameState sgState = (SimGameState) sbGame;
//		sgState.setReadFilePath("01_Story", "01", 1, 1);
//		stateGame.enterState(StateConst.SIM_GAME,
//		stateGame.enterState(StateConst.GAME_START,

		//yukineko
		stateGame.enterState(StateConst.TALK,
				new FadeOutTransition(Color.black, 500),
				new FadeInTransition(Color.black, 500));
	}


	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		inModel = new InputNameModel(this);
		inView = new InputNameView(inModel, this);

		//SpriteSheet ssheet = new SpriteSheet(new Image("./image/InputName.png"), 80, 80);
		//sprite[0] = ssheet.getSubImage(0, 0);

		System.out.println("Enter input Name State");

		keyInputStack.clear();
		keyInputStack.push(inModel);
		rendererArray.clear();
		rendererArray.add(inView);
	}
}
