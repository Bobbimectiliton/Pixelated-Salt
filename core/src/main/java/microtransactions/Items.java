/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 package microtransactions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Items {
    private int rarity;
    private String category;
    private float baseStats;
    private String name;
    private String description;
    private String path;
    public Items(int rarity, float baseStats, String category, String name, String description, String path) {
        this.rarity = rarity;
        this.category = category;
        this.baseStats = baseStats;
        this.name = name;
        this.description = description;
        this.path = path;
    }
    
    public float getScore() {
        return rarity*baseStats;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getRarity() {
        return rarity;
    }

    public Sprite getSprite() {
        Sprite sprite = new Sprite(new Texture(Gdx.files.internal(path)));
        sprite.setSize(400*0.9f, 400*0.9f);
        return sprite;
    } 
}
