package mindustry.server.command;

import arc.Events;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.StateController;
import mindustry.server.events.listeners.GameOverEvent;
import mindustry.server.utils.Bundler;

public class GameOver implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		if (Vars.state.isMenu()) {
			Bundler.logLocalized(LogLevel.err, "commands.gameover.not_playing");
			return;
		}

		Bundler.logLocalized(LogLevel.info, "commands.gameover.core_destroyed");
		StateController.inExtraRound = false;
		Events.fire(new GameOverEvent());
	}

	@Override
	public String getName() {
		return "gameover";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.gameover.description");
	}

	@Override
	public String getParams() {
		return "";
	}
}
