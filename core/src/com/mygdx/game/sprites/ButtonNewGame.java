package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.base.ScaledButton;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.screen.GameScreen;

public class ButtonNewGame extends ScaledButton {


    private GameScreen gameScreen;
    private MainShip shipMain;



    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen, MainShip shipMain) throws GameException {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
        this.shipMain = shipMain;

    }


    @Override
    public void action() {
        shipMain.newGame();
        gameScreen.startNewGAme();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(-0.1f);
    }
}
