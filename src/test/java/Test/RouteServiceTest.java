package Test;

import com.example.dickrunner.model.GeoPoint;
import com.example.dickrunner.service.RouteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    public RouteServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateHeartShapeRoute() {
        double distance = 5.0;
        double userLatitude = 20.;
        double userLongitude = 50.;
        List<GeoPoint> route = routeService.generateHeartShapeRoute(distance, userLatitude, userLongitude);

        assertNotNull(route, "Маршрут не должен быть null");
        assertFalse(route.isEmpty(), "Маршрут не должен быть пустым");
        assertTrue(route.size() > 10, "Количество точек должно быть больше 10 для корректного маршрута");
    }

    @Test
    public void testGenerateHeartShapeRouteInvalidDistance() {
        double distance = 0.0;
        double userLatitude = 20.;
        double userLongitude = 50.;
        List<GeoPoint> route = routeService.generateHeartShapeRoute(distance, userLatitude, userLongitude);

        assertTrue(route.isEmpty());
    }
}
