import java.util.concurrent.ExecutionException;

public interface IGameBoard extends IGameLogic{
    boolean[] getLegalMoves();
    IGameBoard result(int column, int playerID);
    int getPlayerID();
    double utility(int depth);
}
