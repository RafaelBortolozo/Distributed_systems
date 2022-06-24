import numpy as np
from socket import socket, AF_INET, SOCK_STREAM
from utils import *
from time import sleep



class Servidor:
  def __init__(self, num_clientes):
    self.addr = ('127.0.0.1', 7777)
    self.num_clientes = num_clientes
    self.clientes = []
    self.servidor = None
    self.tempo_adicional = get_tempo_aleatorio()
    self.sync_tempo(self.tempo_adicional)



  # Pega o tempo atual do daemon
  def sync_tempo(self, tempo_adicional):
    self.tempo = get_tempo_atual_em_segundos() + tempo_adicional



  # Inicia o servidor, 
  # Aguarda a conexao de 4 clientes, 
  # Inicia a sincronizacao 
  def iniciar(self):
    self.servidor = socket(AF_INET, SOCK_STREAM)
    self.servidor.bind(self.addr)
    self.servidor.listen()

    print(f"Aguardando {self.num_clientes} conexões...")
    for i in range(self.num_clientes):
      cliente_socket, addr = self.servidor.accept()
      print(f'Cliente {addr} conectado.')
      self.clientes.append((cliente_socket, addr))
    self.iniciar_sincronizacao()



  # Desconectar clientes e desligar servidor
  def desconectar(self):
    for cliente in self.clientes:
      cliente_socket, addr = cliente
      cliente_socket.close()

    self.servidor.close()



  # Retorna um array com todos os tempos dos clientes
  def get_tempos(self):
    requisicao = 'get_tempo'
    tempos = []

    for cliente in self.clientes:
      cliente_socket, addr = cliente
      cliente_socket.send(requisicao.encode())
      resposta = cliente_socket.recv(1024).decode()
      tempos.append(resposta)

    return tempos



  # Altera
  def set_tempos(self, tempo):
    requisicao = 'set_tempo'
    
    for cliente in self.clientes:
      cliente_socket, self.addr = cliente
      cliente_socket.send(requisicao.encode())
      cliente_socket.recv(1024) # aqui onde recebo o sinal "pronto"
      cliente_socket.send(tempo.encode())



  def calcular_tempo(self, tempos):
    print(tempos)
    # Coleta o tempo do daemon
    horas, minutos, segundos = self.tempo 
    tempo_servidor = (int(horas) * 3600) + (int(minutos) * 60) + int(segundos)
    tempos_em_segundos = []
    tempos_usados = []

    # Converte todos os tempos dos clientes em segundos
    for tempo in tempos:
      segundos = get_tempo_em_segundos(tempo)
      tempos_em_segundos.append(segundos)

    # Seleciona apenas tempos próximos ao horario do daemon (tolerancia de até 600 segundos)
    for tempo in tempos_em_segundos:
      if(tempo <= tempo_servidor + 600 and tempo >= tempo_servidor - 600):
        tempos_usados.append(tempo)

    # Calcula o novo tempo do servidor
    novo_tempo = (np.sum(tempos_usados) + tempo_servidor) / (len(tempos) + 1)

    # Retorna o tempo devidamente formatado
    return get_segundos_em_tempo(novo_tempo)



  def iniciar_sincronizacao(self):
    while(True):
      print(f'servidor: {self.tempo}')
      tempos = self.get_tempos()
      print(f'clientes: {tempos}\n')
      novo_tempo = self.calcular_tempo(tempos)
      self.set_tempos(novo_tempo)
      self.sync_tempo(0)
      sleep(5)
      



def main():
  servidor = Servidor(num_clientes=2)

  try:
    servidor.iniciar()
  except KeyboardInterrupt:
    servidor.desconectar()
  finally:
    servidor.desconectar()

  servidor.desconectar()

if __name__ == '__main__':
  main()