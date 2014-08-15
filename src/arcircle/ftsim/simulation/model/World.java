package arcircle.ftsim.simulation.model;

import arcircle.ftsim.state.simgame.SimGameModel;

public class World {
	Field field;

	SimGameModel sgModel;

	public World(SimGameModel simGameModel) {
		this.sgModel = simGameModel;
		this.field = new Field(simGameModel);

		String filePath = "Stories/"
				+ sgModel.sectionPath + "/"
				+ sgModel.subStoryPath + "/";
		field.loadMapAndMapChip(filePath + "map.dat", filePath + "mapchip.txt");

		sgModel.keyInputStackPush(field);
		sgModel.rendererArrayAdd(field);
	}
}
