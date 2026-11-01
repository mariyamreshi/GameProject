package se233.gameproject.model;

public class Item {
    private double x,y;
    private double width = 20;
    private double height = 10;
    private double speed = 3.0;
    private String type;
    private boolean active = true;

    public Item(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void move() {
        y += speed;
        if (y > 600) {
            active = false;
        }
    }

    public void deactivate() { active = false; }
    public boolean isActive() { return active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getType() { return type; }
}
