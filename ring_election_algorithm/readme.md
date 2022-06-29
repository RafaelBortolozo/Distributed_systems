# Eleição em anel via RMI

- Python version 3.9.7

Projeto que aborda a lógica de eleição em anel com 4 clientes (processos).

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

