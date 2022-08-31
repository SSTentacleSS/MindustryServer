package mindustry.server.command;

import arc.util.Log.LogLevel;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class PlayerLimit implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (args.length == 0) {
			int playerLimit = Vars.netServer.admins.getPlayerLimit();

			Bundler.logLocalized(
				LogLevel.info,
				"commands.playerlimit.current_limit",
				playerLimit == 0 ? "off" : playerLimit
			);
			return;
		} else if (args[0].equals("off")) {
			Vars.netServer.admins.setPlayerLimit(0);

			Bundler.logLocalized(
				LogLevel.info,
				"commands.playerlimit.disabled"
			);
			return;
		}

		int limit = Strings.parseInt(args[0]);

		if (Strings.canParsePositiveInt(args[0]) && limit > 0) {
			Vars.netServer.admins.setPlayerLimit(limit);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.playerlimit.new_limit",
				limit
			);
		} else Bundler.logLocalized(
			LogLevel.err,
			"commands.playerlimit.number_above_limit"
		);
	}

	@Override
	public String getName() {
		return "playerlimit";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.playerlimit.description");
	}

	@Override
	public String getParams() {
		return "[off/number]";
	}
}
