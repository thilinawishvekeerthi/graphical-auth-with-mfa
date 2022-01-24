package com.graphicauth.authservice.service;

import org.springframework.stereotype.Service;

@Service
public class GraphicAuthService implements IGraphicAuthService{
    @Override
    public Boolean authenticate(String passPoints, String selectedPoints,Long numberOfPoints, Long tolerance) {
        Boolean authenticate = false;
        String[] passPointCoordinates = passPoints.split("\\|");
        String[] selectedPassPointCoordinates = selectedPoints.split("\\|");
        if(passPointCoordinates.length == selectedPassPointCoordinates.length){
            for (int index = 0; index < passPointCoordinates.length; index++) {
                String[] x_y_pass_point = passPointCoordinates[index].split(",");
                String[] x_y_selected_point = selectedPassPointCoordinates[index].split(",");
                double x_difference = Double.parseDouble(x_y_pass_point[0]) - Double.parseDouble(x_y_selected_point[0]);
                double xSquare = Math.pow(x_difference,2);
                double y_difference = Double.parseDouble(x_y_pass_point[1]) - Double.parseDouble(x_y_selected_point[1]);
                double ySquare = Math.pow(y_difference,2);
                double radiusSquare = Math.pow(Double.parseDouble(tolerance.toString()), 2);
                double result = xSquare + ySquare;
                if(result <= radiusSquare){
                    authenticate= true;
                }
                else{
                    authenticate = false;
                    break;
                }

            }
        }
        return authenticate;
    }
}
