package minefield;

import java.util.Scanner;

public class MineSweeper {

    private int rowCount;
    private int columnCount;
    private boolean gameOver;
    private int[][] visibleGameArea;
    private int[][] actualGameArea;

    // Dışarıdan satır ve sütunun kaç tane olacağı gelecek.
    public MineSweeper(int rowCount, int columnCount) {
        this.gameOver = false;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        // Satır ve sütun sayısı kadarlık 2D array başlat.
        // Default her alan 0 olarak başlar (çünkü int array)
        visibleGameArea = new int[rowCount][columnCount];
    }

    // Kaç mayın olacağını hesapla
    public int calculateMineCount() {
        //(satır * sütun) / 4
        return (this.rowCount * this.columnCount) / 4;
    }

    // Mayınları yerleştir
    public void placeMines() {
        //0 1 2 --> 3 defa dönecektir.
        for (int i = 0; i < calculateMineCount(); i++) {

            int randomRowIndex = (int) (Math.random() * this.rowCount);
            int randomColumnIndex = (int) (Math.random() * this.columnCount);
            if (visibleGameArea[randomRowIndex][randomColumnIndex] != -1) {
                visibleGameArea[randomRowIndex][randomColumnIndex] = -1;
            } else {
                // burdaki amaç eğer bu noktada mayın varsa loop devam etmesin, aynı i değeri için tekrar denesin
                i--;
            }
        }
    }

    //Mayına temas eden sağ sol alt üst ve çapraz değerleri her döngüde +1 arttırma
    public void updateContactPoints() {
        int[][] numberedArea = new int[this.rowCount][this.columnCount];
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                // oyun alanında gezerken -1 görürsem
                if (visibleGameArea[row][column] == -1) {
                    numberedArea[row][column] = -1; // Mayın bulunuyosa -1 den devam et
                } else {
                    int count = 0;
                    // Komşuların kontrolünü sağlama
                    for (int innerRow = row - 1; innerRow <= row + 1; innerRow++) {
                        for (int innerColumn = column - 1; innerColumn <= column + 1; innerColumn++) {
                            // Etrafını kontrol ederken kendi üzerime geldiysem bunu geç.
                            if (!(innerRow == row && innerColumn == column) &&
                                    // Satır indexi minimum 0 olabilir. bu yüzden innerRow >= 0 olmalı çünkü eksi index diye bir şey yok. İndex max length - 1 olabilir. Bu yüzden innerRow < this.rowCount
                                    innerRow >= 0 && innerRow < this.rowCount
                                    // sütun indexi minimum 0 olabilir. bu yüzden innerColumn >= 0 olmalı çünkü eksi index diye bir şey yok. İndex max length - 1 olabilir. Bu yüzden innerColumn < this.columnCount
                                    && innerColumn >= 0 && innerColumn < this.columnCount
                                    // Tüm şartları sağladık, bir noktanın etrafında geziyoruz. Gezerken -1 (mayın) gördükçe count artırarak kaç mayın gördüğünü say.
                                    && visibleGameArea[innerRow][innerColumn] == -1) {
                                // gördüğü mayın sayısı
                                count++;
                            }
                        }
                    }
                    numberedArea[row][column] = count; // Etrafında gezdiğim bu row bu column noktası için kaç mayın gördüysem, bu alana o sayıyı yaz.
                }
            }
        }
        this.actualGameArea = numberedArea;
    }

    // mayın tarlasının durumu bastıran
    public void printVisibleGameArea() {
        for (int row = 0; row < this.visibleGameArea.length; row++) {
            for (int column = 0; column < this.visibleGameArea[0].length; column++) {
                if (this.visibleGameArea[row][column] > 0) {
                    System.out.print(" " + (this.visibleGameArea[row][column] - 1) + " ");
                } else {
                    System.out.print(" - ");
                }
            }
            System.out.println();
        }
    }

    public void printActualGameArea() {
        for (int row = 0; row < this.actualGameArea.length; row++) {
            for (int column = 0; column < this.actualGameArea[0].length; column++) {
                if (this.actualGameArea[row][column] == -1) {
                    System.out.print(" * ");
                } else {
                    System.out.print(" " + this.actualGameArea[row][column] + " ");
                }
            }
            System.out.println();
        }
    }

    public void play(int rowIndex, int columnIndex) {
        // burda mayın olmadığından eminim.
        if (isCoordinateChosen(rowIndex, columnIndex)) {
            System.out.println("You repeated yourself!!!" + " Try again.");
            return;
        }
        this.visibleGameArea[rowIndex][columnIndex] = this.actualGameArea[rowIndex][columnIndex] + 1;
    }

    private boolean isCoordinateChosen(int rowIndex, int columnIndex) {
        if (visibleGameArea[rowIndex][columnIndex] != 0) {
            return true;
        }
        return false;
    }

    public boolean isPlayerWon() {
        // visible içinde hiç 0 olan yoksa her yeri açmış (mayınlar hariç)
        for (int row = 0; row < this.visibleGameArea.length; row++) {
            for (int column = 0; column < this.visibleGameArea[0].length; column++) {
                if (this.visibleGameArea[row][column] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //Başka bir sınıf olması gerektiğini düşündüğüm fakat 2 class istenmesinden ve main classımın temiz görünmesini istediğim için buraya aktardığım print ve başlatma methodlarının başladığı yer
    private Scanner scanner;

    public MineSweeper() {
        scanner = new Scanner(System.in);
    }

    public void startGame() {
        int rowCount, columnCount;
        //Negatif sayı girilebilmesini engellemek
        do {
            System.out.print("Enter the number of rows\t: ");
            rowCount = scanner.nextInt();
            if (rowCount < 2) {
                System.out.println("Please enter a value of 2 or greater.");
            }
        } while (rowCount < 2);
        do {
            System.out.print("Enter the number of columns : ");
            columnCount = scanner.nextInt();
            if (columnCount < 2) {
                System.out.println("Please enter a value of 2 or greater.");
            }
        } while (columnCount < 2);

        // Oyunu başlat
        MineSweeper minefield = new MineSweeper(rowCount, columnCount);
        // Mayınları yerleştir
        minefield.placeMines();
        // Mayınların etrafındaki sayıları yerleştir
        minefield.updateContactPoints();

        ////////////////////////////////////
        //kopya çekmek için aktif edilebilir
        //minefield.printActualGameArea();
        //System.out.println();
        ////////////////////////////////////

        while (!minefield.getGameOver()) {
            minefield.printVisibleGameArea();
            System.out.println("--------------------------");
            System.out.print("Row index to play\t : ");
            int rowIndex = scanner.nextInt();
            System.out.print("Column index to play : ");
            int columnIndex = scanner.nextInt();
            System.out.println("--------------------------");
            // oyun alanı indexlerinin dışında bir nokta seçildi
            if (rowIndex < 0 || columnIndex < 0 || rowIndex > minefield.getActualGameArea().length - 1 || columnIndex > minefield.getActualGameArea()[0].length - 1) {
                System.out.println("Invalid coordinates!!!");
                continue;
            }
            // bu seçilen noktada mayın var mı?
            if (minefield.getActualGameArea()[rowIndex][columnIndex] == -1) {
                minefield.setGameOver(true);
                System.out.println("Boom!!!" + " Your Life is Over.");
                System.out.println();
                // mayına bastıysa gerçek oyun alanını göster
                minefield.printActualGameArea();
            } else {
                minefield.play(rowIndex, columnIndex);
                if (minefield.isPlayerWon()) {
                    System.out.println("Congratulations" + " You Are Alive.");
                    System.out.println();
                    minefield.setGameOver(true);
                    minefield.printActualGameArea();
                }
            }
        }
    }

    public int[][] getActualGameArea() {
        return this.actualGameArea;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }
}