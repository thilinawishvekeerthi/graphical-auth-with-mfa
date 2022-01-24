package com.graphicauth.authservice.service;

public interface IGraphicAuthService {
    Boolean authenticate(String passPoints,String selectedPoints, Long numberOfPoints, Long tolerance);
}
