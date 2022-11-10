package mindustry.server.events;

public interface Listener<T> {
	public void listener(T event);

	public Class<T> getListenerClass();
}
