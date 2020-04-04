package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.exception.GameException;
import com.mygdx.game.math.Rect;
import com.mygdx.game.pool.BulletPool;
import com.mygdx.game.pool.EnemyPool;
import com.mygdx.game.sprites.Background;
import com.mygdx.game.sprites.Bullet;
import com.mygdx.game.sprites.EnemyShip;
import com.mygdx.game.sprites.MainShip;
import com.mygdx.game.sprites.Star;

public class GameScreen extends BaseScreen {


    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private Star[] stars;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private MainShip shipMain;
    private EnemyShip enemyShip;

    Music music;

    private float animateInterval = 0.3f;
    private float animateTimer = 0.3f;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        bulletPool = new BulletPool();
        enemyPool = new EnemyPool();
        initSprites();
    }

    @Override
    public void render(float delta) {
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star: stars) {
            star.resize(worldBounds);
        }
        shipMain.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        shipMain.dispose();
        super.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        shipMain.keyDown(keycode);
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        shipMain.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        shipMain.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        shipMain.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta) {
        for (Star star: stars) {
            star.update(delta);
        }
        shipMain.update(delta);
        bulletPool.updateActiveSprites(delta);
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0;
            enemyShip = enemyPool.obtain();
            enemyShip.set(0.3f, 0);

        }
        enemyPool.updateActiveSprites(delta);

    }

    public void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star: stars) {
            star.draw(batch);
        }
        shipMain.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }


    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            shipMain = new MainShip(atlas, bulletPool);
            music.play();
            music.setLooping(true);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }
}
