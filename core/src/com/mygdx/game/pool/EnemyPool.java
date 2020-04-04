package com.mygdx.game.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.base.SpritesPool;
import com.mygdx.game.sprites.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip();
    }
}
