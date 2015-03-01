
public class Minimax implements IGameLogic {
    private int x = 0;
    private int y = 0;
	private int[][] state;
    private int playerID;
    
    public Minimax() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
		state = new int[x][y];
        //TODO Write your implementation for this method
    }
	
	public int getPlayerID() {
		return this.playerID;
	}
	 
	public boolean[] getLegalMoves() {
		boolean[] isLegal = new boolean[x];
		for (int i = 0; i < x; i++) {
			isLegal[i] = state[i][y-1] == 0 ? true : false;
		}
		return isLegal;
	}
	
    public Winner gameFinished() {
        //TODO Write your implementation for this method
        return Winner.NOT_FINISHED;
    }
	
	public double utility() {
		// temporary utility
		int count = 0;
		for (int i = 0; i < y; i++)
			if (state[2][i] == playerID)
				count++;
		return (double) count;
	}
	
	public void setState(int[][] state) {
        for (int i = 0; i < state.length; i++)
            for (int j = 0; j < state[0].length; j++)
		        this.state[i][j] = state[i][j];
	}

    public void insertCoin(int column, int playerID) {
		if (state[column][y-1] == 0) {
			for (int i = 0; i < y; i++) {
				if (state[column][i] == 0) {
					state[column][i] = playerID;
					break;
				}
			}
		}
        //TODO Write your implementation for this method	
    }

	public Minimax result(int move, int playerID) {
		Minimax newState = new Minimax();
		newState.initializeGame(this.x, this.y, this.playerID);
		newState.setState(this.state);
		newState.insertCoin(move, playerID);
		return newState;
	}
	
    public int decideNextMove() {
        //TODO Write your implementation for this method
        
		//return Heuristics.randomDecision(this);
		//return Heuristics.firstDecision(this);
		return Heuristics.miniMaxDecision(this, 3);
    }
	


}
