package arcircle.ftsim.simulation.item;

public class RangedItem extends Item {

	public int rangeType;

	public static int RANGE_NONE = -1;
	public static int RANGE_NEAR = 0;
	public static String RANGE_NEAR_STR = "RANGE_NEAR";
	public static int RANGE_FAR = 1;
	public static String RANGE_FAR_STR = "RANGE_FAR";
	public static int RANGE_NEAR_FAR = 2;
	public static String RANGE_NEAR_FAR_STR = "RANGE_NEAR_FAR";

}
