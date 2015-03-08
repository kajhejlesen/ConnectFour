import java.util.Random;

public class RandomWalks implements IGameLogic {
    private final static int MAX_DEPTH = 8;

    private int col = 0;
    private int row = 0;
    private int[][] state;
    private int playerID;

    private int usedFields = 0;
    private int totalFields;

    private int lastX;
    private int lastY;
    private int lastPlayer;

    public RandomWalks() {
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
            isLegal[i] = state[i][row-1] == 0;
        }
        return isLegal;
    }

    public Winner gameFinished() {
        int r = won(lastX, lastY, lastPlayer);
        //System.out.println(r);
        switch (r) {
            case 0: return Winner.NOT_FINISHED;
            case 1: return Winner.PLAYER1;
            case 2: return Winner.PLAYER2;
            case 3: return Winner.TIE;
            default: return Winner.NOT_FINISHED;
        }
    }

    private int won(int x, int y, int p) {
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

        // Checking for full board
        for(int i = 0; i < col; i++)
            if(state[i][row - 1] == 0) return 0;

        return 3;
    }

    // depth == -1 on the random walks, as weight to solutions found early
    public int eval(int depth) {
        int result = won(lastX, lastY, lastPlayer);
        if (result == 3) return 0;
        else if (result == playerID) return 2 + depth;
        else return -2 - depth;
    }

    public double utility(int depth) {
        if (depth > 0) return eval(depth);
        return randomWalks(20 + usedFields*4, this);

    }

    public void setState(RandomWalks that) {
        for (int i = 0; i < that.state.length; i++)
            this.state[i] = that.state[i].clone();
        this.usedFields = that.usedFields;
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

    public RandomWalks result(int move, int playerID) {
        RandomWalks newState = new RandomWalks();
        newState.initializeGame(this.col, this.row, this.playerID);
        newState.setState(this);
        newState.insertCoin(move, playerID);
        return newState;
    }

    public int decideNextMove() {
        //TODO Write your implementation for this method
        return alphaBeta(this, MAX_DEPTH);
    }



    public static int alphaBeta(RandomWalks state, int depth) {
        boolean[] legalMoves = state.getLegalMoves();
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double value = Double.NEGATIVE_INFINITY;
        int maxMove = -1;

        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i]) {
                double moveValue = minValueAB(state.result(i, state.getPlayerID()), depth - 1, alpha, beta);
                System.out.print(moveValue + " ");
                if (moveValue > value) {
                    value = moveValue;
                    maxMove = i;
                }
                if (alpha <= value) alpha = value;
            }
        }
        System.out.println();
        return maxMove;
    }

    private static double maxValueAB(RandomWalks state, int depth, double alpha, double beta) {
        if (!state.gameFinished().equals(Winner.NOT_FINISHED))
            return state.utility(depth);
        if (depth == 0) return state.utility(-1);

        boolean[] legalMoves = state.getLegalMoves();

        double value = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i]) {
                double moveValue = minValueAB(state.result(i, state.getPlayerID()), depth-1, alpha, beta);

                if (moveValue > value)
                    value = moveValue;
                if (value >= beta) return value;
                if (alpha <= value) alpha = value;
            }
        }
        return value;
    }

    private static double minValueAB(RandomWalks state, int depth, double alpha, double beta) {
        if (!state.gameFinished().equals(Winner.NOT_FINISHED))
            return state.utility(depth);
        if (depth == 0) return state.utility(-1);

        boolean[] legalMoves = state.getLegalMoves();
        double value = Double.POSITIVE_INFINITY;
        int opponent = state.getPlayerID() == 1 ? 2 : 1;

        for (int i = 0; i < legalMoves.length; i++) {
            if (legalMoves[i]) {
                double moveValue = maxValueAB(state.result(i, opponent), depth-1, alpha, beta);
                if (moveValue < value)
                    value = moveValue;
                if (value <= alpha) return value;
                if (beta >= value) beta = value;
            }
        }
        return value;
    }

    public static int randomDecision(RandomWalks state) {
        if (!state.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
            return -1;

        Random gen = new Random();
        boolean[] legalMoves = state.getLegalMoves();
        int[] allowedMoves = new int[legalMoves.length];
        int moves = 0;
        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i]) allowedMoves[moves++] = i;

        return allowedMoves[gen.nextInt(moves)];
    }

    protected static double randomWalks(int count, RandomWalks state) {
        double aggregate = 0.0;
        for (int i = 0; i < count; i++) {
            RandomWalks newState = new RandomWalks();
            newState.initializeGame(state.col, state.row, state.playerID);
            newState.setState(state);
            aggregate += (double) walk(newState);
        }
        return aggregate / count;
    }

    protected static int walk(RandomWalks state) {
        int currentPlayer = state.getPlayerID();
        while (state.gameFinished().equals(Winner.NOT_FINISHED)) {
            currentPlayer = currentPlayer == 1 ? 2 : 1;
            state.insertCoin(randomDecision(state), currentPlayer);
        }
        return state.eval(0);
    }

}
