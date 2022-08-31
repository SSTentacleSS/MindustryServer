package mindustry.server.command;

import arc.Core;
import arc.files.Fi;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.core.GameState.State;
import mindustry.io.SaveIO;
import mindustry.server.utils.Bundler;

public class Load implements ServerRegistrableCommand {

    @Override
    public void listener(String[] args) throws Throwable {
        if (Host.errorIfPlaying()) return;

        Fi file = Vars.saveDirectory.child(args[0] + "." + Vars.saveExtension);

        if (!SaveIO.isSaveValid(file))
            Bundler.logLocalized(LogLevel.err, "commands.load.no_save_data");
        else Core.app.post(
            () -> {
                try {
                    SaveIO.load(file);
                    Vars.state.rules.sector = null;
                    Bundler.logLocalized(LogLevel.info, "commands.load.loaded");
                    Vars.state.set(State.playing);
                    Vars.netServer.openServer();
                } catch (Throwable error) {
                    Bundler.logLocalized(LogLevel.err, "commands.load.load_error");
                }
            }
        );
    }

    @Override
    public String getName() {
        return "load";
    }

    @Override
    public String getDescription() {
        return Bundler.getLocalized("commands.load.description");
    }

    @Override
    public String getParams() {
        return "<slot>";
    }
    
}
