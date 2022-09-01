package mindustry.server.command;

import arc.util.Log.LogLevel;
import java.util.List;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;

public class Admin implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Host.errorIfNotPlaying()) return;

		if (!List.of("remove", "add").contains(args[0])) {
			Bundler.logLocalized(
				LogLevel.err,
				"commands.admin.invalid_usage.add_or_remove"
			);
		}

		PlayerInfo playerInfo = Info.getPlayerInfoOrError(args[0]);

		if (playerInfo == null) return;

		if (args[0].equals("add")) Vars.netServer.admins.adminPlayer(
			playerInfo.id,
			playerInfo.adminUsid
		); else Vars.netServer.admins.unAdminPlayer(playerInfo.id);

		Bundler.logLocalized(
			LogLevel.info,
			"commands.admin.changed_status",
			playerInfo.lastName,
			args[0].equals("add") ? "admin" : "player"
		);
	}

	@Override
	public String getName() {
		return "admin";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.admin.description");
	}

	@Override
	public String getParams() {
		return "<add/remove> <username/UUID/IP>";
	}
}
