// Define named constants to represent the various states of the game
public static final int PLAYING    = 0;
public static final int DRAW       = 1;
public static final int CROSS_WON  = 2;
public static final int NOUGHT_WON = 3;

// The current state of the game
public static int currentState = PLAYING;  // Assigned to a named constant, which is easier to read
// and understand, instead of an int number 0