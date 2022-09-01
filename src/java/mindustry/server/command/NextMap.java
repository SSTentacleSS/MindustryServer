package mindustry.server.command;

import arc.util.Log.LogLevel;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.maps.Map;
import mindustry.server.StateController;
import mindustry.server.utils.Bundler;

public class NextMap implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Map nextMap = Vars
			.maps.all()
			.find(
				map ->
					Maps
						.normalizeMapName(map.name())
						.equalsIgnoreCase(Maps.normalizeMapName(args[0]))
			);

		if (nextMap == null) Bundler.logLocalized(
			LogLevel.err,
			"commands.nextmap.map_not_found",
			args[0]
		); else {
			StateController.nextMapOverride = nextMap;
			Bundler.logLocalized(
				LogLevel.info,
				"commands.nextmap.set",
				Strings.stripColors(nextMap.name())
			);
		}
	}

	@Override
	public String getName() {
		return "nextmap";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.nextmap.description");
	}

	@Override
	public String getParams() {
		return "<mapname...>";
	}
}
