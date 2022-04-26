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
            aux = line.strip('\n').split(":")
            name = aux[0]
            password = aux[1]
            students.append({
                'name': name,
                'password': password
            })
    return students

def verifyLogin(name, password):
    for student in STUDENTS:
        if (student['name'] == name and student['password'] == password):
            return True
    
    return False

def applyQuiz():
    clientCorrectAnswers = 0
    totalQuestions = len(RANDOM_QUESTIONS)
    client.send(str(totalQuestions).encode()) 
    
    for index, obj in enumerate(RANDOM_QUESTIONS):
        question = obj['question']
        answer = obj['answer']
        client.send(("\n" + str(index+1) + " - " + question).encode())
        clientAnswer = client.recv(1024).decode() 
        answerIsCorrect = answer == clientAnswer
        if answerIsCorrect:
            clientCorrectAnswers += 1
            client.send(("Certa resposta!").encode()) 
        else:
            client.send(("Que pena, você errou.").encode())

    resultQuiz = f"\nVocê acertou {clientCorrectAnswers}/{len(RANDOM_QUESTIONS)} questões."
    client.send(resultQuiz.encode())
        
def clientLogin():
    print(f"client {address} logging...")
    name = client.recv(1024).decode()
    password = client.recv(1024).decode()
    authenticated = verifyLogin(name, password)
    message = "authenticated:" + str(authenticated)
    client.send(message.encode())
    if authenticated:
        print(f"client {address} authenticated.")
        return
    else: 
        clientLogin()

IP = '127.0.0.1'
PORT = 7777
RANDOM_QUESTIONS = getRandomQuestions(2, "perguntas.txt")
STUDENTS = getNamesAndPasswords("alunos_e_senhas.txt")

# socket - Conexao com cliente
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((IP, PORT))
server.listen()
print(f"\nListening on {IP}:{PORT}")
client, address = server.accept()
print(f"{address} is connected.\n")

clientLogin()
applyQuiz()