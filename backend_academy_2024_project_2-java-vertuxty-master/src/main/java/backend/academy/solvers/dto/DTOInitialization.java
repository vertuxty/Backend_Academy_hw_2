package backend.academy.solvers.dto;

import backend.academy.mazes.Coordinate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DTOInitialization {

    private final List<Integer> dist;
    private final List<Coordinate> prev;
    private final int n;
    private final int startValue;
    private final int startPos;

    public static class Builder {

        private List<Integer> dist;
        private List<Coordinate> prev;
        private Integer n;
        private Integer startValue;
        private Integer startPos;

        public Builder setDistance(List<Integer> dist) {
            this.dist = dist;
            return this;
        }

        public Builder setPrevious(List<Coordinate> previous) {
            this.prev = previous;
            return this;
        }

        public Builder setSize(int n) {
            this.n = n;
            return this;
        }

        public Builder setStartValue(int startValue) {
            this.startValue = startValue;
            return this;
        }

        public Builder setStartPos(int startPos) {
            this.startPos = startPos;
            return this;
        }

        public DTOInitialization build() {
            if (startValue == null
                || startPos == null
                || n == null
                || dist == null
                || prev == null) {
                throw new IllegalStateException("Expected all args to be set in DTOInitialization builder");
            }
            return new DTOInitialization(dist, prev, n, startValue, startPos);
        }
    }
}
