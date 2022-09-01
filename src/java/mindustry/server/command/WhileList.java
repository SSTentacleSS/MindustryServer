package mindustry.server.command;

import arc.struct.Seq;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class WhileList implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (args.length <= 0) {
			Seq<PlayerInfo> whitelist = Vars.netServer.admins.getWhitelisted();

			if (whitelist.isEmpty()) Bundler.logLocalized(
				LogLevel.info,
				"commands.whitelist.no_players"
			); else {
				Bundler.logLocalized(
					LogLevel.info,
					"commands.whitelist.whitelist"
				);

				for (PlayerInfo playerInfo : whitelist) Bundler.logLocalized(
					LogLevel.info,
					"commands.whitelist.player_info",
					playerInfo.lastName,
					playerInfo.id
				);
			}
		} else if (args.length == 2) {
			PlayerInfo playerInfo = Vars.netServer.admins.getInfoOptional(
				args[1]
			);

			if (playerInfo == null) Bundler.logLocalized(
				LogLevel.info,
				"commands.whitelist.player_not_found"
			); else if (args[0].equals("add")) {
				Vars.netServer.admins.whitelist(args[1]);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.whitelist.whitelisted",
					playerInfo.lastName
				);
			} else if (args[0].equals("remove")) {
				Vars.netServer.admins.unwhitelist(args[1]);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.whitelist.unwhitelisted",
					playerInfo.lastName
				);
			} else Bundler.logLocalized(
				LogLevel.err,
				"commands.whitelist.incorrect_usage.add_or_remove"
			);
		} else Bundler.logLocalized(
			LogLevel.err,
			"commands.whitelist.incorrect_usage.id_add_or_remove"
		);
	}

	@Override
	public String getName() {
		return "whitelist";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.whitelist.description");
	}

	@Override
	public String getParams() {
		return "[add/remove] [ID]";
	}
}
