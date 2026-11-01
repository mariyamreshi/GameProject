package se233.gameproject.model;

public class Laser {
    private double x,y;
    private double width = 5;
    private double height = 15;
    private double speed = 7.0;
    private boolean active = true;

    public Laser(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y -= speed;
        if (y<0) {
            active = false;
        }
    }

    public void deactivate() { active = false; }
    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() {return width; }
    public double getHeight() { return height; }
}
