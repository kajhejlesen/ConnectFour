import java.util.Random;

public class Heuristics {
	
	public static int randomDecision(Minimax state) {
		if (!state.gameFinished().equals(IGameLogic.Winner.NOT_FINISHED))
			return -1;
		Random gen = new Random();
		boolean[] legalMoves = state.getLegalMoves();
		while (true) {
			int choice = gen.nextInt(legalMoves.length);
			if (legalMoves[choice])
				return choice;
		}		
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
	

	
}