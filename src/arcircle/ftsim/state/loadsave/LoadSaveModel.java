package arcircle.ftsim.state.loadsave;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.LoadSaveState;

public class LoadSaveModel implements KeyListner{
	private LoadSaveState lsState;
	public String message = "ロードするデータを選択してください";

	public LoadSaveModel(LoadSaveState lsState){
		super();
		this.lsState = lsState;
	}
	@Override
	public void keyInput(KeyInput keyInput) {
		//デバッグ用スキップキー
		if(keyInput.isKeyDown(Input.KEY_D)){
			lsState.nextState();
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
		} else if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
		} else if (keyInput.isKeyDown(Input.KEY_Z)) {
		}
	}

}
