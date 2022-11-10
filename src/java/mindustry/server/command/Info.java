package mindustry.server.command;

import arc.func.Boolf;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log.LogLevel;
import mindustry.Vars;
import mindustry.net.Administration.PlayerInfo;
import mindustry.server.utils.Bundler;
import mindustry.server.utils.Pipe;

public class Info implements ServerRegistrableCommand {

	@Override
	public void listener(String[] args) throws Throwable {
		ObjectSet<PlayerInfo> playerInfos = Vars.netServer.admins.findByName(
			args[0]
		);

		if (playerInfos.size <= 0) Bundler.logLocalized(
			LogLevel.info,
			"commands.info.no_player",
			args[0]
		); else {
			Bundler.logLocalized(
				LogLevel.info,
				"commands.info.players_found",
				args[0]
			);

			int i = 0;
			for (PlayerInfo playerInfo : playerInfos) {
				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.trace_info",
					i++,
					playerInfo.lastName,
					playerInfo.id
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.1",
					playerInfo.names
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.2",
					playerInfo.lastIP
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.3",
					playerInfo.ips
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.4",
					playerInfo.timesJoined
				);

				Bundler.logLocalized(
					LogLevel.info,
					"commands.info.player_info.5",
					playerInfo.timesKicked
				);
			}
		}
	}

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getDescription() {
		return Bundler.getLocalized("commands.info.description");
	}

	@Override
	public String getParams() {
		return "<ip/uuid/name...>";
	}

	public static PlayerInfo getPlayerInfoOrError(String identify) {
		return getPlayerInfoOrError(identify, playerInfo -> true);
	}

	public static PlayerInfo getPlayerInfoOrError(
		String identify,
		Boolf<PlayerInfo> filter
	) {
		ObjectSet<PlayerInfo> playersInfo = Vars.netServer.admins.findByName(
			identify
		);

		Seq<PlayerInfo> playersInfoSeq = playersInfo.toSeq().filter(filter);

		if (playersInfoSeq.size <= 0) {
			Bundler.logLocalized(
				LogLevel.err,
				"commands.info.player_not_found"
			);
			return null;
		} else if (playersInfoSeq.size > 1) {
			Bundler.logLocalized(
				LogLevel.err,
				"commands.info.too_many_found",
				String.join(
					", ",
					Pipe
						.apply(playersInfoSeq)
						.pipe(seq ->
							seq.map(userInfo ->
								Bundler.getLocalized(
									"commands.info.too_many_found.player_info",
									userInfo.id,
									userInfo.ips
								)
							)
						)
						.result()
				)
			);

			return null;
		}

		return playersInfoSeq.first();
	}
}
