package mindustry.server;

import arc.ApplicationListener;
import arc.Core;
import arc.files.Fi;
import arc.util.CommandHandler;
import mindustry.game.Gamemode;

// TODO; Finish and use as the main controller
public class CustomServerController implements ApplicationListener
{
    public CommandHandler handler = new CommandHandler("");
    public Fi logFolder = Core.settings
        .getDataDirectory()
        .child("logs/");
    public Gamemode gamemode;

    public CustomServerController()
    {
        valueOf(Gamemode.all, Core.settings.getString("lastServerMode", "survival"), Gamemode.survival);
    }

    public static <T extends Enum<T>> T valueOf(T[] values, String name, T defaultValue)
    {
        for (T value : values)
            if (value.name().equals(name))
                return value;
        return defaultValue;
    }
}
