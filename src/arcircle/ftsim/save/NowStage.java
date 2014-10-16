package arcircle.ftsim.save;

public class NowStage {
	public String storyName;
	public String subStoryNum;

	public static final int PROLOGUE = 0;
	public static final int EPILOGUE = 1;

	public int selectLogue;


	public NowStage(String storyName, String subStoryNum) {
		super();
		this.storyName = storyName;
		this.subStoryNum = subStoryNum;
		this.selectLogue = 0;
	}

	public NowStage() {
		super();
		this.storyName = null;
		this.subStoryNum = null;
		this.selectLogue = 0;
	}
}
