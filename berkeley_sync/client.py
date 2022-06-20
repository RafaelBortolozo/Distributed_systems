import numpy as np
from socket import socket, AF_INET, SOCK_STREAM
from utils import tempo_string, get_tempo_atual, get_tempo_em_segundos, get_segundos_em_tempo



class Cliente:
  # Instancia com objeto socket e seu tempo atual
  def __init__(self):
    self.addr = ('127.0.0.1', 7777)
    self.cliente = None
    h, m, s = get_tempo_atual()
    self.set_tempo(h, m, s)



  # Conectar cliente ao servidor 
  def conectar(self):
    print(f"Conectando em {self.addr}...")
    self.cliente = socket(AF_INET, SOCK_STREAM)
    self.cliente.connect(self.addr)
    print("Conectado! Aguarde...")



  # Desconectar cliente
  def desconectar(self):
    self.cliente.close()



  # Retorna o tempo em string
  def get_tempo(self):
    horas, minutos, segundos = self.tempo
    return f'{horas}:{minutos}:{segundos}'



  # Altera o tempo do cliente
  def set_tempo(self, horas, minutos, segundos):
    self.tempo = (horas, minutos, segundos)



  # Inicia algoritmo de sincronizacao
  def iniciar_sincronizacao(self):
    while(True):
      # resposta do servidor, podendo ser um pedido get ou set de tempo
      respostaServidor = self.cliente.recv(1024).decode()

      # get_tempo: Envia o horario do cliente para o servidor
      if(respostaServidor == 'get_tempo'):
        self.cliente.send(self.get_tempo().encode())
      
      # set_tempo: atualiza o horario do cliente
      elif(respostaServidor == 'set_tempo'):
        self.cliente.send('ready'.encode())

        novo_tempo = self.cliente.recv(1024).decode() # Recebe o novo tempo do servidor
        horas, minutos, segundos = novo_tempo.split(':')
        tempo_anterior = get_tempo_em_segundos(self.get_tempo())
        tempo_atual = get_tempo_em_segundos(novo_tempo)
        self.tempo = self.set_tempo(horas, minutos, segundos)
        diferenca = np.abs(tempo_atual - tempo_anterior)

        print(f'tempo atual: {tempo_string((horas, minutos, segundos))}')

        if (tempo_atual > tempo_anterior):
          print(f'Adiante o seu relógio em {get_segundos_em_tempo(diferenca)}')
        elif (tempo_atual < tempo_anterior):
          print(f'Atrase o seu relógio em {get_segundos_em_tempo(diferenca)}')
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