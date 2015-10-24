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
	public static final int BOSS = 6;

	public static final int STORY_NUM = 7;

	public int story;

	private SelectStoryState ssState;

	public boolean[] isClearStage;
	public boolean onlyBossStage;


//	public String message = "どのストーリーで遊びますか？";

	public SelectStoryModel(SelectStoryState ssState) {
		super();

		this.ssState = ssState;
		isClearStage = FTSimulationGame.save.isClearStages();
		onlyBossStage = FTSimulationGame.save.isAllClearedWOBoss();
		// 選択するストーリーがクリア済みなら隣のストーリーに
		for (int i = 0; i < STORY_NUM; i++) {
			if (!isClearStage[i]) {
				story = i;
				break;
			}
		}
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
		if (keyInput.isKeyDown(Input.KEY_LEFT) && !onlyBossStage) {
			do{
				story--;
				if(story < 0){
					story = STORY_NUM - 2; // boss stage分 1引く
				}
			}while(isClearStage[story]);

		} else if (keyInput.isKeyDown(Input.KEY_RIGHT) && !onlyBossStage) {
			do{
				story++;
				if(story > STORY_NUM - 2){ // boss stage分 1引く
					story = 0;
				}
			}while(isClearStage[story]);
		} else if (keyInput.isKeyDown(Input.KEY_UP) && !onlyBossStage) {
			do{
				story -= 3;
				if(story < 0){
					story += 6;
				}
			}while(isClearStage[story]);

		} else if (keyInput.isKeyDown(Input.KEY_DOWN) && !onlyBossStage) {
			do{
				story += 3;
				if(story > STORY_NUM - 2){ // boss stage分 1引く
					story -= 6;
				}
			}while(isClearStage[story]);

		} else if (keyInput.isKeyDown(Input.KEY_Z)) {
			FTSimulationGame.save.getNowStage().storyName = "0" + (story + 1) + "_Story";
			FTSimulationGame.save.getNowStage().subStoryNum = "01";
			FTSimulationGame.save.getNowStage().selectLogue = 0;
			ssState.nextState();
		}


	}
}
