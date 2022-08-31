// package mindustry.server.command;

// import static arc.util.Log.*;
// import static mindustry.Vars.*;

// import arc.*;
// import arc.files.*;
// import arc.struct.*;
// import arc.util.*;
// import arc.util.serialization.*;
// import arc.util.serialization.JsonValue.*;
// import mindustry.core.GameState.*;
// import mindustry.game.*;
// import mindustry.game.EventType.*;
// import mindustry.gen.*;
// import mindustry.io.*;
// import mindustry.maps.*;
// import mindustry.maps.Map;
// import mindustry.maps.Maps.*;
// import mindustry.mod.Mods.*;
// import mindustry.net.Administration.*;
// import mindustry.net.Packets.*;
// import mindustry.server.StateController;
// import mindustry.type.*;

// // TODO: Sort commands by file
// public class BasicCommands {

// 	public void register(CommandHandler handler) {

// 		handler.register(
// 			"js",
// 			"<script...>",
// 			"Run arbitrary Javascript.",
// 			arg -> {
// 				info("&fi&lw&fb" + mods.getScripts().runConsole(arg[0]));
// 			}
// 		);

// 		handler.register(
// 			"say",
// 			"<message...>",
// 			"Send a message to all players.",
// 			arg -> {
// 				if (!state.is(State.playing)) {
// 					err("Not hosting. Host a game first.");
// 					return;
// 				}

// 				Call.sendMessage("[scarlet][[Server]:[] " + arg[0]);

// 				info("&fi&lcServer: &fr@", "&lw" + arg[0]);
// 			}
// 		);

// 		handler.register(
// 			"pause",
// 			"<on/off>",
// 			"Pause or unpause the game.",
// 			arg -> {
// 				boolean pause = arg[0].equals("on");
// 				state.serverPaused = pause;
// 				info(pause ? "Game paused." : "Game unpaused.");
// 			}
// 		);

// 		handler.register(
// 			"rules",
// 			"[remove/add] [name] [value...]",
// 			"List, remove or add global rules. These will apply regardless of map.",
// 			arg -> {
// 				String rules = Core.settings.getString("globalrules");
// 				JsonValue base = JsonIO.json.fromJson(null, rules);

// 				if (arg.length == 0) {
// 					info("Rules:\n@", JsonIO.print(rules));
// 				} else if (arg.length == 1) {
// 					err("Invalid usage. Specify which rule to remove or add.");
// 				} else {
// 					if (!(arg[0].equals("remove") || arg[0].equals("add"))) {
// 						err("Invalid usage. Either add or remove rules.");
// 						return;
// 					}

// 					boolean remove = arg[0].equals("remove");
// 					if (remove) {
// 						if (base.has(arg[1])) {
// 							info("Rule '@' removed.", arg[1]);
// 							base.remove(arg[1]);
// 						} else {
// 							err("Rule not defined, so not removed.");
// 							return;
// 						}
// 					} else {
// 						if (arg.length < 3) {
// 							err(
// 								"Missing last argument. Specify which value to set the rule to."
// 							);
// 							return;
// 						}

// 						try {
// 							JsonValue value = new JsonReader().parse(arg[2]);
// 							value.name = arg[1];

// 							JsonValue parent = new JsonValue(ValueType.object);
// 							parent.addChild(value);

// 							JsonIO.json.readField(
// 								state.rules,
// 								value.name,
// 								parent
// 							);
// 							if (base.has(value.name)) {
// 								base.remove(value.name);
// 							}
// 							base.addChild(arg[1], value);
// 							info(
// 								"Changed rule: @",
// 								value.toString().replace("\n", " ")
// 							);
// 						} catch (Throwable e) {
// 							err("Error parsing rule JSON: @", e.getMessage());
// 						}
// 					}

// 					Core.settings.put("globalrules", base.toString());
// 					Call.setRules(state.rules);
// 				}
// 			}
// 		);

// 		handler.register(
// 			"fillitems",
// 			"[team]",
// 			"Fill the core with items.",
// 			arg -> {
// 				if (!state.is(State.playing)) {
// 					err("Not playing. Host first.");
// 					return;
// 				}

// 				Team team = arg.length == 0
// 					? Team.sharded
// 					: Structs.find(Team.all, t -> t.name.equals(arg[0]));

// 				if (team == null) {
// 					err("No team with that name found.");
// 					return;
// 				}

// 				if (state.teams.cores(team).isEmpty()) {
// 					err("That team has no cores.");
// 					return;
// 				}

// 				for (Item item : content.items()) {
// 					state
// 						.teams.cores(team)
// 						.first()
// 						.items.set(
// 							item,
// 							state.teams.cores(team).first().storageCapacity
// 						);
// 				}

// 				info("Core filled.");
// 			}
// 		);

// 		handler.register(
// 			"playerlimit",
// 			"[off/somenumber]",
// 			"Set the server player limit.",
// 			arg -> {
// 				if (arg.length == 0) {
// 					info(
// 						"Player limit is currently @.",
// 						netServer.admins.getPlayerLimit() == 0
// 							? "off"
// 							: netServer.admins.getPlayerLimit()
// 					);
// 					return;
// 				}
// 				if (arg[0].equals("off")) {
// 					netServer.admins.setPlayerLimit(0);
// 					info("Player limit disabled.");
// 					return;
// 				}

// 				if (
// 					Strings.canParsePositiveInt(arg[0]) &&
// 					Strings.parseInt(arg[0]) > 0
// 				) {
// 					int lim = Strings.parseInt(arg[0]);
// 					netServer.admins.setPlayerLimit(lim);
// 					info("Player limit is now &lc@.", lim);
// 				} else {
// 					err("Limit must be a number above 0.");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"config",
// 			"[name] [value...]",
// 			"Configure server settings.",
// 			arg -> {
// 				if (arg.length == 0) {
// 					info("All config values:");
// 					for (Config c : Config.all) {
// 						info("&lk| @: @", c.name, "&lc&fi" + c.get());
// 						info("&lk| | &lw" + c.description);
// 						info("&lk|");
// 					}
// 					return;
// 				}

// 				Config c = Config.all.find(
// 					param -> param.name.equalsIgnoreCase(arg[0])
// 				);

// 				try {
// 					if (arg.length == 1) {
// 						info("'@' is currently @.", c.name, c.get());
// 					} else {
// 						if (c.isBool()) {
// 							c.set(arg[1].equals("on") || arg[1].equals("true"));
// 						} else if (c.isNum()) {
// 							try {
// 								c.set(Integer.parseInt(arg[1]));
// 							} catch (NumberFormatException e) {
// 								err("Not a valid number: @", arg[1]);
// 								return;
// 							}
// 						} else if (c.isString()) {
// 							c.set(arg[1].replace("\\n", "\n"));
// 						}

// 						info("@ set to @.", c.name, c.get());
// 						Core.settings.forceSave();
// 					}
// 				} catch (IllegalArgumentException e) {
// 					err(
// 						"Unknown config: '@'. Run the command with no arguments to get a list of valid configs.",
// 						arg[0]
// 					);
// 				}
// 			}
// 		);

// 		handler.register(
// 			"subnet-ban",
// 			"[add/remove] [address]",
// 			"Ban a subnet. This simply rejects all connections with IPs starting with some string.",
// 			arg -> {
// 				if (arg.length == 0) {
// 					info(
// 						"Subnets banned: @",
// 						netServer.admins.getSubnetBans().isEmpty()
// 							? "<none>"
// 							: ""
// 					);
// 					for (String subnet : netServer.admins.getSubnetBans()) {
// 						info("&lw  " + subnet);
// 					}
// 				} else if (arg.length == 1) {
// 					err("You must provide a subnet to add or remove.");
// 				} else {
// 					if (arg[0].equals("add")) {
// 						if (netServer.admins.getSubnetBans().contains(arg[1])) {
// 							err("That subnet is already banned.");
// 							return;
// 						}

// 						netServer.admins.addSubnetBan(arg[1]);
// 						info("Banned @**", arg[1]);
// 					} else if (arg[0].equals("remove")) {
// 						if (
// 							!netServer.admins.getSubnetBans().contains(arg[1])
// 						) {
// 							err("That subnet isn't banned.");
// 							return;
// 						}

// 						netServer.admins.removeSubnetBan(arg[1]);
// 						info("Unbanned @**", arg[1]);
// 					} else {
// 						err(
// 							"Incorrect usage. Provide add/remove as the second argument."
// 						);
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"whitelist",
// 			"[add/remove] [ID]",
// 			"Add/remove players from the whitelist using their ID.",
// 			arg -> {
// 				if (arg.length == 0) {
// 					Seq<PlayerInfo> whitelist = netServer.admins.getWhitelisted();

// 					if (whitelist.isEmpty()) {
// 						info("No whitelisted players found.");
// 					} else {
// 						info("Whitelist:");
// 						whitelist.each(
// 							p -> info("- Name: @ / UUID: @", p.lastName, p.id)
// 						);
// 					}
// 				} else {
// 					if (arg.length == 2) {
// 						PlayerInfo info = netServer.admins.getInfoOptional(
// 							arg[1]
// 						);

// 						if (info == null) {
// 							err(
// 								"Player ID not found. You must use the ID displayed when a player joins a server."
// 							);
// 						} else {
// 							if (arg[0].equals("add")) {
// 								netServer.admins.whitelist(arg[1]);
// 								info(
// 									"Player '@' has been whitelisted.",
// 									info.lastName
// 								);
// 							} else if (arg[0].equals("remove")) {
// 								netServer.admins.unwhitelist(arg[1]);
// 								info(
// 									"Player '@' has been un-whitelisted.",
// 									info.lastName
// 								);
// 							} else {
// 								err(
// 									"Incorrect usage. Provide add/remove as the second argument."
// 								);
// 							}
// 						}
// 					} else {
// 						err("Incorrect usage. Provide an ID to add or remove.");
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"shuffle",
// 			"[none/all/custom/builtin]",
// 			"Set map shuffling mode.",
// 			arg -> {
// 				if (arg.length == 0) {
// 					info(
// 						"Shuffle mode current set to '@'.",
// 						maps.getShuffleMode()
// 					);
// 				} else {
// 					try {
// 						ShuffleMode mode = ShuffleMode.valueOf(arg[0]);
// 						Core.settings.put("shufflemode", mode.name());
// 						maps.setShuffleMode(mode);
// 						info("Shuffle mode set to '@'.", arg[0]);
// 					} catch (Exception e) {
// 						err("Invalid shuffle mode.");
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"nextmap",
// 			"<mapname...>",
// 			"Set the next map to be played after a game-over. Overrides shuffling.",
// 			arg -> {
// 				Map res = maps
// 					.all()
// 					.find(
// 						map ->
// 							Strings
// 								.stripColors(map.name().replace('_', ' '))
// 								.equalsIgnoreCase(
// 									Strings
// 										.stripColors(arg[0])
// 										.replace('_', ' ')
// 								)
// 					);
// 				if (res != null) {
// 					StateController.nextMapOverride = res;
// 					info(
// 						"Next map set to '@'.",
// 						Strings.stripColors(res.name())
// 					);
// 				} else {
// 					err("No map '@' found.", arg[0]);
// 				}
// 			}
// 		);

// 		handler.register(
// 			"kick",
// 			"<username...>",
// 			"Kick a person by name.",
// 			arg -> {
// 				if (!state.is(State.playing)) {
// 					err("Not hosting a game yet. Calm down.");
// 					return;
// 				}

// 				Player target = Groups.player.find(
// 					p -> p.name().equals(arg[0])
// 				);

// 				if (target != null) {
// 					Call.sendMessage(
// 						"[scarlet]" +
// 						target.name() +
// 						"[scarlet] has been kicked by the server."
// 					);
// 					target.kick(KickReason.kick);
// 					info("It is done.");
// 				} else {
// 					info("Nobody with that name could be found...");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"ban",
// 			"<type-id/name/ip> <username/IP/ID...>",
// 			"Ban a person.",
// 			arg -> {
// 				if (arg[0].equals("id")) {
// 					netServer.admins.banPlayerID(arg[1]);
// 					info("Banned.");
// 				} else if (arg[0].equals("name")) {
// 					Player target = Groups.player.find(
// 						p -> p.name().equalsIgnoreCase(arg[1])
// 					);
// 					if (target != null) {
// 						netServer.admins.banPlayer(target.uuid());
// 						info("Banned.");
// 					} else {
// 						err("No matches found.");
// 					}
// 				} else if (arg[0].equals("ip")) {
// 					netServer.admins.banPlayerIP(arg[1]);
// 					info("Banned.");
// 				} else {
// 					err("Invalid type.");
// 				}

// 				for (Player player : Groups.player) {
// 					if (netServer.admins.isIDBanned(player.uuid())) {
// 						Call.sendMessage(
// 							"[scarlet]" + player.name + " has been banned."
// 						);
// 						player.con.kick(KickReason.banned);
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"bans",
// 			"List all banned IPs and IDs.",
// 			arg -> {
// 				Seq<PlayerInfo> bans = netServer.admins.getBanned();

// 				if (bans.size == 0) {
// 					info("No ID-banned players have been found.");
// 				} else {
// 					info("Banned players [ID]:");
// 					for (PlayerInfo info : bans) {
// 						info(
// 							" @ / Last known name: '@'",
// 							info.id,
// 							info.lastName
// 						);
// 					}
// 				}

// 				Seq<String> ipbans = netServer.admins.getBannedIPs();

// 				if (ipbans.size == 0) {
// 					info("No IP-banned players have been found.");
// 				} else {
// 					info("Banned players [IP]:");
// 					for (String string : ipbans) {
// 						PlayerInfo info = netServer.admins.findByIP(string);
// 						if (info != null) {
// 							info(
// 								"  '@' / Last known name: '@' / ID: '@'",
// 								string,
// 								info.lastName,
// 								info.id
// 							);
// 						} else {
// 							info("  '@' (No known name or info)", string);
// 						}
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"unban",
// 			"<ip/ID>",
// 			"Completely unban a person by IP or ID.",
// 			arg -> {
// 				if (
// 					netServer.admins.unbanPlayerIP(arg[0]) ||
// 					netServer.admins.unbanPlayerID(arg[0])
// 				) {
// 					info("Unbanned player: @", arg[0]);
// 				} else {
// 					err("That IP/ID is not banned!");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"pardon",
// 			"<ID>",
// 			"Pardons a votekicked player by ID and allows them to join again.",
// 			arg -> {
// 				PlayerInfo info = netServer.admins.getInfoOptional(arg[0]);

// 				if (info != null) {
// 					info.lastKicked = 0;
// 					info("Pardoned player: @", info.lastName);
// 				} else {
// 					err("That ID can't be found.");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"admin",
// 			"<add/remove> <username/ID...>",
// 			"Make an online user admin",
// 			arg -> {
// 				if (!state.is(State.playing)) {
// 					err("Open the server first.");
// 					return;
// 				}

// 				if (!(arg[0].equals("add") || arg[0].equals("remove"))) {
// 					err("Second parameter must be either 'add' or 'remove'.");
// 					return;
// 				}

// 				boolean add = arg[0].equals("add");

// 				PlayerInfo target;
// 				Player playert = Groups.player.find(
// 					p -> p.name.equalsIgnoreCase(arg[1])
// 				);
// 				if (playert != null) {
// 					target = playert.getInfo();
// 				} else {
// 					target = netServer.admins.getInfoOptional(arg[1]);
// 					playert = Groups.player.find(p -> p.getInfo() == target);
// 				}

// 				if (target != null) {
// 					if (add) {
// 						netServer.admins.adminPlayer(
// 							target.id,
// 							target.adminUsid
// 						);
// 					} else {
// 						netServer.admins.unAdminPlayer(target.id);
// 					}
// 					if (playert != null) playert.admin = add;
// 					info("Changed admin status of player: @", target.lastName);
// 				} else {
// 					err(
// 						"Nobody with that name or ID could be found. If adding an admin by name, make sure they're online; otherwise, use their UUID."
// 					);
// 				}
// 			}
// 		);

// 		handler.register(
// 			"admins",
// 			"List all admins.",
// 			arg -> {
// 				Seq<PlayerInfo> admins = netServer.admins.getAdmins();

// 				if (admins.size == 0) {
// 					info("No admins have been found.");
// 				} else {
// 					info("Admins:");
// 					for (PlayerInfo info : admins) {
// 						info(
// 							" &lm @ /  ID: '@' / IP: '@'",
// 							info.lastName,
// 							info.id,
// 							info.lastIP
// 						);
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"players",
// 			"List all players currently in game.",
// 			arg -> {
// 				if (Groups.player.size() == 0) {
// 					info("No players are currently in the server.");
// 				} else {
// 					info("Players: @", Groups.player.size());
// 					for (Player user : Groups.player) {
// 						PlayerInfo userInfo = user.getInfo();
// 						info(
// 							" &lm @ /  ID: @ / IP: @ / Admin: @",
// 							userInfo.lastName,
// 							userInfo.id,
// 							userInfo.lastIP,
// 							userInfo.admin
// 						);
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"runwave",
// 			"Trigger the next wave.",
// 			arg -> {
// 				if (!state.is(State.playing)) {
// 					err("Not hosting. Host a game first.");
// 				} else {
// 					logic.runWave();
// 					info("Wave spawned.");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"load",
// 			"<slot>",
// 			"Load a save from a slot.",
// 			arg -> {
// 				if (state.is(State.playing)) {
// 					err("Already hosting. Type 'stop' to stop hosting first.");
// 					return;
// 				}

// 				Fi file = saveDirectory.child(arg[0] + "." + saveExtension);

// 				if (!SaveIO.isSaveValid(file)) {
// 					err("No (valid) save data found for slot.");
// 					return;
// 				}

// 				Core.app.post(
// 					() -> {
// 						try {
// 							SaveIO.load(file);
// 							state.rules.sector = null;
// 							info("Save loaded.");
// 							state.set(State.playing);
// 							netServer.openServer();
// 						} catch (Throwable t) {
// 							err(
// 								"Failed to load save. Outdated or corrupt file."
// 							);
// 						}
// 					}
// 				);
// 			}
// 		);

// 		handler.register(
// 			"save",
// 			"<slot>",
// 			"Save game state to a slot.",
// 			arg -> {
// 				if (!state.is(State.playing)) {
// 					err("Not hosting. Host a game first.");
// 					return;
// 				}

// 				Fi file = saveDirectory.child(arg[0] + "." + saveExtension);

// 				Core.app.post(
// 					() -> {
// 						SaveIO.save(file);
// 						info("Saved to @.", file);
// 					}
// 				);
// 			}
// 		);

// 		handler.register(
// 			"saves",
// 			"List all saves in the save directory.",
// 			arg -> {
// 				info("Save files: ");
// 				for (Fi file : saveDirectory.list()) {
// 					if (file.extension().equals(saveExtension)) {
// 						info("| @", file.nameWithoutExtension());
// 					}
// 				}
// 			}
// 		);

// 		handler.register(
// 			"gameover",
// 			"Force a game over.",
// 			arg -> {
// 				if (state.isMenu()) {
// 					err("Not playing a map.");
// 					return;
// 				}

// 				info("Core destroyed.");
// 				StateController.inExtraRound = false;
// 				Events.fire(new GameOverEvent(Team.crux));
// 			}
// 		);

// 		handler.register(
// 			"info",
// 			"<IP/UUID/name...>",
// 			"Find player info(s). Can optionally check for all names or IPs a player has had.",
// 			arg -> {
// 				ObjectSet<PlayerInfo> infos = netServer.admins.findByName(
// 					arg[0]
// 				);

// 				if (infos.size > 0) {
// 					info("Players found: @", infos.size);

// 					int i = 0;
// 					for (PlayerInfo info : infos) {
// 						info(
// 							"[@] Trace info for player '@' / UUID @",
// 							i++,
// 							info.lastName,
// 							info.id
// 						);
// 						info("  all names used: @", info.names);
// 						info("  IP: @", info.lastIP);
// 						info("  all IPs used: @", info.ips);
// 						info("  times joined: @", info.timesJoined);
// 						info("  times kicked: @", info.timesKicked);
// 					}
// 				} else {
// 					info("Nobody with that name could be found.");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"search",
// 			"<name...>",
// 			"Search players who have used part of a name.",
// 			arg -> {
// 				ObjectSet<PlayerInfo> infos = netServer.admins.searchNames(
// 					arg[0]
// 				);

// 				if (infos.size > 0) {
// 					info("Players found: @", infos.size);

// 					int i = 0;
// 					for (PlayerInfo info : infos) {
// 						info("- [@] '@' / @", i++, info.lastName, info.id);
// 					}
// 				} else {
// 					info("Nobody with that name could be found.");
// 				}
// 			}
// 		);

// 		handler.register(
// 			"gc",
// 			"Trigger a garbage collection. Testing only.",
// 			arg -> {
// 				int pre = (int) (Core.app.getJavaHeap() / 1024 / 1024);
// 				System.gc();
// 				int post = (int) (Core.app.getJavaHeap() / 1024 / 1024);
// 				info(
// 					"@ MB collected. Memory usage now at @ MB.",
// 					pre - post,
// 					post
// 				);
// 			}
// 		);
// 	}
// }
