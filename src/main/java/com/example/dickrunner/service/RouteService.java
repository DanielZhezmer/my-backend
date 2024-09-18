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
    public List<GeoPoint> generateHeartShapeRoute(double distance) {
        List<GeoPoint> route = new ArrayList<>();
        if (distance <= 0) {
            return Collections.emptyList();  // Возвращаем пустой маршрут
        }

        double scale = distance / 1000.0; // Масштабируем форму в зависимости от расстояния

        // Пример координат в виде сердца (в реальном коде вы можете использовать сложные формулы)
        for (double t = 0; t < Math.PI * 2; t += 0.1) {
            double x = scale * 16 * Math.pow(Math.sin(t), 3);
            double y = scale * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));
            route.add(new GeoPoint(y + 50.0, x + 20.0)); // Добавляем смещение для корректного отображения
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
