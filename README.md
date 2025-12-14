# Projekat Napredni Veb

This repository contains a full-stack food ordering platform consisting of a Spring Boot REST API and an Angular frontend. The backend exposes order management endpoints, handles authentication/authorization, and integrates WebSocket push updates so clients can track their orders in real time. The frontend (in `projekatfront/`) consumes these services to provide the user experience.

## Technologies
- **Backend:** Spring Boot 3.4 with Spring Web, Spring Data JPA, Spring Security, Lombok, and MySQL for persistence. WebSocket support is provided through Spring's STOMP messaging.
- **Frontend:** Angular 16 with the Angular CLI scaffold stored under `projekatfront/`.
- **Messaging & Scheduling:** The backend uses Spring's WebSocket message broker and scheduled tasks for automated order handling.

## Web API Overview
The REST API (located in `ProjekatBack/`) centers around order workflows:
- Place new orders, cancel in-progress ones, or schedule them for later via `/orders` endpoints.
- Search existing orders with filtering and pagination, respecting user roles and permissions.
- Order items reference menu dishes stored in the database.

### Key Backend Endpoints
- `POST /auth/login` — authenticate with email/password and receive a JWT plus granted permissions.
- `POST /orders/search` — filter orders with pagination (returns only the caller's orders unless an admin).
- `POST /orders` — create a new order immediately if capacity allows (429 when the kitchen is saturated).
- `PUT /orders` — cancel an existing order when permitted.
- `POST /orders/schedule` — schedule an order to start later; promoted by the scheduler when due.
- `GET /dishes` — list all dishes; requires `can_search_order` permission.
- `GET /dishes/{name}` — fetch a dish by name.
- `GET /users` — list users (requires `can_read_users`).
- `GET /users/{email}` — fetch a user by email.
- `POST /users` — create a user (requires `can_create_users`).
- `PUT /users` — update a user (requires `can_update_users`).
- `PUT /users/delete/{email}` — soft-delete a user (requires `can_delete_users`).
- `GET /errors` — paginated error log; admins see all, non-admins see their own records.

## Live Order Status with WebSockets
Real-time order status changes are delivered over a STOMP WebSocket endpoint:
- The broker is exposed at `/ws` with SockJS fallback, using the `/app` application prefix and `/topic` destinations.
- When an order status changes (e.g., ORDERED → PREPARING → IN_DELIVERY → DELIVERED), the backend sends a message to `/topic/orders` containing the order ID and new state. Clients subscribed to that topic receive updates instantly and can refresh their UI without polling.

## Scheduled Order Processing
The application supports scheduled orders that should start preparation at a future time. A scheduled task runs every minute to promote due orders:
- It finds orders marked `SCHEDULED` whose `orderedAt` timestamp has passed and are still active.
- Each qualifying order is converted into a normal order and pushed through the standard status progression, including WebSocket notifications.
- If capacity rules prevent starting the order (e.g., too many orders already in progress), the job cancels the scheduled order and logs an error for auditability.

## Repository Structure
- `ProjekatBack/` — Spring Boot source, including controllers, services, repositories, and configuration.
- `projekatfront/` — Angular client application.
