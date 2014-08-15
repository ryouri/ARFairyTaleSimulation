package arcircle.ftsim.state.simgame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.SimGameState;

public class SimGameModel implements KeyListner {
	private SimGameState sgState;

	public SimGameModel(SimGameState simGameState) {
		sgState = simGameState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
	}
}
