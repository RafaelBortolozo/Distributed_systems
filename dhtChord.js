let chord = []

function createNode(id, arquives, active, prevId, nextId) {
  return {
    id,
    arquives,
    active,
    prevId,
    nextId,
    children: [],
    hashTable: new Map()
  }
}

function randomNumber(max) {
  return Math.floor(Math.random() * max)
}

function hashFunction(str){
  // função hash que soma todos os caracteres convertidos
  // em inteiro e calcula o resto, o resultado é um index
  let count = 0
  for(let i in str){
    count += str.charCodeAt(i)
  }

  return (count % chord.length)
}

function getNodeById(id){
  // retorna um nodo com o id especificado
  return chord.find(node => node.id == id)
}

function getActiveNodes(){
  // retorna um array com todos os nodos ativos
  return chord.filter(node => node.active == true)
}

function getChildrenNodes(id){
  return chord.find(node => node.id == id).children
}

function getArquivesNode(id){
  return chord.find(node => node.id == id).arquives
}

function setMap(id, key, value){
  chord[getNodeById(id).id].hashTable.set(key, value)
}

function setHashTables(){
  let activeNodes = getActiveNodes()
  //para cada nó ativo...
  for(let activeNode of activeNodes){
    //reiniciar tabela hash
    chord[getNodeById(activeNode.id).id].hashTable.clear()

    let children = getChildrenNodes(activeNode.id)
    //para cada filho...
    for(let childId of children){
      let arquives = getArquivesNode(childId)
      //para cada arquivo, adiciona nome do arquivo e seu indice na hashTable do nó ativo
      for(let arquive of arquives){
        setMap(activeNode.id, arquive, hashFunction(arquive))
      }
    }
  }
  
}

function setChildren(){
  //defina os filhos de cada nó ativo

  //procura os nós ativos
  let activeNodes = getActiveNodes()

  //para cada nó ativo...
  for(let activeNode of activeNodes){
    let activeId = activeNode.id
    
    //reinicia array (então ao ativar mais nodos, é possivel então recalcular o children de cada um)
    chord[getNodeById(activeId).id].children = []
    
    // definir os filhos do nó ativo 
    // Inicialmente devemos adicionar ele mesmo
    chord[getNodeById(activeId).id].children.push(activeId)

    // Agora devemos adicionar os nodos restantes percorrendo o anel (anti-horário) até encontrar outro nó ativo
    let childPrevId = chord[getNodeById(activeId).id].prevId
    let currentChild = getNodeById(childPrevId)
    for(;;){
      if(!currentChild.active){
        chord[getNodeById(activeId).id].children.push(currentChild.id)
        currentChild = getNodeById(currentChild.prevId)
      } else {
        break
      }
    }
  }
}

function initialInsertArquives(){
  // inserção de diversos arquivos scriptados
  for(let i=0; i<chord.length; i++){
    let arquive = `arquive${i}.pdf`
    insertArquive(arquive)
  }
}

function insertArquive(str){
  // insere um novo arquivo no nodo definido pela função hash
  let hashIndex = hashFunction(str)
  let node = getNodeById(hashIndex)
  chord[node.id].arquives.push(str)
  setHashTables()
  console.log(`Inserir: arquivo "${str}" inserido no nodo ${hashIndex};`)
}

function insertActiveNode(id){
  // ativa o nodo especificado e recalcula as responsabilidades
  let node = chord.find(node => node.id == id)
  if(!node.active){
    chord[getNodeById(node.id).id].active = true
    setChildren()
    setHashTables()
    console.log(`Inserir: nodo ${id} ativado;`)
  } else {
    console.log(`Inserir: nodo ${id} já está ativado;`)
  }
}

function removeActiveNode(id){
  // desativa o nodo especificado e recalcula as responsabilidades
  let node = chord.find(node => node.id == id)
  if(node.active){
    chord[getNodeById(node.id).id].active = false
    chord[getNodeById(node.id).id].children = []
    chord[getNodeById(node.id).id].hashTable = {}
    setChildren()
    setHashTables()
    console.log(`Remover: nodo ${id} desativado;`)
  } else {
    console.log(`Remover: nodo ${id} já está desativado;`)
  }
}

function initialActivateNodes(qtd){
  //ativar alguns nodos aleatorios inicialmente
  let aux = []
  
  for(let i=0; i<qtd ; i++){
    let index = randomNumber(chord.length);
    while(aux.includes(index)){
      index = randomNumber(chord.length);
    }

    chord[getNodeById(index).id].active = true
    aux.push(index)
  }
}

function createCircleList(size){
  //cria uma lista circular com todos os nodos desativados
  for(let i=0; i<size; i++){
    if(i == 0){
      chord.push(createNode(i, [], false, size-1, i+1))
    } else if(i == size-1) {
      chord.push(createNode(i, [], false, i-1, 0))
    } else{
      chord.push(createNode(i, [], false, i-1, i+1))
    }
  }
}

function searchFile(str){
  // Procura o arquivo informado
  let activeNodes = getActiveNodes()
  let resultSearch

  for(let activeNode of activeNodes){
    resultSearch = activeNode.hashTable.get(str)
    if(resultSearch != undefined){
      console.log(`Pesquisa: arquivo "${str}" encontrado no nodo ${resultSearch};`)
      return 
    }    
  }
  console.log(`Pesquisa: arquivo "${str}" não foi encontrado;`)
  
}

function initializateChord(){
  createCircleList(20)
  initialInsertArquives()
  initialActivateNodes(10)
  setChildren()
  setHashTables()
}

initializateChord()
insertActiveNode(5)
insertActiveNode(6)
insertArquive("filme.mp4")
insertArquive("texto.txt")
removeActiveNode(5)
removeActiveNode(10)
searchFile("filme.mp4")
searchFile("EdnaldoPereira.mp4")