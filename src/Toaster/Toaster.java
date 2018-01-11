package Toaster;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Toaster {
    private static final int STARTING_Y_POS = 15;
    private static final int SPACER_DISTANCE = 15;
    private static final ArrayList<ToasterBody> toasterBodies = new ArrayList<>();
    private static int CURRENT_Y_OFFSET = 0;
    private final JPanel panelToToastOn;

    public Toaster(JPanel panelToToastOn) {
        this.panelToToastOn = panelToToastOn;
    }

    public void toast(String message) {
        ToasterBody toasterBody;

        if (toasterBodies.isEmpty()) {
            toasterBody = new ToasterBody(panelToToastOn, message, STARTING_Y_POS);
            CURRENT_Y_OFFSET = STARTING_Y_POS + toasterBody.getHeightOfToast();
        } else {
            toasterBody = new ToasterBody(panelToToastOn, message, CURRENT_Y_OFFSET + SPACER_DISTANCE);
            CURRENT_Y_OFFSET = CURRENT_Y_OFFSET + SPACER_DISTANCE + toasterBody.getHeightOfToast();
        }

        toasterBodies.add(toasterBody);

        new Thread(() -> {
            toasterBody.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    removeToast(toasterBody);
                }
            });

            panelToToastOn.add(toasterBody, 0);
            panelToToastOn.repaint();

            try {
                Thread.sleep(6000);
                removeToast(toasterBody);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private synchronized void removeToast(ToasterBody toasterBody) {
        if (!toasterBody.getStopDisplaying()) {
            toasterBody.setStopDisplaying(true);

            toasterBodies.forEach(toasterBody1 -> {
                if (toasterBodies.indexOf(toasterBody1) >= toasterBodies.indexOf(toasterBody)) {
                    toasterBody1.setyPos(toasterBody1.getyPos()
                            - toasterBody.getHeightOfToast() - SPACER_DISTANCE);
                    toasterBody1.repaint();
                }
            });

            toasterBodies.remove(toasterBody);

            CURRENT_Y_OFFSET = CURRENT_Y_OFFSET - SPACER_DISTANCE - toasterBody.getHeightOfToast();

            panelToToastOn.remove(toasterBody);
            panelToToastOn.repaint();
        }
    }
}