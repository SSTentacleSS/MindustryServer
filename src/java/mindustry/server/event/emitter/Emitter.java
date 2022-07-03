package mindustry.server.event.emitter;

public interface Emitter<T> {
	public Runnable getEmitter();
	public T getEmitterClass();
}
