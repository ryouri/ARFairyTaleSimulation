package arcircle.ftsim.main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.JapaneseFont.JapaneseFontGenerater;
import arcircle.ftsim.state.GameStartState;
import arcircle.ftsim.state.InputNameState;
import arcircle.ftsim.state.SelectGenderState;
import arcircle.ftsim.state.SimGameState;
import arcircle.ftsim.state.StateConst;
import arcircle.ftsim.state.TalkState;

public class FTSimulationGame extends StateBasedGame {
	public static final int WIDTH  = 1120;
	public static final int HEIGHT = 640;
	public static final int FPS = 60;
	public static final String GAMENAME = "FairyTaleSimulation";

	public static UnicodeFont font;
	public static UnicodeFont InputNamefont;

	public FTSimulationGame(String name) {
		super(name);
		this.addState(new GameStartState(StateConst.GAME_START));
		this.addState(new SelectGenderState(StateConst.SELECT_GENDER));
		this.addState(new InputNameState(StateConst.INPUT_NAME));
		this.addState(new SimGameState(StateConst.SIM_GAME));
		this.addState(new TalkState(StateConst.TALK));
	}

	public static String Savename;


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.getState(StateConst.GAME_START).init(container, this);
		this.getState(StateConst.SELECT_GENDER).init(container, this);
		this.getState(StateConst.INPUT_NAME).init(container, this);
		this.getState(StateConst.SIM_GAME).init(container, this);
		this.getState(StateConst.TALK).init(container, this);

		//コンストラクタで実行するとエラー発生するよー
		font = JapaneseFontGenerater.generateFont(24, false, false, null);
		InputNamefont = JapaneseFontGenerater.generateFont(80, false, false, null);
	}
}
