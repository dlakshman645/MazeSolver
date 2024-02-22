import java.util.*;

public class MazeSolver {
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char WALL = '▓';
    private static final char OPEN = '◌';
    private static final char PATH = '◍';

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the size of the maze (n): ");
            int n = scanner.nextInt();

            char[][] maze = generateMaze(n);
            printMazeWithColor(maze);

            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Print the path");
                System.out.println("2. Generate another puzzle");
                System.out.println("3. Exit the game");
                System.out.println("4.Print the path using another algorithm :");
                System.out.print("Choose an option (1/2/3/4): ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        List<int[]> path = bfsPath(maze, n);
                        if (path != null) {
                            markPath(maze, path);
                            System.out.println("\nPath:");
                            printMazeWithColor(maze);
                        } else {
                            System.out.println("\nNo path exists!");
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        scanner.close();
                        return;
                    case 4:
                       
                        List<int[]> p = dfsPath(maze, n);
                        if (p != null) {
                            markPath(maze, p);
                            System.out.println("\nPath:");
                            printMazeWithColor(maze);
                        } else {
                            System.out.println("\nNo path exists!");
                        }
                        break;
                          
                    default:
                        System.out.println("Invalid option! Please choose again.");
                }

                if (choice == 2) {
                    break;
                }
            }
        }
    }

    private static char[][] generateMaze(int n) {
        char[][] maze = new char[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(maze[i], OPEN);
        }

        Random random = new Random();
        maze[0][0] = START;
        maze[n - 1][n - 1] = END;

        int wallCount = n * n / 4;
        while (wallCount > 0) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            if (maze[x][y] == OPEN && (x != 0 || y != 0) && (x != n - 1 || y != n - 1)) {
                maze[x][y] = WALL;
                wallCount--;
            }
        }

        return maze;
    }

   private static void printMaze(char[][] maze) {
       for (char[] row : maze) {
            for (char cell : row) {
                switch (cell) {
                    case START:
                        System.out.print(ANSI_GREEN + cell + ANSI_RESET);
                        break;
                    case END:
                        System.out.print(ANSI_GREEN + cell + ANSI_RESET);
                        break;
                    case WALL:
                        System.out.print(ANSI_RED + cell + ANSI_RESET);
                        break;
                    case PATH:
                        System.out.print(ANSI_GREEN + cell + ANSI_RESET);
                        break;
                    case OPEN:
                        System.out.print(ANSI_BLUE + cell + ANSI_RESET);
                        break;
                    default:
                        System.out.print(cell);
                }
            }
            System.out.println();
        }
   }
private static List<int[]> dfsPath(char[][] maze, int n) {
    boolean[][] visited = new boolean[n][n];
    List<int[]> path = new ArrayList<>();
    boolean found = dfs(maze, 0, 0, n, visited, path);
    return found ? path : null;
}

private static boolean dfs(char[][] maze, int x, int y, int n, boolean[][] visited, List<int[]> path) {
    if (x < 0 || x >= n || y < 0 || y >= n || maze[x][y] == WALL || visited[x][y]) {
        return false;
    }

    path.add(new int[]{x, y});
    visited[x][y] = true;

    if (maze[x][y] == END) {
        return true;
    }

    if (dfs(maze, x + 1, y, n, visited, path) ||
        dfs(maze, x - 1, y, n, visited, path) ||
        dfs(maze, x, y + 1, n, visited, path) ||
        dfs(maze, x, y - 1, n, visited, path)) {
        return true;
    }

    path.remove(path.size() - 1);
    return false;
}
private static void markPath(char[][] maze, List<int[]> path) {
    for (int i = 0; i < maze.length; i++) {
        for (int j = 0; j < maze[i].length; j++) {
            if (maze[i][j] == PATH) {
                maze[i][j] = OPEN;
            }
        }
    }

    
    for (int[] point : path) {
        int x = point[0], y = point[1];
        if (maze[x][y] != START && maze[x][y] != END) {
            maze[x][y] = PATH;
        }
    }
}

    private static List<int[]> bfsPath(char[][] maze, int n) {
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, int[]> parent = new HashMap<>();

        queue.add(new int[]{0, 0});
        visited.add("0,0");

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1];

            if (maze[x][y] == END) {
                List<int[]> path = new ArrayList<>();
                while (x != 0 || y != 0) {
                    path.add(new int[]{x, y});
                    String key = x + "," + y;
                    int[] prev = parent.get(key);
                    x = prev[0];
                    y = prev[1];
                }
                path.add(new int[]{0, 0});
                Collections.reverse(path);
                return path;
            }

            int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : dirs) {
                int nx = x + dir[0], ny = y + dir[1];
                String key = nx + "," + ny;

                if (nx >= 0 && nx < n && ny >= 0 && ny < n && maze[nx][ny] != WALL && !visited.contains(key)) {
                    visited.add(key);
                    parent.put(key, new int[]{x, y});
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        return null;
    }

  
    private static void printMazeWithColor(char[][] maze) {
   
    for (int j = 0; j < maze.length; j++) {
        System.out.print("---+");
    }
    System.out.println();

    for (int i = 0; i < maze.length; i++) {
        System.out.print("|"); 

        for (int j = 0; j < maze[i].length; j++) {
            switch (maze[i][j]) {
                case START:
                    System.out.print(ANSI_GREEN + " " + START + " " + ANSI_RESET);
                    break;
                case END:
                    System.out.print(ANSI_GREEN + " " + END + " " + ANSI_RESET);
                    break;
                case WALL:
                    System.out.print(ANSI_RED + " " + WALL + " " + ANSI_RESET);
                    break;
                case PATH:
                    System.out.print(ANSI_GREEN + " " + PATH + " " + ANSI_RESET);
                    break;
                case OPEN:
                    System.out.print(ANSI_BLUE + " " + OPEN + " " + ANSI_RESET);
                    break;
                default:
                    System.out.print("   "); 
            }

            if (j < maze[i].length - 1) {
                System.out.print("|"); 
            }
        }

        System.out.println();
        
        if (i < maze.length - 1) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print("---+"); 
            }
            System.out.println();
        }
    }

 
    for (int j = 0; j < maze.length; j++) {
        System.out.print("---+");
    }
    System.out.println();
}

}
