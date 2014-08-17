package arcircle.ftsim.simulation.command;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.algorithm.range.CalculateMoveRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class MoveCommand extends Command implements KeyListner, Renderer {
	CalculateMoveRange cmRange;
	private Chara chara;
	private Field field;

	Image moveRange;
	Color moveColor;

	public MoveCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
		try {
			moveRange = new Image("image/commandWindow/moveRange.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		moveColor = new Color(1, 1, 1, 0.3f);
	}

	@Override
	public void pushed(Field field, Chara chara) {
		this.chara = chara;
		this.field = field;
		cmRange = new CalculateMoveRange(field, chara);
		cmRange.calculateRange();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (cmRange == null || field == null || chara == null) {
			return;
		}

		for (int y = field.firstTileY; y < field.lastTileY; y++) {
			for (int x = field.firstTileX; x < field.lastTileX; x++) {
				if (!cmRange.moveRange[y][x]) {
					continue;
				}

				// 各マスのタイルを描画
				g.drawImage(moveRange,
						field.tilesToPixels(x) + field.offsetX,
						field.tilesToPixels(y) + field.offsetY,
						moveColor);
			}
		}
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//キャンセルキーが押されたとき
		if (keyInput.isKeyDown(Input.KEY_X)) {
			sgModel.keyInputStackRemoveFirst();
			sgModel.rendererArrayRemoveEnd();
			return;
		}
	}
}
