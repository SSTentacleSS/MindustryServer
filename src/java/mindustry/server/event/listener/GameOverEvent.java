package mindustry.server.event.listener;

import arc.func.Cons;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Timer;
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

public class GameOverEvent implements Listener<EventType.GameOverEvent> {

	@Override
	public Cons<EventType.GameOverEvent> getListener() {
		return event -> {
			if (StateController.inExtraRound) return;
			if (Vars.state.rules.waves) {
				Log.info(
					"Game over! Reached wave @ with @ players online on map @.",
					Vars.state.wave,
					Groups.player.size(),
					Strings.capitalize(
						Strings.stripColors(Vars.state.map.name())
					)
				);
			} else {
				Log.info(
					"Game over! Team @ is victorious with @ players online on map @.",
					event.winner.name,
					Groups.player.size(),
					Strings.capitalize(
						Strings.stripColors(Vars.state.map.name())
					)
				);
			}

			Map map = StateController.nextMapOverride != null
				? StateController.nextMapOverride
				: Vars.maps.getNextMap(
					StateController.lastMode,
					Vars.state.map
				);
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
		};
	}

	@Override
	public Class<EventType.GameOverEvent> getListenerClass() {
		return EventType.GameOverEvent.class;
	}

	private void play(Runnable run) {
		StateController.inExtraRound = true;
		StateController.lastTask =
			Timer.schedule(
				() -> {
					try {
						WorldReloader reloader = new WorldReloader();

						reloader.begin();

						run.run();

						Vars.state.rules =
							Vars.state.map.applyRules(StateController.lastMode);
						Vars.logic.play();

						reloader.end();
						StateController.inExtraRound = false;
					} catch (MapException e) {
						Log.err(e.map.name() + ": " + e.getMessage());
						Vars.net.closeServer();
					}
				},
				StateController.roundExtraTime
			);
	}
}
