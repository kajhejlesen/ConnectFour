import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class AlphaBeta{

    public static int alphaBeta(IGameBoard board, int cutoff) {
        boolean[] legalMoves = board.getLegalMoves();
        double alpha =  Double.NEGATIVE_INFINITY;
        double beta =   Double.POSITIVE_INFINITY;
        double value =  Double.NEGATIVE_INFINITY;
        int maxMove = -1;

        for (int k = 0; k < legalMoves.length; k++) {
            int i = legalMoves.length/2 + (k % 2 == 0 ? -1 : 1) * (k+1)/2;

            if (legalMoves[i]) {
                double moveValue = minValueAB(board.result(i, board.getPlayerID()), cutoff - 1, alpha, beta);
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

    private static double maxValueAB(IGameBoard board, int depth, double alpha, double beta) {
        if (!board.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
            return board.utility(depth);
        if (depth == 0) return board.utility(-1);

        boolean[] legalMoves = board.getLegalMoves();

        double value = Double.NEGATIVE_INFINITY;

        for (int k = 0; k < legalMoves.length; k++) {
            int i = legalMoves.length/2 + (k % 2 == 0 ? -1 : 1) * (k+1)/2;
            if (legalMoves[i]) {
                double moveValue = minValueAB(board.result(i, board.getPlayerID()), depth-1, alpha, beta);

                if (moveValue > value)
                    value = moveValue;
                if (value >= beta) return value;
                if (alpha <= value) alpha = value;
            }
        }
        return value;
    }

    private static double minValueAB(IGameBoard board, int depth, double alpha, double beta){
        if (!board.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
            return board.utility(depth);
        if (depth == 0) return board.utility(-1);

        boolean[] legalMoves = board.getLegalMoves();
        double value = Double.POSITIVE_INFINITY;
        int opponent = board.getPlayerID() == 1 ? 2 : 1;

        for (int k = 0; k < legalMoves.length; k++) {
            int i = legalMoves.length/2 + (k % 2 == 0 ? -1 : 1) * (k+1)/2;
            if (legalMoves[i]) {
                double moveValue = maxValueAB(board.result(i, opponent), depth-1, alpha, beta);
                if (moveValue < value)
                    value = moveValue;
                if (value <= alpha) return value;
                if (beta >= value) beta = value;
            }
        }
        return value;
    }

}