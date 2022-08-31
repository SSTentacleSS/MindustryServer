package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.gen.Groups;
import mindustry.server.utils.Bundler;

public class Say implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Host.errorIfNotPlaying()) return;
		else {
			Groups.player.forEach(
				player ->
					Bundler.sendLocalized(
						player,
						"commands.say.chat_say",
						args[0]
					)
			);

			Bundler.logLocalized(
				LogLevel.info,
				"commands.say.log_say",
				args[0]
			);
		}
	}

	@Override
	public String getName() {
		return "say";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.say.description");
	}

	@Override
	public String getParams() {
		return "<message...>";
	}
}
