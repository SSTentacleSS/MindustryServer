package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.gen.Groups;
import mindustry.server.events.listeners.PlayerLeaveEvent;
import mindustry.server.utils.Bundler;

public class Exit implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (args.length == 0 || Groups.player.size() == 0) System.exit(
			0
		); else if (args[0] == "quiet") {
			PlayerLeaveEvent.quietExit = true;
			Bundler.logLocalized(LogLevel.info, "commands.exit.quiet");
		}
	}

	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.exit.description");
	}

	@Override
	public String getParams() {
		return "[quiet]";
	}
}
