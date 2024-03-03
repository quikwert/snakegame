import java.util.Scanner;

public class Main{
    public final static int HEIGHT = 20;
    public final static int[] x = new int[HEIGHT*HEIGHT];
    public final static int[] y = new int[HEIGHT*HEIGHT];
    public static int appleX;
    public static int appleY;

    public static int[] direction = new int[2];
    public static int bodyParts = 6;
    public final static String headIcon = "@";
    public static String bodyIcon;
    public final static String appleIcon = "Q";

    public static boolean gameOn = false;
    public static boolean loopOn;
    public static boolean inputBlocked = false;

    public static int tickSpeed = (1000/5);
    public static int speed = 1;
    public static Thread thread;



    public static void resetGame(){
        bodyIcon = "o";
        direction[0]=1;
        direction[1]=0;
        x[0] = HEIGHT/2;
        y[0] = HEIGHT/2;
        bodyParts = 6;
        for (int i = 1; i<bodyParts;i++){
            x[i]=x[i-1]-1;
            y[i] = y[0];
        }
        placeApple();
        for(int i = 3; i >0;i--){
            System.out.println(i);
            try{
                Thread.sleep(1000);
            }catch (Exception e){}
        }
        gameOn = true;
    }

    public static void cycleGame(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    if(gameOn){
                        try{
                            new ProcessBuilder("cmd", "/c", "cls" ).inheritIO().start().waitFor();
                        }catch (Exception e){}
                        move();
                        inputBlocked = false;
                        checkCollision();
                        draw();
                        System.out.println("l - LOOPED");
                        System.out.println("c - CLASSIC");
                        try{
                            Thread.sleep(tickSpeed);
                        }catch (Exception e){}
                    }else{
                        System.out.flush();
                    }
                }
            }
        });
        thread.start();
        controls();
    }
    public static void controls(){
        Scanner sc = new Scanner(System.in);
        while(true){
            String a = sc.next();
            if(a.equals("w")&&direction[1]!=-1 &&!inputBlocked){
                direction[0]=0;
                direction[1]=1;
                inputBlocked = true;
            }
            else if(a.equals("d")&&direction[0]!=-1&&!inputBlocked){
                direction[0]=1;
                direction[1]=0;
                inputBlocked = true;
            }
            else if(a.equals("a")&&direction[0]!=1&&!inputBlocked){
                direction[0]=-1;
                direction[1]=0;
                inputBlocked = true;
            }
            else if(a.equals("s")&&direction[1]!=1&&!inputBlocked){
                direction[0]=0;
                direction[1]=-1;
                inputBlocked = true;
            }
            else if(a.equals("l")){
                gameOn = false;
                loopOn = true;
                resetGame();
            }
            else if(a.equals("c")){
                gameOn = false;
                loopOn = false;
                resetGame();
            }
            else if(a.equals("--s")){
                speed = sc.nextInt();
                tickSpeed = (1000/2)/speed;
                System.out.println(speed + "   tickspeed is " + tickSpeed);
            }
        }
    }
    public static void move(){
        for(int i = bodyParts; i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(loopOn){
            x[0] = ((((x[0] + direction[0]) % HEIGHT) + HEIGHT) % HEIGHT);
            y[0] = ((((y[0] - direction[1]) % HEIGHT) + HEIGHT) % HEIGHT);
        }else {
            x[0] += direction[0];
            y[0] -= direction[1];
        }
    }

    public static void checkCollision(){
        x[bodyParts] = x[bodyParts-1];
        y[bodyParts] = y[bodyParts-1];
        if(x[0]>=HEIGHT||x[0]<0 || y[0]>=HEIGHT||y[0]<0){
            gameOn = false;
            bodyIcon = "x";
        } else if (x[0] == appleX&&y[0]==appleY){
            placeApple();
            bodyParts++;
        }

        for(int i = 1; i < bodyParts; i++){
            if(x[0] == x[i]&&y[0]==y[i]){
                gameOn = false;
                bodyIcon = "x";
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
        String image = "";
        for(int i = 0; i < HEIGHT;i++){
            for(int j = 0; j < HEIGHT;j++){
                String toPrint = ".";
                if(j==appleX&&i==appleY) toPrint = appleIcon;
                for(int k = bodyParts-1;k>=0;k--){
                    if(x[k] == j && y[k] == i){
                        toPrint = bodyIcon;
                        if(k==0){
                            toPrint = headIcon;
                        }
                    }
                }
                image += (toPrint + "  ");
            }
            image += "\n";
        }
        System.out.print(image);
    }

    public static void main(String[] args){
        System.out.println("l - LOOPED");
        System.out.println("c - CLASSIC");
        cycleGame();
    }
}