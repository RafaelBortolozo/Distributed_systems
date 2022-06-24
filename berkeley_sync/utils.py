from datetime import datetime
from random import randint

from sympy import S

def get_tempo_em_segundos(tempo):
  horas, minutos, segundos = tempo
  segundos = (int(horas) * 3600) + (int(minutos) * 60) + int(segundos)
  return segundos

def tempo_string(tempo):
  h, m, s = tempo
  return f'{h}:{m}:{s}'

def get_tempo_atual():
  atual = datetime.now()
  return (atual.hour, atual.minute, atual.second)

def get_tempo_atual_em_segundos():
  h, m, s = get_tempo_atual()
  return (h*3600 + m*60 + s)

def get_segundos_em_tempo(segundos):
  s = segundos
  h = int(s // 3600)
  s = s % 3600
  m = int(s // 60)
  s = int(s % 60)
  return (h, m, s)

def get_tempo_aleatorio():
  return randint(-600,600)