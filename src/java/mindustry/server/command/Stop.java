package mindustry.server.command;

import arc.util.Log;
import mindustry.Vars;
import mindustry.core.GameState.State;
import mindustry.server.StateController;
import mindustry.server.utils.Bundler;

public class Stop implements ServerRegistrableCommand {

    @Override
    public void listener(String[] args) {
        StateController.serverTimer.clear();
        Vars.state.set(State.menu);
        Log.info(Bundler.getLocalized("server.stopped"));
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return Bundler.getLocalized("commands.stop.description");
    }

    @Override
    public String getParams() {
        return "";
    }
    
}
