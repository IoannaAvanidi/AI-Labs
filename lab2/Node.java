
/**
 * 
 * IOANNA AVANIDI - AM:4977
 * GIANNIS KOKKINIS - AM:5109
 * 
 */


import java.util.ArrayList;
import java.util.List;

class Node{
    
    private Node parent;
    /**
     * 
     *  Xplays=true -> X turn
     *  Xplays=false -> Y turn
     */
    private String[][] grid = new String[4][3];
    private int value;
    private boolean isLeaf = false;

    /**
     *  isXplay=true -> X's turn
     *  isXplayfalse -> O's turn
     */
    private boolean isXplay;
    
    /**
     *   minOrMax=false -> MIN LOOKING
     *   minOrMax=true -> MAX LOOKING
     */
    private boolean minOrMax;

    
    private List<Node> kids = new ArrayList<Node>();
    

    public Node(String[][] updateGrid, boolean updateIsXPlay, boolean updateMinOrMax){
        
        for (int row=0; row<4; row++){
            for (int col=0; col<3; col++){
                grid[row][col] = updateGrid[row][col];
            }
        }

        this.isXplay = updateIsXPlay;
        this.minOrMax = updateMinOrMax;
        
    }

    private boolean isGridFilled(){
        for (int v=0; v<4; v++){
            for (int u=0; u<3; u++){
                if (grid[v][u].length()==0){
                    return false;
                }
            }
        }
        return true;
    }
    
    private int UtilityFunction(){
        
        if (
            (grid[0][0].equals("X") && grid[0][1].equals("O") && grid[0][2].equals("X"))
            ||
            (grid[0][0].equals("X") && grid[1][0].equals("O") && grid[2][0].equals("X"))
            ||
            (grid[0][0].equals("X") && grid[1][1].equals("O") && grid[2][2].equals("X"))
            ||
            (grid[0][1].equals("X") && grid[1][1].equals("O") && grid[2][1].equals("X"))
            ||
            (grid[0][2].equals("X") && grid[1][2].equals("O") && grid[3][2].equals("X"))
            ||
            (grid[1][0].equals("X") && grid[1][1].equals("O") && grid[1][2].equals("X"))
            ||
            (grid[1][0].equals("X") && grid[2][0].equals("O") && grid[3][0].equals("X"))
            ||
            (grid[1][1].equals("X") && grid[2][1].equals("O") && grid[3][1].equals("X"))
            ||
            (grid[1][2].equals("X") && grid[2][1].equals("O") && grid[3][0].equals("X"))
            ||
            (grid[1][2].equals("X") && grid[2][2].equals("O") && grid[3][2].equals("X"))
            ||
            (grid[2][0].equals("X") && grid[2][1].equals("O") && grid[2][2].equals("X"))
            ||
            (grid[3][0].equals("X") && grid[3][1].equals("O") && grid[3][2].equals("X"))
        ){
            return 1;
        }else{
            if (
            (grid[0][0].equals("O") && grid[0][1].equals("X") && grid[0][2].equals("O"))
            ||
            (grid[0][0].equals("O") && grid[1][0].equals("X") && grid[2][0].equals("O"))
            ||
            (grid[0][0].equals("O") && grid[1][1].equals("X") && grid[2][2].equals("O"))
            ||
            (grid[0][1].equals("O") && grid[1][1].equals("X") && grid[2][1].equals("O"))
            ||
            (grid[0][2].equals("O") && grid[1][2].equals("X") && grid[2][2].equals("O"))
            ||
            (grid[1][0].equals("O") && grid[1][1].equals("X") && grid[1][2].equals("O"))
            ||
            (grid[1][0].equals("O") && grid[2][0].equals("X") && grid[3][0].equals("O"))
            ||
            (grid[1][1].equals("O") && grid[2][1].equals("X") && grid[3][1].equals("O"))
            ||
            (grid[1][2].equals("O") && grid[2][1].equals("X") && grid[3][0].equals("O"))
            ||
            (grid[1][2].equals("O") && grid[2][2].equals("X") && grid[3][2].equals("O"))
            ||
            (grid[2][0].equals("O") && grid[2][1].equals("X") && grid[2][2].equals("O"))
            ||
            (grid[3][0].equals("O") && grid[3][1].equals("X") && grid[3][2].equals("O"))
            ){
                return -1;
            }else{
                if (isGridFilled()){
                    return 0;
                }
            }
        }


        return -2; //not finish yet
    }

    private void generateKids(){
        //generate kids here
        for(int i=0; i<4; i++){
            for (int j=0; j<3; j++){
                if(grid[i][j].length()==0){ //empty slot found
                    String[][] tempGrid = new String[4][3];

                    //copy grid to temp grid to set on new node
                    for (int c1=0; c1<4; c1++){
                        for (int c2=0; c2<3; c2++){
                            tempGrid[c1][c2] = grid[c1][c2];
                        }
                    }
                    //set the possible move
                    if(isXplay){
                        tempGrid[i][j]="X";
                    }else{
                        tempGrid[i][j]="O";
                    }
                    Node kid = new Node(tempGrid, !isXplay, !minOrMax);
                    this.kids.add(kid);
                }
            }
        }
    }



    public int miniMax(){

        int endValue = UtilityFunction();
        if (endValue!=-2){ //we found end state here
            value=endValue;
            return endValue;
        }

        //generate next nodes
        generateKids();

        if(minOrMax){ //MAX LOOKING
            int max = 0;  
            for(Node kid: kids){
                max = Math.max(max, kid.miniMax());
            }
            value=max;
            return max;       
        
        }else{ //MIN LOOKING
            int min=kids.get(0).miniMax();
            for (int kidInd=1; kidInd<kids.size(); kidInd++){
                Math.min(min, kids.get(kidInd).miniMax());
            }
            value=min;
            return min;
            
        }
        
    }

    public void addKid(Node kid){
        kids.add(kid);
    }

    public List<Node> getKids(){
        return kids;
    }

    public void setParent(Node updateParent){
        this.parent = updateParent;
    }

    public void setValue(int updateValue){
        this.value = updateValue;
    }
    
    public int getValue(){
        return value;
    }

    public String[][] getGrid(){
        return this.grid;
    }

    public void setLeaf(boolean updateLeaf){
        this.isLeaf = updateLeaf;
    }

    public boolean isNodeLeaf(){
        return isLeaf;
    }
    

    
}
