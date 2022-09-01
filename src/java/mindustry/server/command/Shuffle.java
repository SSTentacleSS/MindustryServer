package mindustry.server.command;

import arc.Core;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.maps.Maps.ShuffleMode;
import mindustry.server.utils.Bundler;

public class Shuffle implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (args.length == 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.shuffle.current_mode",
			Vars.maps.getShuffleMode()
		); else {
			try {
				ShuffleMode mode = ShuffleMode.valueOf(args[0]);
				Core.settings.put("shufflemode", mode.name());
				Vars.maps.setShuffleMode(mode);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.shuffle.setted_shuffle_mode",
					args[0]
				);
			} catch (Exception error) {
				Bundler.logLocalized(
					LogLevel.err,
					"commands.shuffle.invalid_mode"
				);
			}
		}
	}

	@Override
	public String getName() {
		return "shuffle";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.shuffle.description");
	}

	@Override
	public String getParams() {
		return "[none/all/custom/builtin]";
	}
}
