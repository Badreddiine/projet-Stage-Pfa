package com.badr.equipement_service.dto;

import java.util.List;


public class UserInfo {
    private String userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String department;
    private String position;
    private Boolean isActive;
    private String preferredLanguage;
    private List<String> roles;
    private List<String> permissions;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public UserInfo(String userId, String username, String email, String firstName, String lastName, String fullName, String department, String position, Boolean isActive, String preferredLanguage, List<String> roles, List<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.department = department;
        this.position = position;
        this.isActive = isActive;
        this.preferredLanguage = preferredLanguage;
        this.roles = roles;
        this.permissions = permissions;
    }

    public UserInfo() {
    }

    public boolean hasRole(String roleName) {
        return roles != null && roles.contains(roleName);
    }

    public boolean hasPermission(String permissionName) {
        return permissions != null && permissions.contains(permissionName);
    }

    public boolean hasAnyRole(List<String> roleNames) {
        if (roles == null || roleNames == null) return false;
        return roleNames.stream().anyMatch(roles::contains);
    }

    public String getFullName() {
        if (fullName != null && !fullName.isEmpty()) {
            return fullName;
        }
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }
}