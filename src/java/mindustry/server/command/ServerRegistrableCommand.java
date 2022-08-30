package mindustry.server.command;

public interface ServerRegistrableCommand {
	public void listener(String[] args);
	public String getName();
	public String getDescription();
	public String getParams();
}
