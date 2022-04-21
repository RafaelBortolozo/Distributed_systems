package jogovelha.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JogoVelhaServidor {
    public static void main(String[] args) {
        try{
            System.setProperty("java.rmi.server.hostname", "localhost");
            Registry registro = LocateRegistry.createRegistry(12345);

            JogoVelhaInterface jogo = new JogoVelha();

            registro.bind("JogoVelha", jogo);
            System.out.println("Que comecem os jogos!!");

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
