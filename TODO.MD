# Rename logic like IOS code
# Improve go back UX in reset password flow

# Handle auto-login failure after password reset — AuthViewModel.completePasswordReset() ignores the Result.failure (fire-and-forget), so if the silent login fails the user is stuck on the success screen with no feedback (worse: the 5s auto-redirect fires it silently too). iOS surfaces a toast via signInAfterReset; since the Android SuccessScreen is generic/stateless, fallback to routing the user to Login (password is already changed) instead.
# Match iOS letter-spacing on the success StatusCard — iOS applies tracking(0.5) to the "ACCOUNT STATUS" title; Android StatusCard has no letterSpacing.