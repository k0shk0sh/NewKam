package com.fastaccess.provider.events;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kosh on 05 Sep 2016, 10:04 PM
 */

public class EventProvider {

    private final EventBus eventBus = EventBus.getDefault();
    private final Map<String, Object> events = new HashMap<>();

    public synchronized void register(@NonNull String key, @NonNull Object object) {
        if (events.containsKey(key)) {
            unregister(key);
        }
        events.put(key, object);
        eventBus.register(object);
    }

    public synchronized void unregister(@NonNull String key) {
        if (isRegistered(key)) {
            if (eventBus.isRegistered(events.get(key))) {
                eventBus.unregister(events.get(key));
            }
        }
        events.remove(key);
    }

    public synchronized boolean isRegistered(@NonNull String key) {
        return events.containsKey(key);
    }

    public synchronized void unregisterAll() {
        for (String o : events.keySet()) {
            unregister(o);
        }
        events.clear();
    }

    public void post(Object event) {
        eventBus.post(event);
    }
}
