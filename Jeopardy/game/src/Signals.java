/*
 * ERR: something is wrong (use for validation)
 * ACK: everything is good (use for validation)
 * GAMEOVER: (the game is finished)
 * GAMECONT: (the game isn't finished)
 * DDW: this question is a daily double & the player can wager
 * DDNW: the question is a daily double and the player can't wager
 * NDD: the question is not a daily double
 */
public enum Signals {
	ERR, ACK, GAMEOVER, GAMECONT, DDW, DDNW, NDD
}
