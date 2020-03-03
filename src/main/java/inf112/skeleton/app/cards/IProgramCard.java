package inf112.skeleton.app.cards;

public interface IProgramCard extends ICard{

    /**
     * Get the priority value of the card
     *
     * @return The value
     */
    int getPriority();

    /**
     * Returns an enum that explains what the cards programs the robot to do.
     *
     * @return The command
     */
    CardType getCommand();
}
