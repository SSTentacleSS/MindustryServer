package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class JS implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Bundler.logLocalized(
			LogLevel.info,
			"commands.js.executed",
			Vars.mods.getScripts().runConsole(args[0])
		);
	}

	@Override
	public String getName() {
		return "js";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.js.description");
	}

	@Override
	public String getParams() {
		return "<script...>";
	}
}
