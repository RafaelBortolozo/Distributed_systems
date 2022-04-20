package questao1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        int servidorPorta = 1234;
        String servidorIP = "127.0.0.1";
        Socket socket = new Socket(servidorIP, servidorPorta);
        Scanner in = new Scanner(System.in);

        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        while(true){
            String message = inputStream.readUTF();
            System.out.println(message);

            if(message.equals("Sair")){
                break;
            }

            outputStream.writeUTF(in.nextLine());
            outputStream.flush();
        }
    }
}
