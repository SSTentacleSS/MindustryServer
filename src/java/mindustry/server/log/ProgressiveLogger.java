package mindustry.server.log;

import arc.util.Log;
import arc.util.Log.LogHandler;
import arc.util.Log.LogLevel;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import mindustry.server.utils.Colors;
import mindustry.server.utils.Pipe;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.SystemCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStyle;

public class ProgressiveLogger implements LogHandler {

	private static final AtomicReference<SimpleDateFormat> dateFormat = new AtomicReference<>(
		new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss.SSS]")
	);

	private volatile String inputPrompt = Colors.applyStyle(
		"[Server] |> ",
		AttributedStyle.BOLD.foreground(170, 255, 50)
	);

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private SystemCompleter systemCompleter = new SystemCompleter();
	private Terminal terminal;
	private LineReader reader;

	public ProgressiveLogger() {
		init();
	}

	public void init() {
		try {
			terminal =
				TerminalBuilder
					.builder()
					.jna(true)
					.name("Mindustry Server I/O")
					.system(true)
					.build();

			reader =
				LineReaderBuilder
					.builder()
					.terminal(terminal)
					.appName("Mindustry Server")
					.build();
			terminal.enterRawMode();
		} catch (IOException e) {
			Log.err(e);
		}
	}

	@Override
	public void log(LogLevel level, String text) {
		String renderedText = "";

		switch (level) {
			case debug:
				renderedText =
					Pipe
						.apply(" [D] " + text)
						.pipe(
							Colors::applyStyle,
							AttributedStyle.BOLD.foreground(170, 255, 50)
						)
						.pipe(ProgressiveLogger::addTimestamp)
						.result();
				break;
			case err:
				renderedText =
					Pipe
						.apply(" [E] " + text)
						.pipe(
							Colors::applyStyle,
							AttributedStyle.BOLD.foreground(255, 15, 15)
						)
						.pipe(ProgressiveLogger::addTimestamp)
						.result();
				break;
			case info:
				renderedText =
					Pipe
						.apply(" [I] " + text)
						.pipe(
							Colors::applyStyle,
							AttributedStyle.BOLD.foreground(0, 177, 210)
						)
						.pipe(ProgressiveLogger::addTimestamp)
						.result();
				break;
			case warn:
				renderedText =
					Pipe
						.apply(" [W] " + text)
						.pipe(
							Colors::applyStyle,
							AttributedStyle.BOLD.foreground(255, 255, 0)
						)
						.pipe(ProgressiveLogger::addTimestamp)
						.result();
				break;
			default:
				log(LogLevel.info, text);
				return;
		}

		reader.printAbove(renderedText);
	}

	public CompletableFuture<String> read() {
		return CompletableFuture.supplyAsync(
			() -> {
				try {
					return reader.readLine(inputPrompt);
				} catch (UserInterruptException err) {
					System.exit(0);
					return null;
				}
			},
			executor
		);
	}

	public static String addTimestamp(String text) {
		return Pipe
			.apply(new Date())
			.pipe(date -> dateFormat.get().format(date))
			.pipe(
				Colors::applyStyle,
				AttributedStyle.BOLD.foreground(128, 128, 128)
			)
			.pipe(string -> string.concat(text))
			.result();
	}

	public static AtomicReference<SimpleDateFormat> getDateformat() {
		return dateFormat;
	}

	public String getInputPrompt() {
		return inputPrompt;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public SystemCompleter getSystemCompleter() {
		return systemCompleter;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public LineReader getReader() {
		return reader;
	}
}
