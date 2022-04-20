package questao1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class Server {
    static HashMap<String, ArrayList<String>> lists = new HashMap<String, ArrayList<String>>();
    static String lastVal = null;

    public static void addElement(String listName,String element){
        ArrayList<String> list = lists.get(listName);
        if(list != null){
            lists.get(listName).add(element);
            lastVal = element;
        }else{
            System.out.println("Lista não encontrada!\n");
        }
    }

    public  static void removeElement(String listName,String element){
        ArrayList<String> list = lists.get(listName);
        System.out.println(list);
        if(list != null){
            lists.get(listName).remove(element);
            lastVal = element;
        }else{
            System.out.println("Lista não encontrada!\n");
            return ;
        }
    }


    public static String getAllLists(){
        StringBuilder val = new StringBuilder();
        for(String k: lists.keySet()){
            val.append(k).append(":\n");
            for(String element: lists.get(k)){
                val.append(element).append("\n");
            }
        }
        return val.toString();
    }

    public static String getListsName(){
        StringBuilder val = new StringBuilder();
        for(String k: lists.keySet()) {
            val.append(k).append("\n");
        }
        return val.toString();
    }

    public static String menu(){
        String menu = "\n\n1) Imprimir listas\n"
                + "2) Inserir em uma lista existente\n"
                + "3) Criar nova lista\n"
                + "4) Imprimir último elemento adicionado\n"
                + "5) Remover elemento de uma lista\n"
                + "6) Encerrar conexão\n";
        return menu;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);

        System.out.println("Socket na porta 1234");
        System.out.println("Aguardando conexão...");

        Socket socket = serverSocket.accept();

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        System.out.println("Cliente conectado,IP:"+ socket.getInetAddress().getHostAddress());

        int opt = -1;
        String message = null;
        String listName = null;
        String element = null;

        while(true){
            switch (opt){
                case 1:
                    outputStream.writeUTF(getAllLists() + menu());
                    break;
                case 2:
                    outputStream.writeUTF("Listas Existentes:\n" + getListsName() + "\nDigite qual lista");

                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);

                    outputStream.writeUTF("Digite o nome do elemento");
                    element = inputStream.readUTF().toLowerCase(Locale.ROOT);

                    addElement(listName,element);
                    outputStream.writeUTF(menu());
                    break;
                case 3:
                    outputStream.writeUTF("Digite o nome da lista");
                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);

                    lists.put(listName, new ArrayList<String>());
                    outputStream.writeUTF(menu());
                    break;
                case 4:
                    outputStream.writeUTF(lastVal + menu());
                    break;
                case 5:
                    outputStream.writeUTF("Digite a lista (existente) a que o elemento pertence");
                    listName = inputStream.readUTF().toLowerCase(Locale.ROOT);

                    outputStream.writeUTF("Digite o elemento que deseja remover");
                    element = inputStream.readUTF().toLowerCase(Locale.ROOT);

                    removeElement(listName,element);
                    outputStream.writeUTF(menu());
                    break;
                case 6:
                    outputStream.writeUTF("Conexão encerrada!");
                    break;
                default:
                    outputStream.writeUTF(menu());
                    break;
            }

            if(opt == 6) {
                inputStream.close();
                outputStream.close();
                socket.close();
                serverSocket.close();
                break;
            }

            message = inputStream.readUTF();
            opt = Integer.parseInt(message);

        }
    }

}