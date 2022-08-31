package mindustry.server.command;

import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.server.utils.Bundler;

public class RunWave implements ServerRegistrableCommand {

    @Override
    public void listener(String[] args) throws Throwable {
        if (!Host.errorIfNotPlaying()) {
            Vars.logic.runWave();
            Bundler.logLocalized(LogLevel.info, "commands.runwave.wave_spawned");
        }
    }

    @Override
    public String getName() {
        return "runwave";
    }

    @Override
    public String getDescription() {
        return Bundler.getLocalized("commands.runwave.description");
    }

    @Override
    public String getParams() {
        return "";
    }
    
}
