package socket_list;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        int port = 1234;
        String host = "127.0.0.1";
        Socket socket = new Socket(host, port);
    }
}
