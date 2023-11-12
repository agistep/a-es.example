package io.agistep.identity;

class RandomProvider implements IdentityValueProvider {

	static IdentityValueProvider instance() {
		return new RandomProvider();
	}

	@Override
	public long newLong() {
		return (long) (Math.random() * 1000000000);
	}


	private RandomProvider() {
	}

}
