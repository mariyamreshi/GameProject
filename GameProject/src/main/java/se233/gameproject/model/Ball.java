package se233.gameproject.model;

import javafx.scene.image.Image;

public class Ball {
    private double x;
    private double y;
    private double radius;
    private double velocityX;
    private double velocityY;

    public Ball(double x, double y, double radius, double velocityX, double velocityY){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velocityX = velocityX;
        this.velocityY = velocityY;

    }

    public void move() {
        x += velocityX;
        y += velocityY;
    }

    public void bounceHorizontally(){
        velocityX = -velocityX;
    }
    public void bounceVertically(){
        velocityY = -velocityY;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getRadius(){
        return radius;
    }
    public double getVelocityY(){
        return velocityY;
    }

    public double getVelocityX(){
        return velocityX;
    }
}
