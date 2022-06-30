package election_ring;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Process implements InterfaceProcess{
    public final int id;
    private int coordinator = 0;
    private int successor = 0;
    public final int timerTempoFazer = 5000;
    public final int timerTempoCoordenador = 10000;
    private Timer timerFazer;
    private Timer timerCoordinator;

    public String name(){
        return this.id + "";
    }

    public Process(int number){
        this.id = number;
    }

    @Override
    public void electionMessage(ArrayList<Integer> list, int process) throws RemoteException {
        // verifica se existe coordenador ativo, se sim entao nao tem eleicao.
        try {
            Registry coordinatorR = LocateRegistry.getRegistry("127.0.0.1", coordinator);
            InterfaceProcess p = (InterfaceProcess) coordinatorR.lookup(coordinator+"");
        } catch (Exception e) {
            try {
                Registry managerR = LocateRegistry.getRegistry("127.0.0.1", Manager.port);
                InterfaceManager manager = (InterfaceManager) managerR.lookup(Manager.name);
            } catch (RemoteException | NotBoundException el) {
                System.out.println("Erro ao remover um processo: " + el.getMessage());
            }
        }

        // Se nao tiver, significa que um coordenador morreu, entao remove-o e começa a eleição
        if (list.contains(this.id)) {
            System.out.println("Lista " + this.id);

            // Loop com o envio das mensagens encadeadas até que seja definido o novo coordenador
            if (this.id == process) {
                int coord = Collections.max(list);

                this.coordinator = coord;
                System.out.println("<" + this.id + "> Novo coordenador = " + coord);

                // por fim, envia a mensagem para o coordenador informando que ele ganhou a eleicao
                if (this.successor!=0) {
                    try {
                        Registry registry = LocateRegistry.getRegistry("127.0.0.1", this.successor);
                        InterfaceProcess successorProcess = (InterfaceProcess) registry.lookup(this.successor + "");
                        successorProcess.coordinatorMessage(new ArrayList<>(), coord);
                    } catch (Exception e) {
                        System.out.println("Erro na mensagem de eleicao: " + e.getMessage());
                    }
                }
                return;
            }
            return;
        }

        // Caso nao existir um processo com aquele id, então adiciona na lista e
        // executa novamente a funcao pra inserir o novo processo na eleicao
        list.add(this.id);
        System.out.println("Adicionado lista " + this.id);

        if (this.successor == 0) {
            this.electionMessage(list, process);
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", this.successor);
            InterfaceProcess successorProcess = (InterfaceProcess) registry.lookup(this.successor + "");
            successorProcess.electionMessage(list, process);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void coordinatorMessage(ArrayList<Integer> list, int coordinator) throws RemoteException {
        System.out.println("<" + this.id + "> Coordenador = " + coordinator);

        // Coordenador vai monitorar os outros processos enviando requisições para eles
        // Se nao for coordenador, então para de monitorar
        if (this.coordinator == this.id) {
            this.iniciarTimerCoordinator();
        } else {
            this.pararTimerCoordinator();
        }

        if (!list.contains(this.id)) {
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", this.successor);
                InterfaceProcess successorProcess = (InterfaceProcess) registry.lookup(this.successor + "");
                successorProcess.coordinatorMessage(list, coordinator);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    // Seta o sucessor do processo
    public void setSuccessor(int process) throws RemoteException {
        this.successor = process;
    }

    @Override
    // Se o timer estiver nulo, então inicia o timer
    public void fazerCoisa() throws RemoteException {
        if (this.timerFazer == null) {
            this.iniciarTimerFazer();
        }
    }

    @Override
    // Verifica se é coordenador
    public boolean isCoordinator() throws RemoteException {
        return this.id == this.coordinator;
    }

    @Override
    // retorna um valor aleatorio entre 1 e 10
    public int enviarCoisa() throws RemoteException {
        return new Random().nextInt(9)+1;
    }

    @Override
    // Coordenador verifica se os processos estao ativos, caso um morrer entao remove-o
    public void verificacaoAtivo() throws RemoteException {
        Registry manR = LocateRegistry.getRegistry("127.0.0.1", Manager.port);
        InterfaceManager manI = null;

        try {
            manI = (InterfaceManager) manR.lookup(Manager.name);
        } catch (RemoteException | NotBoundException el) {
            System.out.println("Erro na verificação do ativo");
            System.exit(0);
        }

        int lastProcess = 0;
        HashMap<Integer, InterfaceProcess> map = manI.executingProcesses();

        for (Map.Entry<Integer, InterfaceProcess> process : map.entrySet()) {
            lastProcess = process.getKey();

            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", process.getKey());
                InterfaceProcess coordinatorInterface = (InterfaceProcess) registry.lookup(process.getKey()+"");
                coordinatorInterface.sendVerification();
                System.out.println("Coordenador: processo " + process.getKey() + " ativo");
            } catch (Exception e) {
                System.out.println("<" + id + "> Coordenador = " + process.getKey() + " inativo");
                manI.removeProcess(lastProcess);
            }
        }
    }

    @Override
    public boolean sendVerification() throws RemoteException {
        return true;
    }

    @Override
    // Monitora o anel em busca de coordenadores mortos e disparar a eleicao a cada 1 segundo
    public void iniciarTimerFazer() throws RemoteException {
        this.timerFazer = new Timer();
        this.timerFazer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (!Manager.work) {
                    return;
                }

                try {
                    if (coordinator == 0) {
                        try {
                            electionMessage(new ArrayList<Integer>(), id);
                        } catch (RemoteException el) {
                            el.printStackTrace();
                        }

                        return;
                    }

                    if (isCoordinator()) {
                        System.out.println("<" + id + "> Coordenador = " + enviarCoisa());
                        return;
                    }

                    Registry registry = LocateRegistry.getRegistry("127.0.0.1", coordinator);
                    InterfaceProcess interfaceCoordinator = (InterfaceProcess) registry.lookup(coordinator+"");

                    int value = interfaceCoordinator.enviarCoisa();
                    System.out.println("Processo " + id + " recebeu do coordenador <" + coordinator + "> o valor: " + value);
                } catch (Exception e) {
                    System.out.println("Coordenador morreu");
                    try {
                        System.out.println("Mensagem de eleição por " + id);
                        electionMessage(new ArrayList<Integer>(), id);
                    } catch (RemoteException el) {
                        el.printStackTrace();
                    }
                }
            }
        }, 1000, timerTempoFazer);
    }

    @Override
    // desligar timer
    public void pararTimerFazer() throws RemoteException {
        if (this.timerFazer != null) {
            this.timerFazer.cancel();
        }
    }

    @Override
    // Coordenador monitora os processos ativos
    public void iniciarTimerCoordinator() throws RemoteException {
        if (this.timerCoordinator != null) {
            return;
        }

        this.timerCoordinator = new Timer();
        this.timerCoordinator.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    verificacaoAtivo();
                } catch (RemoteException el) {
                    System.out.println("Erro ao verificar o ativo = " + el);
                }
            }
        }, 1000, this.timerTempoCoordenador);
    }

    @Override
    // parar o timer do coordenador
    public void pararTimerCoordinator() throws RemoteException {
        if (this.timerCoordinator != null) {
            this.timerCoordinator.cancel();
            this.timerCoordinator = null;
        }
    }
}
