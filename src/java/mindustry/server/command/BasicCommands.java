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
// 	}
// }
