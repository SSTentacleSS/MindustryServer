package mindustry.server.events.listeners;

import mindustry.game.EventType;
import mindustry.game.EventType.PlayerLeave;
import mindustry.gen.Groups;
import mindustry.server.events.Listener;

public class PlayerLeaveEvent implements Listener<EventType.PlayerLeave> {
	public static boolean quietExit = false;

	@Override
	public void listener(PlayerLeave event) {
		if (quietExit && Groups.player.size() - 1 <= 0) System.exit(0);
	}

	@Override
	public Class<PlayerLeave> getListenerClass() {
		return EventType.PlayerLeave.class;
	}
}
