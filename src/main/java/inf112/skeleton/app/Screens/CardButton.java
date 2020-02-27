package inf112.skeleton.app.Screens;

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
    private GameScreen gameScreen;
    private float posX; //X position of the card, this is to remember its original position when unselecting a card
    private float posY = 600;
    private float selectedCardPosX = 1000;
    private float selectedCardPosY = 75;
    private float currentPosX;
    private float currentPosY;
    private int slotNumber = -1; //The position of a card in the list of selected cards, -1 by default
    private static ArrayList<CardButton> listOfCardButtons = new ArrayList<CardButton>(); //Static list of the selected cards
    private static CardButton[] selectedCardButtons = {null, null, null, null, null}; //Static lit of the selected cardbuttons
    private static ImageButton lockInButton;


    public CardButton(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        Texture lockInTexture = new Texture(Gdx.files.internal("lockinbutton.png"));
        TextureRegion lockInTextureRegion = new TextureRegion(lockInTexture);
        TextureRegionDrawable lockInTexRegionDrawable = new TextureRegionDrawable(lockInTextureRegion);
        lockInButton = new ImageButton(lockInTexRegionDrawable);
        lockInButton.getImage().setScale(0.5f,0.5f);
        lockInButton.setScale(0.5f);
        lockInButton.setPosition(1750, 125);
        lockInButtonPressed(lockInButton);
    }
    public CardButton(ProgramCard card, float pos) {
        this.programCard = card;

        if(listOfCardButtons.size()>4){posY -= 225; pos -= 5;}
        this.posX = pos*150 + 1000;
        currentPosX = posX;
        currentPosY = posY;
        //Make an ImageButton with the cards texture, scaling it and positioning it
        TextureRegion myTextureRegion = new TextureRegion(card.getTexture());
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable);
        button.getImage().setScale(0.4f,0.4f);
        button.setScale(0.4f);
        button.setPosition(posX, posY);
        //Make clicklistener for the button (i.e. the card) for different mousepresses
        buttonLeftPressed(button);
        buttonRightPressed(button);
        //Add the CardButton to an list where all elements will be added as actors in Stage in GameScreen
        listOfCardButtons.add(this);
    }
    public float getSelectedCardPosX(){return selectedCardPosX;}
    public float getSelectedCardPosY(){return selectedCardPosY;}
    public float getCurrentPosX(){return currentPosX;}
    public float getCurrentPosY(){return currentPosY;}
    public ArrayList<CardButton> getListOfCardButtons() {return listOfCardButtons;}
    public ImageButton getLockInButton(){return lockInButton;}
    public ImageButton getButton() {return button;}
    public ProgramCard getCard(){return programCard;}
    public void setPos(float x, float y){button.setPosition(x, y);}

    //Methods for the buttonpresses
    public void lockInButtonPressed(ImageButton lockInButton){
        lockInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.executeLockIn(getSelectedProgramCards());
            }
        });
    }
    //Left click to add the card to the list of selected cards
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
        if(slotNumber != -1){return;} //do nothing if the card is already selected
        for (int i = 0; i < 5; i++) {
            if (selectedCardButtons[i] == null) {
                selectedCardButtons[i]= this;
                currentPosX = 150 *i + selectedCardPosX + 5;
                currentPosY = selectedCardPosY + 5;
                setPos(currentPosX, currentPosY);
                slotNumber = i;
                return;
            }
        }
    }
    public void removeCard(){
        if(slotNumber == -1){return;} //do nothing if the card is not already selected
        selectedCardButtons[slotNumber] = null;
        slotNumber = -1;
        setPos(posX, posY); //set the card back to its original position
        currentPosX = posX;
        currentPosY = posY;
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
}
