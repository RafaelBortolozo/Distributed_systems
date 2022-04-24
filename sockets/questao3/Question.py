class Question:
    def __init__(self, title, options, correctOption):
        self.title = title
        self.option = options
        self.correctOption = correctOption
    :

    def getTitle(self):
        return self.title

    def getOptions(self):
        return self.options

    def getCorrectOption(self):
        return self.correctOption