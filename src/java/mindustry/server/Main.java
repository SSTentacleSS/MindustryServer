package mindustry.server;

import arc.ApplicationListener;
import arc.Core;
import arc.Events;
import arc.backend.headless.HeadlessApplication;
import arc.util.Log;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.core.NetServer;
import mindustry.core.Platform;
import mindustry.game.EventType;
import mindustry.game.Gamemode;
import mindustry.maps.Maps.ShuffleMode;
import mindustry.mod.Mod;
import mindustry.mod.Mods.LoadedMod;
import mindustry.net.Net;
import mindustry.server.events.emitters.ServerCloseEvent;
import mindustry.server.log.ProgressiveLogger;
import mindustry.server.utils.Bundler;

public class Main implements ApplicationListener {

	private static String[] args;
	private static HeadlessApplication application;
	private static ProgressiveLogger progressiveLogger = new ProgressiveLogger();

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown));

		Main.args = args;
		initServer();
	}

	@Override
	public void init() {
		initStatic();
		initCoreApp();

		Vars.mods.eachClass(Mod::init);
		Events.fire(new EventType.ServerLoadEvent());
	}

	public static void initStatic() {
		Log.logger = progressiveLogger;
		Core.settings.setDataDirectory(Core.files.local("config"));
		Vars.loadLocales = false;
		Vars.headless = true;

		Vars.loadSettings();
		Vars.init();
		Vars.content.createBaseContent();
		Vars.mods.loadScripts();
		Vars.content.createModContent();
		Vars.content.init();

		Vars.customMapDirectory.mkdirs();

		if (Vars.mods.hasContentErrors()) {
			Log.err("Error occurred loading mod content:");

			Vars.mods
				.list()
				.copy()
				.filter(LoadedMod::hasContentErrors)
				.forEach(erroredMod -> {
					Log.err("| &ly[@]", erroredMod.name);

					erroredMod.erroredContent.forEach(content ->
						Log.err(
							"| | &y@: &c@",
							content.minfo.sourceFile.name(),
							Strings
								.getSimpleMessage(content.minfo.baseError)
								.replace("\n", " ")
						)
					);
				});

			System.exit(1);
		}

		Vars.bases.load();
	}

	public static void initCoreApp() {
		Core.app.addListener(
			new ApplicationListener() {
				@Override
				public void update() {
					Vars.asyncCore.begin();
				}
			}
		);

		Core.app.addListener(Vars.logic = new Logic());
		Core.app.addListener(Vars.netServer = new NetServer());
		Core.app.addListener(new ServerController(args));
		Core.app.addListener(
			new ApplicationListener() {
				@Override
				public void update() {
					Vars.asyncCore.end();
				}
			}
		);
	}

	public static void initStateController() {
		Vars.maps.setShuffleMode(
			valueOf(
				ShuffleMode.values(),
				Core.settings.getString("shufflemode"),
				ShuffleMode.builtin
			)
		);

		StateController.setLastMode(
			valueOf(
				Gamemode.values(),
				Core.settings.getString("lastServerMode", "survival"),
				Gamemode.survival
			)
		);
	}

	public static void initServer() {
		if (Vars.net != null) Vars.net.dispose();

		Vars.platform = new Platform() {};
		Vars.net = new Net(Vars.platform.getNet());

		application = new HeadlessApplication(new Main(), Log::err);
	}

	public static void shutdown() {
		Log.info(Bundler.getLocalized("server.shutdown"));

		Events.fire(new ServerCloseEvent() {});

		Vars.net.dispose();
		progressiveLogger.getExecutor().shutdown();
		Core.app.exit();
	}

	private static <T extends Enum<T>> T valueOf(
		T[] values,
		String name,
		T defaultValue
	) {
		for (T value : values) {
			if (value.name().equals(name)) {
				return value;
			}
		}

		return defaultValue;
	}

	public static String[] getArgs() {
		return args;
	}

	public static HeadlessApplication getApplication() {
		return application;
	}

	public static ProgressiveLogger getProgressiveLogger() {
		return progressiveLogger;
	}
}
