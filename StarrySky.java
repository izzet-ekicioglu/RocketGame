package VLW1;

import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Random;

public class StarrySky extends PApplet {

    private static String version = "1.0";
    private static boolean debug = false;

    public static void main(String[] args) {
        if (debug) {System.out.println("StarrySky V" + version + " started.");}
        PApplet.runSketch(new String[]{""}, new StarrySky());
        System.out.println("Exit");
    }

    // Start-Stop Flag
    boolean pause = true;
    boolean newGame = false;

    // Colors
    int cDarkBlue = color(0, 0, 80);
    int cBeige = color(255, 255, 220);
    int cRed = color(200, 0, 0);
    int cWhite = color(255, 255, 255);

    // Create Stars
    ArrayList<StarrySky.Star> stars = new ArrayList<>();
    public void createStars() {
        int s = 0;
        int r = 0;
        for (int y = 0; y <= 1300; y += 50) {
            for (int x = 0; x <= 1700; x += 100) {
                s = (random.nextInt(10) + 1) * 10;
                r = (random.nextInt(10) + 1) * 10;
                if (y % 100 != 0) {
                    //int c = x + 50;
                    new StarrySky.Star(cBeige, x + s, y + r);
                } else {
                    new StarrySky.Star(cBeige, x + s, y + r);
                }
            }
        }
    }

    public void resetArray() {
        stars.clear();
    }

    Rocket rocket = new Rocket();

    public void settings() {
        size(1700, 1200);
    }

    public void setup() {
        noStroke();
        noLoop();
        frameRate(30);
        createStars();
    }

    public void draw() {
        background(cDarkBlue);
        for (StarrySky.Star star : stars) {
            star.draw();
        }
        rocket.draw();
        if (newGame) {
            startStop();
        }
    }

    public void mouseClicked() {
        startStop();
    }

    public void keyPressed() {
        if (debug) {System.out.println(keyCode + " pressed.");}
        if (keyCode == 32) {
            startStop();
        } else {
            rocket.move(keyCode);

        }
    }

    public void startStop() {
        pause = !pause;
        if (pause) {
            noLoop();
        } else {
            newGame = false;
            loop();
        }
    }

    Random random = new Random();

    int starCounter = 0;

    public class Star {
        int x, y;
        int col;

        boolean sparkle;

        Star(int col, int x, int y) {
            this.col = col;
            this.x = x;
            this.y = y;
            this.sparkle = false;

            stars.add(this);
            if (debug) {System.out.println("Star created.");}
        }

        boolean sparkling() {
            int s = random.nextInt(500);
            return s == 0;
        }

        void move() {
            if (x == 1720) {
                int s = 0;
                while (s < 30) {
                    s = random.nextInt(10) * 20;
                }
                x = -s;
                starCounter++;
            } else if (y == 1320) {
                int r = 0;
                while (r < 30) {
                    r = random.nextInt(10) * 10;
                }
                y = -r;
                starCounter++;
            } else {
                x++;
                y++;
            }
        }

        void draw() {
            move();
            if (sparkling()) {
                fill(col);
                ellipse(x, y, 10, 40);
                fill(col);
                ellipse(x, y, 40, 10);
            } else {
                fill(col);
                ellipse(x, y, 5, 20);
                fill(col);
                ellipse(x, y, 20, 5);
            }


        }
    }

    public class Rocket {
        int x, y;
        boolean landing = false;

        public Rocket() {
            this.x = 850;
            this.y = 1050;
        }

        void move(int keyCode) {
            if (!landing) {
                if (keyCode == 32) {
                    this.x = 850;
                    this.y = 1050;
                    if (debug) System.out.println("RESET");
                } else if (keyCode == 37) {
                    if (this.x > 80) this.x -= 10;
                    if (debug) System.out.println("Move LEFT");
                } else if (keyCode == 38) {
                    if (debug) System.out.println("Move UP");
                    //this.y -= 10;
                } else if (keyCode == 39) {
                    if (this.x < 1620) this.x += 10;
                    if (debug) System.out.println("Move RIGHT");
                } else if (keyCode == 40) {
                    //this.y += 10;
                    if (debug) System.out.println("Move DOWN");
                }
            }
        }

        void collisionCheck() {
            int top = y - 100;
            for (Star star : stars) {
                if (this.x <= star.x + 10 && this.x >= star.x - 10) {
                    if (top <= star.y + 10 && top >= star.y - 10) {
                        landing = true;
                        if (debug) System.out.println("Zusammenstoß!");
                    }

                }
            }
        }

        void startLanding() {
            if (this.x < 850) {
                this.x += 10;
            } else if (this.x > 850) {
                this.x -= 10;
            }
            if (this.x == 850) {
                this.landing = false;
                if (debug) System.out.println("Reset Array starting:");
                resetArray();
                if (debug) System.out.println("Array reseted");
                createStars();
                newGame = true;
                System.out.println("Score: " + starCounter);
                starCounter = 0;
            }
        }

        void draw() {
            // Body
            fill(cRed);
            ellipse(x, y, 60, 200);
            // Wings
            fill(cRed);
            ellipse(x - 50, y + 60, 20, 60);
            fill(cRed);
            ellipse(x + 50, y + 60, 20, 60);
            // Windows
            fill(cWhite);
            ellipse(x, y - 40, 30, 30);
            fill(cWhite);
            ellipse(x, y, 30, 30);
            // Top
            fill(cWhite);
            ellipse(x, y - 100, 10, 10);
            if (landing) {
                startLanding();
            } else {
                collisionCheck();
            }


        }
    }
}

