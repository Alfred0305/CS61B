package tablut;
import java.util.List;

import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Alfred Wang
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move m = findMove();
        _controller.reportMove(m);
        return m.toString();
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = board();
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        int bestScore;
        Move bestMove;
        if (board.winner() != null || depth == 0) {
            return staticScore(board);
        }
        List<Move> allMoves = board.legalMoves(board.turn());
        bestMove = allMoves.get(0);

        if (sense == 1) {
            bestScore = -INFTY;
            for (Move m : allMoves) {
                board.makeMove(m);
                int value = findMove(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undoOnePos();
                if (value > bestScore) {
                    bestMove = m;
                    bestScore = value;
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            if (saveMove) {
                _lastFoundMove = bestMove;
            }
            return bestScore;
        } else {
            bestScore = INFTY;
            for (Move m : board.legalMoves(board.turn())) {
                board.makeMove(m);
                int value = findMove(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undoOnePos();
                if (value < bestScore) {
                    bestMove = m;
                    bestScore = value;
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            if (saveMove) {
                _lastFoundMove = bestMove;
            }
            return bestScore;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        if (board.moveCount() < 5) {
            return 2;
        }
        return 2;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int value;
        if (board.winner() == WHITE) {
            return WINNING_VALUE;
        } else if (board.winner() == BLACK) {
            return -WINNING_VALUE;
        }

        Square kingP = board.kingPosition();

        if (board.turn() == WHITE
                && (kingP.rookMove(0, 8 - kingP.row()) != null
                || kingP.rookMove(2, kingP.row()) != null
                || kingP.rookMove(1, 8 - kingP.col()) != null
                || kingP.rookMove(1, kingP.col()) != null)) {
            return WILL_WIN_VALUE;
        }

        int tmp1 = Math.max(8 - board.kingPosition().row(),
                board.kingPosition().row());
        int tmp2 = Math.max(8 - board.kingPosition().col(),
                board.kingPosition().col());
        value = Math.max(tmp1, tmp2);

        for (int i = 0; i < 4; i++) {
            Square s = kingP.rookMove(0, 1);
            if (s != null) {
                if (board.get(s) == BLACK) {
                    value -= 5;
                }
            }
        }
        return value;

    }
}
