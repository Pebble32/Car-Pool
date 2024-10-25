package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing users.
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return ResponseEntity containing a {@link PageResponse} of {@link UserResponse}.
     */
    @GetMapping("/all/paginated")
    public ResponseEntity<PageResponse<UserResponse>> getUsersPaginated(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        PageResponse<UserResponse> response = userService.findAllUsersPaginated(page, size);
        return ResponseEntity.ok(response);
    }
}