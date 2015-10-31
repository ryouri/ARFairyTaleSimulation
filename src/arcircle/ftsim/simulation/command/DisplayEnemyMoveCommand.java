package arcircle.ftsim.simulation.command;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

public class DisplayEnemyMoveCommand implements KeyListner, Renderer {
	MoveCommand moveCommand;

	SimGameModel sgModel;
	protected Chara chara;
	protected Field field;

	public DisplayEnemyMoveCommand(SimGameModel sgModel, Field field, Chara chara) {
		this.sgModel = sgModel;
		this.field = field;
		this.chara = chara;
		moveCommand = new MoveCommand("", sgModel, null);
		moveCommand.init(field, chara);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		moveCommand.render(container, game, g);
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//キャンセルキーが押されたとき
		if (keyInput.isKeyDown(Input.KEY_Z) || keyInput.isKeyDown(Input.KEY_X)) {
			field.getSoundManager().playSound(SoundManager.SOUND_CANCEL);
			sgModel.removeKeyInputStackFirst();
			sgModel.removeRendererArrayEnd();

			return;
		}
	}

}
