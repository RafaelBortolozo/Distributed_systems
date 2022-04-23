# Transferência de arquivos via socket

- Python version 3.9.7

Para executar basta adicionar o arquivo que deseja trasnferir na raiz do projeto.

Executar os comandos abaixo:

Instalação de dependências:

```
   pip install -r requirements.txt
```

Execução do servidor:

```
    python server.py
```

Execução do cliente:

```
    python client.py
```

Depois de transferido o arquivo irá aparecer da seguinte maneira: <strong>receivedfile_nomedoarquivo.extensão</strong> na pasta <strong>received-files</strong>

Para transferir arquivos maiores que 4mb a variavel <strong>BUFFER_SIZE</strong> deverá ser alterada.
