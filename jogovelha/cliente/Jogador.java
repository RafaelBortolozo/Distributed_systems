package jogovelha.cliente;

import java.io.Serializable;

public class Jogador implements Serializable {
    private int id;
    private String nome;
    private Boolean permissaoJogar;

    public Jogador(int id, String nome, Boolean permissaoJogar){
        this.id = id;
        this.nome = nome;
        this.permissaoJogar = permissaoJogar;
    }

    public int getId(){
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public Boolean getPermissaoJogar() {
        return permissaoJogar;
    }

    public void setPermissaoJogar(Boolean permissaoJogar) {
        this.permissaoJogar = permissaoJogar;
    }
}
