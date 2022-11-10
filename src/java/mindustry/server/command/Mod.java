package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.mod.Mods.LoadedMod;
import mindustry.server.utils.Bundler;

public class Mod implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		LoadedMod mod = Vars.mods
			.list()
			.find(plugin -> plugin.meta.name.equalsIgnoreCase(args[0]));

		if (mod == null) Bundler.logLocalized(
			LogLevel.info,
			"commands.mod.not_found",
			args[0]
		); else {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.mod.mod_info.1",
				mod.meta.displayName()
			);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.mod.mod_info.2",
				mod.name
			);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.mod.mod_info.3",
				mod.meta.version
			);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.mod.mod_info.4",
				mod.meta.author
			);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.mod.mod_info.5",
				mod.file.path()
			);
			Bundler.logLocalized(
				LogLevel.info,
				"commands.mod.mod_info.6",
				mod.meta.description
			);
		}
	}

	@Override
	public String getName() {
		return "mod";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.mod.description");
	}

	@Override
	public String getParams() {
		return "<name...>";
	}
}
