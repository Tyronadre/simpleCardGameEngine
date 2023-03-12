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
    private boolean waitForListener;

    public GameEventThread() {
        this.eventQueue = new ArrayBlockingQueue<>(10);
        this.eventListeners = new ArrayList<>();

        this.start();
    }


    @Override
    public void run() {
        System.out.println("Game Event Thread Started");
        while (true) {
            try {
//                if (!waitForListener) {
                    handleEvent(eventQueue.take());
//                    waitForListener = false;
//                }
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
//            System.out.println("Handling event " + event + " with: \n " + eventListeners);
            for (GameEventListener gameEventListener : eventListenerCopy)
                gameEventListener.handleEvent(event);
        } finally {
            System.out.println("Handling event " + event + " done");
        }
    }


    public synchronized void submitEvent(GameEvent event) {
        try {
            eventQueue.put(event);
//            System.out.println("Received Event " + event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addListener(GameEventListener gameEventListener) {
//        Thread thread = new Thread(() -> {
//            eventListenerLock.readLock().lock();
//            System.out.println(eventListenerLock + " for " + gameEventListener);
//            try {
//                this.eventListeners.add(gameEventListener);
//                System.out.println("Added Listener " + gameEventListener);
//            } finally {
//                eventListenerLock.readLock().unlock();
//            }
//        });
//        thread.start();
        this.eventListeners.add(gameEventListener);


    }

    public void removeListener(GameEventListener gameEventListener) {
//        Thread thread = new Thread(() -> {
//            eventListenerLock.readLock().lock();
//            try {
//                this.eventListeners.remove(gameEventListener);
//                System.out.println("Removed Listener " + gameEventListener);
//            } finally {
//                eventListenerLock.readLock().unlock();
//            }
//        });
//        thread.start();
        this.eventListeners.remove(gameEventListener);

    }

    public void removeAllListener() {
//        new Thread(() -> {
//            eventListenerLock.writeLock().lock();
//            try {
//                System.out.println("Removed Listener " + eventListeners);
//                this.eventListeners.clear();
//            } finally {
//                eventListenerLock.writeLock().unlock();
//            }
//        }).start();
        this.eventListeners.clear();

    }

    /**
     * If this function is called the next event in the queue will be called after all staged event listener are handled
     */
    public void waitForListenerChange() {
        this.waitForListener = true;
    }
}
