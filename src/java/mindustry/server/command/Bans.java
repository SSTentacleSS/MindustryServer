package mindustry.server.command;

import arc.struct.Seq;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Bans implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Seq<PlayerInfo> idBans = Vars.netServer.admins.getBanned();

		if (idBans.size <= 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.bans.not_found.id"
		); else {
			Bundler.logLocalized(LogLevel.info, "commands.bans.banned.id");

			for (PlayerInfo playerInfo : idBans) Bundler.logLocalized(
				LogLevel.info,
				"commands.bans.player_info.id",
				playerInfo.id,
				playerInfo.lastName
			);
		}

		Seq<String> ipBans = Vars.netServer.admins.getBannedIPs();

		if (ipBans.size <= 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.bans.not_found.ip"
		); else {
			Bundler.logLocalized(LogLevel.info, "commands.bans.banned.ip");

			for (String ip : ipBans) {
				PlayerInfo playerInfo = Vars.netServer.admins.findByIP(ip);

				if (playerInfo == null) Bundler.logLocalized(
					LogLevel.info,
					"commands.bans.unknown_info.ip",
					ip
				); else Bundler.logLocalized(
					LogLevel.info,
					"commands.bans.player_info.ip",
					ip,
					playerInfo.lastName,
					playerInfo.id
				);
			}
		}
	}

	@Override
	public String getName() {
		return "bans";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.bans.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
