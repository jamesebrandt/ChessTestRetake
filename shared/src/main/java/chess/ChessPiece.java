package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor teamColor;
    private final ChessPiece.PieceType pieceType;
    private final Collection<ChessMove> moves = new ArrayList<>();

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return getTeamColor() == piece.getTeamColor() && getPieceType() == piece.getPieceType() && Objects.equals(moves, piece.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamColor(), getPieceType(), moves);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        switch (pieceType){
            case KING:
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1), board);

                return moves;

            case QUEEN:

                MoveInDirections(board, myPosition, 1, 1);
                MoveInDirections(board, myPosition, -1, 1);
                MoveInDirections(board, myPosition, -1, -1);
                MoveInDirections(board, myPosition, 1, -1);
                MoveInDirections(board, myPosition, 1, 0);
                MoveInDirections(board, myPosition, 0, 1);
                MoveInDirections(board, myPosition, -1, 0);
                MoveInDirections(board, myPosition, 0, -1);

                return moves;

            case BISHOP:

                MoveInDirections(board, myPosition, 1, 1);
                MoveInDirections(board, myPosition, -1, 1);
                MoveInDirections(board, myPosition, -1, -1);
                MoveInDirections(board, myPosition, 1, -1);


                return moves;

            case KNIGHT:
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2), board);
                ValidateAndAddMoves(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2), board);

                return moves;

            case ROOK:

                MoveInDirections(board, myPosition, 1, 0);
                MoveInDirections(board, myPosition, 0, 1);
                MoveInDirections(board, myPosition, -1, 0);
                MoveInDirections(board, myPosition, 0, -1);

                return moves;

            case PAWN:
                PawnMoves(myPosition, board);


                return moves;

        }
        return moves;
    }

    private void ValidateAndAddMoves(ChessPosition myPosition, ChessPosition endPosition, ChessBoard board) {
        if (IsOnBoard(endPosition) && (Check_Space(endPosition, board) == Space.OPEN || Check_Space(endPosition, board) == Space.CAPTURE)) {
            moves.add(new ChessMove(myPosition, endPosition, null));
        }
    }

    private boolean IsOnBoard(ChessPosition end) {
        if (end.getRow() > 0 && end.getColumn() > 0 && end.getRow() < 9 && end.getColumn() < 9) {
            return true;
        }
        return false;
    }

    private enum Space{
        OPEN,
        CAPTURE,
        SAME_TEAM
    }
    private Space Check_Space(ChessPosition end, ChessBoard board) {
        if (board.getPiece(end) == null){
            return Space.OPEN;
        }
        else if (board.getPiece(end).getTeamColor() != teamColor){
            return Space.CAPTURE;
        }
        else{
            return Space.SAME_TEAM;
        }
    }

    private void MoveInDirections (ChessBoard board, ChessPosition start, int row_direction, int column_direction) {
        ChessPosition nextStep = new ChessPosition(start.getRow()+row_direction, start.getColumn()+column_direction);
        while (IsOnBoard(nextStep)) {
            if (Check_Space(nextStep, board) == Space.OPEN) {
                moves.add(new ChessMove(start, nextStep, null));
            }
            else if (Check_Space(nextStep, board) == Space.CAPTURE) {
                moves.add(new ChessMove(start, nextStep, null));
                break;
            }
            else if (Check_Space(nextStep, board) == Space.SAME_TEAM) {
                break;
            }
            nextStep = new ChessPosition(nextStep.getRow()+row_direction, nextStep.getColumn()+column_direction);
        }
    }

    private void PawnMoves(ChessPosition myPosition, ChessBoard board) {

        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition One_Step = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            ChessPosition Two_Step = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
            ChessPosition Attack_Right = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            ChessPosition Attack_Left = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);

            if (Check_Space(One_Step, board) == Space.OPEN){
                PawnAddAndPromote(myPosition, One_Step);
                if (myPosition.getRow() == 2 && Check_Space(Two_Step, board) == Space.OPEN){
                    PawnAddAndPromote(myPosition, Two_Step);
                }
            }

            if (IsOnBoard(Attack_Right) && Check_Space(Attack_Right, board) == Space.CAPTURE){
                PawnAddAndPromote(myPosition, Attack_Right);
            }

            if (IsOnBoard(Attack_Left) && Check_Space(Attack_Left, board) == Space.CAPTURE){
                PawnAddAndPromote(myPosition, Attack_Left);
            }

        } else {
            ChessPosition One_Step = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            ChessPosition Two_Step = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
            ChessPosition Attack_Right = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            ChessPosition Attack_Left = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);

            if (Check_Space(One_Step, board) == Space.OPEN){
                PawnAddAndPromote(myPosition, One_Step);
                if (myPosition.getRow() == 7 && Check_Space(Two_Step, board) == Space.OPEN){
                    PawnAddAndPromote(myPosition, Two_Step);
                }
            }

            if (IsOnBoard(Attack_Right) && Check_Space(Attack_Right, board) == Space.CAPTURE){
                PawnAddAndPromote(myPosition, Attack_Right);
            }

            if (IsOnBoard(Attack_Left) && Check_Space(Attack_Left, board) == Space.CAPTURE){
                PawnAddAndPromote(myPosition, Attack_Left);
            }
        }
    }

    private void PawnAddAndPromote(ChessPosition start, ChessPosition end) {
        if (IsOnBoard(end)) {
            if (getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8) {
                moves.add(new ChessMove(start, end, PieceType.QUEEN));
                moves.add(new ChessMove(start, end, PieceType.KNIGHT));
                moves.add(new ChessMove(start, end, PieceType.ROOK));
                moves.add(new ChessMove(start, end, PieceType.BISHOP));
            } else if (getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1) {
                moves.add(new ChessMove(start, end, PieceType.QUEEN));
                moves.add(new ChessMove(start, end, PieceType.KNIGHT));
                moves.add(new ChessMove(start, end, PieceType.ROOK));
                moves.add(new ChessMove(start, end, PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(start, end, null));
            }
        }
    }
}
