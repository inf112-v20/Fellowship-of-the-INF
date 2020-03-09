package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

    public ScoreBoardScreen(Game game) {
        this.game = game;
        this.stage = new Stage();
        this.table = new Table();
        Texture texture = new Texture(Gdx.files.internal("background.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
        Image image = new Image(myTexRegionDrawable);
        image.setPosition(3600, 0);

        Texture rowTexture = new Texture((Gdx.files.internal("white.png")));
        Sprite backgroundColor1 = new Sprite(rowTexture);

        float alpha = 0.75f;
        backgroundColor1.setColor(0, 0, 0, alpha);
        evenRowStyle = new Label.LabelStyle();
        evenRowStyle.font = new BitmapFont();
        evenRowStyle.background = new SpriteDrawable(backgroundColor1);
        evenRowStyle.fontColor = Color.WHITE;

        Sprite backgroundColor2 = new Sprite(rowTexture);
        backgroundColor2.setColor(Color.DARK_GRAY);
        oddRowStyle = new Label.LabelStyle();
        oddRowStyle.font = new BitmapFont();
        oddRowStyle.background = new SpriteDrawable(backgroundColor2);
        oddRowStyle.fontColor = Color.WHITE;

        stage.addActor(image);
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
        table.add(titleLabel).fillX();

        /*
        table.row();
        Label testLabel1 = new Label("HELLO", oddRowStyle);
        testLabel1.setFontScale(10);
        Label testLabel2 = new Label("WORLD", oddRowStyle);
        testLabel2.setFontScale(10);
        table.add(testLabel1, testLabel2);

         */

        table.row();
        String headLabelText  = "Name           Lifes           Damage          Flags";
        Label headLabel = new Label(headLabelText, oddRowStyle);
        headLabel.setFontScale(10);
        table.add(headLabel).fillX().left();
        table.row();
        for (int i = 0; i < game.getListOfPlayers().length ; i++) {
            Player player = game.getListOfPlayers()[i];
            String text = "Player " + player.getPlayerNumber() + "          " + player.getLives() + "                   "
                    + player.getDamage() + "                     " + player.getCheckpointsVisited();
            Label playerLabel;
            if(i%2 == 0){ playerLabel = new Label(text, evenRowStyle); }
            else{ playerLabel = new Label(text, oddRowStyle); }
            playerLabel.setFontScale(10);
            table.add(playerLabel).fillX().left();
            table.row();
        }
        // table.debug();
        stage.addActor(table);
    }

    public Stage getStage(){
        return stage;
    }
}
