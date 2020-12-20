package subway.controller;

import subway.domain.*;
import subway.view.InputView;
import subway.view.OutputView;

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
        List<Station> shortest = ShortestPath.getShortestPathByDistance(getDeparture(), getArrival());
        printResult(shortest);
    }

    public void searchByTime() {
        List<Station> shortest = ShortestPath.getShortestPathByTime(getDeparture(), getArrival());
        printResult(shortest);
    }

    private void printResult(List<Station> path) {
        int takenDistance = ShortestPath.getDistanceWeight(path);
        int takenTime = ShortestPath.getTakenTimeWeight(path);
        OutputView.printSearchResult(path, takenDistance, takenTime);
    }

    private Station getDeparture() {
        try {
            String name = InputView.getDepartureStation(scanner);
            return getStationByName(name);
        } catch (Exception e) {
            OutputView.printError(e);
            return getDeparture();
        }
    }

    private Station getArrival() {
        try {
            String name = InputView.getArrivalStation(scanner);
            return getStationByName(name);
        } catch (Exception e) {
            OutputView.printError(e);
            return getArrival();
        }
    }

    private Station getStationByName(String name) {
        return StationRepository.getStation(name);
    }
}
