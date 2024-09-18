package it.project.weather.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ForecastDto {

    @Setter
    private RegionDto location;

    private Current current;

    @Getter
    public static class Current {

        private double temp_c;

        private Condition condition;

        @Getter
        public static class Condition {

            private String text;
        }
    }
}
