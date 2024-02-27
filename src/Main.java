import java.util.Scanner;

public class Main{
    public final static int HEIGHT = 8;
    public final static int WIDTH = HEIGHT;
    public final static int x[] = new int[WIDTH*HEIGHT];
    public final static int y[] = new int[HEIGHT*HEIGHT];
    public static int bodyParts = 6;
    public static int[] direction = new int[2];
    public static int appleX;
    public static int appleY;
    public final static String headIcon = "◉";
    public final static String bodyIcon = "●";
    public final static String appleIcon = "◍";
    public static Thread thread;
    public static Thread thread2;

    public static int tickspeed = 1000;
    public static void resetGame(){
        direction[0]=1;
        direction[1]=0;
        x[0] = HEIGHT/2;
        y[0] = HEIGHT/2;
        for (int i = 1; i<bodyParts;i++){
            x[i]=x[i-1]-1;
            y[i] = y[0];
        }
        placeApple();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    x[bodyParts] = x[bodyParts-1];
                    y[bodyParts] = y[bodyParts-1];
                    move();
                    bodyParts++;
                    checkColllision();
                    draw();
                    try{
                        Thread.sleep(tickspeed);
                    }catch (Exception e){}
                }
            }
        });

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);
                for(;;){
                    String a = sc.next();
                    try{
                        if(a.equals("w")&&direction[1]!=-1){
                            direction[0]=0;
                            direction[1]=1;
                        }
                        if(a.equals("d")&&direction[0]!=-1){
                            direction[0]=1;
                            direction[1]=0;
                        }
                        if(a.equals("a")&&direction[0]!=1){
                            direction[0]=-1;
                            direction[1]=0;
                        }
                        if(a.equals("s")&&direction[1]!=1){
                            direction[0]=0;
                            direction[1]=-1;
                        }
                    }catch (Exception e){}

                }

            }

        });
        thread2.start();
        thread.start();
    }
    public static void move(){
        for(int i = bodyParts; i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        x[0] += direction[0];
        y[0] -= direction[1];
    }

    public static void checkColllision(){
        if(x[0]>=WIDTH||x[0]<0 || y[0]>=HEIGHT||y[0]<0){
            System.out.println("STOP");
            thread.stop();
        } else if (x[0] == appleX&&y[0]==appleY){

            placeApple();
            bodyParts++;
        }
        bodyParts--;
        for(int i = 1; i < bodyParts; i++){
            if(x[0] == x[i]&&y[0]==y[i]){
                System.out.println("STOP");
                thread.stop();
            }
        }
    }
    public static void placeApple(){
       appleX = (int)Math.floor(Math.random()*HEIGHT);
       appleY = (int)Math.floor(Math.random()*HEIGHT);
       for(int k = 0;k<bodyParts;k++){
            if(x[k] == appleX && y[k] == appleY){
                placeApple();
                break;
           }
        }
    }
    public static void draw(){
        System.out.print("\n\n\n\n\n");
        for(int i = 0; i < HEIGHT;i++){
            for(int j = 0; j < WIDTH;j++){
                String toPrint = "◯";
                if(j==appleX&&i==appleY) toPrint = appleIcon;
                for(int k = 0;k<bodyParts;k++){
                    if(x[k] == j && y[k] == i){
                        toPrint = bodyIcon;
                        if(k==0){
                            toPrint = headIcon;
                        }
                    }
                }
                System.out.print(toPrint + "  ");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args){
        resetGame();
    }
}