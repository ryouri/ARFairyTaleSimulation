package arcircle.ftsim.state;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.loadsave.LoadSaveModel;
import arcircle.ftsim.state.loadsave.LoadSaveView;

public class LoadSaveState extends KeyInputState {

	private LoadSaveModel lsModel;
	private LoadSaveView lsView;
	public int selected = 0;
	public File[] files;

	public LoadSaveState(int state) {
		super(state);
		files = getSaveFiles();
	}

	public void nextState() {
		SelectStoryState selectStoryState = (SelectStoryState)stateGame.getState(StateConst.SELECT_STORY);
		// 現Stateで鳴らしているBGMをSelectStoryStateのlastBGMとして保存
		selectStoryState.setLastBGM(lastBGM);
		// ここでロード
		FTSimulationGame.save = FTSimulationGame.save.load(files[selected].getPath());
		// SelectStoryStateへ
		stateGame.enterState(StateConst.SELECT_STORY,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	public void backState() {
		GameStartState gameStartState = (GameStartState)stateGame.getState(StateConst.GAME_START);
		gameStartState.setLastBGM(lastBGM);
		stateGame.enterState(StateConst.GAME_START,
				new FadeOutTransition(Color.black, 100),
				new FadeInTransition(Color.black, 100));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		lsModel = new LoadSaveModel(this);
		lsView = new LoadSaveView(lsModel, this);

		keyInputStack.clear();
		keyInputStack.push(lsModel);
		rendererArray.clear();
		rendererArray.add(lsView);
	}

	private File[] getSaveFiles(){
		String path = "./save/";
		File dir = new File(path);
		FilenameFilter filter = new FilenameFilter(){
			public boolean accept(File dir, String name){
				if (name.endsWith(".sav")) {
					return true;
				}
				return false;
			}
		};
		File[] files = dir.listFiles(filter);
		Arrays.sort(files, Collections.reverseOrder());
		return files;
	}
}
