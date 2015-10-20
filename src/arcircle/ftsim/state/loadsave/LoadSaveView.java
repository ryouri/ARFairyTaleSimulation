package arcircle.ftsim.state.loadsave;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.save.Save;
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

		// 背景ウィンドウの表示
		g.drawImage(lsModel.background, (FTSimulationGame.WIDTH - lsModel.background.getWidth()) / 2
				, FTSimulationGame.HEIGHT / 7 * 4
				- lsModel.background.getHeight() / 2);


		// 選択してくださいのメッセージ
		g.setColor(Color.black);
		g.setFont(lsState.getFont());
		int messageWidth = lsState.getFont().getWidth(lsModel.message);
		g.drawString(lsModel.message,
				(FTSimulationGame.WIDTH - messageWidth) / 2, 50);

		// セーブデータの一覧と選択
		int show_max = 5;
		g.setColor(Color.blue);
		g.setFont(lsState.getFont());
		int height_offset = FTSimulationGame.HEIGHT / 7 * 4 - lsModel.background.getHeight() / 2 + 10;
		int width_offset = (FTSimulationGame.WIDTH - lsModel.background.getWidth()) / 2;
		// それぞれのセーブデータの情報を取得する
		for (int i = 0; i < show_max && i < lsState.files.length; i++){
			Save save = FTSimulationGame.save.load(lsState.files[i].getPath());
			String date = lsState.files[i].getName().replaceAll(".sav", "");
			String name = save.getPlayer().name;
			int lv = save.getPlayer().level;
			ArrayList<String> creaed = save.getClearStoryNameArray();

			// ボックスの表示
			g.drawImage(lsModel.box, width_offset, height_offset + i * lsModel.box.getHeight());
			// 情報の表示
			g.setColor(Color.black);
			g.drawString(name, width_offset + 20, height_offset + i * lsModel.box.getHeight() + 10);
			g.drawString(date, width_offset + 340, height_offset + i * lsModel.box.getHeight() + 10);
			g.drawString("Lv: " + lv, width_offset + 20, height_offset + i * lsModel.box.getHeight() + 40);

		}

		// 選択している枠の表示
		int i = lsState.selected < show_max ? lsState.selected : show_max - 1;
		g.drawImage(lsModel.box2, width_offset, height_offset + i * lsModel.box2.getHeight());


	}
}
