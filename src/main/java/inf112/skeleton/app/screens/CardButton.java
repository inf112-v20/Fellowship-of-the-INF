package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.cards.ProgramCard;

import java.util.ArrayList;

public class CardButton {

    private float currentPosX;
    private float currentPosY;
    private float selectedCardPosY;
    private float textPosX;
    private float textPosY;
    private float gap;
    private float width;
    private Stage stage;

    private ArrayList<Float> xPositions = new ArrayList<>();
    private ArrayList<Float> yPositions = new ArrayList<>();
    private ArrayList<Float> selectedCardXPositions = new ArrayList<>();
    private ArrayList<ImageButton> cardButtons = new ArrayList<>();
    private ArrayList<ImageButton> leftOverCardButtons = new ArrayList<>();
    private ArrayList<Table> cardTexts = new ArrayList<>();
    private ArrayList<Table> leftOverCardTexts = new ArrayList<>();
    private ImageButton[] selectedCardButtons = new ImageButton[5];
    private ProgramCard[] selectedCards = new ProgramCard[5];
    private ArrayList<ProgramCard> playerHand;

    public CardButton(ArrayList<ProgramCard> playerHand, float width, float height, Stage stage) {
        this.playerHand = playerHand;
        this.width = width;
        this.stage = stage;
        selectedCardPosY = height / 20;
        gap = playerHand.get(0).getTexture().getWidth() * 1.7f;
        createSelectedCardsImages();

        for (int i = 0; i < playerHand.size(); i++) {
            ProgramCard programCard = playerHand.get(i);
            float posY = height * 0.6f;
            float posX = (width * 0.52f) + (i * gap);
            if (i > 4) {
                posY -= programCard.getTexture().getHeight() * 1.7f;
                posX = (width * 0.52f) + ((i - 5) * gap);
            }
            //Make an ImageButton with the cards texture and scaling it
            //Create a table with the ImageButton and the priorityNumber(as text) and set its position
            TextureRegion myTextureRegion = new TextureRegion(programCard.getTexture());
            TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
            ImageButton button = new ImageButton(myTexRegionDrawable);
            button.getImage().scaleBy(0.5f);
            button.setPosition(posX, posY);
            cardButtons.add(button);
            leftOverCardButtons.add(button);
            xPositions.add(posX);
            yPositions.add(posY);
            //Make clicklistener for the button (i.e. the card) for different mousepresses
            buttonLeftPressed(button);
            buttonRightPressed(button);
            stage.addActor(button);

            //Create prioritynumber as text on the cards
            Table table = new Table();
            textPosX = button.getWidth() * 1;
            textPosY = button.getHeight() * 1.27f;
            table.setPosition(posX + textPosX, posY + textPosY);
            String priorityText = String.valueOf(programCard.getPriority());
            table.add(drawText(priorityText));
            stage.addActor(table);
            cardTexts.add(table);
            leftOverCardTexts.add(table);

        }

    }

    public ArrayList<ImageButton> getLeftOverCardButtons(){ return leftOverCardButtons; }

    public ArrayList<Table> getLeftOverCardTexts(){return leftOverCardTexts;}

    public ProgramCard[] getSelectedCards(){return selectedCards;}

    public void setPos(ImageButton button, Table cardText, float x, float y){
        button.setPosition(x, y);
        cardText.setPosition(x + textPosX, y + textPosY);
    }

    public void createSelectedCardsImages(){
        for (int i = 0; i < 5; i++) {
            Texture texture = new Texture(Gdx.files.internal("cardslot.png"));
            TextureRegion myTextureRegion = new TextureRegion(texture);
            TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
            Image selectedCardImage = new Image(myTexRegionDrawable);
            selectedCardImage.scaleBy(0.5f);
            float selectedCardPosX = (width * 0.52f) + (i * gap);
            selectedCardImage.setPosition(selectedCardPosX, selectedCardPosY);
            selectedCardXPositions.add(selectedCardPosX);
            stage.addActor(selectedCardImage);
        }
    }

    //Methods for the buttonpresses
    //Left click to add the card to the list of selected cards
    public void buttonLeftPressed(ImageButton button){
        final ImageButton tempButton = button;
        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCard(tempButton);
            }
        });
    }
    //Right click to deselect a card
    public void buttonRightPressed(ImageButton button){
        final ImageButton tempButton = button;
        button.addListener(new ClickListener(Input.Buttons.RIGHT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeCard(tempButton);
            }
        });
    }

    public void addCard(ImageButton button) {
        for (int i = 0; i < 5; i++) {
            if (selectedCardButtons[i] == button) {
                return;
            }
        }
        for (int i = 0; i < 5; i++){
            if (selectedCardButtons[i] == null && selectedCardButtons[i] != button) {
                selectedCardButtons[i] = button;
                currentPosX = selectedCardXPositions.get(i) + 15;
                currentPosY = selectedCardPosY + 15;
                for (int j = 0; j <playerHand.size() ; j++) {
                    if (cardButtons.get(j) == button) {
                        selectedCards[i] = playerHand.get(j);
                        setPos(button, cardTexts.get(j), currentPosX, currentPosY);
                        leftOverCardButtons.set(j, null);
                        leftOverCardTexts.set(j, null);
                    }
                }
                return;
            }
        }
    }

    public void removeCard(ImageButton button){
        for (int i = 0; i < 5; i++) {
            if(selectedCardButtons[i] == button) {
                selectedCardButtons[i] = null;
                selectedCards[i] = null;
                for (int j = 0; j < playerHand.size(); j++) {
                    if(cardButtons.get(j) == button){
                        currentPosX = xPositions.get(j);
                        currentPosY = yPositions.get(j);
                        setPos(button, cardTexts.get(j), currentPosX, currentPosY); //set the card back to its original position
                        leftOverCardButtons.set(j, button);
                        leftOverCardTexts.set(j, cardTexts.get(j));
                    }
                }
            }
        }
    }

    public Label drawText(String text){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.GREEN;
        Label textLabel = new Label(text, labelStyle);
        textLabel.setFontScale(5);
        return textLabel;
    }
}
