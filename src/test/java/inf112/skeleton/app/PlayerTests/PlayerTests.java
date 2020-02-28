package inf112.skeleton.app.PlayerTests;

import com.badlogic.gdx.maps.tiled.TiledMap;
import inf112.skeleton.app.Screens.GameScreen;
import org.junit.Before;
import org.junit.Test;
import inf112.skeleton.app.*;
import inf112.skeleton.app.GridObjects.*;
import inf112.skeleton.app.Grid.*;
import inf112.skeleton.app.Deck.*;
import inf112.skeleton.app.Cards.*;

import static org.junit.Assert.assertEquals;

public class PlayerTests {
    TiledMap tiledMap = new TiledMap();
    Map map = new Map(12,12, tiledMap);
    Game game = new Game(map);
    Player player = new Player(1, game);

    @Test
    public void getPlayerTest(){
        assertEquals(playerpiece, player.getPlayerPiece());
    }
}
