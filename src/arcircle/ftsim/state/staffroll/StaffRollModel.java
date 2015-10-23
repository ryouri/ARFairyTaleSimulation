package arcircle.ftsim.state.staffroll;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.StaffRollState;

public class StaffRollModel implements KeyListner{
	private StaffRollState srState;

	public StaffRollModel(StaffRollState srState){
		super();
		this.srState = srState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
