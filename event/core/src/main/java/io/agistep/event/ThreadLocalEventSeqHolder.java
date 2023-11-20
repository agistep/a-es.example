package io.agistep.event;

import java.util.HashMap;
import java.util.Map;

class ThreadLocalEventSeqHolder {

	private final static ThreadLocal<Map<Long,Long>> changes = ThreadLocal.withInitial(HashMap::new);

	public static ThreadLocalEventSeqHolder instance() {
		return new ThreadLocalEventSeqHolder();
	}

	private ThreadLocalEventSeqHolder() {
		// Do Nothing
	}

	public void setSeq(long aggregateId, long seq) {
		changes.get().put(aggregateId, seq);
	}



	public void clearAll() {
		changes.remove();
	}

	public void clear(Object aggregate) {
		//TODO
		changes.remove();
	}

	long nextSeq(Long aggregateId) {
		return getSeq(aggregateId)+1;
	}

	long getSeq(Long aggregateId) {
		return changes.get().getOrDefault(aggregateId, -1L);
	}
}
