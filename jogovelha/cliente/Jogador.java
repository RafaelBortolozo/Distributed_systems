package jogovelha.cliente;

import java.io.Serializable;

public class Jogador implements Serializable {
    private int id;
    private String nome;

    public Jogador(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public int getId(){
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }
}
