package mindustry.server.event.listener;

import arc.func.Cons;
import mindustry.game.EventType;
import mindustry.net.Administration.Config;
import mindustry.server.StateController;

public class WorldLoadEvent implements Listener<EventType.WorldLoadEvent> {

	@Override
	public Cons<EventType.WorldLoadEvent> getListener() {
		return event -> {
			StateController.autosaveCount.reset(
				0,
				Config.autosaveSpacing.num() * 60
			);
		};
	}

	@Override
	public Class<EventType.WorldLoadEvent> getListenerClass() {
		return EventType.WorldLoadEvent.class;
	}
}
