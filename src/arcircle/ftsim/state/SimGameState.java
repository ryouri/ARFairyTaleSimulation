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

	public String sectionPath;
	public String subStoryPath;
	public int sectionNum;
	public int subStoryNum;

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
		sgModel.setReadFilePath(sectionPath, subStoryPath,
				sectionNum, subStoryNum);
		sgModel.init();
		sgView = new SimGameView(sgModel, this);

		System.out.println("Enter Sim Game State");
	}

	public void setReadFilePath(String sectionPath, String subStoryPath,
			int sectionNum, int subStoryNum) {
			this.sectionPath = sectionPath;
			this.subStoryPath = subStoryPath;
			this.sectionNum = sectionNum;
			this.subStoryNum = subStoryNum;
	}
}
