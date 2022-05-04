# Perguntas e respostas via RMI

- Python version 3.9.7

Aplicação que apresenta perguntas de multipla escolha ao cliente. Ao final é apresentado o total de questões e quantas acertou/errou.

Primeiramente, instale as dependências:
```
    pip install Pyro4
```

Em um terminal exclusivo, você deve manter rodando o seguinte comando para que o servidor funcione:
```
    pyro4-ns
```

Servidor:
```
    python server.py
```

Cliente:
```
    python client.py
```

