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
    self.tempo = get_tempo_atual_em_segundos()



  # Pega o tempo atual do daemon
  def set_tempo_servidor(self, tempo_adicional=0):
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
      resposta = int(cliente_socket.recv(1024).decode())
      tempos.append(resposta)

    return tempos



  # Envia o novo tempo do servidor pra todos os clientes
  def set_tempo_clientes(self, tempo):
    requisicao = 'set_tempo'
    
    for cliente in self.clientes:
      cliente_socket, self.addr = cliente
      cliente_socket.send(requisicao.encode())
      cliente_socket.send(str(tempo).encode())



  def calcular_tempo(self, tempos_clientes):
    # Coleta o tempo do daemon
    tempo_servidor = self.tempo 
    tempos_clientes_validos = []

    # Seleciona apenas tempos próximos ao horario do daemon (tolerancia de até 600 segundos)
    # Será salvo as diferencas de tempo com relacao ao daemon
    for tempo_cliente in tempos_clientes:
      if(tempo_cliente <= tempo_servidor + 600 and tempo_cliente >= tempo_servidor - 600):
        diferenca = get_diferenca_tempo(tempo_servidor, tempo_cliente)
        tempos_clientes_validos.append(diferenca)



    # Calcula o adicional de tempo para o servidor
    novo_tempo = round((np.sum(tempos_clientes_validos)) / (len(tempos_clientes) + 1))

    # Retorna o tempo 
    return novo_tempo



  def get_tempos_clientes_string(self, tempos):
    aux = ""
    for index, tempo in enumerate(tempos):
      if index == len(tempos)-1:
        aux += "{}".format(get_tempo_string(get_segundos_em_tempo(tempo)))
      else:
        aux += "{} | ".format(get_tempo_string(get_segundos_em_tempo(tempo)))
    return aux



  def iniciar_sincronizacao(self):
    while(True):
      print(f'Servidor: {get_tempo_string(get_segundos_em_tempo(self.tempo))}') 
      tempos_clientes = self.get_tempos()
      tempos_clientes_string = self.get_tempos_clientes_string(tempos_clientes)
      print(f'clientes: {tempos_clientes_string}\n')
      tempo_adicional = self.calcular_tempo(tempos_clientes)
      self.set_tempo_servidor(tempo_adicional)
      self.set_tempo_clientes(self.tempo)
      sleep(1)
      
      


def main():
  servidor = Servidor(num_clientes=4)

  try:
    servidor.iniciar()
  except KeyboardInterrupt:
    servidor.desconectar()
  finally:
    servidor.desconectar()

  servidor.desconectar()

if __name__ == '__main__':
  main()