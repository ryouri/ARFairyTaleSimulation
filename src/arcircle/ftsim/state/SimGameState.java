package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.simgame.SimGameModel;
import arcircle.ftsim.state.simgame.SimGameView;


public class SimGameState extends KeyInputState {
	private SimGameModel sgModel;
	private SimGameView sgView;

	public SimGameState(int state) {
		super(state);
	}

	public void nextState() {
		stateGame.enterState(StateConst.GAME_START,
				new FadeOutTransition(Color.black, 500),
				new FadeInTransition(Color.black, 500));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		sgModel = new SimGameModel(this);
		sgView = new SimGameView(sgModel, this);

		System.out.println("Enter Sim Game State");

		keyInputStack.clear();
		rendererArray.clear();
		//rendererArray.add(sgView);
	}
}
