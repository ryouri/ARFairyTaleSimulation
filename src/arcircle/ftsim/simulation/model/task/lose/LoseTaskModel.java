package arcircle.ftsim.simulation.model.task.lose;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.simulation.model.task.LoseTask;

public class LoseTaskModel implements KeyListner {
	private LoseTask lt;
	float alpha;
	boolean isActive;
	String messageGameover;
	String messageRestart;
	Image back;

	public LoseTaskModel(LoseTask lt) {
		this.lt = lt;
		String path = "./image/";
		alpha = 0f;
		isActive = false;
		messageGameover = "Game Over";
		messageRestart = "タイトルへ戻る";
		try {
			back = new Image(path + "gameover.png");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		if (isActive && keyInput.isKeyDown(Input.KEY_Z)) {
			lt.returnTitle();
		}
	}

}
