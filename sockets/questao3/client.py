import socket 

IP = '127.0.0.1'
PORT = 7777

def login():
    name = str(input("Digite o seu nome de usuario: "))
    client.send(name.encode())
    password = str(input("Digite a senha: "))
    client.send(password.encode())
    authenticated = eval(client.recv(1024).decode().split(":")[1])
    if authenticated:
        return
    else: 
        print("login invalido, tente novamente.")
        login()

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((IP, PORT))

login()

print("autenticado")