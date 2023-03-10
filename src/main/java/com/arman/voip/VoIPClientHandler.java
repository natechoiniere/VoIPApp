/*
    VoIP Client (audio)
 */

package com.arman.voip;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class VoIPClientHandler extends Thread {

    private final VoIPServer server;
    private final InputStream in;
    private final OutputStream out;
    private final int id;

    public VoIPClientHandler(VoIPServer server, Socket sock, int id) throws IOException {
        this.server = server;
        this.in = new BufferedInputStream(sock.getInputStream());
        this.out = new BufferedOutputStream(sock.getOutputStream());
        this.id = id;
    }

    public void run() {
        byte[] bytes = new byte[64];
        try {
            while (this.in.read(bytes) != 0) {
                System.out.println(Arrays.toString(bytes));
                this.server.broadcast(bytes, id);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void sendMessage(byte[] bytes) {
        try {
            this.out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.server.remove(this);
    }

    public VoIPServer getServer() {
        return this.server;
    }

    public int getClientID() {
        return this.id;
    }

}
