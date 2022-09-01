package mindustry.server.events.listeners;

import arc.util.Log;
import arc.util.Strings;
import arc.util.Timer.Task;
import mindustry.Vars;
import mindustry.core.GameState.State;
import mindustry.game.EventType;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.maps.Map;
import mindustry.maps.MapException;
import mindustry.net.Packets.KickReason;
import mindustry.net.WorldReloader;
import mindustry.server.StateController;
import mindustry.server.events.Listener;

// TODO: Rewrite
public class GameOverEvent implements Listener<EventType.GameOverEvent> {

	@Override
	public void listener(mindustry.game.EventType.GameOverEvent event) {
		if (StateController.inExtraRound) return;
		if (Vars.state.rules.waves) {
			Log.info(
				"Game over! Reached wave @ with @ players online on map @.",
				Vars.state.wave,
				Groups.player.size(),
				Strings.capitalize(Strings.stripColors(Vars.state.map.name()))
			);
		} else {
			Log.info(
				"Game over! Team @ is victorious with @ players online on map @.",
				event.winner.name,
				Groups.player.size(),
				Strings.capitalize(Strings.stripColors(Vars.state.map.name()))
			);
		}

		Map map = StateController.nextMapOverride != null
			? StateController.nextMapOverride
			: Vars.maps.getNextMap(StateController.lastMode, Vars.state.map);
		StateController.nextMapOverride = null;

		if (map != null) {
			Call.infoMessage(
				(
					Vars.state.rules.pvp
						? "[accent]The " +
						event.winner.name +
						" team is victorious![]\n"
						: "[scarlet]Game over![]\n"
				) +
				"\nNext selected map:[accent] " +
				Strings.stripColors(map.name()) +
				"[]" +
				(
					map.tags.containsKey("author") &&
						!map.tags.get("author").trim().isEmpty()
						? " by[accent] " + map.author() + "[white]"
						: ""
				) +
				"." +
				"\nNew game begins in " +
				StateController.roundExtraTime +
				" seconds."
			);

			Vars.state.gameOver = true;
			Call.updateGameOver(event.winner);

			Log.info(
				"Selected next map to be @.",
				Strings.stripColors(map.name())
			);

			play(
				() ->
					Vars.world.loadMap(
						map,
						map.applyRules(StateController.lastMode)
					)
			);
		} else {
			Vars.netServer.kickAll(KickReason.gameover);
			Vars.state.set(State.menu);
			Vars.net.closeServer();
		}
	}

	@Override
	public Class<EventType.GameOverEvent> getListenerClass() {
		return EventType.GameOverEvent.class;
	}

	public void play(Runnable runnable) {
		StateController.inExtraRound = true;
		StateController.serverTimer.scheduleTask(
			new Task() {

				@Override
				public void run() {
					try {
						WorldReloader reloader = new WorldReloader();

						if (Vars.netClient == null) return;
						reloader.begin();
						runnable.run();

						Vars.state.rules =
							Vars.state.map.applyRules(StateController.lastMode);
						Vars.logic.play();

						reloader.end();
						StateController.inExtraRound = false;
					} catch (MapException error) {
						Log.err(error.map.name() + ": " + error.getMessage());
						System.exit(1);
					}
				}
			},
			StateController.roundExtraTime
		);
	}
}
