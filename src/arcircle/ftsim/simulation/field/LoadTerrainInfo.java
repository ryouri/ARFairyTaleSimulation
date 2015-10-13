package arcircle.ftsim.simulation.field;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import arcircle.ftsim.state.simgame.SimGameModel;

public class LoadTerrainInfo {
	private static void loadAutoTile(TerrainInfoSupplier terrainInfoSupplier, String AutoTileTxtPath) throws IOException {
		File file = new File(AutoTileTxtPath);
		BufferedReader br = new BufferedReader(new FileReader(file));

		String autoTileTerrainLine = br.readLine();

		while ((autoTileTerrainLine = br.readLine()) != null) {
			if (autoTileTerrainLine.length() == 0) {
				continue;
			}

			String[] terrainAutoTileStrs = autoTileTerrainLine.split(",");
			if (terrainAutoTileStrs.length != 2) {
				continue;
			}

			int autoTileNum = Integer.parseInt(terrainAutoTileStrs[0]);
			Terrain autoTileTerrain = terrainInfoSupplier.terrainMap.get(terrainAutoTileStrs[1]);
			terrainInfoSupplier.autoTileTerrainMap.put(autoTileNum, autoTileTerrain);
		}
	}

	public static void loadTerrainInfo(TerrainInfoSupplier terrainInfoSupplier) {
		// マップチップ読み込み TODO;マジックナンバー！！！
		String terrainListPath = SimGameModel.storiesFolder + "/TerrainInfo.txt";
		try {
			File file = new File(terrainListPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String terrainStr;

			terrainStr = br.readLine();
			if (terrainStr != null) {
				String[] terrainStrs = terrainStr.split(",");
				terrainInfoSupplier.terrainChipWidth = Integer.valueOf(terrainStrs[0]);
				terrainInfoSupplier.terrainChipHeight = Integer.valueOf(terrainStrs[1]);
			}

			while ((terrainStr = br.readLine()) != null) {
				if (terrainStr.length() == 0) {
					continue;
				}

				Terrain terrain = loadTerrain(terrainStr);
				if (terrain != null) {
					terrainInfoSupplier.terrainMap.put(terrain.terrainName, terrain);
					terrainInfoSupplier.terrainArray.add(terrain);
				}
			}

			br.close();

			loadAutoTile(terrainInfoSupplier,
				SimGameModel.storiesFolder + "/TerrainInfoAutoTile.txt");
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	//地形名,マップチップの開始座標x,同左y,回避,防御
	//平地,0,0,0,0
	public static Terrain loadTerrain(String terrainStr) {
		String[] terrainStrs = terrainStr.split(",");

		if (terrainStrs.length <= 4) {
			return null;
		}

		Terrain terrain = new Terrain();
		terrain.terrainName = terrainStrs[0];
		System.out.println(terrain.terrainName);
		terrain.mapChipStartX = Integer.valueOf(terrainStrs[1]);
		terrain.mapChipStartY = Integer.valueOf(terrainStrs[2]);
		terrain.avoidPoint = Integer.valueOf(terrainStrs[3]);
		terrain.defencePoint = Integer.valueOf(terrainStrs[4]);

		return terrain;
	}
}
