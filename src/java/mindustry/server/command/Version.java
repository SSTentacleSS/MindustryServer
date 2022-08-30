package mindustry.server.command;

import static mindustry.core.Version.build;
import static mindustry.core.Version.modifier;
import static mindustry.core.Version.number;
import static mindustry.core.Version.revision;
import static mindustry.core.Version.type;

import arc.util.Log;
import arc.util.OS;
import mindustry.server.utils.Bundler;

public class Version implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) {
		Log.info(
			Bundler.getLocalized(
				"server.version",
				number,
				modifier,
				type,
				build,
				revision
			)
		);

		Log.info(Bundler.getLocalized("java.version", OS.javaVersion));
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
