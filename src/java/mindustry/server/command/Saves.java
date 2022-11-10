package mindustry.server.command;

import arc.files.Fi;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class Saves implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Fi[] saves = Vars.saveDirectory.list(file ->
			file.getName().equals(Vars.saveExtension)
		);

		if (saves.length == 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.saves.not_found"
		); else {
			Bundler.logLocalized(LogLevel.info, "commands.saves.files");

			for (Fi file : saves) {
				Bundler.logLocalized(
					LogLevel.info,
					"commands.saves.file",
					file.nameWithoutExtension()
				);
			}
		}

		Bundler.logLocalized(
			LogLevel.info,
			"commands.saves.saves_directory",
			Vars.saveDirectory.absolutePath()
		);
	}

	@Override
	public String getName() {
		return "saves";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.saves.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
