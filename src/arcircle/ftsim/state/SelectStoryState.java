package arcircle.ftsim.state;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.selectstory.SelectStoryModel;
import arcircle.ftsim.state.selectstory.SelectStoryView;

public class SelectStoryState extends KeyInputState {

	private SelectStoryModel ssModel;
	private SelectStoryView ssView;
	
	public int storyNum = 6;
	public boolean[] isClearStage;
	
	public SelectStoryState(int state) {
		super(state);
	}

	public void nextState() {
		TalkState talkState = (TalkState)stateGame.getState(StateConst.TALK);
		talkState.setLastBGM(bgm);
		stateGame.enterState(StateConst.TALK,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		ssModel = new SelectStoryModel(this);
		ssView = new SelectStoryView(ssModel, this);
		
		isClearStage = new boolean[storyNum];	//初期値は多分false
		ArrayList<String> clearStage = FTSimulationGame.save.getClearStoryNameArray();
		if(!clearStage.isEmpty()){
			for(int i = 0 ; i < clearStage.size() ; i++){
				if(clearStage.get(i).equals("01_Story")){
					isClearStage[0] = true;
				}else if(clearStage.get(i).equals("02_Story")){
					isClearStage[1] = true;
				}else if(clearStage.get(i).equals("03_Story")){
					isClearStage[2] = true;
				}else if(clearStage.get(i).equals("04_Story")){
					isClearStage[3] = true;
				}else if(clearStage.get(i).equals("05_Story")){
					isClearStage[4] = true;
				}else if(clearStage.get(i).equals("06_Story")){
					isClearStage[5] = true;
				}else{
					System.out.println("error_SelectStoryView_clearStage");
				}
			}
		}
		
		isClearStage[2] = true;
		
		try {
			bgm = new Sound("./Stories/BGM/FTSim003.ogg");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		lastBGM.stop();
		bgm.loop();
		System.out.println("Enter Select Story State");

		keyInputStack.clear();
		keyInputStack.push(ssModel);
		rendererArray.clear();
		rendererArray.add(ssView);
	}
	//前のステートで流していたBGMを受け取るためのメソッド
	public void receiveBGM(Sound lastBGM){
		this.lastBGM = lastBGM;
	}
}
