package mindustry.server.command;

public interface ServerRegistrableCommand {
	public void listener(String[] args) throws Throwable;
	public String getName();
	public String getDescription();
	public String getParams();
}
