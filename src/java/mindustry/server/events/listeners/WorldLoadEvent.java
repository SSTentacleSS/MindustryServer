package mindustry.server.events.listeners;

import mindustry.game.EventType;
import mindustry.net.Administration.Config;
import mindustry.server.StateController;
import mindustry.server.events.Listener;

public class WorldLoadEvent implements Listener<EventType.WorldLoadEvent> {

	@Override
	public void listener(EventType.WorldLoadEvent event) {
		StateController.autosaveCount.reset(
			0,
			Config.autosaveSpacing.num() * 60
		);
	}

	@Override
	public Class<EventType.WorldLoadEvent> getListenerClass() {
		return EventType.WorldLoadEvent.class;
	}
}
