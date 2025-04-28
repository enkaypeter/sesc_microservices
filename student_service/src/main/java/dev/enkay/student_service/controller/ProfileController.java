package dev.enkay.student_service.controller;

import dev.enkay.student_service.dto.profile.ProfileResponse;
import dev.enkay.student_service.dto.profile.UpdateProfileRequest;
import dev.enkay.student_service.common.ApiResponse;
import dev.enkay.student_service.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile Management", description = "Endpoints for managing student profiles")
public class ProfileController {

  private final ProfileService profileService;

  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @Operation(summary = "Get current student's profile", description = "Fetches the logged-in student's profile information.")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
  @GetMapping("/me")
  public ResponseEntity<ApiResponse<ProfileResponse>> getProfile() {
    var profile = profileService.getProfile();
    var response = ApiResponse.<ProfileResponse>builder()
      .success(true)
      .message("Profile retrieved successfully")
      .data(profile)
      .timestamp(Instant.now())
      .build();

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Update current student's profile", description = "Allows the logged-in student to update their first name and last name.")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully")
  @PutMapping("/me")
  public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile( @Valid @RequestBody UpdateProfileRequest request) {
    var updatedProfile = profileService.updateProfile(request);
    var response = ApiResponse.<ProfileResponse>builder()
      .success(true)
      .message("Profile updated successfully")
      .data(updatedProfile)
      .timestamp(Instant.now())
      .build();

    return ResponseEntity.ok(response);
  }
}
