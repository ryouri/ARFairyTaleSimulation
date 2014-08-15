package arcircle.ftsim.state.inputname;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.InputNameState;


public class InputNameModel implements KeyListner {

	private InputNameState inState;

	public String message = "S e l e c t  Y o u r N a m e ";

	public InputNameModel(InputNameState inputNameState) {
		super();
		this.inState = inputNameState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if(keyInput.isKeyDown(Input.KEY_Z)) {
			inState.nextState();
		}
	}
}
