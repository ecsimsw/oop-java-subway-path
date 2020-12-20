package subway.controller;

import subway.domain.*;
import subway.view.InputView;
import subway.view.OutputView;

import java.awt.desktop.OpenURIEvent;
import java.util.List;
import java.util.Scanner;

public class SearchController {
    private static SearchController searchController = null;
    private final Scanner scanner;

    private SearchController(Scanner scanner) {
        this.scanner = scanner;
    }

    public static SearchController getSearchController(Scanner scanner) {
        if (searchController == null) {
            searchController = new SearchController(scanner);
        }
        return searchController;
    }

    public void searchByDistance() {
        try {
            List shortest = ShortestPath.getShortestPathByDistance(getDeparture(), getArrival());
            printResult(shortest);
        } catch (Exception e) {
            OutputView.printError(e);
            searchByDistance();
        }
    }

    public void searchByTime() {
        try {
            List shortest = ShortestPath.getShortestPathByTime(getDeparture(), getArrival());
            printResult(shortest);
        } catch (Exception e) {
            OutputView.printError(e);
            searchByTime();
        }
    }

    private void printResult(List<Station> path) {
        int distance = ShortestPath.getDistanceWeight(path);
        int takenTime = ShortestPath.getTakenTimeWeight(path);
        OutputView.printSearchResult(path, distance, takenTime);
    }

    private Station getDeparture() {
        String name = InputView.getDepartureStation(scanner);
        return StationRepository.getStation(name);
    }

    private Station getArrival() {
        String name = InputView.getArrivalStation(scanner);
        return StationRepository.getStation(name);
    }
}
