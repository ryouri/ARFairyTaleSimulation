package arcircle.ftsim.state.selectgender;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.SelectGenderState;

public class SelectGenderModel implements KeyListner {

	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public static int GENDER;

	private SelectGenderState sgState;

	public String message = "性別をえらんでください";

	public SelectGenderModel(SelectGenderState sgState) {
		super();
		this.sgState = sgState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if(keyInput.isKeyDown(Input.KEY_LEFT)) {
			GENDER = MALE;
		}
		else if(keyInput.isKeyDown(Input.KEY_RIGHT)) {
			GENDER = FEMALE;
		}
		else if(keyInput.isKeyDown(Input.KEY_Z)) {
			sgState.nextState();
		}
	}
}