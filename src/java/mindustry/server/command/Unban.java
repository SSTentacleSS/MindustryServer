package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class Unban implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (
			Vars.netServer.admins.unbanPlayerIP(args[0]) ||
			Vars.netServer.admins.unbanPlayerID(args[0])
		) Bundler.logLocalized(
			LogLevel.info,
			"commands.unban.unbanned",
			args[0]
		); else Bundler.logLocalized(
			LogLevel.info,
			"commands.unban.not_banned"
		);
	}

	@Override
	public String getName() {
		return "unban";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.unban.description");
	}

	@Override
	public String getParams() {
		return "<UUID/IP>";
	}
}
