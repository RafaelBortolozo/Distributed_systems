import Pyro4
import os
import time

def clientLogin():
    print("\nLOGIN")
    name = str(input("Digite o seu nome de usuario: "))
    password = str(input("Digite a senha: "))
    print("Verificando credenciais...")
    authenticated = quiz.clientLogin(name, password)
    if authenticated:
        print("Autenticado.")
        return
    else: 
        print("login invalido, tente novamente.")
        clientLogin()

def answerQuiz():
    maxQuestions = int(input("\nDigite o máximo de questões que deseja responder: "))
    quiz.generateRandomQuestions(maxQuestions)
    totalQuestions = quiz.getLengthRandomQuestions()
    for index in range(totalQuestions):
        question = quiz.nextQuestion()
        os.system('clear') 
        print("\n{} - {}".format((index+1),question))
        clientAnswer = int(input("Opcao: "))
        feedback = quiz.answerIsCorrect(question, clientAnswer) 
        time.sleep(1)
        print(feedback)
        time.sleep(1)
    
    result = quiz.getResultQuiz()
    os.system('clear')
    print(result)

quiz = Pyro4.Proxy("PYRONAME:ring_election")
clientLogin()
answerQuiz()