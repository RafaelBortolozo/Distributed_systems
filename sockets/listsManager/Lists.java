package listsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lists {
    private HashMap<String, ArrayList<String>> lists = new HashMap<String, ArrayList<String>>();
    private String lastElementAdded = null;
    private String lastListElementAdded = null;

    public boolean addList(String listName){
        ArrayList<String> newList = new ArrayList<String>();
        lists.put(listName, newList);
        return true;
    }

    public boolean addInList(String listName,String name){
            ArrayList<String> list = lists.get(listName);
            if(list != null){
                lists.get(listName).add(name);
                lastListElementAdded = listName;
                lastElementAdded = name;
                return true;
            }else{
                return false;
            }
    }

    public boolean removeList(String listName){
        if(lists.get(listName) != null){
            lists.remove(listName);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeInList(String listName,String name){
        List l = lists.get(listName);
        if((l != null) && l.contains(name)) {
            lists.get(listName).remove(name);
            return true;
        } else {
            return false;
        }
    }

    public String getLastElementAdded(){
        if(lastElementAdded != null){
            return ("ultimo elemento '" +
                    lastElementAdded +
                    "' adicionado em '" +
                    lastListElementAdded +
                    "'");
        } else {
            return "O ultimo elemento adicionado nao existe\n";
        }
    }

    public String getAllLists(){
        StringBuilder str = new StringBuilder();
        for(String k: lists.keySet()){
            str.append(k).append(":\n");
            for(String element: lists.get(k)){
                str.append(element).append("\n");
            }
        }
        return str.toString();
    }

    public String getAllListsName(){
            StringBuilder str = new StringBuilder();
            for(String k: lists.keySet()) {
                str.append(k).append("\n");
            }
            return str.toString();
    }

    public String menu(){
        String menu = "Listas de string, selecione uma das opcoes abaixo digitando o indice: \n" +
                "1 - Adicionar lista;\n" +
                "2 - Adicionar elemento em uma lista;\n" +
                "3 - Remover lista;\n" +
                "4 - Remover elemento em uma lista;\n" +
                "5 - Exibir ultimo elemento adicionado;\n" +
                "6 - Exibir tudo;\n" +
                "7 - Sair.";
        return menu;
    }
}