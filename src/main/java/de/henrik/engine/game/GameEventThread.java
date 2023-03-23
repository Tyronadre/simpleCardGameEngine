package de.henrik.engine.game;

import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameEventThread extends Thread {

    private final ReentrantReadWriteLock eventListenerLock = new ReentrantReadWriteLock();
    protected final List<GameEventListener> eventListeners;
    protected final ArrayBlockingQueue<GameEvent> eventQueue;
    boolean waitForEvent;
    GameEvent lastEvent;
    boolean stop;


    public GameEventThread() {
        this.eventQueue = new ArrayBlockingQueue<>(10);
        this.eventListeners = new ArrayList<>();
        stop = false;
        this.start();
    }

    public void waitForEvent(boolean b) {
        waitForEvent = b;
    }


    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        System.out.println("Game Event Thread Started");
        long lastTime = System.currentTimeMillis();
        while (!stop) {
            try {
                if (lastTime + 50 < System.currentTimeMillis() && !waitForEvent) {
                    lastTime = System.currentTimeMillis();
                    handleEvent(lastEvent = eventQueue.take());
                    System.out.println("running");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    protected synchronized void handleEvent(GameEvent event) {
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

    public void stop(boolean b) {
        stop = true;
    }
}
