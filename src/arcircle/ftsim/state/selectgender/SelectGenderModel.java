package arcircle.ftsim.state.selectgender;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.SelectGenderState;

public class SelectGenderModel implements KeyListner {

	private SelectGenderState gsState;

	public String message = "S e l e c t  Y o u r G e n d e r";

	public SelectGenderModel(SelectGenderState gsState) {
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