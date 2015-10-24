package arcircle.ftsim.state.gamestart;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.GameStartState;

public class GameStartView implements Renderer{
	GameStartModel gsModel;
	private GameStartState gsState;

	private Image title;	//背景画像

	public GameStartView(GameStartModel gsModel, GameStartState gsState) {
		super();
		this.gsModel = gsModel;
		this.gsState = gsState;
		try{
			title = new Image("./Image/title.png");	//背景画像の読み込み
		}catch (SlickException ex) {
	        ex.printStackTrace();
	    }
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);
		g.drawImage(title, 0, 0);

		g.setFont(gsState.getFont());
		g.setColor(Color.blue);

		// セーブデータがなければ開始ボタンのみ
		if (gsModel.onlyStart) {
			// 開始
			int messageWidth = gsState.getFont().getWidth(gsModel.start);
			g.drawString(gsModel.start,
					(FTSimulationGame.WIDTH - messageWidth) / 2, 400);
			// 項目選択の枠を表示
			g.setColor(Color.red);
			int messageHeight = gsState.getFont().getHeight(gsModel.start);
			g.drawRect((FTSimulationGame.WIDTH - messageWidth) / 2, 401, messageWidth, messageHeight);
		} else {
			// 開始
			int messageWidth = gsState.getFont().getWidth(gsModel.start);
			g.drawString(gsModel.start,
					(FTSimulationGame.WIDTH - messageWidth) / 2 - 50, 400);
			// ロード
			messageWidth = gsState.getFont().getWidth(gsModel.load);
			g.drawString(gsModel.load,
					(FTSimulationGame.WIDTH - messageWidth) / 2 + 50, 400);


			// 項目選択の枠を表示
			g.setColor(Color.red);
			// 開始
			if (gsModel.state == 0){
				messageWidth = gsState.getFont().getWidth(gsModel.start);
				int messageHeight = gsState.getFont().getHeight(gsModel.start);
				g.drawRect((FTSimulationGame.WIDTH - messageWidth) / 2 - 50, 401, messageWidth, messageHeight);
			}
			// ロード
			if (gsModel.state == 1){
				messageWidth = gsState.getFont().getWidth(gsModel.load);
				int messageHeight = gsState.getFont().getHeight(gsModel.load);
				g.drawRect((FTSimulationGame.WIDTH - messageWidth) / 2 + 50, 401, messageWidth, messageHeight);
			}
		}
	}
}
