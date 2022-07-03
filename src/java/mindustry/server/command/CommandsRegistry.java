package mindustry.server.command;

import arc.util.CommandHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mindustry.server.utils.Pipe;
import org.jline.console.ArgDesc;
import org.jline.console.CmdDesc;

public class CommandsRegistry extends CommandHandler {

	public CommandsRegistry(String prefix) {
		super(prefix);
	}

	public Map<String, CmdDesc> tailTips = new HashMap<>();
	public List<Command> registeredCommands = new ArrayList<>();

	@Override
	public <T> Command register(
		String text,
		String params,
		String description,
		CommandRunner<T> runner
	) {
		Command command = super.register(text, params, description, runner);
		registerCommand(command);

		return command;
	}

	public void registerCommand(Command command) {
		List<String> parameters = new ArrayList<>();

		for (int i = 0; i < command.params.length; i++) {
			CommandParam parameter = command.params[i];
			Pipe
				.apply(parameter.name)
				.pipe(text -> text.concat(parameter.variadic ? "..." : ""))
				.pipe(
					text ->
						parameter.optional ? "[" + text + "]" : "<" + text + ">"
				)
				.pipe(text -> parameters.add(text));
		}

		registeredCommands.add(command);
		tailTips.put(command.text, new CmdDesc(ArgDesc.doArgNames(parameters)));
	}

	@Override
	public void removeCommand(String commandName) {
		tailTips.remove(commandName);
		registeredCommands.removeIf(command -> command.text == commandName);

		super.removeCommand(commandName);
	}
}
