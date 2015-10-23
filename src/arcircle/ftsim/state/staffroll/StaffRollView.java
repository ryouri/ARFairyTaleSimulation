package arcircle.ftsim.state.staffroll;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.StaffRollState;

public class StaffRollView implements Renderer {
	StaffRollModel srModel;
	private StaffRollState srState;

	public StaffRollView(StaffRollModel srModel, StaffRollState srState){
		super();
		this.srModel = srModel;
		this.srState = srState;
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.white);
		g.setFont(srState.getFont());

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(srModel.getTextFile()));
		} catch (FileNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		String message;
		int h = srModel.output_height;
		try {
			while((message = br.readLine()) != null) {
				int messageWidth = srState.getFont().getWidth(message);
				g.drawString(message,
						(FTSimulationGame.WIDTH - messageWidth) / 2, h);
				h += 30;
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		if (h > FTSimulationGame.HEIGHT /2) {
			srModel.output_height -= 1;
		}
	}

}
