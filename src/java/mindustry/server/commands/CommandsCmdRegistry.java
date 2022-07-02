package mindustry.server.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jline.console.ArgDesc;
import org.jline.console.CmdDesc;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import arc.util.CommandHandler.Command;
import arc.util.CommandHandler.CommandParam;
import mindustry.server.log.Colors;
import mindustry.server.utils.Pipe;

public class CommandsCmdRegistry
{
    public Map<String, CmdDesc> tailTips = new HashMap<>();
    public List<Command> registeredCommands = new ArrayList<>();

    public void registerCommand(Command command)
    {
        List<String> parameters = new ArrayList<>();

        for (int i = 0; i < command.params.length; i++)
        {
            CommandParam parameter = command.params[i];
            Pipe.apply(parameter.name)
                .pipe(text -> text.concat(parameter.variadic ? "..." : ""))
                .pipe(text -> parameter.optional ? "[" + text + "]" : "<" + text + ">")
                .pipe(text -> parameters.add(text));
        }

        registeredCommands.add(command);
        tailTips.put(
            command.text,
            new CmdDesc(
                Arrays.asList(
                    Pipe.apply(command.description)
                        .pipe(Colors::applyStyle, AttributedStyle.BOLD.foreground(255, 15, 15))
                        .pipe(text -> new AttributedString(text))
                        .result()
                ),
                ArgDesc.doArgNames(parameters),
                new HashMap<>()
            )
        );
    }
}
