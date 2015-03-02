
public class Minimax implements IGameLogic {
	private int col = 0;
	private int row = 0;
	private int[][] state;
	private int playerID;

	private int usedFields = 0;
	private int totalFields;

	private int lastX;
	private int lastY;
	private int lastPlayer;
    
	public Minimax() {
		//TODO Write your implementation for this method
	}
	
	public void initializeGame(int x, int y, int playerID) {
		this.col = x;
		this.row = y;
		this.playerID = playerID;
		state = new int[col][row];
		totalFields = col * row;
	    }
	
	public int getPlayerID() {
		return this.playerID;
	}
	 
	public boolean[] getLegalMoves() {
		boolean[] isLegal = new boolean[col];
		for (int i = 0; i < col; i++) {
			isLegal[i] = state[i][row-1] == 0 ? true : false;
		}
		return isLegal;
	}
	
	public Winner gameFinished() {
        int r = Won(lastX, lastY, lastPlayer);
        //System.out.println(r);
        switch (r) {
			case 0: return Winner.NOT_FINISHED;
			case 1: return Winner.PLAYER1;
			case 2: return Winner.PLAYER2;
			case 3: return Winner.TIE;
			default: return Winner.NOT_FINISHED;
		}
	}

	private int Won(int x, int y, int p) {
		int c, ix, iy;

		//Horizontal
		c = 1; //count how many token is on a line
		for(ix = x+1; ix <= x+3 && ix < col; ix++) if(state[ix][y] == p) c++; else break;  
		for(ix = x-1; ix >= x-3 && ix >= 0; ix--) if(state[ix][y] == p) c++; else break;
		if (c >= 4) return p;

		//Vertical
		c = 1;
		for(iy = y-1; iy >= y-3 && iy >= 0; iy--) if(state[x][iy] == p) c++; else break;
		if (c >= 4) return p;

		//Sloope Down
		c = 1;
		for(int i = 1; i <= 3 && x + i < col && y + i < row; i++) if(state[x + i][y + i] == p) c++; else break;
		for(int i = 1; i <= 3 && x - i >= 0 && y - i >= 0; i++)    if(state[x - i][y - i] == p) c++; else break;
		if (c >= 4) return p;

		//Sloope Up
		c = 1;
		ix = x; iy = y;
		for(int i = 1; i <= 3 && x + i < col && y - i >= 0; i++) if(state[x + i][y - i] == p) c++; else break;  
		for(int i = 1; i <= 3 && x - i >= 0 && y + i < row; i++) if(state[x - i][y + i] == p) c++; else break;
		if (c >= 4) return p;


        for(int i = 0; i < col; i++) {
            if(state[i][row-1] == 0) return 0;
        }

		return 3;
	}
	
	public double utility() {
		int result = Won(lastX, lastY, lastPlayer);

		if(result == 3) return 0;
		else if(result == playerID) return 1;
		else return -1;
	}
	
	public void setState(Minimax that) {
        for (int i = 0; i < that.state.length; i++)
            for (int j = 0; j < that.state[0].length; j++)
		        this.state[i][j] = that.state[i][j];
        //this.state = that.state.clone();
	}

	public void insertCoin(int column, int playerID) {
		if (state[column][row-1] == 0) {
			for (int i = 0; i < row; i++) {
				if (state[column][i] == 0) {
					state[column][i] = playerID;
					lastX = column; lastY = i;
					lastPlayer = playerID;
					usedFields++;
					break;
				}
			}
		}
        //TODO Write your implementation for this method	
	}

	public Minimax result(int move, int playerID) {
		Minimax newState = new Minimax();
		newState.initializeGame(this.col, this.row, this.playerID);
		newState.setState(this);
		newState.insertCoin(move, playerID);
		return newState;
	}
	
	public int decideNextMove() {
		//TODO Write your implementation for this method
		//return Heuristics.randomDecision(this);
		//return Heuristics.firstDecision(this);
		return Heuristics.miniMaxDecision(this, 1000000000);
	}
}
