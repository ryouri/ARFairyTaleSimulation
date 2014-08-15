package arcircle.ftsim.state.gamestart;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.GameStartState;

public class GameStartModel implements KeyListner {

	public String title = "F a i r y  T a l e  S i m u l a t i o n !!!!!!!!!!!!!!!!!!!";

	private GameStartState gsState;

	public GameStartModel(GameStartState gsState) {
		super();
		this.gsState = gsState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if(keyInput.isKeyDown(Input.KEY_Z)) {
			gsState.nextState();
		}
	}
	
	
}
