package de.henrik.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;


/**
 * Funktionen:
 * <ul>
 *     <li>Feste Größe</li>
 *     <li>Hintergrundbild definieren</li>
 *     <li>Definieren von Positionen an denen Kartenstapel liegen. Dabei ist Dimension der stapel fest. Die maximale Anzahl der karten kann festgelegt werden.</li>
 *     <li>Rückgabe von Karten die auf kartenstapel liegen</li>
 *     <li>bewegen von karten von einem stapel zum anderen</li>
 *     <li>es muss build() aufgerufen werden um  das GameBoard zu bauen. dann können keine stacks mehr entfernt oder hinzugefügt werden</li>
 * </ul>>
 */
public class GameBoard extends JPanel {
    private Image backgroundImage;
    private final List<CardStack> cardStacks;
    private boolean build;

    private boolean cardDragged;


    public GameBoard(Image backgroundImage, Dimension size) {
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        cardStacks = new ArrayList<>();

        this.backgroundImage = backgroundImage;
        setBackground(Color.CYAN);
    }



    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void addCardStack(CardStack cardStack) {
        cardStacks.add(cardStack);
    }

    public List<CardStack> getCardStacks() {
        return cardStacks;
    }


    public CardStack getCardStack(String name) {
        for (CardStack cardStack : cardStacks)
            if (cardStack.getName().equals(name)) return cardStack;
        return null;
    }

    public void build() {
        build = true;
        setVisible(true);
    }


    @Override
    public void updateUI() {
        System.out.println("updateUI");
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("paint");
        if (!build) {
            return;
        }
        //backgroundImage
        g.drawImage(backgroundImage, 500, 500, null);

        //cardStacks
        cardStacks.forEach(CardStack::paintStack);
        System.out.println("painted");
    }

    @Override
    public void repaint(int x, int y, int width, int height) {
        super.repaint(x,y,width,height);
        System.out.println("repaint");
    }

    @Override
    protected void paintBorder(Graphics g) {
        System.out.println("paintBorder");
    }

    @Override
    protected void paintChildren(Graphics g) {
        System.out.println("paintChildren");
    }

    @Override
    public void paintImmediately(Rectangle r) {
        System.out.println("paintImmediately1");
    }

    @Override
    public void paintImmediately(int x, int y, int w, int h) {
        super.paintImmediately(x,y,w,h);
        System.out.println("paintImmediately2");
    }

    @Override
    public void paintAll(Graphics g) {
        System.out.println("paintAll");
    }

    @Override
    public void paintComponents(Graphics g) {
        System.out.println("paintComponents");
    }

    @Override
    public void repaint(Rectangle r) {
        System.out.println("repaint2");
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        super.repaint(tm,x,y,width,height);
        System.out.println("repaint3");
    }

    @Override
    public void repaint(long tm) {
        System.out.println("repaint4");
    }

    @Override
    public void repaint() {
        System.out.println("repaint5");
    }



    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("paintComponent");
    }

    public boolean isCardDragged() {
        return cardDragged;
    }

    public void setCardDragged(boolean cardDragged) {
        this.cardDragged = cardDragged;
    }


    public boolean isBuild() {
        return build;
    }
}

