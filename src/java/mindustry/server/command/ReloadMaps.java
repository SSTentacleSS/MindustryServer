package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class ReloadMaps implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Vars.maps.reload();

		Bundler.logLocalized(LogLevel.info, "commands.reloadmaps.reloaded");
	}

	@Override
	public String getName() {
		return "reloadmaps";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.reloadmaps.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
