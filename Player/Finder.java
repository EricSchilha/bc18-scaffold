import bc.Direction;
import bc.MapLocation;

import java.util.ArrayList;
import java.util.Collections;

public class Finder {
    private Cell start, end;
    private Cell[][] Grid;
    private int Visited[][];

    private ArrayList<Cell> OpenList = new ArrayList<Cell>();
    private ArrayList<Cell> Path = new ArrayList<Cell>();
    boolean bPathFound = false;
    private Direction[] directions;

    public Finder(Cell start, Cell end, Cell[][] Grid) {
        this.start = start;
        this.end = end;
        this.Grid = Grid;

        directions = Direction.values();

        Visited = new int[Grid[0].length][Grid[1].length];
        for (int y = 0; y < Visited[0].length; y++) {
            for (int x = 0; x < Visited[1].length; x++) {
                this.Visited[y][x] = 0;
            }
        }
        calcHueristics();
        OpenList.add(start);
        end.isTarget = true;
        end.setValue("EE");
        start.setValue("SS");

        Visited[start.getLocation().getY()][start.getLocation().getX()] = 2;
        Visited[end.getLocation().getY()][end.getLocation().getX()] = 3;
    }

    public void calcHueristics() {
        // distance from end
        int vertical = 0, horizontal = 0;

        for (int y = 0; y < Grid[0].length; y++) {
            for (int x = 0; x < Grid[1].length; x++) {
                Cell c = Grid[y][x];

                if (!c.isPassable()) {
                    continue;
                }

                vertical = Math.abs(c.getLocation().getY() - end.getLocation().getY());
                horizontal = Math.abs(c.getLocation().getX() - end.getLocation().getX());

                c.nH = (vertical + horizontal);
                c.nF = c.nH + c.nG;

//                System.out.println("x: " + c.getLocation().getX() + ", y: " + c.getLocation().getY() + ", nF: " + c.nF);

                if (c != end && c != start) {
                    c.setValue(Integer.toString(c.nH));
//                    System.out.println(c.nF);
                }
            }
        }
    }

    public void findPath() {
        while (OpenList.size() != 0 && !bPathFound) {
            Collections.sort(OpenList);

//            System.out.println("OpenList:");
//            for (int x = 0; x < OpenList.size(); x++) {
//                Cell c = OpenList.get(x);
//                System.out.println("x: " + c.getLocation().getX() + ", y: " + c.getLocation().getY() + ", nF: " + c.nF);
//            }
//            System.out.println();

            Cell cuurent = OpenList.get(0);
            Visited[cuurent.getLocation().getY()][cuurent.getLocation().getX()] = 1;
            OpenList.remove(cuurent);
            int tempG;
            int nX, nY;

            for (int i = 0; i < 8; i++) {

                try {
                    MapLocation neighbourDir = cuurent.getLocation().add(directions[i]);
                    nX = neighbourDir.getX();
                    nY = neighbourDir.getY();
                    Cell n = Grid[nY][nX];

                    if (n == end) {
                        Path.add(cuurent);
                        bPathFound = true;
                        break;
                    }

                    if (n.isPassable()) {
                        if (Visited[nY][nX] == 1) {
                            continue;
                        }

                        if (!OpenList.contains(n)) {
                            n.setParentCell(cuurent);
                            OpenList.add(n);
                        }

                        // the cost of moving to this neighbour
                        tempG = cuurent.nG + 1;

                        // if the cost is not lower than before
                        if (tempG > cuurent.nG) {
                            continue;
                        }
                        n.setParentCell(cuurent);
                        n.nG = tempG;
                        n.nF = n.nH + n.nG;

                    }
                } catch (Exception e) {
                    System.out.println("Error occurred: " + e);
                }
            }

//            for (int y = 0; y < Visited[0].length; y++) {
//                for (int x = 0; x < Visited[1].length; x++) {
//                    System.out.print(Visited[y][x] + " ");
//                }
//                System.out.println(", ");
//            }
//            System.out.println();
//
//            System.out.println("Map:");
//
//
//            for (int y = 0; y < Grid[0].length; y++) {
//                for (int x = 0; x < Grid[1].length; x++) {
//                    if (Grid[y][x].getValue().length() == 1) {
//                        System.out.print("0");
//                    }
//                    System.out.print(Grid[y][x].getValue() + " ");
//                }
//                System.out.println(",");
//            }
//            System.out.println();
        }

    }

    public void reconstructPath() {
        Cell current = Path.get(Path.size() - 1);

        int cx, cy;

        while (current != start) {
            cx = current.getLocation().getX();
            cy = current.getLocation().getY();
            current.setValue("++");
            current = current.getParentCell();
            Path.add(current);
        }
    }

    public MapLocation nextMove(){
        return Path.get(0).getLocation();
    }
}