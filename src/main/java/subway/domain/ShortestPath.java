package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;

/*
  Controller에서 DijkstraShortestPath 관련 비즈니스 로직을 분리
 */


public class ShortestPath {
    private static final DijkstraShortestPath dijkstraPathByDistance;
    private static final DijkstraShortestPath dijkstraPathByTime;

    static {
        dijkstraPathByDistance = new DijkstraShortestPath(SearchGraph.getGraphByDistance());
        dijkstraPathByTime = new DijkstraShortestPath(SearchGraph.getGraphByTime());
    }

    public static List getShortestPathByDistance(Station from, Station to) {
        return getVertexPathList(SEARCH_TYPE.SEARCH_BY_DISTANCE, from, to);
    }

    public static List getShortestPathByTime(Station from, Station to) {
        return getVertexPathList(SEARCH_TYPE.SEARCH_BY_TIME, from, to);
    }

    private static List getVertexPathList(SEARCH_TYPE search_type, Station from, Station to) {
        return search_type.getShortestGraphPath(from, to).getVertexList();
    }

    public static int getDistanceWeight(List<Station> path) {
        return getSumOfWeights(path, SEARCH_TYPE.SEARCH_BY_DISTANCE);
    }

    public static int getTakenTimeWeight(List<Station> path) {
        return getSumOfWeights(path, SEARCH_TYPE.SEARCH_BY_TIME);
    }

    private static int getSumOfWeights(List<Station> path, SEARCH_TYPE searchType) {
        int sum = 0;
        for (int index = 0; index < path.size() - 1; index++) {
            Station from = path.get(index);
            Station to = path.get(index + 1);
            sum += searchType.getShortestGraphPath(from, to).getWeight();
        }
        return sum;
    }

    public enum SEARCH_TYPE {
        SEARCH_BY_DISTANCE(dijkstraPathByDistance),
        SEARCH_BY_TIME(dijkstraPathByTime);

        private DijkstraShortestPath shortestPath;

        SEARCH_TYPE(DijkstraShortestPath dijkstraPathByDistance) {
            this.shortestPath = dijkstraPathByDistance;
        }

        public GraphPath getShortestGraphPath(Station s1, Station s2) {
            return shortestPath.getPath(s1, s2);
        }
    }

}
