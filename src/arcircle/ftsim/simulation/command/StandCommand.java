package arcircle.ftsim.simulation.command;

import arcircle.ftsim.state.simgame.SimGameModel;

public class StandCommand extends Command {

	public StandCommand(String name, SimGameModel sgModel, int windowX,
			int windowY) {
		super(name, sgModel, windowX, windowY);
	}

}
