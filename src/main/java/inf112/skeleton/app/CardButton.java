package inf112.skeleton.app;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Cards.ProgramCard;

import java.util.ArrayList;

public class CardButton {
    private ProgramCard programCard;
    private float posX; //X position of the card, this is to remember its original position when unselecting a card
    private float posY = 600;
    private float selectedCardPosX;
    private float selectedCardPosY;
    private float currentPosX;
    private float currentPosY;
    private Table table;
    private int slotNumber = -1; //The position of a card in the list of selected cards, -1 by default
    private static CardButton[] selectedCardButtons = {null, null, null, null, null}; //Static lit of the selected cardbuttons

    public CardButton(ProgramCard card, float posX, float posY, float selectedCardPosX, float selectedCardPosY) {
        this.programCard = card;
        this.posX = posX;
        this.posY = posY;
        this.selectedCardPosX = selectedCardPosX;
        this.selectedCardPosY = selectedCardPosY;
        //Make an ImageButton with the cards texture and scaling it
        //Create a table with the ImageButton and the priorityNumber(as text) and set its position
        TextureRegion myTextureRegion = new TextureRegion(card.getTexture());
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        ImageButton button = new ImageButton(myTexRegionDrawable);
        button.getImage().scaleBy(0.5f);
        button.scaleBy(0.5f);
        table = new Table();
        table.addActor(button);
        String priorityText = String.valueOf(card.getPriority());
        table.addActor(drawText(priorityText));
        table.setPosition(posX,posY);
        //Make clicklistener for the button (i.e. the card) for different mousepresses
        buttonLeftPressed(button);
        buttonRightPressed(button);
    }
    public Table getTable(){
        return table;
    }

    public void setPos(float x, float y){table.setPosition(x, y);}

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
                currentPosX = selectedCardPosX + i*550 + 15;
                currentPosY = selectedCardPosY + 15;
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
    public Table drawText(String text){
        Table textTable = new Table();
        textTable.setPosition(260,640);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.GREEN;
        Label textLabel = new Label(text, labelStyle);
        textLabel.setFontScale(5);
        textTable.addActor(textLabel);
        return textTable;
    }
}
