package jogovelha.server;

import java.io.Serializable;

/**
 * código 1: sucesso - entrar no jogo
 * código 2: erro - id existente
 * código 3: erro - jogo cheio
 *
 *
 * */
public class MensagemRetorno implements Serializable {
    private int cod;
    private String texto;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
