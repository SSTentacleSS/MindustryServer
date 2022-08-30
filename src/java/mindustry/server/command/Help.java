package mindustry.server.command;

import arc.struct.Seq;
import arc.util.CommandHandler.Command;
import arc.util.Log;
import mindustry.server.StateController;
import mindustry.server.utils.Bundler;
import mindustry.server.utils.Pipe;

public class Help implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) {
		Seq<Command> commands = StateController.commandsRegistry.getCommandList();

		if (args.length == 0) {
			Pipe
				.apply("commands.help.founded")
				.pipe(Bundler::getLocalized, commands.size)
				.result(Log::info);

			commands.forEach(
				command -> {
					Log.info(helpStringFromCommand(command));
				}
			);
		} else {
			Pipe
				.apply(
					commands.find(
						command -> command.text.equalsIgnoreCase(args[0])
					)
				)
				.result(
					command -> {
						if (command == null) Log.err(
							Bundler.getLocalized(
								"commands.help.not_founded",
								args[0]
							)
						); else {
							Log.info(command.text + ":");
							Log.info(helpStringFromCommand(command));
						}
					}
				);
		}
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.help.description");
	}

	@Override
	public String getParams() {
		return "[command]";
	}

	private String helpStringFromCommand(Command command) {
		return (
			"  &b&lb " +
			command.text +
			(command.paramText.isEmpty() ? "" : " &lc&fi") +
			command.paramText +
			"&fr - &lw" +
			command.description
		);
	}
}
