package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.player.Player;


public class ScoreBoardScreen {
    private Stage stage;
    private Table table;
    private Game game;
    private Label.LabelStyle evenRowStyle;
    private Label.LabelStyle oddRowStyle;
    private Texture whiteTexture = new Texture(Gdx.files.internal("white.png"));

    public ScoreBoardScreen(Game game) {
        this.game = game;
        this.stage = new Stage();
        this.table = new Table();
        Texture texture = new Texture(Gdx.files.internal("background.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.setPosition(3600, 0);
        stage.addActor(image);

        Sprite backgroundColor1 = new Sprite(whiteTexture);
        backgroundColor1.setColor(Color.BLACK);
        evenRowStyle = new Label.LabelStyle();
        evenRowStyle.font = new BitmapFont();
        evenRowStyle.background = new SpriteDrawable(backgroundColor1);
        evenRowStyle.fontColor = Color.WHITE;

        Sprite backgroundColor2 = new Sprite(whiteTexture);
        backgroundColor2.setColor(Color.DARK_GRAY);
        oddRowStyle = new Label.LabelStyle();
        oddRowStyle.font = new BitmapFont();
        oddRowStyle.background = new SpriteDrawable(backgroundColor2);
        oddRowStyle.fontColor = Color.WHITE;
        update();
    }

    public void update(){
        table.clear();
        table.setWidth(3000);
        table.setHeight(3000);
        table.setPosition(4000,500);

        Label titleLabel = new Label("SCOREBOARD", evenRowStyle);
        titleLabel.setFontScale(25);
        titleLabel.setAlignment(1);

        TextureRegion whiteTR = new TextureRegion(whiteTexture);
        TextureRegionDrawable whiteTRD =  new TextureRegionDrawable(whiteTR);
        Image titleFiller = new Image(whiteTRD);
        titleFiller.setColor(Color.BLACK);

        table.add(titleFiller).fill();
        table.add(titleLabel).fill();
        table.row();


        String headLabelText  = "Name       Lives       Damage      Flags";
        Label headLabel = new Label(headLabelText, oddRowStyle);
        headLabel.setFontScale(10);
        Image headFiller = new Image(whiteTRD);
        headFiller.setColor(Color.DARK_GRAY);
        table.add(headFiller).fill();
        table.add(headLabel).left().fill();
        table.row();

        for (int i = 0; i < game.getListOfPlayers().length ; i++) {
            Player player = game.getListOfPlayers()[i];
            String text = "Player " + player.getPlayerNumber() + "      " + player.getLives() + "               "
                    + player.getDamage() + "                " + player.getCheckpointsVisited();
            Label playerLabel;
            Image backgroundColor = new Image(whiteTRD);
            if(i%2 == 0){
                playerLabel = new Label(text, evenRowStyle);
                backgroundColor.setColor(Color.BLACK);
            }
            else{
                playerLabel = new Label(text, oddRowStyle);
                backgroundColor.setColor(Color.DARK_GRAY);
            }
            playerLabel.setFontScale(10);
            TextureRegion playerPicture = player.getPlayerCell().getTile().getTextureRegion();
            TextureRegionDrawable playerPictureTRD = new TextureRegionDrawable(playerPicture);
            Stack stack = new Stack();
            stack.add(backgroundColor);
            Image playerImage = new Image(playerPictureTRD);
            stack.add(playerImage);
            table.add(stack).fill();
            table.add(playerLabel).expandX().left().fill();
            table.row();
        }
        //table.debug();
        stage.addActor(table);
    }

    public Stage getStage(){
        return stage;
    }
}
