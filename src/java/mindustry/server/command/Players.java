package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Players implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Groups.player.size() == 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.players.no_players"
		); else {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.players.players",
				Groups.player.size()
			);

			for (Player player : Groups.player) {
				PlayerInfo playerInfo = player.getInfo();

				Bundler.logLocalized(
					LogLevel.info,
					"commands.players.player_info",
					playerInfo.lastName,
					playerInfo.id,
					playerInfo.lastIP,
					playerInfo.admin
				);
			}
		}
	}

	@Override
	public String getName() {
		return "players";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.players.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
