package mindustry.server.command;

import mindustry.server.utils.Bundler;

public class NextMap implements ServerRegistrableCommand {

    @Override
    public void listener(String[] args) throws Throwable {
        
    }

    @Override
    public String getName() {
        return "nextmap";
    }

    @Override
    public String getDescription() {
        return Bundler.getLocalized("commands.nextmap.description");
    }

    @Override
    public String getParams() {
        return "<mapname...>";
    }
    
}
