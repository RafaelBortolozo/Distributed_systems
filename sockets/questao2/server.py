import socket
import os

IP = '172.31.1.137'
PORT = int(input('Server Port: '))
ARQUIVES_ADDRESS = ''
while not os.path.exists(ARQUIVES_ADDRESS):
    ARQUIVES_ADDRESS = str(input('Server Arquives Path: '))

# PORT = 7777
# ARQUIVES_ADDRESS = 'arquives_server/'

def progressPercent(currentSize, totalSize):
    try:
        return int(currentSize*100/totalSize)
    except ZeroDivisionError:
        return 0

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
print(f"{address} is connected.\n")


#******************** RECEBER NOME DO ARQUIVO E ENVIAR O ARQUIVO AO CLIENTE ********************#

# Recebe o nome do arquivo que o cliente quer, ja convertido em string (originalmente retorna varios bytes)
nameFileRequested = client_socket.recv(1024).decode()

# Concatena com o endereco da pasta de arquivos
addressFileRequested = ARQUIVES_ADDRESS + nameFileRequested

# Pega tamanho do arquivo (uso em print)
fileSize = os.path.getsize(addressFileRequested)

# Envia ao cliente o tamanho do arquivo (uso em print)
client_socket.send(("fileSize:{}".format(fileSize)).encode('utf-8'))

# Abre o arquivo lendo somente os bytes (rb) e envia o tamanho do arquivo
with open(addressFileRequested, 'rb') as file:
    # Envie os bytes do arquivo em partes (atraves do for() percorrendo os bytes do arquivo)
    print("Uploading: {}...".format(nameFileRequested))
    for data in file.readlines():
        client_socket.send(data)
        
        # tentativa de imprimir progresso de uploading
        #count += len(data)
        #print("Uploading: {} ({}/{})...".format(nameFileRequested, count, fileSize), end='\r')
        
        
    print("\nUploaded!")

