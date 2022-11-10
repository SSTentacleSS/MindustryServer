package mindustry.server;

import arc.util.Interval;
import arc.util.Timer;
import mindustry.game.Gamemode;
import mindustry.maps.Map;
import mindustry.server.command.CommandsRegistry;

public final class StateController {

	private StateController() {}

	private static Timer serverTimer = new Timer();
	private static String serverProviderAuthor = "SSTentacleSS";
	private static String serverProviderName = "MindustryServer";
	private static CommandsRegistry commandsRegistry = new CommandsRegistry("");
	private static Interval autosaveCount = new Interval();
	private static int roundExtraTime = 12;
	private static int configSaveInterval = 60;
	private static boolean inExtraRound;
	private static Gamemode lastMode;
	private static Map nextMapOverride;

	public static Timer getServerTimer() {
		return serverTimer;
	}

	public static void setServerTimer(Timer serverTimer) {
		StateController.serverTimer = serverTimer;
	}

	public static String getServerProviderAuthor() {
		return serverProviderAuthor;
	}

	public static void setServerProviderAuthor(String serverProviderAuthor) {
		StateController.serverProviderAuthor = serverProviderAuthor;
	}

	public static String getServerProviderName() {
		return serverProviderName;
	}

	public static void setServerProviderName(String serverProviderName) {
		StateController.serverProviderName = serverProviderName;
	}

	public static CommandsRegistry getCommandsRegistry() {
		return commandsRegistry;
	}

	public static void setCommandsRegistry(CommandsRegistry commandsRegistry) {
		StateController.commandsRegistry = commandsRegistry;
	}

	public static Interval getAutosaveCount() {
		return autosaveCount;
	}

	public static void setAutosaveCount(Interval autosaveCount) {
		StateController.autosaveCount = autosaveCount;
	}

	public static int getRoundExtraTime() {
		return roundExtraTime;
	}

	public static void setRoundExtraTime(int roundExtraTime) {
		StateController.roundExtraTime = roundExtraTime;
	}

	public static int getConfigSaveInterval() {
		return configSaveInterval;
	}

	public static void setConfigSaveInterval(int configSaveInterval) {
		StateController.configSaveInterval = configSaveInterval;
	}

	public static boolean isInExtraRound() {
		return inExtraRound;
	}

	public static void setInExtraRound(boolean inExtraRound) {
		StateController.inExtraRound = inExtraRound;
	}

	public static Gamemode getLastMode() {
		return lastMode;
	}

	public static void setLastMode(Gamemode lastMode) {
		StateController.lastMode = lastMode;
	}

	public static Map getNextMapOverride() {
		return nextMapOverride;
	}

	public static void setNextMapOverride(Map nextMapOverride) {
		StateController.nextMapOverride = nextMapOverride;
	}
}
