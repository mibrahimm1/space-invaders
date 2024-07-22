package com.ibrahim;

import java.awt.*;

public class Block {
    int x ;
    int y ;
    int width ;
    int height ;
    Image img ;

    public Block(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }
}

class Alien extends Block {
    boolean alive = true ;

    public Alien(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }
}

class Bullet extends Block {
    boolean used = false ;

    public Bullet(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }
}
