package fr.chess.deluxe.movement;

/**
 * pour vérifier de quel type est chaque mouvement effectué, vu que la prise en passant, le roque et la promotion ont
 *  tous les trois des comportements ne pouvant pas se décrire uniquement avec un "pièce x de point a à point b"
 */
public enum PieceMovementRules {

    DEFAULT,
    EN_PASSANT,
    CASTLING,
    PROMOTION
}
