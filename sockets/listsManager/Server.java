package listsManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Server {
    public static void main(String[] args) throws IOException {
        Lists l = new Lists();

        ServerSocket serverSocket = new ServerSocket(7777);

        System.out.println("Socket na porta 7777");
        System.out.println("Aguardando conexão...");

        Socket socket = serverSocket.accept();

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        System.out.println("Cliente conectado, IP:"+ socket.getInetAddress().getHostAddress());

        int opt = -1;
        String message = null;
        String listName = null;
        String element = null;
        boolean feedback = false;

        while(true){
            outputStream.writeUTF(l.menu());
            message = inputStream.readUTF();
            opt = Integer.parseInt(message);
            switch (opt){
                case 1:
                    outputStream.writeUTF("Digite o nome da nova lista: ");
                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);
                    feedback = l.addList(listName);
                    if(feedback){
                        outputStream.writeUTF("Lista adicionada.\n");
                    }else{
                        outputStream.writeUTF("nao foi possivel adicionar lista\n");
                    }
                    break;

                case 2:
                    outputStream.writeUTF("Selecione uma das listas:\n" + l.getAllListsName());
                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);
                    outputStream.writeUTF("Digite o nome do elemento: ");
                    element = inputStream.readUTF().toLowerCase(Locale.ROOT);
                    feedback = l.addInList(listName,element);
                    if(feedback){
                        outputStream.writeUTF("Elemento adicionado.\n");
                    }else{
                        outputStream.writeUTF("Não foi possivel adicionar a lista.\n");
                    }
                    break;
                case 3:
                    outputStream.writeUTF("Qual lista remover?\n" + l.getAllListsName());
                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);
                    feedback = l.removeList(listName);
                    if(feedback){
                        outputStream.writeUTF("Lista removida.\n");
                    }else{
                        outputStream.writeUTF("Não foi possivel remover a lista.\n");
                    }
                    break;
                case 4:
                    outputStream.writeUTF("Em qual lista remover?\n" + l.getAllListsName());
                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);
                    outputStream.writeUTF("Digite o nome do elemento a ser removido:\n");
                    element = inputStream.readUTF().toLowerCase(Locale.ROOT);
                    feedback = l.removeInList(listName,element);
                    if(feedback){
                        outputStream.writeUTF("Elemento removido.\n");
                    }else{
                        outputStream.writeUTF("Não foi possível remover o elemento.\n");
                    }
                    break;
                case 5:
                    outputStream.writeUTF(l.getLastElementAdded());
                    break;
                case 6:
                    outputStream.writeUTF(l.getAllLists());
                    break;
                case 7:
                    outputStream.writeUTF("Conexão encerrada!");
                    break;
                default:
                    break;
            }

            if(opt == 7) {
                inputStream.close();
                outputStream.close();
                socket.close();
                serverSocket.close();
                break;
            }
        }
    }
}