package inf112.skeleton.app.Cards;

public interface IProgramCard extends ICard{

    /**
     * Get the priority value of the card
     *
     * @return The value
     */
    public int getPriority();

    /**
     * Returns an enum that explains what the cards programs the robot to do.
     *
     * @return The command
     */
    public CardType getCommand();
}
