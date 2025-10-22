package app.clinic.order.application.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.order.application.dto.OrderItemDto;
import app.clinic.order.application.dto.OrderRequestDto;
import app.clinic.order.application.dto.OrderResponseDto;
import app.clinic.order.application.mapper.OrderMapper;
import app.clinic.order.application.usecase.AddItemToOrderUseCase;
import app.clinic.order.application.usecase.CreateOrderUseCase;
import app.clinic.order.application.usecase.DeleteOrderUseCase;
import app.clinic.order.application.usecase.GetAllOrdersUseCase;
import app.clinic.order.application.usecase.GetOrderByIdUseCase;
import app.clinic.order.application.usecase.RemoveItemFromOrderUseCase;
import app.clinic.order.application.usecase.UpdateOrderUseCase;
import app.clinic.order.domain.model.MedicalOrder;
import app.clinic.order.domain.model.OrderItem;
import app.clinic.shared.domain.exception.ValidationException;
import app.clinic.shared.infrastructure.config.SecurityUtils;
import app.clinic.user.domain.model.Role;

@RestController
@RequestMapping("/orders")
public class OrderController {

    /**
     * Valida y parsea el rol desde el token JWT.
     */
    private Role validateAndParseRole() {
         String roleStr = SecurityUtils.getCurrentRole();
         if (roleStr == null) {
             throw new ValidationException("Rol no encontrado en el token.");
         }
         return Role.valueOf(roleStr.toUpperCase());
     }

    /**
     * Valida permisos para operación de creación (solo MÉDICO).
     */
    private void validateCreatePermissions(Role role) {
        if (role != Role.MEDICO) {
            throw new SecurityException("Solo médicos pueden crear órdenes");
        }
    }

    /**
     * Valida permisos para operación de lectura (MÉDICO y SOPORTE).
     */
    private void validateReadPermissions(Role role) {
        if (role != Role.MEDICO && role != Role.SOPORTE) {
            throw new SecurityException("Solo médicos y soporte pueden consultar órdenes");
        }
    }

    /**
     * Valida permisos para operación de eliminación (MÉDICO y SOPORTE).
     */
    private void validateDeletePermissions(Role role) {
        if (role != Role.MEDICO && role != Role.SOPORTE) {
            throw new SecurityException("Solo médicos y soporte pueden eliminar órdenes");
        }
    }

    /**
     * Valida permisos para operación de modificación de ítems (MÉDICO y ENFERMERA).
     */
    private void validateItemModificationPermissions(Role role) {
        if (role != Role.MEDICO && role != Role.ENFERMERA) {
            throw new SecurityException("Solo médicos y enfermeras pueden modificar ítems");
        }
    }

    /**
     * Valida que los datos de la solicitud de orden sean válidos.
     */
    private boolean isValidOrderRequest(OrderRequestDto dto) {
        return dto != null &&
               dto.patientId() != null &&
               dto.doctorId() != null &&
               dto.items() != null &&
               !dto.items().isEmpty();
    }

    /**
     * Valida que el número de orden sea válido.
     */
    private boolean isValidOrderNumber(String orderNumber) {
        return orderNumber != null && !orderNumber.trim().isEmpty();
    }

    /**
     * Valida que los datos del ítem sean válidos.
     */
    private boolean isValidItemRequest(OrderItemDto itemDto) {
        return itemDto != null &&
               itemDto.isValid() &&
               itemDto.hasValidType();
    }

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final RemoveItemFromOrderUseCase removeItemFromOrderUseCase;

    public OrderController(
        CreateOrderUseCase createOrderUseCase,
        GetOrderByIdUseCase getOrderByIdUseCase,
        GetAllOrdersUseCase getAllOrdersUseCase,
        UpdateOrderUseCase updateOrderUseCase,
        DeleteOrderUseCase deleteOrderUseCase,
        AddItemToOrderUseCase addItemToOrderUseCase,
        RemoveItemFromOrderUseCase removeItemFromOrderUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.removeItemFromOrderUseCase = removeItemFromOrderUseCase;
    }

    // ✅ Crear una nueva orden médica (solo MÉDICO)
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
         @RequestBody OrderRequestDto dto
     ) {
         try {
             Role role = validateAndParseRole();
            validateCreatePermissions(role);

            if (!isValidOrderRequest(dto)) {
                return ResponseEntity.badRequest().build();
            }

            MedicalOrder order = OrderMapper.toDomain(dto);
            MedicalOrder created = createOrderUseCase.execute(order.getPatientId(), order.getDoctorId());
            return ResponseEntity.ok(OrderMapper.toResponse(created));

        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ Consultar una orden médica por número
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrderById(
         @PathVariable String orderNumber
     ) {
         try {
             Role role = validateAndParseRole();

            // Validar permisos (RRHH no puede consultar órdenes específicas)
            if (role == Role.RECURSOS_HUMANOS) {
                return ResponseEntity.status(403).build();
            }

            if (!isValidOrderNumber(orderNumber)) {
                return ResponseEntity.badRequest().build();
            }

            return getOrderByIdUseCase.execute(orderNumber)
                .map(order -> ResponseEntity.ok(OrderMapper.toResponse(order)))
                .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ Listar todas las órdenes médicas (solo SOPORTE o MÉDICO)
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
         try {
             Role role = validateAndParseRole();
            validateReadPermissions(role);

            List<MedicalOrder> orders = getAllOrdersUseCase.execute();
            return ResponseEntity.ok(
                orders.stream().map(OrderMapper::toResponse).collect(Collectors.toList())
            );

        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ Actualizar una orden (solo MÉDICO)
    @PutMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> updateOrder(
         @PathVariable String orderNumber,
         @RequestBody OrderRequestDto dto
     ) {
         Role role = validateAndParseRole();
        if (role != Role.MEDICO) {
            return ResponseEntity.status(403).build();
        }

        return getOrderByIdUseCase.execute(orderNumber)
            .map(existing -> {
                MedicalOrder updated = OrderMapper.toDomainForUpdate(dto, orderNumber);
                MedicalOrder saved = updateOrderUseCase.execute(updated);
                return ResponseEntity.ok(OrderMapper.toResponse(saved));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar una orden (solo SOPORTE o MÉDICO)
    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrder(
         @PathVariable String orderNumber
     ) {
         try {
             Role role = validateAndParseRole();
            validateDeletePermissions(role);

            if (!isValidOrderNumber(orderNumber)) {
                return ResponseEntity.badRequest().build();
            }

            deleteOrderUseCase.execute(orderNumber);
            return ResponseEntity.noContent().build();

        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ Agregar un ítem a la orden (medicamento, procedimiento o ayuda diagnóstica)
    @PostMapping("/{orderNumber}/items")
    public ResponseEntity<OrderResponseDto> addItemToOrder(
         @PathVariable String orderNumber,
         @RequestBody OrderItemDto itemDto
     ) {
         try {
             Role role = validateAndParseRole();
            validateItemModificationPermissions(role);

            if (!isValidOrderNumber(orderNumber) || !isValidItemRequest(itemDto)) {
                return ResponseEntity.badRequest().build();
            }

            OrderItem newItem = OrderMapper.toDomain(itemDto);
            MedicalOrder updated = addItemToOrderUseCase.execute(orderNumber, newItem);
            return ResponseEntity.ok(OrderMapper.toResponse(updated));

        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ Eliminar un ítem de una orden
    @DeleteMapping("/{orderNumber}/items/{itemNumber}")
    public ResponseEntity<OrderResponseDto> removeItemFromOrder(
         @PathVariable String orderNumber,
         @PathVariable int itemNumber
     ) {
         try {
             Role role = validateAndParseRole();
            validateItemModificationPermissions(role);

            if (!isValidOrderNumber(orderNumber) || itemNumber <= 0) {
                return ResponseEntity.badRequest().build();
            }

            MedicalOrder updated = removeItemFromOrderUseCase.execute(orderNumber, itemNumber);
            return ResponseEntity.ok(OrderMapper.toResponse(updated));

        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
