package mindustry.server.command;

import arc.util.CommandHandler;

public interface RegistrableCommand {
	public void register(CommandHandler handler);
}
