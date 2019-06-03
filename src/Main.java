import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    static int mapHigh = 50;
    static int mapWidth = 50;
    static int sleepTime = 1;


    static Object[][] map = new Object[mapHigh][mapWidth];
    static Object[][] workMap = new Object[mapHigh][mapWidth];
    static boolean[][] visitedPoints = new boolean[mapHigh][mapWidth];
    static int startX;
    static int startY;
    static int endX;
    static int endY;
    static boolean endReach = false;
    static boolean startReach = false;
    static List<String> revTraceList = new ArrayList<>();
    static List<String> traceList = new ArrayList<>();
    static JFrame frame;
    static JPanel[][] panelArray = new JPanel[mapHigh][mapWidth];
    static List<Integer> xQueque = new ArrayList<>();
    static List<Integer> yQueque = new ArrayList<>();
    static int quequeSize = 0;
    static int index = 0;

    public static void main(String[] args) {
        generateMap();
        //map = getTestMatrix3();
        showGui(map);
        System.out.println();
        System.out.println("Start x="+(startX+1)+" ; y="+(startY+1));
        System.out.println();

        workMap = addTime(map);

        System.out.println();

        estimateTimeAndFindEnd(workMap,startX,startY);

        for(int index=0; index<quequeSize ; index++){
            estimateTimeAndFindEnd(workMap,xQueque.get(index),yQueque.get(index));
        }


        if(endReach){
            trace(workMap,endX,endY);
            System.out.println();
            System.out.println(traceList);
        }
        else{
            System.out.println();
            System.out.println("Nie istnieje ścieżka łącząca start i koniec");
        }
    }

    public static void generateMap(){
        Random randomGenerator = new Random();
        for(int i=0 ; i<map.length ; i++){
            for(int j=0 ; j<map[i].length ; j++){
                int x = randomGenerator.nextInt(10);
                if(x>2){
                    map[i][j]=1;
                }
                else{
                    map[i][j]=0;
                }
            }
        }

        generateStart(randomGenerator);
        generateEnd(randomGenerator);
    }

    public static void generateStart(Random randomGenerator){
        startX = randomGenerator.nextInt(mapWidth);
        startY = randomGenerator.nextInt(mapHigh);

        map[startY][startX] = "S";
    }

    public static void generateEnd(Random randomGenerator){
        int endX = randomGenerator.nextInt(mapWidth);
        int endY = randomGenerator.nextInt(mapHigh);

        if(map[endY][endX].equals("S")){
            generateEnd(randomGenerator);
        }
        else{
            map[endY][endX] = "E";
        }
    }

    public static void showMap(Object[][] map){
        for(int i=0 ; i<map.length ; i++){
            for(int j=0 ; j<map[i].length ; j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static Object[][] addTime(Object[][] map){
        Object[][] workMap = new Object[mapHigh][mapWidth];

        for(int i=0 ; i<map.length ; i++){
            for(int j=0 ; j<map[i].length ; j++){
                if(map[i][j].equals("S")){
                    workMap[i][j] = map[i][j] + "(0)";
                }
                else if(map[i][j].equals(1)){
                    workMap[i][j] = map[i][j] + "(x)";
                }
                else{
                    workMap[i][j] = map[i][j] + "   ";
                }
            }
        }

        return workMap;
    }

    public static void estimateTimeAndFindEnd(Object[][] map, int startX, int startY){
        int x = startX;
        int y = startY;
        visitedPoints[y][x] = true;

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        if(x+1>=0 && x+1<mapWidth && y>=0 && y<mapHigh){
            String point = (String)map[y][x+1];
            if(point.startsWith("1") && visitedPoints[y][x+1] == false) {
                setTime(map, x + 1, y, (findFastestNeighbour(map,x+1,y)+1));
                panelArray[y][x+1].setBackground(Color.GREEN);
                if(endReach == false){
                    xQueque.add(x+1);
                    yQueque.add(y);
                    quequeSize++;
                }
            }
            else if(map[y][x+1].equals("E   ")){
                endReach = true;
                map[y][x+1] = "E("+ (findFastestNeighbour(map,x+1,y)+1) +")";
                endX=x+1;
                endY=y;
                index = quequeSize;
            }
        }
        if(x-1>=0 && x-1<mapWidth && y>=0 && y<mapHigh){
            String point = (String)map[y][x-1];
            if(map[y][x-1].equals("1(x)")) {
                setTime(map, x - 1, y, (findFastestNeighbour(map,x-1,y)+1));
                panelArray[y][x-1].setBackground(Color.GREEN);
                if(endReach == false) {
                    xQueque.add(x-1);
                    yQueque.add(y);
                    quequeSize++;
                }
            }
            else if(map[y][x-1].equals("E   ")){
                endReach = true;
                map[y][x-1] = "E("+ (findFastestNeighbour(map,x-1,y)+1) +")";
                endX=x-1;
                endY=y;
                index = quequeSize;
            }
        }
        if(x>=0 && x<mapWidth && y+1>=0 && y+1<mapHigh){
            String point = (String)map[y+1][x];
            if(map[y+1][x].equals("1(x)")) {
                setTime(map, x, y + 1, (findFastestNeighbour(map,x,y+1)+1));
                panelArray[y+1][x].setBackground(Color.GREEN);
                if(endReach == false) {
                    xQueque.add(x);
                    yQueque.add(y+1);
                    quequeSize++;
                }
            }
            else if(map[y+1][x].equals("E   ")){
                endReach = true;
                map[y+1][x] = "E("+ (findFastestNeighbour(map,x,y+1)+1) +")";
                endX=x;
                endY=y+1;
                index = quequeSize;
            }
        }

        if(x>=0 && x<mapWidth && y-1>=0 && y-1<mapHigh){
            String point = (String)map[y-1][x];
            if(map[y-1][x].equals("1(x)")) {
                setTime(map, x, y - 1, (findFastestNeighbour(map,x,y-1)+1));
                panelArray[y-1][x].setBackground(Color.GREEN);
                if(endReach == false) {
                    xQueque.add(x);
                    yQueque.add(y-1);
                    quequeSize++;
                }
            }
            else if(map[y-1][x].equals("E   ")){
                endReach = true;
                map[y-1][x] = "E("+ (findFastestNeighbour(map,x,y-1)+1) +")";
                endX=x;
                endY=y-1;
                index = quequeSize;
            }
        }
    }

    public static int findFastestNeighbour(Object[][] map, int x, int y){
        int fastestX = -1;
        int fastestY = -1;

        if(x>=0 && x<mapWidth && y-1>=0 && y-1<mapHigh) {
            String up = (String) map[y - 1][x];
            if(!up.equals("1(x)") && !up.equals("0   ")) {
                if (up.startsWith("1") || up.startsWith("S")) {
                    if (fastestX < 0) {
                        fastestX = x;
                        fastestY = y - 1;
                    } else {
                        if (getTime(map, fastestX, fastestY) > getTime(map, x, y - 1)) {
                            fastestX = x;
                            fastestY = y - 1;
                        }
                    }
                }
            }
        }
        if(x+1>=0 && x+1<mapWidth && y>=0 && y<mapHigh){
            String right = (String)map[y][x+1];
            if(!right.equals("1(x)") && !right.equals("0   ")){
                if(right.startsWith("1") || right.startsWith("S")){
                    if(fastestX<0){
                        fastestX = x+1;
                        fastestY = y;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x+1,y)){
                            fastestX = x+1;
                            fastestY = y;
                        }
                    }
                }
            }
        }

        if(x>=0 && x<mapWidth && y+1>=0 && y+1<mapHigh){
            String down = (String)map[y+1][x];
            if(!down.equals("1(x)") && !down.equals("0   ")){
                if(down.startsWith("1") || down.startsWith("S")){
                    if(fastestX<0){
                        fastestX = x;
                        fastestY = y+1;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x,y+1)){
                            fastestX = x;
                            fastestY = y+1;
                        }
                    }
                }
            }
        }

        if(x-1>=0 && x-1<mapWidth && y>=0 && y<mapHigh){
            String left = (String)map[y][x-1];
            if(!left.equals("1(x)") && !left.equals("0   ")){
                if(left.startsWith("1") || left.startsWith("S")){
                    if(fastestX<0){
                        fastestX = x-1;
                        fastestY = y;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x-1,y)){
                            fastestX = x-1;
                            fastestY = y;
                        }
                    }
                }
            }

        }


        return getTime(map,fastestX,fastestY);
    }

    public static void trace(Object[][] map, int x, int y){
        String endCord = "(" + (y+1) + "," + (x+1) + ")";
        revTraceList.add(endCord);
        int fastestX = -1;
        int fastestY = -1;

        if(x>=0 && x<mapWidth && y-1>=0 && y-1<mapHigh){
            String up = (String)map[y-1][x];
            if(!up.equals("1(x)")){
                if(up.startsWith("1")){
                    if(fastestX<0){
                        fastestX = x;
                        fastestY = y-1;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x,y-1)){
                            fastestX = x;
                            fastestY = y-1;
                        }
                    }
                }
                else if(up.startsWith("S")){
                    startReach = true;
                    String cord = "(" + (y) + "," + (x+1) + ")";
                    revTraceList.add(cord);
                }
            }
        }

        if(x+1>=0 && x+1<mapWidth && y>=0 && y<mapHigh){
            String right = (String)map[y][x+1];
            if(!right.equals("1(x)")){
                if(right.startsWith("1")){
                    if(fastestX<0){
                        fastestX = x+1;
                        fastestY = y;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x+1,y)){
                            fastestX = x+1;
                            fastestY = y;
                        }
                    }
                }
                else if(right.startsWith("S")){
                    startReach = true;
                    String cord = "(" + (y+1) + "," + (x+2) + ")";
                    revTraceList.add(cord);
                }
            }
        }

        if(x>=0 && x<mapWidth && y+1>=0 && y+1<mapHigh){
            String down = (String)map[y+1][x];
            if(!down.equals("1(x)")){
                if(down.startsWith("1")){
                    if(fastestX<0){
                        fastestX = x;
                        fastestY = y+1;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x,y+1)){
                            fastestX = x;
                            fastestY = y+1;
                        }
                    }
                }
                else if(down.startsWith("S")){
                    startReach = true;
                    String cord = "(" + (y+2) + "," + (x+1) + ")";
                    revTraceList.add(cord);
                }
            }
        }

        if(x-1>=0 && x-1<mapWidth && y>=0 && y<mapHigh){
            String left = (String)map[y][x-1];
            if(!left.equals("1(x)")){
                if(left.startsWith("1")){
                    if(fastestX<0){
                        fastestX = x-1;
                        fastestY = y;
                    }
                    else{
                        if(getTime(map,fastestX,fastestY)>getTime(map,x-1,y)){
                            fastestX = x-1;
                            fastestY = y;
                        }
                    }
                }
                else if(left.startsWith("S")){
                    startReach = true;
                    String cord = "(" + (y+1) + "," + (x) + ")";
                    revTraceList.add(cord);
                }
            }

        }
        if(startReach){
            traceList = reverseList(revTraceList);
        }
        else{
            panelArray[fastestY][fastestX].setBackground(Color.BLUE);
            trace(map,fastestX,fastestY);
        }
    }

    public static List<String> reverseList(List<String> list){
        List<String> reversedList = new ArrayList<>();

        for(int i=list.size()-1 ; i>=0 ; i--){
            reversedList.add(list.get(i));
        }

        return reversedList;
    }

    public static int getTime(Object[][] map,int x, int y){
        int time = -1;

        String content = (String)map[y][x];

        if(content.matches("1\\(\\d+\\)")){
            content = content.substring(2);
            content = content.substring(0, content.length() - 1);
            time = Integer.parseInt(content);
        }
        else if(content.matches("S\\(\\d+\\)")){
            content = content.substring(2);
            content = content.substring(0, content.length() - 1);
            time = Integer.parseInt(content);
        }
        return time;
    }

    public static void setTime(Object[][] map,int x,int y, int time){
            map[y][x] = "1("+time+")";
    }

    public static Object[][] getTestMatrix(){
        Object[][] map = {{1,1,1,0,1,0,1,0,1,1},
                          {1,0,0,1,1,0,0,1,0,1},
                          {1,1,"S",1,0,0,1,0,0,0},
                          {0,0,1,0,0,1,0,0,1,0},
                          {1,0,1,0,0,1,0,0,0,1},
                          {0,1,1,1,1,1,0,0,0,0},
                          {0,0,0,1,0,1,0,1,0,1},
                          {1,1,1,0,0,1,1,0,1,1},
                          {0,1,0,0,0,0,1,1,1,1},
                          {0,0,0,0,1,1,0,1,"E",1}};
        startX = 2;
        startY = 2;
        return map;
    }

    public static Object[][] getTestMatrix2(){
        Object[][] map = {{1,0,1,0,1,0,1,1,1,1},
                         {1,1,1,1,1,"E",1,1,1,1},
                         {0,1,1,1,1,1,1,0,1,1},
                         {0,0,1,0,0,0,1,1,1,1},
                         {1,0,0,1,1,1,1,1,"S",0},
                         {1,1,0,1,1,0,0,1,1,0},
                         {1,1,1,1,1,1,1,1,0,0},
                         {1,0,0,1,0,0,1,1,0,0},
                         {1,0,1,0,1,1,1,1,1,1},
                         {0,1,1,0,1,0,1,0,1,0}};
        startX = 8;
        startY = 4;
        return map;
    }

    public static Object[][] getTestMatrix3(){
        Object[][] map = {{0,0,0,0,0,0,0,0,0,0},
                         {0,0,0,0,0,0,0,0,0,0},
                         {0,0,0,0,0,"E",1,0,0,0},
                         {0,0,0,1,1,0,1,1,1,1},
                         {0,0,1,1,1,1,1,0,1,0},
                         {1,1,1,1,0,1,0,1,1,1},
                         {0,0,1,"S",1,0,1,1,0,0},
                         {0,0,0,1,1,1,1,0,1,1},
                         {0,1,1,1,1,1,1,1,1,0},
                         {0,1,1,1,0,1,0,0,1,0}};
        startX = 3;
        startY = 6;
        return map;
    }

    public static void showGui(Object[][] map){
        frame = new JFrame();
        frame.setLayout(new GridLayout(mapHigh,mapWidth));
        for(int i=0; i<mapHigh ; i++){
            for(int j=0 ; j<mapWidth ; j++){
                JPanel panel = new JPanel();
                String check = map[i][j].toString();
                if(check.equals("1")){
                    panel.setBackground(Color.WHITE);
                }
                else if(check.equals("0")){
                    panel.setBackground(Color.BLACK);
                }
                else if(check.equals("S")){
                    panel.setBackground(Color.RED);
                }
                else{
                    panel.setBackground(Color.MAGENTA);
                }
                panelArray[i][j] = panel;
                frame.add(panel);
            }
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,700);
        frame.setVisible(true);
    }
}