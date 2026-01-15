

/**
 * 
 * IOANNA AVANIDI - AM:4977
 * GIANNIS KOKKINIS - AM:5109
 * 
 */


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class robotLab{

    //n and p

    private int n; //size of the array 
    private double p; //propability to display an obstacle
    
    private int[] S = new int[2]; //start point 
    private int[] G = new int[2]; //Goal Point

    /**Create labyrinth with spots 
     *  
     *  Free -> 0
     *  Reserved -> 1 
     *  
     */
    private int[][] labyrinth;
    

    private int[] spotA = new int[2]; //position A for transition A->B or B->A
    private int[] spotB = new int[2];//position B for transition A->B or B->A

    //for Heuristic values
    private int[][] heurValues;
    

    //For UCS and A* algorithms 
    private List<Node> openList = new ArrayList<Node>();
    private List<Node> closeList = new ArrayList<Node>();

    public robotLab(int updateN,int updateP, int spotS_X, int spotS_Y, int spotG_X, int spotG_Y){
        this.n = updateN;
        this.p = updateP/100.0;
        this.labyrinth = new int[updateN][updateN];
        this.heurValues = new int[updateN][updateN];
        
        //set spot A and spot B
        spotA[0] = updateN-1;
        spotA[1] = 0;
        
        spotB[0] = 0;
        spotB[1] = updateN-1;

        //set the Start and Goal point
        S[0] = spotS_X;
        S[1] = spotS_Y;

        G[0] = spotG_X;
        G[1] = spotG_Y;
        

        


    }
        
    private void printLabyrinth(){

        System.out.println("{");
        for(int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                if (i==S[0] && j==S[1]){
                    System.out.print("S | ");
                    continue;
                }else{
                    if (i==G[0] && j==G[1]){
                        System.out.print("G | ");
                        continue;
                    }else{
                        if (i==spotA[0] && j==spotA[1]){
                        System.out.print("A | ");
                        continue;
                        }else{
                            if (i==spotB[0] && j==spotB[1]){
                            System.out.print("B | ");
                            continue;
                            }
                        }
                    }
                }
                System.out.print(labyrinth[i][j]+" | ");
            }
            System.out.print("\n");
        }
        System.out.println("}");

    }


    /***
     * 
     *  THIS METHOD HERE NEED TO FIX TO GENERATE ALWAYS A PATH 
     * 
     */
    private void gerateRandomObstacles(){
        Random rand = new Random();

        for(int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                if ( (i==S[0]) && (j==S[1]) || (i==G[0]) && (j==G[1]) || (i==spotA[0]) && (j==spotA[1]) || (i==spotB[0]) && (j==spotB[1])  ){
                    continue;
                }
                double posibility = rand.nextInt(101)/100.0;
                if (posibility<=p){
                    labyrinth[i][j] = 1;
                }else{
                    labyrinth[i][j] = 0;
                }
                
            }
        }
    }

    private void GenerateHeurTable(){
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                if (i==G[1] && j==G[0]) {
                  heurValues[i][j]=0;  
                }
                
                //chebyshev distance to goal 
                int chebyshevDistanceGoal = Math.max(Math.abs(G[0] - i), Math.abs(G[1] - j));
                
                //checbyshev distance to A->B->Goal
                int toA = Math.max(Math.abs(spotA[0] - i), Math.abs(spotA[1] - j));
                int fromB = Math.max(Math.abs(G[0] - spotB[0]), Math.abs(G[1] - spotB[1]));
                int AtoBtoG =  toA + 2 + fromB;
                
                //chebyshev distance B->A->Goal
                int toB = Math.max(Math.abs(spotB[0] - i), Math.abs(spotB[1] - j));
                int fromA = Math.max(Math.abs(G[0] - spotA[0]), Math.abs(G[1] - spotA[1]));
                int BtoAtoG =  toB + 2 + fromA;
                
                heurValues[i][j] = Math.min(chebyshevDistanceGoal, Math.min(AtoBtoG, BtoAtoG));
                

            }
        }
    }

    //debugging
    private void printChebyshevTable(){
        System.out.println("{");
        for(int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                System.out.print(heurValues[i][j]+" | ");
            }
            System.out.print("\n");
        }
        System.out.println("}");
    }

    private void removeVfromOpenList(Node v){
        
        //find the index of the node 
        int index=-1;
        for(Node checkNode:openList){
            index++;
            if (checkNode.getName().equals(v.getName())){
                break;
            }
        }
        
        openList.remove(index);//and remove from openList
    }

    private int[] moveRight(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0];
        checkPos[1] = v.getPos()[1]+1;
        
        if ((checkPos[1]>n) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;

    }

    private int[] moveLeft(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0];
        checkPos[1] = v.getPos()[1]-1;
        
        
        if ((checkPos[1]<0) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;
    }

    private int[] moveDown(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0]+1;
        checkPos[1] = v.getPos()[1];
        
        if ((checkPos[0]>n) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;
    }

    private int[] moveUp(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0]-1;
        checkPos[1] = v.getPos()[1];
        
        if ((checkPos[0]<0) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;
    }    

    private int[] moveUpRight(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0]-1;
        checkPos[1] = v.getPos()[1]+1;
        
        if ((checkPos[0]<0) || (checkPos[1]>n) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;
    } 


    private int[] moveUpLeft(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0]-1;
        checkPos[1] = v.getPos()[1]-1;
        
        if ((checkPos[0]<0) || (checkPos[1]<0) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;
    } 


    private int[] moveDownRight(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0]+1;
        checkPos[1] = v.getPos()[1]+1;
        
        if ((checkPos[0]>n) || (checkPos[1]>n) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }

        return checkPos;
    } 


    private int[] moveDownLeft(Node v){
        int[] checkPos = new int[2];
        checkPos[0] = v.getPos()[0]+1;
        checkPos[1] = v.getPos()[1]-1;
        
        if ((checkPos[0]>n) || (checkPos[1]<0) ||  v.isPrec(checkPos) || (labyrinth[checkPos[0]][checkPos[1]]==1)){
            checkPos[0] = -1;
            checkPos[1] = -1;
            return checkPos;
        }
        return checkPos;
    }
    
    private int[] checkIfNodeOnSpotAOrSpotB(Node v){
        
        int[] checkPos = new int[2];

        if((v.getPos()[0] == spotA[0]) && (v.getPos()[1] == spotA[1])){
            checkPos[0] = spotB[0];
            checkPos[1] = spotB[1];
            return checkPos;
        }else{
            if ((v.getPos()[0] == spotB[0]) && (v.getPos()[1] == spotB[1])){
                checkPos[0] = spotA[0];
                checkPos[1] = spotA[1];
                return checkPos;
            }else{
                checkPos[0] = -1;
                checkPos[1] = -1;
                return checkPos;
            }
        }
    }
    

    /**
     * 
     * 
     *  ASTAR:
     *  0 -> UCS
     *  1 -> A*
     * 
     */

    private Node generateKids(Node v, int ASTAR){
        int[] putPos = new int[2];
        /*
         * STEP RIGHT
         * 
         */
        putPos = moveRight(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
        }

        /**
         * 
         * STEP LEFT
         * 
         */

        putPos = moveLeft(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
        }


        /**
         * 
         * STEP DOWN
         * 
         */
        
        putPos = moveDown(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
        }       
        
        /**
         * 
         * STEP UP
         * 
         */
        putPos = moveUp(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
            
        }    

        /**
         * 
         * MOVE UP-RIGHT BOX
         * 
         */
        
        putPos = moveUpRight(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
           
        }         
        
        /**
         * 
         *  MOVE UP-LEFT BOX
         * 
         */
        putPos = moveUpLeft(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
            
        }      

        /**
         * 
         * MOVE DOWN-RIGHT BOX
         * 
         */
        putPos = moveDownRight(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
            
        }           

        /**
         * 
         * 
         *  MOVE DOWN-LEFT BOX
         * 
         */
        putPos = moveDownLeft(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+1;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
            
        }   





        /**
         * 
         * if on spotA then kid is pos on spotB
         * 
         */
        putPos = checkIfNodeOnSpotAOrSpotB(v);
        if ((putPos[0] != -1) && (putPos[1] != -1)){
            //generate kid here
            String nodeName = "("+putPos[0]+","+putPos[1]+")";
            int newCost = v.getCost()+2;
            Node kid = new Node(nodeName,newCost,putPos);
            if(ASTAR==1){
                int herValue = heurValues[putPos[0]][putPos[1]];
                kid.setHerValue(herValue);   
            }
            kid.setParent(v);
            /**set the predecessors */
            for(Node precNode: v.getPrec()){
                kid.addPredec(precNode);
            }
            kid.addPredec(v);
            v.addKid(kid);
            
        }   

        
        return v;

    }


    private boolean isGoal(Node v){
        return (v.getPos()[0] == G[0]) && (v.getPos()[1] == G[1]);
    }

    
    private Node isInOpenOrCloseList(Node v){
        
        

        for(Node openNode: openList){
            if (openNode.getName().equals(v.getName())){
                return openNode;
            }
        }


        for(Node closeNode: closeList){
            if (closeNode.getName().equals(v.getName())){
                return closeNode;
            }
        }

        Node nodeNotFound = new Node("-1", -1, null);
        return nodeNotFound;
    }
    

    private void removeFromOpenOrCloseList(Node u){

        
        for (int i=0; i<openList.size(); i++){
            if(openList.get(i).getName().equals(u.getName())){
                openList.remove(i);
                return;
            }
        }


        for (int i=0; i<closeList.size(); i++){
            if(closeList.get(i).getName().equals(u.getName())){
                closeList.remove(i);
                return;
                
            }
        }
    }
    
    /*
     * 
     *  0->UCS
     *  1->A*
     */
    public int pickTheNextNode(int UCS_OR_ASTAR){
        int index=0;
        int min;
        if (UCS_OR_ASTAR == 0){

            /* f(N) = g(n) */
            
            min = openList.get(0).getCost();
            for (int i=0; i<openList.size(); i++){
                if (openList.get(i).getCost()<min){
                    min = openList.get(i).getCost();
                    index = i;
                    
                }
            }
        }else{

            /* f(n) = g(n) + h(n) */

            min = openList.get(0).getCost()+openList.get(0).getHerValue();
            
            
            for (int i=0; i<openList.size(); i++){
                if ((openList.get(i).getCost()+openList.get(i).getHerValue()) <min){
                    min = (openList.get(i).getCost()+openList.get(i).getHerValue());
                    index = i;
                    
                }
            }            
        }
        return index;
    }

    /***
     *  
     *  UCS Algorithm 
     *  A* Algorithm 
     * 
    */
    private List<String> UCS(){
        //this list is about path
        List<String> pathToGoal = new ArrayList<String>();

        String nodeName = "("+S[0]+","+S[1]+")";
        Node root = new Node(nodeName, 0, S);
        openList.add(root);

        Node v = root;
        while(!(openList.isEmpty())){
            removeVfromOpenList(v);
            //open kids here 
            v = generateKids(v,0);
            closeList.add(v); //add it to close list

            if(isGoal(v)){
                /**
                 * 
                 * make the return path
                 * 
                 */
                System.out.println("FOUND GOAL!");
                System.out.println("TOTAL COST: "+ v.getCost());
                pathToGoal=v.generatePath();

                //clear for next algorithm 
                openList = new ArrayList<Node>();
                closeList = new ArrayList<Node>();
                return pathToGoal;
            }
            
            List<Node> kids = v.getKids();
            
            for(int i=0; i<kids.size(); i++){
                Node checkNodeFromLists = isInOpenOrCloseList(kids.get(i));
                if ((checkNodeFromLists.getCost()!=-1)){
                    /* node is in on open or close list  */
                    if (kids.get(i).getCost()>=checkNodeFromLists.getCost()){
                        v.removeKid(kids.get(i));
                        i--;
                    }else{
                        //remove from kids 
                        removeFromOpenOrCloseList(kids.get(i));
                        openList.add(kids.get(i));
                    }
                }else{
                    /* node not in on open or close list*/
                    openList.add(kids.get(i));
                }
            }
            
            //if open list is empty then there is no possible way
            if(openList.isEmpty()){
                break;
            }

            //Select the minimun using UCS f(n)
            v = openList.get(pickTheNextNode(0));
        }

        pathToGoal.add("PATH NOT FOUND!");
        
        //clear for next algorithm 
        openList = new ArrayList<Node>();
        closeList = new ArrayList<Node>();
        return pathToGoal;
    }

    private List<String> A_STAR(){
        
        List<String> pathToGoal = new ArrayList<String>();
        
        String nodeName = "("+S[0]+","+S[1]+")";
        Node root = new Node(nodeName, 0, S);
        root.setHerValue(heurValues[S[0]][S[1]]);
        openList.add(root);
        
        Node v = root;
        while(!(openList.isEmpty())){

            removeVfromOpenList(v);
            //open kids here 
            v = generateKids(v,1);
            closeList.add(v); //add it to close list

            if(isGoal(v)){
                /**
                 * 
                 * make the return path
                 * 
                 */
                System.out.println("FOUND GOAL!");
                System.out.println("TOTAL COST: "+ v.getCost());
                pathToGoal=v.generatePath();
                return pathToGoal;
            }

            List<Node> kids = v.getKids();

            for(int i=0; i<kids.size(); i++){

                Node checkNodeFromLists = isInOpenOrCloseList(kids.get(i));
                if ((checkNodeFromLists.getCost()!=-1)){
                    /* node is in on open or close list  */
                    if (kids.get(i).getCost()>=checkNodeFromLists.getCost()){
                        v.removeKid(kids.get(i));
                        i--;
                    }else{
                        //remove from kids 
                        removeFromOpenOrCloseList(kids.get(i));
                        openList.add(kids.get(i));
                    }
                }else{
                    /* node not in on open or close list*/
                    openList.add(kids.get(i));                    
                }
            }

            //if open list is empty then there is no possible way
            if(openList.isEmpty()){
                break;
            }
            
            //Select the minimun using A* f(n)
            v = openList.get(pickTheNextNode(1));
            
        }   

        pathToGoal.add("PATH NOT FOUND!");

        return pathToGoal;
    }

    public static void main(String[] args){
        
        Scanner scanner = new Scanner(System.in);
        
        
        //Give the n input
        int n = 0;
        try { 
            System.out.print("Give size N: ");
            n = scanner.nextInt();
            if (n<0){
                System.out.println("Please give positive number for size N");
                return;
            }
        } catch (InputMismatchException e1) {
            System.out.println("Please give integer number for size N");
            return;
        }
        

        //Take probability 
        int p = 0;
        try {
            System.out.print("Give probability P: ");
            p = scanner.nextInt();
            if (p<0 || p>100){
                System.out.println("The probability must be between 0-100%");
                return;
            }
        } catch (InputMismatchException e2) {
            System.out.println("The probability must be between 0-100%");
            return;
        }

        
        
        
        //Give the Start posotion 
        int spotS_X = 0;
        int spotS_Y = 0;
        System.out.println("\nSet start position of robot");
        try {
            System.out.print("Give position X: ");
            spotS_X = scanner.nextInt();
            if (spotS_X<0 || spotS_X>(n-1)){
                System.out.println("Please give X between {0-"+(n-1)+"}");
            }
        } catch (InputMismatchException e3) {
            System.out.println("Please give X between {0-"+(n-1)+"}");
            return;
        }
        try {
            System.out.print("Give position Y: ");
            spotS_Y = scanner.nextInt();
            if (spotS_Y<0 || spotS_Y>(n-1)){
                System.out.println("Please give Y between {0-"+(n-1)+"}");
            }
        } catch (InputMismatchException e4) {
            System.out.println("Please give Y between {0-"+(n-1)+"}");
            return;
        }

        
        
        


        //Give the Goal position
        System.out.println("\nSet Goal position of robot");
        int spotG_X = 0;
        int spotG_Y = 0;

        try {
            System.out.print("Give position X: ");
            spotG_X = scanner.nextInt();
            if (spotG_X<0 || spotG_X>100){
                System.out.println("Please give X between {0-"+(n-1)+"}");
            }
        } catch (Exception e) {
            System.out.println("Please give X between {0-"+(n-1)+"}");
        }

        
        try {
            System.out.print("Give position Y: ");
            spotG_Y = scanner.nextInt();
            if (spotG_Y<0 || spotG_Y>100){
                System.out.println("Please give Y between {0-"+(n-1)+"}");
            }
        } catch (Exception e) {
            System.out.println("Please give Y between {0-"+(n-1)+"}");
        }
        
        
        
        
       

        robotLab RL = new robotLab(n, p, spotS_X, spotS_Y, spotG_X, spotG_Y);
        RL.gerateRandomObstacles();
        RL.GenerateHeurTable();
        RL.printLabyrinth();
        // RL.printChebyshevTable();
        
        System.out.println("UCS : ");
        List<String> generatedPath = RL.UCS();
        
        //print message
        System.out.println("PATH:\n");
        for(String nodePath: generatedPath){
            System.out.print(nodePath+" --> ");
        }
        System.out.print("END");
        System.out.println("\n");

        System.out.println("A* :");
        generatedPath = RL.A_STAR();
        
        //print message
        System.out.println("PATH:\n");
        for(String nodePath: generatedPath){
            System.out.print(nodePath+" --> ");
        }
        System.out.print("END");
        System.out.println("\n");
    }
}