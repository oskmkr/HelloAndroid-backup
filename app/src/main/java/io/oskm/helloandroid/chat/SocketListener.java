/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package io.oskm.helloandroid.chat;

import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Created by sungkyu.eo on 2014-08-22.
 */
public class SocketListener implements Runnable {

    private Socket socket;
    private List<String> msgList;
    private Handler handler;

    public SocketListener(Socket socket, List<String> msgList, Handler handler) {
        this.socket = socket;
        this.msgList = msgList;
        this.handler = handler;
    }

    private void listen() {
        InputStream in = null;
        try {
            in = socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            while (true) {
                String line = bufferedReader.readLine();

                if (null == line) {
                    break;
                }

                msgList.add(line);


                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

                System.out.println(line);
            }

            bufferedReader.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        listen();
    }
}
