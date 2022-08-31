package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.mod.Mods.LoadedMod;
import mindustry.server.utils.Bundler;

public class Mods implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Vars.mods.list().isEmpty()) Bundler.logLocalized(
			LogLevel.info,
			"commands.mods.not_found"
		); else {
			Bundler.logLocalized(LogLevel.info, "commands.mods.found_mods");

			for (LoadedMod mod : Vars.mods.list()) Bundler.logLocalized(
				LogLevel.info,
				"commands.mods.mod_info",
				mod.meta.displayName(),
				mod.meta.version
			);
		}

		Bundler.logLocalized(
			LogLevel.info,
			"commands.mods.mods_directory",
			Vars.modDirectory.absolutePath()
		);
	}

	@Override
	public String getName() {
		return "mods";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.mods.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
