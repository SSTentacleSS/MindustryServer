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
// 	}
// }
