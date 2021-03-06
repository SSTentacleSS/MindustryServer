package mindustry.server.event.listener;

import arc.Core;
import arc.func.Cons;
import arc.util.Log;
import arc.util.serialization.JsonValue;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.io.JsonIO;

public class PlayEvent implements Listener<EventType.PlayEvent> {

	@Override
	public Cons<EventType.PlayEvent> getListener() {
		return event -> {
			try {
				JsonValue value = JsonIO.json.fromJson(
					null,
					Core.settings.getString("globalrules")
				);

				JsonIO.json.readFields(Vars.state.rules, value);
			} catch (Throwable t) {
				Log.err(
					"Error applying custom rules, proceeding without them.",
					t
				);
			}
		};
	}

	@Override
	public Class<EventType.PlayEvent> getListenerClass() {
		return EventType.PlayEvent.class;
	}
}
