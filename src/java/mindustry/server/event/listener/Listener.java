package mindustry.server.event.listener;

import arc.func.Cons;

public interface Listener<T> {
	public Cons<T> getListener();
	public Class<T> getListenerClass();
}
