Fly Away Travel necesita héroes, ¡y ese eres TÚ y tu equipo!
Tu quest: Construir un API épico para reservar vuelos.
Objetivos:
● MUST HAVE → Misiones principales (obligatorias para completar el nivel)
● NICE TO HAVE → Misiones secundarias (desbloquean recompensas extra)
Recompensa: Hasta 1 pt adicional para su PC1
¿Aceptas el desafío?
Nota: Para más detalles leer el README del repositorio en github
NUEVA MISIÓN
DESBLOQUEADA
Endpoint: POST /flights/create (sin protección)
Constraints:
- java -jar week07-tester.jar test -u http://localhost:8081 -n
- Todos los campos son requeridos
- Número de vuelo: A-Z, 0-9, máximo 6 caracteres (ej: AA984)
- Hora salida < Hora llegada
- Asientos disponibles > 0
- Números de vuelo únicos
Recompensa:
+0.1 puntos (must have)
Crear Vuelo
Endpoint: POST /users/register (sin protección)
Constraints:
- Email válido
- Nombre y apellido: mínimo 1 letra mayúscula (A-Z)
- Contraseña: mínimo 8 caracteres, al menos 1 letra y 1 número
- La respuesta solo incluye el id del usuario (Usa Dtos) (Nice to Have)
Recompensa:
+0.1 puntos (must have)
+0.1 puntos (nice to have)
Registro de Usuarios
Endpoint: POST /auth/login (sin protección)
Constraints:
- Email y contraseña obligatorios
- Validar email desconocido
- Validar contraseña incorrecta
- Retorna token JWT para operaciones protegidas
- El token se ve de la siguiente manera { token: "<jwt>" }
Recompensa:
+0.2 puntos (must have)
Autenticación
Endpoint: GET /flights/search (protegido)
Constraints:
- Búsqueda por número de vuelo parcial
- Búsqueda por nombre de aerolínea parcial
- Búsqueda por rango de fechas de salida (Nice To Have)
Recompensa:
+0.1 puntos (must have)
+0.1 puntos (nice to have)
Búsqueda de Vuelos
Endpoint: POST /flights/book (protegido)
Constraints:
- Input: Flight ID
- Auto-calcular: Customer ID, nombres, fecha de reserva
- No sobrevender vuelos
- Endpoint GET para ver reserva: GET /flight/book/{id}
- No reservar vuelos pasados o en tránsito (+0.2 Nice To Have)
- Evitar reservas con conflicto de horario (+0.2 Nice To Have)
Recompensa:
+0.1 puntos (must have)
+0.1 puntos (nice to have)
Reservar Vuelo
Funcionalidad (Nice to have):
- Guardar archivo flight_booking_email_${booking_id}.txt
- Incluir: nombres, número de vuelo, fechas (ISO8601)
Recompensa:
+0.1 puntos (nice to have)
Email de Confirmación
Arquitectura: API REST
Autenticación: JWT
Endpoints GET: Requeridos para /{entity}/{id}
Deben tener un endpoint DELETE /cleanup
El cual se encarga de limpiar completamente la BD, es importante que tengan este endpoint
para que los test funcionen.