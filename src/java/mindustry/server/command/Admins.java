package mindustry.server.command;

import arc.struct.Seq;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Admins implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Seq<PlayerInfo> admins = Vars.netServer.admins.getAdmins();

		if (admins.size <= 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.admins.no_admins"
		); else {
			Bundler.logLocalized(LogLevel.info, "commands.admins.admins");

			for (PlayerInfo adminInfo : admins) Bundler.logLocalized(
				LogLevel.info,
				"commands.admins.admin_info",
				adminInfo.lastName,
				adminInfo.id,
				adminInfo.lastIP
			);
		}
	}

	@Override
	public String getName() {
		return "admins";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.admins.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
