package arcircle.ftsim.state.talk;

public class T_Const {
	/** テキストボックス1行の最大文字数 */
	public static final int MAX_CHARS_PER_LINE = 32;
    /** テキストボックス1ページに表示できる最大行数*/
    public static final int MAX_LINES_PER_PAGE = 5;
    /** テキストボックス1ページに表示できる最大文字数*/
    public static final int MAX_CHARS_PER_PAGE = MAX_CHARS_PER_LINE * MAX_LINES_PER_PAGE;
    /** テキストタグのテキストに格納できる最大ページ数 */
    public static final int MAX_PAGES = 50;
    /** テキストタグのテキストに格納できる最大行数 */
    public static final int MAX_LINES = MAX_PAGES * MAX_LINES_PER_PAGE;
    /** テキストタグのテキストに格納できる最大文字数 */
    public static final int MAX_CHARS = MAX_CHARS_PER_LINE * MAX_LINES;

    //LoadTalkGraphicsの全キャラ画像を保存するハッシュマップへのアクセスキー
    //ex) キャラID(002)の立ち絵へのアクセスキー="002Stand"
    /** 主人公(男) "PlayerMale" */
    public static final String PLAYER_M = "playerMale";
    /** 主人公(女) "PlayerFemale" */
    public static final String PLAYER_F = "playerFemale";
    /** 立ち絵 "Stand" */
    public static final String STAND = "Stand";
    /** 普通の顔画像 "FaceStandard" */
    public static final String FACE_ST = "FaceStandard";
    /** 笑った顔画像 "FaceLaugh" */
    public static final String FACE_LA = "FaceLaugh";
    /** 怒った顔画像 "FaceAngry" */
    public static final String FACE_AN = "FaceAngry";
    /** 苦しそうな顔画像 "FaceSuffer" */
    public static final String FACE_SU = "FaceSuffer";
    /** 名前へのキー "Name" */
    public static final String NAME = "Name";
    /** 透明な立ち絵用画像 "NoStand" */
    public static final String NO_STAND = "NoStand";
    /** 透明な顔画像 "NoFace" */
    public static final String NO_FACE = "NoFace";

    //タグの名前
    /** スピークタグ "SPEAK" */
    public static final String SPEAK = "SPEAK";
    /** BGM変更タグ "CHANGE_BGM" */
    public static final String CHANGE_BGM = "CHANGEBGM";


}
