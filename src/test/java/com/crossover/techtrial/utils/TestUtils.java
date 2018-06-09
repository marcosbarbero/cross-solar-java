package com.crossover.techtrial.utils;

import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class TestUtils {

    private static Random random = new Random();

    public static Panel panel(String... serialId) {
        String serial = serialId.length == 0 ? UUID.randomUUID().toString().substring(0, 15) : serialId[0];

        Panel panel = new Panel();
        panel.setId(random.nextLong());
        panel.setBrand("Tesla");
        panel.setLatitude(random.nextDouble());
        panel.setLongitude(random.nextDouble());
        panel.setSerial(serial);
        return panel;
    }

    public static HourlyElectricity hourlyElectricity() {
        HourlyElectricity hourlyElectricity = new HourlyElectricity();
        hourlyElectricity.setId(random.nextLong());
        hourlyElectricity.setGeneratedElectricity(random.nextLong());
        hourlyElectricity.setPanel(panel());
        hourlyElectricity.setReadingAt(LocalDateTime.now());
        return hourlyElectricity;
    }

    public static DailyElectricity dailyElectricity() {
        return new DailyElectricity(panel(),
                random.nextLong(),
                random.nextLong(),
                random.nextLong(),
                random.nextDouble(),
                LocalDateTime.now()
        );
    }

    public static Pageable pageable() {
        return PageRequest.of(0, 5);
    }
}
