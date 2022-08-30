package mindustry.server.events;

public interface Emitter<T> {
	public Runnable getEmitter();
	public T getEmitterClass();
}
