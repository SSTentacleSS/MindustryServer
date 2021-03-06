package mindustry.server;

import arc.util.Interval;
import arc.util.Timer.Task;
import mindustry.game.Gamemode;
import mindustry.maps.Map;

public final class StateController {
	public static Interval autosaveCount = new Interval();
	public static int roundExtraTime = 12;
	public static int configSaveInterval = 60;
	public static boolean inExtraRound;
	public static Task lastTask;
	public static Gamemode lastMode;
	public static Map nextMapOverride;
}
