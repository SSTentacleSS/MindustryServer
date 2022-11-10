package mindustry.server.command;

import arc.Core;
import arc.files.Fi;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.io.SaveIO;
import mindustry.server.utils.Bundler;

public class Save implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Host.errorIfNotPlaying()) return;

		Fi file = Vars.saveDirectory.child(args[0] + "." + Vars.saveExtension);

		Core.app.post(() -> {
			SaveIO.save(file);
			Bundler.logLocalized(LogLevel.info, "commands.save.saved", file);
		});
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.save.description");
	}

	@Override
	public String getParams() {
		return "<slot>";
	}
}
