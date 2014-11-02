package arcircle.ftsim.simulation.end;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EndConditionManager {
	ArrayList<EndCondition> winEndConditionArray;
	ArrayList<EndCondition> loseEndConditionArray;

	public EndConditionManager() {
		winEndConditionArray = new ArrayList<EndCondition>();
		loseEndConditionArray = new ArrayList<EndCondition>();
	}

	public void loadEndConditionTxt(String endConditionTxtPath) {
		try {
			File file = new File(endConditionTxtPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String endCondStr;

			while ((endCondStr = br.readLine()) != null) {
				if (endCondStr.length() == 0) {
					continue;
				}

				EndCondition endCondition = loadEndCondition(endCondStr);
				if (endCondition != null) {
					if (endCondition.endType == EndCondition.TYPE_WIN) {
						winEndConditionArray.add(endCondition);
					} else if (endCondition.endType == EndCondition.TYPE_LOSE) {
						loseEndConditionArray.add(endCondition);
					}
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	private EndCondition loadEndCondition(String endCondStr) {
		String[] endCondStrs = endCondStr.split(",");
		EndCondition endCondition = new EndCondition();
		if (endCondStrs[0].equals(EndCondition.WIN)) {
			endCondition.endType = EndCondition.TYPE_WIN;
		} else if (endCondStrs[0].equals(EndCondition.LOSE)) {
			endCondition.endType = EndCondition.TYPE_LOSE;
		}
		endCondition.EventID = endCondStrs[1];
		endCondition.nextDo = endCondStrs[2];
		endCondition.viewStr = endCondStrs[3];

		return endCondition;
	}
}
