package arcircle.ftsim.simulation.model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.state.simgame.SimGameModel;

public class World {
	/**
	 * ゲームはFieldを大元に構成される
	 */
	Field field;

	/**
	 * 様々な機能にアクセスするために持っておく
	 */
	SimGameModel sgModel;


	/**
	 * 右側に表示される情報ウィンドウ
	 */
	SubInfoWindow subInfoWindow;

	public World(SimGameModel simGameModel) {
		this.sgModel = simGameModel;
		this.field = new Field(simGameModel);

		String subStoryFolderPath = sgModel.getStoriesFolder() + "/"
				+ sgModel.sectionPath + "/"
				+ sgModel.subStoryPath + "/";
		field.init(subStoryFolderPath);

		this.subInfoWindow = new SubInfoWindow(field);
		this.field.setSubInfoWindow(subInfoWindow);

		sgModel.pushKeyInputStack(field);
		sgModel.addRendererArray(field);
		sgModel.addRendererArray(subInfoWindow);
	}


	public void update(GameContainer container, StateBasedGame game, int delta) {
		field.update(container, game, delta);
	}
}
