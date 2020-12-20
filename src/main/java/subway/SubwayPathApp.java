package subway;

import subway.view.menu.MainMenu;

import java.util.Scanner;

public class SubwayPathApp {
    private final Scanner scanner;

    public SubwayPathApp(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        DummyData.initialize();
        final MainMenu mainMenu = new MainMenu(scanner);
        mainMenu.run();
    }
}
