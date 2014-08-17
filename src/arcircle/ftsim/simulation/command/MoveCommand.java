package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.algorithm.range.CalculateMoveRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class MoveCommand extends Command {
	CalculateMoveRange cmRange;

	public MoveCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
	}

	@Override
	public void pushed(Field field, Chara chara) {
		cmRange = new CalculateMoveRange(field, chara);
		cmRange.calculateRange();
	}
}
