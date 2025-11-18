package com.sdsgpi.configservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationService {
    private List<ConfigurationSysteme> configurations;
    private List<ServiceRegistry> services;
}


