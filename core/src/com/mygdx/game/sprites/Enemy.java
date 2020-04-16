package com.mygdx.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.Ship;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.ExplosionPool;

public class Enemy extends Ship {


    private final Vector2 descentV;


    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        isAutoShoot = false;
        descentV = new Vector2(0, -0.2f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        bulletPos.set(pos.x, pos.y - getHalfHeight());

        if (getTop() <= worldBounds.getTop()) {
            isAutoShoot = true;
            v.set(v0);
        }
        if (getBottom() < worldBounds.getBottom()) {
            destroy();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        isAutoShoot = false;
        v.set(descentV);
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            Sound shootSound,
            int hp,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(descentV);
        setHeightProportion(height);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y);
    }
}
