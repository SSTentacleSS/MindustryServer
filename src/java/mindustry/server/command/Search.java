package mindustry.server.command;

import arc.struct.ObjectSet;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Search implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		ObjectSet<PlayerInfo> playerInfos = Vars.netServer.admins.searchNames(
			args[0]
		);

		if (playerInfos.size <= 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.search.no_player"
		); else {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.search.players_found",
				playerInfos.size
			);

			int i = 0;
			for (PlayerInfo playerInfo : playerInfos) Bundler.logLocalized(
				LogLevel.info,
				"commands.search.player_info",
				i++,
				playerInfo.lastName,
				playerInfo.id
			);
		}
	}

	@Override
	public String getName() {
		return "search";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.search.description");
	}

	@Override
	public String getParams() {
		return "<name...>";
	}
}
