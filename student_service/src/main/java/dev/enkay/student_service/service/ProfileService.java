package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.profile.ProfileResponse;
import dev.enkay.student_service.dto.profile.UpdateProfileRequest;

public interface ProfileService {
  ProfileResponse getProfile();
  ProfileResponse updateProfile(UpdateProfileRequest request);
}
