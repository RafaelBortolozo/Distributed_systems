package ifc.springBoot.sb.controller;

import ifc.springBoot.sb.model.Saudacao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SaudacaoController {
    private static final String MENSAGEM = "Olá %s";
    private AtomicInteger contador = new AtomicInteger();

    @GetMapping("/saudacao")
    public Saudacao saudacao(@RequestParam(value = "nome", defaultValue = "mundo") String nome){
        return new Saudacao(this.contador.incrementAndGet(), String.format(MENSAGEM, nome));
    }
}
