package mindustry.server.command;

import arc.Core;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.maps.Map;
import mindustry.server.StateController;
import mindustry.server.utils.Bundler;

public class Host implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Vars.state.isPlaying()) {
			Bundler.logLocalized(LogLevel.err, "commands.host.already_hosting");
			return;
		}

		StateController.serverTimer.clear();
		Gamemode gamemode = Gamemode.survival;
		Map map;

		try {
			if (args.length > 1) gamemode = Gamemode.valueOf(args[2]);
		} catch (IllegalArgumentException e) {
			Bundler.logLocalized(
				LogLevel.err,
				"commands.host.gamemode_not_found",
				args[1]
			);

			return;
		}

		if (args.length > 0) {
			String mapName = Maps.normalizeMapName(args[0]);

			map =
				Vars
					.maps.all()
					.find(
						serverMap ->
							Maps
								.normalizeMapName(serverMap.name())
								.equalsIgnoreCase(mapName)
					);

			if (map == null) {
				Bundler.logLocalized(
					LogLevel.err,
					"commands.host.map_not_found",
					args[1]
				);
				return;
			}
		} else {
			map = Vars.maps.getShuffleMode().next(gamemode, Vars.state.map);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.host.ranomized_map",
				map.name()
			);
		}

		Bundler.logLocalized(LogLevel.info, "commands.host.loading");
		Vars.logic.reset();
		StateController.lastMode = gamemode;

		Core.settings.put("lastServerMode", gamemode.name());

		Vars.world.loadMap(map, map.applyRules(gamemode));

		Vars.state.rules = map.applyRules(gamemode);
		Vars.logic.play();
		Vars.netServer.openServer();

		Bundler.logLocalized(LogLevel.info, "commands.host.loaded");
	}

	@Override
	public String getName() {
		return "host";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.host.description");
	}

	@Override
	public String getParams() {
		return "[mapname] [mode]";
	}

	public static boolean errorIfNotPlaying() {
		boolean isPlaying = Vars.state.isPlaying();

		if (!isPlaying) Bundler.logLocalized(
			LogLevel.err,
			"commands.host.not_hosting"
		);
		return !isPlaying;
	}

	public static boolean errorIfPlaying() {
		boolean isPlaying = Vars.state.isPlaying();

		if (isPlaying) Bundler.logLocalized(
			LogLevel.err,
			"commands.host.no_menu"
		);
		return isPlaying;
	}
}
