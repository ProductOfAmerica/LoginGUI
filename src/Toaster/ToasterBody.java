package Toaster;

import Utils.UIUtils;

import javax.swing.*;
import java.awt.*;

class ToasterBody extends JPanel {
    private static final int TOAST_PADDING = 15;
    private final int toastWidth;
    private final String message;
    private final Color c;
    private volatile boolean stopDisplaying;
    private int heightOfToast, stringPosX, stringPosY, yPos;
    private JPanel panelToToastOn;

    public ToasterBody(JPanel panelToToastOn, String message, Color bgColor, int yPos) {
        this.panelToToastOn = panelToToastOn;
        this.message = message;
        this.yPos = yPos;
        this.c = bgColor;

        FontMetrics metrics = getFontMetrics(UIUtils.FONT_GENERAL_UI);
        int stringWidth = metrics.stringWidth(this.message);

        toastWidth = stringWidth + (TOAST_PADDING * 2);
        heightOfToast = metrics.getHeight() + TOAST_PADDING;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        setBounds((panelToToastOn.getWidth() - toastWidth) / 2, (int) -(Math.round(heightOfToast / 10.0) * 10), toastWidth, heightOfToast);

        stringPosX = (getWidth() - stringWidth) / 2;
        stringPosY = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        new Thread(() -> {
            while (getBounds().y < yPos) {
                int i1 = (yPos - getBounds().y) / 10;
                i1 = i1 <= 0 ? 1 : i1;
                setBounds((panelToToastOn.getWidth() - toastWidth) / 2, getBounds().y + i1, toastWidth, heightOfToast);
                repaint();
                try {
                    Thread.sleep(5);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = UIUtils.get2dGraphics(g);
        super.paintComponent(g2);

        //Background
        g2.setColor(c);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

        // Font
        g2.setFont(UIUtils.FONT_GENERAL_UI);
        g2.setColor(Color.white);
        g2.drawString(message, stringPosX, stringPosY);
    }

    public int getHeightOfToast() {
        return heightOfToast;
    }

    public synchronized boolean getStopDisplaying() {
        return stopDisplaying;
    }

    public synchronized void setStopDisplaying(boolean hasStoppedDisplaying) {
        this.stopDisplaying = hasStoppedDisplaying;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
//        setBounds((panelToToastOn.getWidth() - toastWidth) / 2, yPos, toastWidth, heightOfToast);

        new Thread(() -> {
            while (getBounds().y > yPos) {
                int i1 = Math.abs((yPos - getBounds().y) / 10);
                i1 = i1 <= 0 ? 1 : i1;
                setBounds((panelToToastOn.getWidth() - toastWidth) / 2, getBounds().y - i1, toastWidth, heightOfToast);
                repaint();
                try {
                    Thread.sleep(5);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    public int getyPos() {
        return yPos;
    }
}