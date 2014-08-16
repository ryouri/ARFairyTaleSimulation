package arcircle.ftsim.simulation.command;

import arcircle.ftsim.state.simgame.SimGameModel;

public class Command {
	public static final String[] commandType = {
		"いどう",   "こうげき", "ほじょ",
		"とくしゅ", "どうぐ",   "たいき"
	};

	public String name;

	SimGameModel sgModel;
	int windowX;
	int windowY;
	public Command(String name, SimGameModel sgModel, int windowX, int windowY) {
		super();
		this.name = name;
		this.sgModel = sgModel;
		this.windowX = windowX;
		this.windowY = windowY;
	}
}
