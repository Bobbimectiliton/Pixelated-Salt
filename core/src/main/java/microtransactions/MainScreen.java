package microtransactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** First screen of the application. Displayed after the application is created. */
public class MainScreen implements Screen {

    io.github.killtheinnocents.Main main;
    public inventory inv;
    Texture background;
    Music funkyBeats;

    ScreenViewport viewport;
    SpriteBatch batch;
    Stage stage;
    Table mainMenu;
    Skin skin;
    BitmapFont font;
    GlyphLayout layout;

    TextButton openCrate;
    TextButton inputMon;
    TextButton openInv;
    TextButton exitShop;

    public static final int ITEM_BOX_WIDTH = 400;
    public static final float WORLD_WIDTH = 1100;
    public static final float WORLD_HEIGHT = 800;
    boolean Pulling;

    Sprite common;
    Sprite rare;
    Sprite epic;
    Sprite legendary;
    Sprite pullCertBg;

    private String moneyText = "";
    private String scoreText = "";

    private HashMap<Integer, ArrayList<Items>> itemPool;
    private LinkedList<Items> inventory;
    private double money;
    private double score;
    private int curDiff;
    private dataReader savedData;
    
    public MainScreen(io.github.killtheinnocents.Main main){
        this.main = main;
        savedData = new dataReader();
        itemPool = savedData.getItemPool();
        inventory = savedData.getInventory();
        money = savedData.getMoney();
        score = savedData.getScore();
        curDiff = savedData.getCurDiff();
        Pulling = false;
        inv = new inventory(inventory, main, MainScreen.this, (float)score);

        Texture font_texture = new Texture(Gdx.files.internal("game_font.png"));
        font_texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font = new BitmapFont(Gdx.files.internal("game_font.fnt"), new TextureRegion(font_texture));
        font.getData().setScale(1.2f);

        viewport = new ScreenViewport();
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("shop/skin/uiskin.json"));

        layout = new GlyphLayout(); // used to apply font to text
    }

    @Override
    public void show() {
        // Prepare your screen here.
        Gdx.input.setInputProcessor(stage);

        background = new Texture("shop/Background.png");
        pullCertBg = new Sprite(new Texture(Gdx.files.internal("shop/Pull bg.png")));

        // Create a new TextButtonStyle and set the font
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = skin.newDrawable("default-round");
        textButtonStyle.down = skin.newDrawable("default-round-down");

        // Rarity BGs
        common = new Sprite(new Texture(Gdx.files.internal("shop/common.png")));
        common.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
        rare = new Sprite(new Texture(Gdx.files.internal("shop/rare.png")));
        rare.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
        epic = new Sprite(new Texture(Gdx.files.internal("shop/epic.png")));
        epic.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);
        legendary = new Sprite(new Texture(Gdx.files.internal("shop/legendary.png")));
        legendary.setSize(ITEM_BOX_WIDTH, ITEM_BOX_WIDTH);

        inputMon = new TextButton("Click to input money", textButtonStyle);
        inputMon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                money += 100;
            }
        });

        char[][] text = new char[3][];
        text[0] = new char[]{'O','p','e','n'};
        text[1] = new char[]{' ', 'a'};
        text[2] = new char[]{' ','C','r','a','t','e'};
        String toBePrinted = "";

        for(int i = 0; i< text.length;i++){
            for(int j = 0; j< text[i].length;j++){
                toBePrinted += text[i][j];
            }
        }

        openCrate = new TextButton(toBePrinted, textButtonStyle);
        openCrate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!Pulling && money>=1000){
                    double rate = Math.random();
                    Items item = openCrate((float)rate);
                    inventory.add(item);
                    money -= 1000;
                    Pulling = true;
                }
            }
        });

        exitShop = new TextButton("Exit Shop", textButtonStyle);
        exitShop.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                try {
                    Gdx.app.exit();
                } catch (Exception e) {
                }
            }
        });

        //I'll implement this when everything is finally fixed
        /*openInv = new TextButton("Open Inventory", textButtonStyle);
        openInv.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                savedData.setInventory(inventory);
                main.setScreen(inv);
            }
        });*/

        mainMenu = new Table();
        mainMenu.setFillParent(true);
        mainMenu.row().row();
        mainMenu.defaults().top().left()/*.expand()*/.pad(5);

        mainMenu.add(openCrate);
        mainMenu.add(openInv);
        mainMenu.add(inputMon);

        stage.addActor(mainMenu);
        /* 
        funkyBeats = Gdx.audio.newMusic(Gdx.files.internal("shop/placeholder.mp3"));
        funkyBeats.setLooping(true);
        funkyBeats.setVolume(0.5f);
        funkyBeats.play();*/
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        batch.draw(background, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();

        stage.act(delta);
        stage.draw();
        
        batch.begin();
        moneyText = "$" + money;
        layout.setText(font, moneyText);
        font.draw(batch, moneyText, WORLD_WIDTH-layout.width-10, WORLD_HEIGHT-1.1f*layout.height);
        
        scoreText = "Current Score: " + String.format("%.2f", savedData.calcScore());
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, WORLD_WIDTH-layout.width-10, WORLD_HEIGHT-2.4f*layout.height);

        // Handle pulling animation
        if (Pulling) {
            Items pulled = inventory.getLast();
            Sprite sprite = pulled.getSprite();
            Sprite itemBg = getItemBackground((int)pulled.getRarity());
            Sprite certBg = new Sprite(pullCertBg);
            certBg.setSize(ITEM_BOX_WIDTH*2f, ITEM_BOX_WIDTH*2f);

            //Center everything
            sprite.setPosition(WORLD_WIDTH/2-sprite.getWidth()/2, WORLD_HEIGHT/2-sprite.getHeight()/2+100);
            itemBg.setPosition(WORLD_WIDTH/2-ITEM_BOX_WIDTH/2, WORLD_HEIGHT/2-ITEM_BOX_WIDTH/2+100);
            certBg.setPosition(WORLD_WIDTH/2-certBg.getWidth()/2, WORLD_HEIGHT/2-certBg.getHeight()/2+50);
            certBg.draw(batch);
            itemBg.draw(batch);
            sprite.draw(batch);
            String itemGot = "You got: " + pulled.getName();
            layout.setText(font, itemGot);
            font.draw(batch, itemGot, WORLD_WIDTH/2-layout.width/2, itemBg.getY()-25);
            layout.setText(font, "Press SPACE to move on");
            font.draw(batch, "Press SPACE to move on", WORLD_WIDTH/2-layout.width/2, itemBg.getY()-75);
            if(Gdx.input.isKeyPressed(Keys.SPACE)){
                Pulling = !Pulling;
            }
        }

        batch.end();
    }

    private Sprite getItemBackground(int rarity) {
        switch (rarity) {
            case 1:
                return new Sprite(common);
            case 2:
                return new Sprite(rare);
            case 3:
                return new Sprite(epic);
            case 4:
                return new Sprite(legendary);
            default:
                System.out.println("Rarity not found: Line 209");
                return new Sprite(common); // Default to common if rarity is invalid
        }
    }

    private Items openCrate(float rate){
        // Open a crate
        if(rate<=0.80 && !itemPool.get(0).isEmpty()){
            inventory.add(itemPool.get(0).get((int)(Math.random()*itemPool.get(0).size())));
        }else if(rate<=0.95 && !itemPool.get(1).isEmpty()){
            inventory.add(itemPool.get(1).get((int)(Math.random()*itemPool.get(1).size())));
        }else if(rate<=0.99 && !itemPool.get(2).isEmpty()){
            inventory.add(itemPool.get(2).get((int)(Math.random()*itemPool.get(2).size())));
        }else{
            inventory.add(itemPool.get(3).get((int)(Math.random()*itemPool.get(3).size())));
        }
        return inventory.getLast();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        batch.dispose();
        background.dispose();
        if (funkyBeats != null) funkyBeats.dispose();
        stage.dispose();
        skin.dispose();
        font.dispose();
        common.getTexture().dispose();
        rare.getTexture().dispose();
        epic.getTexture().dispose();
        legendary.getTexture().dispose();
    }
}