package mindustry.server;

import arc.ApplicationListener;
import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.util.CommandHandler.Command;
import arc.util.CommandHandler.CommandResponse;
import arc.util.CommandHandler.ResponseType;
import arc.util.Log;
import arc.util.Timer;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import mindustry.Vars;
import mindustry.core.GameState.State;
import mindustry.core.Version;
import mindustry.io.SaveIO;
import mindustry.net.Administration.Config;
import mindustry.server.command.CommandsRegistry;
import mindustry.server.command.ServerRegistrableCommand;
import mindustry.server.events.Emitter;
import mindustry.server.events.Listener;
import mindustry.server.events.emitters.TriggerUpdate;
import mindustry.server.utils.Pipe;
import org.jline.widget.TailTipWidgets;
import org.jline.widget.TailTipWidgets.TipType;
import org.reflections.Reflections;

public class ServerController implements ApplicationListener {
	public Fi logFolder = Core.settings.getDataDirectory().child("logs/");

	public ServerController(String[] args) {
		Core.settings.defaults(
			"bans",
			"",
			"admins",
			"",
			"shufflemode",
			"custom",
			"globalrules",
			"{reactorExplosions: false, logicUnitBuild: false}"
		);

		if (Version.build == -1) Log.info("Client checking disabled");

		registerCommands();
		handleInput();

		new TailTipWidgets(
			Main.IO.reader,
			StateController.commandsRegistry.tailTips,
			0,
			TipType.TAIL_TIP
		)
		.enable();

		Timer.schedule(
			() -> Core.settings.forceSave(),
			StateController.configSaveInterval,
			StateController.configSaveInterval
		);

		Core.app.post(
			() -> {
				loadAutosave();
				loadPresetCommands(args);
			}
		);

		registerEventListeners();

		runEvent(new TriggerUpdate());
	}

	public void loadAutosave() {
		if (Config.autoUpdate.bool()) {
			Fi saveFile = Vars.saveDirectory.child(
				"autosavebe." + Vars.saveExtension
			);

			if (saveFile.exists()) {
				try {
					SaveIO.load(saveFile);

					Log.info("Auto-save loaded");

					Vars.state.set(State.playing);
					Vars.netServer.openServer();
				} catch (Throwable e) {
					Log.err(e);
				}
			}
		}
	}

	public void loadPresetCommands(String[] args) {
		String[] argCommands = stringArrayToCommandsArray(args);
		String[] startupCommands = stringToCommandsArray(
			Config.startCommands.string()
		);

		if (argCommands.length != 0) Log.info(
			"Found @ command-line arguments to parse",
			argCommands.length
		);

		if (startupCommands.length != 0) Log.info(
			"Found @ startup commands",
			startupCommands.length
		);

		for (String command : concatWithArrayCopy(
			argCommands,
			startupCommands
		)) {
			CommandResponse response = StateController.commandsRegistry.handleMessage(
				command
			);

			if (response.type != ResponseType.valid) {
				Log.err(
					"Invalid command argument sent: '@': @",
					command,
					response.type.name()
				);

				Log.err(
					"Argument usage: &lb<command-1> <command1-args...>,<command-2> <command-2-args2...>"
				);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void registerEventListeners() {
		getSubClasses(
				Listener.class.getPackageName() + ".listeners",
				Listener.class
			)
			.forEach(
				eventClass -> {
					try {
						Listener<?> command = eventClass
							.getConstructor()
							.newInstance();

						Events.on(
							(Class<Object>) command.getListenerClass(),
							event ->
								((Listener<Object>) command).listener(event)
						);
					} catch (
						NoSuchMethodException
						| SecurityException
						| InstantiationException
						| IllegalAccessException
						| IllegalArgumentException
						| InvocationTargetException e
					) {
						return;
					}
				}
			);
	}

	public void registerCommands() {
		getSubClasses(
				ServerRegistrableCommand.class.getPackageName(),
				ServerRegistrableCommand.class
			)
			.forEach(
				commandClass -> {
					try {
						ServerRegistrableCommand command = commandClass
							.getConstructor()
							.newInstance();

						StateController.commandsRegistry.register(
							command.getName(),
							command.getParams(),
							command.getDescription(),
							args -> command.listener(args)
						);
					} catch (
						NoSuchMethodException
						| SecurityException
						| InstantiationException
						| IllegalAccessException
						| IllegalArgumentException
						| InvocationTargetException e
					) {
						return;
					}
				}
			);
	}

	public void handleInput() {
		CompletableFuture<String> readFuture = Main.IO.read();

		readFuture.handle(
			(result, exception) -> {
				if (exception != null) {
					Log.err(exception);
					return null;
				}

				handleCommandString(result);

				handleInput();
				return null;
			}
		);
	}

	private void handleCommandString(String line) {
		CommandsRegistry handler = StateController.commandsRegistry;
		CommandResponse response = handler.handleMessage(line);

		String message = new String();

		switch (response.type) {
			case manyArguments:
				message =
					"Too many command arguments. Usage: " +
					getUsage(response.command);
				break;
			case fewArguments:
				message =
					"Too few command arguments. Usage: " +
					getUsage(response.command);
				break;
			case noCommand:
				message = "Invalid command. Type 'help' for help";
				break;
			case unknownCommand:
				message = "Invalid command. Type 'help' for help";
				break;
			default:
				return;
		}

		Log.err(message);
	}

	private <T> void runEvent(Emitter<T> emitter) {
		Events.run(emitter.getEmitterClass(), emitter.getEmitter());
	}

	private String getUsage(Command command) {
		return command.text + " " + command.paramText;
	}

	private String[] stringArrayToCommandsArray(String[] commands) {
		return stringToCommandsArray(String.join(" ", commands));
	}

	private String[] stringToCommandsArray(String commands) {
		if (commands.trim().length() == 0) return new String[0];

		return Pipe
			.apply(commands)
			.pipe(commandsStr -> commandsStr.split(","))
			.result();
	}

	private <T> Set<Class<? extends T>> getSubClasses(
		String packageName,
		Class<T> clazz
	) {
		return Pipe
			.apply(packageName)
			.pipe(Reflections::new)
			.pipe(reflection -> reflection.getSubTypesOf(clazz))
			.result();
	}

	private <T> T[] concatWithArrayCopy(T[] array1, T[] array2) {
		T[] result = Arrays.copyOf(array1, array1.length + array2.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}
}
