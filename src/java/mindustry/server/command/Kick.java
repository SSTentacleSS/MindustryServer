package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Administration.PlayerInfo;
import mindustry.net.Packets.KickReason;
import mindustry.server.utils.Bundler;

public class Kick implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Host.errorIfNotPlaying()) return;

		PlayerInfo playerInfo = Info.getPlayerInfoOrError(
			args[0],
			serverPlayerInfo ->
				Groups.player.contains(
					player -> player.name.equals(serverPlayerInfo.lastName)
				)
		);

		if (playerInfo == null) return;

		Player kickPlayer = Groups.player.find(
			player -> player.uuid().equals(playerInfo.id)
		);

		if (kickPlayer == null) Bundler.logLocalized(
			LogLevel.err,
			"commands.kick.player_not_found"
		); else {
			Bundler.logLocalized(LogLevel.info, "commands.kick.kicked.console");
			kickPlayer.kick(KickReason.kick);

			for (Player groupPlayer : Groups.player) Bundler.sendLocalized(
				groupPlayer,
				"commands.kick.kicked.chat"
			);
		}
	}

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.kick.description");
	}

	@Override
	public String getParams() {
		return "<username/UUID/IP...>";
	}
}
