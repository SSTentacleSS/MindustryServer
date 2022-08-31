package mindustry.server.command;

import arc.Core;
import arc.util.Log.LogLevel;
import mindustry.server.utils.Bundler;

public class GC implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		int pre = getCurrentHeap();
		System.gc();
		int post = getCurrentHeap();

		Bundler.logLocalized(
			LogLevel.info,
			"commands.gc.collected",
			pre - post,
			post
		);
	}

	@Override
	public String getName() {
		return "gc";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.gc.description");
	}

	@Override
	public String getParams() {
		return "";
	}

	private int getCurrentHeap() {
		return (int) Core.app.getJavaHeap() / 1024 / 1024;
	}
}
