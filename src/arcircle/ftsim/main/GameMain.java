package arcircle.ftsim.main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * v0.0
 * ゲームの開始，生成部分
 * FairyTaleSimulationをFTSimulationと略す
 * @author misawa
 *
 */
public class GameMain {

	public static void main(String[] args) {
        System.out.println("Hello World\nI am Ryouri");

        //エントリーポイント
        AppGameContainer app;
        try{
            app = new AppGameContainer(new FTSimulationGame(FTSimulationGame.GAMENAME));
            app.setDisplayMode(FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT, false);
            app.setTargetFrameRate(FTSimulationGame.FPS);
            app.setShowFPS(false);
            app.start();
        }catch(SlickException e){
            e.printStackTrace();
        }
	}

}
