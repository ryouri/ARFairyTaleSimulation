package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class StandCommand extends Command {

	public StandCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
	}

	@Override
	public int pushed(Field field, Chara chara) {
		sgModel.removeKeyInputStackByField();
		sgModel.removeRendererArrayBySubInfoWindow();
		chara.setStand(true);
		chara.setMoving(false);
		chara.setMoved(false);

		return Command.PUSHED_NONE;
	}
}
