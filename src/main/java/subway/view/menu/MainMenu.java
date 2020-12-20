package subway.view.menu;

import subway.domain.exception.InvalidMenuSelectException;
import subway.view.InputView;
import subway.view.OutputView;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
    XXX :: Menu 위치에 대한 고민
    Menu의 패키지 위치는 어디가 좋을까
    1. View : 뷰라고 하기엔 선택 다음 동작으로 비즈니스 로직을 호출해야하는게 마음에 안든다.
    2. Controller : 컨트롤러로 할까도 고민했지만, 이 클래스의 주 의미가 메뉴 출력과 메뉴 입력인데,
                컨트롤러로 두는 의미가 있을지 고민했다.
*/

public class MainMenu {
    private final Scanner scanner;
    private final SearchPathMenu searchPathMenu;
    private boolean isAppEnd = false;

    private static MainMenu mainMenu = null;

    private MainMenu(Scanner scanner) {
        this.scanner = scanner;
        searchPathMenu = SearchPathMenu.getInstance(scanner);
    }

    public static MainMenu getInstance(Scanner scanner) {
        if (mainMenu == null) {
            mainMenu = new MainMenu(scanner);
        }
        return mainMenu;
    }

    public void run() {
        do {
            printMenu();
            doSelectedMenu(selectMenu());
        } while (!isAppEnd);
    }

    private void printMenu() {
        List<String> menuNames = Arrays.stream(MAIN_MENU.values())
                .map(menu -> menu.menuName)
                .collect(Collectors.toList());
        OutputView.printMainMenu(menuNames);
    }

    private MAIN_MENU selectMenu() {
        try {
            return MAIN_MENU.of(InputView.selectMenu(scanner));
        } catch (Exception e) {
            OutputView.printError(e);
            return selectMenu();
        }
    }

    private void doSelectedMenu(MAIN_MENU selected) {
        if (selected == MAIN_MENU.SEARCH_PATH) {
            searchPathMenu.run();
        }

        if (selected == MAIN_MENU.EXIT) {
            setAppEnd();
        }
    }

    private void setAppEnd() {
        isAppEnd = true;
    }

    private enum MAIN_MENU {
        SEARCH_PATH("1", "1. 경로 조회"),
        EXIT("Q", "Q. 종료");

        private String code;
        private String menuName;

        MAIN_MENU(String code, String menuName) {
            this.code = code;
            this.menuName = menuName;
        }

        public static MAIN_MENU of(String userInput) {
            return Arrays.stream(values())
                    .filter(menu -> menu.code.equals(userInput))
                    .findFirst()
                    .orElseThrow(() -> new InvalidMenuSelectException());
        }
    }
}
