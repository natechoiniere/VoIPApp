/*
    VoIP Client (audio)
 */
package com.arman.voip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class VoIPClient extends Thread {

    private Socket sock;
    private AudioInputDevice in;
    private AudioOutputDevice out;

    public VoIPClient(InetAddress host, int port) throws IOException {
        this.sock = new Socket(host, port);
        this.out = new AudioOutputDevice(new BufferedInputStream(sock.getInputStream()));
        this.in = new AudioInputDevice(new BufferedOutputStream(sock.getOutputStream()));
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter hostname to connect to:");
        String hostName = input.nextLine();
        System.out.println("Enter port to connect to:");
        int port = input.nextInt();
        input.close();
        InetAddress host = null;

        try {
            host = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            VoIPClient client = new VoIPClient(host, port);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            sock.setKeepAlive(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.out.start();
        this.in.start();
    }

}
