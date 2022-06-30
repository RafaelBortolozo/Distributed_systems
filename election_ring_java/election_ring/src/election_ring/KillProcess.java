package election_ring;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class KillProcess {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        for(;;) {
            System.out.println("Digite um processo: ");
            String process = in.nextLine();

            if (process.equalsIgnoreCase("exit")) {
                in.close();
                break;
            }

            try {
                int p = Integer.parseInt(process);

                Registry processR = LocateRegistry.getRegistry("127.0.0.1", p);
                InterfaceProcess processI = (InterfaceProcess) processR.lookup(process);

                processI.pararTimerCoordinator();
                processI.pararTimerFazer();
                processR.unbind(process);

                System.out.println("unbind " + process + ", " + p);
            } catch (Exception e) {
                System.out.println("Erro = " + e.getMessage());
            }
        }
    }

}
