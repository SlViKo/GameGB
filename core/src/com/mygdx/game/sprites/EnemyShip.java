package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Sprite;
import com.mygdx.game.math.Rect;
import com.mygdx.game.math.Rnd;

public class EnemyShip extends Sprite {

    private final float STEP_SHIP = 0.5f;
    private final float HIGHT_SHIP = 0.15f;

    private Vector2 v;

    private static TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));


    public EnemyShip() {
        super(atlas.findRegion("enemy2"), 1, 2, 2);
        v = new Vector2();
        pos.set(-1f, 0);
    }

    public void set(
        float height,
        float speed
    ) {
        setHeightProportion(height);
        this.v.set(speed, 0);
    }

    @Override
    public void update(float delta) {
        pos.add(v);
        super.update(delta);
    }

}
