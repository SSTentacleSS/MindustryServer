package mindustry.server.command;

import static mindustry.core.Version.build;
import static mindustry.core.Version.modifier;
import static mindustry.core.Version.number;
import static mindustry.core.Version.revision;
import static mindustry.core.Version.type;

import arc.util.Log.LogLevel;
import arc.util.OS;
import mindustry.server.utils.Bundler;

public class Version implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		Bundler.logLocalized(
			LogLevel.info,
			"server.version",
			number,
			modifier,
			type,
			build,
			revision
		);

		Bundler.logLocalized(LogLevel.info, "java.version", OS.javaVersion);
	}

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.version.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
