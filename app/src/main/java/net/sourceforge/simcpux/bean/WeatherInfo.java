package net.sourceforge.simcpux.bean;

import java.util.ArrayList;

/**
 * Created by yanghu on 2018/4/18.
 */

public class WeatherInfo {
    public ArrayList<HeWeather> HeWeather;

    public class HeWeather{
        public String status;
        public Basic basic;
        public Now now;
        public ArrayList<ForeCast> daily_forecast;
        public Aqi aqi;
        public Suggestion suggestion;

        public class Suggestion{
            public Info comf;
            public Info sport;
            public Info cw;

            public class Info{
                public String txt;
            }
        }

        public class Aqi{
            public City city;
            public class City{
                public String aqi;
                public String pm25;
            }
        }

        public class ForeCast{
            public String date;
            public Cond cond;
            public Tmp tmp;

            public class Tmp{
                public String max;
                public String min;
            }
            public class Cond{
                public String txt_d;
            }
        }

        public class Now{
            public String tmp;
            public Cond cond;
            public class Cond{
                public String txt;
            }
        }

        public class Basic{
            public String city;
            public Update update;

            public class Update{
                public String loc;
            }
        }
    }
}
