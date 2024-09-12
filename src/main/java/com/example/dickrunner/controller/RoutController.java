package com.example.dickrunner.controller;

import com.example.dickrunner.dto.GeoRouteResponse;
import lombok.RequiredArgsConstructor;
import com.example.dickrunner.model.GeoPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dickrunner.service.RouteService;

import java.util.List;
import java.util.logging.*;


@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RoutController {

    private final RouteService routeService;
    private final Logger logger = Logger.getLogger(RoutController.class.getName());

    @GetMapping("/heart")
    public ResponseEntity<GeoRouteResponse> getHeartRoute(@RequestParam double distance) {
        logger.info("Получен запрос на генерацию маршрута в форме сердца с расстоянием: " + distance);
        if (distance <= 0) {
            logger.warning("Неверное значение расстояния: " + distance);
            return ResponseEntity.badRequest().body(new GeoRouteResponse("error: invalid distance", null));
        }

        // Вызов сервиса для генерации маршрута
        List<GeoPoint> route;
        try {
            route = routeService.generateHeartShapeRoute(distance);
        } catch (Exception e) {
            logger.severe("Ошибка при генерации маршрута: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeoRouteResponse("error: could not generate route", null));
        }

        if (route == null || route.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GeoRouteResponse("error: could not generate route", null));
        }
        logger.info("Маршрут успешно сгенерирован для расстояния: " + distance);

        return ResponseEntity.ok(new GeoRouteResponse("success", route));
    }
}

