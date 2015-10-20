package arcircle.ftsim.state.loadsave;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.LoadSaveState;

public class LoadSaveModel implements KeyListner{
	private LoadSaveState lsState;
	public String message = "ロードするデータを選択してください";
	private String path = "./image/loadWindow/";
	public Image background, box, box2;


	public LoadSaveModel(LoadSaveState lsState){
		super();
		this.lsState = lsState;
		try {
			background = new Image(path + "window.png");
			box = new Image(path + "box.png");
			box2 = new Image(path + "box2.png");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//デバッグ用スキップキー
		if(keyInput.isKeyDown(Input.KEY_D)){
			lsState.nextState();
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			if (lsState.selected < lsState.files.length - 1){
				lsState.selected++;
			}
		} else if (keyInput.isKeyDown(Input.KEY_UP)) {
			if (lsState.selected > 0){
				lsState.selected--;
			}
		} else if (keyInput.isKeyDown(Input.KEY_Z)) {
			lsState.nextState();
		} else if (keyInput.isKeyDown(Input.KEY_X)) {
			lsState.backState();
		}
	}

}
