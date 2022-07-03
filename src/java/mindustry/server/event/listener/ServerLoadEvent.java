package mindustry.server.event.listener;

import arc.func.Cons;
import arc.util.Log;
import mindustry.game.EventType;

public class ServerLoadEvent implements Listener<EventType.ServerLoadEvent> {

	@Override
	public Cons<mindustry.game.EventType.ServerLoadEvent> getListener() {
		return event -> {
			Log.info("Server loaded. Type 'help' for help.");
		};
	}

	@Override
	public Class<mindustry.game.EventType.ServerLoadEvent> getListenerClass() {
		return EventType.ServerLoadEvent.class;
	}
}
