package mindustry.server;

import arc.util.Interval;
import arc.util.Timer;
import mindustry.game.Gamemode;
import mindustry.maps.Map;
import mindustry.server.command.CommandsRegistry;

public final class StateController {
	public static Timer serverTimer = new Timer();
	public static String serverProviderAuthor = "SSTentacleSS";
	public static String serverProviderName = "MindustryServer";
	public static CommandsRegistry commandsRegistry = new CommandsRegistry("");
	public static Interval autosaveCount = new Interval();
	public static int roundExtraTime = 12;
	public static int configSaveInterval = 60;
	public static boolean inExtraRound;
	public static Gamemode lastMode;
	public static Map nextMapOverride;
}
