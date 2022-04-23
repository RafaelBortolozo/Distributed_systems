import socket
import os

# PORT = int(input('Server Port: '))
# ARQUIVES_ADDRESS = str(input('\nServer Arquives Address: '))

IP = '127.0.0.1'
PORT = 7777
ARQUIVES_ADDRESS = 'arquives_server/'

BUFFER_SIZE = 4000000

# Cria objeto socket especificando o protocolo IPV4 e o tipo de comunicacao (TCP neste caso)
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Liga o objeto socket ao endereco do servidor
server.bind((IP, PORT))

# Com o endereco e porta definidos, agora o servidor pode ouvir outras conexoes socket 
server.listen()
print(f"\nListening on {IP}:{PORT}")

# Todas as conexoes serao aceitas com o seguinte comando
# cliente_socket: Objeto socket do cliente
# address: Endereco IP do cliente
client_socket, address = server.accept()
print(f"{address} is connected.")


#******************** RECEBER NOME DO ARQUIVO E ENVIAR O ARQUIVO AO CLIENTE ********************#

# Recebe o nome do arquivo que o cliente quer, ja convertido em string (originalmente retorna varios bytes)
nameFileRequested = client_socket.recv(1024).decode()

# Concatena com o endereco da pasta de arquivos
addressFileRequested = ARQUIVES_ADDRESS + nameFileRequested

# Abre o arquivo lendo somente os bytes (rb)
with open(addressFileRequested, 'rb') as file:
    for data in file.readlines():
        # Envie os bytes do arquivo em partes (atraves do for() percorrendo os bytes do arquivo)
        client_socket.send(data)

    print("arquivo enviado.")

