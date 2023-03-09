package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;
import mindustry.Vars;

public class Pardon implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		PlayerInfo playerInfo = Info.getPlayerInfoOrError(args[0]);

		if (playerInfo == null) return;

		playerInfo.lastKicked = 0;
		Vars.netServer.admins.kickedIPs.remove(info.lastIP);
		Bundler.logLocalized(
			LogLevel.info,
			"commands.pardon.pardoned",
			playerInfo.lastName
		);
	}

	@Override
	public String getName() {
		return "pardon";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.pardon.description");
	}

	@Override
	public String getParams() {
		return "<username/UUID/IP...>";
	}
}
