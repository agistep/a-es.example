package io.agistep.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.ThreadLocal.withInitial;
import static java.util.Collections.synchronizedList;

class EventContextHolder {

    private final static ThreadLocal<Map<Long,Long>> changes = ThreadLocal.withInitial(HashMap::new);
    private final static ThreadLocal<List<Event>> changes2 = withInitial(() -> synchronizedList(new ArrayList<>()));

}
