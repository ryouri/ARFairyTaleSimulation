package arcircle.ftsim.state.simgame;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.model.SubInfoWindow;
import arcircle.ftsim.simulation.model.World;
import arcircle.ftsim.state.SimGameState;

public class SimGameModel{
	private SimGameState sgState;

	public World world;

	public String sectionPath;
	public String subStoryPath;
	public int sectionNum;
	public int subStoryNum;

	//TODO: 現在は最上層のフォルダは定数で指定しているが，受け取れるようにするべき
	public static final String storiesFolder = "Stories";

	public SimGameModel(SimGameState simGameState) {
		sgState = simGameState;
	}

	public KeyListner getKeyInputStackByFirst() {
		return sgState.getKeyInputStackFirst();
	}

	public void removeKeyInputStackByField() {
		while (!(sgState.getKeyInputStackFirst() instanceof Field)) {
			sgState.removeKeyInputStackFirst();
		}
	}
	public void removeRendererArrayBySubInfoWindow() {
		while (!(sgState.getRendererArrayEnd() instanceof SubInfoWindow)) {
			sgState.removeRendererArrayEnd();
		}
	}

	public void pushKeyInputStack(KeyListner keyListner) {
		sgState.keyInputStackPush(keyListner);
	}

	public void removeKeyInputStackFirst() {
		sgState.removeKeyInputStackFirst();
		sgState.getKeyInput().reset();
	}

	public void addRendererArray(Renderer renderer) {
		sgState.rendererArrayAdd(renderer);
	}

	public void removeRendererArrayEnd() {
		sgState.removeRendererArrayEnd();
	}

	public void setReadFilePath(String sectionPath, String subStoryPath,
			int sectionNum, int subStoryNum) {
			this.sectionPath = sectionPath;
			this.subStoryPath = subStoryPath;
			this.sectionNum = sectionNum;
			this.subStoryNum = subStoryNum;
	}

	public void init() {
		world = new World(this);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		world.update(container, game, delta);
	}

	public String getStoriesFolder() {
		return storiesFolder;
	}

	public void nextState() {
		sgState.nextState();
	}

	public void returnGameStartState() {
		sgState.returnGameStartState();
	}

	//BGMの切り替えを行うメソッド(TalkViewに呼び出される)--------------------------------------
	public void changeBGM(String bgmFilePath) {
		sgState.changeBGM(bgmFilePath);
	}

	public Font getFont() {
		return sgState.getFont();
	}
}
