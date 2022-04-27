package listsManager;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int port = 7777;
        Socket socket = new Socket(ip, port);
        Scanner scanner = new Scanner(System.in);

        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        String menu = null;
        String serverMessage = null;
        int opt = -1;

        while(true){
            menu = inputStream.readUTF();
            System.out.println(menu);
            opt = Integer.parseInt(scanner.nextLine());
            outputStream.writeUTF(Integer.toString(opt));
            outputStream.flush();

            if(opt > 0 && opt < 8){
                if (opt == 1 || opt == 3){
                    System.out.println(inputStream.readUTF());
                    outputStream.writeUTF(scanner.nextLine());
                    System.out.println(inputStream.readUTF());
                } else if (opt == 2 || opt == 4){
                    System.out.println(inputStream.readUTF());
                    outputStream.writeUTF(scanner.nextLine());
                    System.out.println(inputStream.readUTF());
                    outputStream.writeUTF(scanner.nextLine());
                    System.out.println(inputStream.readUTF());
                }else{
                    serverMessage = inputStream.readUTF();
                    System.out.println(serverMessage);
                    if(serverMessage.equals("Conexão encerrada!")) break;
                }
            }
        }
    }
}
