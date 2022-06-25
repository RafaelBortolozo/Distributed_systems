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
    self.tempo_acumulado = 0
    self.tempo = get_tempo_atual_em_segundos()



  # Pega o tempo atual do daemon, considerando o tempo de correcao
  def set_tempo_servidor(self):
    self.tempo = get_tempo_atual_em_segundos() + self.tempo_acumulado



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

  # Envia o tempo do servidor aos clientes
  def send_tempo_servidor(self):
    requisicao = 'send_tempo_servidor'

    for cliente in self.clientes:
      cliente_socket, addr = cliente
      cliente_socket.send(requisicao.encode())
      cliente_socket.send(str(self.tempo).encode())



  # Pega a diferenca de tempo entre servidor e clientes
  def get_diferenca_tempos(self):
    requisicao = 'get_diferenca_tempo'
    diferencas = []

    for cliente in self.clientes:
      cliente_socket, addr = cliente
      cliente_socket.send(requisicao.encode())
      resposta = int(cliente_socket.recv(1024).decode())
      diferencas.append(resposta)

    return diferencas


  # Tempo dos clientes é atualizado usando o tempo adicional
  def set_tempo_clientes(self, diferencas):
    requisicao = 'set_tempo'
    
    for cliente, tempo_adicional in zip(self.clientes, diferencas):
      cliente_socket, self.addr = cliente
      cliente_socket.send(requisicao.encode())
      cliente_socket.send(str(tempo_adicional).encode())



  def calcular_tempo_adicional(self, diferencas):
    # Coleta o tempo do daemon
    diferencas_validas = []

    # Seleciona apenas tempos próximos ao horario do daemon (tolerancia de até 600 segundos)
    # Será salvo as diferencas de tempo com relacao ao daemon
    for diferenca in diferencas:
      if(diferenca < 600 and diferenca >= -600):
        diferencas_validas.append(diferenca)
      else: 
        diferencas_validas.append(0)

    # Calcula a media
    tempo_adicional = round((np.sum(diferencas_validas)) / (len(diferencas) + 1))

    # Retorna o tempo 
    return tempo_adicional



  def get_tempos_clientes_string(self, tempos):
    aux = ""
    for index, tempo in enumerate(tempos):
      if index == len(tempos)-1:
        aux += "{}".format(get_tempo_string(get_segundos_em_tempo(tempo)))
      else:
        aux += "{} | ".format(get_tempo_string(get_segundos_em_tempo(tempo)))
    return aux



  def iniciar_sincronizacao(self):
    # inverte o sinal dos numeros
    def convert(lst):
      return [ -i for i in lst]

    # incrementa um tempo em toda a lista
    def add_tempo_adicional(lst, tempo_adicional):
      return [ i+tempo_adicional for i in lst]

    while(True):
      # imprime os horarios antes da sincronizacao
      tempos_clientes = self.get_tempos()
      tempos_clientes_string = self.get_tempos_clientes_string(tempos_clientes)
      print(f'\nServidor: {get_tempo_string(get_segundos_em_tempo(self.tempo))}') 
      print(f'clientes: {tempos_clientes_string}')
      sleep(0.95)
      
      # Enviar tempo do servidor aos clientes
      self.send_tempo_servidor()
      sleep(0.01)

      # Receba a diferença de tempo dos clientes em relacao ao servidor
      diferencas = self.get_diferenca_tempos()
      sleep(0.01)

      # Calcula a media dessas diferencas
      tempo_adicional = self.calcular_tempo_adicional(diferencas)
      sleep(0.01)

      # Atualiza o tempo do servidor com o tempo_adicional
      self.tempo_acumulado += tempo_adicional
      self.set_tempo_servidor()
      sleep(0.01)

      # Atualiza os clientes, enviando a respectiva diferenca 
      # de tempo de forma que todos fiquem sincronizados
      diferencas = convert(diferencas)
      diferencas = add_tempo_adicional(diferencas, tempo_adicional)
      self.set_tempo_clientes(diferencas)
      sleep(0.01)
      
           
    
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