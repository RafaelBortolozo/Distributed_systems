package socket_list;

import java.util.ArrayList;

public class List {
    ArrayList<String> list = new ArrayList();

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public void printList(){
        for(int i=0; i<list.size() ;i++){
            System.out.printf("%i: %s", i, list.get(i));
        }
    }
}
