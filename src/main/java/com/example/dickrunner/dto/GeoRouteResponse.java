package com.example.dickrunner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.example.dickrunner.model.GeoPoint;

import java.util.List;
@Data
@AllArgsConstructor
public class GeoRouteResponse {
    private String status;
    private List<GeoPoint> route;
}
