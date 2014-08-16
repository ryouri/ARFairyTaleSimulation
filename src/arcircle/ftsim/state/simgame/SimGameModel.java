package arcircle.ftsim.state.simgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
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

	public void keyInputStackPush(KeyListner keyListner) {
		sgState.keyInputStackPush(keyListner);
	}

	public void keyInputStackRemoveFirst() {
		sgState.keyInputStackRemoveFirst();
	}

	public void rendererArrayAdd(Renderer renderer) {
		sgState.rendererArrayAdd(renderer);
	}

	public void rendererArrayRemoveEnd() {
		sgState.rendererArrayRemoveEnd();
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
}
