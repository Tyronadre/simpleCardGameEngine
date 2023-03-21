package de.henrik.engine.game;

import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameEventThread extends Thread {

    private final ReentrantReadWriteLock eventListenerLock = new ReentrantReadWriteLock();
    private final List<GameEventListener> eventListeners;
    private final ArrayBlockingQueue<GameEvent> eventQueue;
    boolean waitForEvent;


    public GameEventThread() {
        this.eventQueue = new ArrayBlockingQueue<>(10);
        this.eventListeners = new ArrayList<>();

        this.start();
    }

    public void waitForEvent(boolean b) {
        waitForEvent = b;
    }


    @Override
    public void run() {
        System.out.println("Game Event Thread Started");
        long lastTime = System.currentTimeMillis();
        while (true) {
            try {
                if (!waitForEvent) {
                    handleEvent(eventQueue.take());
                } else {
                    if (lastTime + 500 < System.currentTimeMillis()) {
                        lastTime = System.currentTimeMillis();
                        System.out.println(eventQueue);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private synchronized void handleEvent(GameEvent event) {
        eventListenerLock.writeLock().lock();
        var eventListenerCopy = new ArrayList<>(eventListeners);
        eventListenerLock.writeLock().unlock();
        try {
            for (GameEventListener gameEventListener : eventListenerCopy)
                gameEventListener.handleEvent(event);
        } finally {
            System.out.println("Handling event " + event + " done");
        }
    }


    public synchronized void submitEvent(GameEvent event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addListener(GameEventListener gameEventListener) {
        this.eventListeners.add(gameEventListener);


    }

    public void removeListener(GameEventListener gameEventListener) {
        this.eventListeners.remove(gameEventListener);
    }

    public void removeAllListener() {
        this.eventListeners.clear();
    }

    public void forceEvent(GameEvent event) {
        handleEvent(event);
    }
}
