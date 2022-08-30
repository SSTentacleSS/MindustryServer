package mindustry.server.events.listeners;

import arc.util.Log;
import mindustry.game.EventType;
import mindustry.server.events.Listener;
import mindustry.server.utils.Bundler;

public class ServerLoadEvent implements Listener<EventType.ServerLoadEvent> {

	@Override
	public void listener(EventType.ServerLoadEvent event) {
		Log.info(Bundler.getLocalized("server.loaded"));
	}

	@Override
	public Class<mindustry.game.EventType.ServerLoadEvent> getListenerClass() {
		return EventType.ServerLoadEvent.class;
	}
}
