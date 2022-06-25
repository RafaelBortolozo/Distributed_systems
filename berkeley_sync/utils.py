from datetime import datetime, timedelta
from random import randint

def get_tempo_em_segundos(tempo):
  horas, minutos, segundos = tempo
  segundos = (int(horas) * 3600) + (int(minutos) * 60) + int(segundos)
  return segundos

def get_tempo_string(tempo):
  h, m, s = tempo
  return f'{h}:{m}:{s}'

def get_tempo_atual():
  atual = datetime.now()
  return [atual.hour, atual.minute, atual.second]

def get_tempo_atual_em_segundos():
  h, m, s = get_tempo_atual()
  return (h*3600 + m*60 + s)

def get_segundos_em_tempo(segundos):
  tempo = str(timedelta(seconds=segundos))
  h, m, s = tempo.split(':')
  return [h, m, s]

def get_tempo_aleatorio():
  return randint(-600,600)

def get_diferenca_tempo(tempo1, tempo2):
  return tempo1-tempo2