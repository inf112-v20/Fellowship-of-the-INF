package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.player.Player;

import java.util.ArrayList;

//TODO Fix removing cards from the selected cards. Fix lockInButton to be disabled on a new round. Make locked cards still be read when starting a new round.
public class CardButton {
    private float selectedCardPosY;
    private float gap;
    private float width;
    private float height;
    private Stage stage;
    private Player player;
    private ImageButton lockInButton;

    private ArrayList<ProgramCard> playerHand;
    private ArrayList<Stack> cardButtons = new ArrayList<>();
    private ArrayList<Stack> leftOverCardButtons = new ArrayList<>();
    private Stack[] selectedCardButtons = new Stack[5];
    private ProgramCard[] selectedCards = new ProgramCard[5];
    private Image[] selectedCardImages = new Image[5];

    public CardButton(Player player, float width, float height, Stage stage, ImageButton lockInButton) {
        this.player = player;
        this.width = width;
        this.height = height;
        this.stage = stage;
        this.lockInButton = lockInButton;
        playerHand = player.getPlayerHandDeck();
        selectedCardPosY = height / 20;
        gap = playerHand.get(0).getTexture().getWidth() * 1.3f;
        createSelectedCardsImages();
        cardButtons = createCardButtons(playerHand);
        leftOverCardButtons = cardButtons;
        createLockedCardButtons();
    }

    public Image[] getSelectedCardImages(){return selectedCardImages;}

    public ArrayList<Stack> getLeftOverCardButtons(){ return leftOverCardButtons; }

    public ProgramCard[] getSelectedCards(){return selectedCards;}

    public Stack[] getSelectedCardButtons(){return selectedCardButtons;}

    public void setPos(Stack cardButton, float x, float y){ cardButton.setPosition(x, y); }

    public void createLockedCardButtons(){
        if(player.getSelectedCards()[4] != null){
            ArrayList<ProgramCard> lockedCards = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                System.out.println(player.getSelectedCards()[i].toString());
                if(player.getSelectedCards()[i] == null){continue;}
                lockedCards.add(player.getSelectedCards()[i]);
            }
            ArrayList<Stack> lockedCardButtons = createCardButtons(lockedCards);
            for (int i = 0; i < lockedCardButtons.size() ; i++) {
                Stack lockedCardButton = lockedCardButtons.get(i);
                selectedCardButtons[4-i] = lockedCardButton;
                float newPosX = selectedCardImages[4-i].getX() + 15;
                float newPosY = selectedCardImages[4-i].getY() + 15;
                setPos(lockedCardButton, newPosX, newPosY);
                lockedCardButtons.get(i).setTouchable(Touchable.disabled);
            }
        }
    }

    public void createSelectedCardsImages(){
        for (int i = 0; i < 5; i++) {
            Texture texture = new Texture(Gdx.files.internal("cardslot.png"));
            TextureRegion myTextureRegion = new TextureRegion(texture);
            TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
            Image selectedCardImage = new Image(myTexRegionDrawable);
            selectedCardImage.scaleBy(0.2f);
            float selectedCardPosX = (width * 0.51f) + (i * gap);
            selectedCardImage.setPosition(selectedCardPosX, selectedCardPosY);
            stage.addActor(selectedCardImage);
            selectedCardImages[i] = selectedCardImage;
        }
    }

    public ArrayList<Stack> createCardButtons(ArrayList<ProgramCard> listOfCards){
        ArrayList<Stack> listOfCardButtons = new ArrayList<>();
        for (int i = 0; i < listOfCards.size(); i++) {
            ProgramCard programCard = listOfCards.get(i);
            float posY = height * 0.55f;
            float posX = (width * 0.51f) + (i * gap);
            if (i > 4) {
                posY -= programCard.getTexture().getHeight() * 1.4f;
                posX = (width * 0.51f) + ((i - 5) * gap);
            }

            TextureRegion myTextureRegion = new TextureRegion(programCard.getTexture());
            TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
            ImageButton button = new ImageButton(myTexRegionDrawable);
            button.getImage().scaleBy(0.2f);

            Stack cardButton = new Stack();
            cardButton.setSize(button.getWidth()*1.2f, button.getHeight()*1.2f);
            cardButton.setOrigin(posX, posY);
            cardButton.setPosition(posX, posY);

            Table cardImage = new Table();
            cardImage.add(button).expand().fill().bottom().left().padTop(button.getHeight()*0.2f).padRight(button.getWidth()*0.2f);
            cardButton.add(cardImage);

            Table textImage = new Table();
            String priorityText = String.valueOf(programCard.getPriority());
            Label priorityTextLabel = drawText(priorityText);
            float leftPad = cardButton.getWidth()*0.55f;
            float topPad = cardButton.getHeight()*0.085f;
            textImage.add(priorityTextLabel).expand().fillX().top().right().padLeft(leftPad).padTop(topPad);
            cardButton.add(textImage);

            buttonLeftPressed(cardButton);
            buttonRightPressed(cardButton);
            stage.addActor(cardButton);
            listOfCardButtons.add(cardButton);
        }
        return listOfCardButtons;
    }

    //Methods for the buttonpresses
    //Left click to add the card to the list of selected cards
    public void buttonLeftPressed(Stack cardButton){
        final Stack tempButton = cardButton;
        cardButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCard(tempButton);
            }
        });
    }
    //Right click to deselect a card
    public void buttonRightPressed(Stack cardButton){
        final Stack tempButton = cardButton;
        cardButton.addListener(new ClickListener(Input.Buttons.RIGHT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeCard(tempButton);
            }
        });
    }

    public void addCard(Stack cardButton) {
        for (int i = 0; i < 5; i++) {
            if (selectedCardButtons[i] == cardButton) {
                return;
            }
        }
        for (int i = 0; i < 5; i++){
            if (selectedCardButtons[i] == null && selectedCardButtons[i] != cardButton) {
                selectedCardButtons[i] = cardButton;
                float newPosX = selectedCardImages[i].getX() + 15;
                float newPosY = selectedCardImages[i].getY()+ 15;
                for (int j = 0; j <playerHand.size() ; j++) {
                    if (cardButtons.get(j) == cardButton) {
                        selectedCards[i] = playerHand.get(j);
                        setPos(cardButton, newPosX, newPosY);
                        leftOverCardButtons.set(j, null);
                        if(hasSelectedFiveCards()){
                            Color c = lockInButton.getColor();
                            lockInButton.setColor(c.r, c.g, c.b, 1f);
                            lockInButton.setTouchable(Touchable.enabled);
                        }
                        return;
                    }
                }
            }
        }
    }

    public void removeCard(Stack cardButton){
        for (int i = 0; i < 5; i++) {
            if(selectedCardButtons[i] == cardButton) {
                selectedCardButtons[i] = null;
                selectedCards[i] = null;
                for (int j = 0; j < playerHand.size(); j++) {
                    if(cardButtons.get(j) == cardButton){
                        setPos(cardButton, cardButton.getOriginX(),cardButton.getOriginY()); //set the card back to its original position
                        leftOverCardButtons.set(j, cardButton);
                        if(!hasSelectedFiveCards()){
                            Color c = lockInButton.getColor();
                            lockInButton.setColor(c.r, c.g, c.b, 0.5f);
                            lockInButton.setTouchable(Touchable.disabled);
                        }
                        return;
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
        textLabel.setFontScale(4);
        return textLabel;
    }

    public boolean hasSelectedFiveCards(){
        for (int i = 0; i < 5; i++) {
            if(selectedCards[i] == null){return false;}
        }
        return true;
    }

    public float getSelectedCardPosY(){
        return selectedCardPosY;
    }
}
