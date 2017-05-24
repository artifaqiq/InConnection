/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.security.service;

/**
 * @author Lomako
 * @version 1.0
 */
public interface SecurityService {
    void authenticate(String email, String password);
}
