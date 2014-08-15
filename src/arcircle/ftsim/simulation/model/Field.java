package arcircle.ftsim.simulation.model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Field implements KeyListner, Renderer {

	SimGameModel sgModel;

	public Field(SimGameModel sgModel) {
		this.sgModel = sgModel;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
	}

	@Override
	public void keyInput(KeyInput keyInput) {
	}
}
