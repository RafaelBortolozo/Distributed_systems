import socket
import tqdm  # Lib to show a progress bar
import os

IP = socket.gethostbyname(socket.gethostname())

PORT = 1234
ADDR = (IP, PORT)

SEPARATOR = "_"
BUFFER_SIZE = 4000000  # 4Mb



client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    client.connect(ADDR)
except:
    print("Connection failed!\nCheck if the server is running.")
    exit()


filename = input(
    str("Digite o nome do arquivo (com a extensão ex:.png ou .mp3 etc): "))


try:
    filesize = os.path.getsize(filename)
except:
    print("File not found")
    exit()


client.send(f"{filename}{SEPARATOR}{filesize}".encode())

progress_bar = tqdm.tqdm(range(
    filesize), f"Sending {filename}", unit="B", unit_scale=True)

with open(filename, "rb") as f:
    while True:
        bytes_read = f.read(BUFFER_SIZE)
        
        if not bytes_read:
            break

        client.sendall(bytes_read)

        progress_bar.update(len(bytes_read))

client.close()
