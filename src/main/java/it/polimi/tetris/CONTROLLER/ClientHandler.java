package it.polimi.tetris.CONTROLLER;

import it.polimi.tetris.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {


    private Socket socket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Client connected: " + socket.getRemoteSocketAddress());

            // loop base che ascolta messaggi
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + socket.getRemoteSocketAddress());
        } finally {
            if (server != null) server.RemoveClient(this);
            try { socket.close(); } catch (IOException e) { }
        }
    }

    public void Send(String message) {
        out.println(message);
    }

}
