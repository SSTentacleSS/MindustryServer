package mindustry.server.command;

import arc.Core;
import arc.util.Log.LogLevel;
import mindustry.net.Administration.Config;
import mindustry.server.utils.Bundler;

public class ConfigCMD implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (args.length <= 0) {
			Bundler.logLocalized(LogLevel.info, "commands.config.configs");

			for (Config config : Config.all) {
				Bundler.logLocalized(
					LogLevel.info,
					"commands.config.config_value.1",
					config.name,
					config.get()
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.config.config_value.2",
					config.description
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.config.config_value.3"
				);
			}

			return;
		}

		Config config = Config.all.find(param ->
			param.name.equalsIgnoreCase(args[0])
		);

		if (config == null) {
			Bundler.logLocalized(
				LogLevel.err,
				"commands.config.config_not_found",
				args[0]
			);
			return;
		}

		if (args.length == 1) Bundler.logLocalized(
			LogLevel.info,
			"commands.config.currently",
			config.name,
			config.get()
		); else if (config.isBool()) config.set(
			args[1].equals("on") || args[1].equals("true")
		); else if (config.isString()) config.set(
			args[1].replace("\\n", "\n")
		); else if (config.isNum()) {
			try {
				config.set(Integer.parseInt(args[1]));
			} catch (Exception error) {
				Bundler.logLocalized(
					LogLevel.err,
					"commands.config.not_valid_number"
				);

				return;
			}
		}

		Bundler.logLocalized(
			LogLevel.info,
			"commands.config.set",
			config.name,
			config.get()
		);

		Core.settings.forceSave();
	}

	@Override
	public String getName() {
		return "config";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.config.description");
	}

	@Override
	public String getParams() {
		return "[name] [value...]";
	}
}
