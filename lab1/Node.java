

/**
 * 
 * IOANNA AVANIDI - AM:4977
 * GIANNIS KOKKINIS - AM:5109
 * 
 */


import java.util.ArrayList;
import java.util.List;
import javax.print.DocFlavor;
import java.util.Collections;

class Node{
    
    private String name;
    private int cost;
    private int[] pos;
    private int heuristicValue; 
    private List<Node> kids = new ArrayList<Node>();
    private List<Node> predecessors = new ArrayList<Node>();
    private Node parent;
    

    public Node(String updateName , int updateCost, int[] udpatePos){
        this.name = updateName;
        this.cost = updateCost;
        this.pos = udpatePos;
    }

    public String getName(){
        return name;
    }
    
    public int[] getPos(){
        return pos;
    }

    public List<Node> getKids(){
        return kids;
    }
    
    public void setCost(int updateCost){
        this.cost = updateCost;
    }
    
    public int getCost(){
        return cost;
    }

    public void setHerValue(int udpateHerValue){
        this.heuristicValue = udpateHerValue;
    }

    public int getHerValue(){
        return heuristicValue;
    }

    public void setParent(Node updateParent){
        this.parent = updateParent;
    }
    

    public Node getParent(){
        return parent;
    }
    
    public boolean haveParent(){
        return parent!=null;
    }


    public void addKid(Node newKid){
        kids.add(newKid);
    }

    public void removeKid(Node revKid){
        int index=-1;
        for(int i=0; i<kids.size(); i++){
            if(kids.get(i).getName().equals(revKid.getName())){
                index=i;
                break;
            }
        }
        kids.remove(index);
    }

    public void addPredec(Node newPre){
        predecessors.add(newPre);
    }

    public boolean isPrec(int[] checkPos){
        
        
        for(Node precNode: predecessors){
            if((precNode.getPos()[0]==checkPos[0]) && (precNode.getPos()[1]==checkPos[1])){
                return true;
            }
        }

        return false;
    }

    public List<Node> getPrec(){
        return predecessors;
    }

    public List<String> generatePath(){
        List<String>  path = new ArrayList<String>();
        
        path.add(this.getName());
        Node putNode = this;
        while(putNode.haveParent()){
            putNode = putNode.getParent();
            path.add(putNode.getName());
        }

        // path = path.reversed();
        Collections.reverse(path);
        return path;

    }

}
