const ip = '127.0.0.1'
const port = 7777
let extensionFile

//importacao da biblioteca manipuladora de arquivos
var fs = require('fs')

//importacao do socket (pertence ao net)
var net = require('net');

//importacao do readline (leitura de dados) para enviar ao servidor
var readLine = require('readline')
var nameFile = readLine.createInterface({
    input: process.stdin,
    output: process.stdout
});

//nova instancia socket
var client = new net.Socket();

//Quando o cliente se conectar, pede o nome do arquivo e envia ao servidor
client.connect(port, ip, ()=>{
    console.log("connected on " + ip + ":" + port)
    console.log("Digite o nome do arquivo: ");
    nameFile.addListener('line', line => {
        extensionFile = line.split('.')[1] //extrai a extensao do arquivo
        client.write(line) //envia o nome do arquivo ao servidor
        console.log("Enviado, Aguarde...")
    })
})

//Ao receber dados, adiciona o buffer em um arquivo
client.on('data', (buffer)=>{
    nameFile = `arquive.${extensionFile}`
    fs.appendFile(nameFile, buffer, () => {
        console.log("Recebendo dados...")
    })
})

// client.on('close', ()=>{
//     console.log("Desconectado.")
//     return
// })













// socket.connect(7777, "127.0.0.1");
// var fs = require('fs');
// var path = require('path');

// var packets = 0;
// var buffer = new Buffer.alloc(0);
// socket.on('data', function(chunk){
//   packets++;
//   console.log(chunk);
//   buffer = Buffer.concat([buffer, chunk]);
// });

// socket.on('close', function(){
//   console.log("total packages", packets);

//   var writeStream = fs.createWriteStream(path.join(__dirname, "out.jpg"));
//   console.log("buffer size", buffer.length);
//   while(buffer.length){
//     var head = buffer.slice(0, 4);
//     console.log("head", head.toString());
//     if(head.toString() != "FILE"){
//       console.log("ERROR!!!!");
//       process.exit(1);
//     }
//     var sizeHex = buffer.slice(4, 8);
//     var size = parseInt(sizeHex, 16);

//     console.log("size", size);

//     var content = buffer.slice(8, size + 8);
//     var delimiter = buffer.slice(size + 8, size + 9);
//     console.log("delimiter", delimiter.toString());
//     if(delimiter != "@"){
//       console.log("wrong delimiter!!!");
//       process.exit(1);
//     }

//     writeStream.write(content);
//     buffer = buffer.slice(size + 9);
//   }

//   setTimeout(function(){
//     writeStream.end();
//   }, 2000);

// });