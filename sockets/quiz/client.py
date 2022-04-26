import socket 

def clientLogin():
    name = str(input("Digite o seu nome de usuario: "))
    client.send(name.encode())
    password = str(input("Digite a senha: "))
    client.send(password.encode())
    authenticated = eval(client.recv(1024).decode().split(":")[1])
    if authenticated:
        print("Autenticado.")
        return
    else: 
        print("login invalido, tente novamente.")
        clientLogin()

def answerQuiz():
    totalQuestions = int(client.recv(1024).decode()) 
    print("Olha la" + totalQuestions)
    for i in range(totalQuestions):
        question = client.recv(1024).decode() 
        print(question)
        answer = int(input("opcao: "))
        client.send((str(answer)).encode()) 
        feedback = client.recv(1024).decode() 
        print(feedback)
    
    result = client.recv(1024).decode()
    print(result)


IP = '127.0.0.1'
PORT = 7777
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((IP, PORT))

clientLogin()
answerQuiz()