package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class AttackCommand extends Command {

	public AttackCommand(String name, SimGameModel sgModel, int windowX,
			int windowY) {
		super(name, sgModel, windowX, windowY);
	}

	@Override
	public void pushed(Field field, Chara chara) {
	}
}
