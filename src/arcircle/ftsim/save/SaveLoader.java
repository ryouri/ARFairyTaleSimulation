package arcircle.ftsim.save;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SaveLoader {

	/**
	 * 指定されたファイルからSaveクラスのインスタンスを返す
	 * @param path 直列化して保存されたバイナリファイルのパス
	 * @return Saveインスタンス
	 */
	public static Save load(String path) {
		ObjectInputStream ois = null;
		Save obj = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(path));
			obj = (Save)ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return obj;
	}

}
