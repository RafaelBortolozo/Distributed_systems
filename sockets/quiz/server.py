import socket
import os
import io
import sys
import random

def getRandomQuestions(maxQuestions, arquive):
    questions = []
    question = ""
    answer = 0
    count = 0
    with io.open(arquive, "r", encoding="utf8") as arq:
        for line in arq.readlines():
            #salva o titulo e as opcoes
            if count >= 0 and count < 5:
                question += str(line)
                count += 1

            #salva a opcao correta e reinicia contador
            else:
                answer = int(line)
                questions.append({
                    "question": question,
                    "answer": answer
                })
                question = ""
                count = 0
    
    random.shuffle(questions)
    return (questions[:maxQuestions])

def getNamesAndPasswords(arquive):
    students = []
    name = ''
    password = ''
    with io.open(arquive, "r", encoding="utf8") as arq:
        for line in arq.readlines():
            aux = line.split(": ")
            name = str(aux[0])
            password = str(aux[1])
            students.append({
                'name': question,
                'password': password  
            })

def verifyLogin(name, password){
    for student in STUDENTS:
        if student['name'] == name and student['password'] == password:
            return true
    
    return false
}

IP = '127.0.0.1'
PORT = 7777
RANDOM_QUESTIONS = getRandomQuestions(2, "perguntas.txt")
STUDENTS = getNamesAndPasswords("alunos_e_senhas.txt")

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((IP, PORT))
server.listen()
print(f"\nListening on {IP}:{PORT}")
client_socket, address = server.accept()
print(f"{address} is connected.\n")


# #******************** RECEBER NOME DO ARQUIVO E ENVIAR O ARQUIVO AO CLIENTE ********************#

# Recebe do cliente do nome de usuario e senha e realiza login
authenticated = false
while not authenticated:
    print("client logging...\n")
    name = client_socket.recv(1024).decode()
    password = client_socket.recv(1024).decode()
    authenticated = verifyLogin(name, password)

print("client logged.")


# # Pega tamanho do arquivo (uso em print)
# fileSize = os.path.getsize(addressFileRequested)

# # Envia ao cliente o tamanho do arquivo (uso em print)
# client_socket.send(("fileSize:{}".format(fileSize)).encode('utf-8'))

# # Abre o arquivo lendo somente os bytes (rb) e envia o tamanho do arquivo
# count = 0
# with open(addressFileRequested, 'rb') as file:
#     # Envie os bytes do arquivo em partes (atraves do for() percorrendo os bytes do arquivo)
#     print("Uploading: {}...".format(nameFileRequested))
#     for data in file.readlines():
#         client_socket.send(data)

#         ## Opcao de imprimir porcentagem, mas o servidor ##
#         ## fica lento e certamente vai corromper o arquivo ##

#         #count += len(data)
#         #print("Uploading: {} ({}%)...".format(nameFileRequested, progressPercent(count, fileSize)), end='\r')
        
#     print("\nUploaded!")

