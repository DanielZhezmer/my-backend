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
    public List<GeoPoint> generateHeartShapeRoute(double distance, double userLatitude, double userLongitude) {
        List<GeoPoint> route = new ArrayList<>();
        if (distance <= 0) {
            return Collections.emptyList();  // Возвращаем пустой маршрут
        }

        double scale = distance / 1000.0; // Масштабируем форму в зависимости от расстояния

        // Создаем координаты для нижних кругов
        for (double t = Math.PI; t <= 2 * Math.PI; t += 0.1) {
            double x = scale * Math.cos(t);
            double y = scale * Math.sin(t);
            route.add(new GeoPoint(userLatitude + (y / 111.32), userLongitude + (x / (111.32 * Math.cos(Math.toRadians(userLatitude))))));
        }

        // Создаем координаты для средней части
        for (double t = 0; t <= 1; t += 0.1) {
            double x = 0; // Прямая линия по x
            double y = -scale * 2 * t; // Прямая линия по y
            route.add(new GeoPoint(userLatitude + (y / 111.32), userLongitude + (x / (111.32 * Math.cos(Math.toRadians(userLatitude))))));
        }

        // Создаем координаты для верхней округлой части
        for (double t = 0; t <= Math.PI; t += 0.1) {
            double x = scale * Math.cos(t);
            double y = -scale * 2 + scale * Math.sin(t);
            route.add(new GeoPoint(userLatitude + (y / 111.32), userLongitude + (x / (111.32 * Math.cos(Math.toRadians(userLatitude))))));
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
