package mindustry.server.command;

import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.CommandHandler.Command;
import arc.util.Log;
import mindustry.server.utils.Bundler;
import mindustry.server.utils.Pipe;

public class Help implements RegistrableCommand {

	@Override
	public void register(CommandHandler handler) {
		handler.register(
			"help",
			Bundler.getLocalized("commands.help.description"),
			args -> {
				Seq<Command> commands = handler.getCommandList();

				if (args.length == 0) {
					Pipe
						.apply("commands.help.founded")
						.pipe(Bundler::getLocalized, commands.size)
						.result(Log::info);

					commands.forEach(
						command -> {
                            // StringBuilder
                        }
					);
				}
			}
		);
	}
}
