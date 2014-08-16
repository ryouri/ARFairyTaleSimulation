package arcircle.ftsim.simulation.model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.state.simgame.SimGameModel;

public class World {
	Field field;

	SimGameModel sgModel;

	SubInfoWindow subInfoWindow;


	public World(SimGameModel simGameModel) {
		this.sgModel = simGameModel;
		this.field = new Field(simGameModel);

		String filePath = sgModel.getStoriesFolder() + "/"
				+ sgModel.sectionPath + "/"
				+ sgModel.subStoryPath + "/";
		field.init(filePath + "map.dat", filePath + "mapchip.txt");

		this.subInfoWindow = new SubInfoWindow();

		sgModel.keyInputStackPush(field);
		sgModel.rendererArrayAdd(field);
		sgModel.rendererArrayAdd(subInfoWindow);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		field.update(container, game, delta);
	}
}
