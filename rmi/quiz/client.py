import Pyro4

quiz = Pyro4.Proxy("PYRONAME:quiz")
print(quiz.helloWorld())