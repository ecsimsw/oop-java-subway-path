package subway.view.menu;

import subway.controller.SearchController;
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

public class SearchPathMenu {
    private final Scanner scanner;
    private final SearchController searchController;

    private static SearchPathMenu searchPathMenu = null;

    private SearchPathMenu(Scanner scanner) {
        this.scanner = scanner;
        searchController = SearchController.getSearchController(scanner);
    }

    public static SearchPathMenu getInstance(Scanner scanner) {
        if (searchPathMenu == null) {
            searchPathMenu = new SearchPathMenu(scanner);
        }
        return searchPathMenu;
    }

    public void run() {
        try {
            printMenu();
            execute(selectMenu());
        } catch (Exception e) {
            OutputView.printError(e);
            run();
        }
    }

    private void printMenu() {
        List<String> menuNames = Arrays.stream(SEARCH_MENU.values())
                .map(menu -> menu.menuName)
                .collect(Collectors.toList());
        OutputView.printSearchMenu(menuNames);
    }

    private SEARCH_MENU selectMenu() {
        try {
            return SEARCH_MENU.of(InputView.selectMenu(scanner));
        } catch (Exception e) {
            OutputView.printError(e);
            return selectMenu();
        }
    }

    private void execute(SEARCH_MENU selected) {
        if (selected == SEARCH_MENU.SEARCH_BY_DISTANCE) {
            searchController.searchByDistance();
        }

        if (selected == SEARCH_MENU.SEARCH_BY_TIME) {
            searchController.searchByTime();
        }
    }

    private enum SEARCH_MENU {
        SEARCH_BY_DISTANCE("1", "1. 최단 거리 조회"),
        SEARCH_BY_TIME("2", "2. 최단 시간 조회"),
        BACK("B", "B. 돌아가기");

        private String code;
        private String menuName;

        SEARCH_MENU(String code, String menuName) {
            this.code = code;
            this.menuName = menuName;
        }

        public static SEARCH_MENU of(String userInput) {
            return Arrays.stream(values())
                    .filter(menu -> menu.code.equals(userInput))
                    .findFirst()
                    .orElseThrow(() -> new InvalidMenuSelectException());
        }
    }
}
