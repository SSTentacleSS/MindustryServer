package mindustry.server.command;

import arc.struct.Seq;
import arc.util.Log;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class SubnetBan implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Seq<String> subnetBans = Vars.netServer.admins.getSubnetBans();

		if (args.length == 0) {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.subnet_ban.banned",
				subnetBans.isEmpty() ? "<none>" : ""
			);

			for (String subnetBan : subnetBans) Log.info("\t&lw" + subnetBan);
		} else if (args.length == 1) Bundler.logLocalized(
			LogLevel.err,
			"commands.subnet_ban.need_subnet"
		); else {
			if (args[0].equals("add")) {
				if (subnetBans.contains(args[1])) {
					Bundler.logLocalized(
						LogLevel.err,
						"commands.subnet_ban.already_banned"
					);

					return;
				}

				Vars.netServer.admins.addSubnetBan(args[1]);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.subnet_ban.banned",
					args[1]
				);
			} else if (args[0].equals("remove")) {
				if (!subnetBans.contains(args[1])) {
					Bundler.logLocalized(
						LogLevel.err,
						"commands.subnet_ban.cant_remove"
					);

					return;
				}

				Vars.netServer.admins.removeSubnetBan(args[1]);
				Bundler.logLocalized(
					LogLevel.info,
					"commands.subnet_ban.unbanned",
					args[1]
				);
			} else Bundler.logLocalized(
				LogLevel.err,
				"commands.subnet_ban.incorrect_usage.add_or_remove"
			);
		}
	}

	@Override
	public String getName() {
		return "subnet-ban";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.subnet_ban.description");
	}

	@Override
	public String getParams() {
		return "[add/remove] [address]";
	}
}
