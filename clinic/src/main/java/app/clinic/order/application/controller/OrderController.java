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
import app.clinic.user.domain.model.Role;

@RestController
@RequestMapping("/orders")
public class OrderController {

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
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto dto) {
        Role role = OrderSecurityValidator.getCurrentRole();
        OrderSecurityValidator.requireDoctorRole(role);

        if (!OrderDataValidator.isValidOrderRequest(dto)) {
            return ResponseUtils.badRequest();
        }

        MedicalOrder order = OrderMapper.toDomain(dto);
        MedicalOrder created = createOrderUseCase.execute(order.getPatientId(), order.getDoctorId());
        return ResponseUtils.ok(OrderMapper.toResponse(created));
    }

    // ✅ Consultar una orden médica por número
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String orderNumber) {
        Role role = OrderSecurityValidator.getCurrentRole();
        OrderSecurityValidator.validateNotHumanResources(role);

        if (!OrderDataValidator.isValidOrderNumber(orderNumber)) {
            return ResponseUtils.badRequest();
        }

        return getOrderByIdUseCase.execute(orderNumber)
            .map(order -> ResponseUtils.ok(OrderMapper.toResponse(order)))
            .orElse(ResponseUtils.notFound());
    }

    // ✅ Listar todas las órdenes médicas (solo SOPORTE o MÉDICO)
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        Role role = OrderSecurityValidator.getCurrentRole();
        OrderSecurityValidator.requireReadPermissions(role);

        List<MedicalOrder> orders = getAllOrdersUseCase.execute();
        return ResponseUtils.ok(
            orders.stream().map(OrderMapper::toResponse).collect(Collectors.toList())
        );
    }

    // ✅ Actualizar una orden (solo MÉDICO)
    @PutMapping("/{orderNumber}")
    public ResponseEntity<OrderResponseDto> updateOrder(
         @PathVariable String orderNumber,
         @RequestBody OrderRequestDto dto
     ) {
        Role role = OrderSecurityValidator.getCurrentRole();
        OrderSecurityValidator.requireDoctorRole(role);

        return getOrderByIdUseCase.execute(orderNumber)
            .map(existing -> {
                MedicalOrder updated = OrderMapper.toDomainForUpdate(dto, orderNumber);
                MedicalOrder saved = updateOrderUseCase.execute(updated);
                return ResponseUtils.ok(OrderMapper.toResponse(saved));
            })
            .orElse(ResponseUtils.notFound());
    }

    // ✅ Eliminar una orden (solo SOPORTE o MÉDICO)
    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderNumber) {
        Role role = OrderSecurityValidator.getCurrentRole();
        OrderSecurityValidator.requireDeletePermissions(role);

        if (!OrderDataValidator.isValidOrderNumber(orderNumber)) {
            return ResponseUtils.badRequest();
        }

        deleteOrderUseCase.execute(orderNumber);
        return ResponseUtils.noContent();
    }

    // ✅ Agregar un ítem a la orden (medicamento, procedimiento o ayuda diagnóstica)
    @PostMapping("/{orderNumber}/items")
    public ResponseEntity<OrderResponseDto> addItemToOrder(
          @PathVariable String orderNumber,
          @RequestBody OrderItemDto itemDto
      ) {
          Role role = OrderSecurityValidator.getCurrentRole();
         OrderSecurityValidator.requireItemModificationPermissions(role);

         if (!OrderDataValidator.isValidOrderNumber(orderNumber) || !OrderDataValidator.isValidItemRequest(itemDto)) {
             return ResponseUtils.badRequest();
         }

         MedicalOrder updated = addItemToOrderUseCase.execute(orderNumber, itemDto);
         return ResponseUtils.ok(OrderMapper.toResponse(updated));
     }

    // ✅ Eliminar un ítem de una orden
    @DeleteMapping("/{orderNumber}/items/{itemNumber}")
    public ResponseEntity<OrderResponseDto> removeItemFromOrder(
         @PathVariable String orderNumber,
         @PathVariable int itemNumber
     ) {
         Role role = OrderSecurityValidator.getCurrentRole();
        OrderSecurityValidator.requireItemModificationPermissions(role);

        if (!OrderDataValidator.isValidOrderNumber(orderNumber) || !OrderDataValidator.isValidItemNumber(itemNumber)) {
            return ResponseUtils.badRequest();
        }

        MedicalOrder updated = removeItemFromOrderUseCase.execute(orderNumber, itemNumber);
        return ResponseUtils.ok(OrderMapper.toResponse(updated));
    }
}
