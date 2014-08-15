package arcircle.ftsim.state.simgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.SimGameState;

public class SimGameView implements Renderer {
	SimGameModel sgModel;
	SimGameState sgState;

	public SimGameView(SimGameModel sgModel, SimGameState simGameState) {
		this.sgModel = sgModel;
		this.sgState = simGameState;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {

	}

}
