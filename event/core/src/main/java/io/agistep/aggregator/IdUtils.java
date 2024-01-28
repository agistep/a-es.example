package io.agistep.aggregator;


import io.agistep.identity.IdentifierProvider;
import io.agistep.identity.spi.IdentifierProviderFactory;

public final class IdUtils {

	private static final IdentifierProvider IDENTIFIER_PROVIDER = IdentifierProviderFactory.load().get();

	public static long idOf(Aggregate aggregate) {
		return aggregate.getId();
	}

	public static boolean notAssignedIdOf(Aggregate aggregate) {
		return aggregate.getId() == 0L;
	}

	public static long gen() {
		return IDENTIFIER_PROVIDER.nextId();
	}
}
