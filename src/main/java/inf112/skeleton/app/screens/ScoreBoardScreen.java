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
import inf112.skeleton.app.game_logic.Game;
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
        Texture texture = new Texture(Gdx.files.internal("ui/background.png"));
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

    public Stage getStage() {
        return stage;
    }

    public void update() {
        table.clear();
        table.setWidth(3000);
        table.setHeight(3000);
        table.setPosition(4000, 500);

        TextureRegion whiteTR = new TextureRegion(whiteTexture);
        TextureRegionDrawable whiteTRD = new TextureRegionDrawable(whiteTR);

        String headLabelText = "Name";
        Label headLabel = new Label(headLabelText, oddRowStyle);
        headLabel.setFontScale(10);
        headLabel.setAlignment(1);
        Image headFiller = new Image(whiteTRD);
        headFiller.setColor(Color.DARK_GRAY);
        table.add(headFiller).fill();
        table.add(headLabel).left().fill();

        Image lifeImage = createImage(new Texture((Gdx.files.internal("ui/lifetoken.png"))));
        Image damageImage = createImage(new Texture((Gdx.files.internal("ui/damagetokentab.png"))));
        Image checkpointImage = createImage(new Texture((Gdx.files.internal("ui/checkpointtoken.png"))));
        Image backgroundColor1 = new Image(whiteTRD);
        Image backgroundColor2 = new Image(whiteTRD);
        Image backgroundColor3 = new Image(whiteTRD);
        backgroundColor1.setColor(Color.DARK_GRAY);
        backgroundColor2.setColor(Color.DARK_GRAY);
        backgroundColor3.setColor(Color.DARK_GRAY);
        Stack lifeStack = new Stack();
        Stack damageStack = new Stack();
        Stack checkpointStack = new Stack();
        lifeStack.add(backgroundColor1);
        lifeStack.add(lifeImage);
        damageStack.add(backgroundColor2);
        damageStack.add(damageImage);
        checkpointStack.add(backgroundColor3);
        checkpointStack.add(checkpointImage);
        table.add(lifeStack).fill();
        table.add(damageStack).fill();
        table.add(checkpointStack).fill();
        table.row();

        for (int i = 0; i < game.getListOfPlayers().length; i++) {
            Player player = game.getListOfPlayers()[i];
            String playerText = "Player " + player.getPlayerNumber();
            String lifeText = "" + player.getLives();
            String damageText = "" + player.getDamage();
            String checkpointText = "" + player.getCheckpointsVisited();
            Label playerLabel;
            Label lifeLabel;
            Label damageLabel;
            Label checkpointLabel;
            Image backgroundColor = new Image(whiteTRD);
            if (i % 2 == 0) {
                playerLabel = new Label(playerText, evenRowStyle);
                lifeLabel = new Label(lifeText, evenRowStyle);
                damageLabel = new Label(damageText, evenRowStyle);
                checkpointLabel = new Label(checkpointText, evenRowStyle);
                backgroundColor.setColor(Color.BLACK);
            } else {
                playerLabel = new Label(playerText, oddRowStyle);
                lifeLabel = new Label(lifeText, oddRowStyle);
                damageLabel = new Label(damageText, oddRowStyle);
                checkpointLabel = new Label(checkpointText, oddRowStyle);
                backgroundColor.setColor(Color.DARK_GRAY);
            }
            playerLabel.setFontScale(10);
            playerLabel.setAlignment(1);
            lifeLabel.setFontScale(10);
            lifeLabel.setAlignment(1);
            damageLabel.setFontScale(10);
            damageLabel.setAlignment(1);
            checkpointLabel.setFontScale(10);
            checkpointLabel.setAlignment(1);
            TextureRegion playerPicture = player.getPlayerCell().getTile().getTextureRegion();
            TextureRegionDrawable playerPictureTRD = new TextureRegionDrawable(playerPicture);
            Stack stack = new Stack();
            stack.add(backgroundColor);
            Image playerImage = new Image(playerPictureTRD);
            stack.add(playerImage);
            table.add(stack).fill();
            table.add(playerLabel).expandX().center().fill();
            table.add(lifeLabel).expandX().center().fill();
            table.add(damageLabel).expandX().center().fill();
            table.add(checkpointLabel).expandX().center().fill();
            table.row();
        }
        stage.addActor(table);
    }

    public Image createImage(Texture texture) {
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        return new Image(textureRegionDrawable);
    }

}
