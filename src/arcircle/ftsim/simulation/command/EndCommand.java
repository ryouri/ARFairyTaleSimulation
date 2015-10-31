package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class EndCommand extends Command {

	public EndCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
	}

	@Override
	public int pushed(Field field, Chara chara) {
		sgModel.removeKeyInputStackByField();
		sgModel.removeRendererArrayBySubInfoWindow();

		field.getTaskManager().addTurnEndTask(field.getCharacters(), Chara.CAMP_FRIEND);

		return Command.PUSHED_NONE;
	}

}
