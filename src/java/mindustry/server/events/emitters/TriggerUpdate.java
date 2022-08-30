package mindustry.server.events.emitters;

import arc.files.Fi;
import arc.struct.Seq;
import arc.util.Log;
import java.time.LocalDateTime;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.EventType.Trigger;
import mindustry.io.SaveIO;
import mindustry.net.Administration.Config;
import mindustry.server.StateController;
import mindustry.server.events.Emitter;
import mindustry.server.log.ProgressiveLogger;

// TODO: Rewrite
public class TriggerUpdate implements Emitter<EventType.Trigger> {

	@Override
	public Runnable getEmitter() {
		return () -> {
			if (Vars.state.isPlaying() && Config.autosave.bool()) {
				if (
					StateController.autosaveCount.get(
						Config.autosaveSpacing.num() * 60
					)
				) {
					int max = Config.autosaveAmount.num();

					//use map file name to make sure it can be saved
					String mapName =
						(
							Vars.state.map.file == null
								? "unknown"
								: Vars.state.map.file.nameWithoutExtension()
						).replace(" ", "_");
					String date = ProgressiveLogger.dateFormat.format(
						LocalDateTime.now()
					);

					Seq<Fi> autosaves = Vars.saveDirectory.findAll(
						f -> f.name().startsWith("auto_")
					);
					autosaves.sort(f -> -f.lastModified());

					//delete older saves
					if (autosaves.size >= max) {
						for (int i = max - 1; i < autosaves.size; i++) {
							autosaves.get(i).delete();
						}
					}

					String fileName =
						"auto_" +
						mapName +
						"_" +
						date +
						"." +
						Vars.saveExtension;
					Fi file = Vars.saveDirectory.child(fileName);
					Log.info("Autosaving...");

					try {
						SaveIO.save(file);
						Log.info("Autosave completed.");
					} catch (Throwable e) {
						Log.err("Autosave failed.", e);
					}
				}
			}
		};
	}

	@Override
	public Trigger getEmitterClass() {
		return EventType.Trigger.update;
	}
}
