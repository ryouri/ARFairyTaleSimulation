package arcircle.ftsim.state.loadsave;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.LoadSaveState;

public class LoadSaveView implements Renderer{
	LoadSaveModel lsModel;
	private LoadSaveState lsState;

	public LoadSaveView(LoadSaveModel lsModel, LoadSaveState lsState){
		super();
		this.lsModel = lsModel;
		this.lsState = lsState;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		// 選択してくださいのメッセージ
		g.setColor(Color.black);
		g.setFont(lsState.getFont());
		int messageWidth = lsState.getFont().getWidth(lsModel.message);
		g.drawString(lsModel.message,
				(FTSimulationGame.WIDTH - messageWidth) / 2, 100);

		// セーブデータの一覧と選択
		int show_max = 10;
		int step_height = 30;
		g.setColor(Color.blue);
		g.setFont(lsState.getFont());
		for (int i = 0; i < show_max && i < lsState.files.length; i++){
			String message = lsState.files[i].getName();
			messageWidth = lsState.getFont().getWidth(message);
			g.drawString(message,
					(FTSimulationGame.WIDTH - messageWidth) / 2, 120 + step_height * (i + 1));
		}

		// 枠の表示
		g.setColor(Color.red);
		int messageHeight = step_height;
		g.drawRect((FTSimulationGame.WIDTH - messageWidth) / 2, 121 + step_height * (lsState.selected + 1), messageWidth, messageHeight);
	}
}
