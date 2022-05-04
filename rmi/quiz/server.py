import Pyro4
import io
import random

from sqlalchemy import null, true

@Pyro4.expose
class Quiz(object):
    def __init__(self):
        self.randomQuestions = 0
        self.clientCorrectAnswers = 0
        self.count = 0

    def getAllQuestions(self, arquive):
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
                        "question": question.upper(),
                        "answer": answer
                    })
                    question = ""
                    count = 0
        return questions

    def generateRandomQuestions(self, maxQuestions):
        allQuestions = self.getAllQuestions("questions.txt")
        random.shuffle(allQuestions)
        self.randomQuestions = allQuestions[:maxQuestions]

    def getLengthRandomQuestions(self):
        return len(self.randomQuestions)

    def nextQuestion(self):
        nextQuestion = self.randomQuestions[self.count]
        self.count += 1
        return nextQuestion["question"]


    def getNamesAndPasswords(self, arquive):
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

    def getResultQuiz(self):
        print("client finished quiz.")
        return f"Questionário finalizado :)\nVocê acertou {self.clientCorrectAnswers}/{len(self.randomQuestions)} questões."

    def clientLogin(self, name, password):
        def verifyLogin(name, password):
            for student in self.getNamesAndPasswords("authorizedUsers.txt"):
                if (student['name'] == name and student['password'] == password):
                    print("client logged")
                    return True
            
            return False

        print("client logging...")
        authenticated = verifyLogin(name, password)
        return authenticated

    def answerIsCorrect(self, question, clientAnswer):
        for obj in self.randomQuestions:
            if (obj["question"]) == question and obj["answer"] == clientAnswer:
                self.clientCorrectAnswers += 1
                return "Certa resposta!"

        return "Que pena, você errou."

    
daemon = Pyro4.Daemon()    # make a Pyro daemon
ns = Pyro4.locateNS()      # find the name server
uri = daemon.register(Quiz)# register the greeting maker as a Pyro object
ns.register("quiz", uri)   # register the object with a name in the name server

print("client conected.")
daemon.requestLoop()       # start the event loop of the server to wait for calls