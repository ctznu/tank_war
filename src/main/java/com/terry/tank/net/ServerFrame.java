package com.terry.tank.net;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {

    public static final ServerFrame INSTANCE = new ServerFrame();

    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();

    Server server = new Server();

    public ServerFrame() {
        setSize(1200, 600);
        setLocation(300, 30);
        add(btnStart, BorderLayout.NORTH);
        Panel p = new Panel(new GridLayout(1, 2));
        p.add(taLeft);
        p.add(taRight);
        add(p);

        taLeft.setFont(new Font("verderna", Font.PLAIN, 25));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
//        btnStart.addActionListener((e)->{
//            server.serverStart();
//        });

    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.serverStart();
    }

    public void updateServerMsg(String string) {
        this.taLeft.setText(taLeft.getText() + System.getProperty("line.separator") + string);
    }

    public void updateClientMsg(String string) {
        this.taRight.setText(taRight.getText() + System.getProperty("line.separator") + string);
    }
}
