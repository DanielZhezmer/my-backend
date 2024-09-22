package com.example.dickrunner.service;

import com.example.dickrunner.entity.Rout;
import com.example.dickrunner.repository.RoutRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.dickrunner.model.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RoutRepository routRepository;
    public List<GeoPoint> generateCustomShapeRoute(double distance, double userLatitude, double userLongitude) {
        List<GeoPoint> route = new ArrayList<>();
        if (distance <= 0) {
            return Collections.emptyList();  // Возвращаем пустой маршрут
        }

        double scale = distance / 1000.0; // Масштабируем форму в зависимости от расстояния

        // Пример координат в виде сердца, смещенных относительно текущих координат пользователя
        for (double t = 0; t < Math.PI * 2; t += 0.5) {
            double x = scale * 16 * Math.pow(Math.sin(t), 3);
            double y = scale * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));

            // Смещаем координаты относительно местоположения пользователя
            double pointLatitude = userLatitude + (y / 111.32);  // 1 градус широты ~111.32 км
            double pointLongitude = userLongitude + (x / (111.32 * Math.cos(Math.toRadians(userLatitude))));  // Учитываем кривизну Земли для долготы

            route.add(new GeoPoint(pointLatitude, pointLongitude));
        }

        return route;
    }

    @Transactional
    public Rout saveRoute(String routeName, List<GeoPoint> coordinates) {
        Rout route = new Rout();
        route.setName(routeName);
        route.setCoordinates(convertToJSON(coordinates)); // преобразуй координаты в JSON
        return routRepository.save(route);
    }

    private String convertToJSON(List<GeoPoint> points) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(points);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при конвертации в JSON", e);
        }
    }

}
