package arcircle.ftsim.state.simgame;

import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.model.World;
import arcircle.ftsim.state.SimGameState;

public class SimGameModel{
	private SimGameState sgState;

	public World world;

	public SimGameModel(SimGameState simGameState) {
		sgState = simGameState;
		world = new World(this);
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
}
