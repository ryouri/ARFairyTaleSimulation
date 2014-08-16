package arcircle.ftsim.simulation.model;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.state.simgame.SimGameModel;

public class World {
	Field field;

	SimGameModel sgModel;

	SubInfoWindow subInfoWindow;

	public HashMap<String, Item> itemList;

	public World(SimGameModel simGameModel) {
		itemList = new HashMap<String, Item>();
		loadItem();

		this.sgModel = simGameModel;
		this.field = new Field(simGameModel);

		String subStoryFolderPath = sgModel.getStoriesFolder() + "/"
				+ sgModel.sectionPath + "/"
				+ sgModel.subStoryPath + "/";
		field.init(subStoryFolderPath);

		this.subInfoWindow = new SubInfoWindow();

		sgModel.keyInputStackPush(field);
		sgModel.rendererArrayAdd(field);
		sgModel.rendererArrayAdd(subInfoWindow);
	}

	public void loadItem() {
		
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		field.update(container, game, delta);
	}
}
