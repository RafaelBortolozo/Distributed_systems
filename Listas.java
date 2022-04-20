package questao1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Listas {
    private Map<String, List<String>> tabela;
    private String lastElementAdded = null;


    //pegar toda a tabela
    public Map<String, List<String>> getTabela() {
        return tabela;
    }

    //pegar ultimo elemento adicionado
    public String getLastElementAdded(){
        if(lastElementAdded != null){
            return lastElementAdded;
        } else{
            return "Nao existe um ultimo elemento por enquanto";
        }
    }

    //inserir nova lista na tabela
    public void addList(String nomeLista) {
        List<String> list = new ArrayList<String>();
        this.tabela.put(nomeLista, list);
    }

    //inserir nova string em uma lista da tabela
    public void addInList(String nomeLista, String str){
        List l = this.tabela.get(nomeLista);
        if(l != null){
            this.lastElementAdded = str;
            l.add(str);
        }
        System.out.println("Não foi possível adicionar elemento pois a lista não existe");
    }

    //remover lista da tabela
    public void removeList(String nomeLista){
        if(this.tabela.get(nomeLista) != null){
            tabela.remove(nomeLista);
        }
    }

    //remover elemento em alguma lista da tabela
    public void removeInList(String nomeLista, String str){
        List l = this.tabela.get(nomeLista);
        if(l != null){
            l.remove(str);
        }
    }

    //retornar nome de todas as listas existentes
    public String getAllListsName(){
        StringBuilder str = new StringBuilder();
        for (String k: this.getTabela().keySet()){
            str.append(k).append("\n");
        }
        return str.toString();
    }

    public String getAllLists(){
        StringBuilder str = new StringBuilder();
        for(String k: this.getTabela().keySet()){
            str.append(k).append(":\n");
            for(String element: this.getTabela().get(k)){
                str.append(element).append("\n");
            }
        }
        return str.toString();
    }
}
