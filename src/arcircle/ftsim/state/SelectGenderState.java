package arcircle.ftsim.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.state.selectgender.SelectGenderModel;
import arcircle.ftsim.state.selectgender.SelectGenderView;

public class SelectGenderState extends KeyInputState {

	private SelectGenderModel sgModel;
	private SelectGenderView sgView;

	public SelectGenderState(int state) {
		super(state);
	}

	public void nextState() {
		InputNameState inputNameState = (InputNameState)stateGame.getState(StateConst.INPUT_NAME);
		inputNameState.setLastBGM(lastBGM);
		stateGame.enterState(StateConst.INPUT_NAME,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		sgModel = new SelectGenderModel(this);
		sgView = new SelectGenderView(sgModel, this);

		System.out.println("Enter Select Gender State");

		keyInputStack.clear();
		keyInputStack.push(sgModel);
		rendererArray.clear();
		rendererArray.add(sgView);
	}
}
