package arcircle.ftsim.simulation.model;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.command.AttackCommand;
import arcircle.ftsim.simulation.command.Command;
import arcircle.ftsim.simulation.command.MoveCommand;
import arcircle.ftsim.simulation.command.StandCommand;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.state.simgame.SimGameModel;

public class CharaCommandWindow implements KeyListner, Renderer {
	SimGameModel sgModel;
	Field field;
	int windowX;
	int windowY;

	Image[] windowImage;
	int commandNum;
	Chara chara;

	boolean[] commandFlagArray;

	ArrayList<Command> commandList;

	public CharaCommandWindow(SimGameModel sgModel, Field field, int windowX,
			int windowY, Chara chara) {
		this.sgModel = sgModel;
		this.field = field;
		this.windowX = windowX;
		this.windowY = windowY;
		this.chara = chara;
		this.commandList = new ArrayList<Command>();

		this.windowImage = new Image[3];
		try {
			this.windowImage[0] = new Image("image/commandWindow/Window_top.png");
			this.windowImage[1] = new Image("image/commandWindow/Window_middle.png");
			this.windowImage[2] = new Image("image/commandWindow/Window_bottom.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		this.commandFlagArray = new boolean[Command.commandType.length];
		this.commandFlagArray[0] = true;
		this.commandFlagArray[5] = true;

		calcCommandList();
	}

	/**
	 * TODO:攻撃範囲によるコマンドの限定，とくしゅコマンドの判定などが未実装
	 *
	 */
	private void calcCommandList() {
		for (Item item : chara.itemList) {
			if (item.type == Item.TYPE_WEAPON) {
				commandFlagArray[1] = true;
			}
			if (item.type == Item.TYPE_SUPPORT) {
				commandFlagArray[2] = true;
			}
			if (item.type == Item.TYPE_USE) {
				commandFlagArray[4] = true;
			}
		}

		for (int i = 0; i < commandFlagArray.length; i++) {
			if (!commandFlagArray[i]) {
				continue;
			}

			if (Command.commandType[i].equals("いどう")) {
				Command command =
						new MoveCommand(Command.commandType[i], sgModel, windowX, windowY);
				commandList.add(command);
			}
			if (Command.commandType[i].equals("こうげき")) {
				Command command =
						new AttackCommand(Command.commandType[i], sgModel, windowX, windowY);
				commandList.add(command);
			}
			if (Command.commandType[i].equals("たいき")) {
				Command command =
						new StandCommand(Command.commandType[i], sgModel, windowX, windowY);
				commandList.add(command);
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		renderCommand(g, commandList.size());
	}

	public static int WINDOW_FRAME = 14;
	public static int WINDOW_T_B_HEIGHT = 46;
	public static int WINDOW_MIDDLE_HEIGHT = 32;

	public static int FONT_INTERVAL = 4;

	public void renderCommand(Graphics g, int commandNum) {
		g.setFont(FTSimulationGame.font);
		Color color = new Color(1, 1, 1, 0.75f);
		//一番上を描画
		g.drawImage(windowImage[0],
				windowX,
				windowY,
				color);
		g.drawString(commandList.get(0).name,
				windowX + WINDOW_FRAME,
				windowY + WINDOW_FRAME - 4);
		//コマンドが1つなら
		if (commandNum == 1) {
			g.drawImage(windowImage[2],
					windowX,
					windowY + WINDOW_FRAME,
					color);
			g.drawString(commandList.get(0).name,
					windowX + WINDOW_FRAME,
					windowY + WINDOW_FRAME - 4);
		} else {
			//コマンドが2つ以上なら
			//2つの場合はすぐにBOTTOMを描画する
			int i;
			for (i = 0; i < commandNum - 2; i++) {
				g.drawImage(windowImage[1],
						windowX,
						windowY + WINDOW_T_B_HEIGHT + i * WINDOW_MIDDLE_HEIGHT,
						color);
				g.drawString(commandList.get(i + 1).name,
						windowX + WINDOW_FRAME,
						windowY + WINDOW_FRAME + (i + 1) * WINDOW_MIDDLE_HEIGHT - 4);
			}
			g.drawImage(windowImage[2],
					windowX,
					windowY + WINDOW_T_B_HEIGHT + i * WINDOW_MIDDLE_HEIGHT,
					color);
			g.drawString(commandList.get(i + 1).name,
					windowX + WINDOW_FRAME,
					windowY + WINDOW_FRAME + (i + 1) * WINDOW_MIDDLE_HEIGHT - 4);
		}
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//キャンセルキーが押されたとき
		if (keyInput.isKeyDown(Input.KEY_X) || keyInput.isKeyPressed(Input.KEY_X)) {
			sgModel.keyInputStackRemoveFirst();
			sgModel.rendererArrayRemoveEnd();
		}
	}
}
