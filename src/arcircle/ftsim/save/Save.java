package arcircle.ftsim.save;

import java.util.ArrayList;
import java.util.HashMap;

import arcircle.ftsim.simulation.chara.Status;

/**
 * キャラクターの持っている武器が保存されないよ！
 *
 */
public class Save {
	public static final String PLAYER = "PLAYER";
	public static final String CHARA = "CHARA";
	public static final String SWITCH = "SWITCH";
	public static final String CLEAR = "CLEAR";
	public static final String NOWSTAGE = "NOWSTAGE";

	private Status player;
	//charaFolderName, status
	private HashMap<String, Status> charaStatusMap;
	private ArrayList<Switch> switchArray;
	//クリアした章（FolderName）の名前を保存する
	private ArrayList<String> clearStoryNameArray;
	private NowStage nowStage;

	public Save() {
		super();
		this.player = new Status();
		this.charaStatusMap = new HashMap<String, Status>();
		this.switchArray = new ArrayList<Switch>();
		this.clearStoryNameArray = new ArrayList<String>();
		this.nowStage = new NowStage();
	}

	public Status getPlayer() {
		return player;
	}

	public void setPlayer(Status player) {
		this.player = player;
	}

	public HashMap<String, Status> getCharaArray() {
		return charaStatusMap;
	}

	public Status getCharaStatus(String folderName) {
		if (charaStatusMap.containsKey(folderName)) {
			return charaStatusMap.get(folderName);
		}

		return null;
	}

	public void putCharaStatus(String folderName, Status charaStatus) {
		charaStatusMap.put(folderName, charaStatus);
	}

	public ArrayList<Switch> getSwitchArray() {
		return switchArray;
	}

	public ArrayList<String> getClearStoryNameArray() {
		return clearStoryNameArray;
	}

	public void clearNowStage() {
		clearStoryNameArray.add(nowStage.storyName);
	}

	public NowStage getNowStage() {
		return nowStage;
	}

	public void setNowStage(NowStage nowStage) {
		this.nowStage = nowStage;
	}
}
