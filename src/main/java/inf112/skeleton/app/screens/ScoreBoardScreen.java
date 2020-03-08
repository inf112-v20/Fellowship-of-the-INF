package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import inf112.skeleton.app.Game;
import inf112.skeleton.app.player.Player;


public class ScoreBoardScreen {
    private Stage stage;
    private Table table;
    private Game game;

    public ScoreBoardScreen(Game game) {
        this.game = game;
        this.stage = new Stage();
        this.table = new Table();
        update();
    }


    public void update(){
        table.remove();
        table = new Table();
        table.setWidth(3000);
        table.setHeight(3000);
        table.setPosition(4000,500);
        table.setColor(Color.GOLD);
        Color c = table.getColor();
        table.setColor(c.r, c.g, c.b, 0.8f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Color.BLACK;
        Label titleLabel = new Label("SCOREBOARD", labelStyle);
        titleLabel.setFontScale(25);
        table.add(titleLabel);
        table.row();
        String headLabelText  = "Name           Lifes           Damage          Flags";
        Label headLabel = new Label(headLabelText, labelStyle);
        headLabel.setFontScale(10);
        table.add(headLabel).left();
        table.row();
        table.row();
        for (int i = 0; i < game.getListOfPlayers().length ; i++) {
            Player player = game.getListOfPlayers()[i];
            String text = "Player " + player.getPlayerNumber() + "          " + player.getLifes() + "                   "
                    + player.getDamage() + "                     " + player.getCheckpointsVisited();
            Label playerLabel = new Label(text, labelStyle);
            playerLabel.setFontScale(10);
            playerLabel.setColor(Color.GOLD);
            table.add(playerLabel).left();
            table.row();
        }
        //table.debug();
        stage.addActor(table);
    }

    public Stage getStage(){
        return stage;
    }
}
