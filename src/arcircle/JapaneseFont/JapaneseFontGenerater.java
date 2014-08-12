package arcircle.JapaneseFont;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

/**
 * getFont用のクラス，日本語フォントを生成します．
 * 生成に時間(3s程度)がかかるので，受け取ったら使いまわすことを推奨．
 * ./font/migmix-1p-regular.ttfがおいてあることが前提です．
 * 自分で調べて理解できたら，プログラムに好きに変更を加えましょう．
 * 参考サイト：http://welovy.hatenablog.com/entry/2013/01/24/193859
 */
public class JapaneseFontGenerater {
	/**
	 * UnicodeFontインスタンスを返す
	 * @param size
	 *            文字サイズ
	 * @param bold
	 *            太字
	 * @param italic
	 *            斜字
	 * @param filePath
	 *            フォントファイルのパス，なければデフォルトでmigmix
	 * @return UnicodeFontのインスタンス，生成に失敗した場合はnullを返す
	 */
	public static UnicodeFont generateFont(int size, boolean bold, boolean italic,
			String filePath) {
		// メソッド呼び出しがあったときに、
		// 初めてインスタンスを生成

		UnicodeFont font = null;

		try {
			String fontFilePath = "./font/migmix-1p-regular.ttf";
			if (filePath != null) {
				fontFilePath = filePath;
			}
			font = new UnicodeFont(fontFilePath, size, bold, italic);

			font.addAsciiGlyphs();

			// Hiragana + katakanab + fullwidth punctuations
			font.addGlyphs(0x3000, 0x30ff);
			// Kanji
			font.addGlyphs(0x4e00, 0x9fc0);

			// おまじない
			font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));

			font.loadGlyphs();
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return font;
	}
}
