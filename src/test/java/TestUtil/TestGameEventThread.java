package TestUtil;

import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.GameEventThread;
import testAdapter.EventAdapter;

import java.util.List;

public class TestGameEventThread extends GameEventThread {


    public TestGameEventThread() {
    }

    @Override
    public void run() {

    }

    public List<GameEvent> getEvents() {
        return this.eventQueue.stream().toList();
    }

    public void handleNextEvent() {
        long start = System.currentTimeMillis();
        while (eventQueue.isEmpty()) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (System.currentTimeMillis() - start > 1000) {
                throw new RuntimeException("Did not receive event in time (1s)");
            }
        }
        handleEvent(eventQueue.poll());
    }

    @Override
    public void forceEvent(GameEvent event) {
        submitEvent(event);
    }

    public void clearEvents() {
        this.eventQueue.clear();
    }

    public GameEvent getNextEvent() {
        return this.eventQueue.peek();
    }

    public void handleAllEvents() {
        while (!eventQueue.isEmpty()) {
            handleEvent(eventQueue.poll());
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public GameEvent getEvent(EventAdapter.EventType eventType) {
        for (GameEvent event : eventQueue) {
            if (EventAdapter.getEventName(eventType).equals(event.getName())) {
                return event;
            }
        }
        throw new RuntimeException("Event not found");
    }
}
