package mindustry.server.command;

import arc.util.Log.LogLevel;
import arc.util.Structs;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.server.utils.Bundler;
import mindustry.type.Item;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

public class FillItems implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Host.errorIfNotPlaying()) return;

		Team team = args.length == 0
			? Team.sharded
			: Structs.find(
				Team.all,
				serverTeam -> serverTeam.name.equalsIgnoreCase(args[0])
			);

		if (team == null) {
			Bundler.logLocalized(
				LogLevel.err,
				"commands.fillitems.no_team",
				args[0]
			);
			return;
		}

		if (Vars.state.teams.cores(team).isEmpty()) {
			Bundler.logLocalized(LogLevel.err, "commands.fillitems.no_core");
			return;
		}

		CoreBuild core = Vars.state.teams.cores(team).first();

		for (Item item : Vars.content.items()) {
			core.items().set(item, core.storageCapacity);
		}

		Bundler.logLocalized(LogLevel.info, "commands.fillitems.filled");
	}

	@Override
	public String getName() {
		return "fillitems";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.fillitems.description");
	}

	@Override
	public String getParams() {
		return "[team]";
	}
}
