package arcircle.ftsim.save;

import java.util.ArrayList;

import arcircle.ftsim.simulation.chara.Status;

public class Save {
	public static final String PLAYER = "PLAYER";
	public static final String CHARA = "CHARA";
	public static final String SWITCH = "SWITCH";
	public static final String CLEAR = "CLEAR";
	public static final String NOWSTAGE = "NOWSTAGE";

	private Status player;
	private ArrayList<Status> charaArray;
	private ArrayList<Switch> switchArray;
	private ArrayList<Clear> clearArray;
	private NowStage nowStage;

	public Save() {
		super();
		this.player = null;
		this.charaArray = new ArrayList<Status>();
		this.switchArray = new ArrayList<Switch>();
		this.clearArray = new ArrayList<Clear>();
		this.nowStage = null;
	}

	public Status getPlayer() {
		return player;
	}

	public void setPlayer(Status player) {
		this.player = player;
	}

	public ArrayList<Status> getCharaArray() {
		return charaArray;
	}

	public ArrayList<Switch> getSwitchArray() {
		return switchArray;
	}

	public ArrayList<Clear> getClearArray() {
		return clearArray;
	}

	public NowStage getNowStage() {
		return nowStage;
	}

	public void setNowStage(NowStage nowStage) {
		this.nowStage = nowStage;
	}
}
