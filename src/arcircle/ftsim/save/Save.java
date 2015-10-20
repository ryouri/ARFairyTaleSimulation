package arcircle.ftsim.save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import arcircle.ftsim.simulation.chara.Status;

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

	/**
	 * 指定されたファイルからこのSaveクラスのインスタンスを返す
	 * @param path 直列化して保存されたバイナリファイルのパス
	 * @return Saveインスタンス
	 */
	public Save load(String path) {
		ObjectInputStream ois;
		Save obj = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(path));
			obj = (Save)ois.readObject();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return obj;
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

	public NowStage getNowStage() {
		return nowStage;
	}

	public void setNowStage(NowStage nowStage) {
		this.nowStage = nowStage;
	}
}
