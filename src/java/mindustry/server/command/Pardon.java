package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Pardon implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		PlayerInfo playerInfo = Info.getPlayerInfoOrError(args[0]);

		if (playerInfo == null) return;

		playerInfo.lastKicked = 0;
		Bundler.logLocalized(
			LogLevel.info,
			"commands.pardon.pardoned",
			playerInfo.lastName
		);
		return;
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
