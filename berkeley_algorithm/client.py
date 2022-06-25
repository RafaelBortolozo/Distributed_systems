import numpy as np
from socket import socket, AF_INET, SOCK_STREAM
from utils import *



class Cliente:
  # Instancia com objeto socket e seu tempo atual
  def __init__(self):
    self.addr = ('127.0.0.1', 7777)
    self.cliente = None
    self.tempo_acumulado = 0
    self.set_tempo()



  # Conectar cliente ao servidor 
  def conectar(self):
    print(f"Conectando em {self.addr}...")
    self.cliente = socket(AF_INET, SOCK_STREAM)
    self.cliente.connect(self.addr)
    print("Conectado! Aguarde...\n")



  # Desconectar cliente
  def desconectar(self):
    self.cliente.close()



  # Altera o tempo do cliente
  def set_tempo(self):
    self.tempo = get_tempo_atual_em_segundos() + self.tempo_acumulado



  # Inicia algoritmo de sincronizacao
  def iniciar_sincronizacao(self):
    tempo_servidor = None
    tempo_adicional = None
    while(True):
      resposta_servidor = str(self.cliente.recv(1024).decode())

      # send_tempo_servidor: Recebe o horario do servidor 
      if(resposta_servidor == 'send_tempo_servidor'):
        tempo_servidor = int(self.cliente.recv(1024).decode())
      
      # get_diferenca_tempo: Envia a diferenca de tempo em relacao com o servidor
      if(resposta_servidor == 'get_diferenca_tempo'):
        tempo_diferenca = get_diferenca_tempo(self.tempo, tempo_servidor)
        self.cliente.send(str(tempo_diferenca).encode())

      # get_tempo: Envia o horario do cliente para o servidor
      elif(resposta_servidor == 'get_tempo'):
        tempo_atual = str(self.tempo)
        self.cliente.send(tempo_atual.encode())
      
      # set_tempo: Atualiza o horario do cliente com o adicional de tempo
      elif(resposta_servidor == 'set_tempo'):
        tempo_adicional = int(self.cliente.recv(1024).decode())
        self.tempo_acumulado += tempo_adicional
        tempo_anterior = self.tempo
        self.set_tempo()
        diferenca = get_diferenca_tempo(self.tempo, tempo_anterior)

        if (diferenca-1 == 0):
          print(f'\ntempo atual: {get_tempo_string(get_segundos_em_tempo(self.tempo))} (sincronizado)')
        else: 
          print(f'\ntempo atual: {get_tempo_string(get_segundos_em_tempo(self.tempo))} (dessincronizado)')

        self.cliente.send(''.encode()) # envio vazio pra nao dar erro 



def main():
  cliente = Cliente()

  try:
    cliente.conectar()
    cliente.iniciar_sincronizacao()
  finally:
    cliente.desconectar()

if __name__ == '__main__':
  main()