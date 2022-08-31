package mindustry.server.command;

import arc.Core;
import arc.util.Log.LogLevel;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonValue;
import arc.util.serialization.JsonValue.ValueType;
import java.util.List;
import mindustry.Vars;
import mindustry.gen.Call;
import mindustry.io.JsonIO;
import mindustry.server.utils.Bundler;

public class Rules implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		String rules = Core.settings.getString("globalrules");
		JsonValue base = JsonIO.json.fromJson(null, rules);

		if (args.length == 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.rules.list",
			JsonIO.print(rules)
		); else if (args.length == 1) Bundler.logLocalized(
			LogLevel.err,
			"commands.rules.invalid_usage.rule"
		); else {
			if (!List.of("remove", "add").contains(args[0])) {
				Bundler.logLocalized(
					LogLevel.err,
					"commands.rules.invalid_usage.add_or_remove"
				);

				return;
			}

			boolean isAdd = args[0].equals("add");

			if (isAdd && args.length < 3) {
				Bundler.logLocalized(
					LogLevel.err,
					"commands.rules.invalid_usage.last_argument"
				);

				return;
			}

			if (!isAdd) {
				if (base.has(args[1])) {
					Bundler.logLocalized(
						LogLevel.info,
						"commands.rules.removed",
						args[1]
					);

					base.remove(args[1]);
				} else {
					Bundler.logLocalized(
						LogLevel.err,
						"commands.rules.invalid_usage.not_defined"
					);

					return;
				}
			} else {
				try {
					JsonValue value = new JsonReader().parse(args[2]);
					value.name = args[1];

					JsonValue parent = new JsonValue(ValueType.object);
					parent.addChild(value);
					JsonIO.json.readField(Vars.state.rules, value.name, parent);

					if (base.has(value.name)) base.remove(value.name);

					base.addChild(args[1], value);
					Bundler.logLocalized(
						LogLevel.info,
						"commands.rules.changed",
						value.toString().replace('\n', ' ')
					);
				} catch (Exception error) {
					Bundler.logLocalized(
						LogLevel.err,
						"commands.rules.error_parsing",
						error.getMessage()
					);
				}
			}

			Core.settings.put("globalrules", base.toString());
			Call.setRules(Vars.state.rules);
		}
	}

	@Override
	public String getName() {
		return "rules";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.rules.description");
	}

	@Override
	public String getParams() {
		return "[remove/add] [name] [value...]";
	}
}
