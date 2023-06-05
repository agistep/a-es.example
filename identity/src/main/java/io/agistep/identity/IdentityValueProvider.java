package io.agistep.identity;

public interface IdentityValueProvider {
	static IdentityValueProvider instance() {
		return FooLongValueProvider.instance();
	}

	long newLong();
}
