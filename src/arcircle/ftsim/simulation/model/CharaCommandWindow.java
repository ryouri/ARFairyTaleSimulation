package arcircle.ftsim.simulation.model;

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
import arcircle.ftsim.state.simgame.SimGameModel;

public class CharaCommandWindow implements KeyListner, Renderer {
	SimGameModel sgModel;
	Field field;
	int windowX;
	int windowY;

	Image[] windowImage;
	int commandNum;



	public CharaCommandWindow(SimGameModel sgModel, Field field, int windowX,
			int windowY, int commandNum) {
		this.sgModel = sgModel;
		this.field = field;
		this.windowX = windowX;
		this.windowY = windowY;
		this.commandNum = commandNum;

		this.windowImage = new Image[3];
		try {
			this.windowImage[0] = new Image("image/commandWindow/Window_top.png");
			this.windowImage[1] = new Image("image/commandWindow/Window_middle.png");
			this.windowImage[2] = new Image("image/commandWindow/Window_bottom.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		renderCommand(g, commandNum);
	}

	public static int WINDOW_FRAME = 14;
	public static int WINDOW_T_B_HEIGHT = 46;
	public static int WINDOW_MIDDLE_HEIGHT = 32;

	public void renderCommand(Graphics g, int commandNum) {
		Color color = new Color(1, 1, 1, 0.75f);
		//一番上を描画
		g.drawImage(windowImage[0],
				windowX,
				windowY,
				color);
		//コマンドが1つなら
		if (commandNum == 1) {
			g.drawImage(windowImage[2],
					windowX,
					windowY + 14,
					color);
		} else {
			//コマンドが2つ以上なら
			//2つの場合はすぐにBOTTOMを描画する
			int i;
			for (i = 0; i < commandNum - 2; i++) {
				g.drawImage(windowImage[1],
						windowX,
						windowY + WINDOW_T_B_HEIGHT + i * WINDOW_MIDDLE_HEIGHT,
						color);
			}
			g.drawImage(windowImage[2],
					windowX,
					windowY + WINDOW_T_B_HEIGHT + i * WINDOW_MIDDLE_HEIGHT,
					color);
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
