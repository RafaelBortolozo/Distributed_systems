package election_ring;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Manager implements InterfaceManager{

    // mapeia o anel e informa a porta do servidor
    private HashMap<Integer, InterfaceProcess> map = new HashMap<Integer, InterfaceProcess>();
    public static final int port = 12345;
    public static final String name = "server";
    public static boolean work = true;

    public static void main(String[] args) {
        try {
            // Inicia Servidor
            Manager manager = new Manager();
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            InterfaceManager stubManager = (InterfaceManager) UnicastRemoteObject.exportObject(manager, 0);
            Registry managerRegistry = LocateRegistry.createRegistry(port);
            managerRegistry.bind(name, stubManager);
            System.out.println("Servidor pronto!");

            // cria 4 processos com id acima de 10000 e adiciona no anel
            for (int i=1; i<=4; i++) {
                Process process = new Process(10000 + i);

                InterfaceProcess stubProcess = (InterfaceProcess) UnicastRemoteObject.exportObject(process, process.id);
                Registry registryProcess = LocateRegistry.createRegistry(process.id);
                registryProcess.bind(process.name(), stubProcess);

                manager.addProcess(process.id, stubProcess);
            }
        } catch (Exception e) {
            System.err.println("Erro no gerenciador: " + e.toString());
            System.err.println(e.getLocalizedMessage());
            System.err.println(e.getStackTrace());
            System.err.println(e.getCause());
            System.err.println(e.getMessage());
        }
    }

    // Ao adicionar, pare o trabalho de todos os processos e adicione na lista
    public void addProcess(int id, InterfaceProcess item) throws RemoteException {
        work = false;

        this.map.put(id, item);

        // mapeia todos os processos de forma ordenada para fim de iteracao
        TreeMap<Integer, InterfaceProcess> sorted = new TreeMap<>(this.map);
        Set<Map.Entry<Integer, InterfaceProcess>> mappings = sorted.entrySet();

        Map.Entry<Integer, InterfaceProcess> previous = null;
        Map.Entry<Integer, InterfaceProcess> first = null;
        Map.Entry<Integer, InterfaceProcess> currentEntry = null;

        int i = 0;
        boolean setSuccessor = false;

        for (Map.Entry<Integer, InterfaceProcess> current : mappings) {
            // Se tiver apenas um processo, então o processo nao tem sucessor
            if (mappings.size() == 1) {
                System.out.println("\t1");
                currentEntry = current;
                currentEntry.getValue().setSuccessor(0);
                break;
            }

            // Se tiver mais processos, entao é definido o sucessor de cada processo
            if (first == null) {
                first = current;
            }

            if (setSuccessor) {
                previous.getValue().setSuccessor(current.getKey());
                break;
            }

            if (id == current.getKey()) {
                currentEntry = current;

                // Se o processo for o ultimo, entao deve-se apontar para o
                // primeiro processo da lista, formando assim o anel
                if ((i+1 == mappings.size())) {
                    current.getValue().setSuccessor(first.getKey());
                    previous.getValue().setSuccessor(current.getKey());
                    break;
                } else {
                    setSuccessor = true;
                }
            }

            i++;
            previous = current;
        }

        //atualiza o anel
        currentEntry.getValue().electionMessage(new ArrayList<>(), id);
        currentEntry.getValue().fazerCoisa();
        work = true;
    }

    @Override
    public HashMap<Integer, InterfaceProcess> executingProcesses() throws RemoteException {
        return this.map;
    }

    @Override
    // Remove um processo, sendo necessario atualizar os sucessores
    public void removeProcess(int id) throws RemoteException {
        if(!this.map.containsKey(id)){
            return;
        }

        System.out.println("*************************************");
        System.out.println("Remover item id " + id);

        this.map.remove(id);

        TreeMap<Integer, InterfaceProcess> sorted = new TreeMap<>(this.map);
        Set<Map.Entry<Integer, InterfaceProcess>> mappings = sorted.entrySet();

        Map.Entry<Integer, InterfaceProcess> previous = null;
        Map.Entry<Integer, InterfaceProcess> first = null;

        for (Map.Entry<Integer, InterfaceProcess> current : mappings) {
            if (first == null) {
                first = current;
            }

            if (previous != null) {
                System.out.println("p " + previous.getKey() + " sucessor = " + current.getKey());
                previous.getValue().setSuccessor(current.getKey());
            }

            previous = current;
        }

        previous.getValue().setSuccessor(first.getKey());
        System.out.println("p " + previous.getKey() + " sucessor = " + first.getKey());
    }

    @Override
    public void initElection(int remove) throws RemoteException {
        work = false;
        this.removeProcess(remove);
    }

    @Override
    public void stopElection(int id, InterfaceProcess item) throws RemoteException {
        work = true;
    }
}







