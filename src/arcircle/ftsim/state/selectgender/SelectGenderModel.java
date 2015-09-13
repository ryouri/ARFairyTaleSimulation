package arcircle.ftsim.state.selectgender;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.SelectGenderState;

public class SelectGenderModel implements KeyListner {

	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public int gender = MALE;

	private SelectGenderState sgState;

	public String message = "キャラを選択してください";

	public SelectGenderModel(SelectGenderState sgState) {
		super();
		this.sgState = sgState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//デバッグ用スキップキー
		if(keyInput.isKeyDown(Input.KEY_D)){
			FTSimulationGame.save.getPlayer().gender = MALE;
			sgState.nextState();
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			gender = MALE;
		} else if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			gender = FEMALE;
		} else if (keyInput.isKeyDown(Input.KEY_Z)) {
			FTSimulationGame.save.getPlayer().gender = gender;
			sgState.nextState();
		}
	}
}
