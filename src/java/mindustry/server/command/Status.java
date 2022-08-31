package mindustry.server.command;

import arc.Core;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.server.utils.Bundler;

public class Status implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Vars.state.isMenu()) Bundler.logLocalized(
			LogLevel.info,
			"commands.status.closed"
		); else {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.status.playing",
				Vars.state.map.name(),
				Vars.state.wave
			);

			if (Vars.state.rules.waves) Bundler.logLocalized(
				LogLevel.info,
				"commands.status.enemies",
				Vars.state.enemies
			); else Bundler.logLocalized(
				LogLevel.info,
				"commands.status.next_wave",
				(int) (Vars.state.wavetime / 60)
			);

			Bundler.logLocalized(
				LogLevel.info,
				"commands.status.server_performance",
				Core.graphics.getFramesPerSecond(),
				Core.app.getJavaHeap()
			);

			if (Groups.player.size() <= 0) Bundler.logLocalized(
				LogLevel.info,
				"commands.status.no_players"
			); else {
				Bundler.logLocalized(
					LogLevel.info,
					"commands.status.players",
					Groups.player.size()
				);

				for (Player player : Groups.player) Bundler.logLocalized(
					LogLevel.info,
					"commands.status.player_info",
					player.name(),
					player.uuid()
				);
			}
		}
	}

	@Override
	public String getName() {
		return "status";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.status.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
