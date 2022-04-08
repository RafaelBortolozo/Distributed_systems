package socket_list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Socket on port 1324");
        System.out.println("Waiting client connection...");

        Socket socket = serverSocket.accept();

        System.out.println("Client connected,IP address:"+socket.getInetAddress().getHostAddress());

        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
        ArrayList<List> lists = new ArrayList();
        
    }

}
