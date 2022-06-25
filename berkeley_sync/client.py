import numpy as np
from socket import socket, AF_INET, SOCK_STREAM
from utils import *



class Cliente:
  # Instancia com objeto socket e seu tempo atual
  def __init__(self):
    self.addr = ('127.0.0.1', 7777)
    self.cliente = None
    self.set_tempo(get_tempo_atual_em_segundos(), get_tempo_aleatorio())



  # Conectar cliente ao servidor 
  def conectar(self):
    print(f"Conectando em {self.addr}...")
    self.cliente = socket(AF_INET, SOCK_STREAM)
    self.cliente.connect(self.addr)
    print("Conectado! Aguarde...\n")



  # Desconectar cliente
  def desconectar(self):
    self.cliente.close()



  # Retorna o tempo em string
  def get_tempo(self):
    return self.tempo



  # Altera o tempo do cliente
  def set_tempo(self, tempo, tempo_adicional=0):
    self.tempo = tempo + tempo_adicional



  # Inicia algoritmo de sincronizacao
  def iniciar_sincronizacao(self):
    while(True):
      # resposta do servidor, podendo ser um pedido get ou set de tempo
      respostaServidor = self.cliente.recv(1024).decode()

      # get_tempo: Envia o horario do cliente para o servidor
      if(respostaServidor == 'get_tempo'):
        tempo_atual = str(self.get_tempo())
        self.cliente.send(tempo_atual.encode())
      
      # set_tempo: atualiza o horario do cliente
      elif(respostaServidor == 'set_tempo'):
        #self.cliente.send('ready'.encode())
        novo_tempo = int(self.cliente.recv(1024).decode()) # Recebe o novo tempo do servidor
        tempo_anterior = self.tempo
        self.set_tempo(novo_tempo)
        diferenca = int(np.abs(novo_tempo - tempo_anterior))

        print(f'\ntempo atual: {get_tempo_string(get_segundos_em_tempo(novo_tempo))}')

        diferenca_tempo = get_tempo_string(get_segundos_em_tempo(diferenca))
        if (novo_tempo-1 > tempo_anterior):
          print(f'Teu relógio deve ser adiantado em {diferenca_tempo}')
        elif (novo_tempo-1 < tempo_anterior):
          print(f'Teu relógio deve ser atrasado em {diferenca_tempo}')
        else:
          print(f'Relógio sincronizado')

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