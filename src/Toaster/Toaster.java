package Toaster;

import javax.swing.*;

public class Toaster {
    private static final int STARTING_Y_POS = 15;
    private static final int SPACER_DISTANCE = 15;
    private static int CURRENT_Y_OFFSET = 0;
    private static int toasterCount = 0;
    private final JPanel panelToToastOn;

    public Toaster(JPanel panelToToastOn) {
        this.panelToToastOn = panelToToastOn;
    }

    public void toast(String message) {
        ToasterBody toasterBody;

        if (toasterCount == 0) {
            toasterBody = new ToasterBody(panelToToastOn, message, STARTING_Y_POS);
            CURRENT_Y_OFFSET = STARTING_Y_POS + toasterBody.getHeightOfToast();
        } else {
            toasterBody = new ToasterBody(panelToToastOn, message, CURRENT_Y_OFFSET + SPACER_DISTANCE);
            CURRENT_Y_OFFSET = CURRENT_Y_OFFSET + SPACER_DISTANCE + toasterBody.getHeightOfToast();
        }

        toasterCount++;

        new Thread(() -> {
            panelToToastOn.add(toasterBody);
            toasterBody.startToast(() -> {
                panelToToastOn.remove(toasterBody);
                panelToToastOn.repaint();
                toasterCount--;
            });
        }).start();
    }
}