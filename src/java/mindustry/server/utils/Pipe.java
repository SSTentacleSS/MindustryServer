package mindustry.server.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Pipe<A> {

	@FunctionalInterface
	public interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}

	@FunctionalInterface
	public interface QuadriFunction<A, B, C, D, R> {
		R apply(A a, B b, C c, D d);
	}

	private A object;

	private Pipe(A object) {
		this.object = object;
	}

	public static <A> Pipe<A> apply(A object) {
		return new Pipe<A>(object);
	}

	public <R> Pipe<R> pipe(Function<A, R> func) {
		return new Pipe<R>(func.apply(object));
	}

	public <R, B> Pipe<R> pipe(BiFunction<A, B, R> func, B argObject1) {
		return new Pipe<R>(func.apply(object, argObject1));
	}

	public <R, B, C> Pipe<R> pipe(
		TriFunction<A, B, C, R> func,
		B argObject1,
		C argObject2
	) {
		return new Pipe<R>(func.apply(object, argObject1, argObject2));
	}

	public <R, B, C, D> Pipe<R> pipe(
		QuadriFunction<A, B, C, D, R> func,
		B argObject1,
		C argObject2,
		D argObject3
	) {
		return new Pipe<R>(
			func.apply(object, argObject1, argObject2, argObject3)
		);
	}

	public A result() {
		return object;
	}
}
