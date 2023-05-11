package fr.chess.deluxe.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Stockes pour chaque joueur, son statut (normal, en échec...), la position de son roit, ainsi que l'ensemble
 *  des coordonnées des cases où ses pièces peuvent se déplacer
 */
public class PlayerInformation {
    private CheckStatus checkStatus;
    private Coordinates kingPosition;
    private Set<Coordinates> possibleMoves;

    public PlayerInformation() {
        this.checkStatus = CheckStatus.NONE;
        this.possibleMoves = new HashSet<>();
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Coordinates getKingPosition() {
        return kingPosition;
    }

    public void setKingPosition(Coordinates kingPosition) {
        this.kingPosition = kingPosition;
    }

    public Set<Coordinates> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(Set<Coordinates> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public enum CheckStatus {

        NONE,
        CHECK,
        CHECKMATE,
        STALEMATE;

        public boolean isCheck() {
            return this.equals(CHECK) || this.equals(CHECKMATE);
        }
    }
}
