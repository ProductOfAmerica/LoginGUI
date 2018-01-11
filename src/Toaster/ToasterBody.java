package Toaster;

import Utils.UIUtils;

import javax.swing.*;
import java.awt.*;

class ToasterBody extends JPanel {
    private JPanel panelToToastOn;
    private final String message;
    private int yPos;
    private final FontMetrics metrics;
    private final Color c = new Color(181, 59, 86);
    private final int stringWidth;
    private static final int TOAST_PADDING = 15;
    private int heightOfToast;
    private volatile boolean stopDisplaying;
    private final int toastWidth;
    private int stringPosX;
    private int stringPosY;

    public ToasterBody(JPanel panelToToastOn, String message, int yPos) {
        this.panelToToastOn = panelToToastOn;
        this.message = message;
        this.yPos = yPos;

        metrics = getFontMetrics(UIUtils.FONT_GENERAL_UI);
        stringWidth = metrics.stringWidth(this.message);

        toastWidth = this.stringWidth + (TOAST_PADDING * 2);
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
                    Thread.sleep(10);
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

    public boolean getStopDisplaying() {
        return stopDisplaying;
    }

    public void setStopDisplaying(boolean hasStoppedDisplaying) {
        this.stopDisplaying = hasStoppedDisplaying;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
        setBounds((panelToToastOn.getWidth() - toastWidth) / 2, yPos, toastWidth, heightOfToast);
    }

    public int getyPos() {
        return yPos;
    }
}