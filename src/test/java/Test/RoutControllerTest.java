package Test;


import com.example.dickrunner.DickRunnerApplication;
import com.example.dickrunner.model.GeoPoint;
import com.example.dickrunner.service.RouteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = DickRunnerApplication.class)
@AutoConfigureMockMvc
public class RoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteService routeService;

    @Test
    public void testGetHeartRouteSuccess() throws Exception {
        // Мокируем возвращаемый маршрут
        List<GeoPoint> mockRoute = List.of(new GeoPoint(50.0, 20.0), new GeoPoint(51.0, 21.0));
        Mockito.when(routeService.generateHeartShapeRoute(5.0)).thenReturn(mockRoute);

        mockMvc.perform(get("/routes/heart")
                        .param("distance", "5.0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.route").isArray());
    }

    @Test
    public void testGetHeartRouteInvalidDistance() throws Exception {
        mockMvc.perform(get("/routes/heart")
                        .param("distance", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error: invalid distance"));
    }

    @Test
    public void testGetHeartRouteEmptyRoute() throws Exception {

        Mockito.when(routeService.generateHeartShapeRoute(5.0)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/routes/heart")
                        .param("distance", "5.0"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("error: could not generate route"));
    }
}

