package arcircle.ftsim.state.staffroll;

import java.io.File;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.StaffRollState;

public class StaffRollModel implements KeyListner{
	private StaffRollState srState;
	private File text_file = new File("./Stories/StaffRoll.txt");
	public int output_height = FTSimulationGame.HEIGHT + 30;

	public StaffRollModel(StaffRollState srState){
		super();
		this.srState = srState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if (keyInput.isKeyDown(Input.KEY_Z)) {
			srState.nextState();
		}
	}

	public File getTextFile() {
		return text_file;
	}
}
