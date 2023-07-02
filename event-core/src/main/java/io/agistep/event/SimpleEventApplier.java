package io.agistep.event;

class SimpleEventApplier implements EventApplier {

	public static EventApplier instance() {
		return new SimpleEventApplier();
	}

	private SimpleEventApplier() {
	}

	@Override
	public void replay(Object aggregate, Event anEvent) {
		Replier.reply(aggregate, anEvent);
	}

	@Override
	public void occurred(Event anEvent) {
		EventList.instance().occurs(anEvent);
	}

}
