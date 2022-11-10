package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.core.GameState.State;
import mindustry.server.utils.Bundler;

public class Pause implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		boolean pause = args[0].equals("on");

		Vars.state.set(pause ? State.paused : State.playing);

		Bundler.logLocalized(
			LogLevel.info,
			pause ? "commands.pause.paused" : "commands.pause.unpaused"
		);
	}

	@Override
	public String getName() {
		return "pause";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.pause.description");
	}

	@Override
	public String getParams() {
		return "<on/off>";
	}
}
