package Toaster;

import Utils.UIUtils;

import javax.swing.*;
import java.awt.*;

class ToasterBody extends JPanel {
    private final JPanel panelToToastOn;
    private final String message;
    private final int yPos;
    private final int toastWidth;

    private final FontMetrics metrics;
    private final Color c = new Color(181, 59, 86);
    private final int stringWidth;
    private static final int TOAST_PADDING = 15;
    private int heightOfToast;

    public ToasterBody(JPanel panelToToastOn, String message, int yPos) {
        this.panelToToastOn = panelToToastOn;
        this.message = message;
        this.yPos = yPos;

        metrics = getFontMetrics(UIUtils.FONT_GENERAL_UI);
        stringWidth = metrics.stringWidth(this.message);

        this.toastWidth = this.stringWidth + (TOAST_PADDING * 2);
        this.heightOfToast = metrics.getHeight() + TOAST_PADDING;
    }

    public int getHeightOfToast() {
        return heightOfToast;
    }

    public void startToast(Runnable after) {
        setOpaque(false);
        setBounds((panelToToastOn.getWidth() - toastWidth) / 2, yPos, toastWidth, heightOfToast);
        try {
            Thread.sleep(2000);
            after.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = UIUtils.get2dGraphics(g);
        super.paintComponent(g2);

        int stringPosX = (getWidth() - stringWidth) / 2;
        int stringPosY = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        //Background
        g2.setColor(c);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

        // Font
        g2.setFont(UIUtils.FONT_GENERAL_UI);
        g2.setColor(Color.white);
        g2.drawString(message, stringPosX, stringPosY);
    }
}
