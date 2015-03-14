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
import arcircle.ftsim.simulation.command.Command;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

abstract class CommandWindow implements KeyListner, Renderer {
	protected SimGameModel sgModel;
	protected Field field;

	protected int windowX;
	protected int windowY;

	protected int cursorViewPosX;
	protected int cursorViewPosY;

	protected Image[] windowImage;
	protected int commandNum;
	protected Chara chara;

	protected boolean[] commandFlagArray;

	protected ArrayList<Command> commandList;

	public int cursorY;

	protected  boolean isVisible;

	public CommandWindow(SimGameModel sgModel, Field field, Chara chara) {
		this.sgModel = sgModel;
		this.field = field;
		this.windowX = 0;
		this.windowY = 0;
		this.setVisible(true);
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
		//動いていれば移動コマンドは利用できない
		if (this.chara != null && chara.isMoved()) {
			this.commandFlagArray[0] = false;
		} else {
			this.commandFlagArray[0] = true;
		}
		this.commandFlagArray[5] = true;

		calcWindowPosition();
		calcCommandList();
	}

	abstract protected void calcCommandList();

	private void calcWindowPosition() {
		// まずはcommandを表示する位置を決定する
		// CharaCommandWindowはCursorの
		// 左上(-1, -1)or右上(1, -1)or右下(1, 1)or左下(-1, 1)に表示
		cursorViewPosX = 1;
		cursorViewPosY = 1;
		if (field.getCursor().x < 10) {
			cursorViewPosX = 1;
		}
		if (field.getCursor().y < 10) {
			cursorViewPosY = 1;
		}
		if (field.getCursor().x > field.col - 10) {
			cursorViewPosX = -1;
		}
		if (field.getCursor().y > field.row - 10) {
			cursorViewPosY = -1;
		}
		int cursorRenderX = field.getCursor().pX + field.offsetX;
		int cursorRenderY = field.getCursor().pY + field.offsetY;
		int windowX = cursorRenderX;
		int windowY = cursorRenderY;
		// 左上(-1, -1)or右上(1, -1)or右下(1, 1)or左下(-1, 1)に表示
		// 右下(1, 1)に表示
		if (cursorViewPosX == 1 && cursorViewPosY == 1) {
			windowX = cursorRenderX + 40;
			windowY = cursorRenderY;
		}
		// 左下(-1, 1)に表示
		if (cursorViewPosX == -1 && cursorViewPosY == 1) {
			windowX = cursorRenderX - 128;
			windowY = cursorRenderY;
		}
		// 左上(-1, -1)に表示
		if (cursorViewPosX == -1 && cursorViewPosY == -1) {
			windowX = cursorRenderX - 128;
			windowY = cursorRenderY - WINDOW_T_B_HEIGHT * (commandList.size() - 1);
		}
		// 右上(1, -1)に表示
		if (cursorViewPosX == 1 && cursorViewPosY == -1) {
			windowX = cursorRenderX + 40;
			windowY = cursorRenderY - WINDOW_T_B_HEIGHT * (commandList.size() - 1);
		}

		this.windowX = windowX;
		this.windowY = windowY;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (isVisible() == false) {
			return;
		}

		renderCommand(g, commandList.size());
		renderRect(g);
	}

	public static int WINDOW_FRAME = 14;
	public static int WINDOW_T_B_HEIGHT = 46;
	public static int WINDOW_MIDDLE_HEIGHT = 32;

	public static int FONT_INTERVAL = 4;
	public static int FONT_SIZE = 24;

	public void renderCommand(Graphics g, int commandNum) {
		g.setFont(FTSimulationGame.font);
		Color color = new Color(1, 1, 1, 0.7f);
		//一番上を描画
		g.drawImage(windowImage[0],
				windowX,
				windowY,
				color);
		g.drawString(commandList.get(0).name,
				windowX + WINDOW_FRAME,
				windowY + WINDOW_FRAME - FONT_INTERVAL);
		//コマンドが1つなら
		if (commandNum == 1) {
			g.drawImage(windowImage[2],
					windowX,
					windowY + WINDOW_FRAME,
					color);
			g.drawString(commandList.get(0).name,
					windowX + WINDOW_FRAME,
					windowY + WINDOW_FRAME - FONT_INTERVAL);
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
						windowY + WINDOW_FRAME + (i + 1) * WINDOW_MIDDLE_HEIGHT - FONT_INTERVAL);
			}
			g.drawImage(windowImage[2],
					windowX,
					windowY + WINDOW_T_B_HEIGHT + i * WINDOW_MIDDLE_HEIGHT,
					color);
			g.drawString(commandList.get(i + 1).name,
					windowX + WINDOW_FRAME,
					windowY + WINDOW_FRAME + (i + 1) * WINDOW_MIDDLE_HEIGHT - FONT_INTERVAL);
		}
	}

	private void renderRect(Graphics g) {
		g.drawRect(
				windowX + WINDOW_FRAME,
				windowY + WINDOW_FRAME + (cursorY) * WINDOW_MIDDLE_HEIGHT - FONT_INTERVAL / 2,
				windowImage[0].getWidth() - WINDOW_FRAME * 2 - FONT_INTERVAL / 2,
				WINDOW_MIDDLE_HEIGHT);
	}

	protected void pushXKey() {
		sgModel.removeKeyInputStackFirst();
		sgModel.removeRendererArrayEnd();
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//キャンセルキーが押されたとき
		if (keyInput.isKeyDown(Input.KEY_X) || keyInput.isKeyPressed(Input.KEY_X)) {
			field.getSoundManager().playSound(SoundManager.SOUND_CANCEL);
			pushXKey();
			return;
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			cursorY++;
			if (cursorY >= commandList.size()) {
				cursorY = 0;
			}
			return;
		}
		if (keyInput.isKeyDown(Input.KEY_UP)) {
			cursorY--;
			if (cursorY < 0) {
				cursorY = commandList.size() - 1;
			}
			return;
		}
		if (keyInput.isKeyDown(Input.KEY_Z)) {
			field.getSoundManager().playSound(SoundManager.SOUND_DECISION);

			Command command = commandList.get(cursorY);
			int pushedState = command.pushed(field, chara);
			if (pushedState == Command.PUSHED_NOT_VISIBLE) {
				setVisible(false);
			}
			return;
		}
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
