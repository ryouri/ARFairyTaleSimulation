package arcircle.ftsim.state.staffroll;

import java.io.File;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.StaffRollState;

public class StaffRollModel implements KeyListner{
	private StaffRollState srState;
	private File textFile = new File("./Stories/StaffRoll.txt");
	private int outputHeight;
	private boolean finishFlag;

	public StaffRollModel(StaffRollState srState){
		super();
		this.srState = srState;
		outputHeight = FTSimulationGame.HEIGHT + 30;
		finishFlag = false;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if (keyInput.isKeyDown(Input.KEY_Z) && finishFlag) {
			srState.nextState();
		} else if (keyInput.isKeyPressed(Input.KEY_Z) && !finishFlag) {
			outputHeight -= 5;
		}
	}

	public void decrementOutputHeight() {
		outputHeight--;
	}

	public int getOutputHeight() {
		return outputHeight;
	}

	public void setFinishTrue() {
		finishFlag = true;
	}

	public File getTextFile() {
		return textFile;
	}
}
