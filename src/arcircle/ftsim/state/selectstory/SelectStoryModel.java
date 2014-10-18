package arcircle.ftsim.state.selectstory;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.SelectStoryState;

public class SelectStoryModel implements KeyListner {

	public static final int MOMOTARO = 0;
	public static final int KAGUYA = 1;
	public static final int AKAZUKIN = 2;
	public static final int MAMENOKI = 3;
	public static final int SHINDERERA = 4;
	public static final int KOBUTA = 5;

	public int story = 0;

	private SelectStoryState ssState;

	public String message = "どのストーリーで遊びますか？";

	public SelectStoryModel(SelectStoryState ssState) {
		super();
		this.ssState = ssState;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//デバッグ用スキップキー
		if(keyInput.isKeyDown(Input.KEY_D)){
			FTSimulationGame.save.getNowStage().storyName = "01_Story";
			FTSimulationGame.save.getNowStage().subStoryNum = "01";
			FTSimulationGame.save.getNowStage().selectLogue = 0;
			ssState.nextState();
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			story--;
			if(story < 0){
				story = 5;
			}
		} else if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			story++;
			if(story > 5){
				story = 0;
			}
		} else if (keyInput.isKeyDown(Input.KEY_UP)) {
			story -= 3;
			if(story < 0){
				story += 6;
			}
		} else if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			story += 3;
			if(story > 5){
				story -= 6;
			}
		} else if (keyInput.isKeyDown(Input.KEY_Z)) {
			FTSimulationGame.save.getNowStage().storyName = "0" + (story + 1) + "_Story";
			FTSimulationGame.save.getNowStage().subStoryNum = "01";
			FTSimulationGame.save.getNowStage().selectLogue = 0;
			ssState.nextState();
		}
	}
}
