/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.foundation.common.event;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.servicecomb.foundation.common.concurrent.ConcurrentHashMapEx;

public class EventBus {
  private final Map<Class, List<EventListener>> allEventListeners = new ConcurrentHashMapEx<>();

  public <T> void registerEventListener(Class<T> cls, EventListener<T> eventListener) {
    List<EventListener> eventListeners = allEventListeners
        .computeIfAbsent(cls, f -> new CopyOnWriteArrayList<>());
    eventListeners.add(eventListener);
  }

  public <T> void unregisterEventListener(Class<T> cls, EventListener<T> eventListener) {
    List<EventListener> eventListeners = allEventListeners
        .computeIfAbsent(cls, f -> new CopyOnWriteArrayList<>());
    if (eventListeners.contains(eventListener)) {
      eventListeners.remove(eventListener);
    }
  }

  public <T> void triggerEvent(T event) {
    List<EventListener> eventListeners = allEventListeners.getOrDefault(event.getClass(), Collections.emptyList());
    for (EventListener eventListener : eventListeners) {
      eventListener.process(event);
    }
  }
}
