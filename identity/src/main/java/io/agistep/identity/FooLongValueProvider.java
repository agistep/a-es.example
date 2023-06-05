package io.agistep.identity;

import java.util.concurrent.atomic.AtomicLong;

class FooLongValueProvider implements IdentityValueProvider {

	static IdentityValueProvider instance() {
		return new FooLongValueProvider();
	}

	final static AtomicLong seq = new AtomicLong(0L);

	@Override
	public long newLong() {
		return seq.getAndIncrement();
	}


	private FooLongValueProvider() {
	}

}
