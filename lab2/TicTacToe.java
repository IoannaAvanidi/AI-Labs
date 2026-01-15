
/**
 * 
 * IOANNA AVANIDI - AM:4977
 * GIANNIS KOKKINIS - AM:5109
 * 
 */

import java.util.Random;

class TicTacToe{

    private String grid[][] = { // 4X3
        {"","",""},
        {"","",""},
        {"","",""},
        {"","",""}
    };

    private Boolean checkIfSucPositions(int x1, int y1,int x2,int y2){
        
        /**
         * 
         * SAME POSITION
         * 
         */

        if ((x1==x2) && (y1==y2)){
            return false;
        }

        /**
         *  Right 
        */

        if ((y1+1==y2) && (y1+1<3)){
            return  false;
        }

        /**
         * left 
         * 
         */
        if (y1-1==y2 && (y1-1>=0)){
            return false;
        }

        /**
         * 
         * DOWN
         */
        if ((x1+1==x2) && (x1+1<4)){
            return false;
        }

        /**
         * 
         * UP
        */
        if ((x1-1==x2) && (x1-1>=0)){
            return false;
        }
        
        /*
         * UP RIGHT
         */
        if ( ((x1-1==x2)&&(y1+1==y2)) && (x1-1>=0) && (y1+1<3)){
            return false;
        } 

        /**
         * UP LEFT
         * 
         */
        if (((x1-1==x2)&&(y1-1==y2)) && (x1-1>=0) && (y1-1>=0)){
            return false;
        }

        /**
         * DOWN RIGHT
         */
        if (((x1+1==x2)&&(y1+1==y2)) && (x1+1<4) && (y1+1<3)){
            return false;
        }

        /**
         * DOWN LEFT
         */
        if (((x1+1==x2)&&(y1-1==y2)) && (x1+1<4) && (y1-1>=0)){
            return false;
        }
         

        return true;
    } 

    public TicTacToe(){
        Random rand = new Random();
        int posX1,posX2, posO1, posO2;

        posX1 = rand.nextInt(4);
        posX2 = rand.nextInt(3);
            
            
        posO1 = rand.nextInt(4);
        posO2 = rand.nextInt(3);
        while (!checkIfSucPositions(posX1,posX2,posO1,posO2)){ //check position 
            posO1 = rand.nextInt(4);
            posO2 = rand.nextInt(3);
        }


        this.grid[posX1][posX2] = "X";
        this.grid[posO1][posO2] = "O";


    }

    public boolean isGridFilled(){

        for(int c1=0; c1<4; c1++){
            for (int c2=0; c2<3; c2++){
                if (grid[c1][c2].length()==0){
                    return false;
                }
            }
        }

        return true;
    }
    
    
    private void copyGrid(String[][] gridToCopy){
        for (int c1=0; c1<4; c1++){
            for (int c2=0; c2<3; c2++){
                grid[c1][c2] = gridToCopy[c1][c2];
            }
        }
    }
    
    private int getWinningNodeIndex(Node v, int winValue){
        
        for(int index=0; index<v.getKids().size(); index++){
            if (v.getKids().get(index).getValue()==winValue){
                return index;
            }
        }

        return -1;
    }

    private void printWhoWins(int winValue){
        if (winValue==1){
            System.out.println("X WINS!");
        }else{
            if (winValue==0){
                System.out.println("TIE!");
            }else{
                System.out.println("O WINS!");
            }
        }
    }
    
    /**
     * 
     *  return true -> still playing
     *  return false -> stop playing
     */
    private boolean Xturn(){
        
        
        Node decNode = new Node(grid, true, true);
        int winValue = decNode.miniMax();
        if(decNode.getKids().isEmpty()){ //this means the game finish!
            printWhoWins(winValue);
            return false; //stop playing 
        }
        copyGrid(decNode.getKids().get(getWinningNodeIndex(decNode, winValue)).getGrid());
        printGrid();    
        return true; //still playing
    }

    private boolean Oturn(){

        Node decNode = new Node(grid, false, true);
        int winValue = decNode.miniMax();
        if(decNode.getKids().isEmpty()){ //this means the game finish!
            printWhoWins(winValue);
            return false; //stop playing
        }
        copyGrid(decNode.getKids().get(getWinningNodeIndex(decNode, winValue)).getGrid());
        printGrid();
        return true;//still playing

    }


    public void printGrid(){

        System.out.println("{");
        for (int i=0; i<4; i++){
            for (int j=0; j<3; j++){
                if (grid[i][j].equals("")){
                    System.out.print(grid[i][j] + " |");
                    continue;
                }
                System.out.print(grid[i][j] + "|");
            }
            System.out.print("\n");
        }
        System.out.println("}");
        
    }

    public void play(){


        boolean stillPlaying = true;

        while(stillPlaying){
            stillPlaying = Xturn();
            if (stillPlaying==false){
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stillPlaying = Oturn();

            
        }
    }
    

    
    
    public static void main(String[] args){
        TicTacToe tto = new TicTacToe();
        tto.printGrid();
        tto.play();
    }
    
     


}