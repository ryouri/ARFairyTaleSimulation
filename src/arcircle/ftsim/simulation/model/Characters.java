package arcircle.ftsim.simulation.model;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.simgame.SimGameModel;

public class Characters implements Renderer {
	private SimGameModel sgModel;

	private int row;
	private int col;

	public static String charactersFolderPath = "Characters/";

	public Characters(SimGameModel sgModel, int row, int col) {
		this.sgModel = sgModel;
		this.row = row;
		this.col = col;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {

	}

	public void init() {
		String charaPath = sgModel.getStoriesFolder() + "/"
				+ charactersFolderPath;
		File dir = new File(charaPath);
		String[] files = dir.list();
		for (String str : files) {
			System.out.println(str);
		}
	}
}
