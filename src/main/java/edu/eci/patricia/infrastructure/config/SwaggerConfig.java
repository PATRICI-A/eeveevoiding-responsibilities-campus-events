package edu.eci.patricia.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI configuration for the Campus Events Service.
 * Registers the {@link OpenAPI} bean that populates the Swagger UI with service metadata,
 * security schemes, and organised API tags.
 */
@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PATRICI.A Campus Events Service API")
                        .description("""
                                This microservice is responsible for managing the full lifecycle of university campus events \
                                for the PATRICI.A platform at Universidad ECI.

                                Key responsibilities include:
                                - **Event Management**: Complete CRUD operations for university events. Organizers can create, \
                                  update, and cancel events. Events require a future date, minimum duration of 15 minutes, \
                                  and optional capacity limits. Each event generates a unique QR code for attendance tracking.

                                - **Student RSVPs**: Students can confirm or cancel their attendance to active events. \
                                  The system enforces capacity limits, prevents duplicate RSVPs, and automatically validates \
                                  event status (only ACTIVE events accept RSVPs). RSVPs are immutable once confirmed.

                                - **Personal Agenda**: Students can retrieve their confirmed events as a personal agenda, \
                                  showing all upcoming events they plan to attend. Empty agendas return a friendly message.

                                - **Event Feed**: Public browsing of active events with optional filters by category and date. \
                                  Supports ACADEMIC, CULTURAL, SPORTS, and other event categories.

                                All write endpoints require a valid Bearer JWT token issued by the authentication service. \
                                The user ID is extracted from the JWT `sub` claim and validated against event ownership \
                                for modification operations.
                                """)
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .name(BEARER_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        JWT Bearer token authentication for all campus events endpoints.

                                        Required roles:
                                        - `ESTUDIANTE` → Access to RSVP endpoints and agenda retrieval
                                        - `ORGANIZADOR` → Access to event creation, update, and cancellation

                                        The token must be obtained from the authentication service.
                                        Include the token in the Authorization header as:
                                        `Authorization: Bearer <your-jwt-token>`

                                        The user ID is extracted from the JWT `sub` claim for:
                                        - RSVP operations (student identity)
                                        - Event ownership validation (organizer identity)
                                        """)))
                .addTagsItem(new Tag()
                        .name("Events")
                        .description("""
                                Endpoints for managing university events. Allows authenticated organizers to:
                                - **POST /events** — Create new events with validation (future date, minimum duration 15 minutes)
                                - **GET /events** — Retrieve public event feed with optional filters (category, date)
                                - **GET /events/{eventId}** — Get detailed event information by UUID
                                - **PUT /events/{eventId}** — Update existing events (organizer-only)
                                - **PATCH /events/{eventId}** — Cancel events (organizer-only, irreversible)

                                Events require: name, date/time (future), duration (≥15 min), location, category, type.
                                Optional: maximum capacity (default unlimited). Each created event generates a QR code.
                                """))
                .addTagsItem(new Tag()
                        .name("Event RSVPs")
                        .description("""
                                Endpoints for managing student attendance to university events. Allows authenticated students to:
                                - **POST /events/{eventId}/rsvp** — Confirm or cancel attendance (action: CONFIRM or CANCEL)
                                - **GET /events/rsvp/agenda** — Retrieve personal agenda of confirmed events

                                Validation rules:
                                - Event must exist and be in ACTIVE status
                                - Event must not have reached maximum capacity (for CONFIRM)
                                - Students cannot confirm twice to the same event
                                - Students can only cancel their own RSVPs

                                Returns HTTP 201 on successful RSVP operations (both CONFIRM and CANCEL).
                                """));
    }
}