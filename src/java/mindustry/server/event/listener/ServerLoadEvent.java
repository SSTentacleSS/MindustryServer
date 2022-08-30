package mindustry.server.event.listener;

import arc.func.Cons;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.server.utils.Bundler;

public class ServerLoadEvent implements Listener<EventType.ServerLoadEvent> {

	@Override
	public Cons<mindustry.game.EventType.ServerLoadEvent> getListener() {
		return event -> {
			Log.info(Bundler.getLocalized("server.loaded"));
		};
	}

	@Override
	public Class<mindustry.game.EventType.ServerLoadEvent> getListenerClass() {
		return EventType.ServerLoadEvent.class;
	}
}
