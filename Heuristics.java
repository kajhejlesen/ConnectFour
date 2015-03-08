import java.util.Random;

public class Heuristics {
	
	public static int randomDecision(Minimax state) {
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
	
	public static int firstDecision(Minimax state) {
		boolean[] legalMoves = state.getLegalMoves();
		for (int i = 0; i < legalMoves.length; i++) {
			if (legalMoves[i])
				return i;
		}
		return -1;
	}
	
	public static int miniMaxDecision(Minimax state, int depth) {
		boolean[] legalMoves = state.getLegalMoves();
		
		double maximum = Double.NEGATIVE_INFINITY;
		int maxMove = -1;
		
		for (int i = 0; i < legalMoves.length; i++) {
			if (legalMoves[i]) {
				double moveValue = minValue(state.result(i, state.getPlayerID()), depth-1);
				System.out.print(moveValue + " ");
				if (moveValue > maximum) {
					maximum = moveValue;
					maxMove = i;
				}
			}
        }
        System.out.println();
		return maxMove;
	}
	
	private static double maxValue(Minimax state, int depth) {
		if (!state.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
			return state.utility();

		boolean[] legalMoves = state.getLegalMoves();

		double v = Double.NEGATIVE_INFINITY;
		
		for (int i = 0; i < legalMoves.length; i++) {
			if (legalMoves[i]) {
				double moveValue = minValue(state.result(i, state.getPlayerID()), depth-1);
				if (moveValue > v)
					v = moveValue;
			}
		}
		return v;
	}
	
	private static double minValue(Minimax state, int depth) {
		if (!state.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
			return state.utility();
		
		boolean[] legalMoves = state.getLegalMoves();

		double v = Double.POSITIVE_INFINITY;
		int opponent = state.getPlayerID() == 1 ? 2 : 1;
		
		for (int i = 0; i < legalMoves.length; i++) {
			if (legalMoves[i]) {
				double moveValue = maxValue(state.result(i, opponent), depth-1);
				if (moveValue < v)
					v = moveValue;
			}
		}		
		return v;
	}


    public static int alphaBeta(Minimax state, int depth) {
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

    private static double maxValueAB(Minimax state, int depth, double alpha, double beta) {
        if (!state.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
            return state.utility();

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

    private static double minValueAB(Minimax state, int depth, double alpha, double beta) {
        if (!state.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
            return state.utility();

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

	
}