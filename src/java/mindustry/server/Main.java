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
import mindustry.ctype.Content;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.mod.Mods.LoadedMod;
import mindustry.net.Net;
import mindustry.server.events.ServerCloseEvent;
import mindustry.server.log.ProgressiveLogger;

public class Main implements ApplicationListener {
	public static String[] args;
	public static HeadlessApplication application;
	public static ProgressiveLogger IO = new ProgressiveLogger();

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown));

		Main.args = args;
		initServer();
	}

	@Override
	public void init() {
		Log.logger = IO;
		Core.settings.setDataDirectory(Core.files.local("config"));
		Vars.loadLocales = false;
		Vars.headless = true;

		Vars.loadSettings();
		Vars.init();
		Vars.content.createBaseContent();
		Vars.mods.loadScripts();
		Vars.content.createModContent();
		Vars.content.init();

		if (Vars.mods.hasContentErrors()) {
			Log.err("Error occurred loading mod content:");

			for (LoadedMod mod : Vars.mods.list()) if (mod.hasContentErrors()) {
				Log.err("| &ly[@]", mod.name);

				for (Content cont : mod.erroredContent) Log.err(
					"| | &y@: &c@",
					cont.minfo.sourceFile.name(),
					Strings
						.getSimpleMessage(cont.minfo.baseError)
						.replace("\n", " ")
				);
			}
			System.exit(1);
		}

		Vars.bases.load();

		Core.app.addListener(
			new ApplicationListener() {

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

				public void update() {
					Vars.asyncCore.end();
				}
			}
		);

		Vars.mods.eachClass(Mod::init);
		Events.fire(new EventType.ServerLoadEvent());
	}

	public static void initServer() {
		if (Vars.net != null) Vars.net.dispose();

		Vars.platform = new Platform() {};
		Vars.net = new Net(Vars.platform.getNet());

		application = new HeadlessApplication(new Main(), Log::err);
	}

	public static void shutdown() {
		Log.info("The server will now be shut down!");

		Events.fire(new ServerCloseEvent());

		Vars.net.dispose();
		IO.executor.shutdown();
		Core.app.exit();
	}
}
