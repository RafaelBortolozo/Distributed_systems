var fs = require('fs') //importacao da biblioteca manipuladora de arquivos
var net = require('net'); //importacao do socket (pertence ao net)
const { StringDecoder } = require('string_decoder'); //importacao do decoder utf-8
var readline = require('readline') //importacao do readline (leitura de dados) para enviar ao servidor

const ip = '127.0.0.1'
const port = 7777
let fileNameRequested
let downloadsPath = 'arquives_client/'
let path

//Funcao pra calcular porcentagem atual durante o download
function progressPercent(currentSize, totalSize){
    if(currentSize != 0 && totalSize != 0){
        return Math.ceil((currentSize*100/totalSize)) 
    } else{
        return 0
    }
}

var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const decoder  = new StringDecoder('utf8')

//nova instancia socket
var client = new net.Socket();

//Quando o cliente se conectar, pede o nome do arquivo e envia ao servidor
client.connect(port, ip, ()=>{
    console.log("connected on " + ip + ":" + port)
    console.log("Digite o nome do arquivo: ");
    rl.addListener('line', line => {
        fileNameRequested = line //pega o nome do arquivo
        client.write(line) //envia o nome do arquivo ao servidor
        path = downloadsPath + fileNameRequested
        console.log("Baixando " + fileNameRequested + "...")
    })
})

//Ao receber dados, adiciona o buffer em um arquivo
let bufferSize = 0
let fileSize = 0
let currentProgressPercent = 0
client.on('data', (buffer)=>{
    //se receber o tamanho do arquivo, salve, pois será usado no print de porcentagem no terminal
    if(decoder.write(buffer).split(":")[0] == "fileSize"){
        fileSize = parseInt(decoder.write(buffer).split(":")[1])
    } else {
        //Envia o arquivo
        fs.appendFileSync(path, buffer)
        
        //Condicional pra diminuir a repeticao de console.log()
        let aux = progressPercent(bufferSize, fileSize)
        if(currentProgressPercent != aux){
            console.log(`Downloading: ${fileNameRequested} (${aux}%)...`)
            currentProgressPercent = aux
        }
        
        bufferSize += buffer.length
    }
    
})

client.on('close', ()=>{
    console.log("Downloaded!")
})
