package arcircle.ftsim.save;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.state.selectstory.SelectStoryModel;

/**
 * キャラクターの持っている武器が保存されないよ！
 *
 */
public class Save implements Serializable{
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

	/**
	 * このインスタンスをsaveディレクトリに直列化する
	 */
	public void save() {
		String path = "./save/";
		// 現在日時をTimestamp型で取得
		Timestamp datetime = new Timestamp(System.currentTimeMillis());
		String dateTimeStr = new SimpleDateFormat("yyyy_MMdd_hhmmss").format(datetime);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(path + dateTimeStr + ".sav"));
			oos.writeObject(this);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
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

	public boolean[] isClearStages() {
		boolean[] isClearStage = new boolean[SelectStoryModel.STORY_NUM];	//初期値は多分false
		if(!clearStoryNameArray.isEmpty()){
			for(int i = 0 ; i < clearStoryNameArray.size() ; i++){
				if(clearStoryNameArray.get(i).equals("01_Story")){
					isClearStage[0] = true;
				}else if(clearStoryNameArray.get(i).equals("02_Story")){
					isClearStage[1] = true;
				}else if(clearStoryNameArray.get(i).equals("03_Story")){
					isClearStage[2] = true;
				}else if(clearStoryNameArray.get(i).equals("04_Story")){
					isClearStage[3] = true;
				}else if(clearStoryNameArray.get(i).equals("05_Story")){
					isClearStage[4] = true;
				}else if(clearStoryNameArray.get(i).equals("06_Story")){
					isClearStage[5] = true;
				}else if(clearStoryNameArray.get(i).equals("07_Story")){
					isClearStage[6] = true;
				}else{
					System.out.println("error_SelectStoryView_clearStage");
				}
			}
		}
		return isClearStage;
	}

	public boolean isAllCleared() {
		for (boolean flag: isClearStages()) {
			if (!flag) {
				return false;
			}
		}
		return true;
	}

	public boolean isAllClearedWOBoss() {
		boolean[] isClearStages = isClearStages();
		for (int i = 0; i < isClearStages.length; i++) {
			if (i == isClearStages.length - 1 && isClearStages[i]) {
				// ボスをクリアしていたらfalse
				return false;
			} else if (!isClearStages[i]) {
				// ボス以外でクリアしていないステージがあるならflase
				return false;
			}
		}
		return true;
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
