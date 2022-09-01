package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.server.utils.Bundler;

public class Ban implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (args[0].equals("id")) {
			Vars.netServer.admins.banPlayerID(args[1]);
			Bundler.logLocalized(LogLevel.info, "commands.ban.banned");
		} else if (args[0].equals("ip")) {
			Vars.netServer.admins.banPlayerIP(args[1]);
			Bundler.logLocalized(LogLevel.info, "commands.ban.banned");
		} else if (args[0].equals("name")) {
			Player player = Groups.player.find(
				groupPlayer -> groupPlayer.name().equalsIgnoreCase(args[1])
			);

			if (player == null) Bundler.logLocalized(
				LogLevel.err,
				"commands.ban.player_not_found"
			); else {
				Vars.netServer.admins.banPlayer(player.uuid());
				Bundler.logLocalized(LogLevel.info, "commands.ban.banned");
			}
		} else {
			Bundler.logLocalized(LogLevel.err, "commands.ban.invalid_type");
			return;
		}

		String name = Vars
			.netServer.admins.findByName(args[1])
			.first()
			.lastName;

		for (Player player : Groups.player) Bundler.sendLocalized(
			player,
			"commands.ban.chat_banned",
			name
		);
	}

	@Override
	public String getName() {
		return "ban";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.ban.description");
	}

	@Override
	public String getParams() {
		return "<uuid/name/ip> <username/IP/UUID...>";
	}
}
