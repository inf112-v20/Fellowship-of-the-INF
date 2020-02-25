package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CardButton {
    private ImageButton button;
    private int movement; //0 = No movement, 1 = Move1, 2 = Move2, 3 = Move3, -1 = BackUp
    private int rotation; //0 = No rotation, 1 = Rotateleft, -1 = Rotateright, 2 = Uturn
    private int posX; //X position of the card, this is to remember its original position when unselecting a card
    private int slotNumber = -1; //The position of a card in the list of selected cards, -1 by default
    private Player player;
    private TiledMapTileLayer playerLayer;
    private static CardButton[] slots = {null, null, null, null, null}; //Static list of the selected cards
    private static ImageButton lockInButton;

    public CardButton(Player player, TiledMapTileLayer playerLayer, int movement, int rotation, int pos) {
        this.movement = movement;
        this.rotation = rotation;
        this.player = player;
        this.playerLayer = playerLayer;
        this.posX = pos;
        //Get correct texture/image depending on what card it is
        Texture myTexture = new Texture(Gdx.files.internal("cardtemplate.png"));
        if(movement == 1) {myTexture = new Texture(Gdx.files.internal("cardmove1.png"));}
        else if(movement == 2){myTexture = new Texture(Gdx.files.internal("cardmove2.png"));}
        else if(movement == 3){myTexture = new Texture(Gdx.files.internal("cardmove3.png"));}
        else if(movement == -1){myTexture = new Texture(Gdx.files.internal("cardbackup.png"));}
        else if(rotation == 1){myTexture = new Texture(Gdx.files.internal("cardlturn.png"));}
        else if(rotation == -1){myTexture = new Texture(Gdx.files.internal("cardrturn.png"));}
        else if(rotation == 2){myTexture = new Texture(Gdx.files.internal("carduturn.png"));}
        //Make an ImageButton with that texture, scaling it and positioning it
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable);
        button.getImage().setScale(0.4f,0.4f);
        button.setScale(0.4f);
        button.setPosition(posX, 300);
        //Make clicklistener for the button (i.e. the card) for different mousepresses
        buttonLeftPressed(button);
        buttonRightPressed(button);
        //Make lockInButton that is a static for the class. Maybe move this button to GameScreen??
        Texture lockInTexture = new Texture(Gdx.files.internal("lockinbutton.png"));
        TextureRegion lockInTextureRegion = new TextureRegion(lockInTexture);
        TextureRegionDrawable lockInTexRegionDrawable = new TextureRegionDrawable(lockInTextureRegion);
        lockInButton = new ImageButton(lockInTexRegionDrawable);
        lockInButton.getImage().setScale(0.6f,0.6f);
        lockInButton.setScale(0.6f);
        lockInButton.setPosition(800, 100);
        lockInButtonPressed(lockInButton);
    }
    public ImageButton getLockInButton(){return lockInButton;}
    public void setPos(float x, float y){button.setPosition(x, y);}
    public ImageButton getButton() {return button;}

    //Methods for the buttonpresses
    public void lockInButtonPressed(ImageButton lockInButton){
        lockInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runSlots();
            }
        });
    }
    //Left click to add the card to the list of selected cards
    //Hold ctrl + left click a card to that cards action
    public void buttonLeftPressed(ImageButton button){
        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){doCardAction(); return;}
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
        for (int slotPosition = 0; slotPosition < 5; slotPosition++) {
            if (slots[slotPosition] == null) {
                slots[slotPosition]= this;
                setPos(150 * slotPosition +5,80);
                this.slotNumber = slotPosition;
                return;
            }
        }
    }
    public void removeCard(){
        if(this.slotNumber == -1){return;}//do nothing if the card is not already selected
        slots[this.slotNumber] = null;
        this.slotNumber = -1;
        this.setPos(posX, 300); //set the card back to its original position
    }
    public void runSlots(){
        for (int i = 0; i < 5 ; i++) {
            if(slots[i]==null){System.out.println("Not enough cards");return;}
        }
        for (int i = 0; i < 5 ; i++) {
            slots[i].doCardAction();
        }
    }
    public void doCardAction(){
        //should change this to update map after every move
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
}
