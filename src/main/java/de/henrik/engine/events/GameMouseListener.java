package de.henrik.engine.events;

import java.awt.event.MouseEvent;

public interface GameMouseListener {

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    void mousePressed(MouseEvent e);

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    void mouseReleased(MouseEvent e);

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    void mouseEntered(MouseEvent e);

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    void mouseExited(MouseEvent e);


    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    void mouseDragged(MouseEvent e);
}
