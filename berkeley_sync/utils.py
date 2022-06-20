from datetime import datetime

def get_tempo_em_segundos(tempo):
  horas, minutos, segundos = tempo.split(':')
  segundos = (int(horas) * 3600) + (int(minutos) * 60) + int(segundos)
  return segundos

def tempo_string(tempo):
  h, m, s = tempo
  return f'{h}:{m}:{s}'

def get_tempo_atual():
  agora = datetime.now()
  return [agora.hour, agora.minute, agora.second]

def get_segundos_em_tempo(segundos):
  s = segundos
  h = int(s // 3600)
  s = s % 3600
  m = int(s // 60)
  s = int(s % 60)
  return f'{h}:{m}:{s}'