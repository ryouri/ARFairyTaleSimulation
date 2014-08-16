/*

package arcircleftsim.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Save {

	try {
		// 出力先を作成する
		File newfile = new File("savedata/saveFile.txt");
		FileWriter fw = new FileWriter("savedata/saveFile.txt", false);
		PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

		// 内容を指定する
		if (gender == MALE) {
			pw.println("PLAYER,0");
		} else if (gender == FEMALE) {
			pw.println("PLAYER,1");
		}
		pw.close();

		// 終了メッセージを画面に出力する
		System.out.println("出力が完了しました。");

	} catch (IOException ex) {
		// 例外時処理
		ex.printStackTrace();
	}


}
*/