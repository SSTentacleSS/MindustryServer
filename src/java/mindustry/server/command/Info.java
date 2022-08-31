package mindustry.server.command;

import arc.struct.ObjectSet;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Info implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		ObjectSet<PlayerInfo> playerInfos = Vars.netServer.admins.findByName(
			args[0]
		);

		if (playerInfos.size <= 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.info.no_player",
			args[0]
		); else {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.info.players_found",
				args[0]
			);

			int i = 0;
			for (PlayerInfo playerInfo : playerInfos) {
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.trace_info",
					i++,
					playerInfo.lastName,
					playerInfo.id
				);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.1",
					playerInfo.names
				);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.2",
					playerInfo.lastIP
				);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.3",
					playerInfo.ips
				);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.4",
					playerInfo.timesJoined
				);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.5",
					playerInfo.timesKicked
				);
			}
		}
	}

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.info.description");
	}

	@Override
	public String getParams() {
		return "<ip/uuid/name...>";
	}
}
