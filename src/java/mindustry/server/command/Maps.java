package mindustry.server.command;

import arc.struct.Seq;
import arc.util.Log.LogLevel;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.maps.Map;
import mindustry.server.utils.Bundler;

public class Maps implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Seq<Map> maps = Vars.maps.all();
		Seq<Map> customMaps = new Seq<>();
		Seq<Map> defaultMaps = new Seq<>();

		if (maps.size == 0) {
			Bundler.logLocalized(LogLevel.info, "commands.maps.not_found");
			return;
		}

		for (Map map : maps) {
			if (map.custom) customMaps.add(map); else defaultMaps.add(map);
		}

		Bundler.logLocalized(
			LogLevel.info,
			"commands.maps.found_maps",
			maps.size
		);
		Bundler.logLocalized(LogLevel.info, "commands.maps.default_maps");

		for (Map map : defaultMaps) Bundler.logLocalized(
			LogLevel.info,
			"commands.maps.default_map",
			normalizeMapName(map.name()),
			map.width,
			map.height
		);

		for (Map map : customMaps) Bundler.logLocalized(
			LogLevel.info,
			"commands.maps.custom_map",
			normalizeMapName(map.name()),
			map.file.name(),
			map.width,
			map.height
		);

		Bundler.logLocalized(
			LogLevel.info,
			"commands.maps.custom_directory",
			Vars.customMapDirectory.absolutePath()
		);
	}

	@Override
	public String getName() {
		return "maps";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.maps.description");
	}

	@Override
	public String getParams() {
		return "";
	}

	public static String normalizeMapName(String mapName) {
		return Strings.stripColors(mapName).replace('_', ' ');
	}
}
