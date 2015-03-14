package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

public class MovedCommandWindow extends CharaCommandWindow {


	private MoveCommand moveCommand;

	public MovedCommandWindow(SimGameModel sgModel, Field field, Chara chara, MoveCommand moveCommand) {
		super(sgModel, field, chara);
		this.moveCommand = moveCommand;
	}

	@Override
	protected void pushXKey() {
		field.getSoundManager().playSound(SoundManager.SOUND_CANCEL);
		chara.setMoved(false);
		sgModel.removeKeyInputStackFirst();
		sgModel.removeRendererArrayEnd();
		moveCommand.setFirstPosition();
		moveCommand.setVisible(true);
	}
}
