package io.agistep.identity;

public interface IdentityValueProvider {
	static IdentityValueProvider instance() {
		return RandomProvider.instance();
	}

	long newLong();
}
