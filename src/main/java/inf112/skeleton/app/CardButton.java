package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Cards.ProgramCard;

import java.util.ArrayList;

public class CardButton {
    private ImageButton button;
    private ProgramCard programCard;
    private int posX; //X position of the card, this is to remember its original position when unselecting a card
    private int slotNumber = -1; //The position of a card in the list of selected cards, -1 by default
    private static ArrayList<CardButton> listOfCardButtons = new ArrayList<CardButton>(); //Static list of the selected cards
    private static CardButton[] selectedCardButtons = {null, null, null, null, null};
    private static ImageButton lockInButton;

    public CardButton(){
        Texture lockInTexture = new Texture(Gdx.files.internal("lockinbutton.png"));
        TextureRegion lockInTextureRegion = new TextureRegion(lockInTexture);
        TextureRegionDrawable lockInTexRegionDrawable = new TextureRegionDrawable(lockInTextureRegion);
        lockInButton = new ImageButton(lockInTexRegionDrawable);
        lockInButton.getImage().setScale(0.6f,0.6f);
        lockInButton.setScale(0.6f);
        lockInButton.setPosition(800, 100);
        lockInButtonPressed(lockInButton);
    }
    public CardButton(ProgramCard card, int pos) {
        this.programCard = card;
        this.posX = pos*150;

        //Make an ImageButton with the cards texture, scaling it and positioning it
        TextureRegion myTextureRegion = new TextureRegion(card.getTexture());
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable);
        button.getImage().setScale(0.4f,0.4f);
        button.setScale(0.4f);
        button.setPosition(posX, 300);
        //Make clicklistener for the button (i.e. the card) for different mousepresses
        buttonLeftPressed(button);
        buttonRightPressed(button);
        listOfCardButtons.add(this);
    }
    public ArrayList<CardButton> getListOfCardButtons() {return listOfCardButtons;}
    public ImageButton getLockInButton(){return lockInButton;}
    public void setPos(float x, float y){button.setPosition(x, y);}
    public ImageButton getButton() {return button;}

    //Methods for the buttonpresses
    public void lockInButtonPressed(ImageButton lockInButton){
        lockInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getSelectedProgramCards();
            }
        });
    }
    //Left click to add the card to the list of selected cards
    //Hold ctrl + left click a card to do that cards action
    public void buttonLeftPressed(ImageButton button){
        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCard();
            }
        });
    }
    //Right click to deselect a card
    public void buttonRightPressed(ImageButton button){
        button.addListener(new ClickListener(Input.Buttons.RIGHT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeCard();
            }
        });
    }

    public void addCard() {
        if(this.slotNumber != -1){return;}//do nothing if the card is already selected
        for (int i = 0; i < 5; i++) {
            if (selectedCardButtons[i] == null) {
                selectedCardButtons[i]= this;
                setPos(150 * i +5,80);
                this.slotNumber = i;
                return;
            }
        }
    }
    public void removeCard(){
        if(this.slotNumber == -1){return;}//do nothing if the card is not already selected
        selectedCardButtons[this.slotNumber] = null;
        this.slotNumber = -1;
        this.setPos(posX, 300); //set the card back to its original position
    }

    public ArrayList<ProgramCard> getSelectedProgramCards(){
        for (int i = 0; i < 5 ; i++) {
            if(selectedCardButtons[i]==null){System.out.println("Not enough cards");return null;}
        }
        ArrayList<ProgramCard> selectedProgramCards = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            selectedProgramCards.add(selectedCardButtons[i].programCard);
        }
        return selectedProgramCards;
    }
    /*
    public void doCardAction(){
        //PLAYER GETS THE LIST OF SELECTED CARDS AND THIS METHOD SHOULD BE IN PLAYER CLASS
        //SHOULD CHANGE THIS TO UPDATE MAP AFTER EVERY MOVE
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, null);
        int direction = player.getPlayerCell().getRotation();
        Vector2 pos = player.getPos();
        float newX = pos.x;
        float newY = pos.y;
        //Directions: 0 = North, 1 = West, 2 = South, 3 = East
        int newDirection = direction + this.rotation;
        if (newDirection > 3) {newDirection -= 4;}
        if (newDirection < 0) {newDirection += 4;}
        //Rotate player if this card a rotation card
        player.getPlayerCell().setRotation(newDirection);
        //Move player depending on the direction of the player
        if (direction == 0) { newY += this.movement; }
        else if (direction == 1) {newX -= this.movement;}
        else if (direction == 2) {newY -= this.movement;}
        else if (direction == 3) {newX += this.movement;}
        player.setPos(newX, newY);
        playerLayer.setCell((int) player.getPos().x, (int) player.getPos().y, player.getPlayerCell());
    }
     */
}
